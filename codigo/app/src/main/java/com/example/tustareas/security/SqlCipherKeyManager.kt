package com.example.tustareas.security

import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Clase que gestiona la encriptacion de la contraseña y el manager
 */
class SqlCipherKeyManager constructor(
    private val sharedPreferences: SharedPreferences
) {
    // Gestiona el patron singleton para evitar multiples instancias del gestor de la contraeña de cifrado de la bd
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    // Inicializa la clase
    init {
        initialize()
    }

    // Comprueba si shared preferences contiene la clave de encriptación
    private fun initialize() {
        generateKeystoreKeyIfNeeded()
        // Comprueba si no existe la clave de encriptación para generarla
        if (!sharedPreferences.contains("encrypted_key")) {
            generateAndEncryptSqlCipherKey()
        }
    }

    // Si la clave todavía no existe genera una nueva
    private fun generateKeystoreKeyIfNeeded() {
        // Busca si el key store del sistema android ya tiene la key store
        // En caso negativo crea una clave simetrica en el key store
        if (!keyStore.containsAlias("sqlcipher_keystore_key")) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            // Configura la clave con los siguiente parametros encriptar y desencriptar, prohibe/rechaza
            // su uso en otras casuisticas, no aplica un patron de encriptado y la construye
            val keyGenSpec = KeyGenParameterSpec.Builder(
                "sqlcipher_keystore_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            // Configura el generador de claves con la configuración previa
            keyGenerator.init(keyGenSpec)
            // Genera la clave
            keyGenerator.generateKey()
        }
    }

    // Genera y  cifra la clave de encriptación
    private fun generateAndEncryptSqlCipherKey() {
        // Obtiene la clave del keyStore
        val secretKey = getSecretKey("sqlcipher_keystore_key")
        // Instanclia al cifrador de la base de datos con los parametros usados para configurar la clave
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        // Inicializa el cifrador
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Genera una clave randon de 32 bits
        val sqlCipherKey = ByteArray(32)
        SecureRandom().nextBytes(sqlCipherKey)

        // Concatena nuestro la clave encriptada con la nueva cadena para evitar que sea predicible
        val encryptedKey = cipher.doFinal(sqlCipherKey)
        // Obtenemos del cipher el vector de inicialización
        val iv = cipher.iv

        // Almacenamos el vector y la clave en bd
        sharedPreferences.edit {
            putString("encrypted_key", Base64.encodeToString(encryptedKey, Base64.NO_WRAP))
            putString("encryption_iv", Base64.encodeToString(iv, Base64.NO_WRAP))
        }

        // Borramos de memoria la clave
        // Zero out the key in memory
        sqlCipherKey.fill(0)
    }

    // Descifra la clave de encriptación
    private fun getDecryptedSqlCipherKey(keyAlias: String, key: String, iv: String): ByteArray {
        // Obtiene las claves previamente cifradas
        val encryptedKey = Base64.decode(key, Base64.NO_WRAP)
        val ivBytes = Base64.decode(iv, Base64.NO_WRAP)

        // Obtiene la clave del key store
        val secretKey = getSecretKey(keyAlias)

        // Instancia a cypher para descifrar en lugar de cifrar
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, ivBytes))

        // Devuelve el valor sin cifrar
        return cipher.doFinal(encryptedKey)
    }

    // Obtiene la clave del key store a partir del alias
    private fun getSecretKey(keyAlias: String): SecretKey =
        (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

    // Devuelve una serie de valores necesarios para la bd
    fun getSupportFactory(): SupportOpenHelperFactory {
        val encryptedKey = sharedPreferences.getString("encrypted_key", null).orEmpty()
        val iv = sharedPreferences.getString("encryption_iv", null).orEmpty()
        val decryptedKey = getDecryptedSqlCipherKey("sqlcipher_keystore_key", encryptedKey, iv)
        return DisposableKeySupportFactory(decryptedKey)
    }
}
package com.example.tustareas.security

import androidx.sqlite.db.SupportSQLiteOpenHelper
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

/**
 * Clase que elimina la clave de encriptación de la memoria
 */
class DisposableKeySupportFactory(private val decryptedKey: ByteArray) :
    SupportOpenHelperFactory(decryptedKey) {

    // Elimina la clave de encriptación de la memoria
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val helper = super.create(configuration)
        decryptedKey.fill(0)
        return helper
    }
}
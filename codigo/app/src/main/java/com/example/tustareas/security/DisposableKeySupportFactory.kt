package com.example.tustareas.security

import androidx.sqlite.db.SupportSQLiteOpenHelper
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

/**
 * Clase que elimina la clave de encriptación de la memoria
 *
 * @param decryptedKey La clave de encriptación desencriptada que se usará para crear el SupportSQLiteOpenHelper
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 * @author proandroiddev.com
 */
class DisposableKeySupportFactory(
    private val decryptedKey: ByteArray,
) : SupportOpenHelperFactory(decryptedKey) {
    /**
     * Crea un nuevo SupportSQLiteOpenHelper y borra la clave de encriptación de la memoria
     *
     * @param configuration La configuración para crear el SupportSQLiteOpenHelper
     * @return El SupportSQLiteOpenHelper creado
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     * @author proandroiddev.com
     */
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val helper = super.create(configuration)
        decryptedKey.fill(0)
        return helper
    }
}

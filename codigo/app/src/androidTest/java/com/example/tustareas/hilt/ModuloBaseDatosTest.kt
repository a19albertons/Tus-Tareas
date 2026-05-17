package com.example.tustareas.hilt

import android.content.Context
import androidx.room.Room
import com.example.tustareas.db.TusTareasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ModuloBaseDatos::class],
)
object ModuloBaseDatosTest {
    @Provides
    @Singleton
    fun proveerBaseDatosEnMemoria(
        @ApplicationContext context: Context,
    ): TusTareasDatabase =
        Room
            .inMemoryDatabaseBuilder(context, TusTareasDatabase::class.java)
            .allowMainThreadQueries() // Permite ejecutar consultas en el hilo principal
            .build()
}

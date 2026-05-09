package com.example.tustareas.hilt

import android.content.Context
import com.example.tustareas.db.TusTareasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuloBaseDatos {

    @Provides
    @Singleton
    fun proveerBaseDatos(@ApplicationContext context: Context): TusTareasDatabase {
        return TusTareasDatabase.getDatabase(context)
    }
}
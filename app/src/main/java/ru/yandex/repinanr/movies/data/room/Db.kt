package ru.yandex.repinanr.movies.data.room

import android.content.Context
import androidx.room.Room

object Db {

    private var instance: AppDb? = null

    fun getInstance(context: Context): AppDb? {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context,
                AppDb::class.java,
                "db-name.db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build()
        }
        return instance
    }

    fun delete() {
        instance?.close()
        instance = null
    }
}
package ru.yandex.repinanr.movies.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `Movies` (" +
                "`service_id` INTEGER PRIMARY KEY NOT NULL," +
                "`description` TEXT NOT NULL," +
                "`name` TEXT NOT NULL," +
                "`image_url` TEXT NOT NULL)")
    }
}
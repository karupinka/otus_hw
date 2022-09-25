package ru.yandex.repinanr.movies.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `movies` (" +
                "`service_id` INTEGER PRIMARY KEY NOT NULL," +
                "`description` TEXT NOT NULL," +
                "`name` TEXT NOT NULL," +
                "`image_url` TEXT NOT NULL)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `movies` ADD COLUMN `is_favorite` INTEGER")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE remote_keys")
    }
}
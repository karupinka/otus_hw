package ru.yandex.repinanr.movies.data.room

import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class DbCallback: RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        Log.d("DbCallback", "create db")

    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
    }
}
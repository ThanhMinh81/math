package com.example.matheasyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.matheasyapp.model.History
import com.example.matheasyapp.view.tool.model.LoanHistory

// Adding annotation for our database entities and db version.
@Database(entities = [History::class, LoanHistory::class], version = 12)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): Dao // DAO for History
    abstract fun loanHistoryDao(): LoanHistoryDao // DAO for LoanHistory

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

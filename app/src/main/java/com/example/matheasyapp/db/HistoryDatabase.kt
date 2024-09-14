package com.example.matheasyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.matheasyapp.model.History
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.model.SearchPDF
import com.example.matheasyapp.view.tool.model.LoanHistory

// Adding annotation for our database entities and db version.
@Database(
    entities = [History::class, LoanHistory::class, CameraHistory::class, PdfFile::class, SearchPDF::class , ImageChunk::class],
    version = 12
)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): Dao // DAO for History
    abstract fun loanHistoryDao(): LoanHistoryDao // DAO for LoanHistory
    abstract fun cameraHistoryDao(): CameraHistoryDao
    abstract fun pdfStart(): PDFDao
    abstract fun searchPDF(): StringDao
    abstract fun  chunkImageChunkWithCamera() : ImageChunkWithCameraHistoryDao
    abstract fun  imageChunk() : ImageChunkDao

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

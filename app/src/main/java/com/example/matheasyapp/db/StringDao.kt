package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.model.SearchPDF

@Dao
interface StringDao {

    @Insert
    fun insert(model: SearchPDF): Long

    @Delete
    fun delete(model: SearchPDF): Int

    @Query("DELETE FROM searchpdf_table")
    fun deleteAllKeyWord()

    @get:Query("SELECT * FROM searchpdf_table")
    val getListKeyWord: List<SearchPDF>
}

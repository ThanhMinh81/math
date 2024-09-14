package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.matheasyapp.model.History
import com.example.matheasyapp.model.PdfFile

@Dao
interface PDFDao {

    @Insert
    fun insert(model: PdfFile): Long

    @Delete
    fun delete(model: PdfFile) : Int

    @Update
    fun  update(pdf : PdfFile)


    @Query("DELETE FROM pdf_start")
    fun deleteAllStart()

    @get:Query("SELECT * FROM pdf_start")
    val getAllStart: List<PdfFile>

    // Thêm hàm truy vấn để trả về những phần tử có piority là true
    @Query("SELECT * FROM pdf_start WHERE piority = 1")
    fun getAllWithPiority(): List<PdfFile>


}
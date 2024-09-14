package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface ImageChunkWithCameraHistoryDao {

    @Transaction
    @Query("SELECT * FROM camera_history WHERE id = :historyId")
    fun getImageChunksWithCameraHistoryById(historyId: Long): ImageChunkWithCameraHistory

    // Hoặc nếu bạn muốn lấy tất cả
    @Transaction
    @Query("SELECT * FROM camera_history")
    fun getAllImageChunksWithCameraHistory(): List<ImageChunkWithCameraHistory>
}


package com.example.matheasyapp.db

import android.media.Image
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.matheasyapp.model.History


@Dao
interface ImageChunkDao {
    // Thêm một ImageChunk vào cơ sở dữ liệu
    @Insert
    suspend fun insert(imageChunk: ImageChunk): Long

    @Insert
    suspend fun insertList(imageChunk: List<ImageChunk>)

    // Xóa một ImageChunk cụ thể
    @Delete
    suspend fun delete(imageChunk: ImageChunk)

    // Xóa tất cả ImageChunks
    @Query("DELETE FROM image_chunk")
    suspend fun deleteAll()

    // Lấy tất cả ImageChunks
    @Query("SELECT * FROM image_chunk")
    suspend fun getAllImageChunks(): List<ImageChunk>

    // Lấy tất cả ImageChunks liên quan đến một CameraHistory cụ thể
    @Query("SELECT * FROM image_chunk WHERE historyId = :historyId")
    suspend fun getImageChunksForHistory(historyId: Long): List<ImageChunk>
}
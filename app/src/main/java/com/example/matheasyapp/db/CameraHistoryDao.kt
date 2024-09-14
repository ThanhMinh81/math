package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.matheasyapp.view.tool.model.LoanHistory

@Dao
interface CameraHistoryDao {

    @Insert
    fun insert(cameraHistory: CameraHistory): Long

    @Delete
    fun delete(cameraHistory: CameraHistory)

    @Query("DELETE FROM camera_history")
    fun deleteAllHistory()

    @Query("SELECT * FROM camera_history")
    fun getAllLoanHistory(): List<CameraHistory>

    @Delete
    fun deleteList(cameraHistoryList: List<CameraHistory>)

}
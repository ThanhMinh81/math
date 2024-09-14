package com.example.matheasyapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.matheasyapp.model.History


@Dao
interface Dao {
    // below method is use to
    // add data to database.
    @Insert
    fun insert(model: History): Long

    @Delete
    fun delete(model: History)

    @Query("DELETE FROM history_table")
    fun deleteAllCourses()

    @get:Query("SELECT * FROM history_table")
    val allHistory: List<History>

    @Delete
    fun deleteHistories(histories: List<History>)

}
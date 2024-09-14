package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.matheasyapp.view.tool.model.LoanHistory

@Dao
interface LoanHistoryDao {
    @Insert
    fun insert(loanHistory: LoanHistory) : Long

    @Delete
    fun delete(loanHistory: LoanHistory)
    @Query("DELETE FROM loan_result")
    fun deleteAllHistory()

    @Query("SELECT * FROM loan_result")
      fun getAllLoanHistory(): List<LoanHistory>

}
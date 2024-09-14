package com.example.matheasyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchpdf_table")
class SearchPDF(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String
) {}
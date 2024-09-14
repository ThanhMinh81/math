package com.example.matheasyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Relation
import androidx.lifecycle.LiveData
import androidx.room.Embedded

class ImageChunkWithCameraHistory(
    @Embedded val cameraHistory: CameraHistory,
    @Relation(
        parentColumn = "id",
        entityColumn = "historyId"
    )
    val imageChunks: List<ImageChunk>
)

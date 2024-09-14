package com.example.matheasyapp.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "image_chunk",
    foreignKeys = [
        ForeignKey(
            entity = CameraHistory::class,
            parentColumns = ["id"],
            childColumns = ["historyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["historyId"])]
)
    class ImageChunk(
    @PrimaryKey(autoGenerate = true) val chunkId: Long = 0,
    val historyId: Long,   // foreign key linking to CameraHistory
    val chunk: String      // Base64 chunk of the image
)

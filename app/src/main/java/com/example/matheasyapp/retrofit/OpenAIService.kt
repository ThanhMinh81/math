package com.example.matheasyapp.retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


data class Choice(
    @SerializedName("index")
    val index: Int,

    @SerializedName("message")
    val message: MessageResponse
)

data class MessageResponse(
    @SerializedName("content")

    val content : String)

// Định nghĩa lớp Response
data class CompletionResponse(
    val choices: List<Choice>
)


// Định nghĩa cho request


// Lớp cha đại diện cho một phần tử `Content`
sealed class ContentItem {
    abstract val type: String
}

// Lớp con đại diện cho loại `text`
data class TextContent(
    @SerializedName("type") override val type: String,
    @SerializedName("text") val text: String
) : ContentItem()

// Lớp con đại diện cho loại `image_url`
data class ImageUrlContent(
    @SerializedName("type") override val type: String,
    @SerializedName("image_url") val imageUrl: ImageUrl
) : ContentItem()

// Lớp mô tả thông tin của `image_url`
data class ImageUrl(
    @SerializedName("url") val url: String,
    @SerializedName("detail") val detail: String
)

// Lớp mô tả `messages` với một danh sách `content`
data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: List<ContentItem>
)

// Lớp mô tả toàn bộ JSON
data class GPTRequest(
    @SerializedName("model") val model: String,
    @SerializedName("max_tokens") val maxTokens: Int,
    @SerializedName("messages") val messages: List<Message>
)



package com.example.linguai.data.translation

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// DTO pentru răspunsul MyMemory
data class MyMemoryResponseData(
    @SerializedName("translatedText") val translatedText: String
)

data class MyMemoryResponse(
    @SerializedName("responseData") val data: MyMemoryResponseData,
    @SerializedName("responseStatus") val status: Int
)

interface MyMemoryApi {

    // GET https://api.mymemory.translated.net/get?q=TEXT&langpair=en|ro
    @GET("get")
    suspend fun getTranslation(
        @Query("q") q: String,
        @Query("langpair") langPair: String
    ): MyMemoryResponse
}

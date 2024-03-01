package com.example.retrofitapp.api

import com.example.retrofitapp.model.Character
import com.example.retrofitapp.model.Data
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface {

    @GET("id/{id}.json")
    suspend fun getCharacter(@Path("id") charId: Int):Response<Character>
    @GET("all.json")
    suspend fun getCharacters(): Response<Data>

    companion object {
        private const val BASE_URL = "https://akabab.github.io/superhero-api/api/"

        fun create(): APIInterface {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(APIInterface::class.java)
        }
    }

}

package ru.netology.nmedia.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface PostApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>

    @GET("posts/{id}")
    fun getByID(@Path("id") id: Long): Call<Post>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun removeByID(@Path("id") id: Long): Call<Unit>

    @POST("posts/{id}/likes")
    fun likeByID(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun dislikeByID(@Path("id") id: Long): Call<Post>
}

object PostApi{
    val service: PostApiService by lazy {
        retrofit.create()
    }
}
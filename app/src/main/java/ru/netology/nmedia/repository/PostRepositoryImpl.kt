package ru.netology.nmedia.repository

import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post
import java.io.IOException


class PostRepositoryImpl: PostRepository {

    override fun getAll(): List<Post> {
        return PostApi.service.getAll()
            .execute()
            .let {
                if (it.isSuccessful) {
                    it.body() ?: throw RuntimeException("body is null")
                } else {
                    throw RuntimeException(it.message())
                }

                }

    }

    override fun getAllAsync(callback: PostRepository.GetAllCalback) {
        PostApi.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if(!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() :? throw RuntimeException("body is null")


                    override fun onFailure(call: Call, e: IOException) {
                        callback.onSucces(body)
                    }

                }

                override fun onFailure(call: Call<List<Post>>, e: Throwable) {
                   callback.onError(e)
                }
            })
    }


    override fun likeByID(
        id: Long,
        likedByMe: Boolean,
        callback: PostRepository.PostCallback<Post>
    ) {
        val requestBuilder = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .apply {
                if (likedByMe) delete() else post("{}".toRequestBody("application/json".toMediaType()))
            }


        client.newCall(requestBuilder.build())

            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(gson.fromJson<Post>(body.string(), Post::class.java))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }

                    }
                }
            )

    }

    override fun shareByID(id: Long) {

    }

    override fun save(post: Post,  callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(
                object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(gson.fromJson<Post>(body.string(), Post::class.java))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }

                }
            )
    }

    override fun removeByID(id: Long, callback: PostRepository.PostCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(
                object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(Unit)
                    }catch (e: Exception){
                        callback.onError(e)

                        }                    }
                }
            )
    }
}

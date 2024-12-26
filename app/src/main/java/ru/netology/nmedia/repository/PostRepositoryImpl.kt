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

    }

    override fun shareByID(id: Long) {

    }

    override fun save(post: Post,  callback: PostRepository.PostCallback<Post>) {
        PostApi.service.save(post)
            .execute()
    }

    override fun removeByID(id: Long, callback: PostRepository.PostCallback<Unit>) {
        PostApi.service.removeByID(id)
            .execute()
    }
}

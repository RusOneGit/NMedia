package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAllAsync(callback: PostCallback<List<Post>>)

    fun likeByID(id: Long, likedByMe: Boolean, callback: PostCallback<Post>)
    fun shareByID(id: Long)
    fun removeByID(id: Long, callback: PostCallback<Unit>)
    fun save(post: Post, callback: PostCallback<Post>)


    interface PostCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: Throwable)
    }
}
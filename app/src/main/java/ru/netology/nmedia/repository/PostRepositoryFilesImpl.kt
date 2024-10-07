package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {

    companion object {
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()
    }

    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextID = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(file.name).bufferedReader().use {

                posts = gson.fromJson(it, typeToken)
                nextID = posts.maxOfOrNull { post -> post.id }?.inc() ?: 1
                data.value = posts
            }
        }


    }


    override fun getAll() = data

    override fun likeByID(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + (if (it.likedByMe) -1 else 1)
            )
        }

        data.value = posts
    }

    override fun shareByID(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(sharedByMe = true, shares = it.shares + 1)
        }

        data.value = posts
    }

    override fun removeByID(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextID++)) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}
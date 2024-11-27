package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import kotlin.concurrent.thread

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    sharedByMe = false
)
class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedState())
    val data: LiveData<FeedState> = _data
    val edited = MutableLiveData(empty)
    fun applyChangesAndSave(newText: String) {
        edited.value?.let {
            val text = newText.trim()
            if(text!= it.content){
                repository.save(it.copy(content = text))
            }
        }
        edited.value = empty
    }
    fun likeByID(id: Long) = repository.likeByID(id)
    fun shareByID(id: Long) = repository.shareByID(id)
    fun removeByID(id: Long) = repository.removeByID(id)

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedState(loading = true))
        }
        try {
            val posts = repository.getAll()
            FeedState(posts = posts, empty = posts.isEmpty())
        } catch (e: Exception) {
            FeedState(error = true)
        }
            .let(_data::postValue)
    }

    fun edit(post: Post){
        edited.value = post
    }
    fun clear() {
        edited.value = empty
    }
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

}
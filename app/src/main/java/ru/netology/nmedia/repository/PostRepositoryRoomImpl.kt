package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {


    override fun getAll(): LiveData<List<Post>> = dao.getAll().map{
        it.map{it.toDto()}
    }

    override fun save(post: Post) = dao.save(PostEntity.fromDto(post))

    override fun likeByID(id: Long) = dao.likeByID(id)

    override fun shareByID(id: Long) = dao.shareByID(id)

    override fun removeByID(id: Long) = dao.removeByID(id)
}

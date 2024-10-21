package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeByID(id: Long)
    fun removeByID(id: Long)
    fun shareByID(id: Long)
}
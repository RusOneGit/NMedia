package ru.netology.nmedia.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val sharedByMe: Boolean = false,
    val shares: Int = 0,
    val view: Int = 0,
    val videoUrl: String? = null
) {

    fun toDto() = Post(id, author, content, published, likedByMe, likes, sharedByMe, shares)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likedByMe,
            post.likes,
            post.sharedByMe,
            post.shares
        )
    }
}
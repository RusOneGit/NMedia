package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostEntity

@Dao
interface PostDao {
    @Query("SELECT *FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>
    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content=:content WHERE id=:postID")
    fun changeContent(postID: Long, content: String)

    fun save(post: PostEntity) = if(post.id != 0L) changeContent(post.id, post.content) else insert(post)

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun likeByID(id: Long)

    @Query("DELETE FROM PostEntity WHERE id=:id")
    fun removeByID(id: Long)

    @Query(
        """
           UPDATE PostEntity SET
               shares = shares + CASE WHEN sharedByMe THEN -1 ELSE 1 END,
               sharedByMe = CASE WHEN sharedByMe THEN 0 ELSE 1 END
           WHERE id =:id;
        """
    )
    fun shareByID(id: Long)
}
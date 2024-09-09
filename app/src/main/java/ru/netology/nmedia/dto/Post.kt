package ru.netology.nmedia.dto

import java.math.RoundingMode
import java.text.DecimalFormat

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    var shares:Int = 0,
    var sharedByMe: Boolean = false,
    var likedByMe: Boolean = false
)
fun formatCount(count: Int): String {
    val df = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.DOWN
    }

    return when {
        count >= 1_000_000 -> "${df.format(count / 1_000_000.0)}M"
        count >= 1_000 -> "${df.format(count / 1_000.0)}K"
        else -> count.toString()
    }
}

package ru.netology.nmedia.dto

import java.math.RoundingMode
import java.text.DecimalFormat

data class Post(
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val sharedByMe: Boolean = false,
    val shares: Int = 0,
    val view: Int = 0,
    val authorAvatar: String = "",
    val videoUrl: String? = null


)

fun formatCount(count: Int): String {
    val df = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.DOWN
    }

    return when {
        count <= 0 -> ""
        count >= 1_000_000 -> "${df.format(count / 1_000_000.0)}M"
        count >= 1_000 -> "${df.format(count / 1_000.0)}K"
        else -> count.toString()
    }
}




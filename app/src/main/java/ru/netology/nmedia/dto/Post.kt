package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.RoundingMode
import java.text.DecimalFormat
@Parcelize
data class Post(
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val likes: Int = 0,
    val shares:Int = 0,
    val view: Int = 0,
    val sharedByMe: Boolean = false,
    val likedByMe: Boolean = false
): Parcelable
fun formatCount(count: Int): String {
    val df = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.DOWN
    }

    return when {
        count <=0 -> ""
        count >= 1_000_000 -> "${df.format(count / 1_000_000.0)}M"
        count >= 1_000 -> "${df.format(count / 1_000.0)}K"
        else -> count.toString()
    }
}

package ru.netology.nmedia.dto

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {

            author.text = post.author
            published.text = post.published
            content.text = post.content
            countLike.text = formatCount(post.likes)
            countShare.text = formatCount(post.shares)

            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_like_on
                else R.drawable.ic_like_off
            )

            share.setImageResource(
                if (post.sharedByMe) R.drawable.ic_share_on
                else R.drawable.ic_share_off
            )

            like.setOnClickListener { onLikeListener(post) }

            share.setOnClickListener { onShareListener(post) }
        }
    }
}
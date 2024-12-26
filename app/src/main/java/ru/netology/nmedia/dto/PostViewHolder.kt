
package ru.netology.nmedia.dto

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.enumeration.AttachmentType.*

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {


            val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            Glide.with(binding.avatar)
                .load(url)
                .placeholder(R.drawable.ic_not_image)
                .error(R.drawable.ic_error)
                .timeout(10_000)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.avatar)



            author.text = post.author
            published.text = post.published
            content.text = post.content

            like.isChecked = post.likedByMe
            like.text = formatCount(post.likes)



            when (post.attachment?.type) {
                VIDEO -> {
                    attachment.visibility = View.VISIBLE
//                    val url = "http://10.0.2.2:9999/images/${"play.jpg"}"
//                    Glide.with(binding.attachment)
//                        .load(url)
//                        .placeholder(R.drawable.ic_not_image)
//                        .error(R.drawable.ic_error)
//                        .timeout(10_000)
//                        .into(binding.attachment)
                    attachment.setOnClickListener { onInteractionListener.onVideo(post) }
                }

                IMAGE -> {
                    attachment.visibility = View.VISIBLE
                    val url = "http://10.0.2.2:9999/images/${post.attachment.url}"
                    Glide.with(binding.attachment)
                        .load(url)
                        .placeholder(R.drawable.ic_not_image)
                        .error(R.drawable.ic_error)
                        .timeout(10_000)
                        .into(binding.attachment)
                }


                null -> attachment.visibility = View.GONE
            }


            share.isChecked = post.sharedByMe
            share.text = formatCount(post.shares)


            view.text = formatCount(post.view)

            content.setOnClickListener { onInteractionListener.onPost(post) }

            like.setOnClickListener { onInteractionListener.onLike(post) }

            share.setOnClickListener { onInteractionListener.onShare(post) }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    }
                }.show()
            }
        }
    }

}

package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.OnInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostViewHolder
import ru.netology.nmedia.dto.formatCount
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.math.PI

class PostFragment : Fragment() {


    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        val postId = arguments?.getLong("postId") ?: return binding.root
        viewModel.data.observe(viewLifecycleOwner) { state -> val posts = state.posts
            val post = posts.find { it.id == postId } ?: return@observe

            binding.apply {


                val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
                Glide.with(binding.avatar)
                    .load(url)
                    .placeholder(R.drawable.ic_not_image)
                    .error(R.drawable.ic_error)
                    .timeout(10_000)
                    .into(binding.avatar)

                author.text = post.author
                published.text = post.published
                content.text = post.content

                like.isChecked = post.likedByMe
                like.text = formatCount(post.likes)

                video.visibility = if(!post.videoUrl.isNullOrBlank())  View.VISIBLE else View.GONE
                video.setOnClickListener{val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    startActivity(intent)}

                share.isChecked = post.sharedByMe
                share.text = formatCount(post.shares)




                like.setOnClickListener { viewModel.likeByID(post.id) }

                share.setOnClickListener { val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent) }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeByID(post.id)
                                    findNavController().navigateUp()

                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(
                                        R.id.action_postFragment_to_newPostFragment,
                                        Bundle().apply { textArg = post.content })
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

        return binding.root
    }

}

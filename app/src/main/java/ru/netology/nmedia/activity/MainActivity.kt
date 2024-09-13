package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.formatCount
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { posts ->
            binding.container?.removeAllViews()
            posts.map { post ->
                CardPostBinding.inflate(layoutInflater, binding.container, true).apply {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                    like.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)
                share.setImageResource(if (post.sharedByMe)R.drawable.ic_baseline_share_on else R.drawable.ic_baseline_share_24)
                    countLike.text = formatCount(post.likes)
                countShare.text = formatCount(post.shares)

                    like.setOnClickListener {
                        viewModel.likeByID(post.id)

                    }

                    share.setOnClickListener {
                        viewModel.shareByID(post.id)

                    }
                }.root





        }
    }
}
}


package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.OnInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostAdapter
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val postLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result?.let {
                viewModel.changeContent(it)
                viewModel.save()
            } ?: viewModel.clear()
}

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeByID(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeByID(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }


            override fun onEdit(post: Post) {
                viewModel.edit(post)
                postLauncher.launch(post.content)
            }

           override fun onVideo(post: Post){
               val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
               startActivity(intent)

           }

        })


        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)

        }


        binding.fab.setOnClickListener {
            postLauncher.launch("")
        }
    }

}

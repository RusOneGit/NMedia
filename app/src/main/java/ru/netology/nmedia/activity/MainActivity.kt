package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.OnInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostAdapter
import ru.netology.nmedia.util.AndroidUtils.hideKeyboard
import ru.netology.nmedia.util.AndroidUtils.showKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeByID(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeByID(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareByID(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }


        })


        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size && adapter.currentList.isNotEmpty()

            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }


        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.editGroup.visibility = View.VISIBLE
                binding.content.showKeyboard()
                binding.editedText.text = it.content
                binding.content.setText(it.content)
            }
        }

        binding.save.setOnClickListener {

            val text = binding.content.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            viewModel.applyChangesAndSave(text)
            binding.content.setText("")
            binding.content.clearFocus()
            binding.editGroup.visibility = View.GONE

        }

        binding.cancel.setOnClickListener {

                val text = binding.content.text.toString()
                viewModel.applyChangesAndSave(text)
                binding.content.setText("")
                binding.content.clearFocus()
                binding.editedText.hideKeyboard()
                binding.editGroup.visibility = View.GONE
            }
        }
    }

package ru.netology.nmedia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.formatCount


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            sharedByMe = false
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published


            like?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if (post.likedByMe) {
                    like.setImageResource(R.drawable.ic_baseline_favorite_24)
                    post.likes++
                    Toast.makeText(this@MainActivity, "Лайк поставлен!", Toast.LENGTH_SHORT).show()
                    countLike?.text = formatCount(post.likes)
                } else {
                    like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    post.likes--
                    Toast.makeText(this@MainActivity, "Лайк убран!", Toast.LENGTH_SHORT).show()
                    countLike?.text = formatCount(post.likes)
                }
            }
            share.setOnClickListener {
                share.setImageResource(R.drawable.ic_baseline_share_on)
                post.shares++
                countShare.text = formatCount(post.shares)
            }


        }



    }
}




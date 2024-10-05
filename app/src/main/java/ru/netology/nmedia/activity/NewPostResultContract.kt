package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi

class NewPostResultContract : ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, NewPostActivity::class.java).apply {
            putExtra("EDIT_TEXT", input)
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra("POST")
        } else {
            null
        }
}
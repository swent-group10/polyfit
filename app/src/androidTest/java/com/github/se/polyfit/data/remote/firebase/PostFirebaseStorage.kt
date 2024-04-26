package com.github.se.polyfit.data.remote.firebase

import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.model.post.Post
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class PostFirebaseStorage {

    @Test
    fun testFirebaseUpload() = runTest {
        val postRepo = PostFirebaseRepository()
        var inputStream =
            InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")

        val post = Post.default()
        val bytes = inputStream.readBytes()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        post.listOfImages = listOf(bitmap, bitmap)

        val documentReference = postRepo.storePost(post).await()
    }

}
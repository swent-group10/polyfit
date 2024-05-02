import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import java.io.File
import com.google.firebase.storage.StorageReference
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

class PostFirebaseRepositoryFetchTest {

    private lateinit var postFirebaseRepository: PostFirebaseRepository
    private val mockPictureDb: FirebaseStorage = mockk(relaxed = true)
    private val mockStorageRef: StorageReference = mockk(relaxed = true)
    private val mockListResult: ListResult = mockk(relaxed = true)

    @Before
    fun setUp() {
        postFirebaseRepository = PostFirebaseRepository(pictureDb = mockPictureDb)
    }


    @Test
fun fetchImagesForPostReturnsBitmapListOnSuccess() = runBlocking {
    val mockBitmap: Bitmap = mockk(relaxed = true)
    val mockImageRef: StorageReference = mockk(relaxed = true)

    val mockTaskSnapshot: FileDownloadTask.TaskSnapshot = mockk(relaxed = true)
    val mockFileDownloadTask: FileDownloadTask = mockk {
        coEvery { await() } returns mockTaskSnapshot
    }

    val fileSlot = slot<File>()
    coEvery { mockPictureDb.getReference("posts/postKey") } returns mockStorageRef
    coEvery { mockStorageRef.listAll().await() } returns mockListResult
    coEvery { mockListResult.items } returns listOf(mockImageRef)
    coEvery { mockImageRef.getFile(capture(fileSlot)) } returns mockFileDownloadTask
    coEvery { BitmapFactory.decodeFile(any()) } returns mockBitmap

    val result = postFirebaseRepository.fetchImagesForPost("postKey")

    assertTrue(result.size == 1)
    assertEquals(mockBitmap, result[0])
}

    @Test
    fun fetchImagesForPostReturnsEmptyListWhenNoImages() = runBlocking {
        coEvery { mockPictureDb.getReference("posts/postKey") } returns mockStorageRef
        coEvery { mockStorageRef.listAll() } returns Tasks.forResult(mockListResult)
        coEvery { mockListResult.items } returns emptyList()

        val result = postFirebaseRepository.fetchImagesForPost("postKey")

        assertEquals(emptyList<Bitmap>(), result)
    }
}
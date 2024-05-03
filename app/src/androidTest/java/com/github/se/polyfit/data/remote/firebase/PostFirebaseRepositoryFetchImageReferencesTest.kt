/*
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostFirebaseRepositoryTest {

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private val mockPictureDb: FirebaseStorage = mockk(relaxed = true)
  private val mockStorageRef: StorageReference = mockk(relaxed = true)
  private val mockListResult: ListResult = mockk(relaxed = true)

  @Before
  fun setUp() {
    postFirebaseRepository = PostFirebaseRepository(pictureDb = mockPictureDb)
  }

  @Test
  fun fetchImageReferencesForPostReturnsListOfStorageReferencesOnSuccess() = runTest {
    val mockImageRef: StorageReference = mockk(relaxed = true)

    coEvery { mockPictureDb.getReference("posts/postKey") } returns mockStorageRef
    coEvery { mockStorageRef.listAll() } returns Tasks.forResult(mockListResult)
    coEvery { mockListResult.items } returns listOf(mockImageRef)

    val result = postFirebaseRepository.fetchImageReferencesForPost("postKey")

    assertEquals(listOf(mockImageRef), result)
  }

  @Test
  fun fetchImageReferencesForPostReturnsEmptyListWhenNoImages() = runTest {
    coEvery { mockPictureDb.getReference("posts/postKey") } returns mockStorageRef
    coEvery { mockStorageRef.listAll() } returns Tasks.forResult(mockListResult)
    coEvery { mockListResult.items } returns emptyList()

    val result = postFirebaseRepository.fetchImageReferencesForPost("postKey")

    assertEquals(emptyList<StorageReference>(), result)
  }
}
*/

import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class PostFirebaseRepositoryTest2 {

  private val firestore = mockk<FirebaseFirestore>()
  private val realtimeDatabase = mockk<FirebaseDatabase>()
  private val collectionReference = mockk<CollectionReference>()
  private val databaseReference = mockk<DatabaseReference>()
  private val documentReference = mockk<DocumentReference>()
  private val documentSnapshot = mockk<DocumentSnapshot>()
  private val childReference = mockk<DatabaseReference>()

  init {
    every { firestore.collection("posts") } returns collectionReference
    every { realtimeDatabase.getReference("posts_location") } returns databaseReference
    every { databaseReference.child(any()) } returns childReference
    every { documentReference.id } returns "mockId"
    every { documentSnapshot.id } returns "mockId"
    every { documentSnapshot.data } returns null
    every { childReference.setValue(any(), any(), any()) } just Runs
  }

  private val repository = PostFirebaseRepository(firestore, realtimeDatabase)

  @Test
  fun storePostSetsLocationInGeoFire() {
    val post = Post.default()
    every { collectionReference.add(any()) } returns Tasks.forResult(documentReference)

    runBlocking { repository.storePost(post) }

    verify { collectionReference.add(any()) }
  }

  @Test
  fun storePostFailsWhenFirestoreFails() = runBlocking {
    val post = Post.default()
    every { collectionReference.add(any()) } returns
        Tasks.forException(Exception("Firestore failure"))

    assertFailsWith<Exception> { repository.storePost(post).await() }

    verify(exactly = 0) {
      childReference.setValue(any(), any())
    } // Ensure no Realtime Database interaction on Firestore failure
  }

  @Test
  fun fetchPostsTriggersGeoQueryEventListener() {
    val keys = listOf("key1", "key2", "key3")
    val querySnapshot = mockk<QuerySnapshot>()
    every { querySnapshot.documents } returns listOf(documentSnapshot)
    every { collectionReference.whereIn(FieldPath.documentId(), keys).get() } returns
        Tasks.forResult(querySnapshot)

    runBlocking { repository.fetchPosts(keys) {} }

    verify { collectionReference.whereIn(FieldPath.documentId(), keys).get() }
  }
}

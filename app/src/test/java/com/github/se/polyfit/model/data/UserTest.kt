import android.net.Uri
import com.github.se.polyfit.model.data.User
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserTest {

  @Test
  fun `User creation with valid data`() {
    val user = User("1", "Test User", "test@example.com", null)
    assertEquals("1", user.id)
    assertEquals("Test User", user.name)
    assertEquals("test@example.com", user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `reset current User`() {
    User.currentUser?.resetCurrentUser()

    assertNull(User.currentUser)
  }

  @Test
  fun `currentUser can be set`() {
    val user = User("10000", "Test User", "test@example.com", null)
    User.currentUser = user
    assertEquals(user, User.currentUser)
  }

  @Test
  fun `currentUser can be set with mock`() {
    // Mock the Uri class
    val mockUri = mockk<Uri>()

    // Mock the User class and its properties
    val mockUser =
        mockk<User> {
          every { id } returns "10000"
          every { name } returns "Test User"
          every { email } returns "test@example.com"
          every { photoURL } returns mockUri
        }

    User.currentUser = mockUser

    // Verify that the currentUser is the mockUser
    assertEquals(mockUser, User.currentUser)
  }
}

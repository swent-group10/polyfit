import com.github.se.polyfit.model.data.User
import org.junit.Assert.*
import org.junit.Test

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
  fun `currentUser is null by default`() {
    assertNull(User.currentUser)
  }

  @Test
  fun `currentUser can be set`() {
    val user = User("1", "Test User", "test@example.com", null)
    User.currentUser = user
    assertEquals(user, User.currentUser)
  }
}

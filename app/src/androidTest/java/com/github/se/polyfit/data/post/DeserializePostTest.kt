package com.github.se.polyfit.data.post

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import java.time.LocalDate
import org.junit.Assert
import org.junit.Test

class DeserializePostTest {
  @Test
  fun deserializePost() {
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to Meal.default().serialize(),
                "createdAt" to LocalDate.now()))
    val expectedPost =
        Post(
            "userId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now())
    Assert.assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentLocation() {
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 10.0,
                        "latitude" to 20.0,
                        "altitude" to 30.0,
                        "name" to "MIT"),
                "meal" to Meal.default().serialize(),
                "createdAt" to LocalDate.now()))
    val expectedPost =
        Post(
            "userId",
            "description",
            Location(10.0, 20.0, 30.0, "MIT"),
            Meal.default(),
            LocalDate.now())
    Assert.assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentUserId() {
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "differentUserId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to Meal.default().serialize(),
                "createdAt" to LocalDate.now()))
    val expectedPost =
        Post(
            "differentUserId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now())
    Assert.assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentDescription() {
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "differentDescription",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to Meal.default().serialize(),
                "createdAt" to LocalDate.now()))
    val expectedPost =
        Post(
            "userId",
            "differentDescription",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now())
    Assert.assertEquals(expectedPost, post)
  }

  @Test(expected = IllegalArgumentException::class)
  fun deserializeThrowsException() {
    val data =
        mapOf(
            "userId" to null,
            "description" to "description",
            "location" to
                mapOf("longitude" to 0.0, "latitude" to 0.0, "altitude" to 10.0, "name" to "EPFL"),
            "meal" to Meal.default().serialize(),
            "createdAt" to LocalDate.now())

    Post.deserialize(data)
  }
}

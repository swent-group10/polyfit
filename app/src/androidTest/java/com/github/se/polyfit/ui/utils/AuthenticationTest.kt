package com.github.se.polyfit.ui.utils

import com.github.se.polyfit.MainActivity
import com.github.se.polyfit.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AuthenticationTest {

    private lateinit var authentication: Authentication
    private lateinit var mockUser: User
    private lateinit var mockAuth: FirebaseAuth

    @Before
    fun setUp() {
        mockUser = Mockito.mock(User::class.java)
        mockAuth = Mockito.mock(FirebaseAuth::class.java)
        val mockActivity = Mockito.mock(MainActivity::class.java)
        authentication = Authentication(mockActivity, mockUser, mockAuth)
    }

    @Test
    fun testIsAuthenticated() {
        Mockito.`when`(mockAuth.currentUser).thenReturn(null)
        Assert.assertFalse(authentication.isAuthenticated())

        Mockito.`when`(mockAuth.currentUser).thenReturn(Mockito.mock(FirebaseUser::class.java))
        Assert.assertTrue(authentication.isAuthenticated())
    }
}
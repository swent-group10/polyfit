package com.github.se.polyfit.data.Location

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class PostLocationModelTest {
    private lateinit var model: PostLocationModel
    private val context = mockk<Context>(relaxed = true)
    private val fusedLocationClient = mockk<FusedLocationProviderClient>()
    private val currentLocationRequest = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()


    @Before
    fun setUp() {

        model = PostLocationModel(context)
    }


    @Test
    fun testGetCurrentLocation(): Unit = runTest {
        mockkStatic(ActivityCompat::class)
        every { ActivityCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_GRANTED

        val mockLocation = mockk<Location>(relaxed = true)
        every { mockLocation.longitude } returns 10.0
        every { mockLocation.latitude } returns 20.0
        every { mockLocation.altitude } returns 5.0

        val task = Tasks.forResult(mockLocation)
        coEvery { fusedLocationClient.getCurrentLocation(currentLocationRequest, any()) } returns task

        val location = model.currentLocation(fusedLocationClient)

        assertEquals(10.0, location.longitude, 0.001)
        assertEquals(20.0, location.latitude, 0.001)
        assertEquals(5.0, location.altitude, 0.001)
        unmockkStatic(ActivityCompat::class)
    }

    @Test
    fun testPermissionDenied() = runTest {
        mockkStatic(ActivityCompat::class)
        every { ActivityCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_DENIED
        every { ActivityCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_DENIED

        val location = model.getCurrentLocation()
        assertEquals(location, com.github.se.polyfit.model.post.Location.default())

        unmockkStatic(ActivityCompat::class)
    }

    @Test
    fun testWithoutGranting() = runTest {
        mockkStatic(ActivityCompat::class)
        every { ActivityCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_DENIED

        val location = model.checkLocationPermissions()
        assertEquals(location, com.github.se.polyfit.model.post.Location.default())

        unmockkStatic(ActivityCompat::class)
    }

}

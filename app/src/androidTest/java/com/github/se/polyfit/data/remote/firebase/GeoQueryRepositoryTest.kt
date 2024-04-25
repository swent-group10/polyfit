package com.github.se.polyfit.data.remote.firebase

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class GeoQueryRepositoryTest {

    @Test
    fun simpleTest() = runBlocking {
        val geoQueryRepository = GeoQueryRepository()
        geoQueryRepository.queryNearbyPosts(0.0, 0.0, 10.0)
        delay(10000)  // wait for 10 seconds
    }
}
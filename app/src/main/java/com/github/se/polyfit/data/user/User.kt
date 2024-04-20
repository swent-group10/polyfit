package com.github.se.polyfit.model.data

import android.net.Uri

data class User(
    var id: String = "",
    var displayName: String? = null,
    var familyName: String? = null,
    var givenName: String? = null,
    var email: String = "",
    var photoURL: Uri? = null
)

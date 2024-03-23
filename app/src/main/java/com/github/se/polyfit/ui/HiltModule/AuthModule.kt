package com.github.se.polyfit.ui.HiltModule

import android.content.Context
import com.github.se.polyfit.ui.Utils.Authentication
import com.github.se.polyfit.ui.Utils.AuthenticationCloud
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
  @Provides
  fun provideAuthentication(@ApplicationContext context: Context): Authentication {
    return AuthenticationCloud(context)
  }
}

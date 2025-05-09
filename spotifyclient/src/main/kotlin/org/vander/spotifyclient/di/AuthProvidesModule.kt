package org.vander.spotifyclient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.vander.spotifyclient.data.remote.datasource.AuthRemoteDataSource
import org.vander.spotifyclient.data.repository.AutRepository
import org.vander.spotifyclient.domain.auth.IAuthRepository
import org.vander.spotifyclient.domain.auth.IDataStoreManager

@Module
@InstallIn(ViewModelComponent::class)
object AuthProvidesModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(
        authRemoteDataSource: AuthRemoteDataSource,
        dataStoreManager: IDataStoreManager
    ): IAuthRepository {
        return AutRepository(authRemoteDataSource, dataStoreManager)
    }
}
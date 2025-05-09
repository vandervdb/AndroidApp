package org.vander.spotifyclient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.plugins.logging.LogLevel
import org.vander.spotifyclient.domain.auth.ITokenProvider
import org.vander.spotifyclient.network.KtorClientConfig
import org.vander.spotifyclient.utils.HTTPS_API_SPOTIFY_COM_V_1_ME
import javax.inject.Named
import javax.inject.Singleton
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object PlaylistRemoteDataSourceModule {

    @Provides
    @Singleton
    @Named("PlaylistHttpClientConfig")
    fun providePlaylistHttpClientConfig(): KtorClientConfig {
        return KtorClientConfig(
            baseUrl = HTTPS_API_SPOTIFY_COM_V_1_ME,
            enableAuthPlugin = true,
            logLevel = LogLevel.ALL
        )
    }

    @Provides
    @Singleton
    @Named("PlaylistHttpClient")
    fun providePlaylistHttpClient(
        tokenProvider: ITokenProvider,
        @Named("PlaylistHttpClientConfig") config: KtorClientConfig
    ): HttpClient {
        return NetworkModule.provideKtorClient(tokenProvider, config)
    }

}
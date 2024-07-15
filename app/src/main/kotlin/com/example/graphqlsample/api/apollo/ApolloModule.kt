package com.example.graphqlsample.api.apollo

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.api.MemoryCacheFactory
import com.apollographql.cache.normalized.normalizedCache
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import com.example.graphqlsample.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {
    private const val SERVER_URL = "https://api.github.com/graphql"

    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val HEADER_AUTHORIZATION_BEARER = "Bearer"


    @Provides
    @Singleton
    fun provideApolloClient(@ApplicationContext applicationContext: Context): ApolloClient {
        val memoryCache = MemoryCacheFactory(maxSizeBytes = 5 * 1024 * 1024)
        val sqlCache = SqlNormalizedCacheFactory(applicationContext, "app.db")
        val memoryThenSqlCache = memoryCache.chain(sqlCache)

        return ApolloClient.Builder()
            .serverUrl(SERVER_URL)

            // Add headers for authentication
            .addHttpHeader(
                HEADER_AUTHORIZATION,
                "$HEADER_AUTHORIZATION_BEARER ${BuildConfig.GITHUB_OAUTH_KEY}"
            )

            // Normalized cache
            .normalizedCache(memoryThenSqlCache)

            .build()
    }
}

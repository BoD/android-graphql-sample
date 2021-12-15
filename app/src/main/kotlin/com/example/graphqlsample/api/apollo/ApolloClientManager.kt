package com.example.graphqlsample.api.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpHeader
import com.apollographql.apollo3.network.http.HeadersInterceptor
import com.example.graphqlsample.BuildConfig

/**
 * Using an object (singleton) for the app's ApolloClient.
 * Note: in a real world app, we would instead use Dagger or a similar tool.
 */
object ApolloClientManager {
    private const val SERVER_URL = "https://api.github.com/graphql"

    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val HEADER_AUTHORIZATION_BEARER = "Bearer"

    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl(SERVER_URL)

        // Add headers for authentication
        .addHttpInterceptor(
            HeadersInterceptor(
                listOf(
                    HttpHeader(
                        HEADER_AUTHORIZATION, "$HEADER_AUTHORIZATION_BEARER ${BuildConfig.GITHUB_OAUTH_KEY}"
                    )
                )
            )
        )
        .build()
}

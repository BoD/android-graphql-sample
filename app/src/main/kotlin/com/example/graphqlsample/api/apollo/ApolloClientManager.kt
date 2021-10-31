package com.example.graphqlsample.api.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpNetworkTransport

/**
 * Using an object (singleton) for the app's ApolloClient.
 * Note: in a real world app, we would instead use Dagger or a similar tool.
 */
object ApolloClientManager {
    private const val SERVER_URL = "https://api.github.com/graphql"

    val apolloClient: ApolloClient = ApolloClient.Builder()
        .networkTransport(
            HttpNetworkTransport(
                serverUrl = SERVER_URL,
                interceptors = listOf(AuthInterceptor())
            )
        )
        .build()

}

package com.example.graphqlsample.api.apollo

import com.apollographql.apollo.ApolloClient

/**
 * Using an object (singleton) for the app's ApolloClient.
 * Note: in a real world app, we would instead use Dagger or a similar tool.
 */
object ApolloClientManager {
    private const val SERVER_URL = "https://api.github.com/graphql"

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl(SERVER_URL)
        .addApplicationInterceptor(AuthInterceptor())
        .build()

}

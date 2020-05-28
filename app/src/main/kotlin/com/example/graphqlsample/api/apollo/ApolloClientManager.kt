package com.example.graphqlsample.api.apollo

import com.apollographql.apollo.ApolloClient

object ApolloClientManager {
    private const val SERVER_URL = "https://api.github.com/graphql"

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl(SERVER_URL)
        .addApplicationInterceptor(AuthInterceptor())
        .build()

}

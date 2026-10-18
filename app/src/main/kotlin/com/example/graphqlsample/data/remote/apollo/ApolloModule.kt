package com.example.graphqlsample.data.remote.apollo

import com.apollographql.apollo.ApolloClient
import com.example.graphqlsample.BuildConfig
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

private const val SERVER_URL = "https://api.github.com/graphql"

private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_AUTHORIZATION_BEARER = "Bearer"


private fun provideApolloClient(): ApolloClient {
  return ApolloClient.Builder()
    .serverUrl(SERVER_URL)

    // Add headers for authentication
    .addHttpHeader(
      HEADER_AUTHORIZATION,
      "$HEADER_AUTHORIZATION_BEARER ${BuildConfig.GITHUB_OAUTH_KEY}",
    )

    .build()
}

val apolloModule = module {
  single { create(::provideApolloClient) }
}

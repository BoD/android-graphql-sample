package com.example.graphqlsample.api.apollo

import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.example.graphqlsample.BuildConfig
import java.util.concurrent.Executor

class AuthInterceptor : ApolloInterceptor {
    override fun interceptAsync(
        request: ApolloInterceptor.InterceptorRequest,
        chain: ApolloInterceptorChain,
        dispatcher: Executor,
        callBack: ApolloInterceptor.CallBack
    ) {
        chain.proceedAsync(
            request.toBuilder().requestHeaders(
                request.requestHeaders.toBuilder().addHeader(
                    HEADER_AUTHORIZATION,
                    "$HEADER_AUTHORIZATION_BEARER ${BuildConfig.GITHUB_OAUTH_KEY}"
                ).build()
            ).build(), dispatcher, callBack
        )
    }

    override fun dispose() {}

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_AUTHORIZATION_BEARER = "Bearer"
    }
}

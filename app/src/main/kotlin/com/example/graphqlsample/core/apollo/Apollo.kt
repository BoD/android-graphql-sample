package com.example.graphqlsample.core.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await

suspend fun <D : Operation.Data, T, V : Operation.Variables> ApolloClient.suspendQuery(query: Query<D, T, V>): Response<T> =
    query(query).await()

suspend fun <D : Operation.Data, T, V : Operation.Variables> ApolloClient.suspendMutate(mutation: Mutation<D, T, V>): Response<T> =
    mutate(mutation).await()

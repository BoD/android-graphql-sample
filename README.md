# GraphQL Sample

## Work in progress!

This is a small example project that uses the [Apollo GraphQL Android library](https://github.com/apollographql/apollo-android),
with Github's API.

The aim is to explore this library and GraphQL in general.

## Get a Github API Oauth token

👉 https://developer.github.com/v4/guides/forming-calls/#authenticating-with-graphql

Then put it in `build.properties` (follow the sample in build.properties.SAMPLE).

## How to get the schema.json file

Either, manually:
👉 https://github.com/octokit/graphql-schema

Or using Apollo's gradle task:

```shell
./gradlew :app:downloadApolloSchema \
--endpoint='https://api.github.com/graphql' \
--schema='app/src/main/graphql/com/example/graphqlsample/queries/schema.json'  \
--header="Authorization: Bearer <your token>"
```

(replace `<your token>` by the token you got from Github )

## Architecture of the project

There is one activity which is simply a dumb host for 3 fragments:

### `ViewerInfoFragment`

Shows information about the "Viewer" (the currently logged-in user is called "Viewer" in the Github API):

- Login
- Name
- Email
- Total number of repos
- Info about the first 10 repos (name, description, number of stars)

Interesting to see: all this information is fetched with a single query (see `ViewerInfoQuery.graphql`).

### `RepositoryListFragment`
Displays a paginated list of the viewer's repositories.

Interesting to see:
- use of a GraphQL "fragment" (see `UserRepositoryListQuery.graphql`)
- how pagination works in GraphQL ("cursor")
- use of the Android Pagination component

### `MiscFragment`
Demonstrates mutations and error handling (see `AddCommentToIssueMutation.graphql`)
- Add comment to an issue that exists
- Add comment to an issue that doesn't exist (error case)
- Use the "search" operation and demonstrate a case with heterogeneous results (either `User` or `Organization`, see `SearchQuery.graphql`)
    
    
## Useful links
- [GraphQL spec](https://graphql.github.io/graphql-spec/June2018/#)
- [Intro to GraphQL](https://graphql.org/learn/)
- [Apollo Android README](https://github.com/apollographql/apollo-android)
- [Medium article about Apollo Android coroutines extensions](https://medium.com/dailymotion/consume-your-graphql-api-with-kotlin-coroutines-8dcf716712b2)
- [Github GraphQL API doc](https://developer.github.com/v4/)
- [Github GraphQL Explorer](https://developer.github.com/v4/explorer/) ([More info](https://developer.github.com/v4/guides/using-the-explorer/))
- [GraphQL pagination](https://graphql.org/learn/pagination/)


## License

This is [PUBLIC DOMAIN](https://creativecommons.org/publicdomain/zero/1.0/legalcode).

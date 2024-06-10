# GraphQL Sample

This is a small example project that uses the [Apollo Kotlin](https://github.com/apollographql/apollo-kotlin) GraphQL library on Android, with Github's API.

The aim is to explore this library and GraphQL in general.

## Get a Github API Oauth token

👉 https://developer.github.com/v4/guides/forming-calls/#authenticating-with-graphql

Then put it in `build.properties` (follow the sample in build.properties.SAMPLE).

## Get the GraphQL schema file

Either, manually:
👉 https://github.com/octokit/graphql-schema

Or using Apollo's gradle task:

```shell
./gradlew downloadGithubApolloSchemaFromIntrospection
```

(replace `<your token>` by the token you got from Github)

## Architecture of the project

There is a single activity which is a host for 4 Compose layouts:

### `ViewerInfoLayout`
Shows information about the "Viewer" (the currently logged-in user is called "Viewer" in the Github API):

- Login
- Name
- Email
- Total number of repos
- Info about the first 10 repos (name, description, number of stars)

Noteworthy:
- all this information is fetched with a single query (see `ViewerInfoQuery.graphql`)
- use of the `@nonnull` annotation (specific to the Apollo Kotlin library), to "improve" the schema

### `RepositoryListLayout`

Displays a paginated list of the viewer's repositories.

Noteworthy:
- use of a GraphQL "fragment" (see `UserRepositoryListQuery.graphql`)
- how pagination works in GraphQL ("cursor")
- integration with the Android Pagination component

### `RepositorySearchLayout`

Use the "search" operation and demonstrate a case with heterogeneous results - either `User` or `Organization` (see `SearchQuery.graphql`)

### `MiscLayout`

Demonstrates mutations and error handling (see `AddCommentToIssueMutation.graphql`)

- Add comment to an issue that exists
- Add comment to an issue that doesn't exist (error case)

## Useful links

- [GraphQL spec](https://spec.graphql.org/)
- [Intro to GraphQL](https://graphql.org/learn/)
- [Apollo Kotlin README](https://github.com/apollographql/apollo-kotlin)
- [Github GraphQL API doc](https://developer.github.com/v4/)
- [Github GraphQL Explorer](https://developer.github.com/v4/explorer/) ([More info](https://developer.github.com/v4/guides/using-the-explorer/))
- [GraphQL pagination](https://graphql.org/learn/pagination/)


## License

This is [PUBLIC DOMAIN](https://creativecommons.org/publicdomain/zero/1.0/legalcode).

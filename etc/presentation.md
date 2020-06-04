# GraphQL

https://docs.google.com/presentation/d/11z7T-tUC0JHZSjI-9TofQnu9FY6Zc5oDGmcEqD_aTZU/edit#slide=id.g5d8e1133a5_0_45


## Context

### In 1 sentence
- A data query and manipulation language for APIs (Source: https://en.wikipedia.org/wiki/GraphQL)
- Language: it's a language with a precise spec, like xml or json for instance
- Data: it's for data
- Query *and* manipulation: both ways
- for APIs: it tackles a specific problem (for instance, it's completely different from SQL)

### History
- Developed internally by Facebook in 2012
- Made public & open source in 2015
- Handled by a foundation in 2018, hosted by the Linux Foundation
- No longer considered a 'draft' since June 2018

### Today
- Getting traction (graph)
    - [StackOverflow BigQuery](https://bigquery.cloud.google.com/savedquery/313882806843:124cdad526bc431d8be1520bf466f9cf)
    - [StackOverflow query](https://data.stackexchange.com/stackoverflow/query/1231914/percentage-of-graphql-related-questions-over-time)
    - [StackOverflow graph](https://docs.google.com/spreadsheets/d/1XYqZXTqijhE__dYMHTSyqbpEFUDrFi2Z_EtIUrxh9Gs/edit#gid=1183648644)
    - [Google trends](https://trends.google.com/trends/explore?date=2015-01-01%202020-04-26&q=%2Fg%2F11cn3w0w9t)


### Adoption
https://graphql.org/users/
- Facebook
- Github
- Gitlab
- Braintree
- Shopify
- Pinterest
- Yelp
- DailyMotion
- AlloCine


## In >1 sentences
(Source: https://graphql.org/)

### Ask for what you need, get exactly that

### Get many resources in a single request

### Describe what's possible with a type system

### Move faster with powerful developer tools

### Evolve your API without versions

### Bring your own data and code


## Zoom
(Source: https://code.fb.com/core-data/graphql-a-data-query-language/)

### Zoom on Queries
https://code.fb.com/wp-content/uploads/2015/09/GItytQBz1lxmkpsBAJVp1VIAAAAAbj0JAAAB.jpg
- Query looks a bit like JSON but is not / Results is JSON
- Defines a data shape
- Hierarchical
- Strongly typed
- Protocol, not storage

### Zoom on Schema
https://code.fb.com/wp-content/uploads/2015/09/GKdytQCnbMRd9ncBAAZVy0wAAAAAbj0JAAAB.jpg
- Introspective
- Version free


## Hands on: Github API

### Github GraphiQL
- https://developer.github.com/v4/explorer/

### Example queries
(Based on https://graphql.org/learn/queries/)

#### Very simple, 1 object, 2 fields
```graphql
query {
  viewer {
    login
    name
  }
}
```
Viewer means "the logged in user" in Github API.

#### Sub-selection (hierarchy)
```graphql
query {
  viewer {
    login
    name
    itemShowcase {
      hasPinnedItems
    }
  }
}
```

#### 2 objects in 1 query! + Argument
```graphql
{
  viewer {
    login
    name
  }

  user(login: "John") {
    login
    name
  }
}
```
Note also that the `query` keyword is optional.

#### 3 objects including 2 of the same type (_aliases_)
```graphql
{
  viewer {
    login
    name
  }

  userBoD: user(login: "BoD") {
    login
    name
  }

  userJohn: user(login: "John") {
    login
    name
  }
}
```

#### Don't repeat yourself (_fragments_)
```graphql
fragment userFields on User {
  login
  name
  bio
  company
}

{
  viewer {
    login
    name
  }
  userBoD: user(login: "BoD") {
    ...userFields
  }
  userJohn: user(login: "John") {
    ...userFields
  }
}

```

#### Named query and variables
```graphql
query UserInfo($userId: String!) {
  user(login: $userId) {
    login
    name
  }
}
```
```
{"userId": "BoD"}
```

#### Lists
```graphql
{
  viewer {
    repositories(first: 10) {
      nodes {
        name
      }
      totalCount
    }
  }
}
```

#### Pagination
```graphql
query ViewerRepositoryListQuery($first: Int!, $after: String) {
  viewer {
    repositories(first: $first, after: $after) {
      totalCount
      edges {
        cursor
        node {
          name
        }
      }
    }
  }
}
```
```
--- First page
{"first": 10}

--- Next page
{
  "first": 10,
  "after": "Y3Vyc29yOnYyOpHOAJgiTQ=="
}
```

#### Use of variables in fragments
```graphql
fragment userFields on User {
  login
  name
  bio
  firstProjects: repositories(first: $firstReposCount) {
    edges {
      node {
        name
      }
    }
  }
}

query twoUsersWithRepos($firstReposCount: Int) {
  viewer {
    login
    name
  }
  userBoD: user(login: "BoD") {
    ...userFields
  }
  userJohn: user(login: "John") {
    ...userFields
  }
}
```

#### Error messages
```graphql
query {
  viewer {
    login
    email
    zlutekz
  }
}
```

```graphql
query ViewerRepositoryListQuery($first: Int!, $after: String) {
  viewer {
    repositories(first: $first, after: $after) {
      totalCount
      edges {
        cursor
        node {
          name
        }
      }
    }
  }
}
```
With no value for `first`
Note that it's possible to have both data and errors in the result. The error format is standard.

#### Mutations
```graphql
mutation AddCommentToIssueMutation($issueId: ID!, $body: String!) {
  addComment(input: {subjectId: $issueId, body: $body}) {
    commentEdge {
      node {
        url
      }
    }
  }
}
```
```
--- Success
{
  "issueId": "MDU6SXNzdWUyMzEzOTE1NTE=",
  "body": "Hello, World! 1000"
}

--- Error
{
  "issueId": "XXX",
  "body": "Hello, World! 1000"
}
```


#### Queries returning different types depending on parameters
```graphql
fragment organizationFields on Organization {
  name
}

fragment userFields on User {
  name
  bio
}

fragment repositoryFields on Repository {
  name
  owner {
    __typename
    ...userFields
    ...organizationFields
  }
}

query {
  search(query: "foo", type: REPOSITORY, first: 100) {
    edges {
      cursor
      textMatches {
        property
        fragment
        highlights {
          beginIndice
          endIndice
          text
        }
      }
      node {
        __typename
        ...repositoryFields
      }
    }
  }
}
```
Here we have a search query that can return different things. The way to deal with that is to
use fragments.

## Hands on: Apollo GraphQL Android

- User Infos (main page)
- Repository list (pagination)
- Search
- Mutation
- Error handling


# Getting Started

The Sequence API is composed of the three main endpoints, the simplicity is one of the many powerful features of the API.

```javascript
POST /v1/transactions
GET  /v1/transactions?account={id}
GET  /v1/balances?account={id}
```

### Try out the API collections

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/33cfd73e447946a7171d#?env%5BTest%20Keys%5D=W3sia2V5IjoicHVibGljX2tleSIsInZhbHVlIjoicGtfMWVIWnVyMXpFMlY2M005d2hJTVBYTXB6VWZlIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJzZWNyZXRfa2V5IiwidmFsdWUiOiJza18wZDA0Nzc5NDA3NzgyZjkyOTliYTkwOTVjZWQzMjRkYSIsImVuYWJsZWQiOnRydWV9XQ==) [![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=Decimals%20Starter&uri=https%3A%2F%2Fgist.githubusercontent.com%2Fandriosr%2F08789da580b69f666820b6ddefcabaa5%2Fraw%2F2ca157bd059ef71ae33a78421354861777930745%2Fstarter.json)

#### Get your Keys

To start using them, the first step is to setup our keys, let's encode our secret key to use it in the Authorization header.

```shell
public_key='pk_test_decimals_docs'
secret_key='sk_test_decimals_docs'

echo 'sk_test_decimals_docs:' | base64
```

#### Seed Accounts

Now let's make our first transaction by seeding funds to our first account. For that we use the Public Key as the origin account.

<!--
type: tab
title: Request Maker
-->

```json http
{
  "method": "post",
  "url": "https://api.decimals.app/v1/transactions",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg=="
  },
  "body": {
    "from": "pk_test_decimals_docs",
    "to": "Alice",
    "amount": 1000,
    "currency": "usd"
  }
}
```

<!--
type: tab
title: cURL
-->

```shell
curl https://api.decimals.app/v1/transactions \
  -H 'authorization: Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg==' \
  -d '{
  "from": "pk_test_decimals_docs",
  "to": "Alice",
  "amount": 1000,
  "currency": "usd"
}'
```

<!-- type: tab-end -->

#### Make Transactions

And now we can make transactions from our first Account.

<!--
type: tab
title: Request Maker
-->

```json http
{
  "method": "post",
  "url": "https://api.decimals.app/v1/transactions",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg=="
  },
  "body": {
    "from": "Alice",
    "to": "Bob",
    "amount": 500,
    "currency": "usd"
  }
}
```

<!--
type: tab
title: cURL
-->

```shell
curl https://api.decimals.app/v1/transactions \
  -H 'authorization: Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg==' \
  -d '{
  "from": "Alice",
  "to": "Bob",
  "amount": 500,
  "currency": "usd"
}'
```

<!-- type: tab-end -->

#### Get Transactions and Balances

Great, let's see what just happened.

<!--
type: tab
title: Request Maker
-->

**List Alice's Balances**

```json http
{
  "method": "get",
  "url": "https://api.decimals.app/v1/balances",
  "query": {
    "account": "Alice"
  },
  "headers": {
    "Authorization": "Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg=="
  }
}
```

**List Alice's Transactions**

```json http
{
  "method": "get",
  "url": "https://api.decimals.app/v1/transactions",
  "query": {
    "account": "Alice"
  },
  "headers": {
    "Authorization": "Basic c2tfdGVzdF9kZWNpbWFsc19kb2NzOg=="
  }
}
```

<!-- type: tab-end -->


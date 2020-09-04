# Getting Started

Decimals API is composed of the three main endpoints, the simplicity is one of the many powerful features of the API.

```javascript
POST /v1/transactions
GET  /v1/transactions?account={id}
GET  /v1/balances?account={id}
```

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


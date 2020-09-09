Sequence
==========

![Sequence CI](https://github.com/decimals/sequence/workflows/Clojure%20CI/badge.svg)

- Website: https://decimals.app/sequence
- Community: [Decimals Slack](https://docs.google.com/forms/d/1K_X6VlnNufptkRqc3oqg0GqzBH7NXb6AAhCd3ITjstU)
- Documentation: [https://docs.decimals.app/](https://docs.decimals.app/docs/sequence)

<img alt="Sequence" src="https://decimals.app/dist/images/sequence.png" width="600px">

Sequence is an API that store validate and reports asset movements. Also known as a Ledger. Sequence is immutable, scalable, and easy to use.

The key features of Sequence are:

- **Multi-Currency**: Store, move and analyse any asset, from regular currencies like USD, to shelf items. 

- **Multi-Tenant**: Run multiple ledgers using the same infrastructure. Simply setup multiple tenants in the configurations and use the different API keys.


- **No-SQL powered**: Sequence runs on top of a No-SQL database. It is horizontally scalable from the 12-factors container to the persistency layer.

- **Immutable**: Most existing ledger use database updates. This is bad for a ledger. Sequence is immutable. The design of the database allows for consistency of balances without a single field using updateds.

- **API simplicity**: Sequence creates things as you use them. When you send value to an account, it is gets created if it does not exist. It gets out of the way. Use it, and things will work.

- **Analytics**: Send asset movement events to multiple destinations. Use it for analytics, fraud-detection, anything.

## Getting Started

### docker-compose

1. Clone and cd into the repository
2. Use docker-compose to bring up DynamoDB local and Sequence

```
git clone https://github.com/decimals/sequence.git

cd sequence

docker-compose up
```

### Docker

1. Start the DynamoDB local container
2. Start the Sequence container

```
docker run -p 8000:8000 amazon/dynamodb-local

docker run -p 8910:8910 docker.pkg.github.com/decimals/sequence/sequence:0.0.1
```

## Configurations

All configurations are loaded from environment variables. The available configurations are:

| environment variable | configuration | dev profile |
|----------------------|---|---------------|
| `DYNAMODB_ENDPOINT`  | The host for the DynamoDB instance. Mostly used for local development.  | `http://localhost:8000` |
| `SEGMENT_KEY`         | Optional Segment.io key to generate analytics events. |                         |
| `KEYS`                 | A string with a list of tenants and their `sha256` API keys digests. In json format. | `[{ "name": "test","email": "test@decimals.app", "public-key": "abc", "secret-key-hash": "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"}]` |

Configurations can also be loaded from the `profiles.clj` file, where the dev configurations are setup.

## Developing Sequence

1. Start a new REPL: `lein repl`
2. Start Sequence in dev-mode: `(def dev-serv (run-dev))`
3. Connect your editor to the running REPL session.
   Re-evaluated code will be seen immediately in the service.

## Links
* [Sequence design](https://decimals.substack.com/p/things-i-wish-i-knew-before-building)
* [Open-source ledgers](http://decimals.app)

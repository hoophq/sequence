Sequence
==========
- Website: https://decimals.app/sequence
- Community: [Decimals Slack](https://decimals.app)
- Documentation: [https://docs.decimals.app/](https://docs.decimals.app/)

Sequence is an API that stores, validates and reports asset movements. Also known as a Ledger. Sequence is immutable, scalable, and minimalist.

The key features of Sequence are:

- **Currency Agnostic**: Store and move any asset, from regular currencies like USD, to shelf items. 

- **Easy API**: Sequence creates things as you use them. When you send value to an account, it is created if it does not exist. It gets out of the way. Just use it, and things will work.

- **Immutability**: Most existing ledger use database updates. This is bad for a ledger. Sequence is completelly immutable. The design of the database allows for consistency of balances without a single field being updated.

- **No-SQL Scalability**: Sequence runs on top of a No-SQL database, making it horizontally scalable from the 12-factors contianer to the database layer.

## Getting Started

### Docker

### Clojure lein

1. Start the application: `lein run`

## Configurations


## Developing Sequence

1. Start a new REPL: `lein repl`
2. Start Sequence in dev-mode: `(def dev-serv (run-dev))`
3. Connect your editor to the running REPL session.
   Re-evaluated code will be seen immediately in the service.

## Links
* [Sequence design](http://decimals.app)
* [Open-source ledgers](http://decimals.app)

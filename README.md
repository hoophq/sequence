Sequence
==========
- Website: https://decimals.app/sequence
- Community: [Decimals Slack](https://decimals.app)
- Documentation: [https://docs.decimals.app/](https://docs.decimals.app/)

<img alt="Sequence" src="https://decimals.app/dist/images/sequence.png" width="600px">

Sequence is an API that store validate and reports asset movements. Also known as a Ledger. Sequence is immutable, scalable, and easy to use.

The key features of Sequence are:

- **Currency Agnostic**: Store, move and analyse any asset, from regular currencies like USD, to shelf items. 

- **Simple API**: Sequence creates things as you use them. When you send value to an account, it is gets created if it does not exist. It gets out of the way. use it, and things will work.

- **Immutable**: Most existing ledger use database updates. This is bad for a ledger. Sequence is immutable. The design of the database allows for consistency of balances without a single field using updateds.

- **No-SQL database**: Sequence runs on top of a No-SQL database. It is horizontally scalable from the 12-factors contianer to the persistency layer.

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

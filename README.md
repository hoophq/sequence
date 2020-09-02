# decimals

```
jq -ncM 'while(true; .+1) | {method: "POST", header: {"x-api-key":["decimals"]}, url: "http://localhost:8890/v1/transactions", body: {amount: ., from: "Bob", to: "Alice", currency: "usd"} | @base64 }' | \
  vegeta attack -rate=50/s -lazy -format=json -duration=30s | \
  tee results.bin | \
  vegeta report
```

FIXME

## Getting Started

1. Start the application: `lein run`
2. Go to [localhost:8080](http://localhost:8080/) to see: `Hello World!`
3. Read your app's source code at src/decimals/service.clj. Explore the docs of functions
   that define routes and responses.
4. Run your app's tests with `lein test`. Read the tests at test/decimals/service_test.clj.
5. Learn more! See the [Links section below](#links).


## Configuration

To configure logging see config/logback.xml. By default, the app logs to stdout and logs/.
To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html).


## Developing your service

1. Start a new REPL: `lein repl`
2. Start your service in dev-mode: `(def dev-serv (run-dev))`
3. Connect your editor to the running REPL session.
   Re-evaluated code will be seen immediately in the service.

### [Docker](https://www.docker.com/) container support

1. Configure your service to accept incoming connections (edit service.clj and add  ::http/host "0.0.0.0" )
2. Build an uberjar of your service: `lein uberjar`
3. Build a Docker image: `sudo docker build -t decimals .`
4. Run your Docker image: `docker run -p 8080:8080 decimals`

### [OSv](http://osv.io/) unikernel support with [Capstan](http://osv.io/capstan/)

1. Build and run your image: `capstan run -f "8080:8080"`

Once the image it built, it's cached.  To delete the image and build a new one:

1. `capstan rmi decimals; capstan build`


## Links
* [Other Pedestal examples](http://pedestal.io/samples)

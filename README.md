# Requirements Engineering 2020 - Developer Team

This project uses Quarkus, Ktor and Traefik.
We realized a currency convertion - API with two microservices and one mainservice.

The project contains three components:
* <b>Mainservice:</b> Quarkus-Application calling the microservices
* <b>Wahrungsrechner-service:</b> Ktor-Application calling the Wahrungsrechner-API
* <b>Alphavantage-service:</b> Quarkus-Application calling the Alphavantage-API 

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .\ 
If you want to learn more about Traefik, please visit its website: https://docs.traefik.io/ .\
If you want to learn more about Ktor, please visit its website: https://ktor.io/ .

One microservice is calling the Alphavantage-API (docs: https://www.alphavantage.co/documentation/) and returns JSON
following the scheme below.

The other microservice is calling the Wahrungsrechner-API (docs: https://wahrungsrechner.org/api) and returns JSON
following the scheme below.

The mainservice calls tries calling the Alphavantage-Service and if this fails, it calls the Wahrungsrechner-service.

<pre><code>{
    "result": {
        // DATE
        // timestamp when the value was fetched (optional)
        "updated": "2020-06-25T22:59:52",
        // STRING
        // 3-char string identifier for base currency
        "fromCurrency": "EUR",
        // STRING
        // 3-char string identifier for convertion currency
        "toCurrency": "USD",
        // DOUBLE
        // Value of 1 fromCurrency
        "value": 1.12246,
        // DOUBLE
        // Quantity of fromCurrency
        "quantity": 1.0,
        // DOUBLE
        // calculated quantity * value
        "amount": 1.12246
    }
    "error": {
        // INT
        // 3-4 digit error code, defined lateron (network problems, internal problems etc..)
        "code": 999
        // STRING
        // Custom message returned by our service
        "message": "Put your custom message here!"
    }
    // ENUM-STRING (OK, FAIL)
    "status": "OK" // or "FAIL"
    // ENUM-STRING (Alphavantage, Wahrungsrechner, Mainservice)
    "source": "Alphavantage"
}</code></pre>

# Installation Guide
Requirements:
* Docker
* Git
* IntelliJ
* Maven
* JDK 1.8 +

1. Clone the master branch of this repository.
2. Open the project in IntelliJ.
3. Run mvn package inside the terminal. (normally this step could be skipped but we found some issues with the automatic image-building so this command assures the correctness)
4. Run docker-compose up inside the terminal. (the docker-images should be created automatically if not present and Traefik should start running)

Additional information:
When customising something inside an application/service it is necessary to follow these steps:
1. Shutdown Docker compose via docker-compose down.
2. Remove the corresponding image using docker rmi [container id].
3. Follow the steps above from point 3.

To start multiple instances of a service run in Terminal: 
    
docker-compose up --scale [service-name]=[number of instances] --scale [otherServiceName]=[number] --no-recreate

# How to use
Open a browser and open the following (usually only the Mainservice is called and redirecting):
* Mainservice: localhost/currencies/[path]
* Alphavantage: localhost/service/av/[path] - only for debug purposes
* Wahrungsrechner: localhost/service/wr/[path] - only for debug purposes

##### [path]-scheme: /[from_currency]/[to_currency]/[quantity]?/debug?/[debug_param]?/[affected_service]?
[from_currency]: 3 digit code of the base currency (e.g. "EUR", "USD"...)\

[to_currency]: 3 digit code of the convertion currency (e.g. "EUR", "USD"...)\

[quantity]: quantity of base currency that should be converted as Double (e.g. 1.5, 5.60...)(OPTIONAL)\

[debug_param]: debug param to simulate the application not answering/ after a while (OPTIONAL), this has to follow after path /debug/:
* fixed - Let the service sleep for 5 seconds
* random - Let the service sleep for a random time (between 0.001 and 10 seconds - can be adjusted in class TRunner in both microservices)
* noresponse - Let the service sleep for 100 seconds

[affected_service]: When passing a debug parameter, specify which microservice should be affected by it: (OPTIONAL)
* av
* wr
* both = standard value

### Examples
localhost/currencies/EUR/USD/2.0\
localhost/currencies/EUR/USD\
localhost/currencies/EUR/USD/debug/noresponse\
localhost/currencies/EUR/USD/2.0/debug/random\
localhost/currencies/EUR/USD/debug/random/noresponse/av\
localhost/currencies/EUR/USD/4.0/debug/random/noresponse/av\

# Approach
We started with implementing the microservices, with respect of the JSON-scheme defined in advance. Following that, we concentrated
on creating a Docker-container to run one service at a time with Docker. After reaching this point, we started implementing the mainservice
and added the docker-compose.yml. After a lot of configuration-testing and trouble-shooting we got it running to the current state.

When making a call to the mainservice, the service is trying to reach an instance of the Alphavantage-service, when none is reachable, he falls
back to an Wahrungsrechner instance, if none is reachable, the service returns a failed json-object.

# Possible improvements
* More tests
* More performance tests and quality assurance (testing came a bit short)
* Error Handling (more information)
* Better handling of the microservices (possible approaches: equal balancing, call both and check for latest timestamp...)
* Handle wrong path-requests to microservices
* Fallback when all microservices fail (save latest fetched rate and give this back, don't handle anything -> decision is not in dev-hand)
* currently "mvn package" has to be run manually, there were unexplainable issues having the command inside the Dockerfile.

# Lessons learned
* Understanding of Docker & Docker-compose and the concept behind it
* Basic configuration of docker-compose + docker (this was a very long process...)
* Configurate and use container orchestration with traefik to handle all services.
* Maven shows sometimes a realy strange behavior
* Migration from Maven to Gradle could cause trouble
* Preferably use one programming language (JAVA or Kotlin) to distribute tasks better
* There have been no problems working with different programming languages in the project

# Recommendations for Requirements-Engineering-Team (post but still important)
* Alphavantage is not the best case with free API-Key, we need a better one
* Loadbalancing is possible by using health checks from Traefik
* We need a special rule to decide, which service is preferred( which one will be the first...) because we could not achieve equal balancing (so we gave the priority to alphavantag)
* Each Microservices with different rate providers will also transmit a different exchange rate. We have to handle that in the right way, if not it can cost us a lot of money, because we calculate the Currency-Exchange-Rate with the wrong or not optimal price fo both, the customer and us.
-> possible solution could be to ask both services and use the latest refreshed rate (approach has to be defined by product owners/requ team)
* We test a configuration with about 70 mircoservices on our Workstation with a Xeon-E5 Quad Core CPU on 32 GB of RAM. If more microservices are needed a Server with more power should handel this.
* More Developers required

# Debug notes
* To enable Traefik-logging to follow the routing and get information about unhealthy services, uncomment --log.level=DEBUG inside the gateway service commands of the docker-compose.yml in the root folder
* If there is trouble with running, try the following:
1. run "docker-compose down" inside the Terminal
2. run "docker images" inside the Terminal
3. run "docker rmi <image-name> <image-name>..." to remove the images of all the services
4. Follow the Installation Guide above
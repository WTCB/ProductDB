# Web service for the Gyproc productDB

A web service is built to make the Gyproc DB accessible via a REST interface.

A live version of the web service is available at: https://pi.pauwel.be/APIs/Gyproc/ws/products (IP: 68.183.163.90).

## Build, run, and deploy the web service
The web service is built using Spring Boot. The web service is built directly on the database of Neo4J, which is currently a Neo4J database (as part of this proof of concept). This database is hosted at http://206.189.75.254:7474/. 

You can build, debug the web service locally, leading to an interface at http://localhost:8080/.

Deploying happens using maven. An executable JAR is built using 

`mvn clean package`

Then copy the resulting JAR file to the server using:

`scp target/gyproc-API-0.1.0.jar root@68.183.163.90:/`

Finally, you can run the JAR file in the server using:

`java -jar gyproc-API-0.1.0.jar`

## To make the request you can use:
* Your web-browser: copy the link in the address bar
* Postman: making a _"GET"_ request
* Linux Bash: using the command _"curl"_

## The usages are:
* _URL_
* _URL_/products : display all products





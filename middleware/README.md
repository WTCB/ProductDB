# Web service for productDB

A TechDat web service is built, aiming to provide access to all product data in various product databases. This web service holds the addresses of the diverse servers, and allows to query each or a combination of these databases for product data. All this happens over HTTP requests.

A live version of the web service has been deployed on Heroku at : https://fast-shelf-85138.herokuapp.com

A more recent live version of the web service is available at : http://pi.pauwel.be/TechDat/ws/ (IP: 68.183.163.90).

## Build, run, and deploy the web service
The web service is built using Spring Boot (see example: https://spring.io/guides/gs/accessing-neo4j-data-rest/). The web service is built directly on a number of databases. One such database is the neo4j database which is hosted at http://206.189.75.254:7474/ (http://pi.pauwel.be/productDB1/). These IP addresses and product databases will eventually be hidden, to be accessed only through the web service. Other databases need to be made available still. The web service provides methods to query any number of those databases.

You can build, debug the web service locally, leading to an interface at http://localhost:8080/.

The web service is also deployed on a Digital Ocean droplet, available at: http://pi.pauwel.be/TechDat/ws/ (IP: 68.183.163.90). Deploying happens using maven. An executable JAR is built using 

`mvn clean package`

Then copy the resulting JAR file to the server using:

`scp target/techdat-0.1.0.jar root@68.183.163.90:/`

Finally, you can run the JAR file in the server using:

`java -jar techdat.jar`

## To make the request you can use:
* Your web-browser: copy the link in the address bar
* Postman: making a _"GET"_ request
* Linux Bash: using the command _"curl"_

## The usages are:
* _URL_
* _URL_/subject : display 20 products
* _URL_/subject/search : display the name of the research method for each property
* _URL_/subject/search/findBy_PropertyName_?name=_yourInput_ : display the products that have _yourInput_ as value for the attibute _propertyName_.

With URL being http://pi.pauwel.be/TechDat/ws/, http://68.183.163.90:8080/, or http://localhost/.

Examples:
* http://68.183.163.90:8080/subject
* http://68.183.163.90:8080/subject/search/findByIntrastatNummer?name=68091100






# Web service for productDB

You can access to the data hosted on GrapheneDB throwing HTTP requests.
The link of the app deployed on Heroku is the following : https://fast-shelf-85138.herokuapp.com

It has been deployed on a free version of Heroku, don't worry if it is a bit long to answer.

## To make the request you can use:
* Your web-browser: copying the link in the address bar
* Postman: making a _"GET"_ request
* Linux Bash: using the command _"curl"_

## The usages are:
* _URL_
* _URL_/subject : display 20 products
* _URL_/subject/search : display the name of the research method for each property
* _URL_/subject/search/findBy_PropertyName_?name=_yourInput_ : display the products that have _yourInput_ as value for the attibute _propertyName_.




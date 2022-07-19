# Library ShowCase

This showcase illustrates how to implement a simple library api.

## Get Started
There are two ways to start the application. Either via docker compose (with a mysql database), or native (with a h2 in memory database).

* Spring-Profile: dev-compose
   1. Clone the repository
   2. Navigate with a terminal to the project root and run "docker compose up"
   3. Use the swagger doc (http://localhost:8080/api-docs/) to browse and test the API.

* Spring-Profile: dev-native
  1. Clone the repository
  2. Run the Main class with active profile 'dev-native'
  3. Use the swagger doc (http://localhost:8080/api-docs/) to browse and test the API.

## Get authenticated

Some endpoints are secured and must be accessed via JWT token - You can use the initial admin user to start using
secured endpoints.

Default credentials for admin are (can be configured via properties):

* email: admin@admin.de
* password: admin

1. Request a token with the
   endpoint http://localhost:8080/swagger-ui/index.html#/token-rest-controller/createAccessToken
2. Scroll up in the swagger doc and click the authorize button and enter the jwt value given in the response from the
   query above.
3. Now you can use all endpoints. Swagger will automatically include the JWT authorization header in the requests.

## Documentation

This showcase is documented via swagger based on the OpenApi definition.

* OpenApi definition can be accessed under: http://localhost:8080/api-docs/
* The swagger documentation can be found under http://localhost:8080/swagger-ui/index.html

# Development Testing

## Test Strategies

### WMS, SSO Auth & NFC Scanning

These three components will have a similar testing strategy. Since they are all external services we will have to create mock services for each service, this will allow us to predict the data being *received* by our system, since it will be hard coded into the mock service. This allows us to ensure that the data is parsed correctly by our system, we can do this by creating unit tests to ensure that the data output of the parser matches what we expect when given the data in the mock service.

We could also attempt to ensure that the requests made by our service are the same as what we are expecting them to be when *requesting a users loan history* for example.

### UI Testing
We will need to create tests for the UI to ensure that the app looks as we expect it to do, this will involve creating unit tests and also doing some manual testing. The unit tests will ensure that making a change to the UI does not affect another part without us knowing. We would also want to do standard user testing to ensure that the UI is easy to use and meets the requirements for accessibility, which are important to our client.


## Testing Frameworks
We will use JUnit to write out tests, these will then be run on Circle CI after each commit. This will ensure that all our components constantly checked after each addition.



## Challenges with testing

There will be several issues when testing, this is because a lot of our key functions rely on connections to external services. This makes it hard to test as we do not know the response of these external services until a request is made making it hard to check the output of a function is correct. 

To overcome this issue we will have to create an interface for each of these external services, then create two concrete classes which implement the interface:
- one will be a *live* class that will query the APIs, this will be used in the production app.
- one will be a *mock* class that will return dummy data as an api response. This will allow us to predict the outputs of the functions, therefore allowing us to check that they function correctly in the specific cases. Both classes will have identical functions to be accessed in the sam way.

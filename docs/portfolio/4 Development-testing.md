
# Development Testing

## Job Intent Services

We will use three different JobIntentServices, WMSNCIPService, AuthService and IMService. All of these services make api requests to either our server, or three different OCLC APIs. Collectively, they are responsible for obtaining all the data required by the application to function. Therefore, we must ensure that these services are tested rigorously, to ensure the app is able remains functioning. Although we are not able to mock these external APIs, we are able to create Request objects and then ensure that the requests made have the correct format and adhere to the correct set of rules outlines by the API provider (either ourselves or OCLC).

If the service is making a request to the server we would outline how data will be sent with the request and in what format, as well as, HTTP methods and any headers that are required. Once we have agreed the protcol required for communicating to the server we could then create a set of tests that would ensure any request we made adhered to this pre-set protocol, by doing this it would allow us to ensure the API Request created in the service would be as expected when it reached our server. We carried out a similar process when creating requests that would be made to the OCLC WMS API, inspecting their documentation, we were able to create some tests to ensure that any request made would be in the correct format with the correct data. By following this approach we were able to ensure that the API requests were of the correct specification for the API.

We will use JUnit for the testing of the requests, this will be supplemented by manual testing of the actual service, for reasons detailed below.

### Challenges

There were a few challenges the we faced when testing the Job Intent Services. Firstly, it is not possible to test an intent service directly, this point is made clear on the Android documentation. However, there is also a method provided to help overcome this limitation. We had to split the logic aspects of the service from the service into a separate testable class, and then call the logic from inside the service. This poses a challenge as we needed to deicide how much logic we wanted to extract from the service and how much should be kept inside. We decided that a good split would be to have classes that generated the requests needed by the service and the service was responsible to for adding those requests to the request queue for them to be actioned on. This meant we were able to test each test independently, whilst also keeping the service in control of what is executed. 

Secondly, we also faced issues working with the APIs themselves, since there was no sandbox environment, for us to test how to use the api before it was implemented. This meant that we had to be careful, when making requests to ensure that we kept track of what we did, so that it could be undone if it needed to be.


## NFC Scanning

We initialled attempted to contact Bilbiotheka for assistance with reading the tags, however after turning a blank, we had to workout how the data on the tags was encoded. This had many challenges, one of the key challenges was testing that any method for decoding the data on the tag had to be tested thoroughly. After working out how the data was encoded, we created tests with the example data we had accumulated from the manual decoding. These tests coud then be used as a basis for ensuring that our implementation of any decoding algorithm was correct.

### Challenges

Challenges related to RFID scanning were significant. It couldn't be tested using a JDK method and there did exist a method to create a mock tag within the Android OS however documentation on it was sparse, one source said it was depreciated. This lead us to using actual library books to test, using logs to manually make sure the data was being parsed correctly and the returned barcode was the same as printed in the book. For security, we observed what the difference for a book was when checking it in and out using the self service machines. We found that the AFI byte in the tag was changed and on one value the alarm went off and on the other it didn't. So we wrote to the tag from the phone to change the AFI value and observed that we could make it so it didn't set off the alarm. That would have been inconvenient for our users.

## Server
Main job was to take request from phone and wrap with extra permissions. Majority of testing will take place on the application, only need to check it executes requests correctly. If request is misformed its the applications fault. Unit Tests can be used to test key components such as generating headers but not everything. The API will be tested using the online API service WMS provides to work out how it works and what is need. This is then implemented in Java for the server. Before deployment the server is tested locally using cURL to simulate requests from the application and observing the results and adjusting where necessary.  

### Challenges

The server interacted with a live system that the library uses so we couldn't make too many requests to check out books. We had to control the checking in and out of specific books as to not inflict upon actual library users, we didn't have a sandpit to test. This meant unit tests to be run at each update would have not worked as it is not separate from the outside world and therefore API calls wouldn't always get the same results. It was just a test when changed, make sure it works and then leave it. 

## UI Testing
The User Interface is an important part of the application, so we must ensure that it is tested. We decided not to implement automated testing of the UI, this is because

We will need to create tests for the UI to ensure that the app looks as we expect it to do, this will involve creating unit tests and also doing some manual testing. The unit tests will ensure that making a change to the UI does not affect another part without us knowing. We would also want to do standard user testing to ensure that the UI is easy to use and meets the requirements for accessibility, which are important to our client.

### Challenges

As the UI evolved we found that many of the tests that we had origionaly created, were no longer relevant or had to be changed to 



# Continuous Integration

In our project, we opted to use Circle CI to allow for continuous testing of each commit made. This ensured that if a new commit failed some tests, it was noticed and informed us through a notification in our team chat on Slack. We also used Circle CI to manage our deployment. This meant that the application would only be deployed if all the tests were successful. For our releases, we have a system where tagging a commit with a version number would trigger a release to be created, with a release being added to GitHub with an apk added as an asset.

We also used a tool called Codacy. This tool analysed our code to ensure that the code we wrote was formatted correctly and up to a good standard. It was setup to check each pull request, this meant that when we merged our branches back into master, all our code was reviewed automatically. If Codacy found flaws in our code, such as inconsistent function/class naming or unused imports, it would comment on the pull request to inform us that the code was not up to standards and would detail what needs to be changed. Using this tool helped us to keep our code consistent among the team, whilst also making sure we did not write anything that would be error prone. 



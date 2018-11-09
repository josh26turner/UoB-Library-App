
# Development Testing

## Test Strategies
### NFC Scanning Class
To test this section we will need to create a mock `NFC API`, this will simulate an NFC tag being scanned. Once we have this we can test our `NFC Scanning Class`, using unit tests to ensure it works as we expect it to. 

### WMS Class
Again we will need to make mock services to simulate the APIs responses during testing. This will then allow us to create unit tests to test the `WMS Class` and esure it functions correctly

### SSO Auth Class
This will need mock services created to ensure that the `SSO Auth Class` returns the correct information about a user, if they attempt to login.

### UI
For this we will mainly test the UI by hand, we will also research how to create unit tests that allow us to ensure that the UI behaves as we expect it to.


## Testing Frameworks
We will use JUnit to write out tests, these will then be run on Circle CI after each commit.




## Challenges with testing

There will be several issues when testing, this is because a lot of our key functions rely on connections to external services. This makes it hard to test as we do not know the response of these external services until a request is made making it hard to check the output of a function is correct. 

To overcome this issue we will have to create an interface for each of these external services, then create two concrete classes which implement the interface:
- one will be a *live* class that will query the APIs, this will be used in the production app.
- one will be a *mock* class that will return dummy data as an api response. This will allow us to predict the outputs of the functions, therefore allowing us to check that they function correctly in the specific cases.



## Probably remove the nfc stuff
The core component of the system that we have chosen to look at is the scanning book capabilities. We have chosen to look at this as it is the most important part of the app, without being able to scan the NFC tags in the library books we would not be able to make the app. 

It is also crucial that this component functions exactly as specified, if it does not then we could causes issues with consequences for the library services such as:

- Breaking the tags on the book. This would mean that tags would have to be replaced causing extra work for the library staff.
- Not deactivating the security aspect of the tags correctly. This would cause alarms to be set off when students attempt to take the book out of the library
- We could accidentally overwrite the books tag, leaving all other library machines unable to scan the book. 


Test Cases:

- Scan books ID
- If book is eligible to be taken from the library:
   - Allocates book to user on OCLC
   - Writes to tag and deactivates security element (no other writes will occur)
      - This aspect can only occur if the book allocation was successful
- If the book is a reference book:
   - Inform user that they cannot take this book from the library
   - Do not write to tag
- If the book is not eligible to be allocated for any other reason:
   - Inform the user to take the book to customer service desk for more information


To enable this to be tested we will communicate with out client to get several testing tags setup, on the library system so that we can test each scenario.


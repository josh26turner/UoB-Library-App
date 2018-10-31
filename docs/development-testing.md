
# Development Testing

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



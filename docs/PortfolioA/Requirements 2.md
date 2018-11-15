Required User Flow
------------------

Requirement: **Scan book through the phone's built-in NFC Reader**
 
Importance: **Essential**
 
Once logged in, the system shall allow the users with a phone that has NFC Capabilities to be able to scan a book from the library once at the "Scan a new book" screen.

  * Once the phone is in near proxitimity, the RFID tag information from the book should be read.
  * The BookID should be retreived from the received RFID tag info.
  * A call should be made to the WMS Search API to retrieve the 'Book Details'.
    * If successful:
      * display the user 'Book Name', 'Author Name', 'Published Year' and 'Publisher Information'.
      * show the user a choice of four buttons: "Confirm", "Rescan Book", "Go Home" and "Problems Scanning?".
        * Case: "Rescan Book": 
           * retrieve previous activity, show previous parent activity & destroy this activity.
        * Case: "Confirm":
           * the WMS NCIP API will be called to tie book to account.
             * If successful: 
               * the book RFID Tag will be deactivated.
               * main activity retrieved from cache
               * update request sent to main activity.
               * main activity will be shown & this activity will be destroyed.     
             * If unsuccessful:
               * the appropriate error message will be displayed.
               * main activity retrieved from cache.
               * main activity will be shown & this activity will be destroyed.    
        * Case: "Go Home":
          * main activity will be retrieved from cache
          * main activity will be shown & this activity will be destroyed
        * Case: "Problems Scanning?": 
          * open & show problems scanning activity.
    * If unsuccessful:
      * the appropriate error message will be displayed.

NOTES:
=====
- The requirements cannot mention activity, this is to do with how the app is built and not how it must function.


Atomic Requirements
-------------------

1. The app must be able to scan the ISO-15693 tag inside the book.
1. The app must be able to read the book id off the ISO-15693 tag.
1. The app must ask the user if they would like to checkout the book.
1. The app must deactivate the security element of the tag, when the book is checked out.
1. The app must communicate with the OCLC API, to ensure the book is not already checked out.
1. The app must communicate with the OCLC API, to ensure the book is not a reference book.
1. The app must communicate with the OCLC API, to checkout the book against a user.
1. If the book is not available to checkout it must display a message asking the user to speak to a librarian.


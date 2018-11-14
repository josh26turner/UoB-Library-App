**Atomic Requirements:** 

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

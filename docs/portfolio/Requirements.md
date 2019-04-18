Stakeholders
------------

1. Students/Library Customers
    * Ease of use. Quicker than using the librarian and quick scan machines as can be done while walking out.
    * Saves those with social anxiety from having to communicate with library staff around.
    * Easy to access. Anyone with a phone can reserve a book or renew a book anywhere anytime.
    * Students who are not able to access the app may be at disadvantage when trying to reserve a book.

1. Librarians
    * They may need to explain how the app works. We may need to provide first-hand instructions of the logic of the app. We can however attempt to avoid this problem by adding tutorial upon first opening the app. 
    * It will shorten their workload. Librarians will only have to interact with those who do not have access to the app.
    * Number of librarians may be reduced.

1. Library service staff
    * They will be providing requirements. What function we need to implement in the app.
    * They will be in charge of distribution and providing maintenance.

1. IT Services
    * They will be in charge of distribution of the app on Google Play Store and providing maintenance and updates of future versions of the app.
    * They will need to support the use of SSO to allow users to login.

1. Customer Service representatives
    * They will have to provide technical support including explaining how the app works and direct users to the download page of the app. 

1. OCLC staff
    * OCLC is the api provider of the current library service. The app will need to be updated if there was any change to the api.


Use Case Diagram
================

![Use Case Diagram](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/use-case-diagram.png "Use Case Diagram")

Requirements
------------

Goal: To validate(check if users are allowed to take books), scan & loan books. (As self service machine)

The key objectives of our app are to be able to login and scan books, below we have designed the flow that is required for the app to function as we expect it to.

1. **Login**
   * Basic Flow:
     * Open Application [internet connection required]
     * Input email
     * Input password
     * Press "login". Retrieve user info from SSO login [query SSO]
     * Direct to app home page (Displayed in English) [query WMS]
   * Alternative Flow:
     * Redirect to forgot password page (handled by SSO) [at any point in the basic flow]
   * Exceptional Flow:
     * User has no internet connection
     * SSO is offline.
     * App will not work if WMS is offline, redirect to offline circulations info page.

1. **Scan book**
   * Basic Flow:
     * Open Sidebar
     * Press "Scan a New Book"
     * Physically put phone next to book
     * Confirm what is scanned on the phone matches the book
     * Press "CONFIRM"
   * Alternative Flow:
     * Press "RESCAN" if the information displayed and the book DO NOT match
     * Display information to visit the service desk if a not-for-loan(reference) book is scanned. [User Guidances]
     * Display information to visit the service desk if RFID tag is not present (book is not tagged). [User Guidances]
   * Exceptional Flow:
     * User has no internet connection
     * SSO is offline.
     * App will not work if WMS is offline, redirect to offline circulations info page.
     * Phone does NOT have NFC Capability. [therefore option is disabled]
     * Phone does NOT read the NFC Tag. [use "report" button]


These are some additional requirement flows that could be included in our app.

1. **Reserve book**
   * Basic Flow:
     * Open Sidebar
     * Press "Reservations"
     * Check current reservations status (books and the available dates)
     * Select options such as "cancel reservation"
     * Or search for a book
     * Press "Reserve" on book details screen
   * Alternative Flow:
     * User have reached maximum number of reservations/loans. Shows warning message why reservation failed
   * Exceptional Flow:
     * User has no internet connection
     * SSO is offline.
     * App will not work if WMS is offline, redirect to offline circulations info page.

1. **Check Current Loans (Homepage)**
   * Basic Flow:
     * Perform login as described
     * User directed to homepage which displays currents loans in chronologic order
   * Alternative Flow:
     * Perform login as described
     * User has unpaid fines, shows warning on homepage and can choose to be directed to payment webpage
     * User account is blocked, shows "account blocked" page, redirect to instrutions to visit service desk
   * Exceptional Flow:
     * User has no internet connection
     * SSO is offline.
     * App will not work if WMS is offline, redirect to offline circulations info page.

1. **Check Fines**
   * Basic Flow:
     * Red warning icon shown on top-right coner (previsional), icon shows amount of fine. Click icon to expand details
   * Alternative Flow:
     * Press warning icon. Then press "View Breakdown" (if API allows) for more information regarding their fines including how to pay
   * Exceptional Flow:
     * User has no internet connection
     * SSO is offline.
     * App will not work if WMS is offline, redirect to offline circulations info page.

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


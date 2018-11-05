Goal: To validate, scan & take out books. (As self service machine)

1. Login
  * Basic Flow:
		* Open Application [internet connection required]
		* Input email
		* Input password
		* Press "login" [query SSO]
		* Direct to home page [query WMS]
  * Alternative Flow:
		* Redirect to forgot password page [at any point in the basic flow]
  * Exceptional Flow:
    * User has no internet connection
    * SSO/WMS is down.

1. Scan book
  * Basic Flow:
    * Open Sidebar
    * Press "Scan a New Book"
    * Physically put phone next to book
    * Confirm what is scanned on the phone matches the book
    * Press "CONFIRM"
  * Alternative Flow:
    * Press "RESCAN" if the information displayed and the book DO NOT match
  * Exceptional Flow:
    * User has no internet connection
    * WMS goes down
    * Phone does NOT have NFC Capability. [therefore option is disabled]
    * Phone does NOT read the NFC Tag. [use "report" button]

1. Reserve book
  * Basic Flow:
    * Open Sidebar
    * Press "Reservations"
    * Press "Make New Reservation"
    * Search and select the required material
  * Alternative Flow:
    * "Make New Reservation" is greyed out as takeout limit reached
    * User cancels another reservation and proceeds
  * Exceptional Flow:
    * User has no internet connection
    * WMS goes down

1. Check Current Loans
  * Basic Flow:
    * Open Sidebar
    * Press "Current Loans"
  * Alternative Flow:
    * N/A
  * Exceptional Flow:
    * User has no internet connection
    * WMS goes down

1. Check Fines
  * Basic Flow:
    * Open Sidebar
    * Press "FINES"
  * Alternative Flow:
    * User can press "View Breakdown" for more information regarding their fines
  * Exceptional Flow:
    * User has no internet connection
    * WMS goes down

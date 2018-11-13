Goal: To validate(check if users are allowed to take books), scan & loan books. (As self service machine)

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

1. **Reserve book**
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

1. **Check Current Loans**
   * Basic Flow:
     * Open Sidebar
     * Press "Current Loans"
   * Alternative Flow:
     * N/A
   * Exceptional Flow:
     * User has no internet connection
     * WMS goes down

1. **Check Fines**
   * Basic Flow:
     * Open Sidebar
     * Press "FINES"
   * Alternative Flow:
     * User can press "View Breakdown" for more information regarding their fines
   * Exceptional Flow:
     * User has no internet connection
     * WMS goes down

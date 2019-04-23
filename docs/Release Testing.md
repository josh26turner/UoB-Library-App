#**Release Testing**

- **Release 1.0.0**
    1. Menu button opens menu.
    1. Menu button closes menu.
    1. Scrolling works on the Loans Fragment 
    1. Scrolling works on the Reservations Fragment
    1. Scroll position is kept even when swiping across fragments.
- **Release 1.0.1**
    1. Check Application Icon displays correctly on the homescreen.
    1. Check Username displays correctly on the sidebar. 
        - With Multiple Screen Sizes.
        - With names exceeding bounds of the sidebar.
    1. Check Loan/Reservation/Overdue status displays correctly within each loan container.
- **Release 1.0.2**
    1. Correct option showing the current page is shown on the sidebar.
    1. Display the correct barcode when a book is scanned.
    1. Display the security byte when a book is scanned.
- **Release 1.0.3**
    1. Check the confirm pages opens when a book is scanned.
    1. Check application splash-screen displays correctly
        - on multiple screen sizes.
        - in Landscape & Portrait modes.
    1. Check sample login page displays correctly
        - on multiple screen sizes.
        - in Landscape & Portrait modes.
- **Release 1.0.4**
    1. Check added WMSHold tests pass.
    1. Check "No Loans" is displayed in the centre of the page when there are no loans 
        - in Landscape & Portrait modes.
    1. Check "No Reservations" is displayed in the centre of the page when there are no reservations.
        - in Landscape & Portrait modes.
- **Release 1.0.5**
    1. Check to see whether their live loans are shown to the user.
    1. Check to see whether their live reservations are shown to the user.
    1. Check pressing an active loan opens a dialog showing the book, publisher and date due back.
- **Release 1.0.6**
    1. Swipe to refresh refreshes the page
        - Open the app, checkout a new book & swipe to refresh hoping the new book appears.
    1. Splash-screen displays correctly on smaller displays.
    1. Security tag is disabled when a book is scanned through the app.
- **Release 1.0.7**
    1. Dashboard is shown when the user opens the app.
    1. Pressing the loans cardview in dashboard redirects to the loans fragment.
    1. Pressing the reservations cardview in dashboard redirects to the reservations fragment.
- **Release 1.0.8**
    1. The different sorting functions correctly sort the list of loans.
    1. Sorting is kept in place when a refresh occurs and a new book is added.
    1. Pressing the back button twice in the dashboard will exit the app.
    1. Pressing the back button during the login process with return to the start of the login.
- **Release 1.0.9** 
    1. Automatic update is triggered in 10 minute intervals.
    1. Dashboard displays the correct number of days remaining of the nearest book due.
    1. Dashboard displays the correct of maximum number of books the user can checkout.
- **Release 1.1.0**
    1. NFC fragment does not open if the user does not have NFC.
    1. NFC fragment does not open when user is not connected to the internet.
    1. NFC fragment redirects to the NFC Settings if the user has NFC disabled.
    1. NFC fragment redirects to the Network Settings if the user is not connected to the internet.
    1. NFC fragment does not open when the user is connected to a network but not to the internet.
- **Release 1.1.1**
    1. Scanning 'confined to library' material will fail checkout.
    1. Scanning 'confined to library' material will not disable security.
    1. User denying access to NCIP Service after login will output a message stating access is required.
    1. Denying access to NCIP 3 times redirects to the start of the login process.
- **Release 2.0.0: LIVE Checkout!**
    1. Logout redirects to the login and removes previous users data.
    1. Scanning a book will wail for confirmation from the API before disabling the security tag.
    1. Live checkout implemented, if a book scan is successful, the book security is disabled.
- **Release 2.0.1**
    1. If a loan is due today, the loans container will say due today.
    1. If a loan is due tomorrow, the loans container will say due tomorrow.





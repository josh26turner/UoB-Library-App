# App Maintenance Guide

Within the app there are several constants that must be set. These are stored in the `app/src/main/java/spe/uoblibraryapp/Constants.java` file.

The constants file is split into 3 sections:

1. Library Details
2. User Authentication Details 
3. Server Details

## Library Details

This section contains all information about the library. 

- The `institutionId` is the identifier for the library
- The `borrowerCategories` is a map of borrower category to the maximum number of loans they are allowed to have. If the university adds a new borrower category, it must add it here. To add a new borrower category add a new line at the end that contains `,{"Category name", x}` where x is the number of books that the category is allowed to checkout. 
- The `libraryBranches` is a map from the `branchId` to the name of the branch. All library branches should be added here. To add a new branch add a new line at the end that contains `,{"branchId", "Branch Name"}`

## User Authentication Details

This sections contains details about how the user is authenticated through OAuth flow. These details should **not** be changed frequently.

- The `clientId` is the Client part of a WSKey. 
- The `redirectUrl` is the same as the redirect url that is set on the WSKey
- The `authFailureUrl` should not be changed.
- The `scopes` are all the services that the Client key has permission to use. `WMS_NCIP` and `refresh_token` and critical for the app to function correctly and should **not** be removed. New scopes can always be added.

## Server Details

This section contain the address for the server.

- The `serverLocation` is the url of the sever. If the domain name is changed the this must be set to the new domain name.


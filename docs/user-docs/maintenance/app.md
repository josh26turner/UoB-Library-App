# App Maintenance Guide

Within the app there are several constants that must be set. These are stored in the app/src/main/java/spe/uoblibraryapp/Constants.java file.

The constants file is split into 3 sections:

1. Library Details
2. User Authentication Details 
3. Server Details

## Library Details

This section contains all information about the library. 

- The `institutionId` is the identifier for the library
- The `borrowerCategories` is a map of borrower category to the maximum number of loans they are allowed to have. If the university adds a new borrower category, it must add it here. 
- The `libraryBranches` is a map from the `branchId` to the name of the branch. All library branches should be added here.

## User Authentication Details


## Server Details



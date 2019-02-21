[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b47afb3862274715927ada24e1bc5855)](https://app.codacy.com/app/RileyEv/UoB-Library-App?utm_source=github.com&utm_medium=referral&utm_content=josh26turner/UoB-Library-App&utm_campaign=Badge_Grade_Dashboard)
[![CircleCI](https://circleci.com/gh/josh26turner/UoB-Library-App.svg?style=svg)](https://circleci.com/gh/josh26turner/UoB-Library-App)
# UoB-Library-App 

## Main parts to the system

1.  UCard scanning
2.  NFC scanning
3.  API connection
4.  GUI and overall system

University of Bristol Library Services

Mobile apps projects

University of Bristol Library Services comprises 9 libraries and 4 study centres. We operate across the print and digital environments, with a print book collection of approximately 1 million titles and an equally large volume of digital content. Library Services supports all students and staff at the University of Bristol.

Key central library services are managed by a library management system. This is currently being replaced with a new library services platform which will go live in January 2019. This new service, WorldShare Management Services (WMS) will be supplied as a software as a service solution by the supplier OCLC. WMS will also provide a new discovery service to find, locate and access all library materials, both print and digital. This will replace the current Library Search service.

As part of the project to migrate to WMS, Library Services is keen to develop a suite of apps to support interactions with our services and collections. These apps would significantly enhance the user experience of our activities and enable us to deliver new and enhanced services to our users.
The apps suggested for development through this project will be created to work with WMS. Access to WMS will be provided by Library Services staff. In addition OCLC has a Developer Network (https://www.oclc.org/developer/home.en.html) and a published directory of currently available APIs (https://platform.worldcat.org/api-explorer/apis).

The following are some of the mobile apps that we are keen to develop. We would welcome the opportunity to work with University of Bristol students to develop these. We believe that this would be both an excellent learning experience for students but would also allow Library Services to work in the co-design of future service offerings.

1.  Mobile self-issue app
    The aim is to develop a mobile app that would allow library users to issue print materials to themselves via their mobile device. This would take advantage of technologies available by the library and by mobile phone manufacturers.
    Library materials are currently issued to users on dedicated stand-alone workstations within our libraries. These machines read the user Ucard to identify the borrower and the RFID tag inside each book to identify the item. The kiosk then communicates with the core library system via the SIP2 protocol to update the borrower record on the central system and de-sensitise the security setting on the RFID tag in the book.

    Moving this operation to the users own mobile device would require that the near field communications (NFC) functionality on the device is used to interact with the RFID tag which would then, in turn, interact with the central system via SIP2.
    The output of this project would be a mobile app that is able to carry out these tasks of reading the borrower Ucard, the RFID tag, and communicating back to the central system. The user experience would be that of a significantly enhanced and flexible service for issuing books to themselves.

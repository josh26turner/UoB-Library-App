# UoB-Library-App

Four main parts to the system:
------------------------------
1. UCard scanning
1. NFC scanning
1. API connection
1. GUI and overall system

University of Bristol Library Services

Mobile apps projects

University of Bristol Library Services comprises 9 libraries and 4 study centres. We operate across the print and digital environments, with a print book collection of approximately 1 million titles and an equally large volume of digital content. Library Services supports all students and staff at the University of Bristol.

Key central library services are managed by a library management system. This is currently being replaced with a new library services platform which will go live in January 2019. This new service, WorldShare Management Services (WMS) will be supplied as a software as a service solution by the supplier OCLC. WMS will also provide a new discovery service to find, locate and access all library materials, both print and digital. This will replace the current Library Search service.

As part of the project to migrate to WMS, Library Services is keen to develop a suite of apps to support interactions with our services and collections. These apps would significantly enhance the user experience of our activities and enable us to deliver new and enhanced services to our users.
The apps suggested for development through this project will be created to work with WMS. Access to WMS will be provided by Library Services staff. In addition OCLC has a Developer Network (https://www.oclc.org/developer/home.en.html) and a published directory of currently available APIs (https://platform.worldcat.org/api-explorer/apis).

The following are some of the mobile apps that we are keen to develop. We would welcome the opportunity to work with University of Bristol students to develop these. We believe that this would be both an excellent learning experience for students but would also allow Library Services to work in the co-design of future service offerings.

1. Mobile self-issue app
   The aim is to develop a mobile app that would allow library users to issue print materials to themselves via their mobile device. This would take advantage of technologies available by the library and by mobile phone manufacturers.
   Library materials are currently issued to users on dedicated stand-alone workstations within our libraries. These machines read the user Ucard to identify the borrower and the RFID tag inside each book to identify the item. The kiosk then communicates with the core library system via the SIP2 protocol to update the borrower record on the central system and de-sensitise the security setting on the RFID tag in the book.

   Moving this operation to the users own mobile device would require that the near field communications (NFC) functionality on the device is used to interact with the RFID tag which would then, in turn, interact with the central system via SIP2.
   The output of this project would be a mobile app that is able to carry out these tasks of reading the borrower Ucard, the RFID tag, and communicating back to the central system. The user experience would be that of a significantly enhanced and flexible service for issuing books to themselves.

2. Push notifications
   The aim is to develop a push notification service that would push information to library users rather than relying on them to retrieve information from library systems and services. Two specific use-cases would be addressed:

   1. Use-case 1
      Push notifications for user transactions
      This app would develop notifications for library users about the status of items that they have on loan. For example, when an item that is out on loan is required by another library user this title is recalled on the central library system. The library user then receives a recall notification, currently an e-mail. The aim of this app would be to replace the e-mail notifications with push notification direct to the user’s mobile device. It is envisioned that this app would work based on Bluetooth beacons to transmit the push notifications.

   2. Use-case 2
      Push notifications for content alerts
      This app would develop notification for library users about digital materials that Library Services makes available in addition to print books. Specifically, this app will promote digital materials within the physical environment of the University of Bristol libraries. When a library user is situated within a specific location within a University of Bristol library, they will receive push notifications about additional materials available. For example, whilst browsing the shelves in the Arts and Social Sciences Library for books on modern European history they will receive notifications of additional digital materials available for the same subject area. It is envisioned that this app would also be based on Bluetooth beacons to transmit the notifications to users. 

3. Location guidance
   Geo location app
   This app would find simple location guidance to library users to find specific areas, or specific books, within the University of Bristol libraries. The app would give geo guidance to the shelf or area direct on the user’s mobile device. Library, collection, and shelf location information is carried within the core library system and this would be the source data for the development.

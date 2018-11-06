**Client:** University of Bristol Library Services.

The client predominantly provides access to a large number print materials (such of books and digital content) to the existing 18000+ University of Bristol members. Graduates, former staff, members of local societies as well as members of universities in the UK & Ireland part of the SCONUL Access Scheme can also apply for memberships and become members but these are subject to individual review. There is also visitor passes available however print materials cannot be issued to such members.

As part of a new upgrade going live in January 2019, the client will be upgrading their library management system to a new cloud based setup hosted by OCLC and managed by WMS. In addition to these upgrades, the client loves the idea of developing a quick and robust app for their existing members to self-issue print materials. This would negate the need for existing members to use the librarian or the self service machines to take out materials as they would be able to do on the fly, with their phones via their fingertips.

Alongside issuing print materials to themselves, it is envisioned the application will provide push notifications which will allow the user to be alerted of over-due books, which will allow them to setup over-due reminders such as the day before or the morning when the book is due as well as notifying the users when their print materials have not been renewed. These new  notifications will replace the current system which is all currently done via email. This current method is not very efficient as not every user is proactive in checking their emails but with this new proposed design, users will be instantly alerted on their mobile phone/tablet.



Four main parts to the system:
------------------------------
1. UCard scanning
1. NFC scanning
1. API connection
1. GUI and overall system

University of Bristol Library Services comprises 9 libraries and 4 study centres. We operate across the print and digital environments, with a print book collection of approximately 1 million titles and an equally large volume of digital content. Library Services supports all students and staff at the University of Bristol.

WMS will also provide a new discovery service to find, locate and access all library materials, both print and digital. This will replace the current Library Search service.

1. Mobile self-issue app
    The aim is to develop a mobile app that would allow library users to issue print materials to themselves via their mobile device. This would take advantage of technologies available by the library and by mobile phone manufacturers.
    Library materials are currently issued to users on dedicated stand-alone workstations within our libraries. These machines read the user Ucard to identify the borrower and the RFID tag inside each book to identify the item. The kiosk then communicates with the core library system via the SIP2 protocol to update the borrower record on the central system and de-sensitise the security setting on the RFID tag in the book.

    Moving this operation to the users own mobile device would require that the near field communications (NFC) functionality on the device is used to interact with the RFID tag which would then, in turn, interact with the central system via SIP2.
    The output of this project would be a mobile app that is able to carry out these tasks of reading the borrower Ucard, the RFID tag, and communicating back to the central system. The user experience would be that of a significantly enhanced and flexible service for issuing books to themselves.

## High level Achitecture

![](includes/high-level-architecture.png "High level architecture")

The above picture provides an overview to the four main parts of the system, of which we will create 2. The WorldShare Management Services (WMS) APIs and Library Book systems are already in place and to be used by us to create the system. The Android Application and the Spring Boot Server (will be referred to as the server from now on in this document) are the part of the system to be created by us.  

The server will be an API for the Android Application, effectively acting a wrapper to make requests that a standard user cannot make, such as checking out a book which requires access to private keys that shouldn't be stored on a user's device.   

The Android Application is the way users will interact with our system, it will provide a method for scanning the RFID tags in the library books and checking them out. There will be three main packages: NFC, WMS and a main package. They will handle communicating with the RFID tag in the book, communicating with the WMS System API and handling the UI respectively.  


## External Systems  

The 2 main external systems that our app and server will intertact with are the RFID tag in the library books and WMS.  

The RFID tags are ISO 15693 tags that use the NfcV class in the Android SDK. The tags have a byte that says whether the book is checked in or out and contain the ID of the book that it is for. These are the main pieces of information that we need from the app. We read the ID from the tag and then, if it checks out successfully, change the security byte so it doesn't set off the alarm in the library.  

WMS has a series of APIs that use standard HTTP/S methods to invoke. These APIs will be used to login, check-out books, get user details etc. 


## Dynamic UML

![](includes/Dynamic-UML.png "Design UML")

This sequence diagram details the flow of data when a user logs in to the app as the data is passed around different parts of the system. The server is not used in this particular flow. When the app is loaded for a new user a web view of the login website will be loaded for the user to enter the username and password. The application never reads the username and password it just gets sent straight to WMS and the app records the access token granted to it. It then uses this access token to make further requests. When logging in, it will then use this access token to get details about the user, most notably what books they have checked out and reserved books. This is done by making a request to the WMS API for that data and parsing the response (usually in XML form, however some will be JSON) and then inserting the relevant data into the UI to display to the user.  


## Static UML

![](includes/Static-UML.png "Static UML")

This UML diagram details the layout and dependencies of the server. It gives a basic design as to what (non private) methods the different classes should have and where dependencies should link classes. It splits the classes into the two main tasks that the server will carry out: checkout and user authorisation requests. The two tasks have distinct purposes, but share classes and the diagram shows which classes need to be shared in a simplified way. It also splits the classes up into different logical layers, with each layer acting almost as an abstraction of the layer below it. This provides simpler method writing for layer above, which means it can cover a broad area of the task. It also showed details on how information about the university library will be shared around the different classes in the form of public access classes which store the data to be accessed from anywhere in the program.  


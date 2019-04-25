## High level Achitecture

![](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/high-level-architecture.png "High level architecture")

The above picture provides an overview to the four main parts of the system we will create. The WMS Systm APIs and Library Book systems are already in place and to be used by us to create the system. The Android Application and the Spring Boot Server (will be referred to as the server from now on in this document) are the part of the system to be created by us.  

The server will be an API for the Android Application, effectively acting a wrapper to make requests that a standard user cannot make, such as checking out a book which requires access to private keys that shouldn't be stored on a user's device.   

The Android Application is the main way users will interact with our system, it will provide a method for scanning the RFID tags in the library books and checking them out. There will be three main packages: NFC, WMS and a main package. They will handle communicating with the RFID tag in the book, communicating with the WMS System API and handling the UI respectively.  


## Dynamic UML

![](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Dynamic-UML.png "Design UML")

This diagram details the flow of data as a request to the WMS API is made to get the data on a particular user. The data would be loaded from the user when they sign in to the applicaiton. This part of the system is modelled as it shows well how the data will flow. It details what form it will be at the different stages in the flow and what parts need to be created and what they do. There will also be similar flows for the different calls to the WMS API and this design sets the precedent for the other API calls too. This requesting and response would be contained mostly within the big WMS box in the high level architecture diagram with the WMS API circle representing the same in the high level architecture. This show a higher level of abstraction of one part of the WMS Class in the high level architecture, more specifically this puts in more detail the handling of communication with the NCIP API for the WMS Service.  
  
## Static UML

![](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Static-UML.png "Static UML")

This UML diagram details the layout and dependencies of the server. It gives a basic design as to what (non private) methods the different class should have and where dependencies should link classes. It splits the classes into the two main tasks that the server will carry out: checkout and user authorisation requests. The two tasks have distinct purposes but share classes and the diagram shows which classes need to be shared in a simplified way. It also splits the classes up into different logical layers with each layer acting almost as an abstraction of the layer below it. This provides simpler method writing for layer above, which means it can cover a broad area of the task while being computationally simple. It also showed details on how information about the university library will be shared around the different classes in the form of public access classes which store the data to be accessed from anywhere in the program.  

## High level Achitecture

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/high-level-architecture.png "High level architecture")

The above picture provides an overview to the four main parts of the system we will create. The WMS Systm APIs and Library Book systems are already in place and to be used by us to create the system. The Android Application and the Spring Boot Server (will be referred to as the server from now on in this document) are the part of the system to be created by us.  

The server will be an API for the Android Application, effectively acting a wrapper to make requests that a standard user cannot make, such as checking out a book which requires access to private keys that shouldn't be stored on a user's device.   

The Android Application is the main way users will interact with our system, it will provide a method for scanning the RFID tags in the library books and checking them out. There will be three main packages: NFC, WMS and a main package. They will handle communicating with the RFID tag in the book, communicating with the WMS System API and handling the UI respectively.  


## Dynamic UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Dynamic-UML.png "Design UML")

This diagram details the flow of data as a request to the WMS API is made to get the data on a particular user. The data would be loaded from the user when they sign in to the applicaiton. This part of the system is modelled as it shows well how the data will flow. It details what form it will be at the different stages in the flow and what parts need to be created and what they do. There will also be similar flows for the different calls to the WMS API and this design sets the precedent for the other API calls too. This requesting and response would be contained mostly within the big WMS box in the high level architecture diagram with the WMS API circle representing the same in the high level architecture. This show a higher level of abstraction of one part of the WMS Class in the high level architecture, more specifically this puts in more detail the handling of communication with the NCIP API for the WMS Service.  
  
## Static UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Static-UML.png "Static UML")

This UML diagram details the layout and dependencies of the server. This simplifies the modelling of shared and separated classes. I chose this to model as it clearly lays out which classes can be shared and worked out a good way of getting methods required by multiple classes to layed out in an efficient, simple way. 

## High level Achitecture

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/high-level-architecture.png "High level architecture")

Each of the pairs of Activity Class and UI on the bottom two rows represent the different activities that will be in our application. The application will first open the "Login" activiy which will display the UI. Providing the login authenticates the application will then load the "Home" activity. This acts as the main homepage of the app upon login. Not included are in the diagram are the calls activities make loading a subsequent activity. But activities would contain buttons to load other activities when, and if, they are required. 

Above this is more the "backend" part of our application. There are 5 main APIs and 3 main classes. The WMS, NFC Scanning and SSO Auth classes act as middleware style classes between the front end and the APIs, this is to make the development more the system more modular and easier to divide into parts such that it can be developed by multiple people. The APIs, on the very top row, are all accessed solely by the relevant class, beneath them in the diagram, and output will be consistent throughout development so all classes don't have to be changed per update. 


## Dynamic UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Dynamic-UML.png "Design UML")

This diagram details the flow of data as a request to the WMS API is made to get the data on a particular user. The data would be loaded from the user when they sign in to the applicaiton. This part of the system is modelled as it shows well how the data will flow. It details what form it will be at the different stages in the flow and what parts need to be created and what they do. There will also be similar flows for the different calls to the WMS API and this design sets the precedent for the other API calls too. This requesting and response would be contained mostly within the big WMS box in the high level architecture diagram with the WMS API circle representing the same in the high level architecture. This show a higher level of abstraction of one part of the WMS Class in the high level architecture, more specifically this puts in more detail the handling of communication with the NCIP API for the WMS Service.  
  
## Static UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/portfolio/includes/Static-UML.png "Static UML")

For WMS{API}Response class it will implement the WMSResponse interface (the {API} is a generalisation, for the NCIP API it wouldbe WMSNCIPResponse). This interface will specify the standard structure that all responses from the WMS API should take. The implementation will change per API which is why an implementation is best suited. This means all the calls to the different WMS APIs can be written in a standard form too. It also extends the XMLResponse class so that the response can be parsed. The parse method is under a protected view so the WMSNIPResponse, and other response classes, should extend the class to access this method. It is protected so that overrides the method with a super call. 


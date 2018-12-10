## Dynamic UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/PortfolioA/Dynamic-UML.png "Design UML")

This diagram details the flow of data as a request to the WMS API is made to get the data on a particular user. The data would be loaded from the user when they sign in to the applicaiton. This part of the system is modelled as it shows well how the data will flow. It details what form it will be at the different stages in the flow and what parts need to be created and what they do. There will also be similar flows for the different calls to the WMS API and this design sets the precedent for the other API calls too. This requesting and response would be contained mostly within the big WMS box in the high level architecture diagram with the WMS API circle representing the same in the high level architecture. This show a higher level of abstraction of one part of the WMS Class in the high level architecture, more specifically this puts in more detail the handling of communication with the NCIP API for the WMS Service.  
  
## Static UML

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/PortfolioA/Static-UML.png "Static UML")

For WMS{API}Response class it will implement the WMSResponse interface (the {API} is a generalisation, for the NCIP API it wouldbe WMSNCIPResponse). This interface will specify the standard structure that all responses from the WMS API should take. The implementation will change per API which is why an implementation is best suited. This means all the calls to the different WMS APIs can be written in a standard form too. It also extends the XMLResponse class so that the response can be parsed. The parse method is under a protected view so the WMSNIPResponse, and other response classes, should extend the class to access this method. It is protected so that overrides the method with a super call. 


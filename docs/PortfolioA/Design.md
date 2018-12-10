## High level Achitecture

![alt text](https://github.com/josh26turner/UoB-Library-App/blob/master/docs/PortfolioA/High-level%20architecture.png "High level architecture")

Each of the pairs of Activity Class and UI on the bottom two rows represent the different activities that will be in our application. The application will first open the "Login" activiy which will display the UI. Providing the login authenticates the application will then load the "Home" activity. This acts as the main homepage of the app upon login. Not included are in the diagram are the calls activities make loading a subsequent activity. But activities would contain buttons to load other activities when, and if, they are required. 

Above this is more the "backend" part of our application. There are 5 main APIs and 3 main classes. The WMS, NFC Scanning and SSO Auth classes act as middleware style classes between the front end and the APIs, this is to make the development more the system more modular and easier to divide into parts such that it can be developed by multiple people. The APIs, on the very top row, are all accessed solely by the relevant class, beneath them in the diagram, and output will be consistent throughout development so all classes don't have to be changed per update. 

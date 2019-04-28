# Evaluation

For evaluation, we put our app to the test in the real world. We interviewed some frequent library users around the campus and gave them brief access to the app. We define frequent users as those who use the library more than twice a week and are already familiar with the current library checkout system. The tests took place in mixed locations such as libraries and study centres. 

As every student using the library has very similar goals, whether this be reading material in the library or taking materials away, we treated those interviewees as a representative sample. 1-in-6 of those students received a questionnaire where we asked for feedback on the navigation, feel and functionality of our product.

Each tester was given approximately 10 minutes of time and a list of tasks to complete such as logging in and out, check the current loans and reservations on their accounts, cancelling a reservation as well as checking out a book. We insisted on giving testers as little hint or guidance as possible in terms of navigation so we could study the intuition of our application.

For our purposes, written questionnaires wouldn't have provided us with as much feeling for how the users found the usage of the application. So we conducted informal interviews as it allowed us to ask testers for feedback and apply that feedback immediately, with follow up questions or extra hints.

## Interviews & Observations

We used release 1.0.6 and told thirty users, "This is a beta version of the new app we are developing for the UoB libraries" and gave them two tasks to complete so we could watch their interaction with our system. Task one asked the user to checkout a new book using the app and task two asked users to look around the app and tell us what functionality they could see the applications offers them.

From our observations, we found users were expecting to be able to click the cardviews in the dashboard. In the next release, 1.0.7, pressing the cardviews on the dashboard page redirected the user to the corresponding loans and reservations fragment. Additionally, three-in-five users seemed unclear on the functionality of the floating action which led us to change its icon from a camera icon to a customised NFC icon which had more familiarity of that of a book. 

Furthermore, all users had difficulty understanding how to checkout a new book once they located the *"Scan a new book"* page. This led us to adding a *"Show me how"* button at the end of the page which would display the instruction only when requested. This helped remedy the problem however when we re-sampled this change, a significant three-in-five users were still confused. Some users would use the phone as a barcode reader, some users as a camera and one user even put the book over the phone thinking it would pick up the weight of the book before actually reading the instructions.

We went away and created a powerful visual cue which replaced the un-necessary NFC logo and the *"Show me how"* button in the *"Scan a new book"* page by means of a two-stage animation. The first stage shows a book being open on the last page with the RFID tag and the second stage showing a phone being held over the tag. This animation was better able to deliver the message as the third sample of users had no difficulties checking out a new book. 

To conclude, through the use of interviews, we ensured our product meets every expectation of the end-user and rather than guessing, we delivered what was required.

## Results  

Results can be found here: https://github.com/josh26turner/UoB-Library-App/tree/master/docs/Testing/Testing%20Round%201

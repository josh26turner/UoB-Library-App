#How did we document the feedback we got.
##How did we use the feedback to refine the app 

# Evaluation

Questionnaires felt contrained for our purpose so we preferred conducting free-flowing informal interviews. We sourced fellow students studying in the university libraries, asked them to use our application then asked for their feedback and where possible made and showed the user those changes straight away. 

As every student using the library has very similar goals, whether this be reading material in the library or taking materials away, we treated those interviewees as a representative sample. 1-in-6 of those students received a questionnaire where we asked for feedback on the navigation, feel and functionality of our product.<sup>1</sup>

## Questionnaires

We asked one-in-six users three questions relating to the overall application: "how did you find process of logging into the application?", "do you find the application intuitive to use?" and "when the application is released to the public, will you make use of the app?" and two questions on specific design choices. <sup>1</sup> 

From our results, every user stated the process of logging in was simple, familiar and easy to do. This is good news and we suspect it was a result of us using the existing UoB SSO interface which other existing UoB applications use. As per the requirements, we kept the UI as simple to use as possible which meant a maximum depth of three layers and the confidence for the user to explore the application knowing the multiple presses of the back button will always get them back to the home dashboard page. We were very excited to know our efforts paid off with four-in-five users stating the application was very intuitive to use. The fifth user had a problem with the dashboard cardviews not being clickable but this was resolved in release 1.0.7 from their feedback.

Our biggest motivator for the use of a questionnaire was when our team was split when choosing between two very different application side-bar designs.<sup>2</sup> Surprisingly, all five-in-five sampled users preferred the left design stating it looked more modern and it looked more in-fitting with the university brand.

__Compare the side-bar designs as well..__ 

## Interviews & Observations

We used release 1.0.6 and told thirty users, "This is a beta version of the new app we are developing for the UoB libraries" and gave them two tasks to complete so we could watch their interaction with our system. Task one asked the user to checkout a new book using the app and task two asked users to look around the app and tell us what functionality they could see the applications offers them.

From our observations, we found users were expecting to be able to click the cardviews in the dashboard. In the next release, 1.0.7, pressing the cardviews on the dashboard page redirected the user to the corresponding loans and reservations fragment. Additionally, three-in-five users seemed unclear on the functionality of the floating action which led us to change its icon from a camera icon to a customised NFC icon which had more familiarity of that of a book. 

Furthermore, five-in-five users had difficulty understanding how to checkout a new book once they located the *"Scan a new book"* page. Despite instructions being clearly written, every user seemed to ignore this and get confused which led us to adding a *"Show me how"* button at the end of the page which would display the instruction only when requested. This helped remedy the problem however when we re-sampled this change, a significant three-in-five users were still confused. Some users would use the phone as a barcode reader, some users as a camera and one user even put the book over the phone thinking it would pick up the weight of the book before actually reading the instructions.

We went away and created a powerful visual cue which replaced the un-necessary NFC logo and the *"Show me how"* button in the *"Scan a new book"* page by means of a two-stage animation. The first stage shows a book being open on the last page with the RFID tag and the second stage showing a phone being held over the tag. This animation was better able to deliver the message as the third sample of ysers had no difficulties checking out a new book. 

To conclude, through the use of interviews and questionnaires, we ensured our product meets every expectation of the end-user and rather than guessing, we delivered exactly what was required.

## References:

1. eval_material_one.pdf
2. eval_material_two.png
3. eval_material_three.png
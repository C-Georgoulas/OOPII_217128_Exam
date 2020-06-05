# TRAVEL GUIDANCE SERVICE

#CONTENTS
- 1. USER USE INFORMATION
- 2. ADMIN USE INFORMATION
- 3. HOW TO RUN THE WEB-SERVICE APP.


#1.1 USER-INTRODUCTION

Travel Guidance Service is a web-service application which serves the purpose of suggesting specific destinations to a
user, after he has filled out specific personal information about himself, as well as filling out prefered destinations
and preferences regarding to his destinations (including but not limited to: interest in museums, cafe/restaurant/bars and so on).

Once the user has filled out the information, a well maintained and built algorithm is ran in the backgroung, comparing cities and their statistics in regards to the selected preferences filled out by the user, then, the algorithm returns a suggested
destination to the user that matches all criteria, at their best.

#1.1 USE

- Select a category of user's travel rights. (Travellers/Tourist/Business)
- Fill out name and age.
- Fill out current residence/city. **(Ensure the country code is correct!)**
- Select the prefered destinations.
- Fill out level of interest within Museums and Cafe/Bar/Restaurants. (Not allowed/taken into account if chose Business)
- Choice to filter out all cities with rain. (Not allowed/taken into account if chose Business).
- Receive suggested destination based on the information/criteria provided. (If Business, suggested destination is one closest to current city of residence, as in, the one closer to the user)

#1.2 USER-RIGHTS

- View the landing page.
- View the special offers page.
- Submit information/criteria and receive suggested destination.

#2.1 ADMINISTRATION-INTRODUCTION

Travel Guidance Service application allows users with administration rights to perform specific actions, view a list of users who have filled out information and used the application, but also allows to delete users off the database, as well as offer out free tickets to specific users through an algorithm that compares all users within a specific selected city and returns back the user that matches the best chosen criteria but also user-statistics, which in result provides him with a free ticket to that suggested destination.

#2.2 ADMIN-RIGHTS

- View all pages including Administration/Free Ticket
- View the database of all Travellers/Tourist/Business users.
- Delete Travellers/Tourists/Business through Administration.
- Select Travellers based on a specific city (same suggested/prefered destination) and run the selection algorithm for the free ticket.

#3 HOW TO RUN

The repository contains two project files: 
1. TravelGuidanceServiceFinal 
2. -Java3

#3.1 RUNNING FRONT-END

**TravelGuidanceServiceFinal** contains the client-side based application (frontend), alongside nodeJS, express and all the other frameworks that were used to build the front/client-side part of the application.

This can be ran by going into the *TravelGuidanceServiceFinal* folder and running the following command: 
**node app.js** through a terminal that can be accessed either on Visual Studio Code or by performing **cd** commands on your
own file browser until you navigate inside the *TravelGuidanceServiceFinal* folder.

Client-side is ran on http://localhost:5000/ which is the one you should access to use the application.

Requirements:
Being able to run node applications by having node installed https://nodejs.org/en/download/
Any working internet browser. (E.g Chrome/Safari)

#3.2 RUNNING BACK-END

**Java3** contains the server-side based application(backend), as in, Spring Boot which contains the initialization of MongoDB and it's collections, as well as each algorithm and collection/getter/setter (JAVA Principles) that were programmed in order for
the server-side application to work properly.

The back-end can be ran with two options:

1. Eclipse IDE (Recommended by this application's developers)
2. Visual Studio Code. (Configure on your own)

**To run through Eclipse IDE:**
**Need to have installed Spring Tools Suite STS(Spring Tools 4 (aka Spring Tool Suite 4))**


**If you do not have the STS plugin on your Eclipse IDE,you can find in the Eclipse's Marketplace(Help>Eclipse Marketplace)
by searching "Spring Tools 4 (aka Spring Tool Suite 4)"**


**After installing the plugin,restart the Eclipse IDE  and select the workspace that you have the Java3 folder.**

**Click on TravellerGuidanceService**
**Right click > Run as > Spring Boot App > Matching items(select): Java2Application - com.example.java2 > OK**

Tomcat web server is initiliazed on localhost 8080, which is something you do not require as you only want to
access the client-side of the application. (http://localhost:5000/ on any selected browser)

Requirements:
Have STS plugin (i.e SpringSource Tool Suite) installed within Eclipse IDE.


Node.js 


#3.3 CONCLUSION

You want to have both client-side and server-side running, you will have two localhosts running at the end. (5000 and 8080)

http://localhost:5000/ is the one you require, you also need to be running the backend.

Ensure to be running both hosts at the same time otherwise you will not get the result you are looking for!

~~https://github.com/users/MJDarling-web/projects/2/views/1
started plan! Started to put it in Github as issues but wanted a more central space to view and review upcoming tasks instead of github issues~~

# Indie Project Plan

- [x]Finish User Stories
- [x]Creat Plan for project
  - [x]Update technologies used in project
  - [x]Finish wire frames of screens
  - [x]Project 1 Check Point Ready for review

## creating project 
- [x]create necessary pom and configure tomcat accurate deployment  
- [x] Finish directory structure of project
- [x]Start JSP files
- [x] Update pom to have hibernate for deployment
- [X] List out DB tables needed
- [x] Start DAO 
- [x] Car Ownership Costs - Brainstorm further database tables

> Week 10 breakdown (don't break down)
-  _Monday_ [X] Plan the week because I'm very tired from restful weekend. 
- _Tuesday-started_ [ ] **Week 10** team project exercise (start Tuesday Videos in morning and exercise in night)
- _Wednesday_ [x] Team meeting at 430-530 start team project that night
- _Thursday Night_ [x] Team project work - mostly done w entities
- _Friday Night_ [ ] Checkpoint 3 chugg along for completion by Sunday night.
  * [ / ] Deployed to AWS - **week 7**, 
  * [x] at least one JSP that displays data from the database is implemented, 
  * [x] Authentication implemented - **Week 8**. Add link to your deployed application here.
- _Saturday_ [ ] **Week 9** RESTful API exercise

> Week 11 breakdown - Check point 3 (week 7), week 9, week 11
* [ ] _Sunday_ 4-5 hours 
* - [X] Last of Week 10 activity, 
  - [x] Review authentication material from week 7
  - [x] Exercise for week 7 

- _Monday_ 
  - [x] Exercise for week 7
  - [ ] TODOs for week 7 exercise   
  - [x] Authentication to project from week 7
  - [ ] Add new users to database after cognito
  - [ ] Submit check point 3
  - [ ] Start Week 9 Restful API material
- _Tuesday_
  * [ ] Finish and Submit group work material 
  * [ ] Indie Project - see suggestions below do 1 of them
  * [ ] Week 11 start
- _Wednesday_
  - [ ] Week 11 continue / indie project
- _Thursday_ 
  - [ ] Indie project - see suggestions below do 2 of them
- 
- 
> Indie project 
- [ ] check point 3

- **suggestions for module 3 to stay on time:**
- [ ] Create all entities and daos (remember to use a generic/abstract dao to minimize the number of daos you need to write)
- [ ] Display your entities on your jsps - essentially, have the data view pages complete, i.e. you should be able to view data (from the database) on your jsps 
- [ ] Begin implementing add/edit functionality 
- [ ] The site is professional-looking (CSS, Bootstrap or some other framework is implemented) 
- [ ] If you are using a public api/web service, call the service from your application to retrieve the data you need (you may not have the response mapped to objects yet, but you can at least see the data is correctly returned)
- [ ] Implement all changes based on peer and instructor feedback 
- [ ] Write unit tests for all non-entity/non-servlet classes. If your servlets perform significant bits of logic, put that code into testable classes or methods.


week 10
- [ ] week 6 AWS project upload 
- [ ] week 6 database display on awd and aws connection
- Week 7 & 8 material...
- Other...
- [ ] Finish JSP pages for Commuting Impact
- [ ] Finish service files for processing input data
- [ ] DAO test files
- [ ] check database to ensure proper configuration for displaying data
- [ ] check/update session for getting data from DAO for jsp/servlet pages to display correctly
- [x] Brainstorm and psuedocode database tables needed for project
- [x] Create database for user, transportation type, others?
- [x] Create Properties files
- [x] Create Hibernate file
- [x] Create Implement pom file
- [ ] Research API options further on Google Maps for distance and time of commute types and Google Graphs
- [ ] Create JSP pages for Commuting Impact

## entity files
- [ ] Outline entity classes need
- [x] write out and brainstorm psuedocode/javadocs of necessary entity classes
- [ ] finish persistence files
- [ ] build out controller/logic files
- [ ] create tests for project Daos
- [ ] Submit project for review
- [ ] Unit tests for entity files... create dump SQL file for Main and cleanDB.sql for testing.
- [ ] create entity classes

## Persistence files
- [ ] Outline persistence DAO java files needed for project
- [ ] write out and brainstorm psuedocode/javadocs for necessary DAO persistence classes.
- [ ] Unit tests for project... create dump SQL file for Main and cleanDB.sql for testing.
- [ ] Create persistence DAO files 

## Resources files
- [ ] create dump SQL file for Main and review cleanDB.sql for testing.
- [ ] Update, create/revise database.properties file
- [ ] implement hibernate configuration 
- [ ] Update pom file 

## Log in authentication
- [ ] 
- [ ] 
## upload to AWS--
- [ ] 
- [ ] 

## Java Entity classes to implement 

### Car Ownership Costs
- [ ] Task 1: Create a form where users can input car ownership costs (maintenance, insurance, fuel, etc.).
- [ ] Task 2: Validate user inputs and calculate total monthly car ownership cost.
- [ ] Task 3: Implement backend logic to store the calculated costs for comparison with alternative transport options.
- [ ] Task 4: Implement UI to display calculated ownership costs on the main page.
- [ ] Task 5: Write unit tests to ensure proper calculation of car ownership costs.

### Multiple Forms of Transport (Update and Change Inputs)
- [ ] Task 1: Implement a user interface allowing users to select multiple modes of transportation (e.g., car, bike, bus).
- [ ] Task 2: Implement dynamic cost calculators for each selected transport type.
- [ ] Task 3: Implement logic to update and change transportation inputs dynamically (e.g., if the user changes the distance).
- [ ] Task 4: Display the transportation cost comparisons side-by-side for the user.
- [ ] Task 5: Write unit tests for updating transportation types and recalculating costs.
Alternative Transportation Costs
- [ ] Task 1: Integrate with Google Maps API (or similar) to fetch public transit, taxi, and bike routes and calculate the distance and time for each mode.
- [ ] Task 2: Create a formula to auto-calculate the cost of alternative transportation based on inputs (e.g., cost per mile for biking, bus fares).
- [ ] Task 3: Allow users to input additional data for biking (e.g., bike maintenance cost) and walking (e.g., shoe cost).
- [ ] Task 4: Display alternative transportation costs alongside car ownership costs.
- [ ] Task 5: Write tests to ensure proper calculations for alternative transportation.

## Time and Health Impact
- [ ] Task 1: Implement a time tracking feature where users can log commute times for different transportation methods.
- [ ] Task 2: Calculate the time spent commuting for each transportation type and display it.
- [ ] Task 3: Integrate health-related data (optional) based on transportation type (e.g., calories burned for biking/walking).
- [ ] Task 4: Implement UI to display time and health impact data in a user-friendly way.
- [ ] Task 5: Write tests to ensure accurate tracking and calculation of time and health impact.

## User Logging
- [ ] Task 1: Create a log page where users can input and view their monthly commute details (mode of transport, time spent, cost).
- [ ] Task 2: Implement backend storage for user commute logs.
- [ ] Task 3: Allow users to view past logs and make updates to them.
- [ ] Task 4: Display the cumulative cost and time spent for the user’s commute over a period.
- [ ] Task 5: Write tests for the logging functionality.

## Future Impact Calculations - 1-Year, 2-Year, and 5-Year Projections
- [ ] Task 1: Implement a financial projection model based on current transportation costs.
- [ ] Task 2: Create a projection formula that factors in car depreciation, fuel price changes, and other variables for 1, 2, and 5 years.
- [ ] Task 3: Integrate Google Charts API to visually display the projections in a graph format.
- [ ] Task 4: Implement an interactive chart that updates based on user inputs (e.g., changing the cost of gas, car payment, etc.).
- [ ] Task 5: Write tests to ensure proper functionality of the projections, including edge cases (e.g., no data entered, incorrect data).


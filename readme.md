## Title: "My Commuting Impact: A Web App for Analyzing the True Financial Costs of Commuting"

**Problem Statement:**
In the United States, car ownership is often seen as a necessity, with many individuals purchasing or leasing cars based on their perceived need to drive, rather than fully understanding the financial implications. People generally focus on the upfront costs or loan payments, but overlook long-term expenses such as maintenance, insurance, fuel, and depreciation. Additionally, environmental and alternative commuting options are seldom considered in the financial equation. This leads to people making decisions that are not financially optimal or sustainable.

**Proposed Solution:**
"My Commuting Impact" is a web application designed to help individuals make informed decisions about their transportation choices by providing a comprehensive breakdown of the true financial costs of car ownership compared to alternative methods, including biking, public transportation, and walking. By offering clear and accessible insights into the real costs—both financially and in terms of time—users can better assess whether owning or maintaining a car is the best option for them.

_**Key Features of the App:**_

**Financial Impact Analysis**
Users can input their current transportation details (car ownership costs, public transit fees, or biking/walking expenses).
The app calculates monthly and long-term (1, 2, and 5 years) expenses of car ownership, considering factors like maintenance, loan payments, insurance, fuel, and depreciation.
Compares these costs with alternative transportation options (public transit, biking, and walking).

**Alternative Transportation Cost Estimator:**
The app will factor in the costs associated with using alternatives to cars: biking (maintenance, gear, calories burned), walking (time, calories burned, shoes), and public transportation (tickets, passes, or rideshare costs).
_Time-based comparison:_ The app will account for varying commute times for different modes of transportation, such as how long it takes to drive versus bike or take public transit, factoring in urban vs. rural differences.
**Impact Over Time:**
Displays projected financial impacts over different time periods (1, 2, and 5 years) to help users assess the long-term savings or costs associated with their commuting choices. - Uses a Google Charts API to create a graph of cost of ownership.
Encourages a mindset shift by highlighting the opportunity cost of car ownership in terms of potential savings.

**User Login and Personalized Data:**
Allows users to create an account and save their inputted information for future reference and comparison, as well as update that information if things change.
Users can track changes over time and adjust data inputs to reflect changes in their lifestyle or commuting preferences.
Users can input their monthly cost of commute and track how much their commuting choices are costing them.

**Goal:**
The main goal of "My Commuting Impact" is to empower individuals to make smarter, more informed decisions about their transportation habits. By clearly showing users the hidden costs of car ownership and comparing them to alternative methods, the app aims to foster a more sustainable and financially conscious approach to commuting, while encouraging exploration of environmentally friendly alternatives.

**Feature/s that might be added later to strengthen project:**
Integration with a google maps API to get the users commute data for each type of transportation method of driving, ride-share, biking, walking, bus. 

_Technologies used:
* JSP pages for front end
* Charts API (& TBD Google Maps API),
* Java for backend application development,
* MySQL for storing user info input data and transportation cost logs
* User Authentication
* AWS for hosting application
* Git and GitHub for version control
* Maven for dependency management and build automation
* JUnit for testing application

These technologies are well-suited for developing "My Commuting Impact" web app, covering everything from front-end user interface development to back-end server-side logic, data management, and deployment. Each technology is chosen based on its strengths in achieving key features: commuters analysis, data visualization, and user tracking._

A Payroll API DEMO By Lok Shun Tse

*** Instructions on how to build/run your application
Prerequisite:
- JAVA, Maven and Mysql

1. Clone the application
2. Create Mysql database
3. Change mysql username and password corresponding to the one when you first install/setup your MySQL database

- open `src/main/resources/application.properties`

- change `spring.datasource.username` and `spring.datasource.password` corresponding to the one when you first install/setup your MySQL database 

4. Build and run the app using maven


(For Junit testing......)
- Becareful of the file name/id. Same file name/id will return forbidden for test case as well.


*** How did you test that your implementation was correct?

I tested my implementation by using unit test and manual test respectively. For the unit test, I created a few cases by using JUnit to test whether the data sorting are correct.

For manual testing, I used Postman to test whether my API can be function correctly. I randomly generate some new data in excel and upload through API and check the response object as well as the mysql database.




*** If this application was destined for a production environment, what would you add or change?

The security of the API has to be improved. OAuth2, JSON Web Token, or JWT could be applied so that authorization would take place. Hence, only specfic paties can use the API to retrieve these sensitive information. Also, sensitive information in mySQL could be encrypted so that people cannot steal the data easily. Also, I would add more test cases such as to check the calculation of the total paid amount.

Also, the algorithm may not be the best. For example, I used a few Collections and mappings to do sorting and categorising. However, there is probably a way to do all the sorting and categoring in one mapping. However, I tried my best to make sure the O(n) performance is lower than O(n^2).

# CRUD-Gradebook

#### IDE - `Netbeans`

### CRUD-GradeBook-srv-mbommir1 - Server Application

- Just clean build and run

#### Assumptions :

1. Student list is already present. (No option to create them seperately)

#### Validation Assumptions :

1. A grade book entry is considered `duplicate` if the `grade item name` and `student id` matches with someother entry.

2. `grade item name`, `student ID` and `grade` are mandatory fiels to create grade book entries.

3. Allowing partial update. (if only `grade` is given in `Update` request, only `grade` will be updated. Other attributes won't change)

4. Accepts only JSON payload.



### CRUD-GradeBook-cli-mbommir1 - Client Application

- Just clean build and run

- For each of the action, the table and the other request information boxes will be filled accordingly.

#### Assumptions:

1. Server Host is `localhost:8080`. If this changes, `BASE_URI` in `FoodItemResource.java` should be changed accordingly in the client app.



### Methods and their Inputs
   
1. `CREATE` - to add a grade book entry. `grade item name`, `student ID` and `grade` are mandatory inputs.
       
	a. API - /gradebook     
	b. Possible Responses Codes	- 201/409/400    
	c. Response Type - JSON      
	d. Response	- created object if 201, error message for other response codes.  
     
	   
2. `GET` - get all the grade book entries. option to filter the list by student ID, grade item name and grade.
	    
	a. API - /gradebook    
	b. Filters Available - `grade_item_name`, `grade` and `student_id`  (eg. /gradebook?student_id=xx&grade=yy)   
	c. Possible Responses Codes	- 200   
	d. Response Type - JSON    
	e. Response	- array with zero or many grade book entries.    
	   
	   
3. `GET` - get a specific grade book entry.
    
	a. API - /gradebook/{id} 
	b. Possible Responses Codes	- 200/404/400
	c. Response Type - JSON
	d. Response - retrieved object if 200, error message for other response codes.
	   
	   
4. `PUT`	- update a specific grade book entry.
    
	a. API 	- /gradebook/{id}   
	b. Possible Responses Codes	- 200/404/400   
	c. Response Type - JSON   
	d. Response	- retrieved object if 200, error message for other response codes.   
	   
	   
5. `DELETE` - delete a specific grade book entry.
    
	a. API 	- /gradebook/{id}    
	b. Possible Responses Codes	- 200/404/400   
	c. Response Type - JSON   
	d. Response	- Successful deletion message if 200, error message for other response codes.   

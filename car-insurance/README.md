# Safety Car 

A web application to help customers request quotes and policies for vehicle insurance. The application simplifies the user experience by offering a friendly interface that allows you to easily manage your requests. It provides a comprehensive database of vehicle models and a quick response based on the client's details. The requests can be tracked and compared, and further developed into policy requests. The application is managed by insurance agents/ administrators who can then approve or reject the requests, with a feedback sent to the user's email address. 

## Class Hierarchy
Here is a little more detailed information about the possibilities of our web application.

- Each Quote request has:
    - vehicle information (registered vehicle):
        - make
        - model
        - model year
        - first registration date
        - cubic capacity (for non-electric vehicles)
	- driver age
    - user information
	- total sum (quote)
- A Quote request can then be developed into a Policy request by adding:
    - policy effective date
    - an image of vehicle registration certificate
    - only authorized users can request policy and their personal details are obtained from their user profile.
- The total sum for a Quote request is calculated dynamically, using the latest Premium reference values, and updated in real time.
- The total sum for a Policy request is calculated statically based on the price of the quote at the time of request.
- The total sum is calculated based on:
    - base amount
    - accident coefficient
    - driver age coefficient 
    - additional taxes;
- Each user has personal details like:
    - name
    - email
    - current address
    - phone number
    - profile picture
    - authentication and authorization details.
Each user can review and compare their requests and see the status of their Policy requests. They can cancel 'pending' requests before an agent has made a decision. 

## Access Control
Currently, the application has an authentication system with three types of user authorities: Anonymous, Regular User and Administrator.

- Anonymous users can only make Quote requests but will have to login if they wanted to proceed them further.
- Regular users can track their Quote requests and turn them into Policy requests.
- Administrators have access to all requests and user information and can edit and delete them.

## Restrictions
When requesting a Quote, users have certain required input parameters:

-	Vehicle make, model and model year – generated automatically from a dropdown menu.
-   Vehicle's engine cubic capacity (not applicable for electric vehicles) - must be an integer
-   Vehicle's first registration date - can not be earlier than model year.
-	Driver age – must be above 16 for the current country regulations.
-	Previous accidents – determines the price of the quote.

When requesting a Policy, registered users must have all personal information added in their user profiles. This includes:

- email address
- current address
- phone number

Users are also required to specify policy effective date and upload an image of vehicle registration certificate.

When registering a new user:
-	Username – required, must be a valid email address
-	Password – required
-	First and Last name – required, between 2 and 20 symbols
-	Phone number

Each user registered automatically has a Regular User role, which can be updated by Administrator to “Administrator”.

## Aditional Functionality

- Administrators can modify current premium reference values through an integrated table editor.
- Administrator can add new vehicle models to the datebase. (Application using USGoT API to obtain new models)
- User registration requires approval through email.
- Users will receive policy request feedback through email at approval/rejection of request.
- Users can track their requests and filter them by date.
- The application users an external API for image upload (imgur.com).

Application REST API documentation can be found at:
https://documenter.getpostman.com/view/12502055/TVYM4w3H

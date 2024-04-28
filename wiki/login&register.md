# Login and Registration

## Introduction

The login and registration guide for both our app and website! Describes the process of accessing your account and creating a new one.
Login and registration are fundamental features of our facebook app, allowing users to securely access personalized content and features.

Follow the step-by-step instructions below to seamlessly login to your existing account or register for a new one. Additionally, we'll provide insights into how authentication works behind the scenes and offer best practices for maintaining the security of your account credentials.

## Login
1. After running the react website as described in the setup guide, the following screen will appear:
  ![image](https://github.com/ilanitb16/Android-App/assets/97344492/14141327-3bec-4007-b4d3-de98ab48a63b)

2. If you already have an account, type in your username and password. Incorrect username or password will result in failure and access to the website will be denied.

![image](https://github.com/ilanitb16/Android-App/assets/97344492/01822d09-bea1-4134-a681-579151b9d450)

4. Successful login will grant the user access to the Hompage which allows to see 20 posts of friends (sorted by date) and 5 posts of other users (which are NOT the current user).
   ![image](https://github.com/ilanitb16/Android-App/assets/97344492/bca9fd4d-1e7d-4056-a53a-0036f3d14380)

After running the Android app, the following screen will appear:

## Registration
1. For registration, click on the "Sign up for Facebook" underneath the login page to get redirected to the registration page.

2. Follow the validation guidelines, making sure the fields all match the requirements. If these requirements are not met, login or registration will not be completed.
- All fields must be non-empty.
- Password must be at least 8 characters long.
- Password must contain both capital and small letters.
- Password must contain numbers.
- Password must contain at least one special character.
- Confirm password must match the password.
  
![image](https://github.com/ilanitb16/Android-App/assets/97344492/74ae6c16-91f2-43a0-af0b-31bdb19858cf)

## Authentication
Login Credentials: When a user attempts to log in, they provide their credentials, in our case a username and password combination.
Client-Server Interaction: Upon entering their credentials, the client (such as a web browser or mobile app) sends a login request to the server. This request contains the user's credentials in a secure format, often encrypted to protect sensitive information during transmission.
Server-Side Verification: Upon receiving the login request, the server processes the request by extracting the provided credentials. It then compares these credentials with the corresponding data stored securely in its database.
Authentication Result: Based on the comparison result, the server generates an authentication response. If the provided credentials are valid, the server generates a session token or cookie and sends it back to the client along with a success message. If the credentials are invalid, an authentication failure message is returned instead.
Authorization: Once authenticated, the user is granted access to specific functionalities of the app/website.
Logout: When the user chooses to log out, the session is terminated, and any associated session tokens or cookies are invalidated. This prevents unauthorized access to the user's account.

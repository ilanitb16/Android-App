# Login and Registration

## Introduction

The login and registration guide for both our app and website. Describes the process of accessing your account and creating a new one.
Login and registration are fundamental features of our facebook app, allowing users to securely access personalized content and features.

Follow the step-by-step instructions below to seamlessly login to your existing account or register for a new one. Additionally, we'll provide insights into how authentication works behind the scenes and offer best practices for maintaining the security of your account credentials.

## Login

### REACT
1. After running the react website as described in the setup guide, the following screen will appear:

2. If you already have an account, type in your username and password. Incorrect username or password will result in failure and access to the website will be denied.

3. Successful login will grant the user access to the Hompage which allows to see 20 posts of friends (sorted by date) and 5 posts of other users (which are NOT the current user).
  

### IMAGES
![image](https://github.com/ilanitb16/Android-App/assets/97344492/14141327-3bec-4007-b4d3-de98ab48a63b)
![image](https://github.com/ilanitb16/Android-App/assets/97344492/01822d09-bea1-4134-a681-579151b9d450)
![image](https://github.com/ilanitb16/Android-App/assets/97344492/bca9fd4d-1e7d-4056-a53a-0036f3d14380)

### ANDROID
1. After running the Android app, the following screen will appear:

2. If you already have an account, type in your username and password. Incorrect username or password will result in failure and access to the website will be denied.

3.  Successful login will grant the user access to the Hompage which allows to see 20 posts of friends (sorted by date) and 5 posts of other users (which are NOT the current user).
   


### IMAGES

![image](https://github.com/ilanitb16/Android-App/assets/97344492/91114da7-c246-4f54-8c6e-7469b9740c08)

![WhatsApp Image 2024-04-28 at 22 22 30_e687ed19](https://github.com/ilanitb16/Android-App/assets/97344492/9799d25c-adec-4006-a183-6b3e20fb8d88)
![image](https://github.com/ilanitb16/Android-App/assets/97344492/f94ff926-2390-458a-aca6-587960751d3b)
![image](https://github.com/ilanitb16/Android-App/assets/97344492/bd6e1c77-a769-4a07-b32b-90c2ff439be1)

![WhatsApp Image 2024-04-28 at 16 23 40_384628f6](https://github.com/ilanitb16/Android-App/assets/97344492/08cf3845-2780-4f97-85e6-add4c0f86465)

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

![WhatsApp Image 2024-04-28 at 22 19 00](https://github.com/ilanitb16/Android-App/assets/97344492/b7e9913f-e008-41bd-a5aa-ef72cbcc621b)
![WhatsApp Image 2024-04-28 at 22 19 00 (3)](https://github.com/ilanitb16/Android-App/assets/97344492/a6274d7b-1c8e-4a62-b18a-641082b142c9)
![WhatsApp Image 2024-04-28 at 22 19 00 (2)](https://github.com/ilanitb16/Android-App/assets/97344492/edc15b3d-7ac7-40c1-bd16-3aa4be932940)


## Authentication
- Login Credentials: When a user attempts to log in, they provide their credentials, in our case a username and password combination.
  
- Client-Server Interaction: Upon entering their credentials, the client (such as a web browser or mobile app) sends a login request to the server. This request contains the user's credentials.

- Server-Side Verification: If a user is found in the system, a JWT token is created, encrypted with a private key which is returned in the body of a response with information about the user. A token is passed in each request as an "Authorization" HTTP header. If not found or expired - a response with a ststus 401 error is returned to the client side.

- In case of registration: A username is transferred from the client side. On the server side, it is checked whether a username already exists in the system.
If it does - a response is returned with status code 409. Otherwise, a new user is registered in the DB.
Logout: When the user chooses to log out, the session is terminated, and any associated session tokens or cookies are invalidated. This prevents unauthorized access to the user's account.

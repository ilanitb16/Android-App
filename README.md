# Project Name: Android App Development

## Overview:
This project aims to develop a social media application for Android devices using Java for the Android version. The application will allow users to register, log in, view a feed of posts, interact with posts (like, share, comment), and manage their profiles. The app allows users to mimic basic features of the Facebook social media platform. In this part we integrated mongoDB and allowed the project to function with a working server written on node.js.

The branch PART 3 was made specifically to store this part of the project.

## Features:
- User authentication (register, log in, log out)
- Feed screen displaying posts with like, share, and comment functionality
- Profile management (view/edit profile information, upload profile picture)
- Dark mode/light mode toggle for UI customization
- Offline support with local storage synchronization

## Getting Started

To get started with the app, follow these steps:

1. Clone this repository.
   ```
   git clone https://github.com/ShmuelGranot/Facebook-iso-APP.git
   ```
2. Open the project in Android Studio.
3. Clone the web repository.
   ```
   https://github.com/ilanitb16/facebook-ex2.git
   ```
3. In a different termianl, change directory to server and run it. 
```
Cd server
Node index.js
````
4. Build and run the project on your Android device or emulator.

## Usage: 
Upon launching the app, users will be directed to the login screen. 
New users can register by providing necessary information such as username, password, etc. Once logged in, users can browse the feed, interact with posts, and manage their profiles. 
Dark mode/light mode can be toggled from the settings menu.

Note: if the posts is not displayed immediately after login, reload it a few times- sometimes the Android Studio is problematic. 


## Features

- **User Authentication:** Users can register and log in to the app securely.
- **Feed Screen:** Users can scroll through a feed of posts, like, share, and comment on posts.
- **Profile Management:** Users can view and edit their profile information, including changing their profile picture.
- **Theme Switching:** Users can switch between dark mode and light mode in the app.

## Screenshots

- Registration:
<img width="190" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/ee2df897-427b-484d-a3a3-f4b185484b06">

- Edit the account info:
<img width="188" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/e00106db-ce50-4054-a265-1e4cd6ee3475">

-	Feed page displaying posts from server:
<img width="141" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/8d0e5586-09df-4fcb-bba4-abc993b530cc">

-	Friend requests:
 <img width="149" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/17c45153-f90a-49f2-aa7b-2d47ec78d12a">

-	Friends List:
  <img width="196" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/131310484/5e55a9e8-f0bb-446d-bd1e-cdc295b0fe6d">

-	Comments:
 <img width="128" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/e566965d-578a-4a65-be52-415932c07f44">

-	Menu:
<img width="138" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/a55b26b6-7141-42be-9923-4f0a3ba4b32c">

-	Post a new "post":
 <img width="121" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/ffc36c13-c6d8-4cf2-939d-cd141531c0f8">

-	Edit post:
  <img width="196" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/96d4501b-4c44-4c41-84fc-3c94820a467f">

-User:
 <img width="181" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/1b0c044a-b763-4ae7-95ac-6ad2aecdf592">

-	User's posts:
<img width="168" alt="image" src="https://github.com/ShmuelGranot/Facebook-iso-APP/assets/97344492/6d3cc654-502c-4ea0-a4cf-1238a4c4d23d">
(after pressing the three dots the menu with the options: "edit" and delete" is opening, while you are in the User Profile section you can see all the posts of the current user))


Note: The server was written by Ilanit for the react part, which is why there are no commits regarding the server in this part of the project. 

### link to web git repo: https://github.com/ilanitb16/facebook-ex2.git


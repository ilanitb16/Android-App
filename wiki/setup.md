# Setting up the Entire Environment

## Introduction
This repository contains the following directories:
- client-android: the android app.
- client-react: the react website.
- server: includes thenode.js server code.
- tcp: includes the c++ server merged with the bloom filter.

## Prerequisites

Before setting up the web application, ensure you have the following prerequisites:
- Node.js installed on your machine
- A compatible web browser (e.g., Chrome, Firefox, Safari)
- Visual Studio Code
- Android Studio

## Installation Steps
1.	Clone the repository to your local machine:
```
git clone https://github.com/ilanitb16/dynamic-client-server.git
```
2.	Navigate to the project directory:
``` 
cd client-react
OR
cd client-android
```
3.	Install dependencies using npm:
```
 npm install 
```

IMPORTANT NOTE:

Before running the android app, navigate to the Java directory, then com.isofacebook, then api and apiendpoints.
change: public static final String baseUrl = "http://10.0.0.17:3000"; to the ip address of the computer you run the node.js on.
Both the phone and the computer should be connected to the same network.
## TCP server
- Run the server using the VM or any linux computer.

## Node.js server
1. Edit the .env file. Change the ip address to the address of your machine (VM).
2. Navigate to the server directory:
```
cd server
```
3. run
 ```
 node index.js
 ```
4. When asking to run in a different port- type yes. 

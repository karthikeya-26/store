# Skill Store

## Overview
Skill Store is a web application built using JAX-RS that connects users looking for specific skill sets with skilled individuals. Users can list their skills for others to find and can book and collaborate with those offering the skills they need.

## Features
- Users can create, read, update, and delete their profiles.
- Users can add, update, and remove skills to/from their profile.
- Authentication filter to secure endpoints.
- Authorization filter to only let users who are permitted to perform actions.
- RESTful API built with JAX-RS.
- Database management using custom query builders.

## Project Structure


## API Endpoints (Resources folder)

### /users
- **GET**: Retrieve a list of users
  
### users/{userName}
- **GET**: Get a single user
- **PUT**: Update user details
- **DELETE**: Delete a user

### /users/{userName}/skills
- **GET**: Retrieve the skills of a specific user
- **POST**: Add new skills to a user's profile
  
### /users/{userName}/skills/{skillId}
- **GET**: Get a specific skill of user
- **PUT**: Update the specific skill of a user
- **DELETE**: Remove the specific skill from a user's profile

### /login
- **POST**: Faciltates login of a user with userName and password

### /register
- **POST**: Create a user

## Services Folder
- **UserDetailsService**: contains /user logic to process data and validate and store it also includes authentication and user registration functionalities.
- **SkillsService**: contains /user/{user}/skills logic to process and validate and store it.

### Cache Folder
- **UserDetailsCacheManager**: maintains users in cache.
- **SessionCacheManager**: maintains sessions in cache.
## Exceptions Folder
- Custom Exceptions to send proper Error Messages to send as Response

## Models Folder
- Contains classes that map to database tables.

## Query Builder 
- contains
-  **mysql** : contains mysql query builders
-  **psql** : contains postgres sql query builders.

## Query Layer
-contains classes such as **Select**, **Insert**, **Update**, **Delete** and other classes which
are used to queries database abstracted queries.

## Utils 
- contains
- ApplicationPropertiesHelper to get application properties.
- QueryLayerSetup to create database tables from json file and create enum files for writing queries.

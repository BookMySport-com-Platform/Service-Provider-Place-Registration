# Service Provider Repository Implementation

Welcome to the Service Provider Repository implementation repository! This repository helps manage service provider configurations and credentials in a centralized manner.

## Table of Contents
- [Overview](#overview)
- [Configuration](#configuration)
## Overview

The Service Provider Repository implementation provides a centralized location for managing service provider configurations and credentials. It ensures consistency across different environments and simplifies the process of updating configurations.

## Installation

To install the Service Provider Repository implementation, follow these steps:

1. Clone this repository to your local machine:

    ```bash
   // git clone https://github.com/BookMySport-com-Platform/Service-Provider-Place-Registration.git
    ```

## Configuration

Before running the service, make sure to set up your environment variables. You can use the provided `.env.example` file as a template. Rename it to `.env` and fill in the values according to your environment.

Here are the example environment variables:

```plaintext


AUTH_SERVICE_URL= ${URL}/api/getuserdetailsbytoken
DATABASE URL FOR DATABASE MYSQL IN LOCAL SYSTEM =  jdbc:mysql://${MYSQL_HOST:localhost}:3306/{DATBASE NAME}
USERNAME= YOUR DATBASE USERNAME
PASSWORD= YOUR DATBASE PASSWORD
SECRET_KEY= SHOULD BE WORDS ONLY WITH MORE THAN 45 CHARACTERS
BUCKET_NAME= YOUR AWS BUCKET NAME 
AWS_ACCESS_KEY_ID= YOUR AWS ACCOUNT ACCESS KEY ID
AWS_SECRET_ACCESS_KEY= YOUR SECRET ACCESS KEY 
SEARCH_URL_OF_AS= ${URL}/api/searchbyaddressandcentrename 
SEARCH_INFO_BY_SPID=${URL}/api/getdetailsbyspid

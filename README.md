# MS3 Coding Challenge

## Summary

- This application takes an input from of the  CSV file (located on files/input folder) that they want to import into SQLite.
- The application imports the records from the file into the **[filename].db**
- After, the application  outputs a **[filename]-bad.csv** and **[filename].log** into *files/output/* folder
- **[filename]-bad.csv** has the values that were not imported
- The file **[filename.log]** shows the number of total, successful and unsuccessful records that were imported from the CSV file



## Steps for Running application
1. Download the ZIP file from GitHub
2. Make sure your computer has SQLite installed 
3. Open up your IDE (Eclipe or Netbeans)
4. Export the zip file into IDE
5. Build the pom.xml file which is the dependencies of the applications
6. Build All the java classes within the package com.ms3.codingAssignment
6. Run the App.java files

## Overview

### Approach
- I decided to Create three classes (**App**, **CSVtoSQL**, **fileReader**, and **OutputWriter**)
- The **App** class is the main client it runs the program
- **CSV to SQL** class takes in the filename from the user's input. The filename is used to create a
   database, table, log file and bad csv file.
- **fileReader** class takes the file and reads through each line
- **OutputWriter** class creates and writes the **[filename].log** and **[filename]-bad.csv** file
- I added a Scanner object in the App Class which takes in the input from user which is the filename without the file type
- My approach was to go through each line of the csv and check for any qoutes or blank data.
- For every data of the line, I count the columns to make sure it matches with column number of database table.
- If the count is greater than the number of columns from database table then the line is invalid
- If any data is missing then the line is invalid
- If the data doesnt have any qoutes I add double qoutes around the data
- If the data does have qoutes then I set the boolean found qoutes to true 
- if found qoutes variable is set to true,  it means the data is very long.  Although the data might contain commas its still part of the initial data
- Whenever the closing qoute is found, found qoutes variable is set to false

### Assumptions
- The user is running the application on an IDE such as Eclipse or NetBeans
- The user has a CSV file in the input folder


MS3 Coding Challenge

Summary

This application takes the user input of the  CSV file that is located on the files/input folder
The CSV file is imported into the SQLite Database
The applications outputs a <filename>-bad.csv and <filename>.log
The file <filename>-bad.csv has the values that were invalid for SQLite Database
The file <filename>.log show the total, successful and  unsuccessful records from the CSV file


Steps for Running application
1. Download the ZIP file
2. Make sure your system has SQLite
3. Open up your IDE (Eclipe or Netbeans)
4. Export the zip file into IDE
5. Build the pom.xml file which is the dependencies of the applications
6. Build All the java classes within the package com.ms3.codingAssignment
6. Run the App.java files

Overview

I decided to Create three class (App, CSVtoSQL, fileReader, and OutputWriter)
The App class is the main client it runs the program
CSV to SQL class takes in the filename from the user's input. The filename is used to create a
database, table, log file and bad csv file.
My approach was to go through each line of the csv and check for any qoutes.
If the data doesnt have any qoutes I make sure I add them
If the data does have qoutes then I flag the boolean found qoutes to true until I find the data with qoutes which I flag the boolean found qoutes to false.
For every data I count the columns to make sure it matches with column number of database table.

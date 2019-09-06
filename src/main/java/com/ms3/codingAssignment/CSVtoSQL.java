package com.ms3.codingAssignment;

import java.util.Scanner;
import java.sql.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * CSV to SQL Class
 * @author edwinmwaniki
 *
 */
public class CSVtoSQL {
	private final String fileName;//filename
	private String fileType = "csv";//file type
	private Connection conn;//Connection to database
	private String [] tableColumns = null;//Table columns
	private fileReader reader = null;//File Reader
	private final String filePath = "files/input/";//File path for the input
	private OutputWriter badCSVwriter = null;//Bad CSV Writer is for values that don't have equal columns to the CSV
	private OutputWriter logFileWriter = null;//Log File Writer track down successful records, unsuccessful records, and records received
	StringBuilder sqlCommand = new StringBuilder();//String builder for any sql commands
	private Statement stmt = null;//Statement for any SQL statments
	
	/**
	 * CSVtoSQL Constructor
	 * @param fileName
	 * @throws Exception
	 */
	public CSVtoSQL (String fileName) throws Exception {
		try {
			//Gets the sqlite jar file from maven
			Class.forName("org.sqlite.JDBC");
			this.conn = null;
		}catch(Exception e) {
			throw new Exception(e);
		}
		this.fileName = fileName;
		//Checks if file exists in the /files/input folder
		if(checkIfFileExists()) {
			//Connects to sqllite db named <filename>.db if it doesnt exist creates one
			this.connectToSQLiteDB();
			//Gets full file name including file path and the file type
			String fullFileName = this.filePath+this.fileName+"."+this.fileType;
			//Initializes the file reader
			this.reader = new fileReader(new File(fullFileName));
			//Initialize tableColumns array from the first line of the CSV
			this.addColumnsFromCSV();
			//runs sql command to create table from the columns of the CSV
			this.createSQLTable();
			//Initializes the FileWrtier for the badCSVValues
			this.initializeBadCSVFileWriter();
			//Initializes the FileWriter for logs
			this.initializeLogWriter();
		}else {
			//Throws error if it doesnt exist
			throw new Exception(this.fileName+"doesnt exist in "+filePath+"");
		}
	}
	/***
	 * getFileName() returns the fileName
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/***
	 * checkIfFileExists() returns true or false if file exists
	 * @return
	 */
	private boolean checkIfFileExists() {
		String fileName = this.getFileName();
		String fullFileName = filePath+fileName+"."+this.fileType;
		File file = new File(fullFileName);
		return file.exists();
	}
	/**
	 * connectToSQLiteDB() connects to sqllite db
	 * @throws Exception
	 */
	private void connectToSQLiteDB() throws Exception {
    	try {
        	this.conn = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
        	System.out.println("SQLite DB connected");
    	}catch(Exception e) {
    		throw new Exception(e);
    	}
	}
	/**
	 * addColumnsFromCSV() initializes the table columns array 
	 * @throws Exception
	 */
	public void addColumnsFromCSV() throws Exception {
		this.tableColumns = this.reader.getText().split(",");
	}
	/**
	 * createSQLTable() creates sql table
	 * @throws Exception
	 */
	private void createSQLTable() throws Exception {
		//Table name is the filename 
		String table = this.getFileName();
		this.sqlCommand.append("CREATE TABLE IF NOT EXISTS "+table+" (\n");
        String [] columns = this.tableColumns;
        for(int i = 0; i < columns.length; i++) {
        	String name = columns[i];
        	if(i + 1 == columns.length) {
        		sqlCommand.append(name+" Varchar\n");
        	}else {
        		sqlCommand.append(name+" Varchar,\n");
        	}
        }
        this.sqlCommand.append(");");
        this.stmt = this.conn.createStatement();
        try {
            this.stmt.execute(sqlCommand.toString());
        }catch(Exception e) {
        	throw new Exception(e);
        }
        this.sqlCommand = new StringBuilder();
	}
	/**
	 * initializeBadCSVFile() initializes the BadCSVFile FileWrtier
	 * @throws Exception
	 */
	private void initializeBadCSVFileWriter() throws Exception {
		String filename = this.fileName+"-bad";
		String filetype = "csv";
		try {
			this.badCSVwriter = new OutputWriter(filename,filetype);
		}catch(Exception e) {
			System.out.println("Error initizalizing badCSVwriter ");
			throw new Exception(e);
		}
	}
	/**
	 * initializeLogWriter() Initializes the Log Writer
	 * @throws Exception
	 */
	private void initializeLogWriter() throws Exception {
		String filename = this.fileName;
		String filetype = "log";
		try {
			this.logFileWriter = new OutputWriter(filename,filetype);
		}catch(Exception e) {
			System.out.println("Error initializing logFileWriter");
			throw new Exception(e);
		}
	}
	/**
	 * importCSVToSQL() imports csv values from filename.csv to SQLite
	 * @throws Exception
	 */
	public void importCSVToSQL() throws Exception {
		int recordsReceived = 0;//Track records received
        int successfulRecords = 0;//Track successfulRecords
        int recordsFailed = 0;//Track Failed Records
        int columnCount = 0;//Track number of columns for the current data in the csv 
        int numberofColumns = this.tableColumns.length;//Get number of columns from the table columns array
        boolean foundQoutes = false;//Check if any quote is found
        String currentLine = reader.getText();//Current Line in the CSV File
        String nextLine = reader.getText();//Next Line in the CSV File
        
        //Adds the INSERT INTO statement in sql Command StringBuilder including the column names
        //badCSVwriter the column name to <filename>-bad.csv file
        this.sqlCommand.append("INSERT INTO "+this.getFileName()+" (");
        String [] columns = this.tableColumns;
        for(int i = 0; i < columns.length; i++) {
        	String name = columns[i];
        	if(i + 1 == columns.length) {
        		this.sqlCommand.append(name+"");
        		this.badCSVwriter.write(name+"\n");
        	}else {
        		this.sqlCommand.append(name+",");
        		this.badCSVwriter.write(name+",");
        	}
        }
        sqlCommand.append(") VALUES ");
        
        //While loop checks each if there no blank columns or extra columns
        //If the current line has extra columns or blank columns badCSVwriter writes the data
        //If the current line doesnt have any extra columns or blank columns Its appended sqlCommand
        while(currentLine != null) {
        	
        	//badCSV boolean to check if the data is  in bad format
        	boolean badCSV = false;
        	
        	//Separates the line into an array by comma
        	String [] column = currentLine.split(",");
        	
        	//Adds the values from the current line
        	StringBuilder values = new StringBuilder();
        	
        	//Counts the columns the data
        	columnCount = 0;
        	
        	//Loops through the column array checking if the data is empty
        	//If its empty badCSV is equal to true and breaks out the loop
        	for(int i = 0; i < column.length; i++) {
        		String data = column[i];
        		
        		//Checks if the data is empty
        		if(data.equals("")) {
        			badCSV = true;
        			break;
        		}
        		//Checks if the data contains a quote
        		if(data.contains("\"")) {
        			//foundQoutes means its a opening quote or else its a closing quote
        			//foundQoutes == false means its the opening quote of certain value and increment the column count
        			//foundQoutes == true means its closing quote of the continuing  value and doesn't increment the column count
        			if(!foundQoutes) {
        				columnCount++;
        			}
        			
        			foundQoutes = !foundQoutes;
        		}else {
        			//If the value foundQoutes == false add quotes into it and increment columnCount
        			//Else skip
        			if(!foundQoutes) {
        				data = "\""+data+"\"";
        				columnCount++;
        			}
        		}
        		
        		//If data is the last one don't add a comma 
        		//Else add a comma
        		if(i + 1 == column.length) {
        			values.append(data);
        		}else {
        			values.append(data+",");
        		}
        	}
        	//If the columnCount is not equal to numberofColumns then its a bad value
        	if(columnCount != numberofColumns) {
        		badCSV = true;
        	}
        	//If badCSV == true the badCSVwriter writes the current line into the csv file
        	if(badCSV == true) {
        		recordsFailed++;
        		badCSVwriter.write(currentLine+"\n");
        	} 
        	//currentLine switches to the nextLine
        	currentLine = nextLine;
        	//nextLine goes to the line after
        	nextLine = reader.getText();
        	//increments records received
        	recordsReceived++;
        	//badCSV == false the record is successful
        	//increments successfulRecords by 1
        	if(badCSV == false) {
        		successfulRecords++;
        		//nextLine == null means its the last record add a semicolon instead of a comma
        		//else add a comma instead of a semi colon
        		if(nextLine != null) {
        			sqlCommand.append("( "+values.toString()+" ) ,\n");
        		}else {
        			sqlCommand.append("( "+values.toString()+" ) ;\n");
        		}
        	}
        	
        }
        
        this.stmt = this.conn.createStatement();
        try {
            this.stmt.execute(sqlCommand.toString());
        }catch(Exception e) {
        	System.out.println("Error inserting records to sqllite"+e);
        	throw new Exception(e);
        }
        
        //Log the recordsReceived, successfulRecords and recordsFailed
        this.logFileWriter.write(recordsReceived+" of records received\n");
        this.logFileWriter.write(successfulRecords+" of records successful\n");
        this.logFileWriter.write(recordsFailed+" of records failed\n");
        
        //Closes the badCSVWriters and logFileWriter
        closeOutputWriters();
        
        System.out.println("Completed importing CSV records to SQLite");
	}
	
	/**
	 * closeOutputWriters() closes the logFileWriter and badCSVwriter
	 * @throws Exception
	 */
	private void closeOutputWriters() throws Exception {
		this.logFileWriter.closeWriter();
		this.badCSVwriter.closeWriter();
	}
	
}

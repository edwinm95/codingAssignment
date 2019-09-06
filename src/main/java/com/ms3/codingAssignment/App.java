package com.ms3.codingAssignment;

import java.sql.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    throws Exception {
    	Class.forName("org.sqlite.JDBC");
    	Connection c = null;    	
        System.out.println( "Welcome to CSV TO SQL Program" );
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name without the file extension");
        String fileName = scanner.next();
    	try {
        	c = DriverManager.getConnection("jdbc:sqlite:"+fileName+".db");
        	System.out.println("SQLite DB connected");
    	}catch(Exception e) {
    		throw new Exception(e);
    	}
        File file = new File("files/input/"+fileName+".csv");
        String sql = "CREATE TABLE IF NOT EXISTS csvrecords (\n";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String [] columns = br.readLine().split(",");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < columns.length; i++) {
        	String name = columns[i];
        	if(i + 1 == columns.length) {
            	sb.append(name+" Varchar\n");
        	}else {
        		sb.append(name+" Varchar,\n");
        	}
        }
        sql += sb.toString() + ");";
        Statement stmt = c.createStatement();
        try {
            stmt.execute(sql);
        }catch(Exception e) {
        	throw new Exception(e);
        }
        StringBuilder sqlString = new StringBuilder();
        File badCSVFile = new File("files/output/"+fileName+"-bad.csv");
        if(!badCSVFile.exists()) {
        	badCSVFile.createNewFile();
        }
        FileWriter badCSVwriter = new FileWriter(badCSVFile);
        sqlString.append("INSERT INTO csvrecords (");
        for(int i = 0; i < columns.length; i++) {
        	String name = columns[i];
        	if(i + 1 == columns.length) {
        		sqlString.append(name+"");
        		badCSVwriter.write(name+"\n");
        	}else {
        		sqlString.append(name+",");
        		badCSVwriter.write(name+",");
        	}
        }
        sqlString.append(") VALUES ");
        File logFile = new File("files/output/"+fileName+".log");
        if(!logFile.exists()) {
        	logFile.createNewFile();
        }
        FileWriter logFileWriter = new FileWriter(logFile);
        String line = br.readLine();
        String nextLine = br.readLine();
        int recordsReceived = 0;
        int successfulRecords = 0;
        int recordsFailed = 0;
        int columnCount = 0;
        boolean foundQoutes = false;
        while(line != null) {
        	boolean badCSV = false;
        	String [] column = line.split(",");
        	StringBuilder values = new StringBuilder();
        	columnCount = 0;
        	for(int i = 0; i < column.length; i++) {
        		String a = column[i];
        		if(a.equals("")) {
        			badCSV = true;
        			break;
        		}
        		if(a.contains("\"")) {
        			if(!foundQoutes) {
        				columnCount++;
        			}
        			foundQoutes = !foundQoutes;
        		}else {
        			if(!foundQoutes) {
        				a = "\""+a+"\"";
        				columnCount++;
        			}
        		}
        		if(i + 1 == column.length) {
        			values.append(a);
        		}else {
        			values.append(a+",");
        		}
        	}
        	if(columnCount != 10) {
        		badCSV = true;
        	}
        	if(badCSV == true) {
        		recordsFailed++;
        		badCSVwriter.write(line+"\n");
        	}        	
        	line = nextLine;
        	nextLine = br.readLine();
        	recordsReceived++;
        	if(badCSV == false) {
        		successfulRecords++;
        		if(nextLine != null) {
        			sqlString.append("( "+values.toString()+" ) ,\n");
        		}else {
        			sqlString.append("( "+values.toString()+" ) ;\n");
        		}
        	}
        }
        badCSVwriter.flush();
        badCSVwriter.close();
        logFileWriter.write(recordsReceived+" of records received\n");
        logFileWriter.write(successfulRecords+" of records successful\n");
        logFileWriter.write(recordsFailed+" of records failed\n");
        logFileWriter.flush();
        logFileWriter.close();
        File sqlOutput = new File("files/output/sqloutput.log");
        if(!sqlOutput.exists()) {
        	sqlOutput.createNewFile();
        }
        FileWriter sqlOutputWriter = new FileWriter(sqlOutput);
        sqlOutputWriter.write(sqlString.toString());
        sqlOutputWriter.flush();
        sqlOutputWriter.close();
        stmt.execute(sqlString.toString());
    }
}

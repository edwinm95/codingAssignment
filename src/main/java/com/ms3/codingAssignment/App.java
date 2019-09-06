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
    	//SQL Lite dependecy 
    	Class.forName("org.sqlite.JDBC");
    	Connection c = null;    	
        System.out.println( "Welcome to CSV TO SQL Program" );
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name without the file extension");
        String fileName = scanner.next();
        CSVtoSQL csvtoSQL = new CSVtoSQL(fileName);
        csvtoSQL.importCSVToSQL();
    }
}

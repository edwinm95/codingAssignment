package com.ms3.codingAssignment;

import java.sql.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int count = 1;
        while((st = br.readLine()) != null && count != 4 ) {
        	System.out.println(st);
        	count++;
        }
    }
}

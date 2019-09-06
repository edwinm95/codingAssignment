package com.ms3.codingAssignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class fileReader {
	
	private BufferedReader reader = null;//BufferReader for the file
	private String currentPointer = null;//Current line Pointer 
	private String nextPointer = null;//Next line pointer
	
	/**
	 * fileReader(File file) 
	 * initializes the BufferedReader, currentPointer and nextPointer
	 * @param file
	 * @throws Exception
	 */
	public fileReader (File file) throws Exception {
		reader = new BufferedReader(new FileReader(file));
		currentPointer = reader.readLine();
		nextPointer = reader.readLine();
	}
	/**
	 * getText()
	 * gets the text from the file
	 * Switches current pointer to next pointer
	 * and next pointer to the next line
	 * @return
	 * @throws Exception
	 */
	public String getText() throws Exception {
		String text = this.currentPointer;
		this.currentPointer = this.nextPointer;
		this.nextPointer = reader.readLine();
		return text;
	}
	
	/**
	 * getNextPointer()
	 * @return next String line
	 */
	public String getNextPointer() {
		return this.nextPointer;
	}
	/**
	 * getCurrentPointer()
	 * @return current String line
	 */
	public String getCurrentPointer() {
		return this.currentPointer;
	}
	
}

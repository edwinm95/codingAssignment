package com.ms3.codingAssignment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {
	
	private FileWriter writer = null;//FileWriter
	private String filePath = "files/output/";//File path for output files
	
	/**
	 * OutputWriter()
	 * Initializes the FileWriter
	 * @param fileName
	 * @param fileType
	 * @throws Exception
	 */
	public OutputWriter(String fileName, String fileType) throws Exception{
		File file = new File(filePath+fileName+"."+fileType);
		if(!file.exists()) {
			try {
			file.createNewFile();
			}catch(Exception e) {
				System.out.println("Error creating file");
				throw new Exception(e);
			}
		}
		try {
			this.writer = new FileWriter(file);
		}catch(Exception e) {
			System.out.println("Error initializing file writer");
			throw new Exception(e);
		}
	}
	/**
	 * write()
	 * takes a string and writes it on the file
	 * @param s
	 * @throws Exception
	 */
	public void write(String s) throws Exception{
		try {
			this.writer.write(s);
		}catch(Exception e) {
			System.out.println("Error writing string");
			throw new Exception (e);
		}
	}
	/**
	 * closeWriter()
	 * closes the file Writer
	 * @throws Exception
	 */
	public void closeWriter() throws Exception {
		this.writer.flush();
		this.writer.close();
	}
	
}

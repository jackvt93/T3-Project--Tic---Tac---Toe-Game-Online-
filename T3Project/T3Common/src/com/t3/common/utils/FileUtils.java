package com.t3.common.utils;

import java.io.File;

/**
 * This class using for processes about file
 * @author Luan Vu
 *
 */
public class FileUtils {
	/**
	 * Create a directory if it not existed
	 * @param directory you want create directory
	 */
	public static void createDirectory(String directory) {
	    File dir = new File (directory);
	    if (!dir.isDirectory())   
	    	dir.mkdir();
	}
	
	
}

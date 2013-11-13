package com.t3.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Luan Vu
 *	This utility class using for write log to file/console
 */
public class Log {
	
	private static final String LOG_DIRECTORY = "log";
	
	private static final String DATE_FORMAT_DIR  = "yyyy_MM";
	private static final String DATE_FORMAT_FILE = "dd_MMM_yyyy";
	private static final String DATE_FORMAT_LOG  = "yyyy/MM/dd hh:mm:ss:SSS";
	
	/** 
	 * Write log to console
	 *  */
	public static void console(String tag, String message) {
		Calendar calender = Calendar.getInstance();
		Date date         = calender.getTime();
		
		SimpleDateFormat dateFormatLog = new SimpleDateFormat(DATE_FORMAT_LOG);
		String time = dateFormatLog.format (date);

		String logMessage =
			"[" + time + ": " + tag +"] " +  message;		
		System.out.println(logMessage);
	}
	
	
	/**
	 * write log to file
	 */
	public static void logfile(String message, Date date) {
		FileUtils.createDirectory (LOG_DIRECTORY);
		SimpleDateFormat dateFormatDir = new SimpleDateFormat(DATE_FORMAT_DIR);
		SimpleDateFormat dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
		
		char separatorChar = File.separatorChar; // '/'
		
		String logDirectory = dateFormatDir.format (date);
		String logFilename  = "log_" + dateFormatFile.format (date) + ".txt";
		
		String fullFileName = LOG_DIRECTORY + separatorChar + logDirectory + separatorChar + logFilename;
		File logFile = new File (fullFileName);
		
		FileUtils.createDirectory (LOG_DIRECTORY + separatorChar + logDirectory);
		
		try {
			BufferedWriter fileout = null;          

			if (logFile.exists()) {
				fileout = new BufferedWriter(new FileWriter(fullFileName, true));   // true = append
				fileout.newLine();
			}
			else                            // else create the file
				fileout = new BufferedWriter(new FileWriter(fullFileName));

			fileout.write (message);
			fileout.close ();
		}
		catch (IOException ioe) {
		    // if error don't worry (do nothing)
		}
		
	}
}

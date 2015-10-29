package ch.hevs.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ch.hevs.ifaces.IIntercom;

/**
 * A class defining a logger (in a file and/or GUI) by static way
 * @custom.category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class Logger
{
	private static FileOutputStream os;
	private static PrintWriter pw;
	public static boolean console;
	
	private static ArrayList<IIntercom> communication = new ArrayList<IIntercom>();
	
	/**
	 * Static method used to perform the initialization of the logger
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param f the file to log data into
	 * @param c if the standard console output have to be used
	 */	
	public static void initLogger(String f, boolean c)
	{
		console = c;
		try
		{	
			os = new FileOutputStream(new File(f));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		if(os == null)
		{
			System.err.println("Failed to create to log file. Path seems to be invalid");
		}
		pw = new PrintWriter(os);
	}
	
	/**
	 * Static method used to perform the log
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param data which contains the data to log in
	 */	
	public static void log(String data)
	{
		pw.append("----------------\n");
		pw.append(LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))
				+" -> " + data+"\n");
		logToWindow(LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))
				+" -> " + data+"\n");
		if(console)
		{
			System.out.println(LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))
				+" -> " + data);
		}
		pw.flush();
	}
	
	/**
	 * Static method register another class to the log functionality
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param e object
	 */	
	public static void addListener(IIntercom e)
	{
		communication.add(e);
	}

	/**
	 * Dispatch the data to log into the GUI log panel
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param s the file to log data into
	 */	
	public static void logToWindow(String s)
	{
		communication.forEach(e->e.updateLogWindow(s));
	}
}

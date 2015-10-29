package ch.hevs.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * A class used to read the configuration file containing the diverse ip addresses of the devices in the network
 * @category SimSeic 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class ConfigReader
{
	private TreeMap<String, String> configholder;
	private InputStream in;
	private BufferedReader bf;
	private String res = "The configuration file contains : \n";
	
	/**
	 * Construct the config reader
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param f which file to read
	 */	
	public ConfigReader(String f)
	{
		configholder = new TreeMap<String, String>();
		try
		{
			in = new FileInputStream(f);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		if(in != null)
		{
			Logger.log("Configuration file successfully found.");
		}
		else
		{
			Logger.log("Error : Opening configuration file.");
		}
		bf = new BufferedReader(new InputStreamReader(in));
		if(bf == null)
		{
			Logger.log("Error : Buffered reader is null!");
		}
		else
		{
			Logger.log("Configuration file can be used now.");
		}
		try
		{
			Thread.sleep(2000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		readconfig(bf);
	}
	
	/**
	 * Read the configuration file
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param bf used to read the file
	 */	
	public void readconfig(BufferedReader bf)
	{
		String line = "";
		try
		{
			line = bf.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		while(line!=null)
		{
			if(line.charAt(0)!='#')
			{
				String[] linesplit = line.split("=");
				configholder.put(linesplit[0], linesplit[1]);
			}
			try
			{
				line = bf.readLine();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			bf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Logger.log("Configuration file successfully loaded & read.");
		Logger.log(toString());
	}
	
	/**
	 * toString util method for logging purposes
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return a representation of the configuration file
	 */	
	@Override
	public String toString()
	{
		res+="\n"+res;
		configholder.forEach((key, value)->
		{
			res+=(key+" : "+value+"\n");
		});
		return res;
	}
	
	/**
	 * Getter of the configuration stored in a TreeMap
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return configuration holder
	 */	
	public TreeMap<String, String> getConfig()
	{
		return configholder;
	}
}

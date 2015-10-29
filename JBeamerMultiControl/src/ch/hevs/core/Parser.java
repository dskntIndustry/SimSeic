package ch.hevs.core;

import ch.hevs.network.CommandEvent;
import ch.hevs.utils.Constants;

/**
 * Util class for handling commands
 * @category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class Parser
{
	/**
	 * Parse the command given as parameter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param e command object
	 * @return which represents the command 
	 */	
	public static String parseCommand(CommandEvent e)
	{
		return (e.getCmd()+e.getData()).toLowerCase();
	}
	public static String[] parse(String in)
	{
		in = in.toLowerCase();
		return in.split(Constants.defaultSeparator);
	}
}

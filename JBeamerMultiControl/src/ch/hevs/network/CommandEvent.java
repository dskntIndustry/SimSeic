package ch.hevs.network;

/**
 * A class representing a command
 * @custom.category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class CommandEvent
{
	private String source;
	private String destination;
	private String cmd;
	private String data;
	
	/**
	 * Constructor without parameter initializing all to empty string
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	public CommandEvent()
	{
		source = "";
		destination = "";
		cmd = "";
		data = "";
	}
	
	/**
	 * Constructor for complete initialization
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param source localhost normally
	 * @param destination to set the destination ip address
	 * @param command which command is present in the message
	 * @param payload the data in the message
	 */	
	public CommandEvent(String source, String destination, String command, String payload)
	{
		this.source = source;
		this.destination = destination;
		this.cmd = command;
		this.data = payload;
	}

	/**
	 * Destination getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return destination of the message
	 */	
	public String getDestination()
	{
		return destination;
	}
	
	/**
	 * Command getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return command present in the message
	 */	
	public String getCmd()
	{
		return cmd;
	}
	
	/**
	 * Command data getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return data present in the message
	 */	
	public String getData()
	{
		return data;
	}
	
	/**
	 * toString util method for logging purposes
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return a representation of the complete message
	 */	
	@Override
	public String toString()
	{
		return "Source : "+source +", "+"Destination : "+destination +", "+"Command : "+cmd +", "+"Data : "+data +".";
	}
	
}

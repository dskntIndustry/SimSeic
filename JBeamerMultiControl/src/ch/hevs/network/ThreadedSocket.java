package ch.hevs.network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.hevs.utils.Constants;
import ch.hevs.utils.Logger;

/**
 * A class defining threaded socket
 * @custom.category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class ThreadedSocket implements Runnable
{
	private Socket socket = null;
	private String host;
	private int port;
	private OutputStream os;
	private PrintWriter w;
	private InputStreamReader is;
	
	
	private String identifier;

	// Default initializing telnet command
	

	private int c;
	
	public static final long TIMEOUT = 2000L;

	private ConcurrentLinkedQueue<CommandEvent> commandQueue;

	/**
	 * Threaded socket constructor
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param h String host ip address
	 * @param p int port (default 23, TELNET port)
	 */	
	public ThreadedSocket(String h, int p)
	{
		host = h;
		port = p;
		identifier = host;
		commandQueue = new ConcurrentLinkedQueue<CommandEvent>();
		initSocket();
	}
	
	/**
	 * Threaded socket initialization method used to open/bind socket to remote host
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	public void initSocket()
	{
		try
		{
			socket = new Socket(host, port);
			w = new PrintWriter(socket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e1)
		{
			Logger.log(e1.getMessage());
		}
		try
		{
			if(socket != null)
			{
				w.print(Constants.telnetInitMessage);
				w.flush();
				if(!socket.isClosed())
				{
					Logger.log("Socket with address " + host + " & port " + socket.getPort() + " is successfully opened and ready to use.");
				}
			}
		}
		catch(Exception e)
		{
			Logger.log("Error : Cannot open socket" + host + socket.getPort());
			Logger.log(e.getMessage());
		}
	}

	/**
	 * Threaded socket "destructor" method.
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	public void deinitSocket()
	{
		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Logger.log("Socket with address " + host + " is being removed from list");

	}

	/**
	 * ConcurrentLinkedQueue getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return the synchronized list in each ThreadedSocket object
	 */	
	public ConcurrentLinkedQueue<CommandEvent> getCommandQueue()
	{
		return commandQueue;
	}

	/**
	 * Identifier/host getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return String identifier which is the ip address of the socket
	 */	
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * Implementation of the transmission of a command to a remote host
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param address String which is the remote host
	 * @param command String which is the address to send
	 */	
	public void sendCommand(String address, String command)
	{
		command = command.toUpperCase();
		command = "(" + command + ")";
		if(!socket.isClosed())
		{
			try
			{
				os = socket.getOutputStream();
				w = new PrintWriter(os, true);
			}
			catch(IOException e)
			{
				Logger.log(e.getMessage());
			}
			w.print(command);
			w.flush();
			Logger.log("Command " + command + " @ address " + address + " sent.");
			if(command.endsWith("!") || command.endsWith("?"))
			{
				receive();
			}
		}

	}

	/**
	 * Implementation of the reception of a previous sent command to a remote host
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return String which is the received data
	 */	
	public String receive()
	{
		String res = "";
		try
		{
			is = new InputStreamReader(socket.getInputStream());
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			String read = "";
			int count = 0;
			Logger.log("Begin reading");
			while(((c = is.read()) != -1))
			{
				read += (char)c;
				++count;
				if(c == 0x29 || c == 0x3f)
				{
					break;
				}
			}
			Logger.log("Data received from device @ address " + host +" : "+ read + ", Data count : " + count);
		}
		catch(IOException e)
		{
			Logger.log(e.getMessage());
			try
			{
				is.close();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * Run method checking the internal command queue and dispatching the event to send
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	@Override
	public void run()
	{
		CommandEvent ce = new CommandEvent();
		for(;;)
		{
			if(!commandQueue.isEmpty() && socket.isConnected())
			{
				ce = commandQueue.poll();
				if(ce != null)
				{
					sendCommand(ce.getDestination(), ce.getCmd() + ce.getData());
				}
			}
			if(!socket.isConnected())
			{
				Logger.log("Socket "+ host+" isn't connected anymore.");
				if(socket.isClosed())
				{
					Logger.log("Socket "+ host+" isn't open anymore.");
				}
				deinitSocket();
			}
		}
	}
}
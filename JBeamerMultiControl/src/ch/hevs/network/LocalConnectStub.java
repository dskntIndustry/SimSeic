package ch.hevs.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.hevs.core.MessagingQueue;
import ch.hevs.core.Parser;
import ch.hevs.utils.Logger;

/**
 * A class defining threaded local network endpoint
 * 
 * @custom.category SimSeic
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class LocalConnectStub implements Runnable
{
	private Socket socket = null;
	private String host;
	private int port;
	private OutputStream os;
	private PrintWriter w;
	private InputStreamReader is;
	private BufferedReader bf;

	private String identifier;

	public static final long TIMEOUT = 2000L;

	private ConcurrentLinkedQueue<NetworkEvent> internalQueue;
	private MessagingQueue localqueue;

	/**
	 * LocalConnectStub constructor
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param localqueue
	 *            the core messaging queue
	 * @param h
	 *            String host ip address
	 * @param p
	 *            int port (default 23, TELNET port)
	 */
	public LocalConnectStub(MessagingQueue localqueue, String h, int p)
	{
		host = h;
		port = p;
		this.localqueue = localqueue;
		identifier = host;
		internalQueue = new ConcurrentLinkedQueue<NetworkEvent>();
		initSocket();
	}

	/**
	 * Threaded socket initialization method used to open/bind socket to remote
	 * host
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */
	public void initSocket()
	{
		try
		{
			socket = new Socket(host, port);
			w = new PrintWriter(socket.getOutputStream());
			is = new InputStreamReader(socket.getInputStream());
			bf = new BufferedReader(is);
		}
		catch(IOException e)
		{
			System.out.println("Error while creating socket : Socket "+socket);
			if(socket == null)
			{
				while (true)
				{
				    try
				    {
				        socket = new Socket(host, port);
						w = new PrintWriter(socket.getOutputStream());
						is = new InputStreamReader(socket.getInputStream());
						bf = new BufferedReader(is);
						break;
				    }
				    catch (SocketTimeoutException ex) 
				    {
				        System.out.println("Trying to connect to " + host + "...");
				    }
				    catch (IOException ex) 
				    {
				    	Logger.log(ex.getMessage());
				    }
				}
			}
			Logger.log(e.getMessage());
		}
		catch(Exception e1)
		{
			Logger.log(e1.getMessage());
		}
		try
		{
			if(socket != null)
			{
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
	 * LocalConnectStub socket "destructor" method.
	 * 
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
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return the synchronized list in each LocalConnectStub object which
	 *         handle NetworkEvents
	 */
	public ConcurrentLinkedQueue<NetworkEvent> getInternalQueue()
	{
		return internalQueue;
	}

	/**
	 * Identifier/host getter
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return String identifier which is the ip address of the socket
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * Implementation of the reception of a previous sent NetworkEvent coming
	 * from a remote or local host
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return String which is the received data
	 */
	public String receive()
	{
		String res = "";
		try
		{
			if(socket.isConnected())
			{

				res = bf.readLine();
			}
		}
		catch(IOException e)
		{
			Logger.log(e.getMessage());
			try
			{
				is.close();
				initSocket();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return res;

	}

	/**
	 * Run method checking the internal event queue and dispatching the event to
	 * send
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */
	@Override
	public void run()
	{
		String input = "";
		String[] p = null;
		NetworkEvent e = null;
		for(;;)
		{
			if(socket !=null)
			{
				input = receive();
				p = Parser.parse(input);
				if(p.length > 3)
				{
					if(p[2].equals("cmd"))
					{
						if(!localqueue.getSockets().isEmpty())
						{
							String[] s = Parser.parse(p[3]);
							localqueue.findInQueue(p[1]).getCommandQueue().add(new CommandEvent(p[0], p[1], s[0], s[1]));
							e = new NetworkEvent(p[0], p[1], p[2], s[0], s[1]);
						}
					}
					else if(p[2].equals("data"))
					{
						// generated backdoor to receive data
						
						System.out.println("Some data???");
						e = new NetworkEvent(p[0], p[1], p[2], null, p[3]);
					}
					Logger.log(e.toString());
				}
				else
				{
					if(socket == null)
					{
						Logger.log("Socket fatal error.");
					}
					else
					{
						Logger.log("Data received from socket " + getIdentifier() + " are invalid. Check syntax of the message.");
					}
					
				}
			}
		}
	}
}

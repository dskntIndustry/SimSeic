package ch.hevs.core;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.hevs.network.CommandEvent;
import ch.hevs.network.ThreadedSocket;

/**
 * 
 * @category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class MessagingQueue implements Runnable
{
	private ConcurrentLinkedQueue<CommandEvent> messagequeue;
	private ConcurrentLinkedQueue<ThreadedSocket> sockets;
	
	/**
	 * Constructor of the MessagingQueue object
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	public MessagingQueue()
	{
		messagequeue = new ConcurrentLinkedQueue<CommandEvent>();
		messagequeue.clear();
		sockets = new ConcurrentLinkedQueue<ThreadedSocket>();
		sockets.clear();
	}

	/**
	 * ConcurrentLinkedQueue<ThreadedSocket> getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return synchronized list of the current socket being used
	 */	
	public ConcurrentLinkedQueue<ThreadedSocket> getSockets()
	{
		return sockets;
	}

	/**
	 * ConcurrentLinkedQueue<CommandEvent> getter
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @return  synchronized list containing commands
	 */	
	public ConcurrentLinkedQueue<CommandEvent> getMessagequeue()
	{
		return messagequeue;
	}
	
	/**
	 * Util method used to find a specific socket into the synchronized list according to its identifier e.g ip address
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param s ip address/identifier  
	 * @return the object if present in the list else null
	 */	
	public ThreadedSocket findInQueue(String s)
	{
		ThreadedSocket r = null;
		for(Iterator<ThreadedSocket> iterator = sockets.iterator(); iterator.hasNext();)
		{
			ThreadedSocket multithreadSocket = (ThreadedSocket)iterator.next();
			if(multithreadSocket.getIdentifier().equals(s))
			{
				r = multithreadSocket;
			}
		}
		return r;
	}
	
	/**
	 * The run() method is implemented from Runnable interface. It checks the message queue and dispatch the message to the queue present in each ThreadedSocket object.
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 */	
	@Override
	public void run()
	{
		System.out.println("Dispatching");

		for(;;)
		{
			if(!messagequeue.isEmpty())
			{
				for(Iterator<CommandEvent> iterator = messagequeue.iterator(); iterator.hasNext();)
				{
					CommandEvent commandEvent = (CommandEvent)iterator.next();
					// Broadcast command
					if(commandEvent.getDestination().equals("0.0.0.0"))
					{
						sockets.forEach(socket ->
						{
							socket.getCommandQueue().add(commandEvent);
						});
						messagequeue.remove();
					}
					//Specific command
					else
					{
						sockets.forEach(e->
						{
							if(e.getIdentifier().equals(commandEvent.getDestination()))
							{
								e.getCommandQueue().add(commandEvent);
							}
						});
						messagequeue.remove();
					}
				}				
			}
		}		
	}
}

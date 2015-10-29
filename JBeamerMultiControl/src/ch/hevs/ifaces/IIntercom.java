package ch.hevs.ifaces;

/**
 * Internal communication interface
 * @custom.category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public interface IIntercom
{
	/**
	 * Declaration of the updateLogWindow(String s) method.
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param s which is the message to send to the log panel
	 */	
	public void updateLogWindow(String s);
}

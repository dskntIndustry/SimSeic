/**
 * 
 */
package ch.hevs.app;

import ch.hevs.core.MessagingQueue;
import ch.hevs.gui.ControlWindow;
import ch.hevs.network.LocalConnectStub;
import ch.hevs.utils.ConfigReader;
import ch.hevs.utils.Logger;

/**
 * 
 * @category SimSeic 
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class App
{
	/**
	 * @param args line command arguments (not used in this version)
	 */
	public static void main(String[] args)
	{
		Logger.initLogger("status.log", true);
		ConfigReader cr = new ConfigReader("config.properties");
		MessagingQueue messagequeue = new MessagingQueue();
		new ControlWindow(messagequeue, cr.getConfig());
		new Thread(messagequeue).start();
		// Activate local server communication with LABview
		//new Thread(new LocalConnectStub(messagequeue, "localhost", 24000)).start();
	}
}

/**
 * 
 */
package ch.hevs.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.util.ArrayList;
import java.util.TreeMap;

import ch.hevs.core.MessagingQueue;
import ch.hevs.ifaces.IIntercom;
import ch.hevs.network.CommandEvent;
import ch.hevs.network.ThreadedSocket;
import ch.hevs.utils.Constants;
import ch.hevs.utils.Logger;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButton;

/**
 * This class defines the main GUI of the program
 * 
 * @custom.category SimSeic
 * @author Mikael Follonier mikael.follonier@hevs.ch
 * @version 1.0
 *
 */
public class ControlWindow extends JFrame implements IIntercom
{
	private ArrayList<IIntercom> communicationListener = new ArrayList<IIntercom>();
	private TreeMap<String, String> ch = new TreeMap<String, String>();

	private static final long serialVersionUID = 1L;
	private JButton btnPowerOff;
	private JLabel lblPowerOn;
	private JButton btnPowerOn;
	private JPanel tab1;
	private JPanel tab0;
	private JTabbedPane tabbedPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton btnSrcHDMI;
	private JButton btnResolution169;
	private JButton btnSrcAutoOn;
	private JButton btnSrcAutoOff;
	private JButton btnCeilOff;
	private JLabel lblSource;
	private JLabel lblResolution;
	private JLabel lblCeiling;
	private JButton btnResolutionNative;
	private JButton btnCeilOn;
	private JLabel lblAutoSource;
	private JButton btnResolution43;
	private JButton btnResolution1610;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JPanel statuspanel;

	private ArrayList<Component> component = new ArrayList<Component>();
	private JCheckBox chckbxLogToConsole;
	private JButton btnFireCustomEvent;
	private JTextField customCommandField;
	private JLabel lblConnected;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmExit;
	private JButton btnOverrideIP;
	private JButton btnStartVideo;
	private JRadioButton rdbtnDanger;

	/**
	 * Constructor of the ControlWindow object (GUI)
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param queue
	 *            MessagingQueue Object
	 * @param configholder
	 *            holding the configuration
	 */
	public ControlWindow(MessagingQueue queue, TreeMap<String, String> configholder)
	{
		ch = configholder;
		// sol = new SerialOverLAN(cr);

		Logger.addListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				queue.getSockets().forEach(s->s.deinitSocket());
				queue.getSockets().forEach(s->Logger.log("Application is closing socket for shutdown."));
				e.getWindow().dispose();
			}
		});

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		setSize(new Dimension(800, 481));
		setResizable(false);
		setTitle("Beamers Remote Control");

		GridBagLayout gridBagLayout = new GridBagLayout();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tab0 = new JPanel();
		tab1 = new JPanel();

		textArea = new JTextArea();
		lblPowerOn = new JLabel("Power ON");
		lblCeiling = new JLabel("Ceiling");
		lblResolution = new JLabel("Resolution");
		lblSource = new JLabel("Source");
		lblAutoSource = new JLabel("Auto source");

		btnConnect = new JButton("Connect");
		btnDisconnect = new JButton("Disconnect");
		btnPowerOff = new JButton("OFF");
		btnPowerOn = new JButton("ON");
		btnSrcAutoOff = new JButton("OFF");
		btnSrcAutoOn = new JButton("ON");
		btnSrcHDMI = new JButton("HDMI");
		btnResolution43 = new JButton("4:3");
		btnResolution1610 = new JButton("16:10");
		btnResolutionNative = new JButton("Native");
		btnCeilOff = new JButton("Ceil OFF");
		btnCeilOff.setEnabled(false);
		btnCeilOn = new JButton("Ceil ON");
		btnCeilOn.setEnabled(false);
		btnResolution169 = new JButton("16:9");

		gridBagLayout.columnWidths = new int[]
		{ 294, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 272, 0, 33, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);

		tabbedPane.addTab("Main controls", null, tab0, null);
		GridBagLayout gbl_tab0 = new GridBagLayout();
		gbl_tab0.columnWidths = new int[]
		{ 116, 100, 70, 0, 0 };
		gbl_tab0.rowHeights = new int[]
		{ 29, 26, 23, 0, 0, 0, 0, 36, 0 };
		gbl_tab0.columnWeights = new double[]
		{ 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_tab0.rowWeights = new double[]
		{ 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		tab0.setLayout(gbl_tab0);

		tabbedPane.addTab("Options", null, tab1, null);
		GridBagLayout gbl_tab1 = new GridBagLayout();
		gbl_tab1.columnWidths = new int[]
		{ 257, 506, 284, 0 };
		gbl_tab1.rowHeights = new int[]
		{ 30, 30, 0, 28, 0, 0 };
		gbl_tab1.columnWeights = new double[]
		{ 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_tab1.rowWeights = new double[]
		{ 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		tab1.setLayout(gbl_tab1);

		chckbxLogToConsole = new JCheckBox("Log to console");
		chckbxLogToConsole.setSelected(true);
		chckbxLogToConsole.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean log = true;
				log = chckbxLogToConsole.isSelected() ? true : false;
				Logger.console = log;
			}
		});
		GridBagConstraints gbc_chckbxLogToConsole = new GridBagConstraints();
		gbc_chckbxLogToConsole.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxLogToConsole.gridx = 0;
		gbc_chckbxLogToConsole.gridy = 0;
		tab1.add(chckbxLogToConsole, gbc_chckbxLogToConsole);

		customCommandField = new JTextField();
		customCommandField.setText("10.13.1.200#pwr#1!");
		customCommandField
				.setToolTipText("To send custom commands to a projector use the following syntax :\r\ndestinationIP#command#data with destination IP\r\n\r\nCase unsensitive and data contains only the transmit data and the ! or ?.");
		GridBagConstraints gbc_customCommandField = new GridBagConstraints();
		gbc_customCommandField.insets = new Insets(0, 0, 5, 5);
		gbc_customCommandField.fill = GridBagConstraints.HORIZONTAL;
		gbc_customCommandField.gridx = 1;
		gbc_customCommandField.gridy = 1;
		tab1.add(customCommandField, gbc_customCommandField);
		customCommandField.setColumns(10);

		btnFireCustomEvent = new JButton("Fire Custom Event");
		btnFireCustomEvent.setToolTipText("Send a command to the given address");
		btnFireCustomEvent.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					String[] cmd = null;
					if((cmd = checkCommand(customCommandField.getText())) != null)
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", cmd[0], cmd[1], cmd[2]));
						System.out.println(cmd);
					}
					else
					{
						customCommandField.setText("Invalid number of params. Impossible to send this command.");
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					String[] cmd = null;
					if((cmd = checkCommand(customCommandField.getText())) != null)
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", cmd[0], cmd[1], cmd[2]));
						System.out.println(cmd);
					}
					else
					{
						customCommandField.setText("Invalid number of params. Impossible to send this command.");
					}
				}
			}
		});
		GridBagConstraints gbc_btnFireCustomEvent = new GridBagConstraints();
		gbc_btnFireCustomEvent.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFireCustomEvent.insets = new Insets(0, 0, 5, 5);
		gbc_btnFireCustomEvent.gridx = 0;
		gbc_btnFireCustomEvent.gridy = 1;
		tab1.add(btnFireCustomEvent, gbc_btnFireCustomEvent);

		btnOverrideIP = new JButton("Override IP Address");
		btnOverrideIP.setToolTipText("Broadcast send overriding given address");
		btnOverrideIP.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					String[] cmd = null;
					if((cmd = checkCommand(customCommandField.getText())) != null)
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", cmd[1], cmd[2]));
						System.out.println(cmd);
					}
					else
					{
						customCommandField.setText("Invalid number of params. Impossible to send this command.");
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					String[] cmd = null;
					if((cmd = checkCommand(customCommandField.getText())) != null)
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", cmd[1], cmd[2]));
						System.out.println(cmd);
					}
					else
					{
						customCommandField.setText("Invalid number of params. Impossible to send this command.");
					}
				}
			}
		});
		GridBagConstraints gbc_btnOverrideIP = new GridBagConstraints();
		gbc_btnOverrideIP.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOverrideIP.insets = new Insets(0, 0, 5, 5);
		gbc_btnOverrideIP.gridx = 0;
		gbc_btnOverrideIP.gridy = 2;
		tab1.add(btnOverrideIP, gbc_btnOverrideIP);
		
		rdbtnDanger = new JRadioButton("Ceiling");
		rdbtnDanger.setToolTipText("Warning : Activate this can result in beamers bad behaviour");
		rdbtnDanger.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{}
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if(Constants.definePressed)
				{
					btnCeilOff.setEnabled(rdbtnDanger.isSelected());
					btnCeilOn.setEnabled(rdbtnDanger.isSelected());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{}
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{}
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if(!Constants.definePressed)
				{
					btnCeilOff.setEnabled(rdbtnDanger.isSelected());
					btnCeilOn.setEnabled(rdbtnDanger.isSelected());
				}
			}
		});
		GridBagConstraints gbc_rdbtnDanger = new GridBagConstraints();
		gbc_rdbtnDanger.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnDanger.gridx = 0;
		gbc_rdbtnDanger.gridy = 4;
		tab1.add(rdbtnDanger, gbc_rdbtnDanger);
		
				lblConnected = new JLabel("Connection status");
				GridBagConstraints gbc_lblConnected = new GridBagConstraints();
				gbc_lblConnected.insets = new Insets(0, 0, 5, 5);
				gbc_lblConnected.gridx = 0;
				gbc_lblConnected.gridy = 0;
				tab0.add(lblConnected, gbc_lblConnected);
		statuspanel = new JPanel();
		
				GridBagConstraints gbc_statuspanel = new GridBagConstraints();
				gbc_statuspanel.insets = new Insets(0, 0, 5, 5);
				gbc_statuspanel.gridx = 1;
				gbc_statuspanel.gridy = 0;
				tab0.add(statuspanel, gbc_statuspanel);
				statuspanel.setBackground(Color.RED);

		textArea.setFont(new Font("Lucida Console", Font.PLAIN, 11));
		scrollPane = new JScrollPane(textArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 7;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 1;
		tab0.add(scrollPane, gbc_scrollPane);
		textArea.setEditable(false);
		textArea.setVisible(true);

		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		GridBagConstraints gbc_lblPowerOn = new GridBagConstraints();
		gbc_lblPowerOn.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_lblPowerOn.ipady = 5;
		gbc_lblPowerOn.ipadx = 5;
		gbc_lblPowerOn.insets = new Insets(0, 0, 5, 5);
		gbc_lblPowerOn.gridx = 0;
		gbc_lblPowerOn.gridy = 2;
		tab0.add(lblPowerOn, gbc_lblPowerOn);

		GridBagConstraints gbc_lblCeiling = new GridBagConstraints();
		gbc_lblCeiling.insets = new Insets(0, 0, 5, 5);
		gbc_lblCeiling.gridx = 0;
		gbc_lblCeiling.gridy = 5;
		tab0.add(lblCeiling, gbc_lblCeiling);

		GridBagConstraints gbc_lblResolution = new GridBagConstraints();
		gbc_lblResolution.insets = new Insets(0, 0, 5, 5);
		gbc_lblResolution.gridx = 0;
		gbc_lblResolution.gridy = 6;
		tab0.add(lblResolution, gbc_lblResolution);

		GridBagConstraints gbc_lblSource = new GridBagConstraints();
		gbc_lblSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblSource.gridx = 0;
		gbc_lblSource.gridy = 4;
		tab0.add(lblSource, gbc_lblSource);

		GridBagConstraints gbc_lblAutoSource = new GridBagConstraints();
		gbc_lblAutoSource.insets = new Insets(0, 0, 5, 5);
		gbc_lblAutoSource.gridx = 0;
		gbc_lblAutoSource.gridy = 3;
		tab0.add(lblAutoSource, gbc_lblAutoSource);

		btnConnect.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					if(btnConnect.isEnabled())
					{
						if(queue.getSockets().isEmpty())
						{
							ch.forEach((k, v)->
							{
								ThreadedSocket ms = new ThreadedSocket(v, 23);
								queue.getSockets().add(ms);
							});
							queue.getSockets().forEach(elem->new Thread(elem).start());
							queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "src", "4!"));
							try
							{
								Thread.sleep(ThreadedSocket.TIMEOUT);
							}
							catch(InterruptedException e1)
							{
								e1.printStackTrace();
							}
							queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1"));
						}
						btnConnect.setEnabled(false);
						btnDisconnect.setEnabled(true);
						statuspanel.setBackground(Color.GREEN);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{

				if(!Constants.definePressed)
				{
					if(btnConnect.isEnabled())
					{
						if(queue.getSockets().isEmpty())
						{
							ch.forEach((k, v)->
							{
								ThreadedSocket ms = new ThreadedSocket(v, 23);
								queue.getSockets().add(ms);
							});
							queue.getSockets().forEach(elem->new Thread(elem).start());
							queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "src", "4!"));
							try
							{
								Thread.sleep(ThreadedSocket.TIMEOUT);
							}
							catch(InterruptedException e1)
							{
								e1.printStackTrace();
							}
							queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1"));
						}
						btnConnect.setEnabled(false);
						btnDisconnect.setEnabled(true);
						statuspanel.setBackground(Color.GREEN);
					}
				}
			}
		});
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 1;
		gbc_btnConnect.gridy = 1;
		tab0.add(btnConnect, gbc_btnConnect);

		btnDisconnect.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					if(btnDisconnect.isEnabled())
					{
						queue.getSockets().forEach(s->s.deinitSocket());
						queue.getSockets().clear();
						btnConnect.setEnabled(true);
						btnDisconnect.setEnabled(false);
						statuspanel.setBackground(Color.RED);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					if(btnDisconnect.isEnabled())
					{
						queue.getSockets().forEach(s->s.deinitSocket());
						queue.getSockets().clear();
						btnConnect.setEnabled(true);
						btnDisconnect.setEnabled(false);
						statuspanel.setBackground(Color.RED);
					}
				}
			}
		});
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnDisconnect.gridx = 2;
		gbc_btnDisconnect.gridy = 1;
		tab0.add(btnDisconnect, gbc_btnDisconnect);

		btnPowerOff.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "pwr", "0!"));

					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(5 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "pwr", "0!"));

					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(5 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnPowerOff = new GridBagConstraints();
		gbc_btnPowerOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPowerOff.insets = new Insets(0, 0, 5, 5);
		gbc_btnPowerOff.anchor = GridBagConstraints.NORTH;
		gbc_btnPowerOff.gridx = 2;
		gbc_btnPowerOff.gridy = 2;
		tab0.add(btnPowerOff, gbc_btnPowerOff);

		btnPowerOn.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "pwr", "1!"));

					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(5 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
					try
					{
						Thread.sleep(ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1"));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "pwr", "1!"));

					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(5 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
					try
					{
						Thread.sleep(ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1"));
				}
			}
		});
		GridBagConstraints gbc_btnPowerOn = new GridBagConstraints();
		gbc_btnPowerOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPowerOn.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_btnPowerOn.insets = new Insets(0, 0, 5, 5);
		gbc_btnPowerOn.gridx = 1;
		gbc_btnPowerOn.gridy = 2;
		tab0.add(btnPowerOn, gbc_btnPowerOn);

		btnSrcAutoOff.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "asc", "0!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "asc", "0!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnSrcAutoOff = new GridBagConstraints();
		gbc_btnSrcAutoOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSrcAutoOff.insets = new Insets(0, 0, 5, 5);
		gbc_btnSrcAutoOff.gridx = 2;
		gbc_btnSrcAutoOff.gridy = 3;
		tab0.add(btnSrcAutoOff, gbc_btnSrcAutoOff);

		btnSrcAutoOn.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "asc", "1!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "asc", "1!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnSrcAutoOn = new GridBagConstraints();
		gbc_btnSrcAutoOn.anchor = GridBagConstraints.SOUTH;
		gbc_btnSrcAutoOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSrcAutoOn.insets = new Insets(0, 0, 5, 5);
		gbc_btnSrcAutoOn.gridx = 1;
		gbc_btnSrcAutoOn.gridy = 3;
		tab0.add(btnSrcAutoOn, gbc_btnSrcAutoOn);

		btnSrcHDMI.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "src", "4!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "src", "4!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnSrcHDMI = new GridBagConstraints();
		gbc_btnSrcHDMI.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSrcHDMI.insets = new Insets(0, 0, 5, 5);
		gbc_btnSrcHDMI.gridx = 2;
		gbc_btnSrcHDMI.gridy = 4;
		tab0.add(btnSrcHDMI, gbc_btnSrcHDMI);

		btnResolution43.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "2!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "2!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		
		btnStartVideo = new JButton("Start Video");
		btnStartVideo.setEnabled(false);
		btnStartVideo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_btnStartVideo = new GridBagConstraints();
		gbc_btnStartVideo.insets = new Insets(0, 0, 0, 5);
		gbc_btnStartVideo.gridx = 0;
		gbc_btnStartVideo.gridy = 7;
		tab0.add(btnStartVideo, gbc_btnStartVideo);
		GridBagConstraints gbc_btnResolution43 = new GridBagConstraints();
		gbc_btnResolution43.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnResolution43.insets = new Insets(0, 0, 0, 5);
		gbc_btnResolution43.gridx = 2;
		gbc_btnResolution43.gridy = 7;
		tab0.add(btnResolution43, gbc_btnResolution43);
		
		/**
		 * Event sent to start projection 
		 */
		btnStartVideo.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseReleased(MouseEvent e)
			{}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "10.13.1.110", "startproj", "on"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "10.13.1.11", "start", "1!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});

		btnResolution1610.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "6!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "6!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnResolution1610 = new GridBagConstraints();
		gbc_btnResolution1610.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnResolution1610.insets = new Insets(0, 0, 0, 5);
		gbc_btnResolution1610.gridx = 1;
		gbc_btnResolution1610.gridy = 7;
		tab0.add(btnResolution1610, gbc_btnResolution1610);

		btnResolutionNative.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "1!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnResolutionNative = new GridBagConstraints();
		gbc_btnResolutionNative.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnResolutionNative.insets = new Insets(0, 0, 5, 5);
		gbc_btnResolutionNative.gridx = 1;
		gbc_btnResolutionNative.gridy = 6;
		tab0.add(btnResolutionNative, gbc_btnResolutionNative);

		btnCeilOff.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					if(rdbtnDanger.isSelected())
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "cel", "0!"));
						component.forEach(d->d.setEnabled(false));
						try
						{
							Thread.sleep(2 * ThreadedSocket.TIMEOUT);
						}
						catch(InterruptedException e1)
						{
							e1.printStackTrace();
						}
						component.forEach(d->d.setEnabled(true));
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					if(rdbtnDanger.isSelected())
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "cel", "0!"));
						component.forEach(d->d.setEnabled(false));
						try
						{
							Thread.sleep(2 * ThreadedSocket.TIMEOUT);
						}
						catch(InterruptedException e1)
						{
							e1.printStackTrace();
						}
						component.forEach(d->d.setEnabled(true));
					}
				}
			}
		});
		GridBagConstraints gbc_btnCeilOff = new GridBagConstraints();
		gbc_btnCeilOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCeilOff.insets = new Insets(0, 0, 5, 5);
		gbc_btnCeilOff.gridx = 2;
		gbc_btnCeilOff.gridy = 5;
		tab0.add(btnCeilOff, gbc_btnCeilOff);

		btnCeilOn.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					if(rdbtnDanger.isSelected())
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "cel", "1!"));
						component.forEach(d->d.setEnabled(false));
						try
						{
							Thread.sleep(2 * ThreadedSocket.TIMEOUT);
						}
						catch(InterruptedException e1)
						{
							e1.printStackTrace();
						}
						component.forEach(d->d.setEnabled(true));
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					if(rdbtnDanger.isSelected())
					{
						queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "cel", "1!"));
						component.forEach(d->d.setEnabled(false));
						try
						{
							Thread.sleep(2 * ThreadedSocket.TIMEOUT);
						}
						catch(InterruptedException e1)
						{
							e1.printStackTrace();
						}
						component.forEach(d->d.setEnabled(true));
					}
				}
			}
		});
		GridBagConstraints gbc_btnCeilOn = new GridBagConstraints();
		gbc_btnCeilOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCeilOn.insets = new Insets(0, 0, 5, 5);
		gbc_btnCeilOn.gridx = 1;
		gbc_btnCeilOn.gridy = 5;
		tab0.add(btnCeilOn, gbc_btnCeilOn);

		btnResolution169.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if(Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "3!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(!Constants.definePressed)
				{
					queue.getMessagequeue().add(new CommandEvent("localhost", "0.0.0.0", "arz", "3!"));
					component.forEach(d->d.setEnabled(false));
					try
					{
						Thread.sleep(1 * ThreadedSocket.TIMEOUT);
					}
					catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
					component.forEach(d->d.setEnabled(true));
				}
			}
		});
		GridBagConstraints gbc_btnResolution169 = new GridBagConstraints();
		gbc_btnResolution169.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnResolution169.insets = new Insets(0, 0, 5, 5);
		gbc_btnResolution169.gridx = 2;
		gbc_btnResolution169.gridy = 6;
		tab0.add(btnResolution169, gbc_btnResolution169);

		component.add(btnCeilOff);
		component.add(btnCeilOn);
		component.add(btnPowerOff);
		component.add(btnPowerOn);
		component.add(btnResolution1610);
		component.add(btnResolution43);
		component.add(btnResolution169);
		component.add(btnResolutionNative);
		component.add(btnSrcAutoOff);
		component.add(btnSrcAutoOn);
		component.add(btnSrcHDMI);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		mntmExit = new JMenuItem("Exit");
		mnNewMenu.add(mntmExit);
		mntmExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				queue.getSockets().forEach(s->s.deinitSocket());
				queue.getSockets().forEach(s->Logger.log("Application is closing socket for shutdown."));
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		this.setVisible(true);
	}

	/**
	 * Add a listener to the IIntercom interface. Mainly used for update
	 * purposes.
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param l
	 *            object
	 */
	public void addListener(IIntercom l)
	{
		communicationListener.add(l);
	}

	/**
	 * Implementation of the updateLogWindow(String s) method present in the
	 * IIntercom interface.
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param s
	 *            which is the message to send to the log panel
	 */
	@Override
	public void updateLogWindow(String s)
	{
		textArea.append(s);
	}

	/**
	 * Check and split the command when it's a specific command which isn't
	 * present in the main tab.
	 * 
	 * @author Mikael Follonier mikael.follonier@hevs.ch
	 * 
	 * @param input
	 *            the command coming from the text fields in option tab
	 * @return containing the command in several fields otherwise null if the
	 *         format isn't respected.
	 */
	public String[] checkCommand(String input)
	{
		String[] out = input.split("#");
		if(out.length == 3)
		{
			return out;
		}
		else
		{
			return null;
		}
	}
}

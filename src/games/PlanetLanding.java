package games;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

public class PlanetLanding extends JFrame{
	//GUI
	private File tmp;
	private JFrame mainFrame;
	private ImagePanel framePanel;
	private ViewerPanel viewerPanel;
	private JPanel controlPanel;
	private JLabel timeLabel;
	private JTextField timeTextField;
	private JLabel positionLabel;
	private JLabel speedLabel;
	private JLabel altitudeLabel;
	private JTextField altitudePositionTextField;
	private JTextField altitudeSpeedTextField;
	private JLabel lateralLabel;
	private JTextField lateralPositionTextField;
	private JTextField lateralSpeedTextField;
	private JPanel fuelPanel;
	private TrajectoryPanel trajectoryPanel;
	private GuidePanel guidePanel;
	private JButton leftThrustButton;
	private JButton downThrustButton;
	private JButton rightThrustButton;
	private JLabel messageLabel;
	private JLabel fuelLabel;
	private JProgressBar fuelProgressBar;
	private JButton startPauseButton;
	private JButton exitStopButton;
	private JButton completeButton;
	private JPanel optionsPanel;	
	private JPanel pilotPanel;
	private JPanel unitsPanel;
	private ButtonGroup pilotButtonGroup;
	private JRadioButton beginnerRadioButton;
	private JRadioButton noviceRadioButton;
	private JRadioButton juniorRadioButton;
	private JRadioButton seniorRadioButton;
	private JRadioButton advancedRadioButton;
	private JCheckBox autoPilotCheckBox;
	private ButtonGroup unitsButtonGroup;
	private JRadioButton metricRadioButton;
	private JRadioButton usRadioButton;
	private JCheckBox muteSoundCheckBox;
	private int muteSoundCheckBoxClicksCounter;
	private int incrementR;
	private int incrementG;
	private int incrementB;
	
	//Logic
	private static Thread gameSoundThread;
	private int pilotLevel;
	private double multiplier;
	private Timer landingTimer;
	private double time;
	protected static double landerX, landerY;
	private double landerXSpeed, landerYSpeed;
	private final double xDelta;
	private final double yDelta;
	private final double gravity;
	private final double drag;
	protected static Image landscape;
	protected static double landscapeX;
	protected static double landscapeY;
	protected static Image lander;
	protected static Image landerCrashed;
	protected static Image pad;
	protected static Image hThrust;
	protected static Image vThrust;
	protected static int landscapeWidth,landscapeHeight;
	protected static int landerWidth, landerHeight;
	protected static int padWidth, padHeight;
	protected static int hThrustWidth, hThrustHeight;
	protected static int vThrustWidth, vThrustHeight;
	protected static double padX, padY;
	private double altitude, lateral;
	private Random myRandom;
	private final double safeLandingSpeed;
	private static double fuelRemaining;
	private final double maximumFuel;
	private final double hFuel;
	private final double vFuel;
	protected static double landerX0, landerY0;
	protected static Vector trajectoryPoints;
	protected static double landerXView, landerYView;
	private double centerX, centerY;
	protected static boolean vThrustOn, lThrustOn, rThrustOn;
	private double altitude0, lateral0, miss;
	
	public PlanetLanding() {
			multiplier=1.0;
			xDelta=1.2;
			yDelta=2.4;
			gravity=9.8/6.0;
			drag=0.1;		
			myRandom=new Random();
			safeLandingSpeed=3.0;	
			maximumFuel=100;
			hFuel=1.0;
			vFuel=2.0;
		if(!Files.isRegularFile(Path.of("files\\.tmp"))) {
			tmp=new File("files\\.tmp");
			
			try {
				tmp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			pilotLevel=1;
			multiplier=1.0;			
			
			mainFrame = new JFrame();
			mainFrame.setTitle("PlanetLanding");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setIconImage(new ImageIcon("image\\icon\\Emblem002.png").getImage());
			mainFrame.setResizable(false);		
			mainFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					tmp.delete();
					System.exit(0);
				}
			});		
			mainFrame.setLayout(new GridBagLayout());
			GridBagConstraints gridConstraints = new GridBagConstraints();
			
			framePanel = new ImagePanel(new ImageIcon("image\\panels\\framePanel.gif").getImage());
			framePanel.setPreferredSize(new Dimension(600, 750));
			framePanel.setBackground(new Color(20, 20, 20));
			framePanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			gridConstraints.gridheight = 2;
			mainFrame.add(framePanel, gridConstraints);		
			
			viewerPanel = new ViewerPanel();
			viewerPanel.setPreferredSize(new Dimension(580, 730));
			viewerPanel.setBackground(Color.WHITE);
			viewerPanel.setVisible(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			framePanel.add(viewerPanel, gridConstraints);		
			
			controlPanel = new JPanel();
			controlPanel.setPreferredSize(new Dimension(400, 650));
			controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
									"Landing Control-Beginner", 0, 0, new Font("Aerial", Font.BOLD, 16), new Color(0xAF0000)));
			controlPanel.setBackground(new Color(20, 20, 20));
			controlPanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 0;
			gridConstraints.gridwidth = 2;
			gridConstraints.anchor = GridBagConstraints.NORTH;
			mainFrame.add(controlPanel, gridConstraints);
			mainFrame.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e){
					thrustButtonKeyReleased(e);
				}
			});
			
			timeLabel = new JLabel();
			timeLabel.setPreferredSize(new Dimension(75, 25));
			timeLabel.setText("Time (s)");
			timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
			timeLabel.setForeground(Color.WHITE);
			timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			gridConstraints.weightx = 0.1;
			gridConstraints.insets = new Insets(0, 0, 10, 0);
			controlPanel.add(timeLabel, gridConstraints);
			
			timeTextField = new JTextField();
			timeTextField.setPreferredSize(new Dimension(120, 25));
			timeTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			timeTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			timeTextField.setText("0.0");
			timeTextField.setEditable(false);
			timeTextField.setBackground(Color.DARK_GRAY);
			timeTextField.setForeground(Color.WHITE);		
			timeTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 0;
			gridConstraints.weightx = 0.9;
			gridConstraints.insets = new Insets(0, 0, 10, 10);
			controlPanel.add(timeTextField, gridConstraints);
			
			positionLabel = new JLabel();
			positionLabel.setPreferredSize(new Dimension(100, 25));
			positionLabel.setText("Position (m)");
			positionLabel.setFont(new Font("Arial", Font.BOLD, 14));
			positionLabel.setForeground(Color.ORANGE);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 1;
			gridConstraints.insets = new Insets(0, 10, 0, 0);
			controlPanel.add(positionLabel, gridConstraints);
			
			speedLabel = new JLabel();
			speedLabel.setPreferredSize(new Dimension(100, 25));
			speedLabel.setText("Speed (m/s)");
			speedLabel.setFont(new Font("Arial", Font.BOLD, 14));
			speedLabel.setForeground(Color.GREEN);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 2;
			gridConstraints.gridy = 1;
			gridConstraints.insets = new Insets(0, 0, 0, 30);
			controlPanel.add(speedLabel, gridConstraints);
			
			altitudeLabel = new JLabel();
			altitudeLabel.setPreferredSize(new Dimension(75, 25));
			altitudeLabel.setText("Altitude");
			altitudeLabel.setFont(new Font("Arial", Font.BOLD, 14));
			altitudeLabel.setForeground(Color.WHITE);
			altitudeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 2;
			gridConstraints.insets = new Insets(0, 0, 20, 0);
			controlPanel.add(altitudeLabel, gridConstraints);
			
			altitudePositionTextField = new JTextField();
			altitudePositionTextField.setPreferredSize(new Dimension(120, 25));
			altitudePositionTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			altitudePositionTextField.setText("0.0");
			altitudePositionTextField.setEditable(false);
			altitudePositionTextField.setBackground(Color.DARK_GRAY);
			altitudePositionTextField.setForeground(Color.WHITE);
			altitudePositionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			altitudePositionTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 2;
			gridConstraints.insets = new Insets(0, 0, 20, 10);
			controlPanel.add(altitudePositionTextField, gridConstraints);
			
			altitudeSpeedTextField = new JTextField();
			altitudeSpeedTextField.setPreferredSize(new Dimension(120, 25));
			altitudeSpeedTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			altitudeSpeedTextField.setText("0.0");
			altitudeSpeedTextField.setEditable(false);
			altitudeSpeedTextField.setBackground(Color.DARK_GRAY);
			altitudeSpeedTextField.setForeground(Color.WHITE);
			altitudeSpeedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			altitudeSpeedTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 2;
			gridConstraints.gridy = 2;
			gridConstraints.insets = new Insets(0, 0, 20, 30);
			controlPanel.add(altitudeSpeedTextField, gridConstraints);
			
			lateralLabel = new JLabel();
			lateralLabel.setPreferredSize(new Dimension(75, 25));
			lateralLabel.setText("Lateral");
			lateralLabel.setFont(new Font("Arial", Font.BOLD, 14));
			lateralLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			lateralLabel.setForeground(Color.WHITE);		
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 3;
			gridConstraints.insets = new Insets(0, 0, 20, 0);
			controlPanel.add(lateralLabel, gridConstraints);
			
			lateralPositionTextField = new JTextField();
			lateralPositionTextField.setPreferredSize(new Dimension(120, 25));
			lateralPositionTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lateralPositionTextField.setText("0.0");
			lateralPositionTextField.setEditable(false);
			lateralPositionTextField.setBackground(Color.DARK_GRAY);
			lateralPositionTextField.setForeground(Color.WHITE);
			lateralPositionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			lateralPositionTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 3;
			gridConstraints.insets = new Insets(0, 0, 20, 10);
			controlPanel.add(lateralPositionTextField, gridConstraints);
			
			lateralSpeedTextField = new JTextField();
			lateralSpeedTextField.setPreferredSize(new Dimension(120, 25));
			lateralSpeedTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lateralSpeedTextField.setText("0.0");
			lateralSpeedTextField.setEditable(false);
			lateralSpeedTextField.setBackground(Color.DARK_GRAY);
			lateralSpeedTextField.setForeground(Color.WHITE);
			lateralSpeedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			lateralSpeedTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 2;
			gridConstraints.gridy = 3;
			gridConstraints.insets = new Insets(0, 0, 20, 30);
			controlPanel.add(lateralSpeedTextField, gridConstraints);
			
			fuelPanel = new JPanel();
			fuelPanel.setPreferredSize(new Dimension(350, 30));	
			fuelPanel.setBackground(new Color(0x202020));		
			fuelPanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 4;
			gridConstraints.gridwidth = 3;
			gridConstraints.insets = new Insets(0, 0, 10, 0);
			controlPanel.add(fuelPanel, gridConstraints);
			
			fuelLabel = new JLabel();
			fuelLabel.setText("Fuel");
			fuelLabel.setFont(new Font("Arial", Font.BOLD, 14));
			fuelLabel.setForeground(Color.WHITE);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			gridConstraints.insets = new Insets(0, 20, 0, 10);
			fuelPanel.add(fuelLabel, gridConstraints);
			
			fuelProgressBar = new JProgressBar();
			fuelProgressBar.setPreferredSize(new Dimension(260, 25));		
			fuelProgressBar.setMinimum(0);
			fuelProgressBar.setMaximum(100);
			fuelProgressBar.setValue(100);
			fuelProgressBar.setBackground(new Color(0x202020));
			fuelProgressBar.setForeground(new Color(0x0026FF));
			fuelProgressBar.setBorderPainted(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 0;
			gridConstraints.insets = new Insets(0, 10, 0, 0);
			fuelPanel.add(fuelProgressBar, gridConstraints);
			
			trajectoryPanel = new TrajectoryPanel();
			trajectoryPanel.setPreferredSize(new Dimension(380, 250));
			trajectoryPanel.setBackground(Color.BLACK);
			trajectoryPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x2A4F00)));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 5;
			gridConstraints.gridwidth = 3;
			gridConstraints.insets = new Insets(10, 0, 0, 0);
			controlPanel.add(trajectoryPanel, gridConstraints);
			
			guidePanel = new GuidePanel();
			guidePanel.setPreferredSize(new Dimension(380, 50));
			guidePanel.setBackground(new Color(30,30,30));
			guidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x000A8F)));
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 6;
			gridConstraints.gridwidth = 3;
			gridConstraints.insets = new Insets(0, 0, 10, 0);
			controlPanel.add(guidePanel, gridConstraints);
						
			leftThrustButton = new JButton(new ImageIcon("image\\buttons\\leftarrow.gif"));
			leftThrustButton.setPreferredSize(new Dimension(70, 32));
			leftThrustButton.setBackground(new Color(60,60,60));
			leftThrustButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0x505050), Color.BLACK));
			leftThrustButton.setFocusable(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 7;
			gridConstraints.insets = new Insets(20, 40, 0, 0);		
			controlPanel.add(leftThrustButton, gridConstraints);
			leftThrustButton.addActionListener(new ActionListener()	{
				public void actionPerformed(ActionEvent e) {
					leftThrustButtonActionPerformed(e);
				}
			});			
			
			downThrustButton = new JButton(new ImageIcon("image\\buttons\\downarrow.gif"));
			downThrustButton.setPreferredSize(new Dimension(70, 32));
			downThrustButton.setBackground(new Color(60,60,60));
			downThrustButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0x505050), Color.BLACK));
			downThrustButton.setFocusable(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 7;
			gridConstraints.insets = new Insets(20, 50, 0, 0);
			controlPanel.add(downThrustButton, gridConstraints);
			downThrustButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					downThrustButtonActionPerformed(e);
				}
			});
			
			rightThrustButton = new JButton(new ImageIcon("image\\buttons\\rightarrow.gif"));
			rightThrustButton.setPreferredSize(new Dimension(70, 32));
			rightThrustButton.setBackground(new Color(60,60,60));
			rightThrustButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0x505050), Color.BLACK));
			rightThrustButton.setFocusable(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 2;
			gridConstraints.gridy = 7;
			gridConstraints.insets = new Insets(20, 10, 0, 0);		
			controlPanel.add(rightThrustButton, gridConstraints);
			rightThrustButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					rightThrustButtonActionPerformed(e);
				}
			});
			
			messageLabel = new JLabel();
			messageLabel.setText("Auto-Pilot On");
			messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
			messageLabel.setForeground(Color.GREEN);
			messageLabel.setVisible(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 7;
			gridConstraints.gridwidth = 3;
			gridConstraints.insets = new Insets(25, 0, 10, 0);
			controlPanel.add(messageLabel, gridConstraints);
			
			startPauseButton = new JButton();
			startPauseButton.setText("Start");
			startPauseButton.setFocusable(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 1;
			gridConstraints.insets = new Insets(0, 90, 0, 0);
			mainFrame.add(startPauseButton, gridConstraints);
			startPauseButton.addActionListener(new ActionListener()	{
				public void actionPerformed(ActionEvent e){
					startPauseButtonActionPerformed(e);
				}
			});
			
			exitStopButton = new JButton();
			exitStopButton.setText("Exit");
			exitStopButton.setFocusable(false);
			exitStopButton.setPreferredSize(startPauseButton.getPreferredSize());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 2;
			gridConstraints.gridy = 1;
			gridConstraints.insets = new Insets(0, 0, 0, 0);
			mainFrame.add(exitStopButton, gridConstraints);
			exitStopButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					exitStopButtonActionPerformed(e);
				}
			});
			
			completeButton = new JButton();
			completeButton.setText("Landing Complete-Click to Continue");
			completeButton.setFocusable(false);
			completeButton.setVisible(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 1;
			gridConstraints.gridwidth = 2;
			gridConstraints.insets = new Insets(0, 10, 0, 10);
			mainFrame.add(completeButton, gridConstraints);
			completeButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					completeButtonActionPerformed(e);
				}
			});
			
			controlPanel.setVisible(false);
			
			optionsPanel = new JPanel();
			optionsPanel.setPreferredSize(new Dimension(400, 650));
			optionsPanel.setBackground(new Color(20, 20, 20));		
			optionsPanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 1;
			gridConstraints.gridy = 0;
			gridConstraints.gridwidth = 2;
			gridConstraints.anchor = GridBagConstraints.EAST;
			mainFrame.add(optionsPanel, gridConstraints);
			
			pilotPanel = new JPanel();
			pilotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(150, 0, 0)),
					"Pilot level", 2, 2, new Font("Aerial", Font.BOLD, 12), Color.WHITE)));
			pilotPanel.setPreferredSize(new Dimension(200, 300));
			pilotPanel.setBackground(new Color(40, 40, 40));		
			pilotPanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			optionsPanel.add(pilotPanel, gridConstraints);
			
			unitsPanel = new JPanel();
			unitsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(150, 0, 0)),
					"Units", 2, 5, new Font("Aerial", Font.BOLD, 12), Color.WHITE)));
			unitsPanel.setPreferredSize(new Dimension(200, 150));
			unitsPanel.setBackground(new Color(40, 40, 40));
			unitsPanel.setLayout(new GridBagLayout());
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 1;
			gridConstraints.insets = new Insets(10, 0, 0, 0);
			optionsPanel.add(unitsPanel, gridConstraints);
			
			muteSoundCheckBox = new JCheckBox("Mute", false);
			muteSoundCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			muteSoundCheckBox.setFont(new Font("TAHOMA", Font.BOLD, 14));
			muteSoundCheckBox.setOpaque(false);		
			muteSoundCheckBox.setForeground(new Color(0xFFFFFF));			
			muteSoundCheckBox.setFocusable(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 2;
			gridConstraints.insets = new Insets(30, 10, 10, 10);
			gridConstraints.anchor = GridBagConstraints.EAST;
			optionsPanel.add(muteSoundCheckBox, gridConstraints);
			muteSoundCheckBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					muteSoundCheckBoxItemStateChanged(e);				
				}			
			});
					
			pilotButtonGroup = new ButtonGroup();
			beginnerRadioButton = new JRadioButton();
			beginnerRadioButton.setText("Beginner");
			beginnerRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			beginnerRadioButton.setForeground(Color.WHITE);
			beginnerRadioButton.setFocusable(false);
			beginnerRadioButton.setOpaque(false);
			beginnerRadioButton.setSelected(true);
			pilotButtonGroup.add(beginnerRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(beginnerRadioButton, gridConstraints);
			beginnerRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					pilotRadioButtonActionPerformed(e);
				}
			});
				
			noviceRadioButton = new JRadioButton();
			noviceRadioButton.setText("Novice");	
			noviceRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			noviceRadioButton.setForeground(Color.WHITE);
			noviceRadioButton.setFocusable(false);
			noviceRadioButton.setOpaque(false);
			pilotButtonGroup.add(noviceRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 1;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(noviceRadioButton, gridConstraints);
			noviceRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					pilotRadioButtonActionPerformed(e);
				}
			});
					
			juniorRadioButton = new JRadioButton();
			juniorRadioButton.setText("Junior");
			juniorRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			juniorRadioButton.setForeground(Color.WHITE);
			juniorRadioButton.setFocusable(false);
			juniorRadioButton.setOpaque(false);
			pilotButtonGroup.add(juniorRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 2;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(juniorRadioButton, gridConstraints);
			juniorRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)		{		
					pilotRadioButtonActionPerformed(e);
				}
			});
			
			seniorRadioButton = new JRadioButton();
			seniorRadioButton.setText("Senior");	
			seniorRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			seniorRadioButton.setForeground(Color.WHITE);
			seniorRadioButton.setFocusable(false);
			seniorRadioButton.setOpaque(false);
			pilotButtonGroup.add(seniorRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 3;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(seniorRadioButton, gridConstraints);
			seniorRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					pilotRadioButtonActionPerformed(e);
				}
			});
			
			advancedRadioButton = new JRadioButton();
			advancedRadioButton.setText("Advanced");
			advancedRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			advancedRadioButton.setForeground(Color.WHITE);
			advancedRadioButton.setFocusable(false);
			advancedRadioButton.setOpaque(false);
			pilotButtonGroup.add(advancedRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 4;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(advancedRadioButton, gridConstraints);
			advancedRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					pilotRadioButtonActionPerformed(e);
				}
			});
			
			autoPilotCheckBox = new JCheckBox();
			autoPilotCheckBox.setFocusable(false);
			autoPilotCheckBox.setText("Auto Pilot Toggle");	
			autoPilotCheckBox.setFont(new Font("Aerial", Font.BOLD, 14));
			autoPilotCheckBox.setForeground(Color.WHITE);			
			autoPilotCheckBox.setOpaque(false);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 5;
			gridConstraints.anchor = GridBagConstraints.WEST;
			pilotPanel.add(autoPilotCheckBox, gridConstraints);
			
			unitsButtonGroup = new ButtonGroup();		
			metricRadioButton = new JRadioButton();
			metricRadioButton.setText("Metric");
			metricRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			metricRadioButton.setForeground(Color.WHITE);
			metricRadioButton.setFocusable(false);
			metricRadioButton.setOpaque(false);
			metricRadioButton.setSelected(true);
			unitsButtonGroup.add(metricRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 0;
			gridConstraints.anchor = GridBagConstraints.WEST;
			unitsPanel.add(metricRadioButton, gridConstraints);
			metricRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					unitsRadioButtonActionPerformed(e);
				}
			});
			
			usRadioButton = new JRadioButton();
			usRadioButton.setText("US (English)");		
			usRadioButton.setFont(new Font("Aerial", Font.BOLD, 14));
			usRadioButton.setForeground(Color.WHITE);
			usRadioButton.setFocusable(false);
			usRadioButton.setOpaque(false);
			unitsButtonGroup.add(usRadioButton);
			gridConstraints = new GridBagConstraints();
			gridConstraints.gridx = 0;
			gridConstraints.gridy = 1;
			gridConstraints.anchor = GridBagConstraints.WEST;
			unitsPanel.add(usRadioButton, gridConstraints);
			usRadioButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					unitsRadioButtonActionPerformed(e);
				}
			});
			
			landingTimer = new Timer(50, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					landingTimerActionPerformed(e);
				}
			});
			
			mainFrame.pack();
			mainFrame.setVisible(true);
			mainFrame.setLocationRelativeTo(null);
			GUIBorderFlash();
			
			landscape=new ImageIcon("image\\planet\\planet1.gif").getImage();
			landscapeWidth=landscape.getWidth(this);
			landscapeHeight=landscape.getHeight(this);
			lander=new ImageIcon("image\\starship\\starship1_400x200.gif").getImage();			
			landerWidth=lander.getWidth(this); 
			landerHeight=lander.getHeight(this);
			landerCrashed=new ImageIcon("image\\starship\\starship1_crashed_400x200.gif").getImage();
			pad=new ImageIcon("image\\landingpad\\landingpad1_400x183.gif").getImage();
			padWidth=pad.getWidth(this);
			padHeight=pad.getHeight(this);
			hThrust=new ImageIcon("image\\thrusters\\hThrust.gif").getImage();
			hThrustWidth=hThrust.getWidth(this);
			hThrustHeight=hThrust.getHeight(this);
			vThrust=new ImageIcon("image\\thrusters\\vThrust.gif").getImage();
			vThrustWidth=vThrust.getWidth(this);
			vThrustHeight=vThrust.getHeight(this);	
			
			trajectoryPoints=new Vector(50,10);			
		} else {
			System.exit(0);
		}
		
	}

	public static void main(String[] args) {
		new PlanetLanding();		
		GameSound gs = new GameSound();
		gameSoundThread = new Thread(gs);
		gameSoundThread.setDaemon(true);
		gameSoundThread.start();		
	}	
	
	public void leftThrustButtonActionPerformed(ActionEvent e) {
		if(fuelRemaining>0) {
			lThrustOn=true;
			if(muteSoundCheckBoxClicksCounter%2==0) {
				GameSound.thrusterSoundClip.setFramePosition(0);
				GameSound.thrusterSoundClip.start();
			}
			if (pilotLevel!=5) landerXSpeed+=xDelta;
			else landerXSpeed+=2.0*xDelta*myRandom.nextDouble();
			if(pilotLevel>3) {
				fuelRemaining-=hFuel;
			}
		}
	}
	
	public void downThrustButtonActionPerformed(ActionEvent e) {
		if(fuelRemaining>0) {
			vThrustOn=true;
			if(muteSoundCheckBoxClicksCounter%2==0) {
				GameSound.thrusterSoundClip.setFramePosition(0);
				GameSound.thrusterSoundClip.start();
			}
			if (pilotLevel!=5) landerYSpeed-=yDelta;
			else landerYSpeed-=2.0*yDelta*myRandom.nextDouble();
			if(pilotLevel>3) {
				fuelRemaining-=vFuel;
			}
		}		
	}
	
	public void rightThrustButtonActionPerformed(ActionEvent e) {
		if(fuelRemaining>0) {
			rThrustOn=true;
			if(muteSoundCheckBoxClicksCounter%2==0) {
				GameSound.thrusterSoundClip.setFramePosition(0);
				GameSound.thrusterSoundClip.start();
			}
			if (pilotLevel!=5) landerXSpeed -= xDelta;
			else landerXSpeed-=2.0*xDelta*myRandom.nextDouble();
			if(pilotLevel>3) {
				fuelRemaining-=hFuel;
			}
		}
	}
	
	public void startPauseButtonActionPerformed(ActionEvent e) {
		if (startPauseButton.getText().equals("Start")) {
			startPauseButton.setText("Pause");
			exitStopButton.setText("Stop");
			viewerPanel.setVisible(true);
			optionsPanel.setVisible(false);
			controlPanel.setVisible(true);
			lander=new ImageIcon("image\\starship\\starship1_400x200.gif").getImage();
			if(muteSoundCheckBoxClicksCounter%2==0) {
				GameSound.welcomeFrameSoundClip.stop();	
				GameSound.mainGameSoundClip.setFramePosition(0);
				GameSound.mainGameSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
			}			
			switch (pilotLevel) {
				case 1: 
					controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
						"Landing Control-Beginner", 0, 0, new Font("Aerial", Font.BOLD, 16), Color.BLUE));
					leftThrustButton.setVisible(false);
					rightThrustButton.setVisible(false);
					downThrustButton.setVisible(true);
					fuelLabel.setVisible(false);
					fuelProgressBar.setVisible(false);
					break;
				case 2:
					controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
							"Landing Control-Novice", 0, 0, new Font("Aerial", Font.BOLD, 16), Color.GREEN));
					leftThrustButton.setVisible(true);
					rightThrustButton.setVisible(true);
					downThrustButton.setVisible(false);
					fuelLabel.setVisible(false);
					fuelProgressBar.setVisible(false);
					break;
				case 3:
					controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
							"Landing Control-Junior", 0, 0, new Font("Aerial", Font.BOLD, 16), Color.ORANGE));
					leftThrustButton.setVisible(true);
					rightThrustButton.setVisible(true);
					downThrustButton.setVisible(true);
					fuelLabel.setVisible(false);
					fuelProgressBar.setVisible(false);
					break;
				case 4:
					controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
							"Landing Control-Senior", 0, 0, new Font("Aerial", Font.BOLD, 16), Color.WHITE));
					leftThrustButton.setVisible(true);
					rightThrustButton.setVisible(true);
					downThrustButton.setVisible(true);
					fuelLabel.setVisible(true);
					fuelProgressBar.setVisible(true);
					break;
				case 5:
					controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE),
							"Landing Control-Advanced", 0, 0, new Font("Aerial", Font.BOLD, 16), Color.RED));
					leftThrustButton.setVisible(true);
					rightThrustButton.setVisible(true);
					downThrustButton.setVisible(true);
					fuelLabel.setVisible(true);
					fuelProgressBar.setVisible(true);
					break;				
			}
			
			if (autoPilotCheckBox.isSelected()) {
				leftThrustButton.setVisible(false);
				rightThrustButton.setVisible(false);
				downThrustButton.setVisible(false);
				messageLabel.setText("Auto-Pilot On");
				messageLabel.setVisible(true);
			} else {
				messageLabel.setVisible(false);
			}
			
			if (metricRadioButton.isSelected()) {
				positionLabel.setText("Position (m)");
				speedLabel.setText("Speed (m/s)");
			} else {
				positionLabel.setText("Position (ft)");
				speedLabel.setText("Speed (ft/s)");
			}	
			
			time = 0.0;
			timeTextField.setText("0.0");
			landerX=(landscapeWidth-landerWidth)*myRandom.nextDouble();;
			landerY=0;
			landerXSpeed=0;
			landerYSpeed=0;
			if (pilotLevel!=1) padX=(landscapeWidth-padWidth)*myRandom.nextDouble();
			else padX=(landerX+landerWidth/2)-padWidth/2;
			padY=landscapeHeight-padHeight;
			fuelRemaining=maximumFuel;
			fuelProgressBar.setValue(100);
			updateStatus();
			trajectoryPoints.removeAllElements();
			updateTrajectory();
			updateViewer();
			viewerPanel.setVisible(true);
			landingTimer.start();
		} else if (startPauseButton.getText().equals("Pause")) {
			startPauseButton.setText("Continue");
			exitStopButton.setEnabled(false);
			leftThrustButton.setEnabled(false);
			downThrustButton.setEnabled(false);
			rightThrustButton.setEnabled(false);
			landingTimer.stop();
		} else {
			startPauseButton.setText("Pause");
			exitStopButton.setEnabled(true);
			leftThrustButton.setEnabled(true);
			downThrustButton.setEnabled(true);
			rightThrustButton.setEnabled(true);
			landingTimer.start();
		}
	}
	
	public void exitStopButtonActionPerformed(ActionEvent e) {
		if (exitStopButton.getText().equals("Stop")){
			exitStopButton.setText("Exit");
			startPauseButton.setText("Start");
			optionsPanel.setVisible(true);
			controlPanel.setVisible(false);
			viewerPanel.setVisible(false);			
			muteSoundCheckBox.setVisible(true);
			if(muteSoundCheckBoxClicksCounter%2==0) {
				GameSound.mainGameSoundClip.stop();
				GameSound.lowFuelSoundClip.stop();
				GameSound.welcomeFrameSoundClip.setFramePosition(0);
				GameSound.welcomeFrameSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			fuelProgressBar.setForeground(new Color(0x0026FF));
			landingTimer.stop();
		} else {
			tmp.delete();
			System.exit(0);			
		}
	}
	
	public void completeButtonActionPerformed(ActionEvent e) {
		if(muteSoundCheckBoxClicksCounter%2==0) {			
			GameSound.crashSoundClip.stop();			
			GameSound.successSoundClip.stop();		
		}
		completeButton.setVisible(false);
		startPauseButton.setVisible(true);
		exitStopButton.setVisible(true);
		exitStopButton.doClick();
	}
	
	public void pilotRadioButtonActionPerformed(ActionEvent e) {
		String t = e.getActionCommand();
		if(t.equals("Beginner")) pilotLevel=1;
		else if(t.equals("Novice")) pilotLevel=2;
		else if(t.equals("Junior")) pilotLevel=3;
		else if(t.equals("Senior")) pilotLevel=4;
		else if(t.equals("Advanced")) pilotLevel=5;
	}
	
	public void unitsRadioButtonActionPerformed(ActionEvent e) {
		String t=e.getActionCommand();
		if(t.equals("Metric"))	multiplier=1.0;
		else multiplier=3.2808;
	}
	
	public void landingTimerActionPerformed(ActionEvent e) {
		time+=(double)(landingTimer.getDelay())/1000;
		timeTextField.setText(new DecimalFormat("0.00").format(time));	
		landerX+=landerXSpeed*(double)(landingTimer.getDelay())/1000;
		landerY+=landerYSpeed*(double)(landingTimer.getDelay())/1000;
		updateStatus();
		updateTrajectory();
		updateViewer();
		// check for landing
		if (altitude<=0) {
			vThrustOn=false;
			lThrustOn=false;
			rThrustOn=false;
			updateViewer();
			landingTimer.stop();
			leftThrustButton.setVisible(false);
			rightThrustButton.setVisible(false);
			downThrustButton.setVisible(false);
			if (autoPilotCheckBox.isSelected())	messageLabel.setText("Auto-Pilot\s");
			else{
				messageLabel.setText("You\s");
				messageLabel.setVisible(true);
			}
			// crash?
			if (landerYSpeed>safeLandingSpeed) {
				if(muteSoundCheckBoxClicksCounter%2==0) {
					GameSound.crashSoundClip.setFramePosition(0);
					GameSound.crashSoundClip.start();		
				}
				lander=landerCrashed;
				messageLabel.setText(messageLabel.getText()+"Crashed!");
			}
			else {
				if(muteSoundCheckBoxClicksCounter%2==0) {					
					GameSound.successSoundClip.setFramePosition(0);
					GameSound.successSoundClip.start();		
				}
				messageLabel.setText(messageLabel.getText()+"Landed Safely");
			}
			// bring up complete button
			startPauseButton.setVisible(false);
			exitStopButton.setVisible(false);
			completeButton.setVisible(true);
			return;
		}
		// autopilot or Novice level - adjust vertical thrust
		if(autoPilotCheckBox.isSelected()||pilotLevel==2) {
			if(altitude>300) {
				if(landerYSpeed>12) downThrustButton.doClick();
			}
			else if(altitude>100) {
				if(landerYSpeed>6) downThrustButton.doClick();
			}
			else{	
				if(landerYSpeed>(2+0.04*altitude)) downThrustButton.doClick();
			}
		}
		
		// autopilot - adjust horizontal thrust
		if(autoPilotCheckBox.isSelected()) {
			miss=lateral-altitude*(lateral0/altitude0);
			if (miss>3)
			rightThrustButton.doClick();
			else if (miss<-3)
			leftThrustButton.doClick();
		}
		landerX+=landerXSpeed;
		landerY+=landerYSpeed;
		// horizontal drag
		if (landerXSpeed>0)
		landerXSpeed-=drag;
		else if (landerXSpeed<0)
		landerXSpeed+=drag;
		// gravity
		landerYSpeed+=gravity*(double)(landingTimer.getDelay())/1000;
	}
	
	private void updateStatus(){
		altitude = (landscapeHeight-padHeight*0.3)-(landerY+landerHeight);
		lateral = (landerX+landerWidth/2)-(padX+padWidth/2);
		if (altitude>0) altitudePositionTextField.setText(new DecimalFormat("0").format(altitude*multiplier));
		else altitudePositionTextField.setText("0");
		if (Math.abs(lateral)<1) lateralPositionTextField.setText("Above");
		else lateralPositionTextField.setText(new DecimalFormat("0").format(lateral*multiplier));
		altitudeSpeedTextField.setText(new DecimalFormat("0.0").format(-landerYSpeed*multiplier));
		lateralSpeedTextField.setText(new DecimalFormat("0.0").format(landerXSpeed*multiplier));
		// update fuel guage
		int fuelPercent=(int)(100*fuelRemaining/maximumFuel);
		fuelProgressBar.setValue(fuelPercent);
		if (fuelPercent<=50&&fuelPercent>20) {
			fuelProgressBar.setForeground(Color.YELLOW);
		}
		if (fuelPercent<=15) {
			if(muteSoundCheckBoxClicksCounter%2==0)GameSound.lowFuelSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
			fuelProgressBar.setForeground(Color.RED);
		}
		// update guide display
		guidePanel.repaint();
	}
	
	private void updateTrajectory(){
		if (time == 0.0) {
			landerX0 = landerX;
			landerY0 = landerY;
			altitude0 = altitude;
			lateral0 = lateral;
		}
		trajectoryPoints.add(new Point2D.Double(landerX+landerWidth/2, landerY+landerHeight));
		trajectoryPanel.repaint();
	}
	
	private void updateViewer() {	
		centerX=(viewerPanel.getWidth()-landerWidth)/2;		
		centerY=(viewerPanel.getHeight()-landerHeight)/2;		
		// adjust landscape background
		landscapeX=landerX-centerX;		
		landerXView=centerX;		
		if(landscapeX<=0) {
			landscapeX=0;
			landerXView=landerX;
		}
		if(landscapeX>=landscapeWidth-viewerPanel.getWidth()) {
			landscapeX=landscapeWidth-viewerPanel.getWidth();
			landerXView=landerX-landscapeX;
		}
		
		landscapeY=landerY-centerY;		
		landerYView=centerY;
		
		if(landscapeY<=0) {
			landscapeY=0;
			landerYView=landerY;
		}
		if(landscapeY>=landscapeHeight-viewerPanel.getHeight()) {
			landscapeY=landscapeHeight-viewerPanel.getHeight();
			landerYView=landerY-landscapeY;
		}		
		// draw landscape		
		viewerPanel.repaint();
	}
	
	public void muteSoundCheckBoxItemStateChanged(ItemEvent e) {		
		GameSound.welcomeFrameSoundClip.stop();	
		muteSoundCheckBoxClicksCounter+=1;
		muteSoundCheckBox.setText("Unmute");
		if(muteSoundCheckBoxClicksCounter%2==0) {
			GameSound.welcomeFrameSoundClip.start();	
			muteSoundCheckBox.setText("Mute");
			muteSoundCheckBoxClicksCounter=0;
		}
	}
	
	public void thrustButtonKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == e.VK_LEFT) {
			leftThrustButton.doClick();
		}
		if (e.getKeyCode() == e.VK_RIGHT) {
			rightThrustButton.doClick();
		}
		if (e.getKeyCode() == e.VK_DOWN) {
			downThrustButton.doClick();
		}
	}
	
	public void GUIBorderFlash() {
		int delay = 10000; 	
		  ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  incrementR = myRandom.nextInt(255);
		    	  incrementG = myRandom.nextInt(255);
		    	  incrementB = myRandom.nextInt(255);
		    	  pilotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color((incrementR+150)%255,
		    			  (incrementG+100)%255, (incrementB+50)%255)),
							"Pilot level", 2, 2, new Font("Aerial", Font.BOLD, 12), Color.WHITE)));
		    	  unitsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(incrementR,
		    			  incrementG, incrementB)),
							"Units", 2, 5, new Font("Aerial", Font.BOLD, 12), Color.WHITE)));
		      }
		  };new Timer(delay, taskPerformer).start();
	}
}

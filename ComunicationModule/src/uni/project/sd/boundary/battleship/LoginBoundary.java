package uni.project.sd.boundary.battleship;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import uni.project.sd.boundary.battleship.listener.LoginActionListener;

public class LoginBoundary {
	public final static boolean shouldFill = true;
	private JFrame loginWindow;
	private JTextField userText;
	private JTextField serverText;
	private JFormattedTextField portField;
	private JButton loginButton;
	
	public LoginBoundary() {
		this.loginWindow = new JFrame("Login Battleship");
		this.createAndShowGUI();
	}

	public void addComponentsToPane(Container pane) {
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			// natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}

		JLabel userLabel = new JLabel("User");
		c.weightx = 0.5;
		c.gridwidth = 1; // 3 columns wide
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(userLabel, c);

		this.userText = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 6; // 3 columns wide
		c.gridx = 1;
		c.gridy = 0;
		pane.add(userText, c);

		c.insets = new Insets(10, 0, 0, 0); // top padding
		
		JLabel serverLabel = new JLabel("Server");
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1; // 3 columns wide
		c.gridx = 0;
		c.gridy = 1;
		pane.add(serverLabel, c);

		this.serverText = new JTextField(18);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 4; // 3 columns wide
		c.gridx = 1;
		c.gridy = 1;
		pane.add(serverText, c);

		JLabel portLabel = new JLabel("Port");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 5;
		c.gridy = 1;
		pane.add(portLabel, c);

		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1099);
		formatter.setMaximum(64999);
		formatter.setCommitsOnValidEdit(true);
		this.portField = new JFormattedTextField(formatter);
		portField.setText("1099");
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 1; 
		pane.add(portField, c);

		loginButton = new JButton("Login Game");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.gridx = 5; // aligned with button 2
		c.gridwidth = 2; // 2 columns wide
		c.gridy = 2; // third row
		pane.add(loginButton, c);
		
		loginButton.addActionListener(new LoginActionListener(this));

		// c.anchor = GridBagConstraints.PAGE_END; // bottom of space
	}

	public String getUsername() {
		return this.userText.getText();
	}

	public String getServer() {
		return this.serverText.getText();
	}

	public Integer getServerPort() {
		return Integer.parseInt(this.portField.getText());	
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private void createAndShowGUI() {
		this.loginWindow.getContentPane().setLayout(new BorderLayout());
		this.loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		addComponentsToPane(this.loginWindow.getContentPane());

		// Display the window.
		this.loginWindow.setSize(250, 150);
		this.loginWindow.setResizable(false);
		this.loginWindow.pack();
		this.loginWindow.setVisible(true);
	}

	public void setEnabled(boolean enabled) {
		this.portField.setEnabled(enabled);
		this.serverText.setEnabled(enabled);
		this.userText.setEnabled(enabled);
		this.loginButton.setEnabled(enabled);
	}

	public void setVisible(boolean b) {
		this.loginWindow.setVisible(false);
	}
}

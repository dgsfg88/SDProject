package uni.project.sd.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import uni.project.sd.Control.DummyController;
import uni.project.sd.Entity.DummyFrontEntity;

public class DummyFront {
	private JFrame mainWindow;
	private JPanel buttonPanel;
	private JPanel logPanel;
	private JButton buttonRelaseToken;
	private JButton buttonSendAction;
	private JTextArea logArea;
	
	private DummyFrontEntity myEntity;
	private DummyController myController;
	
	public DummyFront(DummyController dummyController) {
		mainWindow = new JFrame("Communication test");
		mainWindow.setSize(400, 850);
		myEntity = DummyFrontEntity.getInstance();
		myController = dummyController;
		
		buttonPanel = new JPanel();
		buttonPanel.setSize(400,300);
		logPanel = new JPanel(new BorderLayout());
		logPanel.setSize(400, 550);
		
		mainWindow.getContentPane().setLayout(new GridLayout(2,1));
		mainWindow.getContentPane().add(buttonPanel);
		mainWindow.getContentPane().add(logPanel);
		
		buttonRelaseToken = new JButton("Relase token");
		buttonRelaseToken.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myController.tokenRelased();
			}
		});
		
		buttonSendAction = new JButton("Do something");
		buttonSendAction.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myController.sendAnAction();
			}
		});
		
		buttonPanel.add(buttonSendAction);
		buttonPanel.add(buttonRelaseToken);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setSize(350, 500);
		DefaultCaret caret = (DefaultCaret)logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setSize(350, 500);
		logPanel.add(scrollPane,BorderLayout.CENTER);
		
		//mainWindow.pack();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		
		myEntity.addView(this);
	}
	
	public void setButtonEnabled(boolean enabled) {
		this.buttonRelaseToken.setEnabled(enabled);
		this.buttonSendAction.setEnabled(enabled);
	}
	
	public void addToLog(String string) {
		this.logArea.append(string + "\n");
	}
}

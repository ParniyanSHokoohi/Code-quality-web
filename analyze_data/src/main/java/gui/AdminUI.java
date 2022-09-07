package gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import admin.AdminModel;
import java.awt.Color;
import java.awt.Font;

public class AdminUI implements ActionListener{

	private JFrame frame;
	private JPanel panelMain;
	private JPanel panelBottom;
	private JButton btnGithub;
	private JButton btnSonarQube;
	private JLabel lbStart;
	private JFrame frameMain;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminUI window = new AdminUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}

	/**
	 * Create the application.
	 */
	public AdminUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 350, 225);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("SWT2 Master ADMIN");
		//frame.pack();
		
		panelMain = new JPanel();
		
		lbStart = new JLabel();
		
		panelMain.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panelMain.setBounds(0, 0, 300, 400);
		panelMain.setLayout(new GridLayout(6,1));
		
		btnGithub = new JButton("Delete Github");
		btnGithub.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGithub.setBackground(Color.RED);
		btnGithub.addActionListener(this);
		panelMain.add(btnGithub);
		
		btnSonarQube = new JButton("Delete SonarQube");
		btnSonarQube.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSonarQube.setBackground(Color.RED);
		btnSonarQube.addActionListener(this);
		panelMain.add(btnSonarQube);
		panelMain.add(lbStart);
		
		frame.getContentPane().add(panelMain);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Delete Github"))
		{

		    try {
		    	AdminModel.DeleteGithub();
		    	lbStart.setText("Github-Projects were deleted successfull...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getActionCommand().equals("Delete SonarQube"))
		{
						
	        try {
	        	AdminModel.DeleteSonarQubeByKey();
	        	lbStart.setText("SonarQube-Projects were deleted successfull...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}

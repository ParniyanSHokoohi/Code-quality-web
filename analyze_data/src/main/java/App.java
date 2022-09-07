import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import IO.IOHelper;
import repository.ReposAnalyzer;

public class App implements ActionListener{
	
	private JFrame frameMain;
	private JButton btnStart;
	private JButton btnStop;
	private JPanel panelMain;
	private JPanel panel;
	private JLabel lblSW2;
	private JProgressBar progressBar;
	private static String projectPath;
    private static String sonarPropertiesPath;
    private static String commitYmlPath; 
    private static String [] metrics;
    private ReposAnalyzer reposAnalyzer = new ReposAnalyzer();
	
	public App () {
		initialize();
	}
	
	private void initialize() {
		frameMain = new JFrame();
		frameMain.setBounds(100, 100, 350, 240);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setTitle("Softwaretechnik 2");
		
		panelMain = new JPanel();
		panelMain.setBackground(Color.LIGHT_GRAY);
		panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		// panelMain.setLayout(new GridLayout(1,1));
		
		FlowLayout flowLayout = (FlowLayout) panelMain.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setAlignment(FlowLayout.TRAILING);
		frameMain.getContentPane().add(panelMain, BorderLayout.SOUTH);
		
		btnStart = new JButton("Start");
		btnStart.setHorizontalAlignment(SwingConstants.LEFT);
		btnStart.addActionListener(this);
		panelMain.add(btnStart);
		
		progressBar = new JProgressBar();
		panelMain.add(progressBar);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(this);
		panelMain.add(btnStop);
		
		panel = new JPanel();
		frameMain.getContentPane().add(panel, BorderLayout.CENTER);
		
		lblSW2 = new JLabel("Analyzer WEB-Framework 'Vue vs Angular'");
		lblSW2.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(lblSW2);
	}

    
    public static void main(String[] args) throws Exception {
    	
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frameMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
    	
    	if(args.length != 4){
            System.out.println("Please enter 4 Command Line Arguments. These correspond to paths to 4 files. " +"\r\n"
            		+ "The first one contains the information about the Github repositories. " + "\r\n"
                    + "The second contains the file sonar-project.properties " +"\r\n"
                    + "The third contains the file commit.yml. " +"\r\n"
                    + "and the last one is the Metrics columns. " + "\r\n"+
                    "The 4 files are located in the folder /resources.");
           
            System.exit(1);
        }
    	
    	String filename = args[3];
        String content = IOHelper.GetFileContent(filename);
        metrics = content.split(",");
        projectPath = args[0];
        sonarPropertiesPath = args[1];
        commitYmlPath = args[2]; 
    }


	@Override
	public void actionPerformed(ActionEvent e) {		
		
		if(e.getActionCommand().equals("Start"))
		{
			
			progressBar.setValue(100);
			try {
				progressBar.setVisible(true); 
				// progressBar.
				reposAnalyzer.Analyze(projectPath, sonarPropertiesPath, commitYmlPath);
			    reposAnalyzer.ExportProjectMetrics(metrics, 500);
			    progressBar.setValue(100);
			 	
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	 
		}
		
		if(e.getActionCommand().equals("Stop"))
		{
			System.exit(0);
		}	
	}
}
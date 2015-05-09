import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.UIManager;


public class MainWindow extends JFrame {

	private JPanel mainContent;
	private Tabs tabs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		  try {
		      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		//Frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		//MainPanel
		mainContent = new JPanel();
		mainContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContent.setLayout(new BorderLayout(0, 0));
		setContentPane(mainContent);
		
		tabs = new Tabs();
		mainContent.add(tabs);
		
	}

}

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;


public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		setTitle("BlindMachRace-Debriefing");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		ImageIcon icon = new ImageIcon(getClass().getResource("img/ic_launcher.png"));
		//setIconImage((new ImageIcon("img/ic_launcher.png")).getImage());
		setIconImage(icon.getImage());
		//MainPanel
		mainContent = new JPanel();
		mainContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContent.setLayout(new BorderLayout(0, 0));
		setContentPane(mainContent);
		
		tabs = new Tabs();
		mainContent.add(tabs);
		
	}
}

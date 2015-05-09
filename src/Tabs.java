import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Tabs extends JTabbedPane {
	
	public Tabs(){
		super();
		this.add("tets", new JPanel().add(new JLabel("TEST")));
		this.add("tets2", new JPanel().add(new JLabel("TEST2")));
		
	}
	
	
	
}

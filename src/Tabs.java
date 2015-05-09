import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Tabs extends JTabbedPane {
	
	public Tabs(){
		super();
		this.add("Event KML", new EventKMLPanel());
		//this.add("User KML", new JPanel().add(new JLabel("TEST2")));
	}
	
	
	
}

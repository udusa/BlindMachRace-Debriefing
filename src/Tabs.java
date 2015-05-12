import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Tabs extends JTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Tabs(){
		super();
		this.add("Event KML", new UserCreateKMLPanel());
		this.add("User KML", new EventCreateKMLPanel());
	}
	
	
	
}

import javax.swing.JTabbedPane;


public class Tabs extends JTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Tabs(){
		super();
		this.add("User KML", new UserCreateKMLPanel());
		this.add("Event KML", new EventCreateKMLPanel());
	}
	
	
	
}

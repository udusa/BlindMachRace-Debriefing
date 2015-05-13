import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import serverconnect.KMLgeneratorPerEvent;


public class EventCreateKMLPanel extends UserCreateKMLPanel {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventCreateKMLPanel() {
		super();
		userTxt.setVisible(false);
		userLbl.setVisible(false);
		createKmlPathbtn.setVisible(false);
		createKmlTimebtn.setBounds(115, 180, 200, 25);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean succeed=false;
		String event = eventTxt.getText().replaceAll("\\s","");
		String user = userTxt.getText().replaceAll("\\s","");
		String hURL = historyURL+"&Event="+event+"&User=SailorNULL";
		String bURL = getBuoysURL()+"&Event="+event;
		String path = chooseFile();
		KMLgeneratorPerEvent g = new KMLgeneratorPerEvent(hURL,path,bURL);
		g.setEvent(event);
		g.setUser(user);
		
		succeed = g.createKMLTimeStamp();
		
		if(succeed){
			final String message = "KML Creation Succeed!\n";
			    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
			        JOptionPane.INFORMATION_MESSAGE);
			    //System.exit(0);    
		}else{
			final String message = "KML Creation Fail!\n"
			        + "Please Cheack Infornation Entered\n";
			    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		}
	}
}

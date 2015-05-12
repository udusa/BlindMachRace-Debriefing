import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import serverconnect.KMLgeneratorPerUser;


public class UserCreateKMLPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextField eventTxt;
	protected JLabel eventLbl;
	protected JButton createKmlTimebtn,createKmlPathbtn;
	final protected String historyURL = "http://bmr.comuv.com/androidGetKml.php?Table=history";
	final private String buoysURL="http://bmr.comuv.com/androidGetBuoys.php?Table=events&Event=";
	protected JTextField userTxt;
	protected JLabel userLbl;
	/**
	 * Create the panel.
	 */
	public UserCreateKMLPanel() {
		setLayout(null);
		
		eventTxt = new JTextField();
		eventTxt.setBounds(140, 27, 150, 30);
		eventTxt.setColumns(10);
		add(eventTxt);
		
		eventLbl = new JLabel("Event # :",SwingConstants.CENTER);
		eventLbl.setBounds(140, 11, 150, 10);
		add(eventLbl);
		
		createKmlTimebtn = new JButton("Create KML File (Time Strap)");
		createKmlTimebtn.setBounds(41, 195, 170, 25);
		createKmlTimebtn.addActionListener(this);
		add(createKmlTimebtn);
		
		createKmlPathbtn = new JButton("Create KML File (Path Only)");
		createKmlPathbtn.setBounds(221, 195, 170, 25);
		createKmlPathbtn.addActionListener(this);
		add(createKmlPathbtn);
		
		userTxt = new JTextField();
		userTxt.setColumns(10);
		userTxt.setBounds(140, 102, 150, 30);
		add(userTxt);
		
		userLbl = new JLabel("User # :", SwingConstants.CENTER);
		userLbl.setBounds(140, 86, 150, 10);
		add(userLbl);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean succeed=false;
		String event = eventTxt.getText().replaceAll("\\s","");
		String user = userTxt.getText().replaceAll("\\s","");
		String hURL = historyURL+"&Event="+event+"&User=Sailor"+user;
		String bURL = buoysURL+"&Event="+event;
		String path = chooseFile();
		KMLgeneratorPerUser g = new KMLgeneratorPerUser(hURL,path,bURL);
		g.setEvent(event);
		g.setUser(user);
		if(e.getActionCommand().equals(createKmlTimebtn.getText())){
			succeed = g.createKMLTimeStamp();
		}else{
			succeed = g.createKMLPath();
		}
		
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
	
	protected String chooseFile(){
		String path=null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("choosertitle");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			path=chooser.getSelectedFile().getPath();
		}
		return path;
	}

	public JTextField getEventTxt() {
		return eventTxt;
	}

	public JLabel getEventLbl() {
		return eventLbl;
	}

	public JButton getCreateKmlTimebtn() {
		return createKmlTimebtn;
	}

	public JButton getCreateKmlPathbtn() {
		return createKmlPathbtn;
	}

	public String getHistoryURL() {
		return historyURL;
	}

	public String getBuoysURL() {
		return buoysURL;
	}

	public JTextField getUserTxt() {
		return userTxt;
	}

	public JLabel getUserLbl() {
		return userLbl;
	}
	
	
	
}

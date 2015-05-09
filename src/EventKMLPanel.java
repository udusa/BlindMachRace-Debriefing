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

import serverconnect.GetPathKML;


public class EventKMLPanel extends JPanel implements ActionListener{
	private JTextField eventTxt;
	private JLabel eventLbl;
	private JButton createKmlTimebtn,createKmlPathbtn;
	private String historyURL = "http://bmr.comuv.com/androidGetKmlEvent.php?Table=history";
	private JTextField userTxt;
	private JLabel userLbl;
	/**
	 * Create the panel.
	 */
	public EventKMLPanel() {
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
		String URL = historyURL;
		String event = eventTxt.getText().replaceAll("\\s","");
		String user = userTxt.getText().replaceAll("\\s","");
		String path;
		if(e.getActionCommand().equals(createKmlTimebtn.getText())){
			
		}else{
			path = chooseFile();
			URL+= "&Event="+event+"&User=Sailor"+user;
			//System.out.println(URL);
			
			GetPathKML g = new GetPathKML(URL,path);
			g.setEvent(event);
			g.setUser(user);
			succeed = g.getData();
		}
		
		if(succeed){
			final String message = "\"KML Creation Succeed!\"\n";
			    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
			        JOptionPane.INFORMATION_MESSAGE);
		}else{
			final String message = "\"KML Creation Fail!\"\n"
			        + "Please Cheack Infornation Entered\n";
			    JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		}
		System.exit(0);
	}
	
	private String chooseFile(){
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
}

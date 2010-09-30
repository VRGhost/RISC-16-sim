package AboutPanel;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class AboutPanel extends JFrame {
	private static final long serialVersionUID = 4450931889926763503L;
	
	public AboutPanel(){
		super();
		Container contentPanel=getContentPane();
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		contentPanel.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0f;
		c.anchor=GridBagConstraints.PAGE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		JLabel aboutLabel=new JLabel("About:");
		gridbag.setConstraints(aboutLabel, c);
		contentPanel.add(aboutLabel);
		
		JEditorPane main_info = new JEditorPane("text/html","text");
		main_info.setEditable(false); main_info.setFocusable(false);
		main_info.setOpaque(false);
		
		main_info.setText("<div align=\"center\">" +
				"This RISC-16 Open source emulator was created by:<br>" +
				"[VR]Ghost, Tr33g (...just maybe you want to help us? :) )<br>" +
				"Please, send all bugs to vrghost at gmail dot com<br>" +
				"And don't forget to visit <a href=\"http://risc.nano.lv\">Our homepage ( risc.nano.lv )</a><br>" +
				"Thanks for hosting to Treck<br>" +
				"</div><div align=\"right\"><i>December 2005</i>" +
				"</div>");
		
		gridbag.setConstraints(main_info, c);
		 contentPanel.add(main_info);
		
		JButton closeButton=new JButton("OK, I've got it!");
		gridbag.setConstraints(closeButton, c);
		contentPanel.add(closeButton);

		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setResizable(false);
        pack();
        
        closeButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				setVisible(false);				
			}
			
		});
       // setVisible(true);    
	}
}

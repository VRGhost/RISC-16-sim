package asmV2;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import asmV1.AsmTextArea;
import asmV1.AsmV1Panel;

public class AsmV2Panel extends JPanel {
	private static final long serialVersionUID = 3916171073807250136L;
	AsmV1Panel convertToPanel;
	
	public AsmV2Panel(AsmV1Panel controlledPanel){
		super();
		convertToPanel=controlledPanel;
		
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0f;		
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		JLabel asmLabel1=new JLabel("To Tr33g: please, be nice, write your brand-new super-asm");
		gridbag.setConstraints(asmLabel1, c);
		add(asmLabel1);
		JLabel asmLabel2=new JLabel("HERE and then translate it to NATIVE asmV1 ! ");
		gridbag.setConstraints(asmLabel2, c);
		add(asmLabel2);
		
		AsmTextArea asmV2Editor=new AsmTextArea();
		asmV2Editor.setPreferredSize(new Dimension(200,210));
		JScrollPane AsmV2ScrollPane = new JScrollPane(asmV2Editor);
		gridbag.setConstraints(AsmV2ScrollPane, c);
		add(AsmV2ScrollPane);
		
		asmV2Editor.setText("Editor.");
		
		
		JButton ConvertToAsmV1But = new JButton("This button should compile to asmV1");
		gridbag.setConstraints(ConvertToAsmV1But, c);
		add(ConvertToAsmV1But);	
	}
}

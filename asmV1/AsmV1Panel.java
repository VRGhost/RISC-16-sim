package asmV1;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.BadLocationException;
import  javax.swing.text.Element;

import processor.processor;

public class AsmV1Panel extends JPanel{
	private static final long serialVersionUID = 2391430685394562337L;
	processor CPU;
	private final AsmTextArea AsmEditor= new AsmTextArea();
	private final AsmV1Compiler comp=new AsmV1Compiler();
	final JLabel asmLabel2=new JLabel("Command format: <addr>: <comm>");
	
	public AsmV1Panel(processor controlledCPU){
		super();
		CPU=controlledCPU;
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0f;		
		c.gridwidth = GridBagConstraints.REMAINDER;
		final JLabel asmLabel=new JLabel("Asm editor");
		gridbag.setConstraints(asmLabel, c);
		add(asmLabel);		
		gridbag.setConstraints(asmLabel2, c);
		add(asmLabel2);
		
		JScrollPane AsmScrollPane = new JScrollPane(AsmEditor);
		AsmScrollPane.setPreferredSize(AsmEditor.getPreferredSize());
		AsmScrollPane.setMinimumSize(AsmScrollPane.getMinimumSize());
		gridbag.setConstraints(AsmScrollPane, c);
		add(AsmScrollPane);
		
		JButton ConvertToBinaryBut = new JButton("Take deep breath!");
		gridbag.setConstraints(ConvertToBinaryBut, c);
		add(ConvertToBinaryBut);		

		ConvertToBinaryBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					compileToMemory();
				}        		
        });

	}
	
	private void compileToMemory(){
		//boolean noErrors=true;
		comp.zeroMempoint();
		Element paragraph = AsmEditor.getDocument().getDefaultRootElement();
			int contentCount = paragraph.getElementCount();
			System.out.println(contentCount);
	    for (int i=0; i<contentCount; i++) {
	        Element ee = paragraph.getElement(i);
	        int rangeStart = ee.getStartOffset();
	        int rangeEnd = ee.getEndOffset();
	        try {
	        	String line = AsmEditor.getText(rangeStart, rangeEnd-rangeStart);			        	
	        	line=line.substring(0,line.length()-1); //get rid of new line symbol
	        	System.out.println(line);
	           	int out=comp.processCommand(line);                	
                	if(out>=0 && comp.errno==0){
                		System.out.println("cool! got: lineno="+comp.lineno+" data="+out+"\n loading it to memogy...");
               			CPU.setmem(comp.lineno,out);                		
                		CPU.fireStepListeners();
                	}else{
                		if(comp.errno<0)
                		{
               				AsmEditor.highlightError(line);
                		}                		
                		if(comp.errno==comp.SPACE_COMM){
                			int t=comp.lineno+out;
                			System.out.println("Clearing "+t+" memory words starting at "+comp.lineno);
                			for(int k=comp.lineno;k<t;k++)
                			CPU.setmem(k,0);
                			//comp.errno=0;
                			CPU.fireStepListeners();   
                		}
                		if(comp.errno==comp.IGNORE_LINE){} //do nothing :)
             }
                
	        } catch (BadLocationException ex) {
	        }
	    }
	 //   CPU.fireStepListeners();
	}
	
	public String getProgramCode(){
		return AsmEditor.getText();
	}
	
	public void setProgramCode(String code){
		AsmEditor.setText(code);
	}
	
	public void setNewStyle(boolean b){
		//if b then style is new
	  	comp.setStyle(b);
	  	AsmEditor.setStyle(b);
	  	if(b)
	  		asmLabel2.setText("Command format: <comm>");
	  	else 
	  		asmLabel2.setText("Command format: <addr>: <comm>");
	}
}

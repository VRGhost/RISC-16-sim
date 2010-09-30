import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import processor.ProcessorEventListener;
import processor.processor;
import registerTable.RegisterTableEventListener;
import registerTable.RegistersChangedEvent;
import registerTable.registerTable;

public class controlPanel extends JPanel {
	private static final long serialVersionUID = -908830123678598806L;
	static processor CPU;
	
	controlPanel(processor controlledCPU){
		super();
		CPU=controlledCPU;
		genObjects();
	}
	
	private void genObjects(){
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0f;
		c.anchor=GridBagConstraints.PAGE_START;
		//IP+registers
		
			c.gridwidth = GridBagConstraints.REMAINDER;
			JLabel regLabel=new JLabel("Registers:");
			gridbag.setConstraints(regLabel, c);
			add(regLabel);
			final registerTable regArea=new registerTable();
			gridbag.setConstraints(regArea, c);
			add(regArea);		

			c.gridwidth = GridBagConstraints.RELATIVE;
			JLabel ipLabel=new JLabel("Instruction pointer:");
			gridbag.setConstraints(ipLabel, c);
			add(ipLabel);
			
			c.gridwidth = GridBagConstraints.REMAINDER;
			final IPField ipField = new IPField(16);
			gridbag.setConstraints(ipField, c);
			add(ipField);
			ipField.setText("0000000000000000");
			
			regArea.addRegisterEditedListener(new RegisterTableEventListener(){

				public void RegistersChangedEvent(RegistersChangedEvent evt) {
					CPU.setreg(evt.reg_address, registerTable.getRegAt(evt.reg_address));
				}

				public void myEventOccurred(RegistersChangedEvent evt) {
				}
				
			});
		
		
			c.gridwidth = GridBagConstraints.REMAINDER;//another row
			JButton step_button = new JButton("Step");
	    	gridbag.setConstraints(step_button, c);
	    	add(step_button);
	    	step_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					//go CPU!
					CPU.step();					
				}	    		
	    	});
        	
        {
        	c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
        	JButton runButton = new JButton("RUN");
        	gridbag.setConstraints(runButton, c);
        	add(runButton);
        	runButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					CPU.run();					
				}        		
        	});
        	
        	c.gridwidth = GridBagConstraints.REMAINDER; //end row
        	JButton stopButton = new JButton("STOP");
        	gridbag.setConstraints(stopButton, c);
        	add(stopButton);
        	stopButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					CPU.stop();					
				}        		
        	});
        }
        {
        	c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
        	JButton clr_regs = new JButton("Clear registers");
        	gridbag.setConstraints(clr_regs, c);
        	add(clr_regs);
        	clr_regs.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					CPU.dumpRegisters();					
				}
        		
        	});

        	c.gridwidth = GridBagConstraints.REMAINDER; //end row
        	JButton clr_mem = new JButton("Clear memory");
        	gridbag.setConstraints(clr_mem, c);
        	add(clr_mem);
        	clr_mem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					CPU.dumpMemory();					
				}        		
        	});
        }
        
        //gui creation finished
        CPU.addStepListener(new ProcessorEventListener(){

			public void myEventOccurred() {}

			public void StepEventOccured() {
				String bin_ip=Integer.toBinaryString(CPU.getip());
				while(bin_ip.length()!=16)
					bin_ip="0"+bin_ip;
				
				ipField.setText(bin_ip);
				
				for(int i=1;i<8;i++)
					registerTable.setRegAt(i,CPU.getreg(i));
				repaint();
			}

			public void IPChangeOccured() {}//no need to react this(evrything is performed in step() listener

			public void MemoryDumped() {}

			public void HaltOccured() {
				JOptionPane.showMessageDialog(regArea,
					    "-==|| HALT ||==-",
					    "WARNING",
					    JOptionPane.WARNING_MESSAGE);
			}
        });
        
        ipField.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				CPU.setip(Integer.parseInt(ipField.getText(),2));
			}
        	
        });
     
	}
	
    protected void makebutton(String name,GridBagLayout gridbag,GridBagConstraints c) {
    	JButton button = new JButton(name);
    	gridbag.setConstraints(button, c);
    	add(button);
    }
}

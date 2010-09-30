package MemoryTable;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import processor.ProcessorEventListener;
import processor.processor;




public class memPanel extends JPanel{
	private static final long serialVersionUID = 8184519164378772371L;
	processor CPU;
	
	public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 100;
        return size;
    }
	
	public memPanel(processor controlledCPU){
		super();
		CPU=controlledCPU;
		addObjects();
	}
	
	public memPanel(LayoutManager layout){
		super();
		addObjects();
	}
	
	private void addObjects(){
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0f;
		c.anchor=GridBagConstraints.PAGE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty=1.0f;
		
		final MemoryTable editorTable=new MemoryTable();
		JButton upButton=new JButton("Up");
		JButton downButton=new JButton("Down");
		
		gridbag.setConstraints(editorTable, c);
		gridbag.setConstraints(upButton, c);
		gridbag.setConstraints(downButton, c);
		
        add(upButton);
        add(editorTable);
        add(downButton);
        
        //gui creation finished
        
        editorTable.sethighlightedRow1atAddr(0);
        editorTable.sethighlightedRow2atAddr(0);

        editorTable.addViewPortEventChangerListener(new MemoryTableEventListener() {
            public void myEventOccurred(ViewPortChangedEvent evt) {}

			public void ViewPortChangeOccurred(ViewPortChangedEvent evt) {
				//int start=evt.oldAddr;
				int new_start=evt.newAddr;
				System.out.println(evt.oldAddr);
				
				int size = MemoryTable.cols;
				
				//for(int i=0;i<size;i++)
				//	CPU.setmem(start+i,Integer.parseInt((String) editorTable.getValueAt(i,1),2));

				
				for(int i=0;i<size;i++)
					editorTable.setValueAt(Integer.toBinaryString(CPU.getmem(i+new_start)),i,1);

			}

			public void PleaseSaveAddr(ViewPortChangedEvent evt) {
				CPU.setmem(evt.newAddr,evt.value);
			}

        });
        
        upButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				editorTable.moveViewPortUp(3);				
			}
        	
        });
        
        downButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
				editorTable.moveViewPortDown(3);				
			}
        });
        
        CPU.addStepListener(new ProcessorEventListener(){

			public void myEventOccurred() {}

			public void StepEventOccured() {
				editorTable.sethighlightedRow1atAddr(CPU.getip());
				int size=MemoryTable.cols;
				
				int new_start=editorTable.startAddr;
				//System.out.println("Must update! startt addr="+new_start);
				int data;
				
				for(int i=0;i<size;i++){
					data=CPU.getmem(i+new_start);
					editorTable.setValueAt(Integer.toBinaryString(data),i,1);
				}
			}

			public void IPChangeOccured() {
				editorTable.sethighlightedRow1atAddr(CPU.getip());
			}

			public void MemoryDumped() {
				editorTable.clearAllMemory();
				System.out.println("Memory cleared");
			}

			public void HaltOccured() {
				// TODO Auto-generated method stub
				
			}

        });
	}
}




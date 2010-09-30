import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import asmV1.AsmV1Panel;

import AboutPanel.AboutPanel;

import processor.*;

public class RiscMenu extends JMenuBar {
	processor CPU;
	JFileChooser fc = new JFileChooser();
	AboutPanel AboutPanel=new AboutPanel();
	AsmV1Panel asmPanel;
	
	public RiscMenu(processor mainCPU,AsmV1Panel asmP) {
		final processor CPU=mainCPU;
		asmPanel=asmP;
		JMenu menu = new JMenu("File");
		add(menu);
		
		JMenuItem memOpers=new JMenu("Memory operations");
		JMenuItem asmOpers=new JMenu("Asm operations");
		JMenuItem saveMem = new JMenuItem("Dump memory (may take long time)");
		menu.add(memOpers);
		menu.add(asmOpers);
		
		saveMem.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(RiscMenu.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                System.out.println("Saving: \"" + file.getName() + "\"");
	                
	                if(file.canWrite()||!file.exists()){
	                try {
	                	FileWriter out;
							out = new FileWriter(file);
						                	
	                	String text="Registers:\n";
	                	for(int i=0;i<8;i++)
	                		text=text+"R"+i+" = "+asCpuWord(CPU.getreg(i))+"\n";
	                	text=text+"=============================\nIP = "+asCpuWord(CPU.getip())+"\n=============================\nMemory:\n";
	                	out.write(text);
	                	
	                	for(int i=0;i<65536;i++)
	                		out.write(asCpuWord(CPU.getmem(i))+"\n");
	     
	                	
	                	//out.write(text);
	                    out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	                }else System.out.println("Cannot write this file!");
	            } else {
	            	System.out.println("Save command cancelled by user.");
	            }
				
			}
			
		});
		
		JMenuItem saveState = new JMenuItem("Save processor's state to file");
		memOpers.add(saveState);
		
		saveState.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(RiscMenu.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                
	                if(file.canWrite()||!file.exists()){
	                try {
	                System.out.println("Saving: \"" + file.getCanonicalPath() + "\"");
	  					DataOutputStream out = new DataOutputStream(
                           new FileOutputStream(file.getAbsolutePath()));
	  					out.writeInt(CPU.getip());
	  					for (int i=1; i< 8; i++)
	  					{
	  						out.writeInt(CPU.getreg(i));
	  					};
	  					for (int i=0;i<65536;i++)
	  					{
	  						out.writeInt(CPU.getmem(i));
	  					};
	                    out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	                }else System.out.println("Cannot write this file!");
	            } else {
	            	System.out.println("Save command cancelled by user.");
	            }
				
			}
			
		});
					
		JMenuItem loadState = new JMenuItem("Load processor's state from file");
		memOpers.add(loadState);
		memOpers.add(new JSeparator());
		memOpers.add(saveMem);
		
		loadState.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(RiscMenu.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.

	                
	                if((file.length()==262176)&file.canRead()||!file.exists()){
	                try {
	                System.out.println("Loading: \"" + file.getCanonicalPath() + "\"");
	  					DataInputStream inp = new DataInputStream(
                           new FileInputStream(file.getAbsolutePath()));
	  					int temp = inp.readInt(); 
	  					for (int i=1; i< 8; i++)
	  					{
	  						CPU.setreg(i,inp.readInt());
	  					};
	  					for (int i=0;i<65536;i++)
	  					{
	  						CPU.setmem(i,inp.readInt());
	  					};
	  					CPU.setip(temp);
						CPU.fireStepListeners();
	                    inp.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	                }else System.out.println("Cannot read this file or unknown format!");
	            } else {
	            	System.out.println("Load command cancelled by user.");
	            }
				
			}
			
		});
		
		JMenuItem LoadAsmv1Prog = new JMenuItem("Load asm program");
		asmOpers.add(LoadAsmv1Prog);
		LoadAsmv1Prog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(RiscMenu.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                System.out.println("Loading: \"" + file.getName() + "\"");
	                
	                if(file.canRead()){
	                try {
	                	FileReader inp = new FileReader(file);
						BufferedReader br = new BufferedReader(inp);
						String text="";
						String data="";
						while ( (data=br.readLine()) != null ) 
							text+=data+"\n";
						
						//System.out.println("Text: "+text);
						asmPanel.setProgramCode(text.substring(0,text.length()-1));
	                	//out.write(text);
						inp.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	                }else System.out.println("Cannot read this file!");
	            } else {
	            	System.out.println("Load command cancelled by user.");
	            }
				
			}			
		});
		
		JMenuItem SaveAsmv1Prog = new JMenuItem("Save asm program");
		asmOpers.add(SaveAsmv1Prog);
		SaveAsmv1Prog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(RiscMenu.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                System.out.println("Saving: \"" + file.getName() + "\"");
	                
	                if(file.canWrite()||!file.exists()){
	                try {
	                	FileWriter out;
							out = new FileWriter(file);
	                		out.write(asmPanel.getProgramCode());    
	                	//out.write(text);
	                    out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	                }else System.out.println("Cannot write this file!");
	            } else {
	            	System.out.println("Save command cancelled by user.");
	            }
			}			
		});
		
		JMenuItem prefs=new JMenu("Prefs");
		add(prefs);
		JMenu helpMenu = new JMenu("Help");
		add(helpMenu);
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);
		
		about.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AboutPanel.setVisible(true);
			}
			
		});
		JMenuItem CPUspeed=new JMenu("Set Run speed");
		prefs.add(CPUspeed);
		ButtonGroup SpeedGroup = new ButtonGroup();
		JRadioButtonMenuItem rb100ms = new JRadioButtonMenuItem("100 ms");
		JRadioButtonMenuItem rb500ms = new JRadioButtonMenuItem("500 ms");
		JRadioButtonMenuItem rb1000ms = new JRadioButtonMenuItem("1000 ms");
		JRadioButtonMenuItem rb2000ms = new JRadioButtonMenuItem("2000 ms");
		JRadioButtonMenuItem rb5000ms = new JRadioButtonMenuItem("5000 ms");
		SpeedGroup.add(rb100ms); SpeedGroup.add(rb500ms); SpeedGroup.add(rb1000ms); SpeedGroup.add(rb2000ms); SpeedGroup.add(rb5000ms);
		rb1000ms.setSelected(true);
		CPUspeed.add(rb100ms); CPUspeed.add(rb500ms); CPUspeed.add(rb1000ms); CPUspeed.add(rb2000ms); CPUspeed.add(rb5000ms);
		
		rb100ms.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CPU.setLatency(100);				
			}			
		});
		rb500ms.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CPU.setLatency(500);				
			}			
		});
		rb1000ms.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CPU.setLatency(1000);				
			}			
		});
		rb2000ms.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CPU.setLatency(2000);				
			}			
		});
		rb5000ms.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CPU.setLatency(5000);				
			}			
		});

		JMenuItem asmStyle=new JMenu("Set asm style");
		ButtonGroup asmGroup = new ButtonGroup();
		prefs.add(asmStyle);
		JRadioButtonMenuItem oldStyle=new JRadioButtonMenuItem("Old style");
		JRadioButtonMenuItem newStyle=new JRadioButtonMenuItem("New style");
		asmGroup.add(oldStyle); asmGroup.add(newStyle);
		asmStyle.add(oldStyle); asmStyle.add(newStyle);
		oldStyle.setSelected(true);
		prefs.add(asmStyle);
		
		oldStyle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				asmPanel.setNewStyle(false);				
			}			
		});
		newStyle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				asmPanel.setNewStyle(true);				
			}			
		});
	}
	
	private String asCpuWord(int a){
		String out=Integer.toBinaryString(a);
		
		while(out.length()!=16)
			out="0"+out;
		
		return out;
	}

	private static final long serialVersionUID = 4383336541148264754L;

}

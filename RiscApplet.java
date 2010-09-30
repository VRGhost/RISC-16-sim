//import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import asmV1.AsmV1Panel;

import processor.processor;
import MemoryTable.*;

public class RiscApplet /*extends Applet*/ {
	private static final long serialVersionUID = 5189475231694495716L;
	final static String MEMPANEL = "Memory";
	final static String ASMPANEL = "Assambler";	
	static processor mainCPU;
	static JFrame mainFrame;
	
    public static void addComponentToPane(Container pane) {
    	
        pane.setLayout(new BorderLayout());
        pane.add(new memPanel(mainCPU), BorderLayout.CENTER);
        
        controlPanel cPanel = new controlPanel(mainCPU);
        //creating assambler panes
        JTabbedPane AsmTabbedPane = new JTabbedPane();
        //AsmTabbedPane.setPreferredSize(new Dimension(400,200));
        AsmV1Panel asm1Panel = new AsmV1Panel(mainCPU);
       // AsmV2Panel asm2Panel = new AsmV2Panel(asm1Panel);
        AsmTabbedPane.addTab( "AsmV1", asm1Panel );
        //AsmTabbedPane.addTab( "AsmV2", asm2Panel );
        
        pane.add(AsmTabbedPane, BorderLayout.EAST);
        //alinging control panel to top :)
        JPanel cTopPanel = new JPanel();
        cTopPanel.setLayout(new FlowLayout());
        cTopPanel.add(cPanel);
        pane.add(cTopPanel, BorderLayout.WEST);
        
        RiscMenu MyMenu=new RiscMenu(mainCPU,asm1Panel);
        pane.add(MyMenu, BorderLayout.NORTH);
        
    }
	
    private  static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("RISC-16");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RiscApplet.addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);    
        
        mainFrame=frame;
        
    }
    
    public static void init() {
    	mainCPU=new processor();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        }); 
    }
    
    /*public void paint(Graphics g) {
    	//g.drawString("Hello world!", 50, 25);
    }*/
    
    public static void main(String[] args){
    	init();
    }
}

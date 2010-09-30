package processor;

import javax.swing.JComponent;

public class processor extends JComponent {
	private static final long serialVersionUID = -3697940400548286146L;
	
	private int ip;
	private static int[] registers =new int[8];
	private static int[] memory=new int[65536]; //i'm tired of theese shorts!
	private ProcessorThread mineThread =new ProcessorThread(this);
	
	public processor(){
		super();
		ip=0;
		mineThread.start();
	}
	
	public int[] getAllMem(){
		return memory;
	}
	
	public int getmem(int address) {
		int out=memory[(address+65536)%65536];
		//System.out.println("Memory get @"+address+" value="+out);
		return(out);
	}
	
	public int getreg(int reg) {
		if ((reg<=7)&&(reg>=0))
			return registers[reg];
		else
			return 0;
	}
	
	public int getip() {
		return ip;
	}
	
	public void setmem(int address, int value) {
		memory[((address+65536)%65536)&0xFFFF]=(value&0xFFFF);	
		//System.out.println("Memory set @"+address+" value="+value);
	}
	
	
	public void setreg(int reg, int value) {
		//System.out.println("Reg No"+reg+" was set to "+value);
		if ((reg>0)&&(reg<=7))
			registers[reg]=(value&0xFFFF);
	}
	
	public void setip(int value) {
		ip=(char)((value)&0xFFFF);		
		//System.out.println("IP= "+ip+" "+Integer.toBinaryString(ip));
		fireIPChangeListeners();
	}
	
	private void add(int ra, int rb, int rc) {
		setreg(ra, getreg(rb) + getreg (rc));
	}
	
	private void addi(int ra, int rb, int imm) {
		int mem=imm&0x007F;
		if((mem&0x0040)!=0)
			mem=(mem|0xFFC0)&0xFFFF;
		
		setreg(ra, getreg(rb)+mem);
	}
	
	private void nand(int ra, int rb, int rc) {
		setreg(ra,~(getreg(rb)&getreg(rc)));
	}
	
	private void lui(int ra, int imm) {
		setreg(ra,imm<<6);
	}
	
	private void beq(int ra, int rb, int imm) {
		int mem=imm&0x007F;
		if((mem&0x0040)!=0)
			mem=(mem|0xFFC0)&0xFFFF;
		
		System.out.println("Beq at rel address 1+"+mem+" | "+Integer.toBinaryString(mem));
		
		if(getreg(ra)==getreg(rb))
			setip(getip()+mem);//+1 is made in step();
	}
	
	private void lw(int ra, int rb, int imm) {
		int mem=imm&0x007F;
		if((mem&0x0040)!=0)
			mem=(mem|0xFFC0)&0xFFFF;
		
		setreg(ra, getmem((getreg(rb)+mem)&0xFFFF));
	}
	
	private void sw(int ra, int rb, int imm) {
		int mem=imm&0x007F;
		if((mem&0x0040)!=0)
			mem=(mem|0xFFC0)&0xFFFF;
		//sw works!
		setmem(((getreg(rb)+mem)&0xFFFF), getreg(ra));
	}
	
	private void jalr(int ra, int rb) {
		int cur_ip=getip();
		setip(rb);
		setreg(ra,cur_ip+1);
	}
	
	public void step(){
		int data=getmem(getip());
		int command=data;
		command=command&0xE000;
		command>>=13;
		
		System.out.println("Step called! data="+data+" command="+command);
		
		int regA=data&0x1C00;
		regA>>=10;
		int regB=data&0x0380;
		regB>>=7;
		int regC=data&0x0007;
		
		int oper1=data&0x007F;
		if((data&0x0040)!=0)
			oper1=oper1|0xFFC0;
		
		switch(command){
		case 0:
			if( (data&0x0078) ==0)
				add(regA,regB,regC);
			else
				halt();			
			break;
		case 1:
			addi(regA,regB,oper1);
			System.out.println("addi");
			break;
		case 2:
			if( (data&0x0078) ==0)
				nand(regA,regB,regC);
			else
				halt();
			System.out.println("nand");
			break;
		case 3:
			int imm=data&0x003FF;
			lui(regA,imm);
			System.out.println("lui");
			break;
		case 4:
			lw(regA,regB,oper1);
			System.out.println("lw");
			break;
		case 5:
			sw(regA,regB,oper1);
			System.out.println("sw");
			break;
		case 6:
			beq(regA,regB,oper1);
			System.out.println("beq");
			break;
		case 7:
			if((data&0x007F)==0)
				jalr(regA,regB);
			else
				halt();
			System.out.println("jarl");
			break;			
		}
		setip(getip()+1);
		
		fireStepListeners();
	}
	
	private void halt(){
		System.out.println("HALT");
		stop();
		setip(getip()-1);
		fireHaltListeners();
	}
	
	
	public void dumpRegisters(){
		for(int i=1;i<8;i++)
			registers[i]=0; // using bad style to avoid action flood 
		
		setip(0);
		
		//dummy step to force gui redraw
		fireStepListeners();
	}
	
	public void dumpMemory(){
		for(int i=1;i<65536;i++)
			memory[i]=0; // using bad style to avoid action flood 
		
		//dummy step to force gui redraw
		fireMemoryDumpedListeners();
	}
	
	public void run(){
		mineThread.go();
	}
	
	public void stop(){
		mineThread.Pause();
	}
	
	///////////////////////
    // This methods allows classes to register for MyEvents
	 public void addStepListener(ProcessorEventListener listener) {
        listenerList.add(ProcessorEventListener.class, listener);
    }
	 
    // This methods allows classes to unregister for MyEvents
	 public void removeStepListener(ProcessorEventListener listener) {
        listenerList.remove(ProcessorEventListener.class, listener);
    }
	 
    public void fireStepListeners() {
        Object[] listeners = listenerList.getListenerList();
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ProcessorEventListener.class) {
                ((ProcessorEventListener)listeners[i+1]).StepEventOccured();
            }
        }
    }
    
    private void fireIPChangeListeners() {
        Object[] listeners = listenerList.getListenerList();
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ProcessorEventListener.class) {
                ((ProcessorEventListener)listeners[i+1]).IPChangeOccured();
            }
        }
    }
    
    private void fireMemoryDumpedListeners() {
        Object[] listeners = listenerList.getListenerList();
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ProcessorEventListener.class) {
                ((ProcessorEventListener)listeners[i+1]).MemoryDumped();
            }
        }
    }
    
    public void fireHaltListeners() {
        Object[] listeners = listenerList.getListenerList();
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ProcessorEventListener.class) {
                ((ProcessorEventListener)listeners[i+1]).HaltOccured();
            }
        }
    }
    
    public void setLatency(long time){
    	mineThread.setLatency(time);
    }
	
}

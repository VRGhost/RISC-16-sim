package MemoryTable;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MemoryTable extends JTable {
	private static final long serialVersionUID = 8188857543049066979L;
	static int rows=2;
	static int cols=20;
	Object[][] data=new Object[cols][rows];
	int startAddr;
	private int highlightedRow1=-1;
	private int highlightedRow2=-1;
	
	protected javax.swing.event.EventListenerList listenerList =
        new javax.swing.event.EventListenerList();
	
	//custom event!!!
	public MemoryTable(){
		super(cols,rows);
		setCellSelectionEnabled(false);	
		setPreferredSize(new Dimension(200,200));	
		for(int i=0;i<rows;i++)
			for(int t=0;t<cols;t++)
				data[t][i]="0000000000000000";
		setStartAddress(0);
	}
	
	 public boolean isCellEditable(int row, int col){
		 return (col!=0)||(row==0);//address col is not editable 
	 }
	 
	 public void  sethighlightedRow1(int row){
		 highlightedRow1=row;
		 repaint();
	 }
	 
	 public void sethighlightedRow2(int row){
		 highlightedRow2=row;
		 repaint();
	 }
	 
	 public void  sethighlightedRow1atAddr(int addr){
		 highlightedRow1=(addr-startAddr+65536)%65536;
		 repaint();
	 }
	 
	 public void sethighlightedRow2atAddr(int addr){
		 highlightedRow2=(addr-startAddr+65536)%65536;
		 repaint();
	 }
	 
	 public Object getValueAt(int row, int col){
		 return data[row][col];
	 }
	 
	 public int getColCount(){
		 return cols;
	 }
 
 
	 public void setAllMemory(int[] memory){
		 if(memory.length==cols)
			 for(int i=0;i<cols;i++)
				 setValueAt(Integer.toBinaryString(memory[i]),i,1);
		 
		 repaint();
	 }
	 
	 public void clearAllMemory(){
		 editCellAt(1,0);//go to un-editable cell
		 
		 for(int i=0;i<cols;i++)
			 setValueAt("000000000000000",i,1);
		 
		 repaint();
	 }
	 
	 public void setValueAt(Object value, int row, int col) {
		 String val=value.toString();
		 val=val.replaceAll("[^01]","");
		 if(val.length()>16)
			 val=val.substring(0,16);
		 if(val.length()<16)
			 while(val.length()!=16)
				 val="0"+val;
		 data[row][col]=val;
		 
		 if(col==0 && row==0){
			 int oldAddress=startAddr;
			 setStartAddress(Integer.parseInt(val,2));
			 ViewPortChangedEvent event=new ViewPortChangedEvent(this,oldAddress,startAddr);
			 fireViewPortChanged(event);
		 }else{
			 int int_val=Integer.parseInt(val,2);
			 int addr=(startAddr+row)%65536;
			 ViewPortChangedEvent event=new ViewPortChangedEvent(this,addr,addr,int_val);
			 firePleaseSaveAddr(event);
		 }
			 
	 }
	 
	 public int getMemoryAt(int addr){
		 //System.out.println("Relative addr="+(addr-startAddr));
		 if((addr-startAddr)>=0 && (addr-startAddr)<cols)
			 return Integer.parseInt((String)data[(addr-startAddr)][1],2);
		 else return 0;
	 }
	 
	 public void moveViewPortUp(int offset){
		 editCellAt(1,0);//go to un-editable cell
		int toAddr=startAddr-offset;			 
		if(toAddr<0) toAddr+=65536;
			 int oldAddress=startAddr;
			 setStartAddress(toAddr);
			 ViewPortChangedEvent event=new ViewPortChangedEvent(this,oldAddress,startAddr);
			 fireViewPortChanged(event);
	 }
	 
	 public void moveViewPortDown(int offset){
		 editCellAt(1,0);//go to un-editable cell
		 	int toAddr=(startAddr+offset)%65536;

			 final int oldAddress=startAddr;
			 setStartAddress(toAddr);
			 ViewPortChangedEvent event=new ViewPortChangedEvent(this,oldAddress,startAddr);
			 fireViewPortChanged(event);
	 }
	 
	 public void setStartAddress(int start){
		 String value;
		 String zero="0";
		 for(int i=0;i<cols;i++){
			 value=Integer.toBinaryString((i+start)%65536);
		 	if(value.length()>16)
			 value=value.substring(1,16);
		 	if(value.length()<16)
			 	while(value.length()!=16)
				 value=zero.concat(value);
		 	data[i][0]=value;
		 }
		 highlightedRow2=(highlightedRow2+(startAddr-start)+65536)%65536;
		 highlightedRow1=(highlightedRow1+(startAddr-start)+65536)%65536;
		 startAddr=start;
		 repaint();
	 }
	 
	 public int getStartAddr(){
		 return startAddr;
	 }
	 
     // This methods allows classes to register for MyEvents
	 public void addViewPortEventChangerListener(MemoryTableEventListener listener) {
         listenerList.add(MemoryTableEventListener.class, listener);
     }
	 
     // This methods allows classes to unregister for MyEvents
	 public void removeViewPortEventChangerListener(MemoryTableEventListener listener) {
         listenerList.remove(MemoryTableEventListener.class, listener);
     }
	 
     void fireViewPortChanged(ViewPortChangedEvent evt) {
         Object[] listeners = listenerList.getListenerList();
         for (int i=0; i<listeners.length; i+=2) {
             if (listeners[i]==MemoryTableEventListener.class) {
                 ((MemoryTableEventListener)listeners[i+1]).ViewPortChangeOccurred(evt);
             }
         }
     }
     
     void firePleaseSaveAddr(ViewPortChangedEvent evt) {
         Object[] listeners = listenerList.getListenerList();
         for (int i=0; i<listeners.length; i+=2) {
             if (listeners[i]==MemoryTableEventListener.class) {
                 ((MemoryTableEventListener)listeners[i+1]).PleaseSaveAddr(evt);
             }
         }
     }
     
 	public TableCellRenderer getCellRenderer(int row, int column)
	{
		DefaultTableCellRenderer tcr =
 		 (DefaultTableCellRenderer)super.getCellRenderer(row, column);
		
		//int real_row=(row+65536)%65536;
		//System.out.println("row="+real_row+" highlightedRow1="+highlightedRow1);
		if (row%65536 == highlightedRow1 && row%65536==highlightedRow2)//then both
			tcr.setBackground(Color.gray);
		else
			if(row%65536==highlightedRow1)
				tcr.setBackground(Color.cyan);
			else
				if(row%65536==highlightedRow2)
					tcr.setBackground(Color.lightGray);
				else
					tcr.setBackground(Color.white);
		return tcr;
	}

}


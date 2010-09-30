package registerTable;
import javax.swing.JTable;

public class registerTable extends JTable {
	private static final long serialVersionUID = 3516861280442268552L;
	
	static int rows=2;
	static int cols=8;
	static Object[][] data=new Object[cols][rows];
	
	public registerTable(){
		super(cols,rows);
		setCellSelectionEnabled(false);
		for(int i=0;i<cols;i++){
				data[i][1]="0000000000000000";
				data[i][0]="R".concat(Integer.toString(i));
		}
	}
	
	 public boolean isCellEditable(int row, int col){
		 return (col!=0)&&(row!=0);//register names are not editable + R1 is not editable
	 }
	 
	 public Object getValueAt(int row, int col){
		 return data[row][col];
	 }
	 
	 public static int getRegAt(int reg_no){
		 if(reg_no>=0 && reg_no<8)
			 return Integer.parseInt((String)data[reg_no][1],2);
		 else
			 return 0;
	 }
	 
	 public static void setRegAt(int addr,int value){
		 if(addr>0 && addr<8){
			 String val=Integer.toBinaryString(value&0xFFFF);
			 
			 if(val.length()<16)
				 while(val.length()!=16)
					 val="0".concat(val);
			 
			 data[addr][1]=val;
		 }
	 }
	 
	 public void setValueAt(Object value, int row, int col) {
		 String val=value.toString();
		 val=val.replaceAll("[^01]","");
		 if(val.length()>16)
			 val=val.substring(0,16);
		 if(val.length()<16)
			 while(val.length()!=16)
				 val="0".concat(val);
		 data[row][col]=val;
		 
		 RegistersChangedEvent event=new RegistersChangedEvent(this,row);
		 fireRegisterEditedChanged(event);
	 }
	 
     // This methods allows classes to register for MyEvents
	 public void addRegisterEditedListener(RegisterTableEventListener listener) {
         listenerList.add(RegisterTableEventListener.class, listener);
     }
	 
     // This methods allows classes to unregister for MyEvents
	 public void removeRegisterEditedrListener(RegisterTableEventListener listener) {
         listenerList.remove(RegisterTableEventListener.class, listener);
     }
	 
     void fireRegisterEditedChanged(RegistersChangedEvent evt) {
         Object[] listeners = listenerList.getListenerList();
         for (int i=0; i<listeners.length; i+=2) {
             if (listeners[i]==RegisterTableEventListener.class) {
                 ((RegisterTableEventListener)listeners[i+1]).RegistersChangedEvent(evt);
             }
         }
     }
	 
}

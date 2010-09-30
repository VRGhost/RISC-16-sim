package MemoryTable;
import java.util.EventObject;


public class ViewPortChangedEvent extends EventObject {
	private static final long serialVersionUID = -6030893765085526355L;
	public MemoryTable src;
	public int oldAddr;
	public int newAddr;
	public int value=0;
	
	public ViewPortChangedEvent(Object source,int oldAddress,int NewAddress) {
        super(source);
        src=(MemoryTable) source;
        oldAddr=oldAddress;
        newAddr=NewAddress;
    }
	
	public ViewPortChangedEvent(Object source,int oldAddress,int NewAddress,int val) {
        super(source);
        src=(MemoryTable) source;
        oldAddr=oldAddress;
        newAddr=NewAddress;
        value=val;
    }
}

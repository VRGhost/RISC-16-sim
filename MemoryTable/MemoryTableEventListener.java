package MemoryTable;

import java.util.EventListener;

public interface MemoryTableEventListener extends EventListener {
    public void ViewPortChangeOccurred(ViewPortChangedEvent evt);
    public void myEventOccurred(ViewPortChangedEvent evt);
    public void PleaseSaveAddr(ViewPortChangedEvent evt);
}
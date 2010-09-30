package registerTable;
import java.util.EventListener;


public interface RegisterTableEventListener extends EventListener {
    public void RegistersChangedEvent(RegistersChangedEvent evt);
    public void myEventOccurred(RegistersChangedEvent evt);
}
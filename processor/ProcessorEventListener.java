package processor;
import java.util.EventListener;


public interface ProcessorEventListener extends EventListener {
    public void myEventOccurred();
	public void StepEventOccured();
	public void IPChangeOccured();
	public void MemoryDumped();
	public void HaltOccured();
}

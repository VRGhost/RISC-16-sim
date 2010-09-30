package processor;

public class ProcessorThread extends Thread {
	
	processor CPU;
	private boolean isSusp=true;
	long sleep_time=1000;
	
	ProcessorThread(processor controlledCPU){
		super();
		CPU=controlledCPU;
		
		setDaemon(true);
		isSusp=true;
	}
	
	public void run(){
		while(true){
			
			while (isSusp){
				try {
					sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			CPU.step();
			try {
				sleep(sleep_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("Thread");
		}
	}
	
	public void Pause(){
		isSusp=true;
	}
	
	public void go(){
		isSusp=false;
	}
	
	public void setLatency(long i){
		sleep_time=i;
	}

}

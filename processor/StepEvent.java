package processor;

import java.util.EventObject;

public class StepEvent extends EventObject {
	private static final long serialVersionUID = -4405199449554644810L;

	public StepEvent(Object source) {
		super(source);
	}

}
package registerTable;
import java.util.EventObject;

public class RegistersChangedEvent extends EventObject {
	public int reg_address=11;
	public RegistersChangedEvent(Object source, int addr) {
		super(source);
		reg_address=addr;
	}

	private static final long serialVersionUID = -5030893123085526355L;

}

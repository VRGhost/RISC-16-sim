import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class IPField extends JTextField {
	private static final long serialVersionUID = -1305461613364897639L;
	
	String value;
	
	IPField(int i){
		super(i);
	}
	
    public void processKeyEvent(KeyEvent ev) {
 	
    	char c = ev.getKeyChar();
    	int pos = getCaretPosition();
    	
    	if(c=='0'||c=='1')
    		super.processKeyEvent(ev);
    	else
    		ev.consume();
    	
    	if(getText().length()>16) setText(getText().substring(0,16));
    	setCaretPosition(pos);
    	fireActionPerformed();
    }
}

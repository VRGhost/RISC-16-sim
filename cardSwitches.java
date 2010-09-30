import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class cardSwitches extends JPanel {
	private static final long serialVersionUID = -4541788605603565179L;
	
	public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += 100;
        return size;
    }
	
	public cardSwitches(){
		super();
		addObjects();
	}
	
	private void addObjects(){
		add(new JTextField("TextField", 20));
	}
}

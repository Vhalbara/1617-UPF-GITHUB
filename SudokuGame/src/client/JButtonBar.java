package client;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JButtonBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton jbValue[];
	
//	private JButtonBar(){
//		
//	}

	public JButtonBar(JButton[] list) {
		this.jbValue = list;
		this.initComponents();
	}
	
	private void initComponents(){
		for(JButton button : this.jbValue){
			button.setFont(new Font("Serif", Font.PLAIN, 20));
			this.add(button);
		}
	}
}

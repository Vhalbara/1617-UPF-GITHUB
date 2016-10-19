package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ButtonUI;

public class JBoardGame extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final int WIDTH;
	private final int HEIGHT;
	
	private final int N, GRIDLEN;
	
	private ButtonGroup group;
	private JToggleButton[] jtbCase;
	
	private Vector<Vector<JToggleButton>> square, column, line;
	
//	private JBoardGame(){
//		super();
//		// initialisation des paramètres.
//		this.N = 3;
//		this.GRIDLEN = this.N*this.N;
//		this.HEIGHT = this.WIDTH = this.GRIDLEN *50;
//		// intialisation de la fenêtre.
//		this.setSize();
//		this.initComponents();
//	}
		
	public JBoardGame(int n, JToggleButton[] list){
		super();
		// initialisation des paramètres.
		this.N = n;
		this.GRIDLEN = this.N*this.N;
		this.HEIGHT = this.WIDTH = this.GRIDLEN *50;
		this.jtbCase = list;
		// intialisation de la fenêtre.
		this.setSize();
		this.initComponents();
	}
	
	public int getSelectedIndex(){
		int i = 0;
		for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            if (button.isSelected()) {
            	return i;
            }
         
            i++;
        }
		return -1;
	}
	
	private void setSize(){
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
	}
	
	private void initComponents(){
		this.setBackground(new Color(209, 209, 224));
		this.setLayout(new GridLayout(this.N,this.N));
				
		group = new ButtonGroup();
		
		JPanel[] JpSquare = new JPanel[this.GRIDLEN];
		int [] maskSquare = new int[this.GRIDLEN*this.GRIDLEN];
				
		for(int i=0; i<this.GRIDLEN; i++){
			JpSquare[i] = new JPanel(new GridLayout(this.N,this.N));
			JpSquare[i].setBorder(new LineBorder(Color.DARK_GRAY));
		}
		
		int square=0;
		for(int i=0; i<this.GRIDLEN; i+=this.N){
			for(int j=0; j<this.GRIDLEN; j+=this.N){
				for(int k=0; k<this.N; k++){
					for(int l=0; l<this.N; l++){
						maskSquare[(i+k)*this.GRIDLEN+(j+l)] = square;
					}	
				} // fin du carre
				square++;
			}	
		}	
						
		for(int i=0; i<this.GRIDLEN*this.GRIDLEN; i++){
			this.jtbCase[i].setBorder(new LineBorder(Color.BLACK,1));
			this.jtbCase[i].setFont(new Font("Serif", Font.PLAIN, 24));
			this.jtbCase[i].setForeground(Color.BLACK);
			
			this.group.add(this.jtbCase[i]);
			this.add(this.jtbCase[i]);
		}
		
		for(int i=0; i<this.GRIDLEN; i++){
			this.add(JpSquare[i]);
		}
		
		for(int i=0; i<this.GRIDLEN*this.GRIDLEN; i++){
			Color bg = ((maskSquare[i])%2 == 0)?Color.WHITE: Color.LIGHT_GRAY;
			this.jtbCase[i].setBackground(bg);
			JpSquare[maskSquare[i]].setBackground(bg);
			JpSquare[maskSquare[i]].add(this.jtbCase[i]);
		}
		
	}
	
	void setValidityLine(int x,int y, boolean validity){
		LineBorder border = new LineBorder((validity == true)?Color.BLACK:Color.red);
		for(int i=0; i<this.GRIDLEN;i++){
			this.jtbCase[x*this.GRIDLEN + i].setBorder(border);	
		}
	}
	
	void setValidityLine(int position, boolean validity){
		this.setValidityLine(position/this.GRIDLEN,position%this.GRIDLEN,validity);
	}
	
	void setValidityColumn(int x,int y, boolean validity){
		LineBorder border = new LineBorder((validity == true)?Color.BLACK:Color.red);
		for(int i=0; i<this.GRIDLEN;i++){
			this.jtbCase[i*this.GRIDLEN + y].setBorder(border);
		}
	}
	
	void setValidityColumn(int position, boolean validity){
		this.setValidityColumn(position/this.GRIDLEN,position%this.GRIDLEN,validity);
	}
		
	void setValiditySquare(int x,int y, boolean validity){
		LineBorder border = new LineBorder((validity == true)?Color.BLACK:Color.red);
		int xSquare = x-(x%this.N) , ySquare = y-(y%this.N);  // ou encore : _i = 3*(i/3), _j = 3*(j/3);
	    for (int i=xSquare ; i < xSquare+3; i++){
	        for (int j=ySquare; j < ySquare+3; j++){
	        	this.jtbCase[i*this.GRIDLEN + j].setBorder(border);
	        }
	    }
	}
	
	void setValiditySquare(int position, boolean validity){
		this.setValiditySquare(position/this.GRIDLEN,position%this.GRIDLEN,validity);
	}
}

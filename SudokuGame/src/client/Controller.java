package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

/**
 * @author thomas
 *
 */
public class Controller {

	private Client client;
	private JSudoku frame;
	private JBoardGame boardGame;
	private JToggleButton listCase[];
	private JButtonBar buttonBar;
	private JButton listButton[];
	
	public Controller (){
		this.client = new Client();
		this.frame = new JSudoku();
		this.startNewGame(3);
	}
	
	private void startNewGame(int n){
		int nbValue= n*n;
		
		String newGrid[] = this.client.getNewGrid(n);
		this.listCase = new JToggleButton[newGrid.length];
		for(int i=0; i<newGrid.length; i++){
			if(!newGrid[i].equalsIgnoreCase("0")){
				this.listCase[i] = new JToggleButton(newGrid[i]);
				this.listCase[i].setForeground(Color.BLACK);
				this.listCase[i].setEnabled(false);
			}else {
				this.listCase[i] = new JToggleButton();
			}			
		}
		this.boardGame = new JBoardGame(n,this.listCase);
		
		this.listButton = new JButton[nbValue+2];
		listButton[0] = new JButton("\u2a2f");//new ImageIcon("./bin/img/cadena-unlock-32.jpg"));
		listButton[0].addActionListener(new LockCelluleAction());
		listButton[1] = new JButton();//new ImageIcon("./bin/img/Croix-rouge-boutton-62.png"));
		listButton[1].addActionListener(new NumberButtonAction());
		for(int i=2; i<nbValue+2; i++){
			listButton[i] = new JButton(Integer.toString(i-1));
			listButton[i].addActionListener(new NumberButtonAction());
		}
		this.buttonBar = new JButtonBar(this.listButton);
		this.frame.add(this.boardGame, BorderLayout.CENTER);
	}
	
	private void initView(){	
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		JMenuItem menuItem = new JMenuItem("new game");
		menuItem.addActionListener(new NewGameAction());
		menu.add(menuItem);
		menuItem = new JMenuItem("Verify grid");
		menuItem.addActionListener(new VerifyGridAction());
		menu.add(menuItem);
		menubar.add(menu);
		this.frame.setJMenuBar(menubar);
		this.frame.add(this.buttonBar,BorderLayout.NORTH);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.pack();
	} 
	
	public void run(){
		this.initView();
		this.frame.setVisible(true);
	}
	
	public static void main(String[] argv){
		Controller controller = new Controller();
		controller.run();
	}
	
	private class NewGameAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			startNewGame(3);
		}
		
	};
	
	private class LockCelluleAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	};
	
	private class VerifyGridAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(client.verifyGrid()){
				Object[] options = {"Restart", "New game", "Nothing"};
				int n = JOptionPane.showOptionDialog(frame, "Great, you win !!!", "Congratulation",JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
				
				if(n==0){
					
				} else {
					if(n==1){
						startNewGame(3);
					}
				}
			}
		}
		
	};
	
	private class NumberButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton button = (JButton)arg0.getSource();
			if(boardGame.getSelectedIndex()>=0){
				listCase[boardGame.getSelectedIndex()].setText(button.getText());
				client.updateGrid(boardGame.getSelectedIndex(),(!button.getText().equals(""))?button.getText():"0");
				if(!button.getText().equals("")){
					boardGame.setValidityLine(boardGame.getSelectedIndex(), client.VerifyCase(boardGame.getSelectedIndex(),0));
					boardGame.setValidityColumn(boardGame.getSelectedIndex(), client.VerifyCase(boardGame.getSelectedIndex(),1));
					boardGame.setValiditySquare(boardGame.getSelectedIndex(), client.VerifyCase(boardGame.getSelectedIndex(),2));
				} else {
					boardGame.setValidityLine(boardGame.getSelectedIndex(), true);
					boardGame.setValidityColumn(boardGame.getSelectedIndex(), true);
					boardGame.setValiditySquare(boardGame.getSelectedIndex(), true);
				}
			}
		}
		
	};
}

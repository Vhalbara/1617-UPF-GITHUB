package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Serveur {

	private final String FACILE = "0 8 7 9 0 0 0 0 0 0 0 0 0 7 1 0 6 0 0 0 0 4 8 0 0 5 7 0 1 2 5 0 0 6 7 0 3 0 0 0 1 0 0 0 4 0 5 9 0 0 4 1 2 0 8 9 0 0 4 2 0 0 0 0 2 0 7 5 0 0 0 0 0 0 0 0 0 8 2 3 0";
	
	private Vector<Vector<Integer>> baseGrid, fixedMask;
	private int n=3, gridLen = n*n ;
	
	public Serveur(){
		Vector<Integer> line = new Vector<>();
		for(int i=0; i<this.gridLen; i++){
			line.add(new Integer(0));
		}
		
		this.fixedMask = new Vector<>(this.gridLen);
		this.baseGrid = new Vector<>(this.gridLen);
		
		for(int i=0; i<this.gridLen; i++){
			this.baseGrid.add(i,new Vector<>(line));
			this.fixedMask.add(i,new Vector<>(line));
		}
				
	}
	
	synchronized int getGridAt(int x, int y){
		return baseGrid.get(x).get(y);
	}

	synchronized int getGridAt(int position){
		return this.getGridAt(position/this.gridLen, position%this.gridLen);
	}
	
	synchronized void setGridAt(int x, int y, int value){
		if(fixedMask.get(x).get(y) == 0){
			baseGrid.get(x).set(y,new Integer(value));
		} else {
			logger("<Serveur> La valeur de cette case (int"+ x + ",int" + y +") est fixée.");
		}
	}
	
	synchronized void setGridAt(int position, int value){
		this.setGridAt(position/this.gridLen, position%this.gridLen, value);
	}
	
	synchronized void generateNewGrid(int n){
		this.n=n; 
		this.gridLen = n*n ;
		String sub[] = FACILE.split(" ") ;
		this.fixedMask = new Vector<>(this.gridLen);
		this.baseGrid = new Vector<>(this.gridLen);
		Vector<Integer> line = new Vector<>(this.gridLen), lineMask = new Vector<>(this.gridLen);
		
		for(int i=0; i<this.gridLen; i++){
			for(int j=0; j<this.gridLen; j++){
				line.add(j,new Integer(sub[i*this.gridLen+j]));
				lineMask.add(j,new Integer((Integer.parseInt(sub[i*this.gridLen+j])>0)?1:0));
			}
			this.baseGrid.add(i,new Vector<>(line));
			this.fixedMask.add(i,new Vector<>(line));
		}
	}
	
	synchronized String getCurrentGrid(){
		String grid = Integer.toString(getGridAt(0,0));
		for(int i=0; i<this.gridLen; i++){
			for(int j=(i>0)?0:1; j<this.gridLen; j++){
				grid += " " + Integer.toString(getGridAt(i,j));
			}
		}
		logger("<Serveur> the grid is " + grid + ".");
		return grid;
	}
	
	synchronized Boolean CheckLine(int x,int y){
		for(int i=0; i<this.gridLen;i++){
			if(getGridAt(x,i) == getGridAt(x,y) && i!=y) 
				return false;		
		}
		return true;
	}
	
	synchronized Boolean CheckLine(int position){
		return this.CheckLine(position/this.gridLen,position%this.gridLen);
	}
	
	synchronized Boolean CheckColumn(int x,int y){
		for(int i=0; i<this.gridLen;i++){
			if(getGridAt(i,y) == getGridAt(x,y) && i!=x) 
				return false;	
		}
		return true;
	}
	
	synchronized Boolean CheckColumn(int position){
		return this.CheckColumn(position/this.gridLen,position%this.gridLen);
	}
		
	synchronized Boolean CheckSquare(int x,int y){
		int xSquare = x-(x%this.n) , ySquare = y-(y%this.n);
	    for (int i=xSquare ; i < xSquare+3; i++){
	        for (int j=ySquare; j < ySquare+3; j++){
	        	if (this.getGridAt(i, j) == this.getGridAt(x, y) && (i!=x || j!=y))
	            	return false;
	        }
	    }
	    return true;
	}
	
	synchronized Boolean CheckSquare(int position){
		return this.CheckSquare(position/this.gridLen,position%this.gridLen);
	}
	
	synchronized Boolean VerifyBoardGame(){
		for(int i=0; i<this.gridLen*this.gridLen;i++){
			if(!CheckLine(i) || !CheckColumn(i) || !CheckSquare(i)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int portEcoute = 10309;
		ServerSocket serveurSock;	
		Serveur serveur = new Serveur();
		Socket socket;
		try {
			serveurSock = new ServerSocket(portEcoute);
			System.out.println("Le serveur a bien démarré.");
			while(true) {
				System.out.println();
				socket = serveurSock.accept();
				new Service(socket, serveur);
			}
		} catch(IOException exc) {
			System.out.println("probleme de connexion");
		}
	}

	public void logger(String log) {
		System.out.println(log);
	}
	
}

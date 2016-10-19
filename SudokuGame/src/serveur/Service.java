package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Service extends Thread {
	Socket socket;
	Serveur serveur;
	BufferedReader entree;
	PrintStream sortie;
	
	public Service(Socket socket, Serveur serveur) {
		this.socket = socket;
		try {
			this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.sortie = new PrintStream(socket.getOutputStream());
			this.serveur = serveur;
			this.start();
		} catch(IOException exc) {
			try {
				socket.close();
			}
			catch(IOException e){}
		}
	}
	
	synchronized private void SendNewGrid() throws IOException{
		String texte;
		texte=entree.readLine();
		String sub [] = texte.split("[|]");
		
		serveur.generateNewGrid(Integer.parseInt(sub[0]));
		
		sortie.println(serveur.getCurrentGrid());
		
		serveur.logger("<Service> New grid send.");
	}
	
	synchronized private void SendCurrentGrid() throws IOException{
		String texte;
		texte=entree.readLine();
		String sub [] = texte.split("[|]");
		
		serveur.generateNewGrid(Integer.parseInt(sub[0]));
		
		sortie.println(serveur.getCurrentGrid());
		
		serveur.logger("<Service> Current grid send.");
	}
	
	synchronized private void UpdateGrid() throws IOException{
		String texte;
		texte=entree.readLine();
		String sub [] = texte.split("[|]");
		
		int temp = serveur.getGridAt(Integer.parseInt(sub[0]));
		
		serveur.setGridAt(Integer.parseInt(sub[0]),Integer.parseInt(sub[1]));
		
		serveur.logger("<Service> Last value " + temp + " - New value " + serveur.getGridAt(Integer.parseInt(sub[0])) + ".");
	}
	
	synchronized private void CheckingGrid() throws IOException{
		String texte;
		texte=entree.readLine();
		serveur.logger(texte);
		String sub [] = texte.split("[|]");
				
		Boolean verif = false;
		if(sub[0].equalsIgnoreCase("line")){
			texte = "line";
			verif = serveur.CheckLine(Integer.parseInt(sub[1]));
		} else {
			if(sub[0].equalsIgnoreCase("column")){
				texte = "column";
				verif = serveur.CheckColumn(Integer.parseInt(sub[1]));
			} else {
				if(sub[0].equalsIgnoreCase("square")){
					texte = "square";
					verif = serveur.CheckSquare(Integer.parseInt(sub[1]));
				}
			}
		}
		
		serveur.logger("<Service> "+ texte + " " + verif + ".");
		sortie.println(verif);
	}
	
	synchronized private void CheckingBoardGame() throws IOException{
					
		Boolean verif = serveur.VerifyBoardGame();
		
		serveur.logger("<Service> this boardgame is " + verif + ".");
		sortie.println(verif);
	}
	
	private void SendErrorCode() throws IOException{
		serveur.logger("<Service> Error.");
		sortie.println(25);
	}
	
	public void run() {
		int chx;
		String texte;
		try {
			texte=entree.readLine();
			System.out.println(texte);
			try{
				chx = Integer.parseInt(texte.substring(0, 1));
			} catch(NumberFormatException e) {
				chx = -1;
			}
			switch(chx){
			case 1:
				this.SendNewGrid();
				break;
			case 2:
				this.SendCurrentGrid();
				break;
			case 3:
				this.UpdateGrid();
				break;
			case 4:
				this.CheckingGrid();
				break;
			case 5:
				this.CheckingBoardGame();
				break;
			default:
				this.SendErrorCode(); // code d'erreur (25)
			}
			
			sortie.println("|end");
			
			sortie.close();
			entree.close();
			socket.close();
		}	catch(IOException e) {
			System.exit(0);
		}
	}

}

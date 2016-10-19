package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	int portEcouteServeur;
	BufferedReader entree;
	PrintStream sortie;
	Socket socket;
	
	public Client(){
		this.portEcouteServeur = 10309;
	}
	
	public String[] getNewGrid(int n) {
		try {
			socket = new Socket("localhost", portEcouteServeur);
			entree = new BufferedReader(new	InputStreamReader(socket.getInputStream()));
			sortie = new PrintStream( socket.getOutputStream());
			
			sortie.println("1"+"|cmd");
			sortie.println(n+"|0");
			sortie.println("|end");
			
			String texte = entree.readLine();
			String fin;
			while(!(fin=entree.readLine()).equals("|end")){
				System.out.println(fin);
			}
			
			sortie.close();
			entree.close();
			socket.close();
			return texte.split(" ");
		}
		catch(FileNotFoundException exc) {
			System.out.println("Fichier	introuvable");
		}
		catch(UnknownHostException exc) {
			System.out.println("Destinataire inconnu");
		}
		catch(IOException exc) {
			System.out.println("Probleme d'entree-sortie");
		}
		return null;
	}

	public void updateGrid(int selectedIndex, String text) {
		try{
			socket = new Socket("localhost", portEcouteServeur);
			entree = new BufferedReader(new	InputStreamReader(socket.getInputStream()));
			sortie = new PrintStream( socket.getOutputStream());
			
			sortie.println("3"+"|cmd");
			sortie.println(selectedIndex+"|"+text);
			sortie.println("|end");
												
			sortie.close();
			entree.close();
			socket.close();
		}
		catch(FileNotFoundException exc) {
			System.out.println("Fichier	introuvable");
		}
		catch(UnknownHostException exc) {
			System.out.println("Destinataire inconnu");
		}
		catch(IOException exc) {
			System.out.println("Probleme d'entree-sortie");
		}
	}

	public boolean VerifyCase(int selectedIndex, int i) {
		String texte;
		boolean verif = true;
		try{
			socket = new Socket("localhost", portEcouteServeur);
			entree = new BufferedReader(new	InputStreamReader(socket.getInputStream()));
			sortie = new PrintStream( socket.getOutputStream());
			
			sortie.println("4"+"|cmd");
			sortie.println(((i==0)?"line":(i==1)?"column":"square") + "|" + selectedIndex);
			sortie.println("|end");
			
			while(!(texte=entree.readLine()).equals("|end")){
				verif = (texte.contains("true"))?true:false;
			}
			
			sortie.close();
			entree.close();
			socket.close();
		}
		catch(FileNotFoundException exc) {
			System.out.println("Fichier	introuvable");
		}
		catch(UnknownHostException exc) {
			System.out.println("Destinataire inconnu");
		}
		catch(IOException exc) {
			System.out.println("Probleme d'entree-sortie");
		}
		return verif;
	}

	public boolean verifyGrid() {
		String texte;
		boolean verif = true;
		try{
			socket = new Socket("localhost", portEcouteServeur);
			entree = new BufferedReader(new	InputStreamReader(socket.getInputStream()));
			sortie = new PrintStream( socket.getOutputStream());
			
			sortie.println("5"+"|cmd");
			sortie.println("|end");
			
			while(!(texte=entree.readLine()).equals("|end")){
				verif = (texte.contains("true"))?true:false;
			}
			
			sortie.close();
			entree.close();
			socket.close();
		}
		catch(FileNotFoundException exc) {
			System.out.println("Fichier	introuvable");
		}
		catch(UnknownHostException exc) {
			System.out.println("Destinataire inconnu");
		}
		catch(IOException exc) {
			System.out.println("Probleme d'entree-sortie");
		}
		return verif;
	}
}

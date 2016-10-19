#include "SudokuSolver.hxx"

SudokuSolver::SudokuSolver(){
	n=3;

	int tab[] = {1, 0, 0, 0, 0, 0, 5, 0, 0, 
				0, 0, 2, 8, 0, 3, 0, 6, 0, 
				0, 0, 0, 9, 2, 0, 0, 0, 1, 
				9, 0, 0, 0, 0, 0, 8, 0, 0, 
				0, 2, 0, 0, 9, 0, 0, 3, 0, 
				0, 0, 4, 0, 0, 0, 0, 0, 5,
				8, 0, 0, 0, 3, 2, 0, 0, 0, 
				0, 1, 0, 6, 0, 9, 7, 0, 0, 
				0, 0, 7, 0, 0, 0, 0, 0, 6};

	vector<int> line(Pow(n),0);
	for (int i=0; i<Pow(n); i++) {
   		grid.push_back(line);
		maskFixed.push_back(line);
		maskSquare.push_back(line);
	}

	vector<int> coordonee(2,0);
	int square=1;
	for(int i=0; i<Pow(n); i+=n){
		for(int j=0; j<Pow(n); j+=n){
			coordonee[0] = i; coordonee[1] = j;
			SquarePosition.push_back(coordonee);
			for(int k=0; k<n; k++){
				for(int l=0; l<n; l++){
					maskSquare[i+k][j+l] = square;
				}	
			} // fin du carre
			square++;
		}	
	}
	
	for(int i=0; i<n*n; i++){
		for(int j=0; j<n*n; j++){
			if(tab[i*n*n+j] > 0){
				grid[i][j] = tab[i*n*n+j];
				maskFixed[i][j] = 1;
			}			
		}
	}

}

SudokuSolver::~SudokuSolver(){
	
}

void SudokuSolver::genGrid(int taille){
    
	n=taille;
	vector<int> line(Pow(n),0);
	
	for (int i=0; i<Pow(n); i++) {
   		grid.push_back(line);
		maskFixed.push_back(line);
		maskSquare.push_back(line);
	}

	vector<int> coordonee(2,0);
	int square=1;
	for(int i=0; i<Pow(n); i+=n){
		for(int j=0; j<Pow(n); j+=n){
			coordonee[0] = i; coordonee[1] = j;
			SquarePosition.push_back(coordonee);
			for(int k=0; k<n; k++){
				for(int l=0; l<n; l++){
					maskSquare[i+k][j+l] = square;
				}	
			} // fin du carre
			square++;
		}	
	}
	
	int num;
	srand(time(NULL));
	
	for(int i=0; i<n*n; i++){
		for(int j=0; j<n*n; j++){
			while(!numAcceptable(i, j, num = rand()%9+1))
				grid[i][j] = num;
			maskFixed[i][j] = 1;		
		}
	}

	ShowGrid();
}

void SudokuSolver::Solve(){
	
	float temps;
	clock_t t2, t1 = clock();
	if(this->Backtracking(0)){
		t2 = clock();
		cout << "Solution trouvé." << endl;
		this->ShowGrid();
	} else {
		t2 = clock();
		cout << "Solution introuvable" << endl;
	}
	temps = (float)(t2-t1)/CLOCKS_PER_SEC;
	cout<< endl << "Temps pris pour trouvé le résultat: " << temps << " seconde(s)."<< endl;
}
	
bool SudokuSolver::Backtracking(int position){

    int ligne = position/(n*n), col = position%(n*n);

    if (position >= (n*n * n*n))
       return true; // succes!
 
    // Vérification: la case est-elle fixée?
	if(this->maskFixed[ligne][col] > 0){
		if (Backtracking(position+1))
			return true;
	} else {
		// consider digits 1 to 9
		for (int num = 1; num <= 9; num++){

		    // if looks promising
		    if (numAcceptable(ligne, col, num)) {
				
		        // make tentative assignment
		        grid[ligne][col] = num;
	 
		        // return, if success, yay!
		        if (Backtracking(position+1))
		            return true;
	 
		        // failure, unmake & try again
		        grid[ligne][col] = 0;
		    }
		}
    }

    return false; // this triggers backtracking
}

bool SudokuSolver::numAcceptable(int ligne, int col, int num){
    return AbsentLigne(ligne, num) && AbsentColonne(col, num) && AbsentCarre(ligne, col, num);// && AbsentDiagonale(ligne, col, num);
}

bool SudokuSolver::AbsentLigne(int x, int num){
	for(int y=0; y<n*n;y++){
		if((this->grid.at(x)).at(y) == num) 
			return false;		
	}
	return true;
}

bool SudokuSolver::AbsentColonne(int y, int num){
	for(int x=0; x<n*n;x++){
			if((this->grid.at(x)).at(y) == num) 
				return false;	
		}
		return true;
}

bool SudokuSolver::AbsentCarre(int x, int y, int num){

	int xSquare = x-x%n , ySquare = y-y%n;

    for (int row=0 ; row < n; row++){
        for (int col=0; col < n; col++){
        	if ((this->grid.at(row+xSquare)).at(col+ySquare) == num)
            	return false;
        }
    }
    return true;
}

bool SudokuSolver::AbsentDiagonale(int x, int y, int num){
	if(x == y){
		for (int i=0 ; i < this->Pow(this->n); i++){
			if ((this->grid.at(i)).at(i) == num)
					return false;
		}
	} else {
		if(y == this->Pow(this->n)-x-1){
			for (int i=0 ; i < this->Pow(this->n); i++){
				if (this->grid[i][this->Pow(this->n)-x-1] == num)
					return false;
			}
		}
	}
    return true;
}

void SudokuSolver::readGrid(string nameFile){

	vector<string> gridLoad;

    ifstream file(nameFile.c_str(), ios::in );
    if(file){
		int position = 0, nbVal=0, val;
        string ligne;

        while(getline(file, ligne, '\n')){
			gridLoad.push_back(ligne);
			nbVal++;
        }
		
		n=sqrt(nbVal);
		file.seekg(0, ios::beg);

		if((int)file.tellg() != 0)
        {
                file.clear();
                file.seekg(0, ios::beg);
        }

		while(getline(file, ligne, ' ')){
			val = atoi(ligne.c_str());
	      
			if(val > 0){
				grid[position/nbVal][position%nbVal] = atoi(ligne.c_str());
				maskFixed[position/nbVal][position%nbVal] = 1;
			}
			position++;
		}
		file.close();
	}
    else
    	cerr << "Erreur à l'ouverture !" << endl;
}

void SudokuSolver::ShowGrid(){
	for(int i=0; i<Pow(n); i++){
		cout << "|" << flush;
		for(int j=0; j<Pow(n); j++){
			cout << setw(n) <<  grid[i][j] << "|" << flush;
		}
		cout << endl << "+---+---+---+---+---+---+---+---+---+" << endl;
	}
}

inline int SudokuSolver::Pow(int base){
	return base * base;
}

void SudokuSolver::DebugAllGrid(){
	cout << "Grille de valeur." << endl;
	cout << "+---+---+---+---+---+---+---+---+---+" << endl;
	for(int i=0; i<Pow(n); i++){
		cout << "|" << flush;
		for(int j=0; j<Pow(n); j++){
			cout << setw(n) <<  grid[i][j] << "|" << flush;
		}
		cout << endl << "+---+---+---+---+---+---+---+---+---+" << endl;
	}
	cout << "Masque des valeurs valeurs fixees." << endl;
	cout << "+---+---+---+---+---+---+---+---+---+" << endl;
	for(int i=0; i<Pow(n); i++){
		cout << "|" << flush;
		for(int j=0; j<Pow(n); j++){
			cout << setw(n) <<  maskFixed[i][j] << "|" << flush;
		}
		cout << endl << "+---+---+---+---+---+---+---+---+---+" << endl;
	}
	cout << "Masque des carres." << endl;
	cout << "+---+---+---+---+---+---+---+---+---+" << endl;
	for(int i=0; i<Pow(n); i++){
		cout << "|" << flush;
		for(int j=0; j<Pow(n); j++){
			cout << setw(n) <<  maskSquare[i][j] << "|" << flush;
		}
		cout << endl << "+---+---+---+---+---+---+---+---+---+" << endl;
	}

	cout << "Affichage des coordonees de chaque carre." << endl;
	for(int i=0; i<Pow(n); i++){
		cout << "| carre " << i+1 << ": " << setw(2) <<  SquarePosition[i][0] << " - " << setw(2) << SquarePosition[i][1] << "|" << endl;
	}
}

inline void SudokuSolver::Debug(string str){
	cout << "Debug: " << str << "." << endl;
}

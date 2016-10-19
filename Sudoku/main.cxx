#include <iostream>
#include <cstdlib>

#include "SudokuSolver.hxx"

using namespace std;

int main(int argc, char** argv) {

	cout << "La commande saisie est" << flush;
	for(int i = 0; i < argc; i++) 
		cout << " " << argv[i] << flush;
	cout << endl;

	SudokuSolver sudo;
	
	sudo.genGrid(4);
	sudo.ShowGrid();
	sudo.Solve();
	sudo.ShowGrid();	
	return 0;
}

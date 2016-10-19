#ifndef __SUDOKU_SOLVER__
#define __SUDOKU_SOLVER__

#include <iostream>
#include <iomanip>
#include <vector>
#include <string>
#include <fstream>
#include <cstdlib>
#include <cmath>

using namespace std;

class SudokuSolver{

	int n;
	vector< vector<int> > grid;	// Pour les valeurs de la grille
	vector< vector<int> > maskFixed; // Pour les valeurs fixes
	vector< vector<int> > maskSquare; // Pour les carres

	vector< vector<int> > SquarePosition;

	bool Backtracking(int position);
	
	bool numAcceptable(int ligne, int col, int num);

	bool AbsentLigne(int x, int num);
	bool AbsentColonne(int y, int num);
	bool AbsentCarre(int x, int y, int num);
	bool AbsentDiagonale(int x, int y, int num);

	inline void Debug(string str);
	inline int Pow(int base);
public:
	SudokuSolver();
	~SudokuSolver();
	
	void Solve();
	void ShowGrid();
	void DebugAllGrid();
	void readGrid(string file);
	void genGrid(int taille);
};

#endif

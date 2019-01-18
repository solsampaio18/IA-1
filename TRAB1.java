
import java.lang.*;
import java.util.*;
import java.io.*;

class Node{
	private int[][] matrix;
	int profundidade = 0;
	Node pai = null;
	int custo = 1;
	char jogada = ' ';
	private int positionY;
	private int positionX;

	Node(int[][] matrix, int positionX, int positionY){
		this.matrix = matrix;
		this.positionY = positionY;
		this.positionX = positionX;
	}




	public char getJogada(){
		return jogada;
	}

	public int[][] getTable(){
		return matrix;
	}

	public int getX(){
		return positionX;
	}

	public int getY(){
		return positionY;
	}
}

public class TRAB1{
	static long startTime;
	static long endTime;
	static int positionY;
	static int positionX;
	static String moves = "";


	static boolean checkIfPossible(int[] array){
		int inversions = 0;
	int blank = 1; // 0 if blank in even row . 1 if blank in odd row

	for(int i = 0; i < array.length; i++){
		if(array[i] == 0 && ((i >= 0 && i <= 3) || (i >= 8 && i <= 11))){
			blank = 0;
		}

		if(array[i] != 0){
			inversions += array[i] - 1;
			for(int j = i; j >= 0; j--){
				if(array[j] != 0 && array[j] < array[i]){
					inversions--;
				}
			}
		}
	}


	if(blank == 0 && inversions%2 != 0){
		return true;
	}

	else if(blank == 1 && inversions%2 == 0){
		return true;
	}

	else{
		return false;
	}
}

static boolean compareMatrix(int[][] array1, int[][] array2){
	for(int i = 0; i < array1[0].length; i++){
		for(int j = 0; j < 4; j++){
			if(array1[i][j] != array2[i][j]){
				return false;
			}
		}
	}

	return true;
}


static void IDFS(Node jogoinicial, Node jogofinal){
	startTime = System.nanoTime();
	HashMap<String, Node> visitados = new HashMap<>();
	LinkedList<Node> filhos = new LinkedList<Node>();
	LinkedList<Node> queue = new LinkedList<Node>();
	int limite = 1;
	queue.add(jogoinicial);
	int nosgerados = 0;
    int nosarmazenados = 1;

	while(!queue.isEmpty()){

		Node temporary = queue.remove();
	    //System.out.println(Arrays.deepToString(temporary.getTable()) + " " + temporary.profundidade);

		if(compareMatrix(temporary.getTable(), jogofinal.getTable())){
			endTime = System.nanoTime();
			int profhere = temporary.profundidade;
		//moves = "Profundidade = " + temporary.profundidade + "\n";
			while(temporary.profundidade != 0){
				moves += temporary.getJogada() + " ";
				temporary = temporary.pai;
			}
			moves = new StringBuffer(moves).reverse().toString();
			System.out.println("Generated Nodes: " + nosgerados);
			System.out.println("Stored Nodes: " + nosarmazenados);
			System.out.println("PATH: " + moves);
			System.out.println("Depth: " + profhere);
			System.out.println("Time = " + ((endTime - startTime)/1000000) + " ms");
			return;
		}	


		if(temporary.profundidade + 1 >= limite && queue.isEmpty()){
			queue = new LinkedList<Node>();
		//System.out.println("queue empty = " + queue.isEmpty());
			queue.add(jogoinicial);
			visitados = new HashMap<String, Node>();
			limite++;
		//System.out.println("limite = " + limite);

		}

		else if(temporary.profundidade + 1 < limite){
			filhos = makeDescendants(temporary);
			for(int i = 0; i < filhos.size(); i++){
				nosgerados++;
				if(!visitados.containsKey(Arrays.deepToString(filhos.get(i).getTable()))){
					visitados.put(Arrays.deepToString(filhos.get(i).getTable()), filhos.get(i));
					queue.add(filhos.get(i));
					nosarmazenados++;
				}
			}
		}


	}

	System.out.println("solution not found");
	return;
}

static void GULOSA(Node jogoinicial, Node jogofinal){
	
	Scanner input = new Scanner(System.in);
    
	System.out.println("Selecione a heuristica que pretende: ");
	System.out.println("Elementos Fora     -> 1");
	System.out.println("Manhattan Distance -> 2");
    int heuris = input.nextInt();

    while(heuris != 1 && heuris != 2){
    	System.out.println("Selecione uma das opcoes!");
    	heuris = input.nextInt();
    }

	startTime = System.nanoTime();
	HashMap<String, Node> visitados = new HashMap<>();
	LinkedList<Node> filhos = new LinkedList<Node>();
	LinkedList<Node> queue = new LinkedList<Node>();

	queue.add(jogoinicial);

	visitados.put(Arrays.deepToString(jogoinicial.getTable()), jogoinicial);
	int number = 0;

	int nosgerados = 0;
    int nosarmazenados = 1;


	while(!queue.isEmpty()){
		Node temporary = queue.remove(number);
		//System.out.println("jogada = " + temporary.getJogada());

	    //System.out.println(Arrays.deepToString(temporary.getTable()) + " " + temporary.profundidade);
		if(compareMatrix(temporary.getTable(), jogofinal.getTable())){
		//System.out.println("masewewg");
			endTime = System.nanoTime();
		//moves = "Profundidade = " + temporary.profundidade + "\n";
			int profhere = temporary.profundidade;
			while(temporary.profundidade != 0){
				moves += temporary.getJogada() + " ";
				temporary = temporary.pai;
			}

			moves = new StringBuffer(moves).reverse().toString();
			System.out.println("Generated Nodes: " + nosgerados);
			System.out.println("Stored Nodes: " + nosarmazenados);
			System.out.println("PATH: " + moves);
			System.out.println("Depth: " + profhere);
			System.out.println("Time = " + ((endTime - startTime)/1000000) + " ms");
			return;
		}

		filhos = makeDescendantGreedy(temporary, jogofinal, heuris);

			//System.out.println("hasmap = " + visitados.keySet());

		for(int i = 0; i < filhos.size(); i++){
			nosgerados++;
			if(!visitados.containsKey(Arrays.deepToString(filhos.get(i).getTable()))){
				visitados.put(Arrays.deepToString(filhos.get(i).getTable()), filhos.get(i));
				queue.add(filhos.get(i));
				nosarmazenados++;
			}

		}

		int min = queue.get(0).custo;
		number = 0;
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).custo < min){
				min = queue.get(i).custo;
				number = i;
			}
		}
			//System.out.println("cwjgweg");
	    //System.out.println("wrherh = " + temporary.custo);
	}

	System.out.println("solution not found");
	return;
}

    
static void A_STAR(Node jogoinicial, Node jogofinal){
	
	Scanner input = new Scanner(System.in);
    
	System.out.println("Selecione a heuristica que pretende: ");
	System.out.println("Elementos Fora     -> 1");
	System.out.println("Manhattan Distance -> 2");
    int heuris = input.nextInt();

    while(heuris != 1 && heuris != 2){
    	System.out.println("Selecione uma das opcoes!");
    	heuris = input.nextInt();
    }
	startTime = System.nanoTime();
	HashMap<String, Node> visitados = new HashMap<>();
	LinkedList<Node> filhos = new LinkedList<Node>();
	LinkedList<Node> queue = new LinkedList<Node>();

	queue.add(jogoinicial);

	visitados.put(Arrays.deepToString(jogoinicial.getTable()), jogoinicial);
	int number = 0;

	int nosgerados = 0;
	int nosarmazenados = 1;

	while(!queue.isEmpty()){
		Node temporary = queue.remove(number);

	    //System.out.println(Arrays.deepToString(temporary.getTable()) + " " + temporary.profundidade);
		if(compareMatrix(temporary.getTable(), jogofinal.getTable())){
		//System.out.println("masewewg");
			endTime = System.nanoTime();
		//moves = "Profundidade = " + temporary.profundidade + "\n";
			int profhere = temporary.profundidade;
			while(temporary.profundidade != 0){
				moves += temporary.getJogada() + " ";
				temporary = temporary.pai;
			}

			moves = new StringBuffer(moves).reverse().toString();
			System.out.println("Generated Nodes: " + nosgerados);
			System.out.println("Stored Nodes: " + nosarmazenados);
			System.out.println("PATH: " + moves);
			System.out.println("Depth: " + profhere);
			System.out.println("Time: " + ((endTime - startTime)/1000000) + " ms");
			return;
		}

		filhos = makeDescendantGreedy(temporary, jogofinal, heuris);

			//System.out.println("hasmap = " + visitados.keySet());

		for(int i = 0; i < filhos.size(); i++){
			filhos.get(i).custo += filhos.get(i).pai.custo;
			nosgerados++;
			if(!visitados.containsKey(Arrays.deepToString(filhos.get(i).getTable()))){
				visitados.put(Arrays.deepToString(filhos.get(i).getTable()), filhos.get(i));
				queue.add(filhos.get(i));
				nosarmazenados++;
			}

		}

		int min = queue.get(0).custo;
		number = 0;
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).custo < min){
				min = queue.get(i).custo;
				number = i;
			}
		}
			//System.out.println("cwjgweg");
	    //System.out.println("wrherh = " + temporary.custo);
	}

	System.out.println("solution not found");
	return;

}

      static void BFS(Node jogoinicial, Node jogofinal){
      	startTime = System.nanoTime();
      	HashMap<String, Node> visitados = new HashMap<>();
      	LinkedList<Node> filhos = new LinkedList<Node>();
      	LinkedList<Node> queue = new LinkedList<Node>();

      	queue.add(jogoinicial);
      	int nosgerados = 0;
      	int nosarmazenados = 1;

      	while(!queue.isEmpty()){
      		Node temporary = queue.remove();
	    //System.out.println(Arrays.deepToString(temporary.getTable()) + " " + temporary.profundidade);
      		if(compareMatrix(temporary.getTable(), jogofinal.getTable())){
      			endTime = System.nanoTime();
		//moves = "Profundidade = " + temporary.profundidade + "\n";
      			int profhere = temporary.profundidade;
      			while(temporary.profundidade != 0){
      				moves += temporary.getJogada() + " ";
      				temporary = temporary.pai;
      			}

      			moves = new StringBuffer(moves).reverse().toString();
      			System.out.println("Generated Nodes: " + nosgerados);
				System.out.println("Stored Nodes: " + nosarmazenados);
      			System.out.println("PATH: " + moves);
      			System.out.println("Depth: " + profhere);
      			System.out.println("Time = " + ((endTime - startTime)/1000000) + " ms");
      			return;
      		}

      		filhos = makeDescendants(temporary);

      		for(int i = 0; i < filhos.size(); i++){
      			nosgerados++;
      			if(!visitados.containsKey(Arrays.deepToString(filhos.get(i).getTable()))){
      				visitados.put(Arrays.deepToString(filhos.get(i).getTable()), filhos.get(i));
      				queue.add(filhos.get(i));
      				nosarmazenados++;
      			}

      		}
      	}

      	System.out.println("solution not found");
      	return;

      }

      static void DFS (Node jogoinicial, Node jogofinal){
      	Scanner input = new Scanner(System.in);
      	System.out.print("Introduza o limite de procura: ");
      	int limite = input.nextInt();
      	startTime = System.nanoTime();
      	HashMap<String, Node> visitados = new HashMap<>();
      	LinkedList<Node> filhos = new LinkedList<Node>();
      	LinkedList<Node> queue = new LinkedList<Node>();

      	int nosgerados = 0;
      	int nosarmazenados = 1;

      	queue.add(jogoinicial);
      	while(!queue.isEmpty()){
      		Node temporary = queue.remove();
	    //System.out.println(Arrays.deepToString(temporary.getTable()) + " " + temporary.profundidade);

      		if(compareMatrix(temporary.getTable(), jogofinal.getTable())){
      			endTime = System.nanoTime();
      			int profhere = temporary.profundidade;
		//moves = "Profundidade = " + temporary.profundidade + "\n";
      			while(temporary.profundidade != 0){
      				moves += temporary.getJogada() + " ";
      				temporary = temporary.pai;
      			}
      			moves = new StringBuffer(moves).reverse().toString();
      			System.out.println("Generated Nodes: " + nosgerados);
				System.out.println("Stored Nodes: " + nosarmazenados);
      			System.out.println("PATH: " + moves);
      			System.out.println("Depth: " + profhere);
      			System.out.println("Time = " + ((endTime - startTime)/1000000) + " ms");

      			return;
      		}
      		if (temporary.profundidade + 1 <= limite){
      			filhos = makeDescendants(temporary);
      			for(int i = 0; i < filhos.size(); i++){
      				nosgerados++;
      				if(!visitados.containsKey(Arrays.deepToString(filhos.get(i).getTable()))){
      					visitados.put(Arrays.deepToString(filhos.get(i).getTable()), filhos.get(i));
      					queue.addFirst(filhos.get(i));
      					nosarmazenados++;
      				}

      			}
      		}

      		else{
      			visitados.remove(Arrays.deepToString(temporary.getTable()));
      		}
      	}

      	System.out.println("solution not found");
      	return;
      }


      static Node moveUp(Node game){

      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY - 1][positionX];
      	temp[positionY - 1][positionX] = 0;
      	Node toReturn = new Node(temp,positionX, positionY - 1);
      	toReturn.pai = game;
      	toReturn.jogada = 'U';
      	toReturn.profundidade = game.profundidade + 1;
      	return toReturn;


      }


      static Node moveDown(Node game){


      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY + 1][positionX];
      	temp[positionY + 1][positionX] = 0;
      	Node toReturn = new Node(temp,positionX, positionY + 1);
      	toReturn.pai = game;
      	toReturn.jogada = 'D';
      	toReturn.profundidade = game.profundidade + 1;
      	return toReturn;

      }

      static Node moveLeft(Node game){


      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY][positionX - 1];
      	temp[positionY][positionX - 1] = 0;
      	Node toReturn = new Node(temp,positionX - 1, positionY);
      	toReturn.pai = game;
      	toReturn.jogada = 'L';
      	toReturn.profundidade = game.profundidade + 1;
      	return toReturn;

      }

      static Node moveRight(Node game){
      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY][positionX + 1];
      	temp[positionY][positionX + 1] = 0;
      	Node toReturn = new Node(temp,positionX + 1, positionY);
      	toReturn.pai = game;
      	toReturn.jogada = 'R';
      	toReturn.profundidade = game.profundidade + 1;
      	return toReturn;
      }


    //heuristicas

      static Node moveUp(Node game, Node jogofinal, int heuris){

      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY - 1][positionX];
      	temp[positionY - 1][positionX] = 0;
      	Node toReturn = new Node(temp,positionX, positionY - 1);
      	toReturn.pai = game;
      	toReturn.jogada = 'U';
      	toReturn.profundidade = game.profundidade + 1;
      	
      	if(heuris == 1){
      		int fora = 0;
      		for(int i = 0; i < 4; i++){
      			for(int j = 0; j < 4; j++){
      				if(temp[i][j] != jogofinal.getTable()[i][j] ){
      					fora++;
      				}
      			}
      		}
      		//System.out.println("UP FORA = " + fora);
			toReturn.custo = fora;
      	}

      	else if(heuris == 2){
      		int manh = manhattanDistance(temp, jogofinal.getTable());
      		toReturn.custo = manh;
      	}
    	//System.out.println("mah = " + manh);
      	//int total = manh + fora;
      	//toReturn.custo = total;
    	//System.out.println("total up = " + total);
      	return toReturn;
      }


      static Node moveDown(Node game, Node jogofinal, int heuris){
      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY + 1][positionX];
      	temp[positionY + 1][positionX] = 0;
      	Node toReturn = new Node(temp,positionX, positionY + 1);
      	toReturn.pai = game;
      	toReturn.jogada = 'D';
      	toReturn.profundidade = game.profundidade + 1;
      	
      	if(heuris == 1){
      		int fora = 0;
      		for(int i = 0; i < 4; i++){
      			for(int j = 0; j < 4; j++){
      				if(temp[i][j] != jogofinal.getTable()[i][j] ){
      					fora++;
      				}
      			}
      		}
      		//System.out.println("DOWN FORA = " + fora);
			toReturn.custo = fora;
      	}

      	else if(heuris == 2){
      		int manh = manhattanDistance(temp, jogofinal.getTable());
      		toReturn.custo = manh;
      	}
        //System.out.println("mah = " + manh);
      	//int total = manh + fora;
		//System.out.println("total down = " + total);
      	//toReturn.custo = total;
      	return toReturn;

      }

      static Node moveLeft(Node game, Node jogofinal, int heuris){


      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j]=game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY][positionX - 1];
      	temp[positionY][positionX - 1] = 0;
      	Node toReturn = new Node(temp,positionX - 1, positionY);
      	toReturn.pai = game;
      	toReturn.jogada = 'L';
      	toReturn.profundidade = game.profundidade + 1;
      	
      	if(heuris == 1){
      		int fora = 0;
      		for(int i = 0; i < 4; i++){
      			for(int j = 0; j < 4; j++){
      				if(temp[i][j] != jogofinal.getTable()[i][j] ){
      					fora++;
      				}
      			}
      		}
      		//System.out.println("LEFT FORA = " + fora);
			toReturn.custo = fora;
      	}

      	else if(heuris == 2){
      		int manh = manhattanDistance(temp, jogofinal.getTable());
      		toReturn.custo = manh;
      	}
        //System.out.println("mah = " + manh);
      	//int total = manh + fora;
      	//toReturn.custo = total;
        //System.out.println("total left = " + total);
      	return toReturn;

      }

      static Node moveRight(Node game, Node jogofinal, int heuris){
      	int positionY = game.getY();
      	int positionX = game.getX();
      	int[][] temp = new int[4][4];
      	for(int i = 0; i < 4; i++){
      		for(int j=0 ; j<4; j++) {
      			temp[i][j] = game.getTable()[i][j];
      		}
      	}
      	temp[positionY][positionX] = temp[positionY][positionX + 1];
      	temp[positionY][positionX + 1] = 0;
      	Node toReturn = new Node(temp,positionX + 1, positionY);
      	toReturn.pai = game;
      	toReturn.jogada = 'R';
      	toReturn.profundidade = game.profundidade + 1;
      	

      	if(heuris == 1){
      		int fora = 0;
      		for(int i = 0; i < 4; i++){
      			for(int j = 0; j < 4; j++){
      				if(temp[i][j] != jogofinal.getTable()[i][j] ){
      					fora++;
      				}
      			}
      		}
      		//System.out.println("RIGHT FORA = " + fora);
			toReturn.custo = fora;
      	}

      	else if(heuris == 2){
      		int manh = manhattanDistance(temp, jogofinal.getTable());
      		toReturn.custo = manh;
      	}
      	//int total = manh + fora;
        //System.out.println("mah = " + manh);
      	//toReturn.custo = total;
        //System.out.println("total right = " + total);
      	return toReturn;
      }

      static int manhattanDistance(int[][] array1, int[][] array2){
      	int soma=0;
      	for(int i=0; i<4; i++) {
      		for(int j=0; j<4; j++) {
      			for(int k=0; k<4; k++) {
      				for(int w=0; w<4; w++) {
      					if(array1[i][j]==array2[k][w] && array1[i][j]!=0)
      						soma += (Math.abs(k-i)+Math.abs(w-j));
						//System.out.println("olha aqui = " + soma);

      				}
      			}
      		}
      	}

      	return soma;
      }


      static void arrayToMatrix(int[] array, int[][] config){

      	int i = 0;
      	for(int j = 0; j < 4; j++){
      		for(int z = 0; z < 4; z++){
      			config[j][z] = array[i];
      			if(config[j][z] == 0){
      				positionY = j;
      				positionX = z;
      			}
      			i++;
      		}
      	}


      }

      static void getkey(){
      	try{
      		System.in.read();
      	}catch(IOException e){
      		e.printStackTrace();
      	}
      }

      static LinkedList<Node> makeDescendants(Node pai){
      	LinkedList<Node> children = new LinkedList<Node>();
      	if(pai.getY() != 0){
      		children.add(moveUp(pai));
      	}

      	if(pai.getY() != 3){
      		children.add(moveDown(pai));
      	}

      	if(pai.getX() != 0){
      		children.add(moveLeft(pai));
      	}

      	if(pai.getX() != 3){
      		children.add(moveRight(pai));
      	}

      	return children;
      }

      static LinkedList<Node> makeDescendantGreedy(Node pai, Node jogofinal, int heuris){
      	LinkedList<Node> children = new LinkedList<Node>();
      	if(pai.getY() != 0){
      		children.add(moveUp(pai,jogofinal, heuris));
      	}
    	// 	getkey();
      	if(pai.getY() != 3){
      		children.add(moveDown(pai, jogofinal, heuris));
      	}
    	//getkey();
      	if(pai.getX() != 0){
      		children.add(moveLeft(pai, jogofinal, heuris));
      	}
    	//getkey();
      	if(pai.getX() != 3){
      		children.add(moveRight(pai, jogofinal, heuris));
      	}
    	//getkey();

      	return children;
      }

      static void menu(Node jogoinicial, Node jogofinal){
      	System.out.println("Qual algoritmo quer usar?");
      	System.out.println("IDFS   -> 1");
      	System.out.println("Greedy -> 2");
      	System.out.println("A_STAR -> 3");
      	System.out.println("BFS    -> 4");
      	System.out.println("DFS    -> 5");
      	Scanner input = new Scanner(System.in);
      	int opcao = input.nextInt();
      	switch(opcao){
      		case 1: IDFS(jogoinicial, jogofinal);
      		break;

      		case 2: GULOSA(jogoinicial, jogofinal);
      		break;
	    
	      case 3: A_STAR(jogoinicial, jogofinal);
	      break;

	      case 4: BFS(jogoinicial , jogofinal);
	      break;

	      case 5: DFS(jogoinicial , jogofinal);
	      break;

	      default: System.out.println("Escolha uma das opcoes do menu!");
	      break;		    		
	  }
	}


	public static void main(String[] args){
		System.out.println("Bem-vindo ao Jogo dos 15!");
		System.out.println("Introduza a configuracao inicial do tabuleiro:");
		Scanner input = new Scanner(System.in);
		int[] arrayInicial = new int[16];
		int[] arrayFinal = new int[16];
		int[][] configinicial = new int[4][4];
		int[][] configfinal = new int[4][4];

		Runtime runtime = Runtime.getRuntime();
		long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

		for(int i = 0; i < arrayInicial.length; i++){
			arrayInicial[i] = input.nextInt();
		}

		boolean condI = checkIfPossible(arrayInicial);

		System.out.println("Insira agora a configuracao final:");
		for(int i = 0; i < arrayFinal.length; i++){
			arrayFinal[i] = input.nextInt();
		}
		
		boolean condF = checkIfPossible(arrayFinal);
		
		if(condI == condF){
			arrayToMatrix(arrayInicial, configinicial);

			Node inicial = new Node(configinicial, positionX, positionY); 
			arrayToMatrix(arrayFinal, configfinal);
			Node fim = new Node(configfinal, positionX, positionY); 

			menu(inicial, fim);
		}

		else{
			System.out.println("Impossible");
		}

		long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
    	System.out.println("Memory: " + (usedMemoryAfter-usedMemoryBefore));


		return;

	}
}


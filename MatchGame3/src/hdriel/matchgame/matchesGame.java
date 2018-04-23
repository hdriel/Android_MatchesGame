package hdriel.matchgame;

import java.util.Random;

enum state {PLAY , GAME_OVER, WINNER};

public class matchesGame {
	
	final static int DEFUALT_SIZE = 16;
	final static int  MAX_TAKE = 3;
	final static int  MIN_TAKE = 1;

	private int size;       // Amount of matches
	private boolean board[];// Game board - the array of matches
	private state State;    // The state of the game
	private int minTake;    // Amount of the minimum matches to take
	private int maxTake;    // Amount of the maximum matches to take
	private int index;      // The current index of the left match who have taken
    private boolean vs_computer;
    private Random randnum;
    boolean turnPlayerA; 
    
	private int computingHowTake(){  // The computer compute who many match to take
		int posibileToTake = maxTake - minTake + 1;
		int left = size - (index);

		int needToTake = left % (posibileToTake + 1);

		if (needToTake == 0)
			return (randnum.nextInt(posibileToTake) + 1);
		else
			return needToTake;
	}                     
	private void take(int amount){           // The action of the TAKE some amount of matches from the index
		if (amount >= minTake && amount <= maxTake)
		{
			for (int i = index; i < (index + amount); i++)
			{
				if (i < size){
					board[i] = true;
				}
			}
		}
		else {
		}
		index += amount;	
	}                           
	private boolean checkfinish(){                     // Check if we finish the game - the last match have taken!
		return board[size - 1]; // If the last matches have taken
	}                         
	
    public matchesGame(boolean vs_Coputer, boolean start){                                 // Ctor - defualt
    	randnum = new Random();
    	
    	this.turnPlayerA = start;
    	this.vs_computer = vs_Coputer;
    	this.size = DEFUALT_SIZE;
    	this.board = null;
    	this.State = state.PLAY;
    	this.minTake = MIN_TAKE;
    	this.maxTake = MAX_TAKE;
    	this.index = 0;
    	this.board = new boolean[size];
    	for (int i = 0; i < size; i++)
    		board[i] = false;
    }                                   
	public matchesGame(int size, boolean vs_Coputer, boolean start){                         // Ctor - custom size
		randnum = new Random();
		
    	this.turnPlayerA = start;
		this.vs_computer = vs_Coputer;
		this.size = size;
		this.board = null;
		this.State = state.PLAY;
		this.minTake = MIN_TAKE;
		this.maxTake = MAX_TAKE;
		this.index = 0;
		this.board = new boolean[size];
		for (int i = 0; i < size; i++)
			board[i] = false;
		
	}                          
	
	public matchesGame(int size , int max , boolean vs_Coputer, boolean start){               // Ctor - custom size and range of matches to take 
		randnum = new Random();
		
    	this.turnPlayerA = start;
		this.vs_computer = vs_Coputer;
		this.size = size;
		this.State = state.PLAY;
		this.minTake = MIN_TAKE;
		this.maxTake = max;
		this.index = 0;
		
		this.board = new boolean[size];
		for (int i = 0; i < size; i++)
			board[i] = false;
	}                 

	public boolean play(int amountMatchesPlayer){                                   // Method to run/manage the game
		
		if(vs_computer){
				if (turnPlayerA) // turn of player
				{
					take(amountMatchesPlayer);
					if (checkfinish()) State = state.WINNER;
				}
				else  // turn of computer
				{
					take(computingHowTake());
					if (checkfinish()) State = state.GAME_OVER;
				}
				turnPlayerA = !turnPlayerA; // change the turn
		}
		else{
				// print the update stage game 
				if (turnPlayerA) // turn of player
				{
					take(amountMatchesPlayer);
					if (checkfinish()) State = state.WINNER;
				}
				else  // turn of computer
				{
					take(amountMatchesPlayer);
					if (checkfinish()) State = state.GAME_OVER;
				}
				turnPlayerA = !turnPlayerA; // change the turn
		}
		
		if(State != state.PLAY)
			return false;
		else 
			return true;
		
	} 
	
	public boolean getStateMatchesByIndex(int i){if(i >= 0 && i < size) return board[i]; else return false;}
	
	public int getIndex(){return index;}
	public int getMax(){return maxTake;}
	public int getMin(){return minTake;}
	public boolean getMode(){return vs_computer;}
	public boolean getStart(){return turnPlayerA;}
	public int getSize(){return size;}
}

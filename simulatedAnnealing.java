import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList; // import the ArrayList class
import java.util.Random;
import java.lang.Math;

public class simulatedAnnealing{
  int numberOfPlayers;

  int[] initSol;
  int[] currentSol;
  int[][] matrixGraph;
  String[][] refPlayer;

  public simulatedAnnealing(String filename){
    FileReader instance = new FileReader(filename);
    instance.parseData();
    numberOfPlayers = instance.getNumberOfPlayers();
    refPlayer = instance.getRefPlayer();
    matrixGraph = instance.getDataMatrix();
  }

  public int KemenyScore(int[] solution){
    //System.out.println(solution.length);
    int cost = 0;
    for(int i = 0; i < numberOfPlayers; i++){
      int z = numberOfPlayers - i - 1;
      int playerID = solution[z];
      //System.out.println(solution[z]);

      int a = 0;
      while( a < z){
        cost += matrixGraph[playerID - 1][a];
        a++;
        }
      }
      //System.out.println("Cost of solution: " + cost);
    return cost;
  }

  public void algorithm(){
    // construct initial solution x_0 (initSol)
    // set current solution to = init solution

    initSol = new int[numberOfPlayers];
    currentSol = new int[numberOfPlayers];
    for(int i = 0; i < numberOfPlayers; i++){
      int playerID = Integer.parseInt(refPlayer[i][0]);

      initSol[i] = playerID;
      currentSol[i] = playerID;
    }

    double temperature = 100;   // Set init Temp
    double tempLen = 5;          // Set Temperature Length
    double coolingRatio = 0.5; // Cooling Rate
    int non_num_improve = 4;
    boolean stopNowCriteria = false;
    //System.out.println(currentSol.length);
    int cost = KemenyScore(currentSol);

    int iterationsSinceLastChange = 0;

    while(stopNowCriteria == false){

      for(int i = 1; i <= tempLen; i++){

        iterationsSinceLastChange++;
        // randomly generate a neighbouring solution
        // two change neighbour
        int firstChoice = chooseRandomNumber(numberOfPlayers, 0);
        int secondChoice = getDistinctRandom(firstChoice);
        int[] nowSol = new int[numberOfPlayers];

        for(int j = 0; j < numberOfPlayers; j++){
          nowSol[j] = currentSol[j];
        }

          nowSol[firstChoice] = currentSol[secondChoice];
          nowSol[secondChoice] = currentSol[firstChoice];
          // two change neighbour to currentSol is nowSol

          //System.out.println(KemenyScore(nowSol) +" "+ cost);
          int changeCost = KemenyScore(nowSol) - cost;
          if(changeCost <= 0){
            currentSol = nowSol;
            iterationsSinceLastChange = -1;
          } else{
              double q = Math.random();
              double prob = Math.exp(-changeCost / temperature);
              //System.out.println("Probability of Change: " + prob);
              if(q < prob){
                currentSol = nowSol;
                iterationsSinceLastChange = -1;
                }
              }

            temperature = coolingRatio * temperature;
            cost = cost + changeCost;

            if(iterationsSinceLastChange == non_num_improve){
              System.out.println("Exiting with solution " + cost);
              stopNowCriteria = true;
              break;
            }
          }
        }

        displayResults(currentSol);
      }

  public void displayResults(int[] solution){
    System.out.println("Position | Player Name");
    for(int i =0; i< numberOfPlayers; i++){
      System.out.println(i+1 + "   | " + refPlayer[solution[i] -1 ][1]);
    }
  }

  public int getDistinctRandom(int firstChoice){
    int secondChoice = chooseRandomNumber(numberOfPlayers, 0);
    if(Math.abs(firstChoice - secondChoice) > 1){
      return secondChoice;
    } else {
      getDistinctRandom(firstChoice);

    }
    return secondChoice;
  }

  public int chooseRandomNumber(int max, int min){
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

  public static void main(String[] args){
    if(args.length == 1){
      simulatedAnnealing SA = new simulatedAnnealing(args[0]);
      SA.algorithm();
    } else{
      System.out.println("Missing file parameter");
    }
  }
}



class FileReader{
  // Reads and parses data file
  // Following format of
  // line 1: number of players

  int numberOfPlayers;
  int[][] playerData;
  String[][] refPlayer;

  ArrayList<String> data = new ArrayList<String>();

  public FileReader(String filename){
    try{
      File myFile = new File(filename);
      Scanner myReader = new Scanner(myFile);

      while(myReader.hasNextLine()){
        data.add(myReader.nextLine());
      }

    } catch (FileNotFoundException e){
      System.out.println("File not found");
    }
  }

  public void parseData(){
    // Convert data from one big ArrayList<String> to smaller versions with correct data types
    parseNumberOfPlayers();
    parseRefPlayer();
    parseDataMatrix();

  }

  public void parseNumberOfPlayers(){
    // Identify number of players
      numberOfPlayers = Integer.parseInt(data.get(0));
  }

  public void parseRefPlayer(){
    // Gets list of players and related number codes
    refPlayer = new String[numberOfPlayers][2];

    // Extracting Player Names and Values
    for(int i = 0; i < numberOfPlayers; i++){
      // Get data row and split into array
      String temporary = data.get(i+1);
      String[] tempArray = temporary.split(",");

      // Store playerID in first position and name in second
      refPlayer[i][0] = tempArray[0];
      refPlayer[i][1] = tempArray[1];

    }
  }

  public void parseDataMatrix(){
    // Populates Graph Matrix from text file data

    playerData = new int[numberOfPlayers][numberOfPlayers];
    for(int i = 0; i < numberOfPlayers; i++){
      for(int j=0; j < numberOfPlayers; j++){
          playerData[i][j] = 0;
        }
      }

    // Populate Matrix with winning values only
    // ie.  i wins over j by weight
    for(int i = numberOfPlayers + 2; i < data.size(); i++){
      String[] Line = data.get(i).split(",");
      int weight = Integer.parseInt(Line[0]);
      int winner = Integer.parseInt(Line[1]);
      int loser = Integer.parseInt(Line[2]);

      playerData[winner-1][loser-1] = weight; // i defeated j by margin weight

    }
  }

  public int getNumberOfPlayers(){
    return numberOfPlayers;
  }

  public String[][] getRefPlayer(){
    return refPlayer;
  }

  public int[][] getDataMatrix(){
    // Return Matrix of all data if exists
    return(playerData);
  }

}

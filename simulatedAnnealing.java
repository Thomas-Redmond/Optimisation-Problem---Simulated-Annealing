import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList; // import the ArrayList class
import java.util.Random;
import java.lang.Math;

public class simulatedAnnealing{
  int numberOfPlayers;
  int bestCost;

  int[] BestSolution;
  int[] InitialSolution;
  int[] CurrentSolution;
  int[] PotentialSolution;

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
    // Perform Simulated Annealing Algorithm

    // Declare arrays to be used
    BestSolution = new int[numberOfPlayers];
    InitialSolution = new int[numberOfPlayers];
    CurrentSolution = new int[numberOfPlayers];
    PotentialSolution = new int[numberOfPlayers];

    // Populate Initial, Current, and Best Arrays using order supplied in external doc
    // Using deep-copy approach to retain independance when modifying
    for(int i = 0; i < numberOfPlayers; i++){
      int playerID = Integer.parseInt(refPlayer[i][0]);

      BestSolution[i] = playerID;
      InitialSolution[i] = playerID;
      CurrentSolution[i] = playerID;
    }

    // Declare Starting Variables
    double temperature = 2;       // Set initial Temperature
    double temperatureLength = 1;   // Set Temperature Length
    double coolingRatio = 0.99999;  // Cooling Rate

    int non_num_improve = 1;        // best solution not improved in x iterations, end program
    int iterationsSinceLastChange = 0;

    boolean stopNowCriteria = false;// should algorithm be terminated

    // Get Initial Solution cost
    int cost = KemenyScore(InitialSolution);

    while(stopNowCriteria == false){

      for(int i = 1; i <= temperatureLength; i++){
        System.out.println("New Iteration");
        iterationsSinceLastChange++;

        // Select a random gap between two objects
        // Gap ie 1, 2, 3 (has 2 gaps): Gap 0 being comma between 1 and 2
        // this instance 1 and 2 would be switched
        int randomChoice = chooseRandomNumber(numberOfPlayers - 1, 0);

        // Select values either side of that gap
        // These will be swapped
        int a = CurrentSolution[randomChoice];
        int b = CurrentSolution[randomChoice + 1];

        // Pre-Change
        // If A won against B: [A][B] > 0, but value would not be counted
                          // : [B][A] = 0
        // If B won against A: [B][A] > 0, and value would have been counted
                          // : [A][B] = 0
                          // Hence Minus [B][A]

        // Post Change
        // If A won against B: [A][B] > 0, value would be counted
                          // : [B][A] = 0
        // If B won against A: [B][A] > 0, and value  would not have been counted
                          // : [A][B] = 0

        int changeCost = matrixGraph[b][a] + matrixGraph[a][b];

        // Update to Potential Solution Cost
        cost += changeCost;

        if(changeCost <= 0){
          // Update Current Solution
          CurrentSolution[randomChoice] = b;
          CurrentSolution[randomChoice + 1] = a;
          if(cost < bestCost){
            for(int j =0; j < numberOfPlayers; j++){
              // Deep Copy
              BestSolution[j] = CurrentSolution[j];
            }
            iterationsSinceLastChange = -1;
          } else{
            double q = Math.random();
            double prob = Math.exp(-changeCost / temperature);
            if(q < prob){
              CurrentSolution[randomChoice] = b;
              CurrentSolution[randomChoice + 1] = a;
            }
          }

          temperature = coolingRatio * temperature;

        }

    }

  public void displayResults(int[] solution){
    System.out.println("Position | Player Name");
    for(int i =0; i< numberOfPlayers; i++){
      System.out.println(i+1 + "   | " + refPlayer[solution[i] -1 ][1]);
    }
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
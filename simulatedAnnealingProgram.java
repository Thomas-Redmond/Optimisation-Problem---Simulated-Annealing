import java.util.Random;
import java.lang.Math;

public class simulatedAnnealingProgram{
  // Performs the Simulated Annealing Algorithm on a dataset passed as filename parameter

  // Algorithm in function Algorithm


  int num;
  int cost;
  int bestCost;

  Random random = new Random();

  ranking initialSolution;
  ranking bestSolution;

  FileReader reader;
  DataParser fixesData;
  wgMatrix matrix;

  public simulatedAnnealingProgram(String filename){

    reader = new FileReader(filename); // Read Data from file
    fixesData = new DataParser(reader.data); // Parse Read data according to rules given


    matrix = fixesData.getMatrix(); // grab matrix object from DataParser class

    num = fixesData.getPlayerNum(); // grab numberOfPlayers from DataParser

    // Load Initial Solution on both
    initialSolution = new ranking(num, fixesData.getInitSol());
    bestSolution = new ranking(num, fixesData.getInitSol());

    // Set bestCost and current cost to initialSolution cost
    bestCost = KemenyScore(initialSolution);
    cost = bestCost;

  }

  public int chooseRandomNumber(int max, int min){
    // Get random number from min <= x < max
    return random.nextInt(max - min) + min;
  }

  /*

  */
  public void algorithm(double temp, int tempLen, double coolRat, int noNumImprove){

    // Declare Starting Variables
    double temperature = temp;
    int temperatureLength = tempLen;
    double coolingRatio = coolRat;

    // Stopping Criteria Variables
    int no_num_improve = noNumImprove * temperatureLength;
    int iterSinceLastImprove = 0;
    boolean runSA = true;

    int totalIterations = 0;

    while(runSA){
      // Main SA loop

      for(int i = 0; i <= temperatureLength; i++){
        totalIterations++;
        iterSinceLastImprove++;

        // Choose Random number x: 0 <= x <= num - 1
        int randomChoice = chooseRandomNumber(num - 1, 0 );

        // Make current ranking from initalranking and swap two values
        ranking currentSolution = new ranking(num, initialSolution.getSequence());
        int[] swappedValues = currentSolution.swapValues(randomChoice);

        int changeCost = matrix.getValue(swappedValues[0], swappedValues[1]) - matrix.getValue(swappedValues[1], swappedValues[0]);


        if(changeCost <= 0){

          // Accept new state, update cost
          initialSolution.swapValues(randomChoice);
          cost += changeCost;

          // Update bestCost if current Solution better
          if(cost < bestCost){
            bestCost = cost;
            bestSolution = new ranking(num, currentSolution.getSequence());

            iterSinceLastImprove = 0; // reset
          }

        } else {
          // Should we accept the worse state anyway ?
          double q = Math.random();
          double prob = Math.exp(- changeCost / temperature);
          if(q < prob){
            // Accept new State, update cost
            initialSolution.swapValues(randomChoice);
            cost += changeCost;
          }
        }

        // Test for Stopping Criteria
        if(iterSinceLastImprove == no_num_improve){
          runSA = false; // Stopping Criteria Reached
        }

      }

      // End of Temperature Length iteration
      temperature = coolingRatio * temperature;

    }

    // End of while loop
    //System.out.println("Best cost found: " + bestCost);
    //System.out.println("Total Iterations: " + totalIterations);

    displayResults(bestSolution);
  }

  public int KemenyScore(ranking r){
    // Identify beginning Kemeny Score

    int kCost = 0;

    // Get Worst -> Best Player ID
    for(int i = 0; i < num; i++){

      int badPlayerPointer = num - i - 1;
      int badPlayerID = r.getByPointer(badPlayerPointer);

      // Get Best ID up to current badPlayer ID
      int goodPlayerPointer = 0;
      while( goodPlayerPointer < badPlayerPointer){

        int goodPlayerID =  r.getByPointer(goodPlayerPointer);

        // If bad beat good add to score
        kCost += matrix.getValue(badPlayerID, goodPlayerID);
        goodPlayerPointer++;
        }
      }
      //System.out.println("Cost of solution: " + cost);
    return kCost;
  }

  public void displayResults(ranking solution){
    // Ouput Results in table format
    String[][] playerNames = fixesData.getListPlayerNames();

    System.out.println("Position | Player Name");


    for(int i = 0; i < num; i++){
      int playerID = solution.getByPointer(i);
      String name = playerNames[playerID - 1][1];
      System.out.println(i+1 + " | " + name);
    }

    System.out.println("Best Cost: " + bestCost);

  }


  public static void main(String[] args){
    // Start Timer and begin SA
      long startTime = System.currentTimeMillis();
      // Test for filename given as parameter
      if(args.length < 1){

        System.out.println("Missing File Parameter");
      } else {

        // Pass filename
        simulatedAnnealingProgram findSolution = new simulatedAnnealingProgram(args[0]);

        findSolution.algorithm(10, 10, 0.9999, 25000); // temperature, temperature Length, cooling ratio, and no_num_improve value

      }
      long endTime = System.currentTimeMillis();
      System.out.println("Duration: " + (endTime - startTime) + " milliseconds");
    }
}

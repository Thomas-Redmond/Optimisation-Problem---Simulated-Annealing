import java.util.Random;
import java.lang.Math;

public class SA{

  int num;
  int cost;
  int bestCost;

  Random random = new Random();

  ranking initialSolution;
  ranking bestSolution;

  FileReader reader;
  DataParser fixesData;
  wgMatrix matrix;

  public SA(String filename){

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

    algorithm();

  }

  public int chooseRandomNumber(int max, int min){
    // Get random number from min <= x < max
    return random.nextInt(max - min) + min;
  }

  public void algorithm(){

    // Declare Starting Variables
    double temperature = 10;
    int temperatureLength = 10;
    double coolingRatio = 0.999;

    // Stopping Criteria Variables
    int no_num_improve = 4 * temperatureLength;
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
        currentSolution.swapValues(randomChoice);

        int changeCost = KemenyScore(currentSolution) - cost;

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
    System.out.println("Best cost found: " + bestCost);
    System.out.println("Total Iterations: " + totalIterations);

  }

  public int KemenyScore(ranking r){

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

  public static void main(String[] args){
    // Test for filename given as parameter
    if(args.length < 1){

      System.out.println("Missing File Parameter");
    } else {

      SA solution = new SA(args[0]);

    }

  }




}

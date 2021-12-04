public class SA{

  FileReader reader;
  DataParser fixesData;
  wgMatrix matrix;

  public SA(String filename){
    // Start SimulatedAnnealing process
    reader = new FileReader(filename);
    fixesData = new DataParser(reader.data);

    matrix = fixesData.getMatrix();

    int num = fixesData.getNumberOfPlayers();

    ranking initialSolution = new ranking(num, fixesData.getInitSol());

  }

  public static void main(String[] args){
    // Test for filename given as parameter
    if(args.length < 1){

      System.out.println("Missing File Parameter");
    } else {

      try{
        SA solution = new SA(args[0]);

      } catch (Exception e){
        System.out.println(e);
      }

    }

  }




}

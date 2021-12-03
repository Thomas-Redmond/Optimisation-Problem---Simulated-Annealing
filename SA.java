public class SA{

  FileReader reader;
  DataParser fixData;
  wgMatrix matrix;

  public SA(String filename){
    // Start SimulatedAnnealing process
    reader = new FileReader(filename);
    fixData = new DataParser(reader.data);
    matrix = fixData.getMatrix();

    System.out.println("Present and Correct");
  }

  public static void main(String[] args){
    if(args.length != 1){
      System.out.println("Missing File Parameter")
    }

    try{
      SA solution = new SA(args[0]);
    } catch (Exception e){
      System.out.println(e);
    }
  }

}

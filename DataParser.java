import java.util.ArrayList;

class DataParser{
  // Takes data from given FileReader class
  // Parses data according to rules gven in "How_to_read_tournament_data.pdf"

  int playerNum;
  int[] initSol;
  String[][] listPlayerNames;

  wgMatrix matrix; // Points to weighted graph representing matrix class


  public DataParser(ArrayList<String> fileData){
    // Takes data in String List
    // Grabs number of players and creates matrix accordingly

    playerNum = Integer.parseInt(fileData.get(0));
    matrix = new wgMatrix(playerNum);

    parsePlayerNames(fileData);

    parseMatrix(fileData);

  }

  public void parsePlayerNames(ArrayList<String> fileData){
    // Populate listPlayerNames using format [PlayerID]["Player Name"]
    listPlayerNames = new String[playerNum][2];

    for(int i = 1; i <= playerNum; i++){
      // Splits Each row into seperate Strings by commas
      String[] tempArray = fileData.get(i).split(",");
      listPlayerNames[i - 1][0] = tempArray[0];
      listPlayerNames[i - 1][1] = tempArray[1];

      // Load Initial solution
      initSol[i - 1] = tempArray[0];
    }
  }


  public void parseMatrix(ArrayList<String> fileData){
    // Populate matrix variable
    System.out.println("FileData size: " + fileData.size());
    for(int i = playerNum + 2; i < fileData.size(); i++){
      String[] Line = fileData.get(i).split(",");

      int weight = Integer.parseInt(Line[0]);
      int winner = Integer.parseInt(Line[1]);
      int loser = Integer.parseInt(Line[2]);

      matrix.setValue(winner, loser, weight);
    }
  }


  public int getPlayerNum(){
    return playerNum;
  }

  public int[] getInitSol(){
    return initSol;
  }

  public String[][] getListPlayerNames(){
    return listPlayerNames;
  }

  public wgMatrix getMatrix(){
    return matrix;
  }

}

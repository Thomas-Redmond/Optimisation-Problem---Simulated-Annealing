class DataParser{
  // Takes data from given FileReader class
  // Parses data according to rules gven in "How_to_read_tournament_data.pdf"

  int playerNum;
  String[][] listPlayerNames;

  wgMatrix matrix; // Points to weighted graph representing matrix class


  public DataParser(String[] fileData){
    // Takes data in String List
    // Grabs number of players and creates matrix accordingly

    playerNum = Integer.ParseInt(fileData.[0]);
    matrix = new wgMatrix(playerNum);

    parsePlayerNames(fileData);

    parseMatrix(fileData);
    System.out.println("Matrix created");

  }

  public void parsePlayerNames(String[] fileData){
    // Populate listPlayerNames using format [PlayerID]["Player Name"]
    listPlayerNames = new String[playerNum][2];

    for(int i = 1; i <= playerNum; i++){
      // Splits Each row into seperate Strings by commas
      String[] tempArray = data.get(i).split(",");
      listPlayerNames[i - 1][0] = tempArray[0];
      listPlayerNames[i - 1][0] = tempArray[0];
    }
  }


  public void parseMatrix(String[] fileData){
    // Populate matrix variable
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

  public String[][] getListPlayerNames(){
    return listPlayerNames;
  }

  public wgMatrix getMatrix(){
    return matrix;
  }

}

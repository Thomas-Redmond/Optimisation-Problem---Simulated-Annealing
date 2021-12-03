public class wgMatrix(){
  // Weighted Graph representing Matrix

  int num;
  int[][] matrix;

  public wgMatrix(int number){
    // Set matrix to numberOfPlayer ^2 size

    int num = number;
    matrix = new int[num][num];
    zeroMatrix();

  }

  public void zeroMatrix(){
    // populate zero matrix
    for(int i = 0; i < num; i++){
      for(int j =0; j < num; j++){
        matrix[i][j] = 0;
        }
      }
    }

    public void setValue(int winner, int loser, int weight){
      // Handles Matrix indexing using Player ID values
      matrix[winner - 1][loser - 1] = weight;
    }

    public int getValue(int winner, int loser){
      return matrix[winner - 1][loser - 1];
    }
}

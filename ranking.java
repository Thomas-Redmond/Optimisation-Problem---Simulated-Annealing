public class ranking{

  int num;
  int[] sequence;

  public ranking(int number, int[] values){
    num = number;
    sequence = new int[num];

    deepCopy(values);
  }

  public void deepCopy(int[] values){
    // Copy values using deep copy approach to avoid pointer a = b problems
    for(int i = 0; i < num; i++){
      sequence[i] = values[i];
    }
  }

  public void swapValues(int choice){
    // 0 <= choice <= num - 1 as choice refers to the comma seperating two values
    int a = sequence[choice];
    int b = sequence[choice + 1];

    sequence[choice] = b;
    sequence[choice + 1] = a;
  }

  public int[] getSequence(){
    return sequence;
  }

  public int getByPointer(int pointer){
    return sequence[pointer];
  }

  public void printSequence(){
    String output = "";
    for(int i = 0; i < num; i++){
      output += sequence[i] + " ";
    }
    System.out.println("Current Solution: " + output);
  }

}

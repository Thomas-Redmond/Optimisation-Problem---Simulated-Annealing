import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.ArrayList;


class FileReader{

  ArrayList<String> data;

  public FileReader(String filename){
    // Reads from given file

     data = new ArrayList<String>();

    try{
      File myFile = new File(filename);
      Scanner myReader = new Scanner(myFile);

      while(myReader.hasNextLine()){
        data.add(myReader.nextLine());
      }

      catch(FileNotFoundException e){
        System.out.println("File not found " + filename);
      }
    }

  }

}


/*
 * ProtoTest Genie TCG  is Desktop Web Test Generation tool that generates
 * test cases automatically by using path coverage and each choice techniques
 * to generate test cases that follow links and test forms elements. It also
 * executes tests agains desktop websites on FireFox, IE, Opera, and Safari.
 * it uses selenuim standalone, WebDriver, to run generated test cases.
 * Finally, it results several type of reports that can be used as Test Orcale.
 * June 2, 2013.
 * All copy rights resered to ProtoTest LLC.
 * (Prototest.com, 1999 Browdway Denver, CO, USA). 
 */


package com.prototest.TG_WEB.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 * @author Mahmoud Abdelgawad
 * @serial ProtoTest TG Web
 * @version 1.0
 **/

public final class FileUtile {
    
        
public static void AddLinesToFile(String fileName, ArrayList<String> lines) throws IOException{
        try
            {
              File file = new File(fileName);
              // Check if a file exists
              if (! file.exists()){
                  JOptionPane.showMessageDialog(null, "Could not find file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
              }    
                FileWriter fStream = new FileWriter(fileName, true); // Open in append mode.            
                for(String line : lines){
                fStream.append((line + "\n"));
                fStream.flush();                                  
                }
                fStream.close();
              } catch (IOException e){
                   JOptionPane.showMessageDialog(null, e.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
                }
}


public static void removeLinesContainsOfFromFile(String file, String LinesContainsOfToRemove) {
    try {

      File inFile = new File(file);
      
      if (!inFile.isFile()) {
          JOptionPane.showMessageDialog(null, "Could not find file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      }
       
      //Construct the new file that will later be renamed to the original filename. 
      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
      
      BufferedReader br = new BufferedReader(new FileReader(file));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
      
      String line;

      while ((line = br.readLine()) != null) {
        
        if (!line.trim().contains(LinesContainsOfToRemove)) {
          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();
      
      //Delete the original file
      if (!inFile.delete()) {
        JOptionPane.showMessageDialog(null, "Could not delete file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      }     
      //Rename the new file to the filename the original file had.
      if (!tempFile.renameTo(inFile))
        JOptionPane.showMessageDialog(null, "Could not rename file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
    catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
  }

    
public static void removeLinesEndWithFromFile(String file, String linesEndWithToRemove) {
    try {

      File inFile = new File(file);
      
      if (!inFile.isFile()) {
          JOptionPane.showMessageDialog(null, "Could not find file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      }
       
      //Construct the new file that will later be renamed to the original filename. 
      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
      
      BufferedReader br = new BufferedReader(new FileReader(file));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
      
      String line;

      //Read from the original file and write to the new 
      //unless content matches data to be removed.
      while ((line = br.readLine()) != null) {
        
        if (!line.trim().endsWith(linesEndWithToRemove)) {
          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();
      
      //Delete the original file
      if (!inFile.delete()) {
        JOptionPane.showMessageDialog(null, "Could not delete file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      } 
      
      //Rename the new file to the filename the original file had.
      if (!tempFile.renameTo(inFile))
        JOptionPane.showMessageDialog(null, "Could not rename file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
      
    }
    catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
  }
  
    
public static void removeLinesStartWithFromFile(String file, String linesStartWithToRemove) {

    try {

      File inFile = new File(file);

      if (!inFile.isFile()) {
          JOptionPane.showMessageDialog(null, "Could not find file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      }

      //Construct the new file that will later be renamed to the original filename. 
      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

      BufferedReader br = new BufferedReader(new FileReader(file));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line;

      //Read from the original file and write to the new 
      //unless content matches data to be removed.
      while ((line = br.readLine()) != null) {

        if (!line.trim().startsWith(linesStartWithToRemove)) {
          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();

      //Delete the original file
      if (!inFile.delete()) {
        JOptionPane.showMessageDialog(null, "Could not delete file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
        return;
      } 

      //Rename the new file to the filename the original file had.
      if (!tempFile.renameTo(inFile))
        JOptionPane.showMessageDialog(null, "Could not rename file", "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);

    }
    catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
    }
}
  
    
public static boolean searchFile(String filename) {
     File file = new File(filename);
      // Check if a file exists
      if (file.exists()){
          return true;
      }
      else{
           return false;
      }
}
    
    
public static void writeToFile(String fileName, ArrayList<String> data) throws IOException{
    try
        {
          File file = new File(fileName);
          if (! file.exists()){
              if (!file.createNewFile()){
              }
          }    
          PrintWriter pw = new PrintWriter(new FileWriter(file));
          // Iteration the array into a file             
          for (String line : data) {
            pw.println(line);
            pw.flush();              
            }
            pw.close();

        } catch (IOException e){
               JOptionPane.showMessageDialog(null, e.getMessage(), "Prototest- File Problem", JOptionPane.ERROR_MESSAGE);
            }
}
    
 
    
public static ArrayList<String> readFromFile(String fileName) throws IOException{

    File file = new File(fileName);

    try {
            ArrayList<String> contents = (ArrayList<String>) FileUtils.readLines(file);
            return contents;
        } catch (IOException e) {
            return null;
        }

}


     
public static boolean createFolder(String Dir) {
    boolean dirCreated;
        try {
            dirCreated =  (new File(Dir).mkdirs());
        } catch (Exception e) {
            return false;
        }
    return dirCreated;
}

    
     
public static boolean deleteFolder(String Dir) {
       boolean dirDeleted;
       File dir = new File(Dir); 
        try {
            FileUtils.forceDelete(dir);
            dirDeleted = true;
        } catch (Exception e) {
            return false;
        }
    return dirDeleted;
}

public static boolean copyFile(String sourceFile, String destDirectory) throws IOException {
    File srcFile = new File(sourceFile);
    File destDir = new File(destDirectory);
    if (!srcFile.exists()) {
            return false;
    }
    try {
        FileUtils.copyFileToDirectory(srcFile, destDir);
    } catch(Exception e) {
        return false;
    }

   return true;        
}
    
}
    


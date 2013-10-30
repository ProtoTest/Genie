/*
 * ProtoTest TG 1.0 is Test Generation tool that generates test cases 
 * automatically by using Input-Space Partitioning techniques and executes
 * the result tests against target desktop websites, which lunche on FireFox,
 * IE, Opera, and Safari. We use selenuim standalone, WebDriver, and
 * PageObject Pattern, which is adopted by selenium.   
 * Copyright (c) 2013, ProtoTest LLC.
 * (Prototest.com, 1999 Browdway Denver, CO, USA). 
 */

package com.prototest.TG_WEB.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Mahmoud Abdelgawad
 * @serial ProtoTest TG Web
 * @version 1.0
 **/

public final class ScriptUtile {
    
  public  static ArrayList<String> textValues;    
  public  static ArrayList<String> emailValues;    
  public  static ArrayList<String> areaValues;    
  public  static ArrayList<String> passwordValues;    
    

public static String firstSection(String val){    
    return val.substring(0, val.indexOf("@>>>"));    
}    


public static String lastSection(String val){    
    return val.substring((val.indexOf("@>>>") + 4), val.length());    
}   


public static void fillInputValues(ArrayList<String> inputValues){
        textValues = new ArrayList<String>();    
    emailValues = new ArrayList<String>();    
    areaValues = new ArrayList<String>();    
    passwordValues = new ArrayList<String>();    

    int start, end;
    for(String inputValue: inputValues){
        
      if(inputValue.contains("<text>")){
        start = inputValue.indexOf("<text>") + 6;
        end = inputValue.indexOf("</text>");
        textValues.add(inputValue.substring(start, end));
      }  

      if(inputValue.contains("<email>")){
        start = inputValue.indexOf("<email>") + 7;
        end = inputValue.indexOf("</email>");
        emailValues.add(inputValue.substring(start, end));
      }  

      if(inputValue.contains("<password>")){
        start = inputValue.indexOf("<password>") + 10;
        end = inputValue.indexOf("</password>");
        passwordValues.add(inputValue.substring(start, end));
      }  

      if(inputValue.contains("<area>")){
        start = inputValue.indexOf("<area>") + 6;
        end = inputValue.indexOf("</area>");
        areaValues.add(inputValue.substring(start, end));
      }  

      
    }

}


public static int  getRandom (int size){
    int randomSelection; 
     Random generator = new Random();
     randomSelection = generator.nextInt(size);
     
     if (randomSelection < 0 ) randomSelection = 0;
     
   return randomSelection;
}


public static boolean checkItems (List<List<String>> Items){
  boolean return_value = false;
  
  for(List<String> item: Items){
   if (!item.isEmpty()){
      return_value = true;
      break;
   }      
  }
  
 return return_value;   
}


public static void formtemsScriptRadios(String className, String workDir,  String baseUrl, String driver, 
                                  int loadTimeOut, List<List<Object>> FormElements, ArrayList<String> inputValues,
                                  long numberOfTestcases) throws IOException {

    ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    // Get input values
    fillInputValues(inputValues);
    
    //============ Compose Test Cases ================
    // Script the test cases
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
    
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");    
    scriptData.add("} \n");
    
    
    int testCounter = 0;
    int selectionItemsCounter;
    int s1, e1;
    int i = 0;
    
       for(List<Object> form: FormElements){
           
           String subURL = form.get(0).toString();
           List<List<String>> RadioItems = ((List<List<String>>) form.get(1));
           List<String> ButtonItems = ((List<String>) form.get(5));
           

                    List<String> GRadios;
                    selectionItemsCounter = 0;
                    
                    do{ // Radio Groups loop

                      GRadios = RadioItems.get(getRandom(RadioItems.size())); 
                        
                   if(ButtonItems.isEmpty()){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                                             
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;     
                        testCounter ++;
                       
                   } else {
                       
                   for(String button: ButtonItems){
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                                             
                             
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;     
                        testCounter ++;


                   if (testCounter >= numberOfTestcases) break;                        
                   } // buttons loop
                   
                   }

               if (testCounter >= numberOfTestcases) break;                        
             } while (selectionItemsCounter < numberOfTestcases);
           
           
       if (testCounter >= numberOfTestcases) break;                        
       } //   forms loop 

                
    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);

    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);
    
    
}


public static void formtemsScriptLists(String className, String workDir,  String baseUrl, String driver, 
                                  int loadTimeOut, List<List<Object>> FormElements, ArrayList<String> inputValues,
                                  long numberOfTestcases) throws IOException {

    ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    // Get input values
    fillInputValues(inputValues);
    
    //============ Compose Test Cases ================
    // Script the test cases
    
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
    
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");    
    scriptData.add("} \n");    
    
    int testCounter = 0;
    int selectionItemsCounter;
    int s1, e1, s2, e2;
    int i = 0;
    
       for(List<Object> form: FormElements){
           
           String subURL = form.get(0).toString();
           List<List<String>> selectionItems = ((List<List<String>>) form.get(2));
           List<String> ButtonItems = ((List<String>) form.get(5));
               // Count Selection items 

                    List<String> GSelections;
                    selectionItemsCounter = 0;
    
                      do{

                      GSelections = selectionItems.get(getRandom(selectionItems.size())); 

                     if(ButtonItems.isEmpty()){
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                
                                for(String oneSelectionGroup : GSelections){
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 
                                
                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }
                                
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                         
                     } else {
                     for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                
                                for(String oneSelectionGroup : GSelections){
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 
                                
                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }
                                
                                }

                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;

                        // -----------------

                     if (testCounter >= numberOfTestcases) break;                        
                     } // buttons loop
                     
                     }
                     
                     selectionItemsCounter++;

                     if (testCounter >= numberOfTestcases) break; 
                      } while (selectionItemsCounter < numberOfTestcases);
                      

       if (testCounter >= numberOfTestcases) break;                        
       } //   forms loop 
       
    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);
    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);
    
}



public static void formtemsScriptText(String className, String workDir,  String baseUrl, String driver, 
                                  int loadTimeOut, List<List<Object>> FormElements, ArrayList<String> inputValues,
                                  long numberOfTestcases) throws IOException {

    ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    // Get input values
    fillInputValues(inputValues);
    
    //============ Compose Test Cases ================
    // Script the test cases
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
    
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");    
    scriptData.add("} \n");
    
    
    int testCounter = 0;
    int s1, e1;
    int i = 0;
    String value = "";
    
       for(List<Object> form: FormElements){
           
           String subURL = form.get(0).toString();
           List<String> AreaItems = ((List<String>) form.get(3));
           List<String> InputItems = ((List<String>) form.get(4));
           List<String> ButtonItems = ((List<String>) form.get(5));
                   
                       
                       //----------------
                       for(String textValue: textValues){
                       for(String emailValue: emailValues){
                       for(String passwordValue: passwordValues){
                       for(String areaValue: areaValues){
                       //----------------

                       if(ButtonItems.isEmpty()){
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 

                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                }
                                
                                for(String area: AreaItems){
                                    
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                             
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                           
                       } else {
                                              
                       for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 

                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                }
                                
                                for(String area: AreaItems){
                                    
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                             
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }

                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;

                   if (testCounter >= numberOfTestcases) break;                        
                   } // button loop

                   }
                  //----------------
                          if (testCounter >= numberOfTestcases) break;                        
                          } // Text Values loop

                       
                         if (testCounter >= numberOfTestcases) break;
                         } // email Values loop

                       
                        if (testCounter >= numberOfTestcases) break;
                        } // password Values loop

                       
                       if (testCounter >= numberOfTestcases) break;
                       } // area Values loop
                   //----------------


       if (testCounter >= numberOfTestcases) break;                        
       } //   forms loop 

    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);
    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);
    
}



public static void formtemsScriptAll(String className, String workDir,  String baseUrl, String driver, 
                                  int loadTimeOut, List<List<Object>> FormElements, ArrayList<String> inputValues,
                                  long numberOfTestcases) throws IOException {

     ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    // Get input values
    fillInputValues(inputValues);
    
    //============ Compose Test Cases ================
    // Script the test cases
    
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
    
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");    
    scriptData.add("} \n");
    
    
    int testCounter = 0;
    int selectionItemsCounter;
    int s1, e1, s2, e2;
    int i = 0;
    String value = "";
    
       for(List<Object> form: FormElements){
           
           String subURL = form.get(0).toString();
           List<List<String>> RadioItems = ((List<List<String>>) form.get(1));
           List<List<String>> selectionItems = ((List<List<String>>) form.get(2));
           List<String> AreaItems = ((List<String>) form.get(3));
           List<String> InputItems = ((List<String>) form.get(4));
           List<String> ButtonItems = ((List<String>) form.get(5));
           
           if (!checkItems(RadioItems)){
               // Count Selection items 
               if(!checkItems(selectionItems)){
                   
                       
                       //----------------
                       for(String textValue: textValues){
                       for(String emailValue: emailValues){
                       for(String passwordValue: passwordValues){
                       for(String areaValue: areaValues){
                       //----------------
                     
                    if(ButtonItems.isEmpty()){
                        
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 

                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                        
                    } else {      
                    for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 

                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                                             
                             
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                        

                     if (testCounter >= numberOfTestcases) break;                        
                   } // button loop
                    
                   }
                    //----------------
                          if (testCounter >= numberOfTestcases) break;                        
                          } // Text Values loop
                       
                       
                         if (testCounter >= numberOfTestcases) break;
                         } // email Values loop


                        if (testCounter >= numberOfTestcases) break;
                        } // password Values loop


                       if (testCounter >= numberOfTestcases) break;
                       } // area Values loop
                   //----------------


                   
               }else {

                    List<String> GSelections;
                    selectionItemsCounter = 0;
    
                      do{

                      GSelections = selectionItems.get(getRandom(selectionItems.size())); 
                          
                         
                       //----------------
                       for(String textValue: textValues){
                       for(String emailValue: emailValues){
                       for(String passwordValue: passwordValues){
                       for(String areaValue: areaValues){

                       //----------------
                     if (ButtonItems.isEmpty()){
                         
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                
                                for(String oneSelectionGroup : GSelections){

                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 

                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }                                
                                }
                                
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                         
                     } else {   
                     for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                
                                for(String oneSelectionGroup : GSelections){

                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 

                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }                                
                                }
                             
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }

                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        testCounter ++;
                        
                      if (testCounter >= numberOfTestcases) break;                        
                      } // buttons loop
                     }
                   //----------------
                          if (testCounter >= numberOfTestcases) break;                        
                          } // Text Values loop
                       
                       
                         if (testCounter >= numberOfTestcases) break;
                         } // email Values loop


                        if (testCounter >= numberOfTestcases) break;
                        } // password Values loop


                       if (testCounter >= numberOfTestcases) break;
                       } // area Values loop
                   //----------------


                     selectionItemsCounter++;

                     if (testCounter >= numberOfTestcases) break; 
                     } while (selectionItemsCounter < numberOfTestcases);

               }
               
           } else{ // 

                    List<String> GRadios;
                    selectionItemsCounter = 0;
                    
                    do{ // Radio Groups loop

                      GRadios = RadioItems.get(getRandom(RadioItems.size())); 
                        
               // Count Selection items                       
               if(!checkItems(selectionItems)){
                                                                 
                       //----------------
                       for(String textValue: textValues){
                       for(String emailValue: emailValues){
                       for(String passwordValue: passwordValues){
                       for(String areaValue: areaValues){
                       //----------------
                   if(ButtonItems.isEmpty()){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                                                                         
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;     
                        testCounter ++;
                       
                   } else {
                            
                   for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                                             
                             
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }

                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;     
                        testCounter ++;

                    if (testCounter >= numberOfTestcases) break;                        
                   } // buttons loop
                   }
                   
                   //----------------
                          if (testCounter >= numberOfTestcases) break;                        
                          } // Text Values loop
                       
                       
                         if (testCounter >= numberOfTestcases) break;
                         } // email Values loop


                        if (testCounter >= numberOfTestcases) break;
                        } // password Values loop


                       if (testCounter >= numberOfTestcases) break;
                       } // area Values loop
                   //----------------


                   
               }else {

                    List<String> GSelections;
                    selectionItemsCounter = 0;
    
                      do{

                      GSelections = selectionItems.get(getRandom(selectionItems.size())); 
                     
                       //----------------
                       for(String textValue: textValues){
                       for(String emailValue: emailValues){
                       for(String passwordValue: passwordValues){
                       for(String areaValue: areaValues){
                       //----------------                         
                     if (ButtonItems.isEmpty()){
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                
                                for(String oneSelectionGroup : GSelections){
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 

                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }                                
                                }
                                                             

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                
                                scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                                scriptData.add("} \n");
                                i++;   
                                testCounter ++;
                         
                     } else {
                     for(String button: ButtonItems){
                           
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=text>")) value = textValue;
                                if (input.contains("<type=email>")) value = emailValue;
                                if (input.contains("<type=password>")) value = passwordValue;
                                
                                if(input.contains("<id>")){                                    
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = input.indexOf("<name>") + 6;
                                e1 = input.indexOf("</name>");                                
                                scriptData.add("selenium.typeKeys(\"name=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }

                                }
                                
                                for(String area: AreaItems){
                                value = areaValue;
                                if(area.contains("<id>")){                                    
                                s1 = area.indexOf("<id>") + 4;
                                e1 = area.indexOf("</id>");                                    
                                scriptData.add("selenium.type(\"id=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                else{
                                s1 = area.indexOf("<name>") + 6;
                                e1 = area.indexOf("</name>");                                
                                scriptData.add("selenium.type(\"name=" + area.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                }
                                
                                for(String oneSelectionGroup : GSelections){
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>"); 

                                if(oneSelectionGroup.contains("<id>")){                                    
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");   
                                }
                                else{
                                s1 = oneSelectionGroup.indexOf("<name>") + 6;
                                e1 = oneSelectionGroup.indexOf("</name>");                                
                                scriptData.add("selenium.select(\"name=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                       
                                }                                
                                }
                                                             

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                
                                if(button.contains("<id>")){                                    
                                s1 = button.indexOf("<id>") + 4;
                                e1 = button.indexOf("</id>"); 
                                scriptData.add("selenium.click(\"id=" + button.substring(s1, e1) +"\");");
                                } else
                                {
                                s1 = button.indexOf("<name>") + 6;
                                e1 = button.indexOf("</name>"); 
                                scriptData.add("selenium.click(\"name=" + button.substring(s1, e1) +"\");");                                    
                                }

                                scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                                scriptData.add("} \n");
                                i++;   
                                testCounter ++;

                     if (testCounter >= numberOfTestcases) break;                        
                     } // buttons loop   
                     
                     }
                   //----------------
                          if (testCounter >= numberOfTestcases) break;                        
                          } // Text Values loop
                       
                       
                         if (testCounter >= numberOfTestcases) break;
                         } // email Values loop


                        if (testCounter >= numberOfTestcases) break;
                        } // password Values loop


                       if (testCounter >= numberOfTestcases) break;
                       } // area Values loop
                   //----------------

                     
                       selectionItemsCounter++;

                      if (testCounter >= numberOfTestcases) break;                        
                      } while (selectionItemsCounter < numberOfTestcases);
                   
                   }
               

               
               selectionItemsCounter++;

               if (testCounter >= numberOfTestcases) break;                        
               }while (selectionItemsCounter < numberOfTestcases);
               
           }
           
           
       if (testCounter >= numberOfTestcases) break;                        
       } //   forms loop 

    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);

    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);

}



public static void invalidLinksReport(String className, String workDir,  String baseUrl, ArrayList<String> linksStatus) throws IOException {
    String linkStatusFile = workDir + "/"+ className + "LinkStatus.txt";
    ArrayList<String> ReportLinksStatusData = new ArrayList<String>();
    Date date = new Date();
    
    ReportLinksStatusData.add("=====================================================================================================================\n");
    ReportLinksStatusData.add(" #  ProtoTest: Genie (TCG)   ( Invalid links Report): " + baseUrl + "    Date: " +  date);
    ReportLinksStatusData.add("=====================================================================================================================\n");    
    ReportLinksStatusData.add("    Link (URL)                     HTTP Status                    Status Message         ");
    ReportLinksStatusData.add("---------------------------------------------------------------------------------------------------------------------\n");
    if (linksStatus.isEmpty())
    {
     ReportLinksStatusData.add(" All Links are valid ... Thanks");   
    } else{
    for (int i = 0 ; i < linksStatus.size() ; i ++){
     ReportLinksStatusData.add(" " + (i+1) + ". " + linksStatus.get(i));   
    }        
    }   
        
    ReportLinksStatusData.add("----------------------------------------------------  end -----------------------------------------------------------\n");

    FileUtile.writeToFile(linkStatusFile, ReportLinksStatusData);
    
    
}   


public static void testCheckUrlsListReport(String fileNmae, ArrayList<String> checkList, String baseUrl) throws IOException {
    ArrayList<String> checkListData = new ArrayList<String>();
    Date date = new Date();
    
    checkListData.add("=====================================================================================================================\n");
    checkListData.add(" #  ProtoTest: Genie (TCG)  ( Urls Scenaios list): " + baseUrl + "    Date: " +  date);
    checkListData.add("=====================================================================================================================\n");    
    checkListData.add("    Scenario#                       Scenario Path                                               ");
    checkListData.add("---------------------------------------------------------------------------------------------------------------------\n");
    if (checkList.isEmpty())
    {
     checkListData.add(" No Scenario Paths found ... Thanks");   
    } else{
    for (int i = 0 ; i < checkList.size() ; i ++){
     checkListData.add(" *   " + checkList.get(i));   
    checkListData.add("....................................................................................................................\n");
     
    }        
    }   
        
    checkListData.add("-----------------------------------------------------  end ---------------------------------------------------------\n");

    FileUtile.writeToFile(fileNmae, checkListData);
    
    
}   


public static void testCheckListReport(String fileNmae, ArrayList<String> checkList, String baseUrl) throws IOException {
    ArrayList<String> checkListData = new ArrayList<String>();
    Date date = new Date();
    
    checkListData.add("===============================================================================================================\n");
    checkListData.add(" #  ProtoTest: Genie (TCG) ( Test cases check list): " + baseUrl + "    Date: " +  date);
    checkListData.add("===============================================================================================================\n");    
    checkListData.add("    Test#                     Test Path                                                         ");
    checkListData.add("---------------------------------------------------------------------------------------------------------------\n");
    if (checkList.isEmpty())
    {
     checkListData.add(" No Test Paths found ... Thanks");   
    } else{
    for (int i = 0 ; i < checkList.size() ; i ++){
     checkListData.add(" *   " + checkList.get(i));   
    }        
    }   
        
    checkListData.add("---------------------------------------------------  end ------------------------------------------------------\n");

    FileUtile.writeToFile(fileNmae, checkListData);
    
    
}   


public static void singleScenarioScript(String className, String workDir, String baseUrl, String driver, int loadTimeOut, ArrayList<String> testScenario) throws IOException {
    ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    ArrayList<String> scriptCheckList = new ArrayList<String>();    
    String scriptFileCheckList = workDir + "/"+ className + "TestCheckList.txt";

    ArrayList<String> scripturlCheckList = new ArrayList<String>();    
    String scriptFileurlCheckList = workDir + "/"+ className + "TestUrlCheckList.txt";
    
    //============ Compose Test Cases ================
    // Script the test cases
    
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
    
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");    
    scriptData.add("} \n");
    
    scriptData.add("@Test");	
    scriptData.add("public void test" + className + "() throws Exception {");	
    scriptData.add("selenium.open(\"/\");");
    

    // here we script the test scenario
    String testPath = baseUrl;
    String urlsScenaio = baseUrl;
    
    String link, path , urlPath;

    for(int i=1; i < testScenario.size(); i++){
        
        path = firstSection((testScenario.get(i).toString()).replaceAll("\"", "")).trim();
        urlPath = lastSection((testScenario.get(i).toString())).trim();
        link = "link=" + path;		 
        scriptData.add("selenium.click(\"" + link + "\");");
        
        testPath = testPath + "-> " +  path;        
        urlsScenaio = urlsScenaio + " || " + urlPath; 
        
        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
        
    }
    
    scriptCheckList.add(" * Specific Scenario : " + testPath);
    scripturlCheckList.add(" * Specific Urls Scenario : " + urlsScenaio);

    
    scriptData.add("} \n");
    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);

    // Report the Tests Checklist
    testCheckListReport(scriptFileCheckList, scriptCheckList, baseUrl);

    // Report the Url Checklist
    testCheckUrlsListReport(scriptFileurlCheckList, scripturlCheckList, baseUrl);
    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);

}



public static int multiScenarioScript(String className, String workDir, String baseUrl, String driver, int loadTimeOut, Object[] allTreePaths) throws IOException {
    ArrayList<String> scriptData = new ArrayList<String>();
    String scriptFile = workDir + "/"+ className + ".java";
    
    ArrayList<String> scriptCheckList = new ArrayList<String>();    
    String scriptFileCheckList = workDir + "/"+ className + "TestCheckList.txt";

    ArrayList<String> scripturlCheckList = new ArrayList<String>();    
    String scriptFileurlCheckList = workDir + "/"+ className + "TestUrlCheckList.txt";

    int numScenaios = 0;
    
    //============ Compose Test Cases ================
    // Script the test cases
    
    scriptData.add("import com.thoughtworks.selenium.Selenium;");
 
   if(driver.equals("Firefox")) scriptData.add("import org.openqa.selenium.firefox.FirefoxDriver;");
   if(driver.equals("IE")) scriptData.add("import org.openqa.selenium.ie.InternetExplorerDriver;");
   if(driver.equals("Chrome"))scriptData.add("import org.openqa.selenium.chrome.ChromeDriver;");
   if(driver.equals("Safari"))scriptData.add("import org.openqa.selenium.safari.SafariDriver;");
       
    scriptData.add("import org.openqa.selenium.WebDriver;");
    scriptData.add("import org.openqa.selenium.WebDriverBackedSelenium;");
    scriptData.add("import org.junit.After;");
    scriptData.add("import org.junit.Before;");
    scriptData.add("import org.junit.Test;");
    scriptData.add("import static org.junit.Assert.*;");
    scriptData.add("import java.util.regex.Pattern;");
    scriptData.add("import static org.apache.commons.lang3.StringUtils.join; \n");
    
    scriptData.add("public class " + className + " {");
    scriptData.add("private Selenium selenium; \n");
    scriptData.add("@Before");
    scriptData.add("public void setUp() throws Exception {");
    if(driver.equals("Firefox")) scriptData.add("WebDriver driver = new FirefoxDriver();");
    if(driver.equals("IE"))      scriptData.add("WebDriver driver = new InternetExplorerDriver();");
    if(driver.equals("Chrome"))  scriptData.add("WebDriver driver = new ChromeDriver();");
    if(driver.equals("Safari"))  scriptData.add("WebDriver driver = new SafariDriver();");    
    scriptData.add("String baseUrl =\"" + baseUrl + "\";");    
    scriptData.add("selenium = new WebDriverBackedSelenium(driver, baseUrl);");
    scriptData.add("} \n");
    
    String testScenaio, urlsScenaio;
    for (int i=0; i < allTreePaths.length; i++){                                
    numScenaios ++;
    scriptData.add("@Test");	
    scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
    scriptData.add("selenium.open(\"/\");");
    
    testScenaio = baseUrl;
    urlsScenaio = baseUrl;
    ArrayList<String> testScenario = (ArrayList<String>) allTreePaths[i];
    
    String link, path, urlPath;

    for(int j=1; j < testScenario.size(); j++){
                
        path = firstSection((testScenario.get(j).toString()).replaceAll("\"", "\\\\\"")).trim();
        urlPath = lastSection((testScenario.get(j).toString())).trim();
        link = "link=" + path;		 
        scriptData.add("selenium.click(\"" + link + "\");"); 
        testScenaio = testScenaio + "-> " +  path;
        urlsScenaio = urlsScenaio + " || " + urlPath; 
        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
    }
    
    scriptData.add("} \n");  
    scriptCheckList.add(String.valueOf(i + 1) + "     " + testScenaio);
    scripturlCheckList.add(String.valueOf(i + 1) + "     " + urlsScenaio);
    
    }
    
    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);
    
    // Report the Tests Checklist
    testCheckListReport(scriptFileCheckList, scriptCheckList, baseUrl);

    // Report the Url Checklist
    testCheckUrlsListReport(scriptFileurlCheckList, scripturlCheckList, baseUrl);
    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "\\"+ className + ".java";
    String compileSellFile    = workDir + "\\"+ className + "Compile.bat";
    String command = "javac -sourcepath src -classpath \"" + workDir +
                            "\\j.jar\";\""  + workDir +
                            "\\h.jar\";\""  + workDir +
                            "\\s.jar\" "  + "\"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "\\"+ className + "Run.bat";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -classpath j.jar;s.jar;h.jar;. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);

    return numScenaios;
}

        
}
    

    
    


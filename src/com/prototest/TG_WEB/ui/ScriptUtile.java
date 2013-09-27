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

public static String firstSection(String val){    
    return val.substring(0, val.indexOf("@>>>"));    
}    


public static String lastSection(String val){    
    return val.substring((val.indexOf("@>>>") + 4), val.length());    
}   



public static void formtemsScript(String className, String workDir,  String baseUrl, String driver, 
                                  int loadTimeOut, List<List<Object>> FormElements, ArrayList<String> inputValues,
                                  int numberOfTestcases, int certerion) throws IOException {

    ArrayList<String> scriptData = new ArrayList<String>();    
    String scriptFile = workDir + "/"+ className + ".java";

    
    //============ Compose Test Cases ================
    // Script the test cases
    
    scriptData.add("package com.prototest.TG_WEB.ui;");
    
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
           if (RadioItems.isEmpty()){
               // Count Selection items                       
               if(selectionItems.isEmpty()){
                   
                   for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=email>")) value = "protoTest@prototest.com";
                                if (input.contains("<type=password>")) value = "protoTest123";
                                if (input.contains("<type=text>")) value = "ProtoTest is Testing";
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String area: AreaItems){
                                value = "ProtoTest is Testing Area Item";
                                scriptData.add("selenium.type(\"id=" + area + "\", \"" + value + "\");");                                 
                                }
                                                             
                             
                        scriptData.add("selenium.click(\"id=" + button +"\");");
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;     
                   }
                   
               }else {

                    Random generator = new Random();
                    List<String> GSelections;
                    int randomSelection; 
                    selectionItemsCounter = 0;
    
                      do{

                      randomSelection = generator.nextInt(selectionItems.size());
                      GSelections = selectionItems.get(randomSelection); 
                          

                     for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=email>")) value = "protoTest@prototest.com";
                                if (input.contains("<type=password>")) value = "protoTest123";
                                if (input.contains("<type=text>")) value = "ProtoTest is Testing";
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String area: AreaItems){
                                value = "ProtoTest is Testing Area Item";
                                scriptData.add("selenium.type(\"id=" + area + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String oneSelectionGroup : GSelections){
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                    
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>");                                    
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                 
                                    
                                }
                             
                        scriptData.add("selenium.click(\"id=" + button +"\");");
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        i++;   
                        selectionItemsCounter++;
                        
                      }   
                   
                      } while (selectionItemsCounter < numberOfTestcases);

                   
               }
               
           } else{ // 
                    Random generatorRadios = new Random();
                    List<String> GRadios;
                    int randomRadios; 
                    selectionItemsCounter = 0;
                    
                    do{ // Radio Groups loop

                      randomRadios = generatorRadios.nextInt(RadioItems.size());
                      GRadios = RadioItems.get(randomRadios); 
                        
               // Count Selection items                       
               if(selectionItems.isEmpty()){
                   
                   for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");
                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=email>")) value = "protoTest@prototest.com";
                                if (input.contains("<type=password>")) value = "protoTest123";
                                if (input.contains("<type=text>")) value = "ProtoTest is Testing";
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String area: AreaItems){
                                value = "ProtoTest is Testing Area Item";
                                scriptData.add("selenium.type(\"id=" + area + "\", \"" + value + "\");");                                 
                                }

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                                             
                             
                        scriptData.add("selenium.click(\"id=" + button +"\");");
                        scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                        scriptData.add("} \n");
                        
                        i++;     
                        selectionItemsCounter++;
                   }
                   
               }else {

                    Random generatorSelections = new Random();
                    List<String> GSelections;
                    int randomSelection; 
                    selectionItemsCounter = 0;
    
                      do{ // Selection Groups loop

                      randomSelection = generatorSelections.nextInt(selectionItems.size());
                      GSelections = selectionItems.get(randomSelection); 
                          

                     for(String button: ButtonItems){
                       
                        scriptData.add("@Test");	
                        scriptData.add("public void Test_" + String.valueOf(i + 1) + "_" + className + "() throws Exception {");	
                        scriptData.add("selenium.open(\"" + subURL + "\");");

                                                     
                                for(String input: InputItems){                                 
                                if (input.contains("<type=email>")) value = "protoTest@prototest.com";
                                if (input.contains("<type=password>")) value = "protoTest123";
                                if (input.contains("<type=text>")) value = "ProtoTest is Testing";
                                s1 = input.indexOf("<id>") + 4;
                                e1 = input.indexOf("</id>");                                    
                                scriptData.add("selenium.typeKeys(\"id=" + input.substring(s1, e1) + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String area: AreaItems){
                                value = "ProtoTest is Testing Area Item";
                                scriptData.add("selenium.type(\"id=" + area + "\", \"" + value + "\");");                                 
                                }
                                
                                for(String oneSelectionGroup : GSelections){
                                s1 = oneSelectionGroup.indexOf("<id>") + 4;
                                e1 = oneSelectionGroup.indexOf("</id>");                                    
                                s2 = oneSelectionGroup.indexOf("<label>") + 7;
                                e2 = oneSelectionGroup.indexOf("</label>");                                    
                                scriptData.add("selenium.select(\"id=" + oneSelectionGroup.substring(s1, e1) +
                                               "\", \"label=" + oneSelectionGroup.substring(s2, e2) + "\");");                                 
                                    
                                }
                                                             

                                for(String oneRadiosGroup: GRadios){
                                scriptData.add("selenium.type(\"id=" + oneRadiosGroup + "\");");                                 
                                }
                                
                                scriptData.add("selenium.click(\"id=" + button +"\");");
                                scriptData.add("selenium.waitForPageToLoad(\""+ String.valueOf(loadTimeOut) +"\");");
                                scriptData.add("} \n");

                        i++;   
                        selectionItemsCounter++;
                        
                      }   
                   
                      } while (selectionItemsCounter < numberOfTestcases);

                   
               }
                        

               }while (selectionItemsCounter < numberOfTestcases);
               
               
           }
           
       }
       
    scriptData.add("@After");
    scriptData.add("public void tearDown() throws Exception {");
    scriptData.add("selenium.stop();");
    scriptData.add("} \n");
    scriptData.add("}");
    
    // Save test cases script 
    FileUtile.writeToFile(scriptFile, scriptData);

    
    // Create shell file for compiling test cases
    String scriptSellFile = workDir + "/"+ className + ".java";
    String compileSellFile    = workDir + "/"+ className + "Compile.sh";
    String command = "javac -cp \"" + workDir +
                            "/j.jar\":\""  + workDir +
                            "/h.jar\":\""  + workDir +
                            "/s.jar\":\""  + workDir + "/\" \"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "/"+ className + "Run.sh";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -cp j.jar:s.jar:h.jar:. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);
    
    
}



public static void invalidLinksReport(String className, String workDir,  String baseUrl, ArrayList<String> linksStatus) throws IOException {
    String linkStatusFile = workDir + "/"+ className + "LinkStatus.txt";
    ArrayList<String> ReportLinksStatusData = new ArrayList<String>();
    Date date = new Date();
    
    ReportLinksStatusData.add("==============================================================================================\n");
    ReportLinksStatusData.add(" #  ProtoTest: Genie (TCG)   ( Invalid links Report): " + baseUrl + "    Date: " +  date);
    ReportLinksStatusData.add("==============================================================================================\n");    
    ReportLinksStatusData.add("    Link (URL)                     HTTP Status                    Status Message         ");
    ReportLinksStatusData.add("----------------------------------------------------------------------------------------------\n");
    if (linksStatus.isEmpty())
    {
     ReportLinksStatusData.add(" All Links are valid ... Thanks");   
    } else{
    for (int i = 0 ; i < linksStatus.size() ; i ++){
     ReportLinksStatusData.add(" " + (i+1) + ". " + linksStatus.get(i));   
    }        
    }   
        
    ReportLinksStatusData.add("--------------------------------------------  end ---------------------------------------------\n");

    FileUtile.writeToFile(linkStatusFile, ReportLinksStatusData);
    
    
}   


public static void testCheckUrlsListReport(String fileNmae, ArrayList<String> checkList, String baseUrl) throws IOException {
    ArrayList<String> checkListData = new ArrayList<String>();
    Date date = new Date();
    
    checkListData.add("==============================================================================================\n");
    checkListData.add(" #  ProtoTest: Genie (TCG)  ( Urls Scenaios list): " + baseUrl + "    Date: " +  date);
    checkListData.add("==============================================================================================\n");    
    checkListData.add("    Scenario#                       Scenario Path                                               ");
    checkListData.add("----------------------------------------------------------------------------------------------\n");
    if (checkList.isEmpty())
    {
     checkListData.add(" No Scenario Paths found ... Thanks");   
    } else{
    for (int i = 0 ; i < checkList.size() ; i ++){
     checkListData.add(" *   " + checkList.get(i));   
    checkListData.add("..............................................................................................\n");
     
    }        
    }   
        
    checkListData.add("--------------------------------------------  end ---------------------------------------------\n");

    FileUtile.writeToFile(fileNmae, checkListData);
    
    
}   


public static void testCheckListReport(String fileNmae, ArrayList<String> checkList, String baseUrl) throws IOException {
    ArrayList<String> checkListData = new ArrayList<String>();
    Date date = new Date();
    
    checkListData.add("==============================================================================================\n");
    checkListData.add(" #  ProtoTest: Genie (TCG) ( Test cases check list): " + baseUrl + "    Date: " +  date);
    checkListData.add("==============================================================================================\n");    
    checkListData.add("    Test#                     Test Path                                                         ");
    checkListData.add("----------------------------------------------------------------------------------------------\n");
    if (checkList.isEmpty())
    {
     checkListData.add(" No Test Paths found ... Thanks");   
    } else{
    for (int i = 0 ; i < checkList.size() ; i ++){
     checkListData.add(" *   " + checkList.get(i));   
    }        
    }   
        
    checkListData.add("--------------------------------------------  end ---------------------------------------------\n");

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
    
    scriptData.add("package com.prototest.TG_WEB.ui;");
    
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
    
    scriptCheckList.add(" * Specified Scenario : " + testPath);
    scripturlCheckList.add(" * Specified Urls Scenario : " + urlsScenaio);

    
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
    String scriptSellFile = workDir + "/"+ className + ".java";
    String compileSellFile    = workDir + "/"+ className + "Compile.sh";
    String command = "javac -cp \"" + workDir +
                            "/j.jar\":\""  + workDir +
                            "/h.jar\":\""  + workDir +
                            "/s.jar\":\""  + workDir + "/\" \"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "/"+ className + "Run.sh";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -cp j.jar:s.jar:h.jar:. org.junit.runner.JUnitCore " + className;
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
    scriptData.add("package com.prototest.TG_WEB.ui;");
    
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
    String scriptSellFile = workDir + "/"+ className + ".java";
    String compileSellFile    = workDir + "/"+ className + "Compile.sh";
    String command = "javac -cp \"" + workDir +
                            "/j.jar\":\""  + workDir +
                            "/h.jar\":\""  + workDir +
                            "/s.jar\":\""  + workDir + "/\" \"" + scriptSellFile +"\"" ;
    ArrayList<String> scriptSellCompileData = new ArrayList<String>();
    scriptSellCompileData.add(command);
    FileUtile.writeToFile(compileSellFile, scriptSellCompileData);
    
    // Create shell file for running test cases
    ArrayList<String> scriptSellRunData = new ArrayList<String>();    
    String runSellFile    = workDir + "/"+ className + "Run.sh";
    command = "cd \"" + workDir + "\"";
    scriptSellRunData.add(command);
    command = "java -cp j.jar:s.jar:h.jar:. org.junit.runner.JUnitCore " + className;
    scriptSellRunData.add(command);
    FileUtile.writeToFile(runSellFile, scriptSellRunData);
    return numScenaios;
}

        
}
    

    
    


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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import org.junit.runner.JUnitCore; 
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public final class TestRunner { 

public static Result runTest(String website, String testClassDir, String ClassName, int criterion) throws IOException { 
        
        File file = new File(testClassDir);
        URL url;
        try {
            url = file.toURI().toURL();
            URL[] urls = {url};
            ClassLoader clazz = new URLClassLoader(urls);
            Class claz = clazz.loadClass(ClassName);
            
            Result result = JUnitCore.runClasses(claz);
            
            ArrayList<String> report = new ArrayList<String>();
            Date date = new Date();
            report.add("\n  ProtoTest T.G for : " + website + "             Date: "+ date + "\n");
            
            report.add("--------------------------  Summary ---------------------------------------------------");
            report.add(" # Test Cases: " + String.valueOf(result.getRunCount()));
            report.add(" # Pass: " + String.valueOf(result.getRunCount() - result.getFailureCount()));
            report.add(" # Fail: " + String.valueOf( result.getFailureCount()));
            report.add("---------------------------------------------------------------------------------------\n");
            if (result.getFailureCount() > 0){
            report.add("----------------------------------  Failures ------------------------------------------\n");
                    int i = 1;
                    for (Failure failure : result.getFailures()) { 
                         report.add(" * (" + (i++) + ") Test Case [" + failure.getTestHeader() + "]");
                         report.add("      Failure [" + failure.getMessage()+ "]\n");                         
                     }
            report.add("-----------------------------------  end ----------------------------------------------\n");
                    
            }

          String reportFile;  
          if (criterion == 0)  
          reportFile = testClassDir + ClassName + "TestLinksResults.txt";
          else
          reportFile = testClassDir + ClassName + "TestFormsResults.txt";
              
          FileUtile.writeToFile(reportFile,report);
          return result;
        } catch (MalformedURLException ex) {
              JOptionPane.showMessageDialog(null,ex.getMessage(),
                "PrototestTC- Fail opening", JOptionPane.ERROR_MESSAGE);
              return null;
        } catch (ClassNotFoundException ex) {
              JOptionPane.showMessageDialog(null,ex.getMessage(),
                "PrototestTC- Fail opening", JOptionPane.ERROR_MESSAGE);
              return null;
              
        }
}
}



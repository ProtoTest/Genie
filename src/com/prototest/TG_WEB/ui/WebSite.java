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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author Mahmoud Abdelgawad
 * @serial ProtoTest TG Web
 * @version 1.0
 **/


public class WebSite {
    
    private Integer WebID;
    private String URL;
    private String HTTP_HTTPS;
    private Date RunDate;
    private Integer nodes;
    private Integer links;
    private Integer imports;
    private Integer media;
    private Integer forms;
    private Integer Scenarios;
    private Integer pass;
    private Integer fail;

    public WebSite(Integer webID) {
        this.WebID = webID;
        this.URL = "";
        this.HTTP_HTTPS = "HTTP";
        Date date = new Date();
        this.RunDate = date;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;

    }
    
    public String getAddress(){
        return (this.HTTP_HTTPS.toLowerCase() + "://" + this.URL);
    }
    
    public void AssignWebsite(WebSite Web) {
        this.WebID = Web.WebID;
        this.URL = Web.URL;
        this.HTTP_HTTPS = Web.HTTP_HTTPS;
        this.RunDate = Web.RunDate;
        this.nodes = Web.nodes;
        this.links = Web.links;
        this.imports = Web.imports;
        this.media = Web.media;
        this.forms = Web.forms;
        this.Scenarios = Web.Scenarios;
        this.pass = Web.pass;
        this.fail = Web.fail;
    }
      
    public WebSite(WebSite Web) {
        this.WebID = Web.WebID;
        this.URL = Web.URL;
        this.HTTP_HTTPS = Web.HTTP_HTTPS;
        this.RunDate = Web.RunDate;
        this.nodes = Web.nodes;
        this.links = Web.links;
        this.imports = Web.imports;
        this.media = Web.media;
        this.forms = Web.forms;
        this.Scenarios = Web.Scenarios;
        this.pass = Web.pass;
        this.fail = Web.fail;
    }
    
    public WebSite(ArrayList<String> Web) {
        this.WebID = -1;        
        this.URL = "";
        this.HTTP_HTTPS = "HTTP";
        Date date = new Date();
        this.RunDate = date;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;

        for(String Line : Web){
           // Check string starts with <* and ends with *>
           if (Line.startsWith("<*") && Line.endsWith("*>")){
              
                // Extract URL     
                if (Line.contains("URL")) {
                this.URL  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                
               // Extract the Web WebID from any line. cut after 5 digit which is index word.
                this.WebID =Integer.parseInt(Line.substring((Line.indexOf("index") + 5), (Line.indexOf("row"))));
                }   

                // Extract Web HTTP_HTTPS    
                if (Line.contains("HTTP_HTTPS")) {
                this.HTTP_HTTPS  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   
                                
                // Extract Web RunDate    
                if (Line.contains("RunDate")) {
                //Date date = new Date();
                String dateVal = (Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                    try {
                        this.RunDate  =new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateVal);
                    } catch (ParseException ex) {
                    }
                }   

                // Extract Web nodes    
                if (Line.contains("nodes")) {
                this.nodes  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web links    
                if (Line.contains("links")) {
                this.links  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web imports    
                if (Line.contains("imports")) {
                this.imports  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web media    
                if (Line.contains("media")) {
                this.media  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web forms    
                if (Line.contains("forms")) {
                this.forms  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web Scenarios    
                if (Line.contains("Scenarios")) {
                this.Scenarios  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web pass    
                if (Line.contains("pass")) {
                this.pass  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web fail    
                if (Line.contains("fail")) {
                this.fail  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   
           } // end of if statement that check <*  and *>
        } // end of loop
    }
    
    public boolean HasChange(WebSite Web){
     if (this.WebID.equals(Web.WebID) &&
        this.URL.equals(Web.URL) &&
        this.HTTP_HTTPS.equals(Web.HTTP_HTTPS) &&
        this.nodes.equals(Web.nodes) &&
        this.links.equals(Web.links) &&
        this.imports.equals(Web.imports) &&
        this.media.equals(Web.media) &&
        this.forms.equals(Web.forms) &&
        this.Scenarios.equals(Web.Scenarios) &&
        this.pass.equals(Web.pass) &&
        this.fail.equals(Web.fail)){
          return false;
      } else {
          return true;
      } 
    }
    
    public void setWebData(ArrayList<String> data) {
        this.WebID = -1;
        this.URL = "";
        this.HTTP_HTTPS = "HTTP";
        Date date = new Date();
        this.RunDate = date;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;

        for(String Line : data){
           // Check string starts with <* and ends with *>
           if (Line.startsWith("<*") && Line.endsWith("*>")){
               
               // Extract the Web WebID from any line. cut after 5 digit which is index word.
               this.WebID =Integer.parseInt(Line.substring((Line.indexOf("index") + 5), (Line.indexOf("row"))));                
              
                // Extract Web URL    
                if (Line.contains("URL")) {
                this.URL  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                                // Extract Web HTTP_HTTPS    
                if (Line.contains("HTTP_HTTPS")) {
                this.HTTP_HTTPS  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   
                
                // Extract Web RunDate    
                if (Line.contains("RunDate")) {
                //Date date = new Date();
                String dateVal = (Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                    try {
                        this.RunDate  =new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateVal);
                    } catch (ParseException ex) {
                    }
                }   

                // Extract Web nodes    
                if (Line.contains("nodes")) {
                this.nodes  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web links    
                if (Line.contains("links")) {
                this.links  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web imports    
                if (Line.contains("imports")) {
                this.imports  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web media    
                if (Line.contains("media")) {
                this.media  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web Forms    
                if (Line.contains("forms")) {
                this.forms  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web Scenarios    
                if (Line.contains("Scenarios")) {
                this.Scenarios  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web pass    
                if (Line.contains("pass")) {
                this.pass  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web fail    
                if (Line.contains("fail")) {
                this.fail  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   
           } // end of if statement that check <*  and *>
        } // end of loop
    }
    public void ChangeWebData(ArrayList<String> data) {

        for(String Line : data){
           // Check string starts with <* and ends with *>
           if (Line.startsWith("<*") && Line.endsWith("*>")){
               
                // Extract Web RunDate    
                if (Line.contains("RunDate")) {
                //Date date = new Date();
                String dateVal = (Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                    try {
                        this.RunDate  =new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateVal);
                    } catch (ParseException ex) {
                    }
                }   

                // Extract Web nodes    
                if (Line.contains("nodes")) {
                this.nodes  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web links    
                if (Line.contains("links")) {
                this.links  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web imports    
                if (Line.contains("imports")) {
                this.imports  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web media    
                if (Line.contains("media")) {
                this.media  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web forms    
                if (Line.contains("forms")) {
                this.forms  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web Scenarios    
                if (Line.contains("Scenarios")) {
                this.Scenarios  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web pass    
                if (Line.contains("pass")) {
                this.pass  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   

                // Extract Web fail    
                if (Line.contains("fail")) {
                this.fail  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("*>"))));
                }   
           } // end of if statement that check <*  and *>
        } // end of loop
    }
    
    public ArrayList<String> getWebtData() throws IOException{
        ArrayList newWebSite = new ArrayList();
        newWebSite.add("<* index"+this.WebID.toString()+"row URL=" + this.URL + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row HTTP_HTTPS=" + this.HTTP_HTTPS + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row RunDate=" + this.RunDate + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row nodes=" + this.nodes.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row links=" + this.links.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row imports=" + this.imports.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row media=" + this.media.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row forms=" + this.forms.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row Scenarios=" + this.Scenarios.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row pass=" + this.pass.toString() + "*>");
        newWebSite.add("<* index"+this.WebID.toString()+"row fail=" + this.fail.toString() + "*>");
        return newWebSite;
      }
    
    
// Setter and Getter methods
    
    
    public String getHTTP_HTTPS() {
        return HTTP_HTTPS;
    }

    public void setHTTP_HTTPS(String HTTP_HTTPS) {
        this.HTTP_HTTPS = HTTP_HTTPS;
    }

    
    public Integer getWebID() {
        return WebID;
    }

    public void setWebID(Integer WebID) {
        this.WebID = WebID;
    }
    
    
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public Date getRunDate() {
        return RunDate;
    }

    public void setRunDate(Date RunDate) {
        this.RunDate = RunDate;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public Integer getLinks() {
        return links;
    }

    public void setLinks(Integer links) {
        this.links = links;
    }

    public Integer getImports() {
        return imports;
    }

    public void setImports(Integer imports) {
        this.imports = imports;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getForms() {
        return forms;
    }

    public void setForms(Integer forms) {
        this.forms = forms;
    }

    
    public Integer getScenarios() {
        return Scenarios;
    }

    public void setScenarios(Integer Scenarios) {
        this.Scenarios = Scenarios;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }
    
    
}

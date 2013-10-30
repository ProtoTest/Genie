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

public class Project {
    private String name;
    private String title;
    private Date creationDate;
    private Integer urls;
    private Integer nodes;
    private Integer links;
    private Integer imports;
    private Integer media;
    private Long forms;    
    private Integer Scenarios;
    private Integer pass;
    private Integer fail;
    private String projectPath;
    private ArrayList<WebSite> webSites;

    /**
     *
     * @param name
     * @param title
     * @param creationDate
     * @param urls
     * @param nodes
     * @param links
     * @param imports
     * @param media
     * @param Scenarios
     * @param pass
     * @param fail
     * Constructor to instantiate on Project object. 
     */
    
    public Project() {
        this.name = "";
        this.title = "";
        Date date = new Date();
        this.creationDate = date;
        this.urls = 0;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = (long) 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;
        this.webSites = new ArrayList<WebSite>();
    }
        
    
    public Project(Project P) {
        this.name = P.name;
        this.title = P.title;
        this.creationDate = P.creationDate;
        this.urls = P.urls;
        this.nodes = P.nodes;
        this.links = P.links;
        this.imports = P.imports;
        this.media = P.media;
        this.forms = P.forms;
        this.Scenarios = P.Scenarios;
        this.pass = P.pass;
        this.fail = P.fail;
        this.webSites = new ArrayList<WebSite>();
        this.webSites.addAll(P.webSites);
    }
    
    public void updateProject(){
       int _nodes = 0, _links = 0, _imports = 0, _media = 0,
           _Scenarios = 0, _pass = 0, _fail = 0;
       long _forms = (long) 0;
        for(WebSite web: this.webSites){
            
         _nodes += web.getNodes();
         _links += web.getLinks();
         _imports += web.getImports();
         _media += web.getMedia();
         _forms += web.getForms();
         _Scenarios += web.getScenarios();
         _pass += web.getPass();
         _fail += web.getFail();
                 
        }

        this.nodes = _nodes;
        this.links = _links;
        this.imports = _imports;
        this.media = _media;
        this.forms = _forms;
        this.Scenarios = _Scenarios;
        this.pass = _pass;
        this.fail = _fail;
        
    }

    public void addWebSite(WebSite web){
        this.webSites.add(web);
        this.urls ++;
    }
    
    public void RemoveWebSite(WebSite web){
        this.webSites.remove(web);
        this.urls --;
    }
    
    public Project(ArrayList<String> Proj) {
        
        Integer numberOfURL = 0;
        this.name = "";
        this.title = "";
        Date date = new Date();
        this.creationDate = date;
        this.urls = 0;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = (long) 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;
        this.webSites = new ArrayList<WebSite>();

        for(String Line : Proj){
           // Check string starts with </ and ends with />
           if (Line.startsWith("</") && Line.endsWith("/>")){
              
                // Extract project name    
                if (Line.contains("name")) {
                this.name  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project title    
                if (Line.contains("title")) {
                this.title  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project creationDate    
                if (Line.contains("creationDate")) {
                //Date date = new Date();
                String dateVal = (Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                    try {
                        this.creationDate  =new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateVal);
                    } catch (ParseException ex) {
                    }
                }   

                // Extract project urls    
                if (Line.contains("urls")) {
                this.urls  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project nodes    
                if (Line.contains("nodes")) {
                this.nodes  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project links    
                if (Line.contains("links")) {
                this.links  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project imports    
                if (Line.contains("imports")) {
                this.imports  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project media    
                if (Line.contains("media")) {
                this.media  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project forms    
                if (Line.contains("forms")) {
                this.forms  =Long.parseLong(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project Scenarios    
                if (Line.contains("Scenarios")) {
                this.Scenarios  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project pass    
                if (Line.contains("pass")) {
                this.pass  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project fail    
                if (Line.contains("fail")) {
                this.fail  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   
           } // end of if statement that check </  and />
           
            // Get WebSites IDs
            if ((Line.startsWith("<*") && Line.endsWith("*>")) && (Line.contains("URL"))){
                numberOfURL ++;
            }           
        } // end of project data loop
     
                for(int i=0; i< numberOfURL ; i++){
                ArrayList<String> tmpWeb = new ArrayList<String>();
                // Loop for extract websites data
                for(String Line : Proj){
                if ((Line.startsWith("<*") && Line.endsWith("*>")) && (Line.contains("index"+ (Integer.toString(i)) +"row"))){
                    tmpWeb.add(Line);
                  }            
                } // end of websites data loop
                
                this.webSites.add(i, (new WebSite(tmpWeb)));
             } // end of number of websites loop
    }
    
    public boolean HasChange(Project P){
     if (this.name.equals(P.name) &&
        this.title.equals(P.title) &&
        this.creationDate.equals(P.creationDate) &&
        this.urls.equals(P.urls) &&
        this.nodes.equals(P.nodes) &&
        this.links.equals(P.links) &&
        this.imports.equals(P.imports) &&
        this.media.equals(P.media) &&
        this.forms.equals(P.forms) &&
        this.Scenarios.equals(P.Scenarios) &&
        this.pass.equals(P.pass) &&
        this.fail.equals(P.fail)){
        for(WebSite web:P.webSites){
        if (this.webSites.get(web.getWebID().intValue()).HasChange(web)){
           return true;
            }
        }         
          return false;
      } else {
          return true;
      } 
    }
    
    public void setProjectData(ArrayList<String> data) {
        
        Integer numberOfURL = 0;

        this.name = "";
        this.title = "";
        Date date = new Date();
        this.creationDate = date;
        this.urls = 0;
        this.nodes = 0;
        this.links = 0;
        this.imports = 0;
        this.media = 0;
        this.forms = (long) 0;
        this.Scenarios = 0;
        this.pass = 0;
        this.fail = 0;
        this.webSites = new ArrayList<WebSite>();

        for(String Line : data){
           // Check string starts with </ and ends with />
           if (Line.startsWith("</") && Line.endsWith("/>")){
              
                // Extract project name    
                if (Line.contains("name")) {
                this.name  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project title    
                if (Line.contains("title")) {
                this.title  =(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project creationDate    
                if (Line.contains("creationDate")) {
                //Date date = new Date();
                String dateVal = (Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                    try {
                        this.creationDate  =new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateVal);
                    } catch (ParseException ex) {
                    }
                }   

                // Extract project urls    
                if (Line.contains("urls")) {
                this.urls  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project nodes    
                if (Line.contains("nodes")) {
                this.nodes  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project links    
                if (Line.contains("links")) {
                this.links  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project imports    
                if (Line.contains("imports")) {
                this.imports  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project media    
                if (Line.contains("media")) {
                this.media  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project media    
                if (Line.contains("forms")) {
                this.forms  =Long.parseLong(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project Scenarios    
                if (Line.contains("Scenarios")) {
                this.Scenarios  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project pass    
                if (Line.contains("pass")) {
                this.pass  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));
                }   

                // Extract project fail    
                if (Line.contains("fail")) {
                this.fail  =Integer.parseInt(Line.substring((Line.indexOf("=")+1), (Line.indexOf("/>"))));         
                }   
           } // end of if statement that check </  and />
           
                                       // Get WebSites IDs
                if ((Line.startsWith("<*") && Line.endsWith("*>")) && (Line.contains("URL"))){
                    numberOfURL ++;
                }           

        } // end of loop
        
                for(int i=0; i< numberOfURL ; i++){
                ArrayList<String> tmpWeb = new ArrayList<String>();
                // Loop for extract websites data
                for(String Line : data){
                if ((Line.startsWith("<*") && Line.endsWith("*>")) && (Line.contains("index"+ (Integer.toString(i)) +"row"))){
                    tmpWeb.add(Line);
                  }            
                } // end of websites data loop
                
                this.webSites.add(i, (new WebSite(tmpWeb)));
             } // end of number of websites loop

    }
    
    
    
    public ArrayList<String> getProjectData() throws IOException{
        
        ArrayList newProject = new ArrayList();
        newProject.add("</name=" + this.name + "/>");
        newProject.add("</title=" + this.title + "/>");        
        newProject.add("</creationDate=" + this.creationDate + "/>");
        newProject.add("</urls=" + this.urls.toString() + "/>");
        newProject.add("</nodes=" + this.nodes.toString() + "/>");
        newProject.add("</links=" + this.links.toString() + "/>");
        newProject.add("</imports=" + this.imports.toString() + "/>");
        newProject.add("</media=" + this.media.toString() + "/>");
        newProject.add("</forms=" + this.forms.toString() + "/>");
        newProject.add("</Scenarios=" + this.Scenarios.toString() + "/>");
        newProject.add("</pass=" + this.pass.toString() + "/>");
        newProject.add("</fail=" + this.fail.toString() + "/>");
        
            for(WebSite web:this.webSites){
                newProject.addAll(web.getWebtData());
            }

        return newProject;
      }

    public ArrayList<WebSite> getWebSites() {
        return webSites;
    }

    public void setWebSites(ArrayList<WebSite> webSites) {
        this.webSites = webSites;
    }
    
    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getUrls() {
        return urls;
    }

    public Integer getNodes() {
        return nodes;
    }

    public Integer getLinks() {
        return links;
    }

    public Integer getImports() {
        return imports;
    }

    public Integer getMedia() {
        return media;
    }

    public Long getForms() {
        return forms;
    }

    public void setForms(Long forms) {
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

    public Integer getFail() {
        return fail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public void setUrls(Integer urls) {
        this.urls = urls;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public void setLinks(Integer links) {
        this.links = links;
    }

    public void setImports(Integer imports) {
        this.imports = imports;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }
    
  
}


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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.net.ssl.SSLHandshakeException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Mahmoud Abdelgawad
 * @serial ProtoTest TG Web
 * @version 1.0
 **/

public final  class FetchTreeWebSite {
      
    private static ArrayList<String> allLinks;
    private static ArrayList<String> linksStatus;

    
    public static ArrayList<String> getAllLinks() {
        return allLinks;
    }

    public static ArrayList<String> getLinksStatus() {
        return linksStatus;
    }
    

    
    public  ArrayList<String> FetchArrayList(String url) throws IOException {
        ArrayList<String> FAList = new ArrayList<String>();
        Document doc= null;

        try{
             doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException ex){ 
        } catch (HttpStatusException ex){
        } catch (SSLHandshakeException  ex){
        }
        if (doc != null){
            
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        FAList.add (FormatElement("<*media=%d*>", media.size()));
        for (Element src : media) {
            if (src.tagName().equals("img"))
                FAList.add (FormatElement("<Media:%s:<%s>:alt:%s:Media>",
                        src.tagName(), src.attr("abs:src"), src.attr("alt")));
            else
                FAList.add (FormatElement("<Media:%s:<%s>:alt:%s:Media>", src.tagName(), src.attr("abs:src"), src.tagName()));
        }

        FAList.add (FormatElement("<*imports=%d*>", imports.size()));
        for (Element link : imports) {
            FAList.add (FormatElement("<Imports:%s:<%s>:rel:%s:Imports>", link.tagName(),link.attr("abs:href"), link.attr("rel")));
        }
        
        // Pick up only links that have title.
        int numLinks = 0;
        for (Element link : links) {
            if (!link.text().isEmpty()){
                FAList.add (FormatElement("<Links:<%s>:title:%s:Links>", link.attr("abs:href"), link.text()));
                numLinks ++;
            }
        }
        
        FAList.add (FormatElement("<*links=%d*>", numLinks));
        return FAList;

        
       } else return null;       
 }

    private static boolean isURL(String str){
        URL u = null;
    try {
      u = new URL(str);
    } catch (MalformedURLException ex) {
        return false;
    }
        return true;
}

    private int numNonLeafLinks(Elements links, String parent){
        int numLinks = 0;
        for (Element link : links) {
           
            if ((!(link.text().trim()).isEmpty()) && (!isRoot(parent, link.attr("abs:href"))) &&
                (isURL(link.attr("abs:href"))) && (!allLinks.contains(link.attr("abs:href")))){
                numLinks ++;
            }
        }
        return numLinks;
}    
   
    private int numLeafLinks(Elements links, String parent){
        int numLinks = 0;
        for (Element link : links) {           
            if ((!(link.text().trim()).isEmpty()) && (!isRoot(parent, link.attr("abs:href"))) &&
                (isURL(link.attr("abs:href")))){
                numLinks ++;
            }
        }
        return numLinks;
    }  
    
    
    public  static DefaultMutableTreeNode FetchTree (String url, String linkTitle, int maximumLevel, int fetchTimeout) throws IOException{
        allLinks = new ArrayList<String>();
        linksStatus = new ArrayList<String>();

        return (processHierarchy(FetchUntilLevel(url, linkTitle, 0, maximumLevel, fetchTimeout)));
    }    

    
    private static boolean isOutofDomain(String str) throws MalformedURLException{
        String subURL = (new URL(str)).getHost();
        if ( allLinks.get(0).contains(subURL)) return false;
        else return true;
    }

    public static  Object[] FetchUntilLevel (String url, String LinkTitle, int currentLevel, int maxLevel, int fetchTimeout) throws IOException {
    
    if (currentLevel < maxLevel){
          Document doc= null;
            try{
                
               Connection connection = Jsoup.connect(url);
				connection.followRedirects(false);
                                connection.timeout(fetchTimeout);
                                connection.userAgent("Mozilla");
				Connection.Response response = connection.execute();
                                
                
                if (response.statusCode() >= 200 && response.statusCode() < 400) {
                    doc = response.parse();
                } else {
                    
                  linksStatus.add(url + " : " + response.statusCode() + " : " + response.statusMessage());
                  
                }
             
                  
               } catch (SocketTimeoutException ex) {} 
                 catch (HttpStatusException ex)    {}
                 catch (SSLHandshakeException  ex) {}
            

                if (doc != null){ // Here we get sub-Trees recursivly 
                        // we need to deliver doc to get other elements
                        
                        Elements _links = doc.select("a[href]");
                        
                        if(_links.size() > 0){
                            
                        Object[] tree = new Object[(_links.size()*_links.size()) + 1]; // We give extra space and remove them at the end
                        tree[0] = LinkTitle;
                            
                            
                            allLinks.add(url);
                            
                        int i=1;
                        // 
                        for (Element link : _links) {

                            if ((!(link.text().trim()).isEmpty()) && (!isRoot(url, link.attr("abs:href")))   &&
                               (isURL(link.attr("abs:href"))) && (!allLinks.contains(link.attr("abs:href"))) &&
                               (!link.attr("abs:href").contains(".jpg")) && (!link.attr("abs:href").contains(".png")) &&
                               (!link.attr("abs:href").contains(".gif")) && (!link.attr("abs:href").startsWith("mailto:")) &&
                               (!isOutofDomain(link.attr("abs:href")))){ 
                                
                                

                                    tree [i] = FetchUntilLevel(link.attr("abs:href"), link.text().trim(), (currentLevel+1), maxLevel, fetchTimeout);                                    
                                    i++;

                            } // end of if statement that check for good links
                          }// end of loop
                        
                         return removeNull(tree);
                        
                        } // end of if lengh > 0    

                        
                    }
                }
    //System.out.println(url + " : " + LinkTitle);
    Object[] singleNode = new Object[1];
    singleNode[0]= LinkTitle;
    return singleNode;

    }

    private String getMetaTag(Document document, String attr) {
    Elements elements = document.select("meta[name=" + attr + "]");
    for (Element element : elements) {
        final String s = element.attr("content");
        if (s != null) return s;
    }
    elements = document.select("meta[property=" + attr + "]");
    for (Element element : elements) {
        final String s = element.attr("content");
        if (s != null) return s;
    }
    return null;
}


    public static boolean isRoot(String root, String url){
    if ((url.equals(root)) ||
         url.equals("http://" + (root.substring(root.indexOf("www.") + 4 , root.length()))+ "/")) return true;
    else return false;
    
}

    public  static Object[] removeNull (Object[] tree){
    Object[] tmp = new Object[tree.length];
    int counter = 0;
    for (Object obj : tree) {
        if (obj != null) {
            tmp[counter++] = obj;
        }
    }
    Object[] ret = new Object[counter];
    System.arraycopy(tmp, 0, ret, 0, counter);
    return ret;
}

    public String FormatElement(String msg, Object... args) {
        return ((String.format(msg, args)));
    }


    public static DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
    DefaultMutableTreeNode node =
      new DefaultMutableTreeNode(hierarchy[0]);
    DefaultMutableTreeNode child;
    for(int i=1; i < hierarchy.length; i++) {
      Object nodeSpecifier = hierarchy[i];
      if (nodeSpecifier instanceof Object[])  //  node with children
        child = processHierarchy((Object[]) nodeSpecifier);
      else
        child = new DefaultMutableTreeNode(nodeSpecifier); // Leaf
      node.add(child);
    }
    return(node);
  }

    
    public static Object[] getAllTreePaths(DefaultMutableTreeNode tree){
        
        if (tree != null){
            
            Object[] allPaths = new Object[tree.getLeafCount()];
            DefaultMutableTreeNode selectedNode;

            // get first path from root to first leaf
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getFirstLeaf();
            TreePath path = new TreePath(node.getPath());  
            DefaultMutableTreeNode firstPathToleaf;
            ArrayList<String> singlePath = new ArrayList<String>();
            
            for(int j=0; j < path.getPathCount(); j++){
            firstPathToleaf = (DefaultMutableTreeNode) path.getPathComponent(j);
            singlePath.add(((String) firstPathToleaf.getUserObject()));
            }
            
            allPaths[0] = singlePath;
            
            for(int i= 1; i < tree.getLeafCount(); i++){ 
            singlePath = new ArrayList<String>();                
            node = (DefaultMutableTreeNode) node.getNextLeaf();
            if (node.isLeaf()){
            path = new TreePath(node.getPath());  
            for(int j=0; j < path.getPathCount(); j++){
            selectedNode = (DefaultMutableTreeNode) path.getPathComponent(j);
            singlePath.add(((String) selectedNode.getUserObject()));
            }                
            }
                                    
           allPaths[i] = singlePath;           
        }
            return allPaths;
   
        } else return null;
        
        
    }
 
    
    public static int NodeCount(DefaultMutableTreeNode treeNode){
        
           Enumeration iterator = treeNode.depthFirstEnumeration();
           int i = 0;
           while (iterator.hasMoreElements()){
             iterator.nextElement();
             i++;
           }
        return i;
        
    }
}

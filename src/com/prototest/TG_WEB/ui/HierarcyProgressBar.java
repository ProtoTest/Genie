/*
 * ProtoTest TG 1.0 is Test Generation tool that generates test cases 
 * automatically by using Input-Space Partitioning techniques and executes
 * the result tests against target desktop websites, which lunche on FireFox,
 * IE, Opera, and Safari. We use selenuim standalone, WebDriver, and
 * PageObject Pattern, which is adopted by selenium.   
 * June 2, 2013.
 * All copy rights resered to ProtoTest LLC.
 * (Prototest.com, 1999 Browdway Denver, CO, USA). 
 */

package com.prototest.TG_WEB.ui;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import static com.prototest.TG_WEB.ui.FetchTreeWebSite.isRoot;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLHandshakeException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
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

public final class HierarcyProgressBar extends JDialog {
        private DefaultMutableTreeNode root;
        private static ArrayList<String> allLinks;
        int IncreaseProgressValue;
        private static ArrayList<String> linksStatus;
        private int maximumLevel;
        private int fetchTimeout;
        private String webSite;
        private String linkTitle;
        public boolean UI_Action;
        private int links;
        private int media;
        private int formsNum;
        private int imports;
        private List<List<Object>> formElementsList;
        

    /**
     * Creates new form HierarcyProgressBar
     */
HierarcyProgressBar(JFrame parent, String webSite, String linkTitle, int maxlevel, int _fetchTimeOut) throws IOException, InterruptedException {
        super(parent,true);
        initComponents();

        setUndecorated(true);
        Progression.setValue(0);
        this.webSite = webSite;
        this.linkTitle = linkTitle;
        maximumLevel = maxlevel; 
        fetchTimeout = _fetchTimeOut;
        ProgressLabel.setText("Fetch, " + this.webSite + " [ until level: " + String.valueOf(maximumLevel) + " ]");
        this.getRootPane().setDefaultButton(CancelButton);
        this.root = null;
        Progression.setStringPainted(true);
        pack();
        // Center in the screen
        Rectangle parentBounds = parent.getBounds();
        Dimension size = getSize();
        // Center in the parent
        int x = Math.max(0, parentBounds.x + (parentBounds.width - size.width) / 2);
        int y = Math.max(0, parentBounds.y + (parentBounds.height - size.height) / 2);
        setLocation(new Point(x, y));
        // ArrayList as Hash table to eliminate repeated links.
        allLinks = new ArrayList<String>();
        linksStatus = new ArrayList<String>();
        formElementsList = Lists.newArrayList();
        links = 0;
        media = 0;
        formsNum = 0;
        imports = 0;
    }

    public List<List<Object>> getFormElementsList() {
        return formElementsList;
    }


    public ArrayList<String> getLinksStatus() {
        return linksStatus;
    }



public  DefaultMutableTreeNode FetchTree () throws IOException, InterruptedException {
    URL aURL = null;
    try{
        aURL = new URL(webSite);
    }catch (MalformedURLException ex){}
    
        if (aURL != null){ // Here we get sub-Trees recursivly 
            linkTitle = (!aURL.getHost().isEmpty()? aURL.getHost() : webSite);
            return (processHierarchy(FetchUntilLevel(webSite, linkTitle, 0, maximumLevel, fetchTimeout)));
        } else{
            return null;
        }
}    

private boolean isURL(String str){
        URL u = null;
    try {
      u = new URL(str);
    } catch (MalformedURLException ex) {
        return false;
    }
        return true;
}


private boolean isOutofDomain(String str) throws MalformedURLException{
        String subURL = (new URL(str)).getHost();
        if ( allLinks.get(0).contains(subURL)) return false;
        else return true;
}


private boolean isRoot(String root, String url){
    if ((url.equals(root)) ||
         url.equals("http://" + (root.substring(root.indexOf("www.") + 4 , root.length()))+ "/")) return true;
    else return false;
    
}

private  Object[] removeNull (Object[] tree){
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
  

private void getNumberOfTags(Document doc) {
    
        Elements _links = doc.select("a[href]");
        Elements _media = doc.select("[src]");
        Elements _imports = doc.select("link[href]");
        links = links + _links.size();
        media = media + _media.size();
        imports = imports + _imports.size();
}

private   List<List<String>> radioOptions(Elements radios){

    Set<String> radioNameSet= Sets.newHashSet();

    for (Element link : radios) {
         radioNameSet.add(link.attr("name").toString());
    }

    List<Elements> radioGroups = Lists.newArrayList();

    for(String name : radioNameSet) {             
    radioGroups.add(radios.select("[name="+name+"]"));
    }

    List<Set<String>> sets = new ArrayList<Set<String>>();   

    for(Elements sub_radio: radioGroups){

    List<String> subSet = Lists.newArrayList();

    for (Element link : sub_radio) {
    subSet.add(link.attr("id"));
    }

    sets.add(new HashSet(subSet));

    } 

    Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
    List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);

    return cartesianList;
}


private  List<List<String>> selectionOptions(Elements selects){
    List<Set<String>> sets = new ArrayList<Set<String>>();   
    for (Element link : selects) {

    List<String> subSet = Lists.newArrayList();            
    Elements optionGroups = link.getElementsByTag("option");
    for (Element opt : optionGroups ) {
        subSet.add("<id>" + link.attr("id") +"</id><lable>" + opt.attr("value") + "</lable>");            
    }   

    sets.add(new HashSet(subSet));

    } 
    Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
    List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);
    return cartesianList;        
}

private  List<String> areasList(Elements areas){
    List<String> areaIDs = Lists.newArrayList();

    for (Element link : areas) {
        if(!link.attr("style").equals("display:none"))
         areaIDs.add(link.attr("id")); 
    }

    return areaIDs;        
}

private  List<String> inputsList(Elements inputs){

    List<String> inputsID = Lists.newArrayList();

    for (Element link : inputs) {

        if(! ( link.attr("id").isEmpty() || link.attr("type").equals("hidden") || link.attr("type").equals("file") )){

        // catch email input
        if (link.attr("id").contains("email")  || link.attr("type").equals("email") ||
            link.attr("name").contains("email")|| link.attr("class").contains("email")) {                
            inputsID.add("<type=email><id>" + link.attr("id") + "</id>");                
        } else                  
        // catch password input
        if (link.attr("type").equals("password")) { // catch text input

            inputsID.add("<type=password><id>" + link.attr("id") + "</id>");                

         } else {
            inputsID.add("<type=text><id>" + link.attr("id") + "</id>");                
        }

        } // input elements we need

    } // end for loop
  return inputsID;   

}

private  List<String> buttonsList(Elements buttons){
    List<String> buttonIDs = Lists.newArrayList();
    for (Element link : buttons) {
      buttonIDs.add(link.attr("id")); 
    }        
    return buttonIDs;        
}

private void getFormElements(Document doc, String url){
    
        Elements radios = doc.select("input[type=radio]");    
        Elements selects = doc.select("select");
        Elements areas = doc.select("textarea");
        Elements inputs = doc.select("input[type=text], input[type=email], input[type=password]");
        Elements buttons = doc.select("input[type=button], input[type=submit], input[type=reset], button");
        
        formsNum = formsNum + radios.size() + selects.size() + areas.size() + inputs.size() + buttons.size();

        // Get radio cartesian product carcombinations
        List<List<String>> radioGroups = radioOptions(radios);

        // Get selection cartesian product carcombinations
        List<List<String>> selectionGroups = selectionOptions(selects);
                
        // Get list of areas
        List<String> areasGroup = areasList(areas);
        
        // Get list of inputs
        List<String> inputsGroup = inputsList(inputs);

        // Get list of buttons
        List<String> buttonsGroup = buttonsList(buttons);
        
        List<Object> oneForm = Lists.newArrayList();
        
        oneForm.add(url);
        oneForm.add(radioGroups);
        oneForm.add(selectionGroups);
        oneForm.add(areasGroup);
        oneForm.add(inputsGroup);
        oneForm.add(buttonsGroup);
        
        formElementsList.add(oneForm);
}

private  Object[] FetchUntilLevel (String url, String LinkTitle, int currentLevel, int maxLevel, int fetchTimeout) throws IOException {
    
    if (currentLevel < maxLevel){
          Document doc= null;
            try{
                
               Connection connection = Jsoup.connect(url);
				connection.followRedirects(false);
                                connection.timeout(fetchTimeout);
                                connection.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/536.30.1 (KHTML, like Gecko) Version/6.0.5 Safari/536.30.1");
				Response response = connection.execute();
                                
                
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    doc = response.parse();
                } else {
                    
                  linksStatus.add(url + "    " + response.statusCode() + "    " + response.statusMessage());
                  
                }
             
                  
               } catch (SocketTimeoutException ex) {} 
                 catch (HttpStatusException ex)    {}
                 catch (SSLHandshakeException  ex) {}
            

                if (doc != null){ // Here we get sub-Trees recursivly 
                    
                        // count other elemenets                        
                        getNumberOfTags(doc);
                        
                        // get form elements
                        getFormElements(doc, url);
                        
                        //Elements _links = doc.select("div[id~=^\\\\d], div[class~=^\\\\d], a[href]");
                        Elements _links = doc.select("a[href]");
                        
                        if(_links.size() > 0){
                            
                        Object[] tree = new Object[(_links.size()*_links.size()) + 1]; // We give extra space and remove them at the end
                        //tree[0] = LinkTitle;
                        tree[0] = LinkTitle + "@>>>" + url;
                            
                            IncreaseProgressValue = (int) (100/ _links.size());
                            int progess = IncreaseProgressValue;
                            ProcessShow.append(LinkTitle + " .\n");                             
                            Rectangle processRect = ProcessShow.getBounds();
                            processRect.x = 0;
                            processRect.y = 0;
                            ProcessShow.paintImmediately(processRect);
                            
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
                                    progess = progess + IncreaseProgressValue;
                                    if (progess > 100) progess = 100;
                                    Progression.setValue(progess);
                                    Rectangle progressRect = Progression.getBounds();
                                    progressRect.x = 0;
                                    progressRect.y = 0;
                                    Progression.paintImmediately( progressRect );

                            } // end of if statement that check for good links
                          }// end of loop
                        
                         return removeNull(tree);
                        
                        } // end of if lengh > 0    
                        
                    }
                }
    //System.out.println(url + " : " + LinkTitle);
    Object[] singleNode = new Object[1];
    //singleNode[0]= LinkTitle;
    singleNode[0]= LinkTitle + "@>>>" + url;
    
    ProcessShow.append(LinkTitle + "... \n");
    Rectangle processRect = ProcessShow.getBounds();
    processRect.x = 0;
    processRect.y = 0;
    ProcessShow.paintImmediately( processRect);
    
    return singleNode;
}



public DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
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


public DefaultMutableTreeNode getRoot() {
    return this.root;
}

public void setRoot(DefaultMutableTreeNode root) {
        this.root = root;
    }

    public int getLinks() {
        return links;
    }

    public int getMedia() {
        return media;
    }

    public int getImports() {
        return imports;
    }

    public int getFormsNum() {
        return formsNum;
    }

    



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ProcessShow = new javax.swing.JTextArea();
        Progression = new javax.swing.JProgressBar();
        ProgressLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        CancelButton = new javax.swing.JButton();
        StartButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        ProcessShow.setLineWrap(true);
        ProcessShow.setWrapStyleWord(true);
        ProcessShow.setFocusable(false);
        jScrollPane1.setViewportView(ProcessShow);

        Progression.setPreferredSize(new java.awt.Dimension(147, 20));
        Progression.setRequestFocusEnabled(false);
        Progression.setStringPainted(true);

        ProgressLabel.setForeground(new java.awt.Color(0, 0, 204));
        ProgressLabel.setText("Fetching : ");
        ProgressLabel.setPreferredSize(new java.awt.Dimension(175, 16));
        ProgressLabel.setRequestFocusEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 51)));

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        StartButton.setText("Start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(CancelButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(StartButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(StartButton)
                    .add(CancelButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jScrollPane1)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(ProgressLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(Progression, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ProgressLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(Progression, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(292, 367));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        this.UI_Action = false;
        setVisible(false);
    }//GEN-LAST:event_CancelButtonActionPerformed

    
    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
        CancelButton.setEnabled(false);
        StartButton.setEnabled(false);                
        Rectangle processRect = CancelButton.getBounds();
        processRect.x = 0;
        processRect.y = 0;
        CancelButton.paintImmediately( processRect );
        processRect = StartButton.getBounds();
        processRect.x = 0;
        processRect.y = 0;
        StartButton.paintImmediately( processRect );
      try {
            // Here we got entire website tree
            this.setRoot(FetchTree());            
            while (Progression.getValue() < Progression.getMaximum()){
            Progression.setValue(Progression.getValue() + 1);
            
            }
          } catch (IOException ex) {} catch (InterruptedException ex) {}
      CancelButton.setEnabled(true);
      StartButton.setEnabled(true);                
      UI_Action = true;
      setVisible(false);
    }//GEN-LAST:event_StartButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JTextArea ProcessShow;
    private javax.swing.JLabel ProgressLabel;
    private javax.swing.JProgressBar Progression;
    private javax.swing.JButton StartButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

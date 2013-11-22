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
import org.apache.commons.codec.binary.Base64;
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
        private String UserName;
        private String Password;
        private String linkTitle;
        public boolean UI_Action;
        private int links;
        private int media;
        private int imports;
        private List<List<Object>> formElementsList;
        private Set<List<Object>> formElementsSet;
        private Set<String> checkURLs;
        private List<Element> selectionLists;
        

    /**
     * Creates new form HierarcyProgressBar
     */
HierarcyProgressBar(JFrame parent, String webSite, String _UserName, String _Password, String linkTitle, int maxlevel, int _fetchTimeOut) throws IOException, InterruptedException {
        super(parent,true);
        initComponents();

        setUndecorated(true);
        Progression.setValue(0);
        this.webSite = webSite;
        this.UserName = _UserName;
        this.Password = _Password;
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
        formElementsSet = Sets.newHashSet();
        checkURLs = Sets.newHashSet();
        selectionLists = Lists.newArrayList();

        links = 0;
        media = 0;
        imports = 0;
    }

    public List<List<Object>> getFormElementsList() {
        formElementsList.addAll(formElementsSet);
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
            return (processHierarchy(FetchUntilLevel(webSite, UserName, Password, linkTitle, 0, maximumLevel, fetchTimeout)));
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
        if(!link.attr("name").isEmpty())
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
    if(!link.attr("id").isEmpty())        
    subSet.add(link.attr("id"));
    }

    sets.add(new HashSet(subSet));

    } 

    Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
    List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);

    return cartesianList;
}


private boolean selectIsExistOrEmpty(Element link){
    boolean returnValue = true;
    if (!(link.attr("id").isEmpty() && link.attr("name").isEmpty())){
    //Elements optionGroups = link.getElementsByTag("option");

    for(Element select: selectionLists){
        
    //Elements options = select.getElementsByTag("option");
     if ((link.attr("id").equals(select.attr("id")) && link.attr("name").equals(select.attr("name")))){
     return returnValue;
     } 
    
    }
    
     returnValue = false;    
     selectionLists.add(link);
   }
    
    return returnValue;
}


private  List<List<String>> selectionOptions(Elements selects){
    List<Set<String>> sets = new ArrayList<Set<String>>();   
    for (Element link : selects) {
        
    if(!selectIsExistOrEmpty(link)){

    List<String> subSet = Lists.newArrayList();            
    Elements optionGroups = link.getElementsByTag("option");
    for (Element opt : optionGroups ){
        if (!link.attr("id").isEmpty())
        subSet.add("<id>" + link.attr("id") +"</id><label>" + opt.attr("value") + "</label>"); 
        else
        subSet.add("<name>" + link.attr("name") +"</name><label>" + opt.attr("value") + "</label>"); 
    }   

    sets.add(new HashSet(subSet));
    
    }
    
    } 
    Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
    List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);
    return cartesianList;        
}


private  List<String> areasList(Elements areas){
    Set<String> areaIDsSet= Sets.newHashSet();

    for (Element link : areas) {
        
        if(!(link.attr("style").equals("display:none") || ( link.attr("id").isEmpty() && link.attr("name").isEmpty()) ) ){
            
         if(!link.attr("id").isEmpty())   
         areaIDsSet.add("<id>" + link.attr("id") + "</id>");
         else
         areaIDsSet.add("<name>" + link.attr("name") + "</name>");
         
        }  
    }
    
    List<String> areaIDs = Lists.newArrayList(areaIDsSet);
    return areaIDs;        
}


private  List<String> inputsList(Elements inputs){

    Set<String> inputIDsSet= Sets.newHashSet();

    for (Element link : inputs) {

        if(! ((link.attr("id").isEmpty() && link.attr("name").isEmpty()) || link.attr("type").equals("hidden") || link.attr("type").equals("file") )){

        // catch email input
        if (link.attr("id").contains("email")  || link.attr("type").equals("email") ||
            link.attr("name").contains("email")|| link.attr("class").contains("email")) {  
            
            if (!link.attr("id").isEmpty())
            inputIDsSet.add("<type=email><id>" + link.attr("id") + "</id>");
            else
            inputIDsSet.add("<type=email><name>" + link.attr("name") + "</name>");
                
        } else                  
        // catch password input
        if (link.attr("type").equals("password")) { // catch text input

            if (!link.attr("id").isEmpty())
            inputIDsSet.add("<type=password><id>" + link.attr("id") + "</id>");
            else
            inputIDsSet.add("<type=password><name>" + link.attr("name") + "</name>");

         } else {
            if (!link.attr("id").isEmpty())            
            inputIDsSet.add("<type=text><id>" + link.attr("id") + "</id>");                
            else
            inputIDsSet.add("<type=text><name>" + link.attr("name") + "</name>");                                
        }

        } // input elements we need

    } // end for loop

  List<String> inputIDs = Lists.newArrayList(inputIDsSet);    
  return inputIDs;   

}


private  List<String> buttonsList(Elements buttons){
    Set<String> buttonIDsSet= Sets.newHashSet();    
    for (Element link : buttons) {
        
        if(!(link.attr("id").isEmpty() && link.attr("name").isEmpty())){ 
            
        if (!link.attr("id").isEmpty())    
        buttonIDsSet.add("<id>" + link.attr("id") + "</id>");
        else
        buttonIDsSet.add("<name>" + link.attr("name") + "</name>");        
        }
        
    }     
    
    List<String> buttonIDs = Lists.newArrayList(buttonIDsSet);    
    return buttonIDs;        
}


private long getTotalItems(){

    int radiosGroups = 0;
    int selectionGroups = 0;
    int areas = 0;
    int inputs = 0;
    int buttons = 0;
       for(List<Object> form: formElementsList){
       // Count Radio items
       List<List<String>> RadioItems = ((List<List<String>>) form.get(1));
       for(List<String> GR: RadioItems){
       if (!GR.isEmpty()) radiosGroups++;   
       }

       // Count Selection items                       
       List<List<String>> selectionItems = ((List<List<String>>) form.get(2));
       for(List<String> GS: selectionItems){
       if (!GS.isEmpty()) selectionGroups++;   
       }

       // Count area items                       
       List<String> AreaItems = ((List<String>) form.get(3));
       areas += AreaItems.size();

       // Count input items                       
       List<String> InputItems = ((List<String>) form.get(4));
       inputs += InputItems.size();

       // Count buttons items                       
       List<String> ButtonItems = ((List<String>) form.get(5));
       buttons += ButtonItems.size();
       
       }

     return (radiosGroups + selectionGroups + areas + inputs  + buttons);
}


private String getURLwithAuthorization(String url, String _userName, String _password){
 if (!(_userName.isEmpty() && _password.isEmpty())){
     
     String aURL; 
     if (url.contains("https://")) aURL = url.substring(0,8) + _userName + ":" + _password + "@" + url.substring(8,url.length());
     else aURL = url.substring(0,7) + _userName + ":" + _password + "@" + url.substring(7,url.length());
     
 return aURL; 
 
 } else return url;
 
}


private boolean checkRadioList(List<List<String>> _radioGroups){
    boolean retuenValue = false;
    for(List<String> list: _radioGroups){
     if (!list.isEmpty()){
         retuenValue = true;
         break;
     }   
    }    
    return  retuenValue;
}


private boolean checkSelectList(List<List<String>> _selectionGroups){
    boolean retuenValue = false;
    for(List<String> list: _selectionGroups){
     if (!list.isEmpty()){
         retuenValue = true;
         break;
     }   
    }    
    return  retuenValue;
}



private void getFormElements(Document doc, String url, String userName, String password){
    
        if(!checkURLs.contains(url)){
        checkURLs.add(url);
        Elements radios = doc.select("input[type=radio]");    
        Elements selects = doc.select("select");
        Elements areas = doc.select("textarea");
        Elements inputs = doc.select("input[type=text], input[type=email], input[type=password]");
        Elements buttons = doc.select("input[type=button], input[type=submit], input[type=reset], button");

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
        
        if(checkRadioList(radioGroups) || checkSelectList(selectionGroups) ||
          (!areasGroup.isEmpty()) || (!inputsGroup.isEmpty()) || (!buttonsGroup.isEmpty())){
            
        oneForm.add(getURLwithAuthorization(url, userName, password));
        oneForm.add(radioGroups);
        oneForm.add(selectionGroups);
        oneForm.add(areasGroup);
        oneForm.add(inputsGroup);
        oneForm.add(buttonsGroup);        
        formElementsSet.add(oneForm);
        
        }
    }

}

private  Object[] FetchUntilLevel (String url, String _userName, String _password, String LinkTitle, int currentLevel, int maxLevel, int fetchTimeout) throws IOException {
    
    if (currentLevel < maxLevel){
          Document doc= null;
            try{

             String login = _userName + ":" + _password;
             String base64login = new String(Base64.encodeBase64(login.getBytes()));
                
               Connection connection = Jsoup.connect(url);

                             if (!( _userName.isEmpty() && _password.isEmpty())){
                                connection.header("Authorization", "Basic " + base64login);
                                }
                                
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
                        getFormElements(doc, url, _userName,  _password);
                        
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

                                    tree [i] = FetchUntilLevel(link.attr("abs:href"), _userName, _password, link.text().trim(), (currentLevel+1), maxLevel, fetchTimeout);                                    
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

    public long getFormsNum() {
        return getTotalItems();
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

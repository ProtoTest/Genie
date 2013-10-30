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

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.io.FilenameUtils;
import org.junit.runner.Result;

/**
 * @author Mahmoud Abdelgawad
 * @serial ProtoTest TG Web
 * @version 1.0
 **/

public class PrototestTC_Web extends javax.swing.JFrame {
    private Project currentProject;
    private Project initialProject;
    private WebSite currentWebSite;
    private WebSite fetchedweb;
    private WebSite changedWeb;    
    private List<List<Object>> FormElements;
    private DefaultMutableTreeNode hierarcyTree;
    private DefaultTableModel model;
    private ArrayList<String> specifedTreePath; 
    private ArrayList<String> invalidLinks; 
    private ArrayList<String> tmpInputValues; 
    private ArrayList<String> inputValues; 

    private Object allTreePaths[];
    private String workDir;
    private String libraryDir;
    
    private ArrayList<String> textValues;    
    private ArrayList<String> emailValues;    
    private ArrayList<String> areaValues;    
    private ArrayList<String> passwordValues;    
    
    
    
   private void getSizeOfInputValues (){
       
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

          if(inputValue.contains("<area>")){
            start = inputValue.indexOf("<area>") + 6;
            end = inputValue.indexOf("</area>");
            areaValues.add(inputValue.substring(start, end));
          }  

          if(inputValue.contains("<password>")){
            start = inputValue.indexOf("<password>") + 10;
            end = inputValue.indexOf("</password>");
            passwordValues.add(inputValue.substring(start, end));
          }  

        }
    } 
    
    public final String getWorkDir() {
        return workDir;
    }

    public final void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public final String getLibraryDir() {
        return libraryDir;
    }

    public final void setLibraryDir(String libraryDir) {
        this.libraryDir = libraryDir;
    }



    /**
     * Creates new form PrototestTC_Web
     */
    public PrototestTC_Web() {
        initComponents(); 
        
        // Center alignment of table headers        
        TableCellRenderer rendererFromHeader = WebsitesList.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        model = (DefaultTableModel) WebsitesList.getModel();        
        this.setWorkDir(((new File("").getAbsolutePath()) + "\\workspace\\"));
        this.setLibraryDir(((new File("").getAbsolutePath()) + "\\lib\\"));

        if (!FileUtile.searchFile(this.getWorkDir())){
           FileUtile.createFolder(this.getWorkDir());
        }

        this.inputValues = new ArrayList<String>();      

        this.tmpInputValues = new ArrayList<String>();      
        this.tmpInputValues.add("<text>ProtoTest is Testing</text>");
        this.tmpInputValues.add("<area>ProtoTest is Testing so don't worry about this submission</area>");
        this.tmpInputValues.add("<email>test@prototest.com</email>");
        this.tmpInputValues.add("<password>ProtoTest123</password>");
        
        displayProjectOnFrame();
}
    

    public final void NewProject(){
       this.currentProject = new Project();
       this.currentProject.setProjectPath(this.getWorkDir());
       NewprojectFrame npf = new NewprojectFrame(this, this.currentProject);
       npf.setVisible(true);
       if (npf.UI_Action){
           try {
                FileUtile.createFolder((this.getWorkDir() + this.currentProject.getName()));
                FileUtile.writeToFile((this.getWorkDir()  + this.currentProject.getName() +
                    "\\" + this.currentProject.getName() + ".dat" ), this.currentProject.getProjectData());
                
                // Create intial input values file
                FileUtile.writeToFile((this.getWorkDir()  + this.currentProject.getName() +
                    "\\" + this.currentProject.getName() + "InputValues.txt" ),  this.tmpInputValues);
               
               // Make copy of all elements 
               if(!this.inputValues.isEmpty()) this.inputValues.clear();               
               this.inputValues.addAll(this.tmpInputValues);
                              
               this.currentProject.setProjectPath((this.getWorkDir() + this.currentProject.getName()));
               this.initialProject =  new Project(currentProject);
               displayProjectOnFrame();
               FileUtile.copyFile(this.getLibraryDir()+"\\j.jar", (this.getWorkDir() + this.currentProject.getName()));
               FileUtile.copyFile(this.getLibraryDir()+"\\s.jar", (this.getWorkDir() + this.currentProject.getName()));
               FileUtile.copyFile(this.getLibraryDir()+"\\h.jar", (this.getWorkDir() + this.currentProject.getName()));
           } catch (IOException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage(),
                 "PrototestTC- Fail saving", JOptionPane.WARNING_MESSAGE);
         }
       } else {
            this.currentProject = null;
            this.initialProject = null;
       }
    npf.dispose();
     
    }

    public final void NewWebsite(){
       // Set the number of rows as web ID, e.g. we have 3 rows so the next index will be 3 means forth row
       this.currentWebSite = new WebSite((Integer) (model.getRowCount()));
       NewWebsiteFrame nwf = new NewWebsiteFrame(this, this.currentWebSite);
       nwf.setVisible(true);
       if (nwf.UI_Action){
               this.currentProject.addWebSite(this.currentWebSite);
       }
    nwf.dispose();
     
    }

    public final void openProject(){
      File path = new File(this.getWorkDir());
      JFileChooser chooser = new JFileChooser();
      chooser.setDialogTitle("PrototestTC- Open Project");              
      chooser.setCurrentDirectory(path);
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setApproveButtonText("Open Project");
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
      String selectedDir = chooser.getSelectedFile().getAbsolutePath() +
                        "\\" + chooser.getSelectedFile().getName() + ".dat";
      // Open 
      String inputValuefile = chooser.getSelectedFile().getAbsolutePath() +
                        "\\" + chooser.getSelectedFile().getName() + "InputValues.txt";
      
      if (FileUtile.searchFile(selectedDir)){

           try {
                this.currentProject = new Project((FileUtile.readFromFile(selectedDir)));
                
                // Make copy of all input values from file 
                if(!this.inputValues.isEmpty()) this.inputValues.clear();               
                this.inputValues.addAll(FileUtile.readFromFile(inputValuefile));
                               
                this.currentProject.setProjectPath((this.getWorkDir() + this.currentProject.getName()));
                this.initialProject =  new Project(currentProject);

               displayProjectOnFrame();
           } catch (IOException ex) {
              JOptionPane.showMessageDialog(null,ex.getMessage(),
         "PrototestTC- Fail opening", JOptionPane.ERROR_MESSAGE);

           }
       }
    }       
       
   }
      
    public final void saveProject() throws IOException{
        try{
        FileUtile.removeLinesStartWithFromFile((this.currentProject.getProjectPath() +
                "\\" + this.currentProject.getName() + ".dat" ), "</");
        FileUtile.removeLinesStartWithFromFile((this.currentProject.getProjectPath() +
                "\\" + this.currentProject.getName() + ".dat" ), "<*");
        FileUtile.AddLinesToFile((this.currentProject.getProjectPath() + "\\" +
                this.currentProject.getName() + ".dat"), this.currentProject.getProjectData());            
        } catch (IOException ex){
                 JOptionPane.showMessageDialog(null,ex.getMessage(),
                 "PrototestTC- Fail saving", JOptionPane.WARNING_MESSAGE);            
        }
    }
    
    public final void projectUpdate(){
        
             if (this.currentProject != null){  
                 
             Integer _nodes = 0;
             Integer _links = 0;
             Integer _imports = 0;
             Integer _media = 0;
             Long _forms = (long) 0;
             Integer _Scenarios = 0;
             Integer _pass = 0;
             Integer _fail = 0;

            if (this.currentProject != null){        
            for(WebSite web: this.currentProject.getWebSites()){
                _nodes = _nodes + web.getNodes();
                _links = _links + web.getLinks();
                _imports = _imports + web.getImports();
                _media = _media + web.getMedia();
                _forms = _forms + web.getForms();
                _Scenarios = _Scenarios + web.getScenarios();
                _pass = _pass + web.getPass();
                _fail = _fail + web.getFail();                
           } 
            
            this.currentProject.setUrls(this.currentProject.getWebSites().size());
            this.currentProject.setNodes(_nodes);
            this.currentProject.setLinks(_links);
            this.currentProject.setImports(_imports);
            this.currentProject.setMedia(_media);
            this.currentProject.setForms(_forms);
            this.currentProject.setScenarios(_Scenarios);
            this.currentProject.setPass(_pass);
            this.currentProject.setFail(_fail);     
       }  

      }
             
    }
      
    public final void displayWebSitesOnFrame(){

        while(model.getRowCount()>0) {
            model.removeRow(0);
        }

        if (this.currentProject != null){        
            for(WebSite web: this.currentProject.getWebSites()){
            model.insertRow(web.getWebID(), new Object[]{web.getURL(), web.getHTTP_HTTPS(),
                        web.getUserName() + "|" + web.getPassword(),    
                        web.getRunDate().toString(), web.getNodes().toString(),
                        web.getLinks().toString(), web.getImports().toString(), 
                        web.getMedia().toString(), web.getForms().toString(),
                        web.getScenarios().toString(), web.getPass().toString(),
                        web.getFail().toString()}); 
           }            
       }  
    }
    
    public final void displayProjectOnFrame(){
       if ((this.currentProject != null)){
       setTitle("Genie TCG (Website TestCases Generator) [ " + this.currentProject.getName() + " ]");
       Title.setText(this.currentProject.getTitle());
       creationDate.setText(String.format("%1$tb %1$td, %1$tY", this.currentProject.getCreationDate()));
       URLS.setText(this.currentProject.getUrls().toString());
       nodes.setText(this.currentProject.getNodes().toString());
       links.setText(this.currentProject.getLinks().toString());
       imports.setText(this.currentProject.getImports().toString());
       media.setText(this.currentProject.getMedia().toString());
       forms.setText(this.currentProject.getForms().toString());
       Scenarios.setText(this.currentProject.getScenarios().toString());
       Pass.setText(this.currentProject.getPass().toString());
       Fail.setText(this.currentProject.getFail().toString());
        }else{
           setTitle("Genie TCG (Website TestCases Generator)");
           Title.setText("");
           creationDate.setText("");
           URLS.setText("0");
           nodes.setText("0");
           links.setText("0");
           imports.setText("0");
           media.setText("0");
           forms.setText("0");
           Scenarios.setText("0");
           Pass.setText("0");
           Fail.setText("0"); 
           CurrentScript.setText("none");
           fetchingLevel.setValue(1);
           fetchTimeOut.setValue(1);
        }
       
        displayWebSitesOnFrame();
    }
    
    public final void generateTestFormItems(int certerion){
        
        // Check if we have fetched
        if ((FormElements == null)||(fetchedweb == null)){
         JOptionPane.showMessageDialog(null, "You need to fetch the website before generating test cases");
         return;
        }   
        
        // Get number of input values  
        getSizeOfInputValues(); 
        int numberOfTextValues = textValues.size()* emailValues.size() * passwordValues.size();
        if(numberOfTextValues >= 0) numberOfTextValues = 1;
        int numberOfAreaValues = areaValues.size();
        PercentageFrame PF = new PercentageFrame(this, FormElements, certerion, numberOfTextValues, numberOfAreaValues);
        PF.setVisible(true);
        if(PF.UI_Action){

        long numberOfTestcases = PF.getTotalTestCases();
        
        if(numberOfTestcases > 0 ){
            
        NewScriptFrame NSF = new NewScriptFrame(this, currentProject);
        NSF.setVisible(true);
        if (NSF.UI_Action){                                    
            try {
                
                switch (certerion){
                    case 0: ScriptUtile.formtemsScriptAll(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getAddressWithAuthorize(), NSF.getBrowser(),
                                           NSF.getTimeOut(), FormElements,inputValues, numberOfTestcases); break;
                    case 1: ScriptUtile.formtemsScriptText(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getAddressWithAuthorize(), NSF.getBrowser(),
                                           NSF.getTimeOut(), FormElements,inputValues, numberOfTestcases); break;
                    case 2: ScriptUtile.formtemsScriptLists(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getAddressWithAuthorize(), NSF.getBrowser(),
                                           NSF.getTimeOut(), FormElements,inputValues, numberOfTestcases); break;
                    case 3: ScriptUtile.formtemsScriptRadios(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getAddressWithAuthorize(), NSF.getBrowser(),
                                           NSF.getTimeOut(), FormElements,inputValues, numberOfTestcases); break;
                } // end of switch
                
                changedWeb.setForms(numberOfTestcases);
                currentProject.getWebSites().get(WebsitesList.getSelectedRow()).AssignWebsite(changedWeb);
                CurrentScript.setText(NSF.getFileName());
                
                projectUpdate();
                saveProject();
                displayProjectOnFrame();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Scripting is failed. it faced problem to save");
            }
        }
        
        NSF.dispose();                                        

        }
        
      }       
        
        PF.dispose();                                        

    }
        
    public final void generateAllPaths(){
        
        allTreePaths = FetchTreeWebSite.getAllTreePaths(hierarcyTree);     
        NewScriptFrame NSF = new NewScriptFrame(this, currentProject);
        NSF.setVisible(true);
        if (NSF.UI_Action){                                    
            try {

                int numScearios = ScriptUtile.multiScenarioScript(NSF.getFileName(), currentProject.getProjectPath() ,fetchedweb.getAddressWithAuthorize(), NSF.getBrowser(), NSF.getTimeOut(), allTreePaths);
                changedWeb.setScenarios(numScearios);
                currentProject.getWebSites().get(WebsitesList.getSelectedRow()).AssignWebsite(changedWeb);
                CurrentScript.setText(NSF.getFileName());
                
                projectUpdate();
                saveProject();
                displayProjectOnFrame();
                
              if (JOptionPane.showConfirmDialog(null,"Do you like to report all invlaid links too?",
                  "ProtoTest TG- Reporting ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){                
                ScriptUtile.invalidLinksReport(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getURL(), invalidLinks);
              }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Scripting is failed. it faced problem to save");
            }
        }

        NSF.dispose();        
    }

    public final void generateSpecifiedPaths(){
        
            TreeWebsiteFrame specifiedPathFrame = new TreeWebsiteFrame(this, hierarcyTree);
            specifiedPathFrame.setVisible(true);
           if(specifiedPathFrame.UI_Action && specifiedPathFrame.getSelectedPath() != null){

                specifedTreePath = new ArrayList<String>(); 
                specifedTreePath.addAll(specifiedPathFrame.getSelectedPath());
                NewScriptFrame NSF = new NewScriptFrame(this, currentProject);                                
                NSF.setVisible(true);
            if (NSF.UI_Action){                                    
                try {

                    ScriptUtile.singleScenarioScript(NSF.getFileName(), currentProject.getProjectPath() ,fetchedweb.getAddressWithAuthorize() , NSF.getBrowser(), NSF.getTimeOut(), specifedTreePath);
                    changedWeb.setScenarios(1);
                    currentProject.getWebSites().get(WebsitesList.getSelectedRow()).AssignWebsite(changedWeb);
                    CurrentScript.setText(NSF.getFileName());
                    
                    projectUpdate();
                    saveProject();
                    displayProjectOnFrame();     

                 if (JOptionPane.showConfirmDialog(null,"Do you like to report all invlaid links too?",
                  "ProtoTest TG- Reporting ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){                
                ScriptUtile.invalidLinksReport(NSF.getFileName(), currentProject.getProjectPath(),fetchedweb.getURL(), invalidLinks);
                 }
                 
                 
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Scripting is failed. it encountered problem to save");
                }
            }

            NSF.dispose();

           }

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        ProjectPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Title = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        creationDate = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        URLS = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nodes = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        links = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        imports = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Pass = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Scenarios = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        Fail = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        media = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        forms = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        NewWebsiteButton = new javax.swing.JButton();
        RemoveWebsiteButton = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        WebsitesList = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        CurrentScript = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jSeparator7 = new javax.swing.JSeparator();
        HierarcyCoverageList = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        fetchingLevel = new javax.swing.JSpinner();
        jSeparator8 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        fetchTimeOut = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        FormsCoverageList = new javax.swing.JComboBox();
        LinksCriteria = new javax.swing.JRadioButton();
        FormsCriteria = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        NewProjectMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        OpenProjectMenuItem = new javax.swing.JMenuItem();
        SaveProjectMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        CloseProjectMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        ExitMenuItem = new javax.swing.JMenuItem();
        ReportMenuItem = new javax.swing.JMenu();
        InvalidLinks = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        TestCheckList = new javax.swing.JMenuItem();
        UrlScenarioList = new javax.swing.JMenuItem();
        TestResults = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        TestFormsRsults = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        AboutMenuItem = new javax.swing.JMenuItem();

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        setTitle("ProtoTest TG 1.0");

        ProjectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project"));
        ProjectPanel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ProjectPanel.setOpaque(false);
        ProjectPanel.setRequestFocusEnabled(false);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 153));
        jLabel1.setText("Title:");

        Title.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        Title.setPreferredSize(new java.awt.Dimension(404, 16));
        Title.setRequestFocusEnabled(false);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Creation Date:");

        creationDate.setPreferredSize(new java.awt.Dimension(124, 16));
        creationDate.setRequestFocusEnabled(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(255, 153, 102)));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setPreferredSize(new java.awt.Dimension(885, 52));
        jPanel4.setRequestFocusEnabled(false);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 102));
        jLabel4.setText("#URLs:");

        URLS.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        URLS.setText("1234");
        URLS.setPreferredSize(new java.awt.Dimension(37, 16));
        URLS.setRequestFocusEnabled(false);
        URLS.setVerifyInputWhenFocusTarget(false);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 102));
        jLabel5.setText("#Nodes:");

        nodes.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        nodes.setText("1234567890");
        nodes.setPreferredSize(new java.awt.Dimension(81, 16));
        nodes.setRequestFocusEnabled(false);

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 153, 102));
        jLabel7.setText("#Links:");

        links.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        links.setText("1234567890");
        links.setPreferredSize(new java.awt.Dimension(81, 16));
        links.setRequestFocusEnabled(false);

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 102));
        jLabel9.setText("#Imports:");

        imports.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        imports.setText("1234567890");
        imports.setPreferredSize(new java.awt.Dimension(81, 16));
        imports.setRequestFocusEnabled(false);

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("#Pass:");

        Pass.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        Pass.setForeground(new java.awt.Color(0, 153, 0));
        Pass.setText("1234567890");
        Pass.setPreferredSize(new java.awt.Dimension(81, 16));
        Pass.setRequestFocusEnabled(false);

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("#Test Scenarios:");

        Scenarios.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        Scenarios.setText("1234567890");
        Scenarios.setPreferredSize(new java.awt.Dimension(81, 16));
        Scenarios.setRequestFocusEnabled(false);

        jLabel15.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("#Fail:");

        Fail.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        Fail.setForeground(new java.awt.Color(255, 0, 0));
        Fail.setText("1234567890");
        Fail.setPreferredSize(new java.awt.Dimension(81, 16));
        Fail.setRequestFocusEnabled(false);

        jLabel17.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 153, 102));
        jLabel17.setText("#Media:");

        media.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        media.setText("1234567890");
        media.setPreferredSize(new java.awt.Dimension(81, 16));
        media.setRequestFocusEnabled(false);

        jLabel12.setBackground(new java.awt.Color(255, 153, 102));

        jLabel14.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 153));
        jLabel14.setText("#Form Items:");

        forms.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        forms.setText("123456");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(41, 41, 41)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(URLS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nodes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(links, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel14)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(forms, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(18, 18, 18)
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(Scenarios, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel9)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel11))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(Pass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(imports, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel17)
                    .add(jLabel15))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(media, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Fail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 211, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(URLS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5)
                    .add(nodes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7)
                    .add(links, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9)
                    .add(imports, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel17)
                    .add(media, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(Scenarios, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13)
                    .add(jLabel11)
                    .add(Pass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel12)
                    .add(jLabel14)
                    .add(forms)
                    .add(jLabel15)
                    .add(Fail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 924, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout ProjectPanelLayout = new org.jdesktop.layout.GroupLayout(ProjectPanel);
        ProjectPanel.setLayout(ProjectPanelLayout);
        ProjectPanelLayout.setHorizontalGroup(
            ProjectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ProjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(ProjectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, ProjectPanelLayout.createSequentialGroup()
                        .add(0, 44, Short.MAX_VALUE)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(Title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 510, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(101, 101, 101)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(creationDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ProjectPanelLayout.setVerticalGroup(
            ProjectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ProjectPanelLayout.createSequentialGroup()
                .add(ProjectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(creationDate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel3)
                    .add(Title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Websites"));
        jPanel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 51)));
        jPanel6.setPreferredSize(new java.awt.Dimension(947, 55));

        NewWebsiteButton.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        NewWebsiteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/plus.gif"))); // NOI18N
        NewWebsiteButton.setText("New");
        NewWebsiteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewWebsiteButtonActionPerformed(evt);
            }
        });

        RemoveWebsiteButton.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        RemoveWebsiteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/minus.gif"))); // NOI18N
        RemoveWebsiteButton.setText("Remove");
        RemoveWebsiteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveWebsiteButtonActionPerformed(evt);
            }
        });

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jSeparator6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(RemoveWebsiteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(NewWebsiteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, RemoveWebsiteButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, NewWebsiteButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator6))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(255, 102, 51)));

        WebsitesList.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 51)));
        WebsitesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "URL", "HTTP/HTTPS", "Authorization", "Last Run", "#Nodes", "#Links", "#Imports", "#Media", "#Forms", "#Scenarios", "Pass", "Fail"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        WebsitesList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        WebsitesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        WebsitesList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(WebsitesList);
        WebsitesList.getColumnModel().getColumn(0).setPreferredWidth(160);
        WebsitesList.getColumnModel().getColumn(1).setPreferredWidth(85);
        WebsitesList.getColumnModel().getColumn(2).setPreferredWidth(90);
        WebsitesList.getColumnModel().getColumn(3).setPreferredWidth(90);
        WebsitesList.getColumnModel().getColumn(4).setPreferredWidth(65);
        WebsitesList.getColumnModel().getColumn(5).setPreferredWidth(60);
        WebsitesList.getColumnModel().getColumn(6).setPreferredWidth(65);
        WebsitesList.getColumnModel().getColumn(7).setPreferredWidth(60);
        WebsitesList.getColumnModel().getColumn(8).setPreferredWidth(65);
        WebsitesList.getColumnModel().getColumn(9).setPreferredWidth(70);
        WebsitesList.getColumnModel().getColumn(10).setPreferredWidth(47);
        WebsitesList.getColumnModel().getColumn(11).setPreferredWidth(47);

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Execution"));

        jButton5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/go.gif"))); // NOI18N
        jButton5.setText(" Test execution");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/control-forward.gif"))); // NOI18N
        jButton6.setText("Test Script Loader");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        CurrentScript.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        CurrentScript.setForeground(new java.awt.Color(153, 0, 51));
        CurrentScript.setText("none");

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("Current Test Script:");

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .add(16, 16, 16)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(CurrentScript, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 347, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jSeparator10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jSeparator10)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButton6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7Layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(CurrentScript)
                            .add(jLabel2))
                        .add(0, 11, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButton5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Generation (Links /Forms)"));

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(255, 102, 51)));
        jPanel10.setPreferredSize(new java.awt.Dimension(872, 82));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        HierarcyCoverageList.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        HierarcyCoverageList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Branchs", "Specified Branch", " " }));

        jButton3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/database.gif"))); // NOI18N
        jButton3.setText(" Fetch Elements");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel8.setText("Fetch level");

        fetchingLevel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fetchingLevel.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prototest/TG_WEB/ui/two-docs.gif"))); // NOI18N
        jButton4.setText("Test Generation");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel6.setText("Fetch Time out");

        fetchTimeOut.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        fetchTimeOut.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel10.setText("Test Criteria");

        FormsCoverageList.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        FormsCoverageList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Item Types", "Text Inputs", "List Items", "Radio Items", " " }));

        buttonGroup1.add(LinksCriteria);
        LinksCriteria.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        LinksCriteria.setSelected(true);
        LinksCriteria.setText("Links");

        buttonGroup1.add(FormsCriteria);
        FormsCriteria.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        FormsCriteria.setText("Forms");

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel10Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fetchingLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fetchTimeOut, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(LinksCriteria)
                    .add(FormsCriteria))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(HierarcyCoverageList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(FormsCoverageList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator9)
                    .add(jSeparator8)
                    .add(jSeparator7)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel10Layout.createSequentialGroup()
                                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(fetchingLevel)
                                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, fetchTimeOut, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(jPanel10Layout.createSequentialGroup()
                                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(HierarcyCoverageList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel10)
                                    .add(LinksCriteria))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(FormsCoverageList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(FormsCriteria)))
                            .add(jPanel10Layout.createSequentialGroup()
                                .add(9, 9, 9)
                                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .add(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 940, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FileMenu.setText("File");
        FileMenu.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        NewProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        NewProjectMenuItem.setFont(FileMenu.getFont());
        NewProjectMenuItem.setText("New Project");
        NewProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewProjectMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(NewProjectMenuItem);
        FileMenu.add(jSeparator5);

        OpenProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        OpenProjectMenuItem.setFont(FileMenu.getFont());
        OpenProjectMenuItem.setText("Open Project");
        OpenProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenProjectMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(OpenProjectMenuItem);

        SaveProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        SaveProjectMenuItem.setFont(FileMenu.getFont());
        SaveProjectMenuItem.setText("Save Project");
        SaveProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveProjectMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(SaveProjectMenuItem);
        FileMenu.add(jSeparator1);

        CloseProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        CloseProjectMenuItem.setFont(FileMenu.getFont());
        CloseProjectMenuItem.setText("Close Project");
        CloseProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseProjectMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(CloseProjectMenuItem);
        FileMenu.add(jSeparator2);

        ExitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        ExitMenuItem.setFont(FileMenu.getFont());
        ExitMenuItem.setText("Exit");
        ExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(ExitMenuItem);

        jMenuBar1.add(FileMenu);

        ReportMenuItem.setText("Reports");
        ReportMenuItem.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        InvalidLinks.setFont(ReportMenuItem.getFont());
        InvalidLinks.setText("Invalid Links");
        InvalidLinks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InvalidLinksActionPerformed(evt);
            }
        });
        ReportMenuItem.add(InvalidLinks);
        ReportMenuItem.add(jSeparator4);

        TestCheckList.setFont(ReportMenuItem.getFont());
        TestCheckList.setText("Test Cases Check List");
        TestCheckList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestCheckListActionPerformed(evt);
            }
        });
        ReportMenuItem.add(TestCheckList);

        UrlScenarioList.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        UrlScenarioList.setText("Urls Scenarios List");
        UrlScenarioList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UrlScenarioListActionPerformed(evt);
            }
        });
        ReportMenuItem.add(UrlScenarioList);

        TestResults.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        TestResults.setText("Test Scenario Results");
        TestResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestResultsActionPerformed(evt);
            }
        });
        ReportMenuItem.add(TestResults);
        ReportMenuItem.add(jSeparator3);

        TestFormsRsults.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        TestFormsRsults.setText("Test Forms Results");
        TestFormsRsults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TestFormsRsultsActionPerformed(evt);
            }
        });
        ReportMenuItem.add(TestFormsRsults);

        jMenuBar1.add(ReportMenuItem);

        HelpMenu.setText("Help");
        HelpMenu.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        AboutMenuItem.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        AboutMenuItem.setText("About");
        AboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(AboutMenuItem);

        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, ProjectPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(ProjectPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1012, 697));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SaveProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveProjectMenuItemActionPerformed
       if ((this.currentProject != null) && (this.currentProject.HasChange(this.initialProject))){
                  try {
                      // Save data before close project
                      projectUpdate();  
                      saveProject();
                      this.initialProject = null;
                      this.initialProject = new Project(this.currentProject);
                      JOptionPane.showMessageDialog(null, "Changes is saved successfully.");
                      
                      displayProjectOnFrame();

                  } catch (IOException ex) {
                  }
           }            

    }//GEN-LAST:event_SaveProjectMenuItemActionPerformed

    private void CloseProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseProjectMenuItemActionPerformed
       if ((this.currentProject != null) && (this.currentProject.HasChange(initialProject))){
              if (JOptionPane.showConfirmDialog(null,"There are unsaved data, do you want to save before close project?",
                  "ProtoTest TG- Close Project ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                  try {
                      // Save data before close project
                     saveProject();
                     
                  } catch (IOException ex) {
                  }
              } 
           }
     this.currentProject = null;
     this.initialProject = null;        
     displayProjectOnFrame();
    }//GEN-LAST:event_CloseProjectMenuItemActionPerformed

    private void ExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuItemActionPerformed
       if ((this.currentProject != null) && (this.currentProject.HasChange(initialProject))){
          if (JOptionPane.showConfirmDialog(null,this.currentProject.getName() +" project is still open, want to save data before exist?",
              "ProtoTest TG- Leaving ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
              try {
                  // Save data before exist
                  projectUpdate();                    
                  saveProject();
              } catch (IOException ex) {
              }
          
          } 
       }
     System.exit(0);
    }//GEN-LAST:event_ExitMenuItemActionPerformed

    private void NewProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewProjectMenuItemActionPerformed
       if ((this.currentProject != null) && (this.currentProject.HasChange(initialProject))){
           if (JOptionPane.showConfirmDialog(null,this.currentProject.getName() +" project is still open, want to save data before open new project?",
              "ProtoTest TG- New Project ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
               try {
                   // Save data before get new project 
                 projectUpdate();  
                 saveProject();
               } catch (IOException ex) {
               }
              }            
        }
    this.currentProject = null;
    this.initialProject = null;
    displayProjectOnFrame();
    NewProject();  
    }//GEN-LAST:event_NewProjectMenuItemActionPerformed

    private void OpenProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenProjectMenuItemActionPerformed
       if ((this.currentProject != null) && (this.currentProject.HasChange(initialProject))){
           if (JOptionPane.showConfirmDialog(null,this.currentProject.getName() +" project is still open, want to save data before open a project?",
              "ProtoTest TG- Open ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
               try {
                   // Save data before open another project
                 projectUpdate();                     
                 saveProject();
               } catch (IOException ex) {
               }
              } 
        }
   this.currentProject = null;
   this.initialProject = null;
   displayProjectOnFrame();
   openProject();

    }//GEN-LAST:event_OpenProjectMenuItemActionPerformed

    private void RemoveWebsiteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveWebsiteButtonActionPerformed

        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) || (WebsitesList.getSelectedRow() < 0)){
         JOptionPane.showMessageDialog(null, "Please, open a project and select website first");
         return;
        }

        
            if (JOptionPane.showConfirmDialog(null,"Deleting "+ this.currentProject.getWebSites().get(WebsitesList.getSelectedRow()).getURL() +". Are you sure?",
              "ProtoTest TG- Remove Website ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){     
               this.currentProject.RemoveWebSite(this.currentProject.getWebSites().get(WebsitesList.getSelectedRow()));
               projectUpdate();
               displayProjectOnFrame();               
           }
        
    }//GEN-LAST:event_RemoveWebsiteButtonActionPerformed

    private void InvalidLinksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InvalidLinksActionPerformed

        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
           (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project, select website first and script file");
           return;
        }

            String Command = "\"" + currentProject.getProjectPath() + "\\" + CurrentScript.getText().trim() + "LinkStatus.txt\"" ;            
            try {
            Runtime.getRuntime().exec(new String[]{"notepad.exe",Command});

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Open invalid links report failed. Make sure you load  a test script file first!");              
            }

    }//GEN-LAST:event_InvalidLinksActionPerformed

    private void NewWebsiteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewWebsiteButtonActionPerformed
        if(this.currentProject != null){
           NewWebsite();
           projectUpdate();
           displayProjectOnFrame();               
        }       
    }//GEN-LAST:event_NewWebsiteButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) || (WebsitesList.getSelectedRow() < 0)){
           JOptionPane.showMessageDialog(null, "No Project is open or No website is selected, Please open a project and select website first");
           return;
        }

        if (fetchedweb != null) fetchedweb = null; 
        if(hierarcyTree != null) hierarcyTree = null;
        if(FormElements != null) FormElements = null;
         
         fetchedweb= new WebSite(this.currentProject.getWebSites().get(WebsitesList.getSelectedRow()));

        try {
            HierarcyProgressBar HPB = new HierarcyProgressBar(this,fetchedweb.getAddress(), fetchedweb.getUserName(), fetchedweb.getPassword() , "Unknown",
                                    (Integer.parseInt(fetchingLevel.getValue().toString())), (Integer.parseInt(fetchTimeOut.getValue().toString()) * 1000));
                HPB.setVisible(true);
            
                if (HPB.UI_Action){

                        // Get Links Tree
                        hierarcyTree = HPB.getRoot();
                        
                        // Get Forms Elements
                        FormElements = HPB.getFormElementsList();

                        
                        if (!((hierarcyTree == null) || (hierarcyTree.getChildCount() == 0))){                                                    

                        invalidLinks = HPB.getLinksStatus();

                        if (changedWeb != null) changedWeb = null; 
                           changedWeb= new WebSite(fetchedweb);
                           changedWeb.setLinks(HPB.getLinks());
                           changedWeb.setImports(HPB.getImports());
                           changedWeb.setMedia(HPB.getMedia());
                           changedWeb.setNodes( HPB.getLinks() + HPB.getImports() + HPB.getMedia());
                           Date date = new Date();
                           changedWeb.setRunDate(date);
                           JOptionPane.showMessageDialog(null, "Feching: " + fetchedweb.getURL() + " is done successfuly");
                       
                        } else {
                            
                        JOptionPane.showMessageDialog(null, "Feching: " + fetchedweb.getURL() + " is fail. Please, try again!");                            
                        
                       }  
                }
                
            HPB.dispose();

        } catch (IOException ex) {  
        } catch (InterruptedException ex) {
        }
            
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        
        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) || (WebsitesList.getSelectedRow() < 0)){
         JOptionPane.showMessageDialog(null, "Please, open a project and select website first");
         return;
        }
        

        
       if (LinksCriteria.isSelected()){ 

        // Check if we have fetched
        if ((hierarcyTree == null)||(fetchedweb == null)){
         JOptionPane.showMessageDialog(null, "You need to fetch the website before generating test cases");
         return;
        }   
           
           
           switch (HierarcyCoverageList.getSelectedIndex()){
            case 0: // All PAths
                    generateAllPaths(); 
                    break;                        
            case 1:  // Specified Path
                    generateSpecifiedPaths();                           
                    break;                         
            } //end of switch
       }
       

   if (FormsCriteria.isSelected()){ 
        generateTestFormItems(FormsCoverageList.getSelectedIndex());           
   } // end of test form generation
       
                        
    }//GEN-LAST:event_jButton4ActionPerformed
        
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
           (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project and select website first");
           return;
        }
            
            String Command1 = "\""+ currentProject.getProjectPath()+ "\\" + CurrentScript.getText().trim() + "Compile.bat\"" ;
            
            try {
                
                Runtime.getRuntime().exec(new String[]{Command1});
                
                           // delay the user by confirming dialog box 
                          if (JOptionPane.showConfirmDialog(null,"Script file has been compiled, do you like run it right now?",
                          "ProtoTest TG- Comfirm running ...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            String web = currentProject.getWebSites().get(WebsitesList.getSelectedRow()).getURL().toString(); 
                            
                            this.setState(Frame.ICONIFIED);

                            int certerion;
                            if (LinksCriteria.isSelected()) certerion = 0;
                            else certerion = 1;

                            Result result = TestRunner.runTest(web, currentProject.getProjectPath()+ "\\", CurrentScript.getText().trim(), certerion);
                            if (result != null){
                                currentProject.getWebSites().get(WebsitesList.getSelectedRow()).setScenarios(result.getRunCount());
                                currentProject.getWebSites().get(WebsitesList.getSelectedRow()).setPass(result.getRunCount() - result.getFailureCount() );
                                currentProject.getWebSites().get(WebsitesList.getSelectedRow()).setFail(result.getFailureCount());

                                projectUpdate();
                                saveProject();
                                displayProjectOnFrame();

                            }
                            
                            this.setState(Frame.NORMAL);    
                  }
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Cannot compile the shell script file");              
            }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) || (WebsitesList.getSelectedRow() < 0)){
           JOptionPane.showMessageDialog(null, "Please, open a project and select website first");
           return;
        }

          File path = new File(this.getWorkDir());
          JFileChooser chooser = new JFileChooser();
          chooser.setDialogTitle("PrototestTC- Load script file");              
          chooser.setCurrentDirectory(path);
          chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          FileNameExtensionFilter filter = new FileNameExtensionFilter("Java files", "java");
          chooser.setFileFilter(filter);
          chooser.setAcceptAllFileFilterUsed(false);
          if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
              String checkCompileFile = (FilenameUtils.removeExtension(currentProject.getProjectPath() + "\\" + chooser.getSelectedFile().getName())) + "Compile.bat";
              String checkRunFile = (FilenameUtils.removeExtension(currentProject.getProjectPath() + "\\" + chooser.getSelectedFile().getName())) + "Run.bat";

              if ((currentProject.getProjectPath().equals(chooser.getSelectedFile().getParent())) &&
                  (FileUtile.searchFile(checkCompileFile)) && (FileUtile.searchFile(checkRunFile))){

                  CurrentScript.setText(FilenameUtils.removeExtension(chooser.getSelectedFile().getName()));
                  
              } else
              {
                 JOptionPane.showMessageDialog(null, "Can not load this file becuase it is not related to the opening prject");
                 
              }
               
          }     

    }//GEN-LAST:event_jButton6ActionPerformed

    private void AboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuItemActionPerformed
        About ab = new About(this);
        ab.setVisible(true);
        ab.dispose();
    }//GEN-LAST:event_AboutMenuItemActionPerformed

    private void TestCheckListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestCheckListActionPerformed
        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
           (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project, select website first and script file");
           return;
        }

            String Command =  "\"" + currentProject.getProjectPath() + "\\" + CurrentScript.getText().trim() + "TestCheckList.txt\"" ;            
            try {
            Runtime.getRuntime().exec(new String[]{"notepad.exe",Command});

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Open Test Checklist failed. Make sure you load  a test script file first!");              
            }
    }//GEN-LAST:event_TestCheckListActionPerformed

    private void TestResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestResultsActionPerformed

        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
            (!LinksCriteria.isSelected())|| (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project, select website, Certerion, and script file");
           return;
        }

            String Command =  "\"" + currentProject.getProjectPath() + "\\" + CurrentScript.getText().trim() + "TestLinksResults.txt\"" ;            
            try {
            Runtime.getRuntime().exec(new String[]{"notepad.exe",Command});

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Open Test Results failed. Make sure you load and execute a test script file first!");              
            }

    }//GEN-LAST:event_TestResultsActionPerformed

    private void UrlScenarioListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UrlScenarioListActionPerformed
        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
           (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project, select website first and script file");
           return;
        }

            String Command =  "\"" + currentProject.getProjectPath() + "\\" + CurrentScript.getText().trim() + "TestUrlCheckList.txt\"" ;            
            try {
            Runtime.getRuntime().exec(new String[]{"notepad.exe",Command});

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Open Test Checklist failed. Make sure you load  a test script file first!");              
            }
    }//GEN-LAST:event_UrlScenarioListActionPerformed
    
    private void TestFormsRsultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TestFormsRsultsActionPerformed
        if((this.currentProject == null) || (this.currentProject.getWebSites().isEmpty()) ||
            (!FormsCriteria.isSelected())|| (WebsitesList.getSelectedRow() < 0) || (CurrentScript.getText().equals("none"))){
           JOptionPane.showMessageDialog(null, "Please, open a project, select website, Certerion, and script file");
           return;
        }

            String Command =  "\"" + currentProject.getProjectPath() + "\\" + CurrentScript.getText().trim() + "TestFormsResults.txt\"" ;            
            try {
            Runtime.getRuntime().exec(new String[]{"notepad.exe",Command});

            } catch (IOException ex) {
              JOptionPane.showMessageDialog(null, "Open Test Results failed. Make sure you load and execute a test script file first!");              
            }
    }//GEN-LAST:event_TestFormsRsultsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PrototestTG 1.0");
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrototestTC_Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrototestTC_Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrototestTC_Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrototestTC_Web.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrototestTC_Web().setVisible(true);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenuItem;
    private javax.swing.JMenuItem CloseProjectMenuItem;
    private javax.swing.JLabel CurrentScript;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JLabel Fail;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JComboBox FormsCoverageList;
    private javax.swing.JRadioButton FormsCriteria;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JComboBox HierarcyCoverageList;
    private javax.swing.JMenuItem InvalidLinks;
    private javax.swing.JRadioButton LinksCriteria;
    private javax.swing.JMenuItem NewProjectMenuItem;
    private javax.swing.JButton NewWebsiteButton;
    private javax.swing.JMenuItem OpenProjectMenuItem;
    private javax.swing.JLabel Pass;
    private javax.swing.JPanel ProjectPanel;
    private javax.swing.JButton RemoveWebsiteButton;
    private javax.swing.JMenu ReportMenuItem;
    private javax.swing.JMenuItem SaveProjectMenuItem;
    private javax.swing.JLabel Scenarios;
    private javax.swing.JMenuItem TestCheckList;
    private javax.swing.JMenuItem TestFormsRsults;
    private javax.swing.JMenuItem TestResults;
    private javax.swing.JLabel Title;
    private javax.swing.JLabel URLS;
    private javax.swing.JMenuItem UrlScenarioList;
    private javax.swing.JTable WebsitesList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel creationDate;
    private javax.swing.JSpinner fetchTimeOut;
    private javax.swing.JSpinner fetchingLevel;
    private javax.swing.JLabel forms;
    private javax.swing.JLabel imports;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel links;
    private javax.swing.JLabel media;
    private javax.swing.JLabel nodes;
    // End of variables declaration//GEN-END:variables
}

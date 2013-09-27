/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prototest.TG_WEB.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author melshalwy
 */
public class PercentageFrame extends JDialog {
    public boolean UI_Action;
    private double percentValue;
    private int numItems;
    private int TotalTestCases;

    /**
     * Creates new form PercentageFrame
     */
    
    public int getTotalTestCases() {
        return TotalTestCases;
    }

               
    public PercentageFrame(Frame parent, List<List<Object>> FormElements, int certerion) {
        super(parent, true);
        initComponents();
        
        this.getRootPane().setDefaultButton(CancelButton);
        this.setUndecorated(true);
               
        pack();
        Rectangle parentBounds = parent.getBounds();
        Dimension size = getSize();
        // Center in the parent
        int x = Math.max(0, parentBounds.x + (parentBounds.width - size.width) / 2);
        int y = Math.max(0, parentBounds.y + (parentBounds.height - size.height) / 2);
        setLocation(new Point(x, y));
        numItems = 0;
        TotalTestCases = 0;
        int radiosGroups = 0;
        int selectionGroups = 0;
        int areas = 0;
        int inputs = 0;
        int buttons = 0;
        int textIputs;
        
        switch (certerion){
            case 0: // All Item Types
                
               for(List<Object> form: FormElements){
                   
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
                   if(radiosGroups == 0) radiosGroups = 1;
                   if(selectionGroups == 0) selectionGroups = 1;
                   
                   if((areas == 0) && (inputs == 0)){
                       textIputs = 1;
                   } else {
                       textIputs = (areas + inputs);                                                      
                   }
                       
                                                                 
                  // if(buttons == 0) buttons = 1;

                   numItems = radiosGroups * selectionGroups * textIputs * buttons; 

                   break;  
                
            case 1:  // Text Items

               for(List<Object> form: FormElements){
                      
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
                   if((areas == 0) && (inputs == 0)){
                       textIputs = 1;
                   } else if (areas == 0){
                       textIputs = inputs * 2;
                   } else if (inputs == 0){
                       textIputs = areas * 2;                           
                   } else {
                       textIputs = (areas + inputs) * 2;                                                      
                   }
                       
                                                                 
                  // if(buttons == 0) buttons = 1;

                   numItems = textIputs * buttons; 
                
                break;  
                
            case 2:  // List Items
                
               for(List<Object> form: FormElements){
                                      
                   // Count Selection items                       
                   List<List<String>> selectionItems = ((List<List<String>>) form.get(2));
                   for(List<String> GS: selectionItems){
                   if (!GS.isEmpty()) selectionGroups++;   
                   }
   
                   // Count buttons items                       
                   List<String> ButtonItems = ((List<String>) form.get(5));
                   buttons += ButtonItems.size();
                   
               }  
                   if(selectionGroups == 0) selectionGroups = 1;
                                                                                    
                  // if(buttons == 0) buttons = 1;

                   numItems = selectionGroups * buttons; 

                
                  break;  
                
            case 3:  // Radio Items
                
               for(List<Object> form: FormElements){
                   
                   // Count Radio items
                   List<List<String>> RadioItems = ((List<List<String>>) form.get(1));
                   for(List<String> GR: RadioItems){
                   if (!GR.isEmpty()) radiosGroups++;   
                   }
                   
                   // Count buttons items                       
                   List<String> ButtonItems = ((List<String>) form.get(5));
                   buttons += ButtonItems.size();
                   
                   }  
                   if(radiosGroups == 0) radiosGroups = 1;
                                                                 
                  // if(buttons == 0) buttons = 1;

                   numItems = radiosGroups * buttons; 
                
                    break;     

            } //end of switch
        
        
        Total.setText(Integer.toString(numItems));
        SubTotal.setText(Integer.toString(numItems));
        TotalTestCases = numItems;
        Percentage.setValue(100);
        percentValue = 100;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Percentage = new javax.swing.JSpinner();
        SubTotal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        SelectButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Percentage"));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Cartesian product of all possible selections will be: ");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Randomly, ");

        Total.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        Total.setText("1234567890");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setText("%  , Number of test cases will be :");

        Percentage.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        Percentage.setModel(new javax.swing.SpinnerNumberModel(100, 1, 100, 1));
        Percentage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PercentageStateChanged(evt);
            }
        });

        SubTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        SubTotal.setText("1234567890");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(Total, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(Percentage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(SubTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(Total, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                            .add(jLabel2)
                            .addContainerGap())
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, Percentage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(SubTotal))
                        .addContainerGap())))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        SelectButton.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        SelectButton.setText("Select");
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });

        CancelButton.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(278, Short.MAX_VALUE)
                .add(CancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(SelectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(SelectButton)
                    .add(CancelButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

    }// </editor-fold>//GEN-END:initComponents

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        this.UI_Action = false;
        setVisible(false);
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectButtonActionPerformed
       this.UI_Action = true;
       setVisible(false);
    }//GEN-LAST:event_SelectButtonActionPerformed

    private void PercentageStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PercentageStateChanged

       percentValue = Double.parseDouble(Percentage.getValue().toString()) ;
       SubTotal.setText(Integer.toString((int) (numItems * (percentValue/100))));
       TotalTestCases = Integer.parseInt(SubTotal.getText());
        
    }//GEN-LAST:event_PercentageStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JSpinner Percentage;
    private javax.swing.JButton SelectButton;
    private javax.swing.JLabel SubTotal;
    private javax.swing.JLabel Total;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}

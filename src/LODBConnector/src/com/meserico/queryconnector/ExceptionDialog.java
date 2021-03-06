/*
    QueryConnector - Attach a query to a Calc document
    Copyright (C) 2013 Enrico Giuseppe Messina

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.meserico.queryconnector;

import java.awt.Dialog;

/**
 *
 * @author Enrico Messina
 */
public class ExceptionDialog extends JExtDialog {

    /**
     * Creates new form SQLErrorDialog
     */
    public ExceptionDialog(java.awt.Dialog parent, Exception ex) {
        super((Dialog) parent, false);
        initComponents();
        String msg = ex.getMessage();
        if(msg == null || msg.equals(""))
            msg = tr("EXCEPTION_MESSAGE_NOT_FOUND");
        this.errorText.setText(msg);
        this.detailedErrorText.setText(this.exceptionToString(ex));
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        detailButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        errorText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        detailContainer = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        detailedErrorText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Exception...");
        setResizable(false);

        mainContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mainContainer.setPreferredSize(new java.awt.Dimension(500, 150));
        mainContainer.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        closeButton.setText(tr("CLOSE_BUTTON")); // NOI18N
        closeButton.setPreferredSize(new java.awt.Dimension(120, 25));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel3.add(closeButton);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        detailButton.setText(tr("DETAILS_OPEN_BUTTON")); // NOI18N
        detailButton.setPreferredSize(new java.awt.Dimension(120, 25));
        detailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailButtonActionPerformed(evt);
            }
        });
        jPanel4.add(detailButton);

        jPanel2.add(jPanel4);

        mainContainer.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        errorText.setColumns(20);
        errorText.setEditable(false);
        errorText.setForeground(new java.awt.Color(204, 0, 0));
        errorText.setLineWrap(true);
        errorText.setRows(5);
        errorText.setWrapStyleWord(true);
        jScrollPane1.setViewportView(errorText);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setText(tr("EXCEPTION_OCCURRED")); // NOI18N
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        mainContainer.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainContainer, java.awt.BorderLayout.NORTH);

        detailContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        detailContainer.setPreferredSize(new java.awt.Dimension(476, 250));
        detailContainer.setLayout(new java.awt.BorderLayout());
        detailContainer.setVisible(false);

        detailedErrorText.setColumns(20);
        detailedErrorText.setEditable(false);
        detailedErrorText.setForeground(new java.awt.Color(204, 0, 0));
        detailedErrorText.setRows(5);
        jScrollPane2.setViewportView(detailedErrorText);

        detailContainer.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(detailContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void detailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailButtonActionPerformed
        this.detailContainer.setVisible(!this.detailContainer.isVisible());
        if(this.detailContainer.isVisible())
            this.detailButton.setText(tr("DETAILS_CLOSE_BUTTON"));
        else this.detailButton.setText(tr("DETAILS_OPEN_BUTTON"));
        this.setResizable(true);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(this.getParent());
    }//GEN-LAST:event_detailButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton detailButton;
    private javax.swing.JPanel detailContainer;
    private javax.swing.JTextArea detailedErrorText;
    private javax.swing.JTextArea errorText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainContainer;
    // End of variables declaration//GEN-END:variables

    public static void show(java.awt.Dialog parent, Exception ex){
        ExceptionDialog d = new ExceptionDialog(parent, ex);
        d.setVisible(true);
    }
    
    public static void show(Exception ex){
        ExceptionDialog.show(null, ex);
    }
}

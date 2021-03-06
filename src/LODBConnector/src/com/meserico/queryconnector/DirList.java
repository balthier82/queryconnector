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

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Enrico Messina
 */
public class DirList extends JExtList {

    /**
     * Creates new form DirList
     */
    public DirList() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void loadDir(File file, String filter){
        File[] files = file.listFiles(new DirFilter(filter));
        for(int i=0; i<files.length; i++)
            this.addItem(new FileItem(files[i]));
    }
    
    public File getSelectedFile(){
        Object obj = this.getSelectedValue();
        if(obj != null){
            FileItem item = (FileItem) obj;
            return item.file;
        }
        return null;
    }
    
    private class DirFilter implements FileFilter{
        private String filter;
        
        public DirFilter(String filter){
            this.filter = filter;
        }

        @Override
        public boolean accept(File file) {
            if(file.isDirectory())
                return false;
            return file.getName().toLowerCase().matches(filter);
        }
    }
    
    private class FileItem{
        private final File file;
        
        protected FileItem(File file){
            this.file = file;
        }
        
        @Override
        public String toString(){
            return file.getName();
        }
    }
}

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

/**
 *
 * @author Enrico Messina
 */
public class ODBCSource {
    private Object source;
    
    public ODBCSource(Object source){
        this.source = source;
    }
    
    public boolean isFile(){
        if(this.source == null)
            return false;
        return this.source instanceof File;
    }
    
    public boolean isValid(){
        return this.source != null;
    }
    
    public File asFile(){
        return (File) source;
    }
    
    @Override
    public String toString(){
        if(!this.isValid())
            return java.util.ResourceBundle.getBundle("com/meserico/queryconnector/languages").getString("NOT_SELECTED_ODBC_SOURCE_LABEL");
        if(this.source instanceof File)
            return ((File) this.source).getName();
        else return this.source.toString();
    }
}

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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Enrico Messina
 */
public class ResultSetTableModel extends DefaultTableModel {
    
    public ResultSetTableModel(ResultSet result){
        try{
            ResultSetMetaData metaData = result.getMetaData();
            int colCount = metaData.getColumnCount();
            for(int i=1; i<=colCount; i++){
                String colName = metaData.getColumnLabel(i);
                this.addColumn(colName);
            }
            while(result.next()){
                Vector row = new Vector();
                for(int i=1; i<=colCount; i++)
                    row.addElement(result.getObject(i));
                this.addRow(row);
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

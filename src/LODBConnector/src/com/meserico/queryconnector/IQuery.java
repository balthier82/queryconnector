/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meserico.queryconnector;

/**
 *
 * @author Enrico
 */
public interface IQuery {
    public String getName();
    public String getQuery();
    public String getDriverClass();
    public String getURL();
    public String getUsername();
    public String getPassword();
    public int getStartColumn();
    public int getStartRow();
    public int getColumnCount();
    public int getRowCount();
    public String getSheetID();
}

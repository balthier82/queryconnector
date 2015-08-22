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

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Enrico Messina
 */
public class DBConnection {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClass;
    
    private DBConnection(){
        this.username = "";
        this.password = "";
        this.jdbcUrl = "";
    }
    
    public DBConnection(ODBCSource source){
        if(!source.isValid())
            throw new RuntimeException("");
        else {
            this.driverClass = "sun.jdbc.odbc.JdbcOdbcDriver";
            this.jdbcUrl = "jdbc:odbc:";
            if(source.isFile()){
                IniFile ini = new IniFile(source.asFile());
                for(String name : ini.propertyNames()){
                    String value = ini.getProperty(name);
                    if(name.toLowerCase().equals("driver"))
                        value = "{"+value+"}";
                    this.jdbcUrl += name + "=" + value + ";";
                }
            }else
                this.jdbcUrl += source.toString();
        }
    }
    
    public DBConnection(String driverClass, String jdbcUrl, String username, String password){
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
    }
    
    public Connection getConnection(){
        try{
            Class.forName(this.driverClass);
            return DriverManager.getConnection(jdbcUrl, username, password);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public String getDriver() {
        return driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

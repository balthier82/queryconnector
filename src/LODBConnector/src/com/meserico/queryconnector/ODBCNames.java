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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Enrico Messina
 */
public class ODBCNames {
    
    private static final String ODBC_DATA_SOURCES = "ODBC Data Sources";
    
    public static enum Type {
        System("HKEY_LOCAL_MACHINE"),
        User("HKEY_CURRENT_USER");
        
        private String windowRegRoot;
        
        private Type(String w){
            this.windowRegRoot = w;
        }
        
        @Override
        public String toString(){
            if(OS.isWindows())
                return this.windowRegRoot;
            else return super.toString();
        }
    }
    
    public static boolean isSupportedOS(){
        return OS.isWindows() || OS.isUnix();
    }
    
    public static List<String> getSystemNames(){
        if(OS.isWindows())
            return getWindowODBCNames(Type.System);
        else if(OS.isUnix())
            return getUnixODBCNames(Type.System);
        else{
            System.out.println("Unsupported OS");
            return new ArrayList<String>();
        }
    }
    
    public static List<String> getUserNames(){
        if(OS.isWindows())
            return getWindowODBCNames(Type.User);
        else if(OS.isUnix())
            return getUnixODBCNames(Type.User);
        else{
            System.out.println("Unsupported OS");
            return new ArrayList<String>();
        }
    }
    
    private static List<String> getWindowODBCNames(Type type){
        try{
            String regPath = "\"" + type.toString() + "\\SOFTWARE\\ODBC\\ODBC.INI\\ODBC Data Sources\"";
            ProcessBuilder builder = new ProcessBuilder("reg","query",regPath);
            Process process = builder.start();
            BufferedReader stream = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String dsn;  
            List<String> dsnList = new ArrayList<String>();
            while((dsn=stream.readLine())!=null) {  
                if(dsn.indexOf(type.toString()) == -1){
                    String parts[] = dsn.split(" ");
                    String dsnName = extractDSNName(parts);
                    if(dsnName != null)
                        dsnList.add(dsnName);
                }
            }  
            return dsnList;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private static List<String> getUnixODBCNames(Type type){
        if(unixCommandExists("odbc_config")){
            try{
                String path;
                if(type.equals(Type.System)){
                    String ret = execCommand("odbc_config", "--odbcini");
                    if(ret == null)
                        throw new RuntimeException("Execution of odbc_config command failed. Cannot obtain system *.ini config file.");
                    path = ret;
                }else {
                    path = System.getProperty("user.home") + File.separator + ".odbc.ini";
                }
                List<String> dsnList = new ArrayList<String>();
                if(new File(path).exists()){
                    IniFile ini = new IniFile(path);
                    if(ini.hasMappedProperties(ODBC_DATA_SOURCES)){
                        Properties sources = ini.getMappedProperties(ODBC_DATA_SOURCES);
                        for(String key : sources.stringPropertyNames())
                            dsnList.add(key);
                    }
                }
                return dsnList;
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }else
            throw new RuntimeException("Command iodbc-config does not exists. iODBC must be installed.");
    }
    
    private static boolean unixCommandExists(String command){
        try{
            ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c","type -P " + command);
            Process process = builder.start();
            process.waitFor();
            return process.exitValue() == 0;
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    private static String execCommand(String cmd, String... args){
        try{
            List<String> cmds = new ArrayList<String>();
            cmds.add(cmd);
            cmds.addAll(Arrays.asList(args));
            ProcessBuilder builder = new ProcessBuilder(cmds);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();
            return line;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private static String extractDSNName(String[] list){
        int i=0; 
        String dsnName = "";
        for(; i<list.length; i++){
            if(!list[i].trim().equals("REG_SZ"))
                dsnName +=  " " + list[i];
            else break;
        }
        return dsnName.trim();
    }
}

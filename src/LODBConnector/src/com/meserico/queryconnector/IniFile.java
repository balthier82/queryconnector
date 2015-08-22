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

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Enrico Messina
 */
public class IniFile {
    
    private Properties props;
    private Map<String, Properties> mappedProperties;
    
    public IniFile(){
        super();
        this.props = new Properties();
        this.mappedProperties = new HashMap<String, Properties>();
    }
    
    public IniFile(File file){
        this();
        try{
            FileInputStream ifile = new FileInputStream(file.getAbsolutePath());
            this.load(ifile);        
            ifile.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public IniFile(String filepath){
        this(new File(filepath));
    }
    
    public final void load(InputStream input){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            Properties subProperties = null;
            while((line = reader.readLine()) != null){
                if(line.startsWith("[")){
                    String name = line.substring(1, line.length()-1);
                    subProperties = new Properties();
                    this.mappedProperties.put(name.trim(), subProperties);
                }else{
                    int idx = line.indexOf("=");
                    if(idx != -1){
                        String name = line.substring(0, idx).trim();
                        String value = line.substring(idx+1, line.length()).trim();
                        props.setProperty(name, value);
                        if(subProperties != null)
                            subProperties.setProperty(name, value);
                    }
                }
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public boolean hasMappedProperties(String mappedName){
        return this.getMappedProperties(mappedName) != null;
    }
    
    public Set<String> getMappedPropertyNames(){
        return this.mappedProperties.keySet();
    }
    
    public Properties getMappedProperties(String mappedName){
        return this.mappedProperties.get(mappedName);
    }
    
    public String getMappedProperty(String mappedName, String property){
        Properties p = this.getMappedProperties(mappedName);
        if(p != null)
            return p.getProperty(property);
        return null;
    }
    
    public String getProperty(String name){
        return this.props.getProperty(name);
    }
    
    public Set<String> propertyNames(){
        return this.props.stringPropertyNames();
    }
}

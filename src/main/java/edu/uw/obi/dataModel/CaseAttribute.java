/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.obi.dataModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author wlau
 */
public class CaseAttribute {
    private String name;
    private String value;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CaseAttribute(String name, String value, int type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public void build(JsonObjectBuilder jbuilder)
    {
            if ((value == null) || value.trim().equals("")) {
                jbuilder.add(name.startsWith("Z_") ? name.replaceAll("Z_", "_").toLowerCase() : name, "");
                } else if ((type == java.sql.Types.DOUBLE)
                    || (type == java.sql.Types.FLOAT)) {
                jbuilder.add(name.startsWith("Z_") ? name.replaceAll("Z_", "_").toLowerCase() : name, Double.parseDouble(value));
            } else if ((type == java.sql.Types.SMALLINT)
                    || (type == java.sql.Types.BIGINT)
                    || (type == java.sql.Types.INTEGER)) {
                jbuilder.add(name.startsWith("Z_") ? name.replaceAll("Z_", "_").toLowerCase() : name, Integer.parseInt(value));

            } else if ((type == java.sql.Types.DATE) || (type == java.sql.Types.TIMESTAMP)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                jbuilder.add(name.startsWith("Z_") ? name.replaceAll("Z_", "_").toLowerCase() : name,  LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).toString());

            } else {
                jbuilder.add(name.startsWith("Z_") ? name.replaceAll("Z_", "_").toLowerCase() : name, value);
            }
        
        
    }
    
    
}

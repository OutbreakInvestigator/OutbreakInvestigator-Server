/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.obi.dataModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.Json;

/**
 *
 * @author wlau
 */
public class Case extends HashMap {

//    public List<CaseAttribute> caseAttributes;
//
    public Case() {
//        this.caseAttributes = new ArrayList();
        super();
    }
//
//    public Case(List<CaseAttribute> caseAttributes) {
//        this.caseAttributes = caseAttributes;
//    }

    public Case add(String key, CaseAttribute value) {
        this.put(key, value);
        return this;
    }

    public JsonObjectBuilder build() throws ParseException {
        JsonObjectBuilder jbuilder = Json.createObjectBuilder().add("_id", Integer.parseInt(((CaseAttribute) this.get("DBID")).getValue()))
                .add("_type", "vertex");

        ArrayList<CaseAttribute> caseAttributes = new ArrayList<CaseAttribute>(this.values());
        for (int idx = 0; idx < caseAttributes.size(); idx++) {
//            String columnLabel = caseAttributes.get(idx).getName();
//            if ((caseAttributes.get(idx).getValue() == null) || caseAttributes.get(idx).getValue().trim().equals("")) {
//                jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, "");
//            } else if ((caseAttributes.get(idx).getType() == java.sql.Types.DOUBLE)
//                    || (caseAttributes.get(idx).getType() == java.sql.Types.FLOAT)) {
//                jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, Double.parseDouble(caseAttributes.get(idx).getValue()));
//            } else if ((caseAttributes.get(idx).getType() == java.sql.Types.SMALLINT)
//                    || (caseAttributes.get(idx).getType() == java.sql.Types.BIGINT)
//                    || (caseAttributes.get(idx).getType() == java.sql.Types.INTEGER)) {
//                jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, Integer.parseInt(caseAttributes.get(idx).getValue()));
//
//            } else if ((caseAttributes.get(idx).getType() == java.sql.Types.DATE) || (caseAttributes.get(idx).getType() == java.sql.Types.TIMESTAMP)) {
//                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//                jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel,  LocalDate.parse(caseAttributes.get(idx).getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).toString());
//
//            } else {
//                jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, caseAttributes.get(idx).getValue());
//            }
            caseAttributes.get(idx).build(jbuilder);
        }
        return jbuilder;
    }

}

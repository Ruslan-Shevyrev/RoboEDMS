/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.roboedms.connect.ConnectToPostgres;
import org.roboedms.settings.Setting;
import org.roboedms.valueobject.ListValue;

/**
 *
 * @author shrv
 */
public class AnswerSql {

    private final String SQLINSERTDOC = "select * from edms.delo_table where is_answer=false and ref_id is not null and answer is not null and group_id=?";
    private final String SQlUPDATE = "UPDATE edms.delo_table SET is_answer = true WHERE id = ?";

    public ArrayList<ListValue> answer() {
        ArrayList<ListValue> listValues = new ArrayList<ListValue>();
        ListValue value;
        try (Connection conEDMS = ConnectToPostgres.getConnection()) {
            PreparedStatement csInsertDoc = conEDMS.prepareStatement(SQLINSERTDOC);
            csInsertDoc.setLong(1, Setting.orgID);
            ResultSet rs = csInsertDoc.executeQuery();
            while (rs.next()) {
                value = new ListValue();
                value.annotat = rs.getString("annotat");
                value.type_doc = rs.getString("type_doc");
                if ("Ис".equals(value.type_doc)) {
                    value.date = rs.getString("corr_date_doc");
                    value.doc_numb = rs.getString("corr_doc_numb");
                } else {
                    value.date = rs.getString("date_doc");
                    value.doc_numb = rs.getString("doc_numb");
                }
                value.answer = rs.getString("answer");
                value.id_doc = rs.getLong("id");
                listValues.add(value);
            }
        } catch (SQLException ex) {
            Logger.getLogger(insert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listValues;
    }

    public void update(Long id) {
        try (Connection conEDMS = ConnectToPostgres.getConnection()) {
            PreparedStatement psUpdateDoc = conEDMS.prepareStatement(SQlUPDATE);
            psUpdateDoc.setLong(1, id);
            psUpdateDoc.execute();
        } catch (SQLException ex) {
            Logger.getLogger(insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

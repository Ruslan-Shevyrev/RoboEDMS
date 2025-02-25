/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.roboedms.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.roboedms.connect.ConnectToOracle;
import org.roboedms.connect.ConnectToPostgres;
import org.roboedms.settings.FixTwinDoc;
import org.roboedms.settings.Setting;

/**
 *
 * @author shrv
 */
public class insert {

    // private final String SQLINSERTDOC = "{ ? = call edms.test_delo_function (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private final String SQLINSERTDOC = "INSERT INTO edms.delo_table ( \n"
            + "type_doc, \n"
            + "date_doc, \n"
            + "access, \n"
            + "composition, \n"
            + "delivery, \n"
            + "plandate, \n"
            + "annotat, \n"
            + "corr, \n"
            + "doc_numb, \n"
            + "signer, \n"
            + "text_doc, \n"
            + "author_errand,\n"
            + "corr_date_doc,\n"
            + "corr_doc_numb,\n"
            + "group_id) \n"
            + "VALUES(\n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?, \n"
            + "?,\n"
            + "?,\n"
            + "?,\n"
            + "?,\n"
            + "?)\n"
            + "returning id;";

    private final String SQLINSERTFILE = "INSERT INTO edms.delo_files\n"
            + "(id, blob_content, id_docs, file_name)\n"
            + "VALUES(nextval('test_id'::regclass), ?, ?, ?);";

    private final String SQLSELECTBLOB = "SELECT contents FROM delo.file_contents2 WHERE isn_ref_file = ?";

    private final String SQLSELECTFILENAME = "SELECT description FROM delo.ref_file WHERE isn_ref_file = ?";

    public void insertSql(String type_doc,
            String date_doc,
            String access,
            String composition,
            String delivery,
            String plandate,
            String annotat,
            String corr,
            String docNumb,
            String signer,
            String textDoc,
            String author_errand,
            String corr_date_doc,
            String corr_doc_numb,
            ArrayList<String> filesId) {
        try (Connection conEDMS = ConnectToPostgres.getConnection(); Connection conDelo = ConnectToOracle.getConnection()) {
            if (!((FixTwinDoc.Olddate_doc == null ? date_doc == null : FixTwinDoc.Olddate_doc.equals(date_doc))
                    && (FixTwinDoc.Oldaccess == null ? access == null : FixTwinDoc.Oldaccess.equals(access)) 
                    && (FixTwinDoc.Oldcomposition == null ? composition == null : FixTwinDoc.Oldcomposition.equals(composition))
                    && (FixTwinDoc.Olddelivery == null ? delivery == null : FixTwinDoc.Olddelivery.equals(delivery))
                    && (FixTwinDoc.Oldplandate == null ? plandate == null : FixTwinDoc.Oldplandate.equals(plandate))
                    && (FixTwinDoc.Oldannotat == null ? annotat == null : FixTwinDoc.Oldannotat.equals(annotat))
                    && (FixTwinDoc.Oldcorr == null ? corr == null : FixTwinDoc.Oldcorr.equals(corr))
                    && (FixTwinDoc.OlddocNumb == null ? docNumb == null : FixTwinDoc.OlddocNumb.equals(docNumb))
                    && (FixTwinDoc.Oldsigner == null ? signer == null : FixTwinDoc.Oldsigner.equals(signer))
                    && (FixTwinDoc.OldtextDoc == null ? textDoc == null : FixTwinDoc.OldtextDoc.equals(textDoc))
                    && (FixTwinDoc.Oldauthor_errand == null ? author_errand == null : FixTwinDoc.Oldauthor_errand.equals(author_errand))
                    && (FixTwinDoc.Oldcorr_date_doc == null ? corr_date_doc == null : FixTwinDoc.Oldcorr_date_doc.equals(corr_date_doc))
                    && (FixTwinDoc.Oldcorr_doc_numb == null ? corr_doc_numb == null : FixTwinDoc.Oldcorr_doc_numb.equals(corr_doc_numb)))) {
                PreparedStatement psSelectDelo;
                PreparedStatement csInsertDoc = conEDMS.prepareStatement(SQLINSERTDOC);
                csInsertDoc.setString(1, type_doc);
                csInsertDoc.setString(2, date_doc);
                csInsertDoc.setString(3, access);
                csInsertDoc.setString(4, composition);
                csInsertDoc.setString(5, delivery);
                csInsertDoc.setString(6, plandate);
                csInsertDoc.setString(7, annotat);
                csInsertDoc.setString(8, corr);
                csInsertDoc.setString(9, docNumb);
                csInsertDoc.setString(10, signer);
                csInsertDoc.setString(11, textDoc);
                csInsertDoc.setString(12, author_errand);
                csInsertDoc.setString(13, corr_date_doc);
                csInsertDoc.setString(14, corr_doc_numb);
                csInsertDoc.setLong(15, Setting.orgID);
                ResultSet rsId = csInsertDoc.executeQuery();
                int newid = 0;
                if (rsId.next()) {
                    newid = rsId.getInt("id");
                }
                //int newid = csInsertDoc.getInt(1);
                for (String file : filesId) {
                    psSelectDelo = conDelo.prepareStatement(SQLSELECTBLOB);
                    int fileId = Integer.parseInt(file);
                    psSelectDelo.setInt(1, fileId);
                    ResultSet rsFileBlob = psSelectDelo.executeQuery();
                    psSelectDelo = conDelo.prepareStatement(SQLSELECTFILENAME);
                    psSelectDelo.setInt(1, fileId);
                    ResultSet rsFileName = psSelectDelo.executeQuery();
                    while (rsFileBlob.next()) {
                        PreparedStatement psInsertFile = conEDMS.prepareStatement(SQLINSERTFILE);
                        psInsertFile.setBinaryStream(1, rsFileBlob.getBinaryStream("CONTENTS"));
                        psInsertFile.setInt(2, newid);
                        if (rsFileName.next()) {
                            psInsertFile.setString(3, rsFileName.getString("DESCRIPTION"));
                        } else {
                            psInsertFile.setString(3, "no_Name");
                        }
                        psInsertFile.execute();
                    }
                }

                FixTwinDoc.Olddate_doc = date_doc;
                FixTwinDoc.Oldaccess = access;
                FixTwinDoc.Oldcomposition = composition;
                FixTwinDoc.Olddelivery = delivery;
                FixTwinDoc.Oldplandate = plandate;
                FixTwinDoc.Oldannotat = annotat;
                FixTwinDoc.Oldcorr = corr;
                FixTwinDoc.OlddocNumb = docNumb;
                FixTwinDoc.Oldsigner = signer;
                FixTwinDoc.OldtextDoc = textDoc;
                FixTwinDoc.Oldauthor_errand = author_errand;
                FixTwinDoc.Oldcorr_date_doc = corr_date_doc;
                FixTwinDoc.Oldcorr_doc_numb = corr_doc_numb;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(insert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

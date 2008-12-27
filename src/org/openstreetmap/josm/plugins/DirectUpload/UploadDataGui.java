/*
 * UploadDataGui.java
 *
 * Created on August 17, 2008, 6:56 PM
 * Copyright by Subhodip Biswas
 * This program is free software and licensed under GPL.
 */

package org.openstreetmap.josm.plugins.DirectUpload;
import java.awt.event.ItemEvent;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.io.GpxWriter;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.GpxLayer;
import org.openstreetmap.josm.gui.MapView;

/**
 *
 * @author  subhodip
 */
public class UploadDataGui extends javax.swing.JFrame {
 
 String tagging;   
 String Descriptionfield;  
 String urlDescription;
 String urlTags;
 String UserName;
 String PassWord;
 //GpxData gpxData;
         
        public static final String API_VERSION = "0.5";
        private static final String BASE64_ENC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        private static final String BOUNDARY = "----------------------------d10f7aa230e8";
        private static final String LINE_END = "\r\n";
     
        boolean dfield;
        boolean taggy;
        boolean choosy;
        boolean user;
        boolean pass;
        boolean publicheck;
     
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();    
        String datename = dateFormat.format(date);
  
    /** Creates new form UploadDataGui */
    public UploadDataGui() {
        initComponents();
    }

  
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        OutputDisplay = new javax.swing.JTextArea();
        OkButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        PublicTrace = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        descriptionfield = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tagfield = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        OutputDisplay.setColumns(20);
        OutputDisplay.setRows(5);
        jScrollPane1.setViewportView(OutputDisplay);

        OkButton.setText("Ok");
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
        jLabel2.setText("Direct Upload to OpenStreetMap");

        PublicTrace.setText("Public");
        PublicTrace.setToolTipText("Selected makes your trace public in openstreetmap.org");
        PublicTrace.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PublicTraceItemStateChanged(evt);
            }
        });

        jLabel1.setText("Description");

        descriptionfield.setToolTipText("Please enter Description about your trace.");
        descriptionfield.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descriptionfieldFocusLost(evt);
            }
        });

        jLabel3.setText("Tags");

        tagfield.setToolTipText("Please enter tags about your trace.");
        tagfield.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tagfieldFocusLost(evt);
            }
        });


    
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.fill = 1;
    contentPane.add(OutputDisplay, c);

    c.gridwidth = 1;
    c.gridy = 1;
    contentPane.add(jLabel3, c);
    c.gridx = 1;
    contentPane.add(tagfield, c);

    c.gridy = 2; c.gridx = 0;
    contentPane.add(jLabel1, c);
    c.gridx = 1;
    contentPane.add(descriptionfield, c);

    c.gridy = 3; c.gridx = 0;
    contentPane.add(jLabel2, c);
    c.gridx = 1;
    contentPane.add(PublicTrace, c);

    c.gridy = 4; c.gridx = 0;
    contentPane.add(CancelButton, c);
    c.gridx = 1;
    contentPane.add(OkButton, c);

        pack();
    }// </editor-fold>//GEN-END:initComponents



public void upload(String username, String password, String Descriptionfield, String tagging , GpxData gpxData ) throws IOException {
        OutputDisplay.setText("Starting to upload selected file to openstreetmap.org");
    
    try {
        
        urlDescription = Descriptionfield.replaceAll("\\.;&?,/","_");
        if (urlDescription == null || urlDescription.length() == 0) {
             OutputDisplay.setText("No description provided .Please provide some description . For the time being ignore the exception error ");
           
        }
           urlTags = tagging.replaceAll("\\\\.;&?,/","_");
      
       
        URL url = new URL("http://www.openstreetmap.org/api/" + API_VERSION + "/gpx/create");
            System.err.println("url: " + url);
            OutputDisplay.setText("Uploading in Progress");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setConnectTimeout(15000);
        connect.setRequestMethod("POST");
        connect.setDoOutput(true);
        connect.addRequestProperty("Authorization", "Basic "+encodeBase64(username+":"+password));
        connect.addRequestProperty("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
        connect.addRequestProperty("Connection", "close"); // counterpart of keep-alive
        connect.addRequestProperty("Expect", "");
        connect.connect();
        DataOutputStream out  = new DataOutputStream(new BufferedOutputStream(connect.getOutputStream()));
        writeContentDispositionGpxData(out, "file", gpxData);
        writeContentDisposition(out, "description", urlDescription);
        writeContentDisposition(out, "tags", urlTags);
           if(publicheck) {
        System.out.println(publicheck);
        writeContentDisposition(out, "public", "1");
           } else {
        writeContentDisposition(out, "public", "0");   
           }
        out.writeBytes("--" + BOUNDARY + "--" + LINE_END);
        out.flush();
      
        int returnCode = connect.getResponseCode();
            String returnMsg = connect.getResponseMessage();
            System.err.println(returnCode);
            OutputDisplay.setText(returnMsg);
        if (returnCode != 200) {  
            if (connect.getHeaderField("Error") != null)
               returnMsg += "\n" + connect.getHeaderField("Error");
            connect.disconnect();
       }
        out.close();
        connect.disconnect();
       
        } catch(UnsupportedEncodingException ignore) { 
        } catch (MalformedURLException e) {
                OutputDisplay.setText("Cant Upload .");
                e.printStackTrace();
    }
  
}



 public static String getPreferencesDir() {
    if (System.getenv("APPDATA") != null)
       return System.getenv("APPDATA")+"/JOSM/";
       return System.getProperty("user.home")+"/.josm/";
    }

private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
     GpxData gpxData; 
       
        UserName  =  Main.pref.get("osm-server.username");   
        PassWord =   Main.pref.get("osm-server.password");
        
        if(Main.map == null || Main.map.mapView == null ||Main.map.mapView.getActiveLayer() == null ||
                !(Main.map.mapView.getActiveLayer() instanceof GpxLayer)){
                JOptionPane.showMessageDialog(Main.parent,
                tr("No GpxLayer selected. Cannot upload a trace.")
                );
                return;
        }
        gpxData = ((GpxLayer)Main.map.mapView.getActiveLayer()).data;
                System.out.println(Descriptionfield);
        try { 
                upload(UserName,PassWord,Descriptionfield ,tagging ,gpxData) ;
        } catch (IOException ex) {
                Logger.getLogger(UploadDataGui.class.getName()).log(Level.SEVERE, null, ex);
    }     
    
}//GEN-LAST:event_OkButtonActionPerformed

private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        dispose();
}//GEN-LAST:event_CancelButtonActionPerformed

private void PublicTraceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PublicTraceItemStateChanged

    if (evt.getStateChange() == ItemEvent.SELECTED) {
        publicheck = true;
         
    }
}//GEN-LAST:event_PublicTraceItemStateChanged

private void descriptionfieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionfieldFocusLost

     JTextField Descsel = (JTextField)evt.getSource();
        Descriptionfield =  Descsel.getText();
    
}//GEN-LAST:event_descriptionfieldFocusLost

private void tagfieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tagfieldFocusLost

     JTextField Tagsel = (JTextField)evt.getSource();
        tagging =  Tagsel.getText();
    
}//GEN-LAST:event_tagfieldFocusLost


    private void writeContentDisposition(DataOutputStream out, String name, String value) throws IOException {
        out.writeBytes("--" + BOUNDARY + LINE_END);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_END);
        out.writeBytes(LINE_END);
        out.writeBytes(value + LINE_END);
        
    }

    private void writeContentDispositionGpxData(DataOutputStream out, String name, GpxData gpxData ) throws IOException {
    
        out.writeBytes("--" + BOUNDARY + LINE_END);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + datename +".gpx" + "\"" + LINE_END);
        //out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + gpxData.storageFile.getName() + "\"" + LINE_END);
        out.writeBytes("Content-Type: application/octet-stream" + LINE_END);
        out.writeBytes(LINE_END);
          
        OutputDisplay.setText("Transferring data to server");
            new GpxWriter(out).write(gpxData);
        out.flush();
        out.writeBytes(LINE_END); 
     
    }
    
 // Taken from Christof Dallermassal java class :  
public String encodeBase64(String s) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < (s.length()+2)/3; ++i) {
            int l = Math.min(3, s.length()-i*3);
            String buf = s.substring(i*3, i*3+l);
            out.append(BASE64_ENC.charAt(buf.charAt(0)>>2));
            out.append(BASE64_ENC.charAt((buf.charAt(0) & 0x03) << 4 | (l==1?0:(buf.charAt(1) & 0xf0) >> 4)));
            out.append(l>1 ? BASE64_ENC.charAt((buf.charAt(1) & 0x0f) << 2 | (l==2 ? 0 : (buf.charAt(2) & 0xc0) >> 6)) : '=');
            out.append(l>2 ? BASE64_ENC.charAt(buf.charAt(2) & 0x3f) : '=');
           
        }
        return out.toString();
        }    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton OkButton;
    private javax.swing.JTextArea OutputDisplay;
    private javax.swing.JCheckBox PublicTrace;
    private javax.swing.JTextField descriptionfield;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tagfield;
    // End of variables declaration//GEN-END:variables

}
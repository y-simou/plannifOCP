/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import jxl.read.biff.BiffException;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.Readxl;

/**
 *
 * @author Yassine.SIMOU
 */
@Named(value = "xlController")
@SessionScoped
public class XlController implements Serializable {

    /**
     * Creates a new instance of XlController
     */
    private File xlFile;
    private FileUpload upFile;
    private UploadedFile file;
    @EJB
    private Readxl facade;
    int panelCtr = 0;
    int trenchCtr = 0;
    int parcelCtr = 0;
    int layerCtr = 0;
    
    public void copyFile(String fileName, InputStream in) {
           try {
              
              
                // write the inputStream to a FileOutputStream
                OutputStream out = new FileOutputStream(new File(fileName));
              
                int read = 0;
                byte[] bytes = new byte[1024];
              
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
              
                in.close();
                out.flush();
                out.close();
              
                System.out.println("New file created!");
                } catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }

    public File handleFileUpload() throws IOException {
        File destFile = new File(file.getFileName());
        copyFile(destFile.getName(),file.getInputstream());
//        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //This is used for new file creation.
//        File f = new File(servletContext.get, file.getFileName());
        return destFile;
    }

    public void database() {
        Long rows = 0L;
        try {
            rows = facade.read(handleFileUpload());
        } catch (IOException | BiffException ex) {
            Logger.getLogger(XlController.class.getName()).log(Level.SEVERE, null, ex);
        }
        panelCtr = facade.panelConteur;
        parcelCtr = facade.parcelConteur;
        trenchCtr = facade.trenchConteur;
        layerCtr = facade.layerConteur;
        String msg = "";
        msg += rows + " Rows Executed.\n";
        msg += panelCtr + " Panels Added. " + trenchCtr + " Trenchs Added. " + parcelCtr + " Parcels Added. " + layerCtr + " Layers, Chemical Component Layer Added.";
        showMessage(msg);
    }

    public void showMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", msg));
    }

    public void showErrorMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "WARNING !", msg));
    }

    public XlController() {
    }

    public File getXlFile() {
        return xlFile;
    }

    public void setXlFile(File xlFile) {
        this.xlFile = xlFile;
    }

    public Readxl getFacade() {
        return facade;
    }

    public FileUpload getUpFile() {
        return upFile;
    }

    public void setUpFile(FileUpload upFile) {
        this.upFile = upFile;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}

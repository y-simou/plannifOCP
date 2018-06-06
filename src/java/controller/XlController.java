/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.Annee;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;
import service.Readxl;

import org.primefaces.event.FileUploadEvent;

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
    private UploadedFile file;
    @EJB
    private Readxl facade;
    private Annee annee;
    int panelCtr = 0;
    int trenchCtr = 0;
    int parcelCtr = 0;
    int layerCtr = 0;

    public void handleFileUpload() {
        showMessage("Succesful "+ file.getFileName() + " is uploaded.");
    }

    public void database() {
        Long rows = 0L;
        try {
            rows = facade.read(file.getInputstream(),getAnnee().getAnnee());
        } catch (IOException | BiffException ex) {
            Logger.getLogger(XlController.class.getName()).log(Level.SEVERE, null, ex);
        }
        panelCtr = Readxl.panelConteur;
        parcelCtr = Readxl.parcelConteur;
        trenchCtr = Readxl.trenchConteur;
        layerCtr = Readxl.layerConteur;
        String msg = rows + " Rows Executed.\n";
        msg += panelCtr + " Panels Added. " + trenchCtr + " Trenchs Added. " + parcelCtr + " Parcels Added. " + layerCtr + " Layers, " + layerCtr*5 + " Chemical Component Layer Added.";
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

    public Readxl getFacade() {
        return facade;
    }

    public Annee getAnnee() {
        if (annee == null) {
            annee = new Annee();
        }
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}

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
        showMessage("Succesful " + file.getFileName() + " is uploaded.");
    }


    public void database() {
        try {
            Long rows = facade.read(file.getInputstream(), getAnnee().getAnnee());
            if (rows == null) {
                showErrorMessage("the File is Big,\n max is 3500 Rows");
            } else {
                String msg = rows + " Rows Executed.";
                showMessage(msg);
                msg = Readxl.panelConteur + " Panels Added. " + Readxl.trenchConteur + " Trenchs Added. \n" + Readxl.parcelConteur + " Parcels Added. " + Readxl.layerConteur + " Layers, \n" + Readxl.layerConteur * 5 + " Chemical Component Layer Added.";
                showMessage(msg);
            }
        } catch (IOException | BiffException ex) {
            Logger.getLogger(XlController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

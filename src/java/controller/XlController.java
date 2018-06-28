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
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
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
    private UploadedFile fileGraphe;
    @EJB
    private Readxl ejbFacade;
    private Annee annee;
    private LineChartModel lineChartModel;
    private HorizontalBarChartModel horizontalBarModel;
    public static BarChartModel barModel;

    public void handleFileUpload() {
        showMessage("Succesful " + file.getFileName() + " is uploaded.");
    }

    public void database() {
        try {
            Long size = ejbFacade.read(file.getInputstream(), getAnnee().getAnnee());
            if (size == null) {
                showErrorMessage("the File is Big,\n max is 3500 Rows");
            } else {
                String msg = size + " Rows Executed.";
                showMessage(msg);
                msg = Readxl.panelConteur + " Panels Added. " + Readxl.trenchConteur + " Trenchs Added. \n" + Readxl.parcelConteur + " Parcels Added. " + Readxl.layerConteur + " Layers, \n" + Readxl.layerConteur * 5 + " Chemical Component Layer Added.";
                showMessage(msg);
            }
        } catch (IOException | BiffException ex) {
            Logger.getLogger(XlController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void afficherChart() {
        createLineModels();
    }
    

    private void createLineModels() {

        try {
            lineChartModel = ejbFacade.createLineChart(fileGraphe.getInputstream());
        } catch (IOException ex) {
            System.out.println("error of reading Graphe file");
        }

        lineChartModel.setTitle("Cumulative Layer Feed Curve");
        lineChartModel.setLegendPosition("ne");
        lineChartModel.setShowPointLabels(true);
        lineChartModel.getAxes().put(AxisType.X, new CategoryAxis("Période(Hours)"));
        Axis yAxis = lineChartModel.getAxis(AxisType.Y);
        yAxis.setLabel("Volume m3");
        yAxis.setMin(0);
        yAxis.setMax(Readxl.max.multiply(new BigDecimal(1.1)));
        System.out.println("max::"+Readxl.max);
        Axis xAxis = lineChartModel.getAxis(AxisType.X);
        xAxis.setMin(0);
        
        barModel.setTitle("Date de fin au plut tot du gerbage");
        barModel.setLegendPosition("ne");
        barModel.setShowPointLabels(true);
        barModel.getAxes().put(AxisType.X, new CategoryAxis("Période(Hours)"));
        yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Volume m3");
        yAxis.setMin(0);
        yAxis.setMax(Readxl.max1.multiply(new BigDecimal(1.1)));
        System.out.println("max::"+Readxl.max1);
        xAxis = barModel.getAxis(AxisType.X);
        xAxis.setMin(0);
        barModel.setBarMargin(1);
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
        return ejbFacade;
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

    public UploadedFile getFileGraphe() {
        return fileGraphe;
    }

    public void setFileGraphe(UploadedFile fileGraphe) {
        this.fileGraphe = fileGraphe;
    }

    public LineChartModel getLineCharModel() {
        if (lineChartModel == null) {
            lineChartModel = new LineChartModel();
        }
        return lineChartModel;
    }

    public void setLineCharModel(LineChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }
    
//    public HorizontalBarChartModel getHorizontalBarModel() {
//        if (horizontalBarModel == null) {
//           horizontalBarModel = ejbFacade.createHorizontalBarModel();
//        }
//        return horizontalBarModel;
//    }
//
//    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
//        this.horizontalBarModel = horizontalBarModel;
//    }

    public BarChartModel getBarModel() {
        if (barModel == null) {
            barModel = new BarChartModel();
        }
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        XlController.barModel = barModel;
    }
}

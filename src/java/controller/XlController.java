/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.Annee;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import jxl.read.biff.BiffException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.timeline.TimelineModel;
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
    public static BarChartModel barModel;
    public static TimelineModel timeLineModel;
    public static XSSFSheet sheet;
    private Date start;
    private Date end;
    private String machineFilter;
    private String operationFilter;
    private Date dateDebut;
    private List<String> machines;
    private List<String> operations = new ArrayList();

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

    public void filterGantt() {
        System.out.println("hi 1");
        System.out.println("op :: " + getOperationFilter() + " ma :: " + getMachineFilter());
        if (sheet == null) {
            System.out.println("op ::  ma :: ");
            showMessage("Please Choose an Excel File");
        } else {
            try {
                ejbFacade.filterGantt(sheet, getOperationFilter(), getMachineFilter(), dateDebut);
//                RequestContext context = RequestContext.getCurrentInstance();
//                context.update("timeline");
            } catch (IOException ex) {
                showErrorMessage("error of reading Graphe file");
            }
        }
    }

    public void afficherChart() throws IOException {
        if (sheet == null) {
            try {
                InputStream excelFile = fileGraphe.getInputstream();
                XSSFWorkbook wb = new XSSFWorkbook(excelFile);
                sheet = wb.getSheetAt(0);
            } catch (IOException ex) {
                Logger.getLogger(XlController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        createLineModels();
    }

    private void createLineModels() {
        System.out.println("op :: " + operationFilter + " ma :: " + machineFilter);
        if (fileGraphe == null) {
            showMessage("Please Choose an Excel File");
        } else {
            try {
                lineChartModel = ejbFacade.createLineChart(sheet, operationFilter, machineFilter, dateDebut);
            } catch (IOException ex) {
                showErrorMessage("error of reading Graphe file");
            }

            ///////////////////Line Chart/////////////////
            lineChartModel.setTitle("Cumulative Layer Feed Curve");
            lineChartModel.setLegendPosition("ne");
            lineChartModel.getAxes().put(AxisType.X, new CategoryAxis("Période"));
            Axis yAxis = lineChartModel.getAxis(AxisType.Y);
            yAxis.setLabel("Volume m3");
            yAxis.setMin(0);
            yAxis.setMax(Readxl.max.multiply(new BigDecimal(1.1)));
            System.out.println("max::" + Readxl.max);
            Axis xAxis = lineChartModel.getAxis(AxisType.X);
            xAxis.setTickAngle(-60);

            ///////////////////Bar Chart/////////////////
            barModel.setTitle("Date de fin au plut tot du gerbage");
            barModel.setLegendPosition("ne");
            barModel.getAxes().put(AxisType.X, new CategoryAxis("Période"));
            yAxis = barModel.getAxis(AxisType.Y);
            yAxis.setLabel("Volume m3");
            yAxis.setMin(0);
            yAxis.setMax(Readxl.max1.multiply(new BigDecimal(1.1)));
            System.out.println("max::" + Readxl.max1);
            xAxis = barModel.getAxis(AxisType.X);
            xAxis.setTickAngle(-60);
            barModel.setBarMargin(1);
            lineChartModel.setExtender("extLegend");
            barModel.setExtender("extLegend");

            /////////////////// Time Line Chart (Gantt) ///////////////
            // set initial start / end dates for the axis of the timeline  
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            Date now = dateDebut;

            cal.setTimeInMillis(now.getTime());
            start = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, 7);
            end = cal.getTime();
        }
    }

    public void onSelect(TimelineSelectEvent e) {
        System.out.println("onclick");
        showMessage(e.getTimelineEvent().getData() + "");
    }

    public void initOperations() {
        operations.add("Amenagement Foration");
        operations.add("Amenagement Decapage");
        operations.add("Chargement");
        operations.add("Decapage");
        operations.add("Foration");
        operations.add("Gerbage");
        operations.add("Sautage");
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

    public BarChartModel getBarModel() {
        if (barModel == null) {
            barModel = new BarChartModel();
        }
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        XlController.barModel = barModel;
    }

    public TimelineModel getTimeLineModel() {
        return timeLineModel;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getMachineFilter() {
        return machineFilter;
    }

    public void setMachineFilter(String machineFilter) {
        this.machineFilter = machineFilter;
    }

    public String getOperationFilter() {
        return operationFilter;
    }

    public void setOperationFilter(String operationFilter) {
        this.operationFilter = operationFilter;
    }

    public List<String> getMachines() {
        if (machines == null) {
            return Readxl.allMachines;
        }
        return machines;
    }

    public void setMachines(List<String> machines) {
        this.machines = machines;
    }

    public List<String> getOperations() {
        if (operations.isEmpty()) {
            initOperations();
            System.out.println("operations :: "+ operations.toString());
        }
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

}

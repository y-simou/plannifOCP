/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.InputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

import bean.ChemicalComponent;
import bean.Layer;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.SubPanel;
import bean.Trench;
import controller.XlController;
import controller.util.MathUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jxl.read.biff.BiffException;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class Readxl {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    @EJB
    private PanelFacade panelFacade;
    @EJB
    private SubPanelFacade subpanelFacade;
    @EJB
    private TrenchFacade trenchFacade;
    @EJB
    private ParcelFacade parcelFacade;
    @EJB
    private LevelLayerFacade levelLayerFacade;
    @EJB
    private LayerFacade layerFacade;
    @EJB
    private ChemicalComponentFacade chemicalComponentFacade;
    @EJB
    private ChemicalComponentLayerFacade chemicalComponentLayerFacade;

    public static int panelConteur;
    public static int trenchConteur;
    public static int parcelConteur;
    public static int layerConteur;
    public static BigDecimal max;
    public static BigDecimal max1;

//    public HorizontalBarChartModel createHorizontalBarModel() {
//        HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();
////        horizontalBarModel.setExtender("chartExtender");
//
//        ChartSeries boys = new ChartSeries();
//        boys.setLabel("Boys");
//        boys.set("2004", 50);
//        boys.set("2005", 96);
//        boys.set("2006", 0);
////        horizontalBarModel.setSeriesColors("#fff");
//
//        ChartSeries girls = new ChartSeries();
//        girls.setLabel("Girls");
//        girls.set("2004", 0);
//        girls.set("2005", 60);
//        girls.set("2006", 82);
//
//        horizontalBarModel.addSeries(boys);
//        horizontalBarModel.addSeries(girls);
//        
//
//        horizontalBarModel.setTitle("Horizontal and Stacked");
//        horizontalBarModel.setStacked(true);
//
//        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
//        xAxis.setLabel("Births");
//        xAxis.setMin(0);
//        xAxis.setMax(200);
//
//        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Gender");
//horizontalBarModel.setExtender("chartExtender");
//        return horizontalBarModel;
//    }
////courbe d'alimentation
    public LineChartModel createLineChart(InputStream inputStream) throws IOException {

        LineChartModel model = new LineChartModel();
        BarChartModel barModel = new BarChartModel();

        InputStream excelFile = inputStream;
        XSSFWorkbook wb = new XSSFWorkbook(excelFile);
        XSSFSheet s = wb.getSheetAt(1);
        Iterator<Row> rows = s.rowIterator();
        BigDecimal maxim;
        max = new BigDecimal(0);
        max1 = new BigDecimal(0);
        XSSFRow row = (XSSFRow) rows.next();
        //Size
        int size = 0;
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            if (row.getCell(0) != null) {
                size++;
            }
        }
        ////// Simulation Time list => times
        rows = s.rowIterator();
        row = (XSSFRow) rows.next();
        BigDecimal[] times = new BigDecimal[size];
        for (int j = 0; j < size; j++) {
            row = (XSSFRow) rows.next();
            if (row.getCell(0) != null) {
                times[j] = new BigDecimal(row.getCell(0).toString());
            }
        }

        // Results     
        for (int i = 1; i < 32; i = i + 2) {
            BigDecimal[] resultas = new BigDecimal[size];
            //retour au debut par rowIterator
            rows = s.rowIterator();
            row = (XSSFRow) rows.next();
            ChartSeries cumul = new ChartSeries();
            ChartSeries barSerie = new ChartSeries();
            BigDecimal previous = new BigDecimal(0);
            BigDecimal barVal;
            Double value;

            if (row.getCell(i) != null) {
                cumul.setLabel("Cumul " + row.getCell(i).toString());
                barSerie.setLabel(row.getCell(i).toString());
                System.out.println(cumul.getLabel());
                int j = 0;
                for (int k = 0; k < size; k++) {
                    row = (XSSFRow) rows.next();

                    //LineChart
                    if (row.getCell(i) != null && !"".equals(row.getCell(i).toString())) {
                        System.out.println(row.getCell(i));
                        value = new Double(row.getCell(i).toString());
                        resultas[j] = previous.add(new BigDecimal(value));
                    } else {
                        resultas[j] = previous;
                    }
                    cumul.set(times[j], resultas[j]);
                    previous = resultas[j];
                    

                    //BarChart
                    if (row.getCell(i) != null && !"".equals(row.getCell(i).toString())) {
                        barVal = new BigDecimal(row.getCell(i).toString());
                        barSerie.set(times[j], barVal);
                        max1 = barVal.compareTo(max1) == 1 ? barVal : max1;
                    }
                    j++;
                }
                maxim = MathUtil.calculerMax(resultas);
                max = maxim.compareTo(max) == 1 ? maxim : max;
                System.out.println("max :::::" + max);
                if (maxim.compareTo(new BigDecimal(0)) == 1) {
                    model.addSeries(cumul);
                    System.out.println("added : " + i);
                    barModel.addSeries(barSerie);
                }

            }
        }

        //Bar Model//////////////////////////////
//        BarChartModel barModel = new BarChartModel();
//        rows = s.rowIterator();
//        row = (XSSFRow) rows.next();
//        //get all bars's labels
//        List<String> labels = new ArrayList();
//        String prevLabel = "";
//        for (int i = 1; i < 32; i = i + 2) {
//            if (row.getCell(i) != null && !"".equals(row.getCell(i).toString())) {
//                System.out.println(i + " :: " + row.getCell(i).toString());
//                labels.add(row.getCell(i).toString());
//            }
//        }
//        BigDecimal value;
//        max1 = new BigDecimal(0);
//        for (int j = 0; j < size; j++) {
//
//            row = (XSSFRow) rows.next();
//            for (int i = 1; i < 32; i = i + 2) {
//                if (row.getCell(i) != null && !"".equals(row.getCell(i).toString())) {
//                    ChartSeries serie = new ChartSeries();
//                    if (!labels.get((i - 1) / 2).equals(prevLabel)) {
//                        serie.setLabel(labels.get((i - 1) / 2));
//                        prevLabel = labels.get((i - 1) / 2);
//                    }
//                    value = new BigDecimal(row.getCell(i).toString());
//                    serie.set(times, value);
//                    barModel.addSeries(serie);
//                    max1 = value.compareTo(max1) == 1 ? value : max1;
//                }
//            }
//        }
        XlController.barModel = barModel;

        return model;
    }

    public Long read(InputStream inputStream, Long annee) throws IOException, BiffException {

        Long rowsConteur = 0L;
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        Layer layer;
        String valeur, cellYear, year = "";
        panelConteur = trenchConteur = parcelConteur = layerConteur = 0;
        ChemicalComponent bpl = chemicalComponentFacade.findByNom("bp1");
        ChemicalComponent co2 = chemicalComponentFacade.findByNom("co2");
        ChemicalComponent mgo = chemicalComponentFacade.findByNom("mgo");
        ChemicalComponent sio2 = chemicalComponentFacade.findByNom("sio2");
        ChemicalComponent cd = chemicalComponentFacade.findByNom("cd");

        try {
            InputStream excelFile = inputStream;
            XSSFWorkbook wb = new XSSFWorkbook(excelFile);
            XSSFSheet s = wb.getSheetAt(0);
            XSSFRow row;
            Long size = new Long(s.getLastRowNum());
            if (size > 3600) {
                return null;
            }
            Iterator<Row> rows = s.rowIterator();

            row = (XSSFRow) rows.next();

            //subpanel don't exist in the excel so i create a subpanel with id 1 ,i need subpanel to create parcel 
            SubPanel subpanel = subpanelFacade.find(1L);
            if (subpanel == null) {
                subpanel = new SubPanel(subpanelFacade.generateId("SubPanel", "id"));
            }

            if (annee != null) {
                year = annee.toString();
            }
            while (rows.hasNext()) {

                row = (XSSFRow) rows.next();
                rowsConteur++;

                if (row.getCell(18) == null) {
                    cellYear = "";
                } else {
                    cellYear = row.getCell(18).toString();
                }

                if (year.equals(cellYear)) {
                    System.out.println("haaa row   ::::::::  " + row.getRowNum());

                    //geting value of cell 0 and creating panel if not exist
                    if (row.getCell(0) != null) {
                        valeur = row.getCell(0).toString();
                    } else {
                        valeur = "";
                    }

                    panel = panelFacade.createNull(valeur);

                    //geting value of cell 1 and creating trench if not exist
                    if (row.getCell(1) != null) {
                        valeur = row.getCell(1).toString();
                    } else {
                        valeur = "";
                    }

                    trench = trenchFacade.createNull(valeur, panel);

                    //geting value of cell 2 and creating parcel if not exist
                    if (row.getCell(2) != null) {
                        valeur = row.getCell(2).toString();
                    } else {
                        valeur = "";
                    }

                    parcel = parcelFacade.createNull(valeur, trench, subpanel);

                    //level don't exist in the excel so i create a level depend on parcel id 
                    level = levelLayerFacade.createNull(parcel);

                    //geting value of cell 7 and creating layer if not exist
                    if (row.getCell(7) != null) {
                        valeur = row.getCell(7).toString();
                    } else {
                        valeur = "";
                    }
                    layer = layerFacade.createNull(valeur, level);

                    //the cell != null and has only numbers
                    if (row.getCell(13) != null && row.getCell(13).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(13).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, bpl, new BigDecimal(new Double(row.getCell(13).toString())));
                        }
                    }

                    if (row.getCell(14) != null && row.getCell(14).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(14).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, co2, new BigDecimal(new Double(row.getCell(14).toString().replaceAll(",", "."))));
                        }
                    }

                    if (row.getCell(15) != null && row.getCell(15).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(15).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, mgo, new BigDecimal(new Double(row.getCell(15).toString().replaceAll(",", "."))));
                        }
                    }

                    if (row.getCell(16) != null && row.getCell(16).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(16).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, sio2, new BigDecimal(new Double(row.getCell(16).toString().replaceAll(",", "."))));
                        }
                    }

                    if (row.getCell(17) != null && row.getCell(17).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(17).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, cd, new BigDecimal(new Double(row.getCell(17).toString().replaceAll(",", "."))));
                        }
                    }
                }
            }

        } catch (IOException e) {
        }
        return rowsConteur;
    }
}

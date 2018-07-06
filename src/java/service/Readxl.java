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
import controller.util.DateUtil;
import controller.util.MathUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jxl.read.biff.BiffException;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

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
    public static List<String> allMachines;

    public LineChartModel createLineChart(XSSFSheet sheet, String o, String m, Date dateSimulation) throws IOException {

        LineChartModel model = new LineChartModel();
        BarChartModel barModel = new BarChartModel();
        TimelineModel timeLineModel = new TimelineModel();

        // groups for time line (Gantt)
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        List<String> machines = new ArrayList(),times = new ArrayList(), operations = new ArrayList(); // "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage"
        Collections.addAll(operations, "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage");
        List<List<Integer>> debutOperations = new ArrayList<List<Integer>>();
        List<Integer> tableDebutOperations;
        String machine, machineFilter = m, operationFilter = o, operation;
        int indexOfMachine, hours, dateDebut, size = 0;
        double dbHours;
        Date debut;

        XSSFSheet s = sheet;
        XlController.sheet = s;
        Iterator<Row> rows = s.rowIterator();
        BigDecimal maxim;
        max = max1 = new BigDecimal(0);
        XSSFRow row = (XSSFRow) rows.next();

        //Size and Simulation Time list => times
        Long lignes = 0L;
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            if (row.getCell(0) != null) {
                lignes++;
                if (row.getCell(6) != null && row.getCell(5) != null && row.getCell(7) != null) {

                    machine = row.getCell(7).toString();
                    operation = row.getCell(6).toString();
                    if (m == null) {
                        machineFilter = machine;
                    }
                    if (o == null) {
                        operationFilter = operation;
                    }

                    cal.setTime(dateSimulation); // sets calendar time/date au debut de gantt
                    cal.add(Calendar.HOUR_OF_DAY, (int) (double) new Double(row.getCell(0).toString()));// Date debut operation
                    debut = cal.getTime();
                    if (operation.equals("Gerbage") && row.getCell(5).toString().equals("Fin")) {
                        times.add(size, (new SimpleDateFormat("yyyy-MM-dd")).format(debut));
                        size++;
                    }

                    //time line Chart////////////
                    if (machine.equals(machineFilter) && operation.equals(operationFilter)) {
                        dbHours = new Double(row.getCell(0).toString());
                        hours = (int) dbHours;
                        indexOfMachine = machines.indexOf(machine);
                        if (indexOfMachine != -1) {
                            dateDebut = debutOperations.get(indexOfMachine).get(operations.indexOf(operation));
                            if (row.getCell(5).toString().equals("Fin") && !operation.equals("") && dateDebut != -1) {
                                // create timeline model
                                cal.setTime(dateSimulation); // sets calendar time/date au debut de gantt
                                cal.add(Calendar.HOUR_OF_DAY, dateDebut);// Date debut operation
                                debut = cal.getTime();
                                //date fin
                                cal.setTime(dateSimulation); // sets calendar time/date
                                cal.add(Calendar.HOUR_OF_DAY, hours); // adds hours
                                cal.getTime(); // returns new date object .. in the future
                                Date fin = cal.getTime();

                                //legende of event
                                String title = operation + "(" + (hours - dateDebut) + "H) " + " - P" + ((int) (double) new Double(row.getCell(1).toString())) + " T" + ((int) (double) new Double(row.getCell(2).toString())) + " C" + ((int) (double) new Double(row.getCell(3).toString()));

                                // create an event with content, start / end dates, editable flag, group name and custom style class  
                                TimelineEvent event = new TimelineEvent(title, debut, fin, false, machine, operation.toLowerCase().replaceAll(" ", ""));
                                timeLineModel.add(event);
                                //delete the start date
                                debutOperations.get(indexOfMachine).set(operations.indexOf(operation), -1);
                            } else if (row.getCell(5).toString().equals("Debut")) {
                                debutOperations.get(indexOfMachine).set(operations.indexOf(operation), hours);
                            }
                        } else {
                            indexOfMachine = machines.size();
                            machines.add(indexOfMachine, machine);
                            tableDebutOperations = new ArrayList();
                            for (int i = 0; i < 7; i++) {
                                tableDebutOperations.add(-1);
                            }
                            debutOperations.add(tableDebutOperations);
                            if (row.getCell(5).toString().equals("Debut")) {
                                debutOperations.get(indexOfMachine).set(operations.indexOf(operation), hours);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Lignes ::" + lignes);
        System.out.println("size ::" + size);

        // Results     
        BigDecimal[][] lineResulta = new BigDecimal[16][size], barResulta = new BigDecimal[16][size];
        List<BigDecimal> resultas = new ArrayList();

        //retour au debut par rowIterator
        rows = s.rowIterator();
        row = (XSSFRow) rows.next();
        BigDecimal[] previous = new BigDecimal[16];
        String[] labels = new String[16];
        BigDecimal result, barVal;
        Double value;

        for (int i = 33; i < 49; i++) {
            if (row.getCell(i) != null) {
                labels[i - 33] = "Cumul " + row.getCell(i).toString();
                previous[i - 33] = new BigDecimal(0);
                resultas.add(i - 33, new BigDecimal(0));
            }
        }

        int t = 0;
        for (int j = 0; j < lignes; j++) {
            row = (XSSFRow) rows.next();
            int k = 0; //t index of the size and k index of chartSerie
            if (row.getCell(6) != null && row.getCell(6).toString().equals("Gerbage") && row.getCell(5) != null && row.getCell(5).toString().equals("Fin")) {
                //LineChart
                for (int i = 33; i < 49; i++) {
                    if (row.getCell(i) != null && !"".equals(row.getCell(i).toString())) {
                        value = new Double(row.getCell(i).toString());
                        result = new BigDecimal(value);
                        barVal = new BigDecimal(value);
                        result = previous[k].add(result);
                    } else {
                        barVal = new BigDecimal(0);
                        result = previous[k];
                    }
                    lineResulta[k][t] = result;
                    barResulta[k][t] = barVal;
                    resultas.set(k, result);
                    //the cumul
                    previous[k] = result;
                    k++;
                }
                t++;
            }

        }

        System.out.println(resultas);
        if (resultas.size() > 0) {
            maxim = MathUtil.calculerMax(resultas);
            max = maxim.compareTo(max) == 1 ? maxim : max;
        }
        for (int i = 0; i < 16; i++) {
            ChartSeries chartSerie = new ChartSeries();
            ChartSeries barSerie = new ChartSeries();
            chartSerie.setLabel(labels[i]);
            barSerie.setLabel(labels[i]);
            //add all points to the serie i
            for (int j = 0; j < size; j++) {
                chartSerie.set(times.get(j), lineResulta[i][j]);
                barVal = barResulta[i][j];
                barSerie.set(times.get(j), barVal);
                max1 = barVal.compareTo(max1) == 1 ? barVal : max1;
            }
            if (previous[i].compareTo(new BigDecimal(0)) == 1) {
                model.addSeries(chartSerie);
                barModel.addSeries(barSerie);
            }

        }
        allMachines = machines;
        XlController.barModel = barModel;
        XlController.timeLineModel = timeLineModel;

        return model;
    }

    public void filterGantt(XSSFSheet sheet, String o, String m, Date dateSimulation) throws IOException {
        TimelineModel timeLineModel = new TimelineModel();

        // groups for time line (Gantt)
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        List<String> machines = new ArrayList();
        List<String> operations = new ArrayList(); // "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage"
        Collections.addAll(operations, "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage");
        List<List<Integer>> debutOperations = new ArrayList<List<Integer>>();
        List<Integer> tableDebutOperations;
        String machine, machineFilter = m, operationFilter = o, operation;
        int indexOfMachine, hours, dateDebut;
        double dbHours;
        Date debut;

        XSSFSheet s = sheet;
        Iterator<Row> rows = s.rowIterator();
        XSSFRow row = (XSSFRow) rows.next();

        //Size and Simulation Time list => times
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            if (row.getCell(0) != null) {
                machine = row.getCell(7).toString();
                operation = row.getCell(6).toString();
                if (m == null) {
                    machineFilter = machine;
                }
                if (o == null) {
                    operationFilter = operation;
                }
                //time line Chart////////////
                if (row.getCell(0) != null && row.getCell(5) != null && row.getCell(6) != null && row.getCell(7) != null && machine.equals(machineFilter) && operation.equals(operationFilter)) {
                    dbHours = new Double(row.getCell(0).toString());
                    hours = (int) dbHours;
                    indexOfMachine = machines.indexOf(machine);
                    if (indexOfMachine != -1) {
                        dateDebut = debutOperations.get(indexOfMachine).get(operations.indexOf(operation));
                        if (row.getCell(5).toString().equals("Fin") && !operation.equals("") && dateDebut != -1) {
                            // create timeline model  
                            //date debut

                            cal.setTime(dateSimulation); // sets calendar time/date au debut de gantt
                            cal.add(Calendar.HOUR_OF_DAY, dateDebut);// Date debut operation
                            debut = cal.getTime();
                            //date fin
                            cal.setTime(dateSimulation); // sets calendar time/date
                            cal.add(Calendar.HOUR_OF_DAY, hours); // adds hours
                            cal.getTime(); // returns new date object .. in the future
                            Date fin = cal.getTime();

                            //legende of event
                            String title = operation + "(" + (hours - dateDebut) + "H) " + " - P" + ((int) (double) new Double(row.getCell(1).toString())) + " T" + ((int) (double) new Double(row.getCell(2).toString())) + " C" + ((int) (double) new Double(row.getCell(3).toString()));

                            // create an event with content, start / end dates, editable flag, group name and custom style class  
                            TimelineEvent event = new TimelineEvent(title, debut, fin, false, machine, operation.toLowerCase().replaceAll(" ", ""));
//                            System.out.println(":::::::::::::::::::::::::" + operation + "::::::::::::::::::::::::::::::::: " + machine);
                            timeLineModel.add(event);
                            //delete the start date
                            debutOperations.get(indexOfMachine).set(operations.indexOf(operation), -1);
                        } else if (row.getCell(5).toString().equals("Debut")) {
                            debutOperations.get(indexOfMachine).set(operations.indexOf(operation), hours);
                        }
                    } else {
                        indexOfMachine = machines.size();
                        machines.add(indexOfMachine, machine);
                        tableDebutOperations = new ArrayList();
                        for (int i = 0; i < 7; i++) {
                            tableDebutOperations.add(-1);
                        }
                        debutOperations.add(tableDebutOperations);
                        if (row.getCell(5).toString().equals("Debut")) {
                            debutOperations.get(indexOfMachine).set(operations.indexOf(operation), hours);
                        }
                    }
                }
            }
        }
        XlController.timeLineModel = timeLineModel;
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

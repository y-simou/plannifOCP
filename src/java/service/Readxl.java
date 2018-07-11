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
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.SubPanel;
import bean.Trench;
import controller.XlController;
import controller.util.DateUtil;
import controller.util.MathUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import jxl.write.Label;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
        List<String> machines = new ArrayList(), times = new ArrayList(), operations = new ArrayList(); // "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage"
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
        XSSFRow row;

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
        XlController.timeLineModelCtrl = timeLineModel;

        return model;
    }

    public TimelineModel filterGantt(TimelineModel timelineModelCtrl, String o, String m, Date dateSimulation) throws IOException {
        TimelineModel timeLineModel = new TimelineModel();

        String machine, machineFilter = m, operationFilter = o, operation;
        for (int i = 0; i < timelineModelCtrl.getEvents().size(); i++) {
            TimelineEvent event = timelineModelCtrl.getEvent(i);
            machine = event.getGroup();
            operation = event.getStyleClass();
            if (m == null) {
                machineFilter = machine;
            }
            if (o == null) {
                operationFilter = operation;
            }
            if (event.getGroup().equals(machineFilter) && event.getStyleClass().equals(operationFilter)) {
                timeLineModel.add(event);
            }
        }

        return timeLineModel;
    }

    public Long read(InputStream inputStream, Long annee) throws IOException, BiffException {

        Long rowsConteur = 0L;
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        String valeur, cellYear, year = "";
        Double puissance, surface, volume, thc, taux;
        panelConteur = trenchConteur = parcelConteur = layerConteur = 0;
        ChemicalComponent bpl = chemicalComponentFacade.findByNom("bpl");
        ChemicalComponent co2 = chemicalComponentFacade.findByNom("co2");
        ChemicalComponent mgo = chemicalComponentFacade.findByNom("mgo");
        ChemicalComponent sio2 = chemicalComponentFacade.findByNom("sio2");
        ChemicalComponent cd = chemicalComponentFacade.findByNom("cd");
        System.out.println(bpl + "" + co2 + "" + mgo + "" + sio2 + "" + cd);

        try {
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet s = wb.getSheetAt(0);
            XSSFRow row;
            Long size = new Long(s.getLastRowNum());
            if (size > 3600) {
                return null;
            }
            Iterator<Row> rows = s.rowIterator();

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
                    if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null && row.getCell(7) != null) {
                        valeur = row.getCell(0).toString();
                        panel = panelFacade.createNull(valeur);

                        //geting value of cell 1 and creating trench if not exist
                        valeur = row.getCell(1).toString();

                        trench = trenchFacade.createNull(valeur, panel);

                        //geting value of cell 2 and creating parcel if not exist
                        valeur = row.getCell(2).toString();

                        parcel = parcelFacade.createNull(valeur, trench);

                        if (row.getCell(4) != null && !row.getCell(4).toString().equals("")) {
                            puissance = new Double(row.getCell(4).toString().replaceAll(",", "."));
                            System.out.println("puissance level ::" + puissance);
                            //geting value of cell 3 and creating level if not exist
                            if (row.getCell(3) != null) {
                                valeur = row.getCell(3).toString();
                            } else {
                                valeur = "";
                            }
                            surface = new Double(row.getCell(5).toString().replaceAll(",", "."));
                            volume = new Double(row.getCell(6).toString().replaceAll(",", "."));
                            levelLayerFacade.createLevelNull(valeur, puissance, surface, volume, parcel);
                        }

                        //geting value of cell 7 and creating layer if not exist
                        valeur = row.getCell(7).toString();
                        puissance = new Double(row.getCell(8).toString().replaceAll(",", "."));
                        surface = new Double(row.getCell(9).toString().replaceAll(",", "."));
                        volume = new Double(row.getCell(10).toString().replaceAll(",", "."));
                        thc = new Double(row.getCell(11).toString().replaceAll(",", "."));
                        taux = new Double(row.getCell(12).toString().replaceAll(",", "."));
                        level = levelLayerFacade.createNull(valeur, parcel, puissance, surface, volume, thc, taux);

                        //the cell != null and has only numbers
                        if (row.getCell(13) != null && row.getCell(13).toString().matches("-?\\d+(\\.\\d+)?")) {
                            if (!row.getCell(13).toString().equals("0.0")) {
                                chemicalComponentLayerFacade.createComponantLevel(level, bpl, new BigDecimal(new Double(row.getCell(13).toString().replaceAll(",", "."))));
                            }
                        }

                        if (row.getCell(14) != null && row.getCell(14).toString().matches("-?\\d+(\\.\\d+)?")) {
                            if (!row.getCell(14).toString().equals("0.0")) {
                                chemicalComponentLayerFacade.createComponantLevel(level, co2, new BigDecimal(new Double(row.getCell(14).toString().replaceAll(",", "."))));
                            }
                        }

                        if (row.getCell(15) != null && row.getCell(15).toString().matches("-?\\d+(\\.\\d+)?")) {
                            if (!row.getCell(15).toString().equals("0.0")) {
                                chemicalComponentLayerFacade.createComponantLevel(level, mgo, new BigDecimal(new Double(row.getCell(15).toString().replaceAll(",", "."))));
                            }
                        }

                        if (row.getCell(16) != null && row.getCell(16).toString().matches("-?\\d+(\\.\\d+)?")) {
                            if (!row.getCell(16).toString().equals("0.0")) {
                                chemicalComponentLayerFacade.createComponantLevel(level, sio2, new BigDecimal(new Double(row.getCell(16).toString().replaceAll(",", "."))));
                            }
                        }

                        if (row.getCell(17) != null && row.getCell(17).toString().matches("-?\\d+(\\.\\d+)?")) {
                            if (!row.getCell(17).toString().equals("0.0")) {
                                chemicalComponentLayerFacade.createComponantLevel(level, cd, new BigDecimal(new Double(row.getCell(17).toString().replaceAll(",", "."))));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return rowsConteur;
    }

    public void generateData(List<Parcel> ps) {
        List<Parcel> parcels = ps;
        List<String> structure, s;
        List<SubPanel> subPanels = new ArrayList();
        List<List<String>> structures = new ArrayList<List<String>>();
        int subPanelCntr = 0, exist = 0;
        SubPanel subPanel;
        Parcel parcel = new Parcel();
        int max = 0;
        for (int i = 0; i < parcels.size(); i++) {

            parcel = parcels.get(i);
            structure = levelLayerFacade.findByParcel(parcel.getId());
            if (structure.size() > max) {
                max = structure.size();
            }
            Object[] s1 = {structure};
            for (int j = 0; j < structures.size(); j++) {
                s = structures.get(j);
                Object[] s2 = {s};
                if (Arrays.equals(s1, s2)) {
                    subPanelCntr = j;
                    exist = 1;
                    break;
                }
            }
            if (exist == 1) {
                subPanel = subPanels.get(subPanelCntr);
                exist = 0;
            } else {
                subPanel = subpanelFacade.createStructure("subPanel " + (structures.size() + 1), parcel.getTrench().getPanel());
                subPanels.add(subPanel);
                structures.add(structure);
            }
            parcel.setSubPanel(subPanel);
        }
        System.out.println(structures);
        if (structures.size() > 0) {
            List<List<String>> structures2 = new ArrayList<List<String>>();
            List<String> structs2 = new ArrayList();
            for (int j = 0; j < structures.size(); j++) {
                String get = "Structure " + (j + 1);
                structs2.add(get);
            }
            structures2.add(structs2);
            for (int i = 0; i < max; i++) {
                structs2 = new ArrayList();
                for (int j = 0; j < structures.size(); j++) {
                    if (structures.get(j).size() < i + 1) {
                        structures.get(j).add(i, "");
                    }
                    String get = structures.get(j).get(i);
                    structs2.add(get);
                }
                structures2.add(structs2);
            }
            try {
                String filename = "C:/Structures Panel " + parcel.getTrench().getPanel().getNom();
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Application 1");
                HSSFRow row;
                for (int i = 0; i < structures2.size(); i++) {
                    row = sheet.createRow((short) i);
                    List<String> structs = structures2.get(i);
                    for (int j = 0; j < structs.size(); j++) {
                        String struct = structs.get(j);
                        row.createCell(i).setCellValue(struct);
                    }

                }

                FileOutputStream fileOut = new FileOutputStream(filename);
                workbook.write(fileOut);
                fileOut.close();
//                workbook.close();
                System.out.println("Your excel file has been generated!");

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}

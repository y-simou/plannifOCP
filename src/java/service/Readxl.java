/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CCL;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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
    private CCLFacade cclFacade;
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

    public LineChartModel createLineChart(XSSFSheet sheet, String o, String m, Date dateSimulation, Date dateMin, Date dateMax) throws IOException {

        LineChartModel model = new LineChartModel();
        BarChartModel barModel = new BarChartModel();
        TimelineModel timeLineModel = new TimelineModel();

        // groups for time line (Gantt)
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        List<Date> times = new ArrayList();
        List<String> machines = new ArrayList(), operations = new ArrayList(); // "Amenagement Foration", "Amenagement Decapage", "Chargement", "Decapage", "Foration", "Gerbage", "Sautage"
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
                        times.add(size, debut);
                        size++;
                    }

                    Date dateMaxFilter;
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
                                //filter
                                dateMaxFilter = dateMax;
                                if (dateMaxFilter == null) {
                                    dateMaxFilter = fin;
                                }
                                if (!debut.before(dateMin) && !fin.after(dateMaxFilter)) {
                                    //legende of event
                                    String title = operation + " (" + (hours - dateDebut) + "H) " + " - P" + ((int) (double) new Double(row.getCell(1).toString())) + " T" + ((int) (double) new Double(row.getCell(2).toString())) + " C" + ((int) (double) new Double(row.getCell(3).toString()));

                                    // create an event with content, start / end dates, editable flag, group name and custom style class  
                                    TimelineEvent event = new TimelineEvent(title, debut, fin, false, machine, operation.toLowerCase().replaceAll(" ", ""));
                                    timeLineModel.add(event);

                                }
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
        BigDecimal[][] lineResulta = new BigDecimal[17][size], barResulta = new BigDecimal[17][size];
        List<BigDecimal> resultas = new ArrayList();

        //retour au debut par rowIterator
        rows = s.rowIterator();
        row = (XSSFRow) rows.next();
        BigDecimal[] previous = new BigDecimal[17];
        List<String> labels = new ArrayList();
        Collections.addAll(labels, "Couche 1_0", "Sillon B", "Sillon A2", "Couche 0", "Couche 1", "Couche 2 inférieure", "Couche 2 supérieure", "Couche 3 supérieure", "Couche 3 inférieure", "Couche 3", "Couche 4 supérieure", "Couche 4 inférieure", "Couche 4", "Couche 5 supérieure", "Couche 5 inférieure", "Couche 5", "Couche 6");
        BigDecimal result, barVal;
        Double value;

        for (int i = 0; i < 17; i++) {
            previous[i] = new BigDecimal(0);
            resultas.add(i, new BigDecimal(0));
        }

        int t = 0;
        for (int j = 0; j < lignes - 1; j++) {
            row = (XSSFRow) rows.next();
            int k = 0; //t index of the size and k index of chartSerie
            if (row.getCell(6) != null && row.getCell(6).toString().equals("Gerbage") && row.getCell(5) != null && row.getCell(5).toString().equals("Fin")) {
                //LineChart
                for (int i = 33; i < 50; i++) {
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
        String dateStringDebutFormat;
        Date dateDebutFormat;
        Date dateMaxFilter;
        for (int i = 0; i < 17; i++) {
            ChartSeries chartSerie = new ChartSeries();
            ChartSeries barSerie = new ChartSeries();
            chartSerie.setLabel(labels.get(i));
            barSerie.setLabel(labels.get(i));
            //add all points to the serie i
            for (int j = 0; j < size; j++) {
                dateDebutFormat = times.get(j);
                dateMaxFilter = dateMax;
                if (dateMaxFilter == null) {
                    dateMaxFilter = dateDebutFormat;
                }
                dateStringDebutFormat = (new SimpleDateFormat("yyyy-MM-dd")).format(dateDebutFormat);

                if (!dateDebutFormat.before(dateMin) && !dateDebutFormat.after(dateMaxFilter)) {
                    chartSerie.set(dateStringDebutFormat, lineResulta[i][j]);
                    barVal = barResulta[i][j];
                    barSerie.set(dateStringDebutFormat, barVal);
                    max1 = barVal.compareTo(max1) == 1 ? barVal : max1;
                }

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

    public Long read(XSSFSheet sheet, Long annee) throws IOException, BiffException {

        Long rowsConteur = 0L;
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        String valeur, cellYear, year = "";
        Double puissance, surface, volume, thc, taux;
        panelConteur = trenchConteur = parcelConteur = layerConteur = 0;
        ChemicalComponent bpl = chemicalComponentFacade.findByNom("BPL");
        ChemicalComponent co2 = chemicalComponentFacade.findByNom("CO2");
        ChemicalComponent mgo = chemicalComponentFacade.findByNom("MGO");
        ChemicalComponent sio2 = chemicalComponentFacade.findByNom("SIO2");
        ChemicalComponent cd = chemicalComponentFacade.findByNom("CD");

        XSSFSheet s = sheet;
        XSSFRow row;
        Long size = new Long(s.getLastRowNum());
        if (size > 3600) {
            return null;
        }
        System.out.println("siiiiiiiiiiiize : " + size);
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
                System.out.println("row   ::::::::  " + row.getRowNum());

                //geting value of cell 0 and creating panel if not exist
                if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null && row.getCell(7) != null) {
                    valeur = row.getCell(0).toString();
                    panel = panelFacade.createNull(valeur);

                    //geting value of cell 1 and creating trench if not exist
                    valeur = row.getCell(1).toString();

                    trench = trenchFacade.createNull(valeur, panel, cellYear);

                    //geting value of cell 2 and creating parcel if not exist
                    valeur = row.getCell(2).toString();

                    parcel = parcelFacade.createNull(valeur, trench);
                    puissance = surface = volume = thc = taux = 0.0;
                    if (row.getCell(4) != null && !row.getCell(4).toString().equals("") && row.getCell(4).toString().matches("-?\\d+(\\.\\d+)?")) {
                        puissance = new Double(row.getCell(4).toString().replaceAll(",", "."));
                        if (puissance != 0.0) {
                            //geting value of cell 3 and creating level if not exist
                            if (row.getCell(3) != null) {
                                valeur = row.getCell(3).toString();
                            } else {
                                valeur = "";
                            }
                            if (row.getCell(5) != null && !row.getCell(5).toString().equals("") && row.getCell(5).toString().matches("-?\\d+(\\.\\d+)?")) {
                                surface = new Double(row.getCell(5).toString().replaceAll(",", "."));
                            }
                            if (row.getCell(6) != null && !row.getCell(6).toString().equals("") && row.getCell(6).toString().matches("-?\\d+(\\.\\d+)?")) {
                                volume = new Double(row.getCell(6).toString().replaceAll(",", "."));
                            }
                            levelLayerFacade.createLevelNull(valeur, puissance, surface, volume, parcel);
                        }
                    }
                    puissance = surface = volume = thc = taux = 0.0;
                    //geting value of cell 7 and creating layer if not exist
                    valeur = row.getCell(7).toString();
                    if (row.getCell(8) != null && !row.getCell(8).toString().equals("") && row.getCell(8).toString().matches("-?\\d+(\\.\\d+)?")) {
                        puissance = new Double(row.getCell(8).toString().replaceAll(",", "."));
                        if (puissance != 0.0) {
                            if (row.getCell(9) != null && !row.getCell(9).toString().equals("") && row.getCell(9).toString().matches("-?\\d+(\\.\\d+)?")) {
                                surface = new Double(row.getCell(9).toString().replaceAll(",", "."));
                            }
                            if (row.getCell(10) != null && !row.getCell(10).toString().equals("") && row.getCell(10).toString().matches("-?\\d+(\\.\\d+)?")) {
                                volume = new Double(row.getCell(10).toString().replaceAll(",", "."));
                            }
                            if (row.getCell(11) != null && !row.getCell(11).toString().equals("") && row.getCell(11).toString().matches("-?\\d+(\\.\\d+)?")) {
                                thc = new Double(row.getCell(11).toString().replaceAll(",", "."));
                            }
                            taux = thc * 0.9;
                            level = levelLayerFacade.createNull(valeur, parcel, puissance, surface, volume, thc, taux);

                            //the cell != null and has only numbers
                            if (row.getCell(13) != null && row.getCell(13).toString().matches("-?\\d+(\\.\\d+)?")) {
                                if (!row.getCell(13).toString().equals("0.0")) {
                                    chemicalComponentLayerFacade.createComponantLevel(level, bpl, new Double(row.getCell(13).toString().replaceAll(",", ".")));
                                }
                            }

                            if (row.getCell(14) != null && row.getCell(14).toString().matches("-?\\d+(\\.\\d+)?")) {
                                if (!row.getCell(14).toString().equals("0.0")) {
                                    chemicalComponentLayerFacade.createComponantLevel(level, co2, new Double(row.getCell(14).toString().replaceAll(",", ".")));
                                }
                            }

                            if (row.getCell(15) != null && row.getCell(15).toString().matches("-?\\d+(\\.\\d+)?")) {
                                if (!row.getCell(15).toString().equals("0.0")) {
                                    chemicalComponentLayerFacade.createComponantLevel(level, mgo, new Double(row.getCell(15).toString().replaceAll(",", ".")));
                                }
                            }

                            if (row.getCell(16) != null && row.getCell(16).toString().matches("-?\\d+(\\.\\d+)?")) {
                                if (!row.getCell(16).toString().equals("0.0")) {
                                    chemicalComponentLayerFacade.createComponantLevel(level, sio2, new Double(row.getCell(16).toString().replaceAll(",", ".")));
                                }
                            }

                            if (row.getCell(17) != null && row.getCell(17).toString().matches("-?\\d+(\\.\\d+)?")) {
                                if (!row.getCell(17).toString().equals("0.0")) {
                                    chemicalComponentLayerFacade.createComponantLevel(level, cd, new Double(row.getCell(17).toString().replaceAll(",", ".")));
                                }
                            }
                        }
                    }
                }
            }
        }
        return rowsConteur;
    }

    public String generateData(Panel pane) {
        String filename = "";
        List<Parcel> ps;
        if (pane == null) {
            ps = parcelFacade.findAllOrder();
        } else {
            ps = parcelFacade.findByPanel(pane.getId());
        }
        List<Parcel> parcels = ps;
        List<String> structure, s, structsHeader = new ArrayList(), groupesHeader = new ArrayList();
        List<SubPanel> subPanels = new ArrayList();
        List<LevelLayer> listStructure = new ArrayList();
        List<List<LevelLayer>> listStructures = new ArrayList<List<LevelLayer>>();
        List<List<String>> structures = new ArrayList<List<String>>();
        int subPanelCntr = 0, exist = 0;
        SubPanel subPanel;
        Parcel parcel = new Parcel();
        int max = 0;
        for (int i = 0; i < parcels.size(); i++) {

            parcel = parcels.get(i);
            structure = levelLayerFacade.findNomByParcel(parcel.getId());
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
                subPanel = subpanelFacade.createStructure(parcel);
                subPanels.add(subPanel);
                String get = "Structure " + subPanel.getNom();
                structures.add(structure);
                listStructure = levelLayerFacade.findByParcel(parcel.getId());
                listStructures.add(listStructure);
                structsHeader.add(get);
            }
            parcel.setSubPanel(subPanel);
            parcelFacade.edit(parcel);
        }
        if (structures.size() > 0) {
            List<List<String>> structures2 = new ArrayList<>();
            List<String> structs2;
            List<Boolean> booleanStructure = new ArrayList();
            List<List<Boolean>> booleanStructures = new ArrayList<List<Boolean>>();

            //revert the array
            for (int i = 0; i < max; i++) {
                structs2 = new ArrayList();
                booleanStructure = new ArrayList();
                for (int j = 0; j < structures.size(); j++) {
                    String get;
                    Boolean b;
                    if (structures.get(j).size() < i + 1) {
                        get = "";
                    } else {
                        get = structures.get(j).get(i);
                    }
                    if (listStructures.get(j).size() < i + 1) {
                        b = null;
                    } else {
                        b = listStructures.get(j).get(i).isPhosphate();
                    }
                    structs2.add(get);
                    booleanStructure.add(b);
                }
                structures2.add(structs2);
                booleanStructures.add(booleanStructure);
            }

            //////////groupes//////////////////////////////////////////////////////////////////////////////////
            List<List<Double>> puissanceGroupes = new ArrayList<List<Double>>();
            CCL ccl;
            max = 0;
            List<Integer> nombreOfGroupeByStruct = new ArrayList();
//            Long idPanel = parcel.getTrench().getPanel().getId();
            for (int i = 0; i < subPanels.size(); i++) {
                SubPanel subPanel1 = subPanels.get(i);
                System.out.println(subPanel1);
                parcels = parcelFacade.findBySubPanel(subPanel1.getId());
                System.out.println("parcels :: " + parcels.size());
                List<List<Double>> puissanceGroupes1 = new ArrayList<List<Double>>();
                List<Double> puissanceGroupe = new ArrayList(), p;
                List<CCL> ccls = new ArrayList();
                int cclIndex = 0, cclCntr = 0;

                for (int j = 0; j < parcels.size(); j++) {
                    Parcel parcel1 = parcels.get(j);

//                    if (parcel1.getTrench().getPanel().getId().equals(idPanel)) {
                    puissanceGroupe = levelLayerFacade.findPuissanceByParcel(parcel1.getId());
                    if (puissanceGroupe.size() > max) {
                        max = puissanceGroupe.size();
                    }
                    for (int k = 0; k < puissanceGroupes1.size(); k++) {
                        p = puissanceGroupes1.get(k);
                        if (compareArray(puissanceGroupe, p) == 0) {
                            cclIndex = k;
                            exist = 1;
                            break;
                        }
                    }
                    if (exist == 1) {
                        ccl = ccls.get(cclIndex);
                        exist = 0;
                    } else {
                        ccl = cclFacade.createGroupe("Groupe " + (puissanceGroupes1.size() + 1), subPanel1);
                        ccls.add(ccl);
                        String get = "Groupe " + (puissanceGroupes1.size() + 1);
                        puissanceGroupes1.add(puissanceGroupe);
                        groupesHeader.add(get);
                        cclCntr++;
                    }
                    parcel1.setCcl(ccl);
                    parcelFacade.edit(parcel);

//                    }
                    nombreOfGroupeByStruct.add(i, cclCntr);
                }
                puissanceGroupes.addAll(puissanceGroupes1);
            }

            System.out.println("max :: " + max);
            List<List<Double>> groupes2 = new ArrayList<List<Double>>();
            List<Double> groups2 = new ArrayList();

            //revert the array
            System.out.println("puissanceGroupes size :: " + puissanceGroupes.size());
            for (int i = 0; i < max; i++) {
                groups2 = new ArrayList();
                for (int j = 0; j < puissanceGroupes.size(); j++) {
                    Double get;
                    if (puissanceGroupes.get(j).size() < i + 1) {
                        get = null;
                    } else {
                        get = puissanceGroupes.get(j).get(i);
                    }
                    groups2.add(get);
                }
                groupes2.add(groups2);
            }
            parcels = ps;
            System.out.println("parcels :: " + parcels.size());
            List<Long> conteurs = new ArrayList();
            try {
                if (pane == null) {
                    filename = "D:/Structures.xls";
                } else {
                    filename = "D:/Structures_Panel_" + parcel.getTrench().getPanel().getNom() + ".xls";
                }

                XSSFWorkbook workbook = new XSSFWorkbook();

                //////////////////////Application 1////////////////////
                XSSFSheet sheet = workbook.createSheet("Application 1");
                XSSFRow row = sheet.createRow(0);
                // Create a Font for styling header cells
                Font headerFont = workbook.createFont();
                headerFont.setBoldweight((short) 12);
                headerFont.setFontHeightInPoints((short) 14);
                headerFont.setColor(IndexedColors.BLACK.getIndex());

                // Create a CellStyle for the first row
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                for (int j = 0; j < structsHeader.size(); j++) {
                    String struct = structsHeader.get(j);
                    row.createCell(j).setCellValue(struct);
                    row.getCell(j).setCellStyle(headerCellStyle);
                    conteurs.add(0L);
                }
                //other rows
                for (int i = 0; i < structures2.size(); i++) {
                    row = sheet.createRow((short) (i + 1));
                    List<String> structs = structures2.get(i);
                    for (int j = 0; j < structs.size(); j++) {
                        String struct = structs.get(j);
                        row.createCell(j).setCellValue(struct);
                    }

                }

                //////////////////////Application 2////////////////////
                sheet = workbook.createSheet("Application 2");
                row = sheet.createRow(0);

                // Create the first row
                row.createCell(0).setCellValue("Num Parcel ");
                row.createCell(1).setCellValue("Num Panel ");
                row.createCell(2).setCellValue("Num Trench ");
                row.createCell(3).setCellValue("Num Structure ");
                row.createCell(4).setCellValue("Num Groupe ");

                for (int i = 0; i < parcels.size(); i++) {
                    Parcel p = parcels.get(i);
                    row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(p.getNom());
                    row.createCell(1).setCellValue(p.getTrench().getPanel().getNom());
                    row.createCell(2).setCellValue(p.getTrench().getNom());
                    row.createCell(3).setCellValue(new Double(p.getSubPanel().getNom()));
                    String[] values = p.getCcl().getNom().split(" ");
                    row.createCell(4).setCellValue(new Double(values[1]));
                }
                ////////////////////////////CCL/////////////////////////////////
                sheet = workbook.createSheet("Application 3");
                row = sheet.createRow(0);

                // Create a CellStyle for the first row
                row.createCell(0).setCellValue("Structures ");
                row.getCell(0).setCellStyle(headerCellStyle);
                int col1 = 1, col2, index = 0;
                for (String struct : structsHeader) {
                    row.createCell(col1).setCellValue(struct);
                    row.getCell(col1).setCellStyle(headerCellStyle);
                    col2 = col1 + nombreOfGroupeByStruct.get(index) - 1;
                    index++;
                    // Merges the cells
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, col1, col2);
                    col1 = col2 + 1;
                    sheet.addMergedRegion(cellRangeAddress);
                }
                row = sheet.createRow(1);

                // Create the seconde row
                row.createCell(0).setCellValue("Groupes ");
                row.getCell(0).setCellStyle(headerCellStyle);
                for (int j = 1; j < groupesHeader.size() + 1; j++) {
                    String groupe = groupesHeader.get(j - 1);
                    row.createCell(j).setCellValue(groupe);
                    row.getCell(j).setCellStyle(headerCellStyle);
                }
                //other rows
                for (int i = 0; i < groupes2.size(); i++) {
                    row = sheet.createRow((short) (i + 2));
                    List<Double> groups = groupes2.get(i);
                    for (int j = 1; j < groups.size() + 1; j++) {
                        Double group = groups.get(j - 1);
                        if (group != null) {
                            row.createCell(j).setCellValue(group);
                        }
                    }
                }
                //////////////////////Coupes litho////////////////////
                sheet = workbook.createSheet("Coupes litho");
                row = sheet.createRow(0);
                XSSFRow row2 = sheet.createRow(1);
//                CellStyle headerCellStyle1 = headerCellStyle;
//                headerCellStyle1.setFillBackgroundColor(IndexedColors.ORANGE.index);
//                headerCellStyle1.setAlignment(CellStyle.ALIGN_CENTER);

                // Create a CellStyle for the first row
                col1 = 0;
                String struct;
                for (int j = 0; j < structsHeader.size(); j++) {
                    row2.createCell(col1).setCellValue("Nom niveau");
                    row2.createCell(col1 + 1).setCellValue("Num niveau");
                    row2.createCell(col1 + 2).setCellValue("Amenagement Foration");
                    row2.createCell(col1 + 3).setCellValue("Foration");
                    row2.createCell(col1 + 4).setCellValue("Sautage");
                    row2.createCell(col1 + 5).setCellValue("Amenagement Decapage");
                    row2.createCell(col1 + 6).setCellValue("Decapage");
                    row2.createCell(col1 + 7).setCellValue("Gerbage");
                    row2.createCell(col1 + 8).setCellValue("Charg/Transport");
                    if (pane == null) {
                        struct = "" + structsHeader.get(j);
                    } else {
                        struct = "Panel " + parcel.getTrench().getPanel().getNom() + "_" + structsHeader.get(j);
                    }

                    row.createCell(col1).setCellValue(struct);
                    row.getCell(col1).setCellStyle(headerCellStyle);
                    col2 = col1 + 8;
                    // Merges the cells
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, col1, col2);
                    col1 = col2 + 1;
                    sheet.addMergedRegion(cellRangeAddress);

                }
                //other rows
                for (int i = 0; i < structures2.size(); i++) {
                    row = sheet.createRow((short) (i + 2));
                    int k = 0;
                    Long val;
                    List<String> structs = structures2.get(i);
                    for (int j = 0; j < structs.size(); j++) {
                        Boolean b = booleanStructures.get(i).get(j);
                        struct = structs.get(j);
                        row.createCell(k).setCellValue(struct);
                        if (b != null) {
                            if (b) {
                                val = conteurs.get(j) + 1;
                                row.createCell(k + 7).setCellValue(val);
                                val++;
                                row.createCell(k + 8).setCellValue(val);
                                conteurs.set(j, val);

                            } else {
                                val = conteurs.get(j) + 1;
                                row.createCell(k + 1).setCellValue(val);
                                val++;
                                row.createCell(k + 2).setCellValue(val);
                                val++;
                                row.createCell(k + 3).setCellValue(val);
                                val++;
                                row.createCell(k + 4).setCellValue(val);
                                val++;
                                row.createCell(k + 5).setCellValue(val);
                                val++;
                                row.createCell(k + 6).setCellValue(val);
                                conteurs.set(j, val);
                            }
                        }
                        k += 9;
                    }

                }

                try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                    workbook.write(fileOut);
                }
//                workbook.close();
                System.out.println("Your excel file has been generated!");

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return filename;
    }

    private int compareArray(List<Double> l1, List<Double> l2) {
        int c = 0;
        if (l1.size() == l2.size()) {
            for (int i = 0; i < l2.size(); i++) {
                if (l1.get(i).compareTo(l2.get(i) + 0.50) > 0 || l1.get(i).compareTo(l2.get(i) - 0.50) < 0) {
                    System.out.println(":::::::::::::::::::::::::::::::::::::diff::::::::::::::::::::::::::::::::::::::::");
                    c = 1;
                    break;
                }
                System.out.println("------------------saame-----------------------------");
            }
        } else {
            c = 1;
        }
        return c;
    }
}

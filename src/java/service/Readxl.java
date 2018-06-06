/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.Iterator;

import bean.ChemicalComponent;
import bean.ChemicalComponentLayer;
import bean.Layer;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.SubPanel;
import bean.Trench;
import java.io.IOException;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jxl.read.biff.BiffException;

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

    public Long read(InputStream inputStream, Long annee) throws IOException, BiffException {

        Long rowsConteur = 0L;
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        Layer layer;
        String valeur;
        panelConteur = 0;
        trenchConteur = 0;
        parcelConteur = 0;
        layerConteur = 0;
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
            Iterator<Row> rows = s.rowIterator();

            // style for PR >= 5
            CellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.GREEN.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            row = (XSSFRow) rows.next();

            //                subpanel don't exist in the excel so i create a subpanel with id 1 ,i need subpanel to create parcel 
            SubPanel subpanel = subpanelFacade.find(1L);
            if (subpanel == null) {
                subpanel = new SubPanel(subpanelFacade.generateId("SubPanel", "id"));
            }

            String year = "";
            String cellYear;
            if (annee != null) {
                year = annee.toString();
            }
            while (rows.hasNext()) {

                row = (XSSFRow) rows.next();
                rowsConteur++;

                if (row.getCell(18) == null) {
                    cellYear = "";
                }else{
                    cellYear = row.getCell(18).toString();
                }

                if (year.equals(cellYear)) {
                System.out.println("haaa row   ::::::::  " + row.getRowNum());
                System.out.println("haaa col lwl  :::::::::" + row.getCell(0).toString());

                //                geting value of cell 0 and creating panel if not exist
                if (row.getCell(0) != null) {
                    valeur = row.getCell(0).toString();
                } else {
                    valeur = "";
                }
                if (valeur != null && valeur.length() > 0 && valeur.charAt(valeur.length() - 1) == '0' && valeur.charAt(valeur.length() - 2) == '.') {
                    valeur = valeur.substring(0, valeur.length() - 2);
                }

                System.out.println("Panel :: " + valeur);
                panel = panelFacade.findByNom(valeur);
                if (panel == null) {
                    System.out.println("null");
                    panel = new Panel(valeur);
                    panel.setId(panelFacade.generateId("Panel", "id"));
                    panelFacade.create(panel);
                    panelConteur++;

                }
                System.out.println(panel);

                //                geting value of cell 1 and creating trench if not exist
                if (row.getCell(1) != null) {
                    valeur = row.getCell(1).toString();
                } else {
                    valeur = "";
                }

                if (valeur != null && valeur.length() > 0 && valeur.charAt(valeur.length() - 1) == '0' && valeur.charAt(valeur.length() - 2) == '.') {
                    valeur = valeur.substring(0, valeur.length() - 2);
                }

                trench = trenchFacade.findByNomAndPanel(valeur, panel.getId());
                System.out.println("Trench :: " + valeur);
                if (trench == null) {
                    System.out.println("null");
                    trench = new Trench(valeur, panel);
                    trench.setId(trenchFacade.generateId("Trench", "id"));
                    trenchFacade.create(trench);
                    trenchConteur++;

                }
                System.out.println(trench);

                //                geting value of cell 2 and creating parcel if not exist
                if (row.getCell(2) != null) {
                    valeur = row.getCell(2).toString();
                } else {
                    valeur = "";
                }

                if (valeur != null && valeur.length() > 0 && valeur.charAt(valeur.length() - 1) == '0' && valeur.charAt(valeur.length() - 2) == '.') {
                    valeur = valeur.substring(0, valeur.length() - 2);
                }

                parcel = parcelFacade.findByNomAndTrench(valeur, trench.getId());
                System.out.println("Parcel ::" + valeur);
                if (parcel == null) {
                    System.out.println("null");
                    parcel = new Parcel(parcelFacade.generateId("Parcel", "id"), valeur, trench, subpanel);
                    parcelFacade.create(parcel);
                    parcelConteur++;

                }
                System.out.println(parcel);

                //                level don't exist in the excel so i create a level depend on parcel id 
                System.out.println("level :: " + parcel.getId());
                level = levelLayerFacade.find(parcel.getId());
                if (level == null) {
                    System.out.println("null");
                    level = new LevelLayer(parcel.getId(), parcel);
                    levelLayerFacade.create(level);
                    level = levelLayerFacade.find(level.getId());
                }
                System.out.println(level);

                //                geting value of cell 7 and creating layer if not exist
                if (row.getCell(7) != null) {
                    valeur = row.getCell(7).toString();
                } else {
                    valeur = "";
                }
                layer = layerFacade.findByNomAndLevel(valeur, level.getId());
                System.out.println(valeur);
                if (layer == null) {
                    System.out.println("null");
                    layer = new Layer(layerFacade.generateId("Layer", "id"), valeur, level);
                    layerFacade.create(layer);
                    layerConteur++;
                    layer = layerFacade.find(layer.getId());
                }
                System.out.println(layer);

                if (row.getCell(13) != null && row.getCell(13).toString().matches("-?\\d+(\\.\\d+)?")) {
                    if (!row.getCell(13).toString().equals("0.0")) {
                        chemicalComponentLayerFacade.createComponantLayer(layer, bpl, new BigDecimal(row.getCell(13).toString()));
                        System.out.println(bpl);
                        System.out.println(row.getCell(13));
                    }

                }

                if (row.getCell(14) != null && row.getCell(14).toString().matches("-?\\d+(\\.\\d+)?")) {
                    if (!row.getCell(14).toString().equals("0.0")) {
                        chemicalComponentLayerFacade.createComponantLayer(layer, co2, new BigDecimal(row.getCell(14).toString()));
                        System.out.println(co2);
                        System.out.println(row.getCell(14));
                    }

                }

                if (row.getCell(15) != null && row.getCell(15).toString().matches("-?\\d+(\\.\\d+)?")) {
                    if (!row.getCell(15).toString().equals("0.0")) {
                        chemicalComponentLayerFacade.createComponantLayer(layer, mgo, new BigDecimal(row.getCell(15).toString()));
                        System.out.println(mgo);
                        System.out.println(row.getCell(15));
                    }

                }

                if (row.getCell(16) != null && row.getCell(16).toString().matches("-?\\d+(\\.\\d+)?")) {
                    if (!row.getCell(16).toString().equals("0.0")) {
                        chemicalComponentLayerFacade.createComponantLayer(layer, sio2, new BigDecimal(row.getCell(16).toString()));
                        System.out.println(sio2);
                        System.out.println(row.getCell(16));
                    }

                }

                if (row.getCell(17) != null && row.getCell(17).toString().matches("-?\\d+(\\.\\d+)?")) {
                    if (!row.getCell(17).toString().equals("0.0")) {
                        chemicalComponentLayerFacade.createComponantLayer(layer, cd, new BigDecimal(row.getCell(17).toString()));
                        System.out.println(cd);
                        System.out.println(row.getCell(17));
                    }

                }

                System.out.println("----------------------------------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------------------------------");
                System.out.println("----------------------------------------------------------------------------------------");
                }
            }

        } catch (IOException e) {
        }
        return rowsConteur;
    }
}

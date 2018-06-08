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
                    System.out.println(panel);

                    //geting value of cell 1 and creating trench if not exist
                    if (row.getCell(1) != null) {
                        valeur = row.getCell(1).toString();
                    } else {
                        valeur = "";
                    }

                    trench = trenchFacade.createNull(valeur, panel);
                    System.out.println(trench);

                    //geting value of cell 2 and creating parcel if not exist
                    if (row.getCell(2) != null) {
                        valeur = row.getCell(2).toString();
                    } else {
                        valeur = "";
                    }

                    parcel = parcelFacade.createNull(valeur, trench, subpanel);
                    System.out.println(parcel);

                    //level don't exist in the excel so i create a level depend on parcel id 
                    level = levelLayerFacade.createNull(parcel);
                    System.out.println(level);

                    //geting value of cell 7 and creating layer if not exist
                    if (row.getCell(7) != null) {
                        valeur = row.getCell(7).toString();
                    } else {
                        valeur = "";
                    }
                    layer = layerFacade.createNull(valeur, level);
                    System.out.println(layer);

                    //the cell != null and has only numbers
                    if (row.getCell(13) != null && row.getCell(13).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(13).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, bpl, new BigDecimal(new Double(row.getCell(13).toString())));
                            System.out.println(row.getCell(13));
                        }

                    }

                    if (row.getCell(14) != null && row.getCell(14).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(14).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, co2, new BigDecimal(new Double(row.getCell(14).toString())));
                            System.out.println(row.getCell(14));
                        }

                    }

                    if (row.getCell(15) != null && row.getCell(15).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(15).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, mgo, new BigDecimal(new Double(row.getCell(15).toString())));
                            System.out.println(row.getCell(15));
                        }

                    }

                    if (row.getCell(16) != null && row.getCell(16).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(16).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, sio2, new BigDecimal(new Double(row.getCell(16).toString())));
                            System.out.println(row.getCell(16));
                        }

                    }

                    if (row.getCell(17) != null && row.getCell(17).toString().matches("-?\\d+(\\.\\d+)?")) {
                        if (!row.getCell(17).toString().equals("0.0")) {
                            chemicalComponentLayerFacade.createComponantLayer(layer, cd, new BigDecimal(new Double(row.getCell(17).toString())));
                            System.out.println(row.getCell(17));
                        }

                    }

                    System.out.println("----------------------------------------------------------------------------------------");
                }
            }
            
        } catch (IOException e) {
        }
        return rowsConteur;
    }
}

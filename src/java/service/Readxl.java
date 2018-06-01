/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ChemicalComponent;
import bean.ChemicalComponentLayer;
import bean.Layer;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.Trench;
import java.io.File;
import java.io.IOException;
import javax.ejb.EJB;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Yassine.SIMOU
 */
public class Readxl {

    @EJB
    PanelFacade panelFacade = new PanelFacade();
    @EJB
    TrenchFacade trenchFacade = new TrenchFacade();
    @EJB
    ParcelFacade parcelFacade = new ParcelFacade();
    @EJB
    LevelLayerFacade levelLayerFacade = new LevelLayerFacade();
    @EJB
    LayerFacade layerFacade = new LayerFacade();
    @EJB
    ChemicalComponentFacade chemicalComponentFacade = new ChemicalComponentFacade();
    @EJB
    ChemicalComponentLayerFacade chemicalComponentLayerFacade = new ChemicalComponentLayerFacade();

    static int panelConteur;
    static int trenchConteur;
    static int parcelConteur;
    static int layerConteur;

    public Long read(File xlFile) throws IOException, BiffException {
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        Layer layer;
        ChemicalComponent bpl = chemicalComponentFacade.findByNom("bpl");
        ChemicalComponent co2 = chemicalComponentFacade.findByNom("co2");
        ChemicalComponent mgo = chemicalComponentFacade.findByNom("mgo");
        ChemicalComponent sio2 = chemicalComponentFacade.findByNom("sio2");
        ChemicalComponent cd = chemicalComponentFacade.findByNom("cd");
        Workbook w = Workbook.getWorkbook(xlFile);
        Sheet s = w.getSheet(0);
        panelConteur = 0;
        trenchConteur = 0;
        parcelConteur = 0;
        layerConteur = 0;
        for (int i = 1;
                i < s.getRows();
                i++) {
            panel = panelFacade.findByNom(s.getCell(0, i).getContents());
            if (panel == null) {
                panel = new Panel(s.getCell(0, i).getContents());
                panel.setId(panelFacade.generateId("Panel", "id"));
                panelFacade.create(panel);
                panelConteur++;
            }

            trench = trenchFacade.findByNomAndPanel(s.getCell(1, i).getContents(), panel.getId());
            if (trench == null) {
                trench = new Trench(s.getCell(1, i).getContents(), panel);
                trench.setId(trenchFacade.generateId("Trench", "id"));
                trenchFacade.create(trench);
                trenchConteur++;
            }

            parcel = parcelFacade.findByNomAndTrench(s.getCell(2, i).getContents(), trench.getId());
            if (parcel == null) {
                parcel = new Parcel(s.getCell(1, i).getContents(), trench);
                parcel.setId(parcelFacade.generateId("Parcel", "id"));
                parcelFacade.create(parcel);
                parcelConteur++;
            }

            level = levelLayerFacade.findByNomAndParcel(new Integer(s.getCell(1, i).getContents()), parcel.getId());
            if (level == null) {
                level = new LevelLayer(levelLayerFacade.generateId("LevelLayer", "id"), new Integer(s.getCell(2, i).getContents()), parcel);
                levelLayerFacade.create(level);
            }

            layer = layerFacade.findByNomAndLevel(s.getCell(7, i).getContents(), level.getId());
            if (layer == null) {
                layer = new Layer(levelLayerFacade.generateId("LevelLayer", "id"), s.getCell(1, i).getContents(), level);
                layerFacade.create(layer);
                layerConteur++;
            }

            ChemicalComponentLayer chemicalComponentLayer = new ChemicalComponentLayer(layer, bpl, new Long(s.getCell(13, i).getContents()));
            chemicalComponentLayerFacade.create(chemicalComponentLayer);
            chemicalComponentLayer = new ChemicalComponentLayer(layer, co2, new Long(s.getCell(14, i).getContents()));
            chemicalComponentLayerFacade.create(chemicalComponentLayer);
            chemicalComponentLayer = new ChemicalComponentLayer(layer, mgo, new Long(s.getCell(15, i).getContents()));
            chemicalComponentLayerFacade.create(chemicalComponentLayer);
            chemicalComponentLayer = new ChemicalComponentLayer(layer, sio2, new Long(s.getCell(16, i).getContents()));
            chemicalComponentLayerFacade.create(chemicalComponentLayer);
            chemicalComponentLayer = new ChemicalComponentLayer(layer, cd, new Long(s.getCell(17, i).getContents()));
            chemicalComponentLayerFacade.create(chemicalComponentLayer);
        }

        return new Long(s.getRows() - 1);
    }
}

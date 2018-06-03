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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jxl.Sheet;
import jxl.Workbook;
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

    public int panelConteur;
    public int trenchConteur;
    public int parcelConteur;
    public int layerConteur;
    
    

    public Long read(File xlFile) throws IOException, BiffException {
        Panel panel;
        Trench trench;
        Parcel parcel;
        LevelLayer level;
        Layer layer;
        panelConteur = 0;
        trenchConteur = 0;
        parcelConteur = 0;
        layerConteur = 0;
        ChemicalComponent bpl = chemicalComponentFacade.find(new Long("1"));
        ChemicalComponent co2 = chemicalComponentFacade.find(new Long("1"));
        ChemicalComponent mgo = chemicalComponentFacade.find(new Long("1"));
        ChemicalComponent sio2 = chemicalComponentFacade.find(new Long("1"));
        ChemicalComponent cd = chemicalComponentFacade.find(new Long("1"));
        Workbook w = Workbook.getWorkbook(xlFile);
        Sheet s = w.getSheet(0);
        
        for (int i = 1;i < s.getRows();i++) {
            panel = panelFacade.findByNom(s.getCell(0, i).getContents());
            System.out.println();
            if (panel == null) {
                panel = new Panel(s.getCell(0, i).getContents());
                panel.setId(panelFacade.generateId("Panel", "id"));
                panelFacade.create(panel);
                panelConteur++;
            }
            

            trench = trenchFacade.findByNomAndPanel(s.getCell(1, i).getContents(), panel.getId());
            System.out.println(trench);
            System.out.println(s.getCell(1, i));
            if (trench == null) {
                trench = new Trench(s.getCell(1, i).getContents(), panel);
                trench.setId(trenchFacade.generateId("Trench", "id"));
                trenchFacade.create(trench);
                trenchConteur++;
                System.out.println(trench);
            }

            parcel = parcelFacade.findByNomAndTrench(s.getCell(2, i).getContents(), trench.getId());
            System.out.println(parcel);
            System.out.println(s.getCell(2, i));
            if (parcel == null) {
                parcel = new Parcel(s.getCell(1, i).getContents(), trench);
                parcel.setId(parcelFacade.generateId("Parcel", "id"));
                parcelFacade.create(parcel);
                parcelConteur++;
                System.out.println(parcel);
            }

            level = levelLayerFacade.findByNomAndParcel(new Integer(s.getCell(1, i).getContents()), parcel.getId());
            System.out.println(s.getCell(1, i).getContents());
            System.out.println(level);
            if (level == null) {
                level = new LevelLayer(levelLayerFacade.generateId("LevelLayer", "id"), new Integer(s.getCell(2, i).getContents()), parcel);
                levelLayerFacade.create(level);
                System.out.println(level);
            }

            layer = layerFacade.findByNomAndLevel(s.getCell(7, i).getContents(), level.getId());
            System.out.println(s.getCell(7, i).getContents());
            System.out.println(layer);
            if (layer == null) {
                layer = new Layer(levelLayerFacade.generateId("LevelLayer", "id"), s.getCell(1, i).getContents(), level);
                layerFacade.create(layer);
                layerConteur++;
                System.out.println(layer);
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

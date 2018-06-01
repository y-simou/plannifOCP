/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
    private File xlFile;
    private Readxl facade = new Readxl();
    
    
    public void database(){
        
    }

    public XlController() {
    }

    public File getXlFile() {
        return xlFile;
    }

    public void setXlFile(File xlFile) {
        this.xlFile = xlFile;
    }

    public Readxl getFacade() {
        return facade;
    }

}

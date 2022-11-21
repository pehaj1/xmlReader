/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlreader;

import java.io.FileNotFoundException;

/**
 *
 * @author HajekP
 */
public class MainXml {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        SuborXml suborXml = new SuborXml("KL605RUK.XML");
        suborXml.convert();
    }
    
}

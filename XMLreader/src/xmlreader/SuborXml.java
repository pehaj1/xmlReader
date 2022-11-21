package xmlreader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.XMLConstants;

public class SuborXml {

    private final File novySubor;
    private final PrintWriter pw;
    private Document doc;
    private String udajeBd;
    private String typMeraca;
    private String riadok;
    private String vchod;
    private String posch;
    private String byt;
    private String meno;
    private String origData;
    private String miestnost;
    private String pomRetaz = "";
    private Boolean verzia = true;
    private String poznamka;

    public SuborXml(String cestaSubor) throws FileNotFoundException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new File(cestaSubor));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        
        typMeraca = null;
        NodeList list = doc.getElementsByTagName("Task");
        Node node = list.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element0 = (Element) node;
            typMeraca = element0.getAttribute("Agent").substring(22, 33);

        }
        novySubor = new File(cestaSubor.replaceAll(".XML", ".csv"));
        pw = new PrintWriter(novySubor);

        
        
    }

    
    public void convert() {

        switch (typMeraca) {
            case "Son556-read":
                set566R();
                break;
            case "Son566-read":
                set566R();
                break;
            case "Son581-read":
                set581R();
                break;
            default:
                riadok = "Nerozpoznaný typ merača,alebo súboru."; 
                System.out.println(riadok);
                pw.println(riadok);
                pw.close();
        }

    }

    
    public void set566R() {

        // Udaje o BD, dat odpoctu a typ merača 
        NodeList list = doc.getElementsByTagName("Group");
        Node node = list.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            udajeBd = element.getAttribute("Caption");
            pw.println(udajeBd);
            System.out.println(udajeBd); 
            //Zápis hlavičky súboru
            if (verzia) {
                pw.println("\nEv.číslo;Priezvisko a Meno;Vchod;Poschodie;Č.bytu;Miestnosť;Č.PRVN;Aktuálna spotreba;Minuloročná spotreba;Poznámka");
                System.out.println("\nEv.číslo;Priezvisko a Meno;Vchod;Poschodie;Č.bytu;Miestnosť;Č.PRVN;Akt.spotreba;Minuloročná spotreba;Poznámka");
            } else {
                String datSp = "";
                for (int i = 2; i < 19; i++) {
                    datSp = datSp.concat("Dátum_" + i + ";" + "Spotreba_" + i + ";");
                }
                pw.println("\nEv.číslo;Priezvisko a Meno;Vchod;Poschodie;Č.bytu;Miestnosť;Ev.číslo;Č.merača;Typ_odpočtu;Príznak;PC_čas;Č.merača;Výrobca;Typové_č.;Typ_merača;Prístupy;Staus;Podpis;Čas_merača;Akt.spotreba;Neident.dát.;Minuloročná_spotreba;Max.tepl.rad.MR;Jed.;Dátum_1;Spotreba_1;Register;" + datSp + "Koef 1;Koef 2;Tep.rad;Jed.;Tep.miestnosť;Jed.;Max.tep.rad.akt.r.;Jed.;Otvorenie;Jed.;Dát.posl.otvorenia;Dát.aktivácie;Soft.verzia;Err.flag;StateOfParameterActivation;DeviceAccessRightLevel;Tovaren.číslo;Poznámka;Poznámka");
                System.out.println("\nEv.číslo;Priezvisko a Meno;Vchod;Poschodie;Č.bytu;Miestnosť;Ev.číslo;Č.merača;Typ odpočtu;Príznak;PC čas,Č.merača;Výrobca;Typ.č.;Typ merača;Prítupy;Staus;Podpis;Čas merača;Akt.spotreba;Neident.dát.;Minuloročná spotreba;Max.tepl.rad.MR;Jed.;Dátum 1;Spotreba 1;Register;" + datSp + "Koef 1;Koef 2;Tep.rad;Jed.;Tep.miestnosť;Jed.;Max.tep.rad.akt.r.;Jed.;Otvorenie;Jed.;Dát.posl.otvorenia;Dát.aktivácie;Soft.verzia;Err.flag;StateOfParameterActivation;DeviceAccessRightLevel;Tovaren.číslo;Poznámka;Poznámka");
            }
        }
        list = doc.getElementsByTagName("Task");
        for (int temp = 0; temp < list.getLength(); temp++) {
            node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element parent = (Element) node.getParentNode();
                Element element = (Element) node;
                String vchPosByt = parent.getAttribute("Caption");
                int index = vchPosByt.indexOf(" ", 1);
                
               
                vchod = vchPosByt.substring(0, index);
                posch = vchPosByt.substring(index + 1, index + 3);
                byt = vchPosByt.substring(index + 4, index + 7);
                meno = vchPosByt.substring(index + 7);
                miestnost = element.getAttribute("Caption").replace(" ", "");
                origData = element.getTextContent().replaceAll("\\s+ ", ";");
                String[] udaje = new String[160];
                riadok = meno + ";" + vchod + ";" + posch + ";" + byt + origData + pomRetaz;
                udaje = riadok.split(";");

                // Skrátená verzia súboru
                if (verzia) {
                    for (int i = 0; i < 74; i++) {
                        pomRetaz = pomRetaz.concat(";neodpočítaný");
                    }
                    switch (udaje[14]) {
                        case "80":
                            poznamka = "Možný zásah,vykonať kontrolu.";
                            break;
                        case "48":
                            poznamka = "Chyba teploty,vykonať kontrolu.";
                            break;
                        default:
                            poznamka = "";
                    }
                    String uprRiadok = (udaje[4] + ";" + udaje[0] + ";" + udaje[1] + ";" + udaje[2] + ";" + udaje[3] + ";" + miestnost + ";" + udaje[5] + ";" + udaje[17] + ";" + udaje[19] + ";" + poznamka);
                    System.out.println(uprRiadok);
                    pw.println(uprRiadok);

                    // Kompletné údaje súboru    
                } else {

                    String uprRiadok2 = (udaje[4] + ";" + udaje[0] + ";" +  udaje[1] + ";" + udaje[2] + ";" + udaje[3] + ";" + miestnost + origData);
                    System.out.println(uprRiadok2);
                    pw.println(uprRiadok2);
                }
              
            }
        }
        pw.close();

    }

    public void set581R() {

        // Udaje o BD, dat odpoctu a typ merača 
        NodeList list = doc.getElementsByTagName("Group");
        Node node = list.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            udajeBd = element.getAttribute("Caption");
            System.out.println(udajeBd);
        }
        list = doc.getElementsByTagName("Task");
        for (int temp = 0; temp < list.getLength(); temp++) {
            node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String vchPosByt = element.getAttribute("Caption");
                int index = vchPosByt.indexOf(" ", 1);
                String voda = vchPosByt.substring(0, index);
                vchod = vchPosByt.substring(index + 1, index + 3);
                posch = vchPosByt.substring(index + 3, index + 6);
                byt = vchPosByt.substring(index + 7, index + 10);
                meno = vchPosByt.substring(index + 11);
                origData = element.getTextContent().replaceAll("\\s+ ", ";");
                riadok = voda + ";" + vchod + ";" + posch + ";" + byt + ";" + meno + origData;
                System.out.println(riadok);
                pw.println(riadok);

            }
        }
        pw.close();

    }

    public void setVerzia(Boolean verzia) {
        this.verzia = verzia;
    }

}

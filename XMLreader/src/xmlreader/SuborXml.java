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
                riadok = "Nerozpoznan?? typ mera??a,alebo s??boru."; 
                System.out.println(riadok);
                pw.println(riadok);
                pw.close();
        }

    }

    
    public void set566R() {

        // Udaje o BD, dat odpoctu a typ mera??a 
        NodeList list = doc.getElementsByTagName("Group");
        Node node = list.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            udajeBd = element.getAttribute("Caption");
            pw.println(udajeBd);
            System.out.println(udajeBd); 
            //Z??pis hlavi??ky s??boru
            if (verzia) {
                pw.println("\nEv.????slo;Priezvisko a Meno;Vchod;Poschodie;??.bytu;Miestnos??;??.PRVN;Aktu??lna spotreba;Minuloro??n?? spotreba;Pozn??mka");
                System.out.println("\nEv.????slo;Priezvisko a Meno;Vchod;Poschodie;??.bytu;Miestnos??;??.PRVN;Akt.spotreba;Minuloro??n?? spotreba;Pozn??mka");
            } else {
                String datSp = "";
                for (int i = 2; i < 19; i++) {
                    datSp = datSp.concat("D??tum_" + i + ";" + "Spotreba_" + i + ";");
                }
                pw.println("\nEv.????slo;Priezvisko a Meno;Vchod;Poschodie;??.bytu;Miestnos??;Ev.????slo;??.mera??a;Typ_odpo??tu;Pr??znak;PC_??as;??.mera??a;V??robca;Typov??_??.;Typ_mera??a;Pr??stupy;Staus;Podpis;??as_mera??a;Akt.spotreba;Neident.d??t.;Minuloro??n??_spotreba;Max.tepl.rad.MR;Jed.;D??tum_1;Spotreba_1;Register;" + datSp + "Koef 1;Koef 2;Tep.rad;Jed.;Tep.miestnos??;Jed.;Max.tep.rad.akt.r.;Jed.;Otvorenie;Jed.;D??t.posl.otvorenia;D??t.aktiv??cie;Soft.verzia;Err.flag;StateOfParameterActivation;DeviceAccessRightLevel;Tovaren.????slo;Pozn??mka;Pozn??mka");
                System.out.println("\nEv.????slo;Priezvisko a Meno;Vchod;Poschodie;??.bytu;Miestnos??;Ev.????slo;??.mera??a;Typ odpo??tu;Pr??znak;PC ??as,??.mera??a;V??robca;Typ.??.;Typ mera??a;Pr??tupy;Staus;Podpis;??as mera??a;Akt.spotreba;Neident.d??t.;Minuloro??n?? spotreba;Max.tepl.rad.MR;Jed.;D??tum 1;Spotreba 1;Register;" + datSp + "Koef 1;Koef 2;Tep.rad;Jed.;Tep.miestnos??;Jed.;Max.tep.rad.akt.r.;Jed.;Otvorenie;Jed.;D??t.posl.otvorenia;D??t.aktiv??cie;Soft.verzia;Err.flag;StateOfParameterActivation;DeviceAccessRightLevel;Tovaren.????slo;Pozn??mka;Pozn??mka");
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

                // Skr??ten?? verzia s??boru
                if (verzia) {
                    for (int i = 0; i < 74; i++) {
                        pomRetaz = pomRetaz.concat(";neodpo????tan??");
                    }
                    switch (udaje[14]) {
                        case "80":
                            poznamka = "Mo??n?? z??sah,vykona?? kontrolu.";
                            break;
                        case "48":
                            poznamka = "Chyba teploty,vykona?? kontrolu.";
                            break;
                        default:
                            poznamka = "";
                    }
                    String uprRiadok = (udaje[4] + ";" + udaje[0] + ";" + udaje[1] + ";" + udaje[2] + ";" + udaje[3] + ";" + miestnost + ";" + udaje[5] + ";" + udaje[17] + ";" + udaje[19] + ";" + poznamka);
                    System.out.println(uprRiadok);
                    pw.println(uprRiadok);

                    // Kompletn?? ??daje s??boru    
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

        // Udaje o BD, dat odpoctu a typ mera??a 
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

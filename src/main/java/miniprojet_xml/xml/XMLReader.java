package miniprojet_xml.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import miniprojet_xml.model.Produit;

public interface XMLReader {
	public void readAndDisplay(File file) throws JDOMException, IOException;
	
	public ArrayList<Produit> readAndMakeProduit(File file) throws JDOMException, IOException;
}

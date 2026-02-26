package miniprojet_xml.miniprojet_xml;

import java.io.InputStream;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

public class XMLParser {
	
	public Document createDocument(String root, String file) {
		Element rootxml = new Element(root);
		DocType docType = new DocType(root, file);
		Document document = new Document(rootxml, docType);
		return document;
	}
	
	public void display(String path) throws JDOMException {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = getClass().getResourceAsStream(path);
			if(is == null) {
				System.out.println("Fichier introuvable !");
				return;
			}
			Document document = builder.build(is);
			XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			output.output(document, System.out);
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}
}

package com.t3.common.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.t3.common.models.Bundle;

public class ConfigUltils {
	private static final String CONFIG_FILE_NAME = "config.xml";
	private static final String CONFIG_TAG_NAME	= "config";
	
	private Document document;
	
	public ConfigUltils() throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = documentBuilder.parse(CONFIG_FILE_NAME);
	}
	
	public Document getDocument() {
		return document;
	}
	
	
	public static Bundle getClientConfigure() throws Exception {
		ConfigUltils configUltils = new ConfigUltils();
		Document document = configUltils.getDocument();
		
		NodeList nodels= document.getElementsByTagName(CONFIG_TAG_NAME);
		NodeList children = nodels.item(0).getChildNodes();
		
		int lang = 0;
		String server = "";
		int port = 0;
		boolean save = false;
		for (int j = 0; j < children.getLength(); j++) {
			Node node=children.item(j);
			if(node!=null)
			{
				if(node.getNodeName().equalsIgnoreCase("lang")) {
					lang = Integer.parseInt(node.getTextContent().trim());
				} else if("server".equals(node.getNodeName())) {
					server = node.getTextContent().trim();
				} else if("port".equals(node.getNodeName())) 
					port = Integer.parseInt(node.getTextContent().trim());
				if(node.getNodeName().equals("save")) {
					save = Boolean.parseBoolean(node.getTextContent().trim());
				}
			}
		}
		Bundle bundle = new Bundle();
		bundle.putInt("lang", lang);
		bundle.putString("server", server);
		bundle.putInt("port", port);
		bundle.putBoolean("save", save);
		return bundle;
	}

	public static boolean updateClientConfigure(String key, Bundle configure) throws Exception {
		ConfigUltils configUltils = new ConfigUltils(); 
		Document doc = configUltils.getDocument();
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression ex= xPath.compile("//config[server=\""+ key +"\"]");
		Object ob=ex.evaluate(doc.getDocumentElement(), XPathConstants.NODE);

		if(ob!=null)
		{
			Node node=(Node) ob;
			NodeList chil=node.getChildNodes();
			for (int i = 0; i < chil.getLength(); i++) {
				Node n=chil.item(i);
				if(n.getNodeName().equalsIgnoreCase("lang")) {
					n.setTextContent(configure.getInt("lang") + "");
				}
				if("server".equals(n.getNodeName())) 
					n.setTextContent(configure.getString("server"));
				if("port".equals(n.getNodeName())) 
					n.setTextContent("" + configure.getInt("port"));
				if(n.getNodeName().equalsIgnoreCase("save")) {
					n.setTextContent("" + configure.getBoolean("save"));
				}
			}
			savefile(doc);
			return true;
		}
		else
			return false;

	}
	
	private static void savefile(Document document) throws  Exception {
		
		DOMSource source = new DOMSource(document);
		StreamResult result=new StreamResult(new File(CONFIG_FILE_NAME));
		Transformer trs=TransformerFactory.newInstance().
				newTransformer();
		trs.setOutputProperty(OutputKeys.INDENT, "yes");
		trs.transform(source, result);

	}
}

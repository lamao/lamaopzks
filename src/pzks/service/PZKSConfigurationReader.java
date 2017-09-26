package pzks.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class PZKSConfigurationReader 
{
//	public static final String RENDERER_KEY = "renderer";
	
	private XPath _xpath = null;
	private Document _doc = null;
	
	public static PZKSConfigurationReader _defaultReader = null;
	public static PZKSConfigurationReader getDefaultReader()
	{
		if (_defaultReader == null)
		{
			_defaultReader = new PZKSConfigurationReader();
		}
		return _defaultReader;
	}
	
	
	public boolean readConfiguration(String filename) 
	{
		File file = new File(filename);
		return readConfiguration(file);
	}
	
	public boolean readConfiguration(File file) 
	{
		try 
		{
			init(file);

			NodeList configuration = (NodeList)_xpath.evaluate(
					"/configuration/*", _doc, XPathConstants.NODESET);
//			// read configuration
//			Node configuration = (Node) _xpath.evaluate("/configuration/user[@login='"
//					+ System.getProperty("login") + "']", _doc,	XPathConstants.NODE);
//
//			if (configuration == null || !((Element) configuration).
//					getAttribute("password").equals(System.getProperty("password"))) 
//			{
//				return false;
//			}
//
//			if (configuration.hasChildNodes()) 
//			{
//				NodeList parameters = configuration.getChildNodes();
				NodeList parameters = configuration;
				for (int j = 0; j < parameters.getLength(); j++) 
				{
					if (parameters.item(j) instanceof Element) 
					{
						Element key = (Element) parameters.item(j);
						System.setProperty(key.getTagName(), key.getTextContent());
					}
				}
//			}
		} 
		catch (ParserConfigurationException e) 
		{
			System.out.println("Error during parser configuration");
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			System.out.println("SAXException during parsing. Some error in"
					+ "configuration.xml");
			System.out.println(e.getMessage());
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File with configuration not found");
			if (file != null)
			{
				System.out.println("Filename is " + file.getAbsolutePath());
			}
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("IOException during parsing configuration file");
			e.printStackTrace();
		}
		catch (XPathExpressionException e) 
		{
			System.out.println("Error during reading xml");
			e.printStackTrace();
		}

		return true;
	}
	
	public void init(File file) 
			throws ParserConfigurationException, SAXException, IOException
	{
		if (file == null || !file.exists())
		{
			throw new FileNotFoundException();
		}
		
		// construct DOM document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		final String JAXP_SCHEMA_LANGUAGE =
             "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
        final String W3C_XML_SCHEMA =
             "http://www.w3.org/2001/XMLSchema"; 
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
        factory.setIgnoringElementContentWhitespace(true);
        
		DocumentBuilder builder = factory.newDocumentBuilder();
		_doc = builder.parse(file);
		
		removeWhitespaces(_doc.getDocumentElement());
		
		// construct xpath classes
		XPathFactory xpathFactory = XPathFactory.newInstance();
		_xpath = xpathFactory.newXPath();
	}
	
	private void removeWhitespaces(Node node)
	{
		NodeList children = node.getChildNodes();
		if (children.getLength() > 1)
		{
			int count = 0;
			for (int i = children.getLength() - 1; i >= 0; i--)
			{
				Node child = children.item(i);
				if (child instanceof Text && ((Text) child).getData().trim().length() == 0)
				{
					node.removeChild(child);
					count++;
				}
				else if (child instanceof Element)
				{
					removeWhitespaces((Element) child);
				}
			}
		}
	} 

}

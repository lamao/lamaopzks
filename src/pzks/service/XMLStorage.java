package pzks.service;

/**
 @version 1.10 2009-03-01
 @author Denis
 */

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * This storage uses an XML file to invoke components 
 */
public class XMLStorage 
{
    /**
     * If value starts with this prefix it means that it is not title, but
     * just a key for localized entry in messages_*.properties and.
     */
    private final String localizedValuePrefix = "##";
    
	/**
	 * Constructs a storage.
	 * @param filename - the name of the XML file that contains components
	 */

	public XMLStorage(String filename) 
	{

		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(filename));

			parseResourse(doc.getDocumentElement());
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Removes whitespace from element content
	 * 
	 * @param e - the root element
	 * @return the number of whitespace nodes that were removed.
	 */
	
//	private int removeWhitespace(Element e) 
//	{
//		NodeList children = e.getChildNodes();
//		if (children.getLength() <= 1)
//			return 0;
//		int count = 0;
//		for (int i = children.getLength() - 1; i >= 0; i--) 
//		{
//			Node child = children.item(i);
//			if (child instanceof Text
//					&& ((Text) child).getData().trim().length() == 0) 
//			{
//				e.removeChild(child);
//				count++;
//			} 
//			else if (child instanceof Element)
//			{
//				count += removeWhitespace((Element) child);
//			}
//		}
//		return count;
//	}

	/**
	 * Gets a component with a given name
	 * 
	 * @param name - a component name
	 * @return the component with the given name, or null if no component in
	 *         this resource has the given name
	 */

	public Component get(String name) 
	{
		for (int i = 0; i < components.size(); i++) 
		{
			if (components.get(i).getName().equals(name))
			{
				return components.get(i);
			}
		}

		return null;
	}

	/**
	 * Parses a resourse element.
	 * 
	 * @param e - a resourse element
	 */

	private void parseResourse(Element e) 
	{
		NodeList components = e.getChildNodes();
		for (int i = 0; i < components.getLength(); i++) 
		{
			Element item = (Element) components.item(i);
			parseItem(item);
		}
	}

	/**
	 * Parses an item element.
	 * 
	 * @param e - an item element
	 */

	private void parseItem(Element e) 
	{
		JComponent comp = (JComponent) parseBean((Element) e.getFirstChild());
		components.add(comp);
	}

	/**
	 * Parses a bean element.
	 * 
	 * @param e - a bean element
	 */

	private Object parseBean(Element e) 
	{
		Object obj;

		try 
		{

			NodeList children = e.getChildNodes();
			Element classElement = (Element) children.item(0);
			String className = ((Text) classElement.getFirstChild()).getData();

			Class<?> cl = Class.forName(className);

			if (className.contains("Icon")) 
			{
				Class<?>[] argsClass = new Class[] { String.class };
				Object[] args = new Object[] { e.getAttribute("url") };
				Constructor<?> constructor = cl.getConstructor(argsClass);

				obj = createObject(constructor, args);
				return obj;
			}

			obj = cl.newInstance();

			if (obj instanceof JComponent)
			{
				((JComponent) obj).setName(e.getAttribute("id"));
			}
			
			for (int i = 1; i < children.getLength(); i++) 
			{
				Node propertyElement = children.item(i);
				Element nameElement = (Element) propertyElement.getFirstChild();

				if (propertyElement.getNodeName().equals("subitem")) 
				{
					((JComponent) obj).add((JComponent) parseBean(nameElement));
					continue;
				}

				String propertyName = ((Text) nameElement.getFirstChild())
						.getData();
				Element valueElement = (Element) propertyElement.getLastChild();
				Object value = parseValue(valueElement);
				
				
				// TODO remove storing action from here
				// store <action,button> pair
				if (propertyName.equals("actionCommand"))
				{
					PZKSButtonActionsManager.defaultManager()
						.addAction((String)value, (AbstractButton)obj);
				}
				else if (propertyName.equals("text"))
				{
				    String s = (String)value;
				    if (s.startsWith(localizedValuePrefix))
				    {
				        ResourceBundle bundle = PZKSAppStrings.getBundle();
				        String key = s.substring(localizedValuePrefix.length());
				        value = bundle.getString(key);
				    }
				}
				    
				
				BeanInfo beanInfo = Introspector.getBeanInfo(cl);
				PropertyDescriptor[] descriptors = beanInfo
						.getPropertyDescriptors();

				
				boolean done = false;

				for (int j = 0; !done && j < descriptors.length; j++) 
				{
					if (descriptors[j].getName().equals(propertyName)) 
					{
						descriptors[j].getWriteMethod().invoke(obj, value);
						done = true;
					}
				}
			}

			return obj;
		} 
		catch (Exception ex) 
		{ 
			// the reflection methods can throw various exceptions
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Parses a value element.
	 * 
	 * @param e - a value element
	 */

	private Object parseValue(Element e) 
	{
		Element child = (Element) e.getFirstChild();

		if (child.getTagName().equals("bean")) 
		{
			return parseBean(child);
		}

		String text = ((Text) child.getFirstChild()).getData();

		if (child.getTagName().equals("int")) 
		{
			return new Integer(text);

		} 
		else if (child.getTagName().equals("boolean")) 
		{
			return new Boolean(text);
		} 
		else if (child.getTagName().equals("string")) 
		{
			return text;
		}

		else
			return null;
	}

	/**
	 * Object Reflection: invoke constructor with parameters
	 * 
	 * @param constructor
	 * @param arguments
	 */

	public static Object createObject(Constructor<?> constructor, Object[] arguments) 
	{

		//System.out.println("Constructor: " + constructor.toString());
		Object object = null;

		try 
		{
			object = constructor.newInstance(arguments);

			return object;

		} 
		catch (InstantiationException e) 
		{
			System.out.println(e);
		} 
		catch (IllegalAccessException e) 
		{
			System.out.println(e);
		} 
		catch (IllegalArgumentException e) 
		{
			System.out.println(e);
		} 
		catch (InvocationTargetException e) 
		{
			System.out.println(e);
		}

		return object;
	}

	private ArrayList<Component> components = new ArrayList<Component>();
}

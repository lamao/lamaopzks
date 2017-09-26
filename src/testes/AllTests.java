package testes;

import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.runner.LoadingTestCollector;

public class AllTests
{

	public static Test suite() throws ClassNotFoundException
	{
		TestSuite suite = new TestSuite("Test for all testes");
		//$JUnit-BEGIN$
		
		Enumeration<?> enu = new LoadingTestCollector().collectTests();
		while (enu.hasMoreElements()) 
		{
			String testClassName = (String)enu.nextElement();
			if (testClassName.startsWith("testes") && 
				!testClassName.startsWith("testes.AllTests")) 
			{
				suite.addTestSuite(Class.forName(testClassName));
				System.out.println(testClassName);
			}
		}
		//$JUnit-END$
		return suite;
	}
	
}

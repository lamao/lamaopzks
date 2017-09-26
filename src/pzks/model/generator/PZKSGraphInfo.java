package pzks.model.generator;

import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import pzks.service.PZKSAppStrings;

public class PZKSGraphInfo 
{
	
	private int _maxNodeValue;
	private int _minNodeValue; 
	private int _maxLinkValue;
	private int _minLinkValue;
	private int _connectivity;

	int _numVerses;
	int _numLinks;

	Random random;  
	
	public PZKSGraphInfo(int aConnectivity, int aNumVerses, int aMinNodeValue, 
			int aMaxNodeValue, int aMinLinkValue, int aMaxLinkValue) 
	{
		_connectivity = aConnectivity;
		_numVerses = aNumVerses;
		_maxNodeValue = aMaxNodeValue;
		_minNodeValue = aMinNodeValue;
		_maxLinkValue = aMaxLinkValue;
		_minLinkValue = aMinLinkValue;
		random = new Random();
	}

	
	public int getMaxNodeValue()
	{
		return _maxNodeValue;
	}

	public int getMinNodeValue()
	{
		return _minNodeValue;
	}

	public int getMaxLinkValue()
	{
		return _maxLinkValue;
	}

	public int getMinLinkValue()
	{
		return _minLinkValue;
	}

	public int getNumVerses()
	{
		return _numVerses;
	}

	public int getConnectivity()
	{
		return _connectivity;
	}

	public int EvalLinkValue()
	{
		return _minLinkValue + random.nextInt(_maxLinkValue - _minLinkValue);
	}

	public int EvalNodeValue()
	{
		return _minNodeValue + random.nextInt(_maxNodeValue - _minNodeValue);
	}
	

	public boolean Validate()
	{
		if (_connectivity < 1 || _connectivity > 99 || _numVerses < 1 || _minNodeValue < 0 || 
				_maxNodeValue < 0 || _minLinkValue < 0 || _maxLinkValue < 0	||
				_minNodeValue > _maxNodeValue || _minLinkValue > _maxLinkValue)
		{
		    ResourceBundle bundle = PZKSAppStrings.getBundle();
			JOptionPane.showMessageDialog(null, 
			        bundle.getString(PZKSAppStrings.GRPAH_INFO_WRONG_DATA_TITLE_KEY),
			        bundle.getString(PZKSAppStrings.GRAPH_INFO_WRONG_DATA_STRING_KEY), 
			        JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
		{
			return true;
		}
	}

}

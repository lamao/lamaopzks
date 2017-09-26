package pzks.service;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class stores keys for all strings in application 
 * that should be translated. Localization is organized through
 * Bundle*.properties.
 */
public class PZKSAppStrings
{
    public final static String BUNDLE_NAME = "localization/messages";
    
    public final static String MAIN_FRAME_TITLE_KEY = "LocalisedFrame.title";
    public final static String SYSTEM_GRAPH_TAB_TITLE_KEY = "LocalisedFrame.systemGraph.title";
    public final static String TASK_GRAPH_TAB_TITLE_KEY = "LocalisedFrame.taskGraph.title";
    
    public final static String ABOUT_BOX_TITLE_KEY = "LocalisedFrame.aboutBox.title";
    public final static String IMMERSE_DIALOG_TITLE_KEY = "LocalisedFrame.immerseDialog.title";
    
    public final static String GRPAH_INFO_WRONG_DATA_TITLE_KEY = "LocalisedFrame.graphInfo.wrongData.title";
    public final static String GRAPH_INFO_WRONG_DATA_STRING_KEY = "LocalisedFrame.graphInfo.wrongData.string";
    
    public final static String OPENSAVE_FILE_FILTER_NAME_KEY = "LocalisedFrame.opensaveFileFilter.name";
    
    public final static String GRAPH_VALIDATION_TITLE_KEY = "LocalisedFrame.graphValidation.title";
    public final static String GRAPH_VALIDATION_FALSE_KEY = "LocalisedFrame.graphValidation.false";
    public final static String GRAPH_VALIDATION_TRUE_KEY = "LocalisedFrame.graphValidation.true";
    
    public final static String GRAPH_INPUT_WEIGHT_NODE_STRING_KEY = "LocalisedFrame.inputWeightOfNode.string";
    public final static String GRAPH_INPUT_WEIGHT_CONNECTION_STRING_KEY = "LocalisedFrame.inputWeightOfConnection.string";    
    public final static String INVALID_VALUE_KEY = "LocalisedFrame.invalidValue.string";
    public final static String ERROR_KEY = "LocalisedFrame.error.string";
    
       
    public static ResourceBundle getBundle()
    {
        return ResourceBundle.getBundle(BUNDLE_NAME, 
                new Locale(System.getProperty("language")));
    }
}

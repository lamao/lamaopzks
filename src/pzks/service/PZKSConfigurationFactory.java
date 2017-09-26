package pzks.service;

import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.planners.assignment.PZKSAssignmentElement;
import pzks.model.planners.assignment.PZKSAssignmentPlanner;
import pzks.model.planners.assignment.PZKSAssignmentStrategy;
import pzks.model.planners.assignment.PZKSPreactStrategy;
import pzks.model.planners.assignment.PZKSVariant3AssignmentPlanner;
import pzks.model.planners.queue.PZKSQueueElement;
import pzks.model.planners.queue.PZKSQueuePlanner;
import pzks.model.planners.queue.PZKSVariant11QueuePlanner;
import pzks.model.planners.queue.PZKSVariant2QueuePlanner;
import pzks.model.planners.queue.PZKSVariant9QueuePlanner;
import pzks.ui.dialogs.ImmerseDialog;
import pzks.ui.dialogs.PZKSAbstractGenerateDialog;
import pzks.ui.dialogs.PZKSLamaoGenerateDialog;
import pzks.ui.dialogs.PZKSLamaoImmerseDialog;
import pzks.ui.renderer.PZKSGantRenderer;
import pzks.ui.renderer.PZKSLamaoGantRenderer;
import pzks.ui.renderer.PZKSLamaoRenderer;
import pzks.ui.renderer.PZKSRenderer;

public class PZKSConfigurationFactory 
{

	//******************************* Renderers *******************************
	public static PZKSRenderer createRenderer(PZKSGraph graph)
	{
		PZKSRenderer renderer = null;
		
		String className = System.getProperty("renderer");
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(PZKSGraph.class);
			renderer = (PZKSRenderer) constructor.newInstance(graph);
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating renderer " + className + 
					". Default renderer will be used");
			System.out.println(e.getMessage());
			renderer = new PZKSLamaoRenderer(graph);
		}
		
		return renderer;		
	}
	
	public static PZKSGantRenderer createGantRenderer(Queue<List<PZKSAssignmentElement>> elements)
	{
		PZKSGantRenderer renderer = null;
		
		String className = System.getProperty("gant-renderer");
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(Queue.class);
			renderer = (PZKSGantRenderer) constructor.newInstance(elements);
		}
		catch (Exception e)
		{
			System.out.println("Error during creating gant renderer " + className + 
					". Default renderer will be used");
			System.out.println(e.getMessage());
			renderer = new PZKSLamaoGantRenderer(elements);
		}
		return renderer;
	}

	//******************************* Queue planners ***************************
	public static PZKSQueuePlanner createFirstQueuePlanner(PZKSGraph graph)
	{
		PZKSQueuePlanner planner = null;
		
		String className = System.getProperty("queue1");
		planner = createQueuePlanner(className, graph);
		if (planner == null)
		{
			planner = new PZKSVariant2QueuePlanner(graph);
		}
		
		return planner;
	}
	public static PZKSQueuePlanner createSecondQueuePlanner(PZKSGraph graph)
	{
		PZKSQueuePlanner planner = null;
		
		String className = System.getProperty("queue2");
		planner = createQueuePlanner(className, graph);
		if (planner == null)
		{
			planner = new PZKSVariant9QueuePlanner(graph);
		}
		
		return planner;
	}
	public static PZKSQueuePlanner createThirdQueuePlanner(PZKSGraph graph)
	{
		PZKSQueuePlanner planner = null;
		
		String className = System.getProperty("queue3");
		planner = createQueuePlanner(className, graph);
		if (planner == null)
		{
			planner = new PZKSVariant11QueuePlanner(graph);
		}
		
		return planner;
	}
	
	private static PZKSQueuePlanner createQueuePlanner(
						String className, PZKSGraph graph)
	{
		PZKSQueuePlanner planner = null;
		
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(PZKSGraph.class);
			planner = (PZKSQueuePlanner) constructor.newInstance(graph);
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating queue planner " + className + 
					". Default planner will be used");
			//System.out.println(e.getMessage());
		}
		
		return planner; 
	}
	
	//************************ Assignment planners *****************************
	public static PZKSAssignmentPlanner createFirstAssignmentPlanner(
			Queue<PZKSQueueElement> queue, PZKSGraph systemGraph)
	{
		String strategyName = System.getProperty("planner-strategy1");
		PZKSAssignmentStrategy strategy = createAssignmentStrategy(strategyName);
		
		PZKSAssignmentPlanner planner = null;
		
		String className = System.getProperty("planner1");
		planner = createAssignmentPlanner(className, queue, systemGraph, strategy);
		if (planner == null)
		{
			planner = new PZKSVariant3AssignmentPlanner(queue, systemGraph, strategy);
		}
		
		return planner;
	}
	public static PZKSAssignmentPlanner createSecondAssignmentPlanner(
			Queue<PZKSQueueElement> queue, PZKSGraph systemGraph)
	{
		String strategyName = System.getProperty("planner-strategy1");
		PZKSAssignmentStrategy strategy = createAssignmentStrategy(strategyName);

		
		PZKSAssignmentPlanner planner = null;
		
		String className = System.getProperty("planner2");
		planner = createAssignmentPlanner(className, queue, systemGraph, strategy);
		if (planner == null)
		{
			planner = new PZKSVariant3AssignmentPlanner(queue, systemGraph, strategy);
		}
		
		return planner;
	}
		
	private static PZKSAssignmentPlanner createAssignmentPlanner(
						String className, Queue<PZKSQueueElement> queue,
						PZKSGraph systemGraph, PZKSAssignmentStrategy strategy)
	{
		PZKSAssignmentPlanner planner = null;
		
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(Queue.class, 
							PZKSGraph.class, PZKSAssignmentStrategy.class);
			planner = (PZKSAssignmentPlanner) constructor.newInstance(queue,
					systemGraph, strategy);
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating assignment planner " + className + 
					". Default planner will be used");
			e.printStackTrace();
		}
		
		return planner; 
	}
	
	//******************************* assignment strategy **********************
	public static PZKSAssignmentStrategy createAssignmentStrategy(String className)
	{
		PZKSAssignmentStrategy strategy = null;
		
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor();
			strategy = (PZKSAssignmentStrategy) constructor.newInstance();
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating assignment strategy " 
					+ className + ". Default strategy will be used");
			e.printStackTrace();
			
			strategy = new PZKSPreactStrategy();
		}
		
		return strategy; 
	}
	
	//******************************* dialogs **********************************
	public static ImmerseDialog createImmerseDialog(Window window, String title)
	{
		ImmerseDialog dialog = null;
		
		String className = System.getProperty("immerse-dialog");
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(
							Window.class, String.class);
			dialog = (ImmerseDialog) constructor.newInstance(window, title);
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating immerse dialog " + 
					className + ". Default dialog will be used");
			System.out.println(e.getMessage());
			dialog = new PZKSLamaoImmerseDialog(window, title);
		}
		
		return dialog;
	}
	
	public static PZKSAbstractGenerateDialog createGeneraetDialog(Frame owner)
	{
		PZKSAbstractGenerateDialog dialog = null;
		
		String className = System.getProperty("generate-dialog");
		Class<?> clazz = null;
		try 
		{
			clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor(
							Frame.class);
			dialog = (PZKSAbstractGenerateDialog) constructor
						.newInstance(owner);
		} 
		catch (Exception e) 
		{
			System.out.println("Error during creating generate dialog " + 
					className + ". Default dialog will be used");
			System.out.println(e.getMessage());
			dialog = new PZKSLamaoGenerateDialog(owner);
		}
		
		return dialog;
	}
}

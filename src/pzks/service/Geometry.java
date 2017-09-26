package pzks.service;

import java.awt.Point;

public class Geometry 
{
	public static double getDistanceToLine(Point a, Point b, Point m)
	{
		float A = b.y - a.y;
		float B = a.x - b.x;
		float C = a.y * b.x - a.x * b.y;
		
		double result = Math.abs(A * m.x + B * m.y + C) / Math.sqrt(A * A + B * B);
		return result;
	}

}

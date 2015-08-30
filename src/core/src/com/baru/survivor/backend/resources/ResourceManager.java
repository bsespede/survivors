package com.baru.survivor.backend.resources;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import com.baru.survivor.backend.map.TileType;

public class ResourceManager {

	private Map<Point, Resource> resources = new HashMap<Point, Resource>();
	
	public void generateFood(Point spawnablePoint, long curTime, int amount) {
		resources.put(spawnablePoint, new Resource(TileType.FOOD, curTime, amount));
	}

	public void generateWater(Point spawnablePoint, long curTime, int amount) {
		resources.put(spawnablePoint, new Resource(TileType.LAKE, curTime, amount));
	}
	
	public Resource getResource(int x, int y) {
		Point resourcePoint = new Point(x,y);
		if (resources.containsKey(resourcePoint)){
			return resources.get(resourcePoint);
		} else {
			return null;
		}
	}
	
}
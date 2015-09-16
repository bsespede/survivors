package com.baru.survivor.backend.resources;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ReservoirManager {

	private Map<Point, Reservoir> reservoirs = new HashMap<Point, Reservoir>();
	
	public void generateFood(Point spawnablePoint, long curTime, int amount) {
		reservoirs.put(spawnablePoint, new Reservoir(ResourceType.FOOD, amount));
	}

	public void generateWater(Point spawnablePoint, long curTime, int amount) {
		reservoirs.put(spawnablePoint, new Reservoir(ResourceType.WATER, amount));
	}
	
	public Reservoir getReservoir(int x, int y) {
		Point reservoirPoint = new Point(x,y);
		if (reservoirs.containsKey(reservoirPoint)){
			return reservoirs.get(reservoirPoint);
		} else {
			return null;
		}
	}
	
}
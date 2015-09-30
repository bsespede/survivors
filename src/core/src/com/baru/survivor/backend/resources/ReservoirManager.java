package com.baru.survivor.backend.resources;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReservoirManager implements Serializable{

	private Map<Point, Reservoir> reservoirs = new HashMap<Point, Reservoir>();
	
	public void generateFood(Point spawnablePoint, long curTime, int amount) {
		reservoirs.put(spawnablePoint, new Reservoir(ResourceType.FOOD, amount));
	}

	public void generateWater(Point spawnablePoint, long curTime, int amount) {
		reservoirs.put(spawnablePoint, new Reservoir(ResourceType.WATER, amount));
	}

	public Reservoir getReservoirAt(Point position) {
		if (reservoirs.containsKey(position)){
			return reservoirs.get(position);
		} else {
			return null;
		}
	}
	
}
package com.baru.survivor.backend.pheromones;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.baru.survivor.backend.agents.Direction;
import com.baru.survivor.backend.map.TerrainManager;

public class Pheromones implements Serializable{
	
	private float pheromones[][];
	private int width;
	private int height;
	
	private final float minPheromones = 0.0001f;
	private final float maxPheromones = 1f;
	private final float stepPheromone = 0.1f;
	private final float pheromoneLoss = 0.01f;
	private final float interestCoeff = 10f;
	private final float pheromoneCoeff = 0f;
	
	private final float randomMoveH = 0.2f;
	
	public Pheromones(int width, int height){
		this.pheromones = new float[width][height];
		this.width = width;
		this.height = height;
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				pheromones[x][y] = minPheromones;
			}
		}
	}
	
	public void addPheromone(int x, int y){
		pheromones[x][y] = (pheromones[x][y]+stepPheromone > maxPheromones)? maxPheromones: pheromones[x][y]+stepPheromone;
	}
	
	public void evaporatePheromones(){
		for (int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pheromones[x][y] = (pheromones[x][y]-pheromoneLoss < minPheromones)? minPheromones: pheromones[x][y]-pheromoneLoss;
			}
		}
	}
	
	public Direction getDirFrom(TerrainManager terrainManager, Point position, Point lastPosition, Point goal){
		Map<Direction, Double> directionsValues = new HashMap<Direction, Double>();
		double totalValue = 0;
		for (int x = -1; x <= 1; x++){
			for (int y = -1; y <= 1; y++){
				if (!(x == 0 && y == 0)){
					Direction dir = Direction.valueOf(x, y);
					Point target = new Point(position.x+x, position.y+y);
					double difference = 1;
					if (lastPosition != null){
						difference = eulerDist(lastPosition, target);
					}
					double interest = (goal == null)? randomMoveH * difference: 1/bfsDist(target, goal, new HashMap<Point, Integer>, 0);
					if (TerrainManager.isValidPoint(target) && !terrainManager.isBlocked(target)){
						if (interest == Double.POSITIVE_INFINITY){
							return dir;
						}
						double dirValue = Math.pow(pheromones[target.x][target.y], pheromoneCoeff) * Math.pow(interest, interestCoeff);
						directionsValues.put(dir, dirValue);
						totalValue += dirValue;
					}
				}
			}
		}
		return throwDiceForDirection(directionsValues, totalValue);
	}

	private int bfsDist(Point cur, Point goal,
			HashMap<Point, Integer> hashMap, int length) {
		if (cur.equals(goal)){
			return length;
		}
		if ()
		return 0;
	}

	private Direction throwDiceForDirection(
			Map<Direction, Double> directionsValues, double totalValue) {
		Random rnd = new Random();
		double diceValue = rnd.nextFloat() * totalValue;
		double sum = 0;
		for (Direction dir: directionsValues.keySet()){
			Double value = directionsValues.get(dir);
			sum += value;
			if (diceValue <= sum){	
				return dir;
			}
		}
		return null;
	}

	private double eulerDist(Point from, Point to){
		return Math.sqrt(Math.pow(to.x - from.x, 2)+Math.pow(to.y - from.y, 2));
	}
	
	public float getIntensity(int x, int y) {
		return pheromones[x][y];
	}

	public float getMaxPheromones() {
		return maxPheromones;
	}
}

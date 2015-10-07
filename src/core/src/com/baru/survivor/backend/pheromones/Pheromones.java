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
	private final float minPheromones = 1f;
	private final float maxPheromones = 50f;
	private final float stepPheromone = 1f;
	private final float pheromoneLoss = 0.2f;
	private final float interestCoeff = 1f;
	private final float pheromoneCoeff = 1f;
	
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
		pheromones[x][y] += (pheromones[x][y]+stepPheromone > maxPheromones)? maxPheromones: pheromones[x][y]+stepPheromone;
	}
	
	public void evaporatePheromones(){
		for (int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pheromones[x][y] = (pheromones[x][y]-pheromoneLoss < minPheromones)? minPheromones: pheromones[x][y]-pheromoneLoss;
			}
		}
	}
	
	public Direction getDirFrom(TerrainManager terrainManager, Point position, Point goal){
		Map<Direction, Double> directionsValues = new HashMap<Direction, Double>();
		double totalValue = 0;
		for (int x = -1; x <= 1; x++){
			for (int y = -1; y <= 1; y++){
				if (!(x == 0 && y == 0)){
					Direction dir = Direction.valueOf(x, y);
					int targetX = position.x+x;
					int targetY = position.y+y;
					double interest = (goal == null)? 1: eulerDist(position, goal);
					if (!terrainManager.isBlocked(new Point(targetX, targetY))){
						double dirValue = Math.pow(pheromones[targetX][targetY], pheromoneCoeff) * Math.pow(interest, interestCoeff);
						directionsValues.put(dir, dirValue);
						totalValue += dirValue;
						System.out.println(dir+" "+dirValue+" "+totalValue);
					}
				}
			}
		}
		return throwDiceForDirection(directionsValues, totalValue);
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
		}System.out.println(directionsValues);
		System.out.println("Total: "+ totalValue);
		System.out.println("Dice: "+diceValue);
		System.out.println("Sum: "+sum);
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

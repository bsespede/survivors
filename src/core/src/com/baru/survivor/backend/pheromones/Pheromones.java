package com.baru.survivor.backend.pheromones;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.AgentManager;
import com.baru.survivor.backend.agents.Direction;
import com.baru.survivor.backend.map.TerrainManager;

public class Pheromones implements Serializable{
	
	private float pheromones[][];
	private int width;
	private int height;
	
	private final float minPheromones = 0.0001f;
	private final float maxPheromones = 1f;
	private final float stepPheromone = 1f;
	private final float pheromoneLoss = 0.03f;
	private final float interestCoeff = 10f;
	private final float pheromoneCoeff = 0.3f;
	
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
	
	public void addPheromone(int x, int y, float intensity){
		pheromones[x][y] = (pheromones[x][y]+stepPheromone > maxPheromones)? maxPheromones: pheromones[x][y]+(stepPheromone*intensity);
	}
	
	public void evaporatePheromones(){
		for (int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pheromones[x][y] = (pheromones[x][y]-pheromoneLoss < minPheromones)? minPheromones: pheromones[x][y]-pheromoneLoss;
			}
		}
	}
	
	public Direction getDirFrom(AgentManager agentManager, Point tribePosition, TerrainManager terrainManager, Point position, Point lastPosition, Point goal){
		Map<Direction, Double> directionsValues = new HashMap<Direction, Double>();
		double totalValue = 0;
		for (int x = -1; x <= 1; x++){
			for (int y = -1; y <= 1; y++){
				if (!(x == 0 && y == 0)){
					Direction dir = Direction.valueOf(x, y);
					Point target = new Point(position.x+x, position.y+y);
					double difference;
					if (lastPosition == null){
						difference = 1;
					}else{
						difference = eulerDist(terrainManager, lastPosition, target);
					}
					double interest = (goal == null)? difference: difference/eulerDist(terrainManager, target, goal);
					if ((Survivor.pathBlockingDisabled || agentManager.noAgentsAt(target) || target.equals(tribePosition)) && 
							TerrainManager.isValidPoint(target) && !terrainManager.isBlocked(target)){
						if (interest == Double.POSITIVE_INFINITY || Double.isNaN(interest)){
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

	private double eulerDist(TerrainManager tm, Point from, Point to){
		if (tm.isBlocked(from)){
			return Double.MAX_VALUE;
		}
		return Math.sqrt(Math.pow(to.x - from.x, 2)+Math.pow(to.y - from.y, 2));
	}
	
	public float getIntensity(int x, int y) {
		return pheromones[x][y];
	}

	public float getMaxPheromones() {
		return maxPheromones;
	}
}

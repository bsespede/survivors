package com.baru.survivor.backend;

import com.baru.survivor.backend.agents.AgentManager;
import com.baru.survivor.backend.map.TerrainGenerator;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.resources.ResourceManager;
import com.baru.survivor.backend.village.Village;

public class Status {

	private TerrainManager terrainManager = new TerrainManager();
	private AgentManager agentManager = new AgentManager();
	private ResourceManager resourceManager = new ResourceManager();
	private long lastTick = System.currentTimeMillis();
	private final int tickTime = 1000;

	public void nextState(long curTime){
		if (curTime - lastTick > tickTime) {
			agentManager.tickTime();
			lastTick = curTime;
		}
	}
	
	public void create(int tribesNum, int villagersPerTribe, int foodNum, int foodDur, int lakeNum, int lakeDur) {
		generateMap();
		generateTribes(tribesNum, villagersPerTribe);
		generateFood(foodNum, foodDur);
		generateWater(lakeNum, lakeDur);
	}

	public void generateMap() {
		terrainManager = TerrainGenerator.generateMap();
	}

	public TerrainManager getTerrainManager() {
		return terrainManager;
	}
	
	private void generateTribes(int tribesNum, int villagersPerTribe) {
		for (int i = 0; i < tribesNum; i++) {
			Village tribe = new Village(terrainManager.getSpawnablePoint());
			for (int j = 0; j < villagersPerTribe; j++) {
				agentManager.generateAgent(tribe);
			}
		}		
	}
	
	private void generateWater(int lakeNum, int lakeDur) {
		long curTime = System.currentTimeMillis();
		for (int i = 0; i < lakeNum; i++) {
			resourceManager.generateWater(terrainManager.getSpawnablePoint(), curTime, lakeDur);			
		}		
	}

	private void generateFood(int foodNum, int foodDur) {
		long curTime = System.currentTimeMillis();
		for (int i = 0; i < foodNum; i++) {
			resourceManager.generateFood(terrainManager.getSpawnablePoint(), curTime, foodDur);			
		}
	}
	
}

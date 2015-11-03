package com.baru.survivor.backend;

import java.awt.Point;
import java.io.Serializable;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.AgentManager;
import com.baru.survivor.backend.agents.DayCycle;
import com.baru.survivor.backend.log.Log;
import com.baru.survivor.backend.map.TerrainGenerator;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.pheromones.Pheromones;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.village.Tribe;
import com.baru.survivor.backend.village.TribeManager;

public class State implements Serializable{

	private TerrainManager terrainManager = new TerrainManager();
	private AgentManager agentManager = new AgentManager();
	private ReservoirManager resourceManager = new ReservoirManager();
	private TribeManager tribeManager = new TribeManager();
	private DayCycle cycle = DayCycle.DAY;
	private int tickCounter = 0;
	private long lastTick = System.currentTimeMillis();
	private transient Pheromones pheromones = new Pheromones(Survivor.width, Survivor.height);
	private transient Log log;

	public void nextState(long curTime){
		long elapsedTime = curTime - lastTick;
		if (elapsedTime > Survivor.tickTime) {
			if (tickCounter >= Survivor.dayTicks * 0.75){
				cycle = DayCycle.NIGHT;
				if (tickCounter > Survivor.dayTicks){
					cycle = DayCycle.DAY;
					tickCounter = 0;
				}
			}
			agentManager.tickTime(terrainManager, resourceManager, cycle, pheromones);
			log.tick(tribeManager, resourceManager);
			lastTick = curTime;	
			tickCounter++;
		}
	}
	
	public void resetLog(){
		log = new Log(tribeManager);
	}
	
	public void resetPheromones(){
		pheromones = new Pheromones(Survivor.width, Survivor.height);
	}
	
	public State(int tribesNum, int villagersPerTribe, int foodNum, int foodDur, int lakeNum, int lakeDur) {
		generateMap();
		generateFood(foodNum, foodDur);
		generateWater(lakeNum, lakeDur);
		generateTribes(tribesNum, villagersPerTribe);
		resetLog();
	}

	public Log getLog(){
		return log;
	}
	
	public void generateMap() {
		terrainManager = TerrainGenerator.generateMap();
	}

	public TerrainManager getTerrainManager() {
		return terrainManager;
	}
	
	public DayCycle getCycle(){
		return cycle;
	}
	
	private void generateTribes(int tribesNum, int villagersPerTribe) {
		for (int i = 0; i < tribesNum; i++) {
			boolean validLocation = false;
			Point tribeLocation = null;
			while(!validLocation){
				tribeLocation = terrainManager.getSpawnablePoint();
				if (resourceManager.getReservoirAt(tribeLocation) == null){
					validLocation = true;
				}
			}
			Tribe tribe = new Tribe(tribeLocation);
			tribeManager.addTribe(tribe);
			for (int j = 0; j < villagersPerTribe; j++) {
				agentManager.generateAgent(tribeLocation, tribe);
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

	public AgentManager getAgentsManager() {
		return agentManager;
	}

	public ReservoirManager getResourceManager() {
		return resourceManager;
	}

	public TribeManager getTribeManager() {
		return tribeManager;
	}

	public Pheromones getPheromones() {
		return pheromones;
	}
	
}

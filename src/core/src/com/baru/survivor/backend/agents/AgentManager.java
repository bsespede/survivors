package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.map.TileType;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.resources.ResourceManager;

public class AgentManager {

	private List<Agent> agents = new ArrayList<Agent>();

	public void tickTime() {
		for (Agent agent: agents){
			agent.thirstTick();
			agent.hungerTick();
		}
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void generateAgent(Point position) {
		Agent agent = new Agent(position);
		agents.add(agent);
	}
	
	public Agent getAgent(int x, int y) {
		for (Agent agent: agents) {
			if (agent.x() == x && agent.y() == y) {
				return agent;
			}
		}
		return null;
	}

	public void move(TerrainManager terrainManager) {
		for (Agent agent: agents) {
			if (!agent.isDead()) {
				int directionIndex = new Random().nextInt(Direction.values().length);
				Direction dir = Direction.values()[directionIndex];				
				Point destPoint = new Point(agent.x() + dir.getX(), agent.y() + dir.getY());
				if (validPoint(destPoint) && !terrainManager.isBlocked(destPoint.x, destPoint.y) && getAgent(destPoint.x, destPoint.y) == null) {
					agent.move(dir);
				}
			}
		}
	}
	
	private boolean validPoint(Point point){
		return (point.x >= 0 && point.x < Survivor.width) && (point.y >= 0 && point.y < Survivor.height);
	}

	public void pickup(ResourceManager resourceManager) {
		for (Agent agent: agents) {
			Resource resource = resourceManager.getResource(agent.x(), agent.y());
			if (resource != null) {
				if (resource.getResourceType() == TileType.FOOD) {
					resource.consumeResource();
					agent.removeHunger();
				} else {
					resource.consumeResource();
					agent.removeThirst();
				}
			}			
		}
	}
	
}

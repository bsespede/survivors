package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.resources.ReservoirManager;

public class AgentManager {

	private List<Agent> agents = new ArrayList<Agent>();

	public void tickTime(TerrainManager terrainManager, ReservoirManager resourceManager) {
		for (Agent agent: agents){
			agent.addHungerThirst();
			agent.move(terrainManager);
			agent.pickUp(resourceManager);
		}
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void generateAgent(Point position) {
		Agent agent = new Agent(position);
		agents.add(agent);
	}
	
	public Agent getAgentAt(Point position) {
		for (Agent agent: agents) {
			if (agent.position().equals(position)) {
				return agent;
			}
		}
		return null;
	}
	
}

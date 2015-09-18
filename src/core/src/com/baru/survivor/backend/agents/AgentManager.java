package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.village.Tribe;

public class AgentManager {

	private List<Agent> agents = new ArrayList<Agent>();
	private Map<Agent, Tribe> tribes = new HashMap<Agent, Tribe>();

	public void tickTime(TerrainManager terrainManager, ReservoirManager resourceManager, boolean callHome) {
		for (Agent agent: agents){
			agent.addHungerThirst();
			if (!callHome){
				if (!agent.moving()){
					agent.explore(terrainManager);									
				}else{
					agent.continueMoving(terrainManager);
				}
			}else{
				agent.goTo(terrainManager, tribes.get(agent).position());
			}
			agent.pickUp(resourceManager);
		}
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void generateAgent(Point position, Tribe tribe) {
		Agent agent = new Agent(position);
		agents.add(agent);
		tribes.put(agent, tribe);
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

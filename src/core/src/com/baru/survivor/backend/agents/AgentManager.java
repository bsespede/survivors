package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.baru.survivor.backend.village.Village;

public class AgentManager {

	private Map<Point, Agent> agents = new HashMap<Point, Agent>();

	public void tickTime() {
		for (Point agentCoord: agents.keySet()){
			Agent agent = agents.get(agentCoord);
			agent.thirstTick();
			agent.hungerTick();
			if (agent.isDead()){
				agents.remove(agent);
			}
		}
	}
	
	public Collection<Agent> getAgents() {
		return agents.values();
	}

	public Agent getAgentAt(int x, int y) {
		return agents.get(new Point(x, y));
	}

	public void generateAgent(Village tribe) {
		Agent agent = new Agent(tribe);
		agents.put(new Point(tribe.getVillageX(), tribe.getVillageY()), agent);	
		agent.enterVillage();
	}
	
}

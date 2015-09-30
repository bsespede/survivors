package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.village.Tribe;

public class AgentManager implements Serializable{

	private List<Agent> agents = new ArrayList<Agent>();
	private Map<Agent, Tribe> tribes = new HashMap<Agent, Tribe>();

	public void tickTime(TerrainManager terrainManager, ReservoirManager resourceManager, DayCycle cycle) {
		for (Agent agent: agents){
			agent.addHungerThirst();
			if (agent.isMoving() && !agent.isDead()){
					agent.continueMoving(terrainManager);
			}else if (cycle == DayCycle.DAY){
//				if (agent.wantsToShare()){
//					agent.goTo(terrainManager, tribes.get(agent).position());
//				}
//				if (agent.position().equals(tribes.get(agent).position() && agent.wentToShare() != null)){
//					tribes.get(agent)
//				}
				agent.explore(terrainManager, resourceManager);
			}else{
				if (agent.position().equals(tribes.get(agent).position())){
					agent.depositInTribeBag(tribes.get(agent));
					agent.pickUpFromTribeBag(tribes.get(agent));
				}else{
					agent.goTo(terrainManager, tribes.get(agent).position());
					agent.onTheWayToVillage();
				}	
			}
			agent.pickUp(resourceManager);
			agent.consumeFromBags();
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

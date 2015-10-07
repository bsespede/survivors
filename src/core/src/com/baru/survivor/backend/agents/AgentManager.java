package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.pheromones.Pheromones;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.village.Tribe;

public class AgentManager implements Serializable{

	private List<Agent> agents = new ArrayList<Agent>();
	private Map<Agent, Tribe> tribes = new HashMap<Agent, Tribe>();

	public void tickTime(TerrainManager terrainManager, ReservoirManager reservoirManager, DayCycle cycle, Pheromones pheromones) {
		for (Agent agent: agents){
			agent.addHungerThirst();
			if (cycle == DayCycle.NIGHT){
				if (agent.position().equals(tribes.get(agent).position())){
					agent.pickUpFromTribeBag(tribes.get(agent));
				}else{
					agent.setGoalPoint(tribes.get(agent).position());
					agent.explore(terrainManager, pheromones);
					if (agent.position().equals(tribes.get(agent).position())){
						agent.depositInTribeBag(tribes.get(agent));
					}
				}
			}else{
				if (agent.pickUp(reservoirManager)){
					agent.setGoalPoint(tribes.get(agent).position());
				}else if (agent.position().equals(tribes.get(agent).position())){
					agent.depositInTribeBag(tribes.get(agent));
					agent.setGoalPoint(null);
				}else{
					Point reservoirNearby = reservoirManager.getReservoirInRange(agent.position(), agent.getVision());
					agent.setGoalPoint(reservoirNearby);
				}
				agent.explore(terrainManager, pheromones);
			}
			agent.consumeFromBags();
			if (!agent.isDead() && !agent.position().equals(tribes.get(agent).position())){
				pheromones.addPheromone(agent.position().x, agent.position().y);
			}
		}
		pheromones.evaporatePheromones();
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

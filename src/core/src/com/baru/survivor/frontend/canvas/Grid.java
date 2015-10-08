package com.baru.survivor.frontend.canvas;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.Agent;
import com.baru.survivor.backend.agents.AgentManager;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.map.TileType;
import com.baru.survivor.backend.pheromones.Pheromones;
import com.baru.survivor.backend.resources.Reservoir;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.resources.ResourceType;
import com.baru.survivor.backend.village.Tribe;
import com.baru.survivor.backend.village.TribeManager;
import com.baru.survivor.frontend.sprite.SimpleSprite;
import com.baru.survivor.frontend.sprite.SpriteGenerator;
import com.baru.survivor.frontend.sprite.SpriteType;

public class Grid {

	private SpriteGenerator spriteGenerator = new SpriteGenerator();
	private Map<Agent, SimpleSprite> agentVisuals = new HashMap<Agent, SimpleSprite>();
	private List<Layer> layers = new ArrayList<Layer>();
	
	public Grid(){
		for (int i = 0; i < 6; i++){
			layers.add(null);
		}
	}

	public void fillTerrainLayers(TerrainManager map, TribeManager tribes) {
		Random rand = new Random();
		try {
			spriteGenerator.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Layer waterLayer = new Layer();
		Layer landLayer = new Layer();
		Layer shadeLayer = new Layer();
		Layer decoLayer = new Layer();
		
		boolean shouldShade = true;
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				Point position = new Point(x, y);
				if (shouldShade){
					//shadeLayer.setSprite(spriteGenerator.generateIndex(SpriteType.SHADE, 0), position);
					shouldShade = false;
				} else {
					shouldShade = true;
				}
				if (map.getTileType(position) == TileType.WATER){
					waterLayer.setSprite(spriteGenerator.generateIndex(SpriteType.WATER, 0), position);
					if (map.getTileType(position) == TileType.WATER && rand.nextFloat() < 0.005f){
						decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.WATER_DECORATION), position);
					}
				}
				if (map.getTileType(position) == TileType.COAST) {
					waterLayer.setSprite(spriteGenerator.generateIndex(SpriteType.WATER, 0), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, map.getAutoTile(position)), position);
				}
				if (map.getTileType(position) == TileType.GRASS) {
					if (rand.nextFloat() < 0.1f){
						landLayer.setSprite(spriteGenerator.generateRandom(SpriteType.GRASS_DECORATION), position);
					} else {
						landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
					}	
				}
				if (map.getTileType(position) == TileType.FOREST){
					decoLayer.setSprite(spriteGenerator.generateIndex(SpriteType.FOREST, 0), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
				}
				if (map.getTileType(position) == TileType.TREE){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.TREE), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
				}
				if (map.getTileType(position) == TileType.MOUNTAIN){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.MOUNTAIN), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
				}
				if (map.getTileType(position) == TileType.PLATEAU){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.PLATEAU), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
				}
				if (map.getTileType(position) == TileType.HOUSE){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.HOUSE), position);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), position);
				}
			}			
		}	
		List<Tribe> villages = tribes.getVillages();
		for(int i = 0; i < villages.size(); i++){
			Tribe curTribe = villages.get(i);
			decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.HOUSE), curTribe.position());
			landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), curTribe.position());
		}
		
		layers.set(0, waterLayer);
		layers.set(1, landLayer);
		layers.set(2, shadeLayer);
		layers.set(3, decoLayer);
	}
	
	public void fillAgentVisuals(AgentManager agentManager) {
		for (Agent agent: agentManager.getAgents()){
			agentVisuals.put(agent, spriteGenerator.generateRandom(SpriteType.VILLAGER));
		}
	}
	
	public void updateLayer(AgentManager agentManager, ReservoirManager reservoirManager, Pheromones pheromones){
		Layer agentLayer = new Layer();
		Layer resourceLayer = new Layer();
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				Point position = new Point(x, y);
				Agent agent = agentManager.getAgentAt(position);
				if (agent != null) {
					if (agent.isDead()) {
						agentVisuals.put(agent, spriteGenerator.generateRandom(SpriteType.SKULL));
					}
					agentLayer.setSprite(agentVisuals.get(agent), position);
				}
				Reservoir reservoir = reservoirManager.getReservoirAt(position);
				if (reservoir != null && reservoir.hasResource()) {
					SpriteType reservoirType;
					if (reservoir.type() == ResourceType.FOOD){
						reservoirType = SpriteType.FOOD;
					}else{
						reservoirType = SpriteType.LAKE;
					}
					resourceLayer.setSprite(spriteGenerator.generateIndex(reservoirType, 0), position);
				}
			}							
		}
		layers.set(4, resourceLayer);
		layers.set(5, agentLayer);
	}
	
	public void draw(SpriteBatch batch){		
		for (Layer layer: layers){
			layer.draw(batch);
		}		
	}

	public void dispose() {
		spriteGenerator.getTexture().dispose();
	}
}

package com.baru.survivor.frontend.canvas;

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
import com.baru.survivor.backend.resources.Reservoir;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.resources.ResourceType;
import com.baru.survivor.frontend.sprite.SimpleSprite;
import com.baru.survivor.frontend.sprite.SpriteGenerator;
import com.baru.survivor.frontend.sprite.SpriteType;

public class Grid {

	private SpriteGenerator spriteGenerator = new SpriteGenerator();
	private Map<Agent, SimpleSprite> agentVisuals = new HashMap<Agent, SimpleSprite>();
	private List<Layer> layers = new ArrayList<Layer>();
	
	public Grid() {
		for (int i = 0; i < 6; i++) {
			layers.add(new Layer());
		}
	}
	
	public SimpleSprite getSprite(int x, int y, int layer) {
		return layers.get(layer).getSprite(x, y);
	}

	public void fillTerrainLayers(TerrainManager map) {
		Random rand = new Random();
		try {
			spriteGenerator.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Layer waterLayer = layers.get(0);
		Layer landLayer = layers.get(1);
		Layer shadeLayer = layers.get(2);
		Layer decoLayer = layers.get(3);
		
		boolean shouldShade = true;
		for (int i = 0; i < Survivor.width; i++) {
			for (int j = 0; j < Survivor.height; j++) {
				if (shouldShade){
					shadeLayer.setSprite(spriteGenerator.generateIndex(SpriteType.SHADE, 0), i, j);
					shouldShade = false;
				} else {
					shouldShade = true;
				}
				if (map.getTileType(i, j) == TileType.WATER){
					waterLayer.setSprite(spriteGenerator.generateIndex(SpriteType.WATER, 0), i, j);
					if (map.getTileType(i, j) == TileType.WATER && rand.nextFloat() < 0.005f){
						decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.WATER_DECORATION), i, j);
					}
				}
				if (map.getTileType(i, j) == TileType.COAST) {
					waterLayer.setSprite(spriteGenerator.generateIndex(SpriteType.WATER, 0), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, map.getAutoTile(i, j)), i, j);
				}
				if (map.getTileType(i, j) == TileType.GRASS) {
					if (rand.nextFloat() < 0.1f){
						landLayer.setSprite(spriteGenerator.generateRandom(SpriteType.GRASS_DECORATION), i, j);
					} else {
						landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
					}	
				}
				if (map.getTileType(i, j) == TileType.FOREST){
					decoLayer.setSprite(spriteGenerator.generateIndex(SpriteType.FOREST, 0), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.TREE){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.TREE), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.MOUNTAIN){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.MOUNTAIN), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.PLATEAU){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.PLATEAU), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.HOUSE){
					decoLayer.setSprite(spriteGenerator.generateRandom(SpriteType.HOUSE), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(SpriteType.GRASS, 0), i, j);
				}
			}
		}		
	}
	
	public void fillAgentVisuals(AgentManager agentManager) {
		for (Agent agent: agentManager.getAgents()){
			agentVisuals.put(agent, spriteGenerator.generateRandom(SpriteType.VILLAGER));
		}
	}
	
	public void updateLayer(AgentManager agentManager, ReservoirManager reservoirManager){
		Layer agentLayer = new Layer();
		Layer resourceLayer = new Layer();
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				Agent agent = agentManager.getAgent(x, y);
				if (agent != null) {
					if (agent.isDead()) {
						agentVisuals.put(agent, spriteGenerator.generateRandom(SpriteType.SKULL));
					}
					agentLayer.setSprite(agentVisuals.get(agent), x, y);
				}
				Reservoir reservoir = reservoirManager.getReservoir(x, y);
				if (reservoir != null && reservoir.hasResource()) {
					SpriteType reservoirType;
					if (reservoir.type() == ResourceType.FOOD){
						reservoirType = SpriteType.FOOD;
					}else{
						reservoirType = SpriteType.LAKE;
					}
					resourceLayer.setSprite(spriteGenerator.generateIndex(reservoirType, 0), x, y);
				}
			}							
		}
		layers.set(5, agentLayer);
		layers.set(4, resourceLayer);
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

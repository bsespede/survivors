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
import com.baru.survivor.frontend.sprite.SimpleSprite;
import com.baru.survivor.frontend.sprite.SpriteGenerator;

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
					shadeLayer.setSprite(spriteGenerator.generateIndex(TileType.SHADE, 0), i, j);
					shouldShade = false;
				} else {
					shouldShade = true;
				}
				if (map.getTileType(i, j) == TileType.WATER){
					waterLayer.setSprite(spriteGenerator.generateIndex(TileType.WATER, 0), i, j);
					if (map.getTileType(i, j) == TileType.WATER && rand.nextFloat() < 0.005f){
						decoLayer.setSprite(spriteGenerator.generateRandom(TileType.WATER_DECORATION), i, j);
					}
				}
				if (map.getTileType(i, j) == TileType.COAST) {
					waterLayer.setSprite(spriteGenerator.generateIndex(TileType.WATER, 0), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, map.getAutoTile(i, j)), i, j);
				}
				if (map.getTileType(i, j) == TileType.GRASS) {
					if (rand.nextFloat() < 0.1f){
						landLayer.setSprite(spriteGenerator.generateRandom(TileType.GRASS_DECORATION), i, j);
					} else {
						landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
					}	
				}
				if (map.getTileType(i, j) == TileType.FOREST){
					decoLayer.setSprite(spriteGenerator.generateIndex(TileType.FOREST, 0), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.TREE){
					decoLayer.setSprite(spriteGenerator.generateRandom(TileType.TREE), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.MOUNTAIN){
					decoLayer.setSprite(spriteGenerator.generateRandom(TileType.MOUNTAIN), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.PLATEAU){
					decoLayer.setSprite(spriteGenerator.generateRandom(TileType.PLATEAU), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.HOUSE){
					decoLayer.setSprite(spriteGenerator.generateRandom(TileType.HOUSE), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.LAKE){
					decoLayer.setSprite(spriteGenerator.generateIndex(TileType.LAKE, 0), i, j);
					landLayer.setSprite(spriteGenerator.generateIndex(TileType.GRASS, 0), i, j);
				}
				if (map.getTileType(i, j) == TileType.WINDMILL){
					decoLayer.setSprite(spriteGenerator.generateIndex(TileType.WINDMILL, 0), i, j);
				}
			}
		}		
	}
	
	public void fillAgentVisuals(AgentManager agentManager) {
		for (Agent agent: agentManager.getAgents()){
			agentVisuals.put(agent, spriteGenerator.generateRandom(TileType.VILLAGER));
		}
	}
	
	public void updateAgentLayer(AgentManager agentManager){
		Layer agentLayer = new Layer();
		for (Point agentCoord: agentManager.getCoords){
			agentLayer.setSprite(agentVisuals.get(a), agent.x(), agent.y());
		}
		layers.add(4, agentLayer);
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

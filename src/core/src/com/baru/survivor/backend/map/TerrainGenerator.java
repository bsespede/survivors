package com.baru.survivor.backend.map;

import java.util.Random;

import com.baru.survivor.Survivor;

public class TerrainGenerator {
	
	public static TerrainManager generateMap(){
		Random rand = new Random();
		ParticleDeposition particleGen = new ParticleDeposition(Survivor.width, Survivor.height);
		double[][] particles = particleGen.makeDeposition(5, 3);
		Tile[][] tiles = new Tile[Survivor.width][Survivor.height];
		AutoTile autoTiles = new AutoTile();
		for (int i = 0; i < Survivor.width; i++) {
			for (int j = 0; j < Survivor.height; j++) {
				double particleHeight = particles[i][j];
				if (particleHeight < 0.1){
					tiles[i][j] = new Tile(TileType.WATER, true);
				} else {
					if (particleHeight < 0.35){
						tiles[i][j] = new Tile(TileType.GRASS, false);
						float grassLandContent = rand.nextFloat();
						if (grassLandContent < 0.1) {
							tiles[i][j] = new Tile(TileType.TREE, false);
						} else if (grassLandContent < 0.15) {
							tiles[i][j] = new Tile(TileType.PLATEAU, false);
						}
					} else if (particleHeight < 0.4){
						if (rand.nextFloat() < 0.7) {
							tiles[i][j] = new Tile(TileType.TREE, false);
						} else {
							tiles[i][j] = new Tile(TileType.FOREST, false);
						}
						autoTiles.addTile(i, j);
					} else if (particleHeight < 0.5){
						tiles[i][j] = new Tile(TileType.FOREST, false);
					} else {
						tiles[i][j] = new Tile(TileType.MOUNTAIN, true);
					}
					autoTiles.addTile(i, j);
				}
			}
		}
		
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				if (tiles[x][y].getType() == TileType.WATER && autoTiles.get(x, y) == 0){
					{
						tiles[x][y] = new Tile(TileType.GRASS, false);	
						autoTiles.addTile(x, y);
					}
				}
			}
		}
		
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				if (tiles[x][y].getType() == TileType.WATER && autoTiles.get(x, y) != 15){
					tiles[x][y] = new Tile(TileType.COAST, true);
				}
			}
		}
		
		return new TerrainManager(tiles, autoTiles);
	}	
}

package com.baru.survivor.backend.map;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.Direction;

public class TerrainManager implements Serializable{

	private Tile[][] tiles;
	private AutoTile autoTiles;
	
	public TerrainManager() {
		tiles = new Tile[Survivor.width][Survivor.height];
	}
	
	public TerrainManager(Tile[][] tiles, AutoTile autoTiles) {
		this.tiles = tiles;
		this.autoTiles = autoTiles;
	}
	
	public TileType getTileType(Point point) {
		return tiles[point.x][point.y].getType();
	}
	
	public byte getAutoTile(Point point){
		return autoTiles.get(point.x, point.y);
	}
	
	public boolean isBlocked(Point point) {
		return tiles[point.x][point.y].isBlocked();
	}

	public Point getSpawnablePoint() {
		Random rand = new Random();
		while (true) {
			int x = (int) (rand.nextFloat() * Survivor.width);
			int y = (int) (rand.nextFloat() * Survivor.height);
			if (!tiles[x][y].isBlocked()){
				return new Point(x, y);
			}
		}
	}

	public static boolean isValidPoint(Point point) {
		return (point.x >= 0 && point.x < Survivor.width) && (point.y >= 0 && point.y < Survivor.height);
	}

	public Direction getRandomValidDirectionFrom(int x, int y) {
		boolean foundValidDirection = false;
		Direction resultDirection = null;
		Random rnd = new Random();
		while (!foundValidDirection){
			int possibleX = -1 + rnd.nextInt(3);
			int possibleY = -1 + rnd.nextInt(3);
			Point possiblePoint = new Point(x+possibleX, y+possibleY);
			Point curPoint = new Point(x, y);
			if (!curPoint.equals(possiblePoint) && isValidPoint(possiblePoint) && !isBlocked(possiblePoint)){
				resultDirection = Direction.valueOf(possibleX, possibleY);
				foundValidDirection = true;
			}
		}
		return resultDirection;
	}
}

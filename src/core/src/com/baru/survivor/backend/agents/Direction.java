package com.baru.survivor.backend.agents;

public enum Direction {
	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0),
	NW(-1, -1), NE(1, -1), SW(-1, 1), SE(1, 1);
	
	private final int y;
	private final int x;
	
	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { 
		return x;
	}
    public int getY() {
    	return y;
    }
}

package com.baru.survivor.backend.map;

import java.util.Random;

public class ParticleDeposition {
	
	private double[][] heightMap;
	private int width;
	private int height;
	
	public ParticleDeposition(int width, int height){
		heightMap = new double[width][height];
		this.width = width;
		this.height = height;
	}
	
	public double[][] makeDeposition(int passes, int depositions){	
		Random rand = new Random();
		double maxDist = distanceToCenter(0.0, 0.0);
		while (passes > 0){
			double curMaxDist = maxDist - (maxDist / (1.5*passes)) ;
			int curDeposition = depositions;
			while (curDeposition > 0) {	
				
				int x = (int) (rand.nextFloat() * width);
				int y = (int) (rand.nextFloat() * height);
				double curDist = distanceToCenter(x, y);
				if (curDist <= curMaxDist) {
					int particlesAmount = (int)(rand.nextFloat() * 50) + 50;
					while (particlesAmount > 0) {
						rollParticle(x, y);
						particlesAmount --;
					}
					curDeposition--;
					
				}
			}
			passes --;
		}
		return heightMap;
	}

	private void rollParticle(int x, int y) {
		if (canRollDown(x, y, x, y-1)){
			rollParticle(x,y-1);
			return;
		} else if (canRollDown(x, y, x+1, y-1)){
			rollParticle(x+1,y-1);
			return;
		} else if (canRollDown(x, y, x+1, y)){
			rollParticle(x+1,y);
			return;
		} else if (canRollDown(x, y, x+1, y+1)){
			rollParticle(x+1,y+1);
			return;
		} else if (canRollDown(x, y, x, y+1)){
			rollParticle(x,y+1);
			return;
		} else if (canRollDown(x, y, x-1, y+1)){
			rollParticle(x-1,y+1);
			return;
		} else if (canRollDown(x, y, x-1, y)){
			rollParticle(x-1,y);
			return;
		} else if (canRollDown(x, y, x-1, y-1)){
			rollParticle(x-1,y-1);
			return;
		} else{
			if (validPoint(x, y)){
				heightMap[x][y] += 0.1;
			}
			return;
		}
	}

	private boolean canRollDown(int fromX, int fromY, int toX, int toY) {
		return validPoint(fromX, fromY) && validPoint(toX, toY)&& (heightMap[toX][toY] < heightMap[fromX][fromY]);
	}
	
	private boolean validPoint(int x, int y){
		return (x >= 2 && x < width-2) && (y >= 2 && y < height-2);
	}
	private double distanceToCenter(double x, double y){
		double centerX = width/2;
		double centerY = height/2;
		
		return Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
	}
	
}

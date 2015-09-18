package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.IOException;
import java.util.Deque;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.baru.survivor.Survivor;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.pathfinding.AStar;
import com.baru.survivor.backend.resources.Reservoir;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.resources.ResourceType;

public class Agent {

	private String name;
	private Point position;
	private boolean moving;
	private float hunger;
	private float thirst;
	private float kindness;
	private Bag foodBag;
	private Bag waterBag;
	private Deque<Point> path;
	
	public Agent(Point position){
		Random rand = new Random();
		this.kindness = rand.nextFloat();
		this.foodBag = new Bag();
		this.waterBag = new Bag();
		this.hunger = 1;
		this.thirst = 1;	
		this.position = position;
		NameGenerator ng;
		try {
			ng = new NameGenerator(Gdx.files.internal("roman.syl").reader(15));
			name = ng.compose(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CharSequence name() {
		return name;
	}

	public void consumeFromBags(){
		if (kindness > 0.8){
			if (hunger < 0.2){
				consumeFromFoodBag();
			}
			if (thirst < 0.2){
				consumeFromWaterBag();
			}
		}else if (kindness > 0.4){
			if (hunger < 0.5){
				consumeFromFoodBag();
			}
			if (thirst < 0.5){
				consumeFromWaterBag();
			}
		}else{
			if (hunger < 0.8){
				consumeFromFoodBag();
			}
			if (thirst < 0.8){
				consumeFromWaterBag();
			}
		}
	}
	
	private void consumeFromFoodBag() {
		Resource resource = foodBag.getResource();
		if (resource != null){
			hunger += 0.2;
		}
	}
	
	private void consumeFromWaterBag() {
		Resource resource = waterBag.getResource();
		if (resource != null){
			thirst += 0.2;
		}
	}

	public void addHungerThirst() {
		float amountToReduce = 1.0f / (Survivor.secondsPerDay * (1000/Survivor.tickTime));
		if (hunger > 0){
			hunger -= amountToReduce;
		}
		if (thirst > 0){
			thirst -= amountToReduce;
		}
	}

	public void explore(TerrainManager terrainManager) {
		int directionIndex = new Random().nextInt(Direction.values().length);
		Direction dir = Direction.values()[directionIndex];				
		move(terrainManager, dir);
	}
	
	public boolean move(TerrainManager terrainManager, Direction dir) {
		if (!isDead()) {	
			Point destPoint = new Point(position.x + dir.getX(), position.y + dir.getY());
			if (TerrainManager.isValidPoint(destPoint) && !terrainManager.isBlocked(destPoint)) {
				position = new Point(position.x + dir.getX(), position.y + dir.getY());
				return true;
			}else{
				return false;
			}
		}else{
			return false;			
		}
	}
	
	public void pickUp(ReservoirManager reservoirManager) {
		Reservoir reservoir = reservoirManager.getReservoirAt(position);
		if (reservoir != null) {
				consumeResourceTillFull(reservoir);
				fillBagWithResource(reservoir);
		}			
	}

	private void fillBagWithResource(Reservoir reservoir) {
		Bag sameTypeBag = getSameTypeBag(reservoir);
		int resourcesToGrab = sameTypeBag.getAvailableSlots();
		while (resourcesToGrab > 0 && reservoir.hasResource()){
			Resource resourceToGrab = reservoir.getResource();
			sameTypeBag.addresource(resourceToGrab);
			resourcesToGrab--;
		}
	}

	private void consumeResourceTillFull(Reservoir reservoir){
		ResourceType type = reservoir.type();
		while (reservoir.hasResource()){
			if (type == ResourceType.FOOD){
				if (hunger < 0.8){ 
					reservoir.getResource();
					hunger += 0.2; 
				}else{
					return;
				}
			}
			if (type == ResourceType.WATER){
				if (thirst < 0.8){
					reservoir.getResource();
					thirst += 0.2; 					
				}else{
					return;
				}
			}
		}
	}
	
	private Bag getSameTypeBag(Reservoir reservoir){
		if (reservoir.type() == ResourceType.FOOD){
			return foodBag;
		}else{
			return waterBag;
		}
	}
	
	public Point position(){
		return position;
	}
	
	public float getKindness(){
		return kindness;
	}
	
	public void addResource(Resource resource){
		if (resource.getResourceType() == ResourceType.FOOD){
			foodBag.addresource(resource);
		} else {
			waterBag.addresource(resource);
		}
		
	}
	
	public Resource getFoodFromBag(){
		return foodBag.getResource();
	}
	
	public Resource getWaterFromBag(){
		return waterBag.getResource();
	}
	
	public boolean isDead() {
		return thirst <= 0 || hunger <= 0;
	}

	public float getHunger() {
		return hunger;
	}

	public float getThirst() {
		return thirst;
	}

	public void removeHunger() {
		hunger = 1;
	}

	public void removeThirst() {
		thirst = 1;
	}

	public boolean isMoving() {
		return moving;
	}

	public void continueMoving(TerrainManager terrain) {
		if (moving){
			if (!path.isEmpty()){
				Point nextPoint = path.peek();
				if (terrain.isBlocked(nextPoint)){
					Point destination = path.peekLast();
					path = (new AStar()).getPath(position, destination, terrain);
				}else{
					position = path.pop();					
				}
			} else {
				moving = false;
			}
		}
	}

	public void goTo(TerrainManager terrain, Point destination) {
		moving = true;
		path = (new AStar()).getPath(position, destination, terrain);
		System.out.println(name+" "+path);
		if (path == null){
			moving = false;
			return;
		}
	}

}

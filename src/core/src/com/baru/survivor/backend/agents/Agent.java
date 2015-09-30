package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
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
import com.baru.survivor.backend.village.Tribe;

public class Agent implements Serializable{

	private String name;
	private Point position;
	private boolean moving;
	private boolean justArrivedVillage;
	private float hunger;
	private float thirst;
	private int visionRange;
	private float kindness;
	private Bag foodBag;
	private Bag waterBag;
	private Deque<Point> path;
	
	public Agent(Point position){
		Random rand = new Random();
		this.kindness = rand.nextFloat();
		this.foodBag = new Bag(Survivor.agentSlots);
		this.waterBag = new Bag(Survivor.agentSlots);
		this.hunger = 1;
		this.thirst = 1;	
		this.visionRange = 2;
		this.position = position;
		NameGenerator ng;
		try {
			ng = new NameGenerator(Gdx.files.internal("roman.syl").reader(15));
			name = ng.compose(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onTheWayToVillage(){
		justArrivedVillage = true;
	}		
	
	public CharSequence name() {
		return name;
	}

	public void consumeFromBags(){
		if (isHungry()){
			consumeFromFoodBag();
		}
		if (isThirsty()){
			consumeFromWaterBag();
		}
	}
	
	public boolean isHungry(){
		if (kindness > 0.8){
			if (hunger < 0.2){
				return true;
			}
			return false;
		}else if (kindness > 0.4){
			if (hunger < 0.5){
				return true;
			}
			return false;
		}else{
			if (hunger < 0.8){
				return true;
			}
			return false;
		}
	}
	
	public boolean isThirsty(){
		if (kindness > 0.8){
			if (thirst < 0.2){
				return true;
			}
			return false;
		}else if (kindness > 0.4){
			if (thirst < 0.5){
				return true;
			}
			return false;
		}else{
			if (thirst < 0.8){
				return true;
			}
			return false;
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

	public void explore(TerrainManager terrainManager, ReservoirManager reservoirManager) {
		Point reservoirLocation = reservoirNearby(reservoirManager);
		if (reservoirLocation != null && reservoirManager.getReservoirAt(reservoirLocation).hasResource()){
			goTo(terrainManager, reservoirLocation);
		}else{
			int directionIndex = new Random().nextInt(Direction.values().length);
			Direction dir = Direction.values()[directionIndex];			
			move(terrainManager, dir);
		}			
	}
	
	private Point reservoirNearby(ReservoirManager reservoirManager) {
		for (int x = (-1)*visionRange; x < visionRange+1; x++){
			for (int y = (-1)*visionRange; y < visionRange+1; y++){
				Point point = new Point(x + position.x, y + position.y);
				Reservoir reservoir = reservoirManager.getReservoirAt(point);
				if (reservoir != null){
					if (reservoir.type() == ResourceType.FOOD && (hunger < 0.8 || !getSameTypeBag(reservoir).isFull())){
						return point;
					}else if (reservoir.type() == ResourceType.WATER && (thirst < 0.8|| !getSameTypeBag(reservoir).isFull())){
						return point;
					}
				}
			}
		}
		return null;
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
		if (!isDead()){
			moving = true;
			path = (new AStar()).getPath(position, destination, terrain);
			if (path == null){
				moving = false;
				return;
			}
		}		
	}

	public String foodStg() {
		return ((Integer)foodBag.usedSlots()).toString();
	}
	
	public String waterStg() {
		return ((Integer)waterBag.usedSlots()).toString();
	}

	public void depositInTribeBag(Tribe tribe) {
		if (justArrivedVillage){
			int foodToShare = (int)(kindness * foodBag.usedSlots());
			int waterToShare = (int)(kindness * waterBag.usedSlots());
			while (foodToShare > 0){
				Resource food = foodBag.getResource();
				tribe.getFoodVault().addresource(food);
				foodToShare--;
			}
			while (waterToShare > 0){
				Resource water = waterBag.getResource();
				tribe.getWaterVault().addresource(water);
				waterToShare--;
			}
			justArrivedVillage = false;
		}
	}

	public void pickUpFromTribeBag(Tribe tribe) {
		if (isHungry()){
			Resource food = tribe.getFoodVault().getResource();
			if (food != null){
				foodBag.addresource(food);
			}
		}
		if (isThirsty()){
			Resource water = tribe.getWaterVault().getResource();
			if (water != null){
				waterBag.addresource(water);
			}
		}
	}

}

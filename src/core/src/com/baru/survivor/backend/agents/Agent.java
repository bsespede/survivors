package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.baru.survivor.Survivor;
import com.baru.survivor.backend.map.TerrainManager;
import com.baru.survivor.backend.pheromones.Pheromones;
import com.baru.survivor.backend.resources.Reservoir;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.resources.ResourceType;
import com.baru.survivor.backend.village.Tribe;

public class Agent implements Serializable{

	private String name;
	private Point position;
	private Point lastPosition;
	private List<Point> path;
	private float pheromoneIntensity;
	private float hunger;
	private float thirst;
	private int visionRange;
	private float kindness;
	private Bag foodBag;
	private Bag waterBag;
	private Point goal;
	private Status goalState;
	
	public Agent(Point position){
		Random rand = new Random();
		this.kindness = 1;//rand.nextFloat();
		this.foodBag = new Bag(Survivor.agentSlots);
		this.waterBag = new Bag(Survivor.agentSlots);
		this.hunger = 1;
		this.path = new LinkedList<Point>();
		this.path.add(position);
		this.thirst = 1;	
		this.pheromoneIntensity = 1;
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
			if (hunger <= 0){
				thirst = 0;
			}
		}
		if (thirst > 0){
			thirst -= amountToReduce;
			if (thirst <= 0){
				hunger = 0;
			}
		}
	}

	public void move(TerrainManager terrainManager, AgentManager agentManager, Point tribePosition, Pheromones pheromones) {
		if (goalState == Status.GO_HOME){
			if (path.size() == 0){
				System.out.println(this.name);
			}else{
				Point newPosition = path.get(0);
				if (Survivor.pathBlockingDisabled || agentManager.noAgentsAt(newPosition) || newPosition.equals(tribePosition)){
					lastPosition = position;
					position = new Point(newPosition);
					if (!newPosition.equals(tribePosition)){
						path.remove(0);
					}else{
						lastPosition = null;
						pheromoneIntensity = 1;
					}
				}
			}
		}else{
			Direction dir = pheromones.getDirFrom(agentManager, tribePosition, terrainManager, position, lastPosition, goal);
			if (dir != null){
				Point newPosition = new Point(position.x + dir.getX(), position.y + dir.getY());
				lastPosition = position;
				position = new Point(newPosition);
				path.add(0, newPosition);
				pheromoneIntensity *= 1f - Survivor.pheromoneLossPercentage;
			}
		}		
	}
	
	public float getPheromoneIntensity(){
		return pheromoneIntensity;		
	}
	
	public boolean pickUp(ReservoirManager reservoirManager) {
		Reservoir reservoir = reservoirManager.getReservoirAt(position);
		if (reservoir != null && reservoir.hasResource()) {
			consumeResourceTillFull(reservoir);
			fillBagWithResource(reservoir);
			return true;
		}			
		return false;
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

	public String foodStg() {
		return ((Integer)foodBag.usedSlots()).toString();
	}
	
	public String waterStg() {
		return ((Integer)waterBag.usedSlots()).toString();
	}

	public void depositInTribeBag(Tribe tribe) {
		int foodToShare = (int)(getPercentageToDeposit() * foodBag.usedSlots());
		int waterToShare = (int)(getPercentageToDeposit() * waterBag.usedSlots());
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
	}

	public float getPercentageToDeposit(){
		return 1;
		/*if (kindness > 0.8){
			return 1;
		}else if (kindness > 0.5){
			return 0.5f;
		}else {
			return 0;
		}*/
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

	public int getVision() {
		return visionRange;
	}

	public void setGoalPoint(Point goal, Status goalState) {
		this.goal = goal;
		this.goalState = goalState;
	}

	public Status getGoalState() {
		return goalState;
	}

	public void cleanPath() {
		path.remove(0);
		List<Point> visited = new LinkedList<Point>();
		findRepeats(0, visited);
	}

	private void findRepeats(int curIndex, List<Point> visited) {
		if (curIndex == path.size()){
			return;
		}
		Point curPoint = path.get(curIndex);
		if (visited.contains(curPoint)){
			curIndex = removeUntil(curPoint, curIndex-1, visited);
		}else{
			visited.add(curPoint);
		}
		findRepeats(curIndex+1, visited);
	}

	private int removeUntil(Point origPoint, int i, List<Point> visited) {
		Point removedPoint = path.remove(i);
		visited.remove(removedPoint);
		if (removedPoint.equals(origPoint)){
			return i;
		}
		return removeUntil(origPoint, i-1, visited);
	}

	public int getPathLength() {
		return path.size();
	}

	public Point getGoalPoint() {
		return goal;
	}

}

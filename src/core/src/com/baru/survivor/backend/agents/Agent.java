package com.baru.survivor.backend.agents;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.baru.survivor.backend.resources.Resource;

public class Agent {

	private String name;
	private Point position;
	private float hunger;
	private float thirst;
	private float kindness;
	private List<Resource> bag = new ArrayList<Resource>();
	
	public Agent(Point position){
		Random rand = new Random();
		this.kindness = rand.nextFloat();
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

	public void hungerTick() {
		if (hunger > 0){
			hunger -= 0.01;			
		}
	}

	public void thirstTick() {
		if (thirst > 0){
			thirst -= 0.01;		
		}
	}
	
	public int x() {
		return position.x;
	}
	
	public int y() {
		return position.y;
	}
	
	public float getKindness(){
		return kindness;
	}
	
	public void addResource(Resource resource){
		bag.add(resource);
	}
	
	public void removeResource(Resource resource){
		bag.remove(resource);
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

	public void move(Direction dir) {
		this.position = new Point(position.x + dir.getX(), position.y + dir.getY());
	}

	public void removeHunger() {
		hunger = 1;
	}

	public void removeThirst() {
		thirst = 1;
	}
	
}

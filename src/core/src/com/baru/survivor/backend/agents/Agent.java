package com.baru.survivor.backend.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.village.Village;

public class Agent {

	private String name;
	private Village tribe;
	private float hunger;
	private float thirst;
	private float kindness;
	private List<Resource> bag = new ArrayList<Resource>();
	
	public Agent(Village tribe){
		Random rand = new Random();
		this.kindness = rand.nextFloat();
		this.tribe = tribe;
		this.hunger = 1;
		this.thirst = 1;		
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
			hunger -= 0.05;			
		}
	}

	public void thirstTick() {
		if (thirst > 0){
			thirst -= 0.025;		
		}
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

	public void enterVillage(){
		tribe.enterVillage(this);
	}
	
	public void leaveVillage(){
		tribe.leaveVillage(this);
	}
	
	public boolean isDead() {
		return thirst <= 0 || hunger <= 0;
	}
	
}

package com.baru.survivor.backend.agents;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.baru.survivor.frontend.Survivors;

public class Agent {
	
	private int x;
	private int y;
	private String name;
	private float hunger = 1f;
	private float thirst = 1f;
	
	public Agent(){
		Random rand = new Random();
		x = (int) (rand.nextFloat() * Survivors.width);
		y = (int) (rand.nextFloat() * Survivors.height);
		NameGenerator ng;
		try {
			ng = new NameGenerator(Gdx.files.internal("roman.syl").reader(15));
			name = ng.compose(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}

	public CharSequence name() {
		return name;
	}

	public float getThirst() {
		return thirst;
	}

	public float getHunger() {
		return hunger;
	}

	public void hungerTick() {
		if (hunger > 0){
			hunger -= 0.05;			
		} else {
			//kill
		}
		
	}

	public void thirstTick() {
		if (thirst > 0){
			thirst -= 0.025;		
		} else {
			//kill
		}
	}
	
}

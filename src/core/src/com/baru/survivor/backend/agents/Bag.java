package com.baru.survivor.backend.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.backend.resources.Resource;

public class Bag implements Serializable{
		
	private List<Resource> bag = new ArrayList<Resource>();
	private int maxSlots = 3;
	
	public Bag(int maxSlots){
		this.maxSlots = maxSlots;
	}
	
	public void addresource(Resource resource){
		if (bag.size() < maxSlots){
			bag.add(resource);			
		}
	}
	
	public int getAvailableSlots(){
		return maxSlots - bag.size();
	}
	
	public Resource getResource(){
		if (bag.size() > 0){
			return bag.remove(bag.size()-1);
		} else {
			return null;
		}
	}

	public int usedSlots() {
		return bag.size();
	}
	
	public boolean isFull(){
		return bag.size() == maxSlots;
	}
}

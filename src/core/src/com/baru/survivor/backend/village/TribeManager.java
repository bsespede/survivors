package com.baru.survivor.backend.village;

import java.util.ArrayList;
import java.util.List;

public class TribeManager {

	private List<Tribe> villages = new ArrayList<Tribe>();

	public void addTribe(Tribe tribe) {
		villages.add(tribe);
	}
	
	public List<Tribe> getVillages(){
		return villages;
	}
}

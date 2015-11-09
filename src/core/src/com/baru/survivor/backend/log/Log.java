package com.baru.survivor.backend.log;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baru.survivor.backend.agents.Agent;
import com.baru.survivor.backend.resources.ReservoirManager;
import com.baru.survivor.backend.village.Tribe;
import com.baru.survivor.backend.village.TribeManager;

public class Log {
	
	private int curTick;
	private Map<Tribe, FileWriter> openTribeStatusFile;
	private Map<Tribe, FileWriter> openTribeDistFile;
	
	public Log(TribeManager tribeManager){
		curTick = 0;
		openTribeStatusFile = new HashMap<Tribe, FileWriter>();
		openTribeDistFile = new HashMap<Tribe, FileWriter>();
		int tribeNum = 1;
		for (Tribe curTribe: tribeManager.getVillages()){
			FileWriter tribeStatusFile, tribeDistFile;
			try {
				File statusFile = new File("./logs/t"+tribeNum+"-status.csv");
				statusFile.createNewFile();
				tribeStatusFile = new FileWriter(statusFile);
				File distFile = new File("./logs/t"+tribeNum+"-dist.csv");
				distFile.createNewFile();
				tribeDistFile = new FileWriter(distFile);
				openTribeStatusFile.put(curTribe, tribeStatusFile);
				openTribeDistFile.put(curTribe, tribeDistFile);
				tribeNum++;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void tick(TribeManager tribeManager, ReservoirManager reservoirManager){
		for (Tribe curTribe: tribeManager.getVillages()){	
			FileWriter tribeDistFile = openTribeDistFile.get(curTribe);
			FileWriter tribeStatusFile = openTribeStatusFile.get(curTribe);
			try {
				float curHunger = 0;
				float curThirst = 0;
				int curDead = 0;
				List<Agent> tribeAgents = curTribe.getAgents();
				int tribeSize = tribeAgents.size();
				for (Agent curAgent: tribeAgents){
					if(curAgent.isDead()){
						curDead++;
					}else{
						curHunger += curAgent.getHunger();
						curThirst += curAgent.getThirst();
						if (reservoirManager.getReservoirAt(curAgent.position()) != null){
							tribeDistFile.append(Integer.toString(curTick));
							tribeDistFile.append(',');
							tribeDistFile.append(Integer.toString(curAgent.getPathLength()));
							tribeDistFile.append('\n');
						}
					}				
				}
				int curAlive = tribeSize - curDead;
				tribeStatusFile.append(Integer.toString(curTick));
				tribeStatusFile.append(',');				
				tribeStatusFile.append(Float.toString(curHunger/curAlive));
				tribeStatusFile.append(',');
				tribeStatusFile.append(Float.toString(curThirst/curAlive));
				tribeStatusFile.append(',');
				tribeStatusFile.append(Integer.toString(curAlive));
				tribeStatusFile.append('\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		curTick++;
	}
	
	public void closeFiles(){
		try {
			for (Tribe file: openTribeStatusFile.keySet()){
				openTribeStatusFile.get(file).close();				
			}
			for (Tribe file: openTribeDistFile.keySet()){
				openTribeDistFile.get(file).close();				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package com.baru.survivor.backend.pathtracing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.baru.survivor.backend.map.TerrainManager;

public class AStar {

	public List<Point> getPath(Point from, Point to, TerrainManager terrain){
		List<Point> path = new LinkedList<Point>();
		Queue<DirectionNode> closed = new PriorityQueue<DirectionNode>();
		Queue<DirectionNode> open = new PriorityQueue<DirectionNode>();
		DirectionNode firstNode = new DirectionNode(null, from);
		firstNode.f = eulerDist(from, to);
		open.add(firstNode);
		
		while (!open.isEmpty()){
			DirectionNode curNode = open.poll();
			if(curNode.point.equals(to)){
				makePath(curNode, path);
				return path;
			}
			closed.add(curNode);
			for (Point neighbourPoint: curNode.getNeighbours()){
				if (nodeInQueue(neighbourPoint, closed) != null || terrain.isBlocked(neighbourPoint)){
					continue;
				}
				DirectionNode neighbourNode = nodeInQueue(neighbourPoint, open);
				if (neighbourNode == null){
					DirectionNode candidate = new DirectionNode(curNode, neighbourPoint);
					candidate.g = curNode.g + 1;
					candidate.f = candidate.g + eulerDist(neighbourPoint, to);
					open.add(candidate);
				}else if (curNode.g + 1 < neighbourNode.g){
					neighbourNode.g = curNode.g + 1;
					neighbourNode.f = neighbourNode.g + eulerDist(neighbourPoint, to);
				}
			}
		}
		
		
		return null;
		
	}

	private void makePath(DirectionNode curNode, List<Point> path) {
		if (curNode != null){
			path.add(0, curNode.point);
			makePath(curNode.parent, path);
		}
	}

	private double eulerDist(Point from, Point to){
		return Math.sqrt(Math.pow(to.x - from.x, 2)+Math.pow(to.x - from.x, 2));		
	}
	
	private DirectionNode nodeInQueue(Point point, Queue<DirectionNode> queue){
		for(DirectionNode dn: queue){
			if (dn.point.equals(point)){
				return dn;
			}
		}
		return null;
	}
	
	private class DirectionNode implements Comparable<DirectionNode>{

		private DirectionNode parent;
		private Double f;
		private Double g;
		private Point point;
		
		public DirectionNode(DirectionNode parent, Point point){
			this.point = point;
			this.parent = parent;
		}
		
		public List<Point> getNeighbours() {
			List<Point> neighbours = new ArrayList<Point>();
			return neighbours;
		}
		
		public int compareTo(DirectionNode other) {
			return f.compareTo(other.f);
		}

	}
}

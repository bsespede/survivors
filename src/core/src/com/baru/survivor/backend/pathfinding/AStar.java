package com.baru.survivor.backend.pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.baru.survivor.backend.map.TerrainManager;

public class AStar {

	public Deque<Point> getPath(Point from, Point to, TerrainManager terrain){
		Deque<Point> path = new LinkedList<Point>();
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
				if (neighbourNode == null || curNode.g + 1 < neighbourNode.g){
					DirectionNode candidate = new DirectionNode(curNode, neighbourPoint);
					candidate.g = curNode.g + 1;
					candidate.f = candidate.g + eulerDist(neighbourPoint, to);
					open.add(candidate);
				}
			}
		}		
		
		return null;
		
	}

	private void makePath(DirectionNode curNode, Deque<Point> path) {
		if (curNode != null){
			path.push(curNode.point);
			makePath(curNode.parent, path);
		}
	}

	private double eulerDist(Point from, Point to){
		return Math.sqrt(Math.pow(to.x - from.x, 2)+Math.pow(to.y - from.y, 2));
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
		private double f;
		private double g;
		private Point point;
		
		public DirectionNode(DirectionNode parent, Point point){
			this.point = point;
			this.parent = parent;
		}
		
		public List<Point> getNeighbours() {
			List<Point> neighbours = new ArrayList<Point>();
			for (int x = -1; x < 2; x++){
				for (int y = -1; y < 2; y++){
					Point neighbourPoint = new Point(point.x+x, point.y+y);
					if (!neighbourPoint.equals(new Point(0, 0))){
						neighbours.add(neighbourPoint);
					}
				}
			}
			return neighbours;
		}
		
		public int compareTo(DirectionNode other) {
			return ((Double)f).compareTo(other.f);
		}

	}
}

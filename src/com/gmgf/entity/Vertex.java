package com.gmgf.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Vertex implements Serializable, Comparable<Vertex> {

	private static final long serialVersionUID = 1L;
	private static final AtomicInteger count = new AtomicInteger(0);
	public HashSet<Edge> neighbours = new HashSet<Edge>();

	public int id;
	public String name;
	public double distance;
	public Vertex prev = null;
	public boolean isVisited;
	public LinkedList<Vertex> path;

	public Vertex(String nm) {
		id = count.incrementAndGet();
		name = nm;
		distance = Double.MAX_VALUE;
		path = new LinkedList<Vertex>();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Vertex vertex = (Vertex) obj;
		if (this.name.equals(vertex.name)) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return this.name.hashCode();
	}

	public int compareTo(Vertex v) {
		return Double.compare(distance, v.distance);
	}
}

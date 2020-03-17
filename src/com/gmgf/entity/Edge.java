package com.gmgf.entity;

import java.io.Serializable;

public class Edge implements Serializable {

	private static final long serialVersionUID = 1L;
	public Vertex src;
	public Vertex dest;
	public double weight;

	public Edge(Vertex v1, Vertex v2, double sim) {
		src = v1;
		dest = v2;
		weight = 1-sim;
	}

	public String toString() {
		String edgString;
		edgString = "Src -->" + src.name + "\t" + " Destination-->" + dest.name + "\t" + "weight--> " + weight;
		return edgString;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Edge edge = (Edge) obj;
		if (this.dest.name.equals(edge.dest.name) && this.src.name.equals(edge.src.name)) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return this.src.name.hashCode() + this.dest.name.hashCode();
	}

	// Test equals method
	public static void main(String[] args) {
		Edge edge = new Edge(new Vertex("sm"), new Vertex("dm"), 0.35);
		Edge edge1 = new Edge(new Vertex("sm"), new Vertex("dm"), 0.35);
		Edge edge3 = new Edge(new Vertex("sm"), new Vertex("rdm"), 0.35);
		System.out.println(edge.equals(edge1));
		System.out.println(edge.equals(edge3));
	}
}

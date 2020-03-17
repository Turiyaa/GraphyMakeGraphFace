package com.gmgf.graph;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.gmgf.entity.Edge;
import com.gmgf.entity.Vertex;
import com.gmgf.json.EdgeJsonObject;
import com.gmgf.json.VertexJsonObject;
import com.gmgf.util.Utility;
import com.google.gson.Gson;

public class Dijkstra {
	public String jsonVertices;
	public String jsonEdges;
	Gson gson = new Gson();
	public void dijkstraShortestPath(String source, String target) throws FileNotFoundException {
		boolean pathFound = false;
		source = "https://en.wikipedia.org/wiki/".concat(source);
		target = "https://en.wikipedia.org/wiki/".concat(target);
		Vertex srcVertex = Utility.keyValVertexList.get(source);
		Vertex targetVertex = Utility.keyValVertexList.get(target);
		PriorityQueue<Vertex> pq = new PriorityQueue<>();
		
		srcVertex.distance = 0;
		pq.add(srcVertex);
		while (!pq.isEmpty()) {
			Vertex u = pq.poll();
			for (Edge e : u.neighbours) {
				Vertex v = e.dest;
				double alt = u.distance + e.weight;
				if (alt < v.distance) {
					pq.remove(v);
					v.distance = alt;
					v.path = new LinkedList<>(u.path);
					v.path.add(u);
					pq.add(v);
				}
			}
			if (u.equals(targetVertex)) {
				pathFound = true;
				buildJsonObject(u);
				return;
			}
		}
		if(!pathFound) {
			HashSet<VertexJsonObject> jsonObj = new HashSet<>();
			jsonObj.add(new VertexJsonObject(0, "Path Not Found"));
			List<EdgeJsonObject> edgeObj = new ArrayList<>();
			edgeObj.add(new EdgeJsonObject(0, 0));
			jsonVertices = gson.toJson(jsonObj);
			System.out.println(jsonVertices);
			jsonEdges = gson.toJson(edgeObj);
		}
	}

	private void buildJsonObject(Vertex u) throws FileNotFoundException {
		HashSet<Edge> edges = new HashSet<Edge>();
		HashSet<Vertex> vertex = new HashSet<Vertex>();
		for (Vertex pathvert : u.path) {
			System.out.print(pathvert.name.substring(30) + " ");
			Vertex v2;
			int index = u.path.indexOf(pathvert);
			if(index < u.path.size()-1) {
				v2 = u.path.get(index+1);						
			}else {
				v2 = u;
			}
			edges.add(new Edge(pathvert, v2, .01));
		}
		
		for(Edge e: edges) {
			vertex.add(e.src);
			vertex.add(e.dest);
		}
		
		
		HashSet<VertexJsonObject> jsonObj = new HashSet<>();
		List<EdgeJsonObject> edgeObj = new ArrayList<>();
		for (Edge str : edges) {
			edgeObj.add(new EdgeJsonObject(str.src.id, str.dest.id));
		}
		for (Vertex v : vertex) {
			jsonObj.add(new VertexJsonObject(v.id, v.name));
		}
		jsonEdges = gson.toJson(edgeObj);
		jsonEdges = jsonEdges.replaceAll("\"src\":", "");
		jsonEdges = jsonEdges.replaceAll("\"dest\":", "");

		jsonVertices = gson.toJson(jsonObj);
	}
}

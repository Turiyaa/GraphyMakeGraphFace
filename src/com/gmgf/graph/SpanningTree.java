package com.gmgf.graph;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.gmgf.entity.Edge;
import com.gmgf.entity.Vertex;
import com.gmgf.json.EdgeJsonObject;
import com.gmgf.json.VertexJsonObject;
import com.gmgf.util.Utility;
import com.google.gson.Gson;

public class SpanningTree {
	public String jsonVertices;
	public String jsonEdges;
	public String srcTargetJson;
	public void KMST() throws FileNotFoundException {
		PriorityQueue<Edge> pq = new PriorityQueue<>(Utility.masterList.size(),
				Comparator.comparingDouble(o -> o.weight));
		pq.addAll(Utility.masterList);

		Map<Vertex, Vertex> parent = new HashMap<>();

		makeSet(parent);

		ArrayList<Edge> mst = new ArrayList<>();
		int index = 0;
		while (index < Utility.vertexList.size() - 1) {
			Edge edge = pq.remove();
			Vertex u_set = find(parent, edge.src);
			Vertex v_set = find(parent, edge.dest);

			if (!v_set.equals(u_set)) {
				mst.add(edge);
				index++;
				union(parent, u_set, v_set);
			}
		}
		HashSet<Vertex> countVertex = new HashSet<Vertex>();
		List<String> sourceAndTarget = new ArrayList<String>();
		for (Edge e : mst) {
			countVertex.add(e.src);
			countVertex.add(e.dest);
			//System.out.println(e);
		}

		System.out.println("Number of Edges: " + mst.size());
		System.out.println("Number of Vertices: " + countVertex.size());

		Gson gson = new Gson();
		HashSet<VertexJsonObject> jsonObj = new HashSet<>();
		List<EdgeJsonObject> edgeObj = new ArrayList<>();
		for (Edge str : mst) {
			edgeObj.add(new EdgeJsonObject(str.src.id, str.dest.id));
		}
		for (Vertex v : countVertex) {
			jsonObj.add(new VertexJsonObject(v.id, v.name));
			sourceAndTarget.add(v.name.substring(30));
			
		}
		jsonEdges = gson.toJson(edgeObj);
		jsonEdges = jsonEdges.replaceAll("\"src\":", "");
		jsonEdges = jsonEdges.replaceAll("\"dest\":", "");
		
		jsonVertices = gson.toJson(jsonObj);
		srcTargetJson = gson.toJson(sourceAndTarget);
	}

	public static void makeSet(Map<Vertex, Vertex> parent) {
		for (Vertex v : Utility.vertexList) {
			parent.put(v, v);
		}
	}

	public static Vertex find(Map<Vertex, Vertex> parent, Vertex vertex) {
		if (parent.get(vertex).equals(vertex))
			return vertex;
		return find(parent, parent.get(vertex));
	}

	public static void union(Map<Vertex, Vertex> parent, Vertex x, Vertex y) {
		Vertex x_set_parent = find(parent, x);
		Vertex y_set_parent = find(parent, y);
		parent.put(x_set_parent, y_set_parent);
	}

}

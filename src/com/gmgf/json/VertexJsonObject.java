package com.gmgf.json;

public class VertexJsonObject {
int id;
String label;
public VertexJsonObject(int nodeId, String lbl) {
	id = nodeId;
	if(lbl.length()>30) {
		label = lbl.substring(30);		
	}else {
		label = lbl;
	}
}
}

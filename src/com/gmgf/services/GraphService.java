package com.gmgf.services;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.gmgf.graph.Dijkstra;
import com.gmgf.graph.SpanningTree;
import com.gmgf.util.Utility;

@Path("/graph")
public class GraphService {
	static Dijkstra ijk = new Dijkstra();
	static SpanningTree kmst = new SpanningTree();
    @GET
    @Path("/{name}")
    public Response sayHello(@PathParam("name") String msg) {
        String output = "Hello, " + msg + "!";
        return Response.status(200).entity(output).build();
    }
    
    @GET
    @Path("/edges")
    public Response getKmsEdges() throws ClassNotFoundException, IOException {
    	Utility util = new Utility();
		util.createGraph();
		kmst.KMST();
        return Response.status(200).entity(kmst.jsonEdges).build();
    }
    
    @GET
    @Path("/vertices")
    public Response getKmsVertices() throws ClassNotFoundException, IOException {
    	Utility util = new Utility();
		util.createGraph();
		kmst.KMST();
        return Response.status(200).entity(kmst.jsonVertices).build();
    }
    
    @GET
    @Path("/edges/{source}/{target}")
    public Response getPathEdges(@PathParam("source") String source, @PathParam("target") String target) throws ClassNotFoundException, IOException {
    	Utility util = new Utility();
		util.createGraph();
		kmst.KMST();
		ijk.dijkstraShortestPath(source, target);
        return Response.status(200).entity(ijk.jsonEdges).build();
    }
    @GET
    @Path("/vertex/{source}/{target}")
    public Response getPathVertices(@PathParam("source") String source, @PathParam("target") String target) throws ClassNotFoundException, IOException {
        return Response.status(200).entity(ijk.jsonVertices).build();
    }
    
    @GET
    @Path("/srctarget")
    public Response getVertices() throws ClassNotFoundException, IOException {
        return Response.status(200).entity(kmst.srcTargetJson).build();
    }
}
package com.gmgf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gmgf.entity.Edge;
import com.gmgf.entity.Vertex;

import wiki.wisuggest.service.WiService;

public class Utility {
	public static HashSet<Edge> masterList = new HashSet<Edge>();
	public static HashSet<Vertex> vertexList = new HashSet<Vertex>();
	public static Map<String, Vertex> keyValVertexList = new HashMap<String, Vertex>();
	public static Map<Vertex, HashSet<Edge>> neighbours = new HashMap<Vertex, HashSet<Edge>>();

	static Map<String, Vertex> cacheVertex = new HashMap<String, Vertex>();
	private static final int NUMBER_OF_LINK = 10;
	private static int callCounter = 0;
	public void createGraph() throws IOException, ClassNotFoundException {
		if (!isFileExist()) {
			processUrl();
			writeAllEdges();
		}
		getAllEdges();
		getAllVertices();
	}

	private static boolean validateUrl(String s) {
		try {
			// ignore link if it's longer than 60 character
			// ignore link if it's a subpage of wiki page
			if (s.length() > 60 || s.contains("#") || !s.contains("https://en.wikipedia.org/wiki/")
					|| checkIfRelatedToWikipage(s)) {
				return false;
			} else {
				new URL(s).toURI();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean checkIfRelatedToWikipage(String s) {
		int counter = 0;
		for (int i = 0; i < s.length(); i++) {
			// if there's more than one colon, wiki page is about itself
			if (s.charAt(i) == ':') {
				counter++;
				if (counter > 1) {
					return true;
				}
			}
		}
		return false;
	}

	public static void collectPageLinks(Elements links, Set<String> setList) {
		for (Element lnk : links) {
			if (setList.size() < NUMBER_OF_LINK) {
				String s = lnk.attr("abs:href");
				if (validateUrl(s)) {
					// store unique link
					setList.add(s);
				}
			}
		}
	}

	public static void collectPageLinks(String link, Elements links) throws IOException {
		Set<String> pageSetList = new HashSet<String>();
		for (Element lnk : links) {
			// test for 10 link only for now
			if (pageSetList.size() < NUMBER_OF_LINK) {
				String s = lnk.attr("abs:href");
				if (validateUrl(s)) {
					// store unique link
					pageSetList.add(s);
				}
			}
		}
		calculateSimilarityMatrix(link, pageSetList);
		callCounter++;
		if(callCounter < 10) {
			recursiveLinkCall(pageSetList);
		}
		
		// pageLinks.put(link, pageSetList);
	}

	public static void calculateSimilarityMatrix(String rootLink, Set<String> nodeList) throws IOException {
		double[] similarityMatrix = new double[nodeList.size() - 1];
		HashSet<Edge> fileStructureList = new HashSet<Edge>();

		WiService service = new WiService();
		similarityMatrix = service.processArticles(rootLink, nodeList);
		nodeList.remove(rootLink);
		int counter = 0;
		for (String v2 : nodeList) {
			Vertex root;
			Vertex des;
			if (cacheVertex.get(rootLink) == null) {
				root = new Vertex(rootLink);
				cacheVertex.put(rootLink, root);
			}
			if (cacheVertex.get(v2) == null) {
				des = new Vertex(v2);
				cacheVertex.put(v2, des);
			}
			fileStructureList.add(new Edge(cacheVertex.get(rootLink), cacheVertex.get(v2), similarityMatrix[counter]));
			counter++;
		}
		masterList.addAll(fileStructureList);
		// edges.put(new Edge(rootLink, "", 0), fileStructureList);
	}

	public static void processUrl() throws IOException {
		Set<String> setList = new HashSet<String>();
		HashMap<String, HashSet<String>> pageLinks = new HashMap<String, HashSet<String>>();

		String rootLink = "https://en.wikipedia.org/wiki/Philosophy";
		Elements links = getElements(rootLink);

		// Collect links from page
		collectPageLinks(links, setList);
		int counter = 0;
		int pagesLinkCounter = 0;

		// remove root link
		setList.remove(rootLink);

		for (String lnk : setList) {
			//System.out.println(lnk);
			counter++;
		}
		calculateSimilarityMatrix(rootLink, setList);
		// get page links of all root page links
		recursiveLinkCall(setList);
		// Remove the parent url

		for (Entry<String, HashSet<String>> entry : pageLinks.entrySet()) {
			for (String link : entry.getValue()) {
				//System.out.println(link);
				pagesLinkCounter++;
			}
		}
		System.out.println("Root links counter : " + counter);
		System.out.println("Pages links counter : " + pagesLinkCounter);
	}

	private static void recursiveLinkCall(Set<String> setList) throws IOException {
		for (String link : setList) {
			Elements childLinks = getElements(link);
			collectPageLinks(link, childLinks);
		}
	}

	public static Elements getElements(String link) throws IOException {
		Document doc = Jsoup.connect(link).get();
		Elements links = doc.select("a[href]");
		return links;
	}

	public void writeAllEdges() throws IOException {
		// Write vertex, vertex, weight
		FileOutputStream file = new FileOutputStream(getClass().getClassLoader().getResource("adjacencyList.txt").getFile());
		ObjectOutputStream oos = new ObjectOutputStream(file);
		oos.writeObject(masterList);
		oos.close();
	}

	public void getAllEdges() throws IOException, ClassNotFoundException {

		//FileInputStream file = new FileInputStream("adjacencyList.txt");
		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("adjacencyList.txt");
		ObjectInputStream ois = new ObjectInputStream(inputStream);
		try {
			masterList = (HashSet<Edge>) ois.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Done reading edges");
		} finally {
			ois.close();
		}

		/*
		 * Vertex math = new Vertex("Math"); Vertex chem = new Vertex("Chemistry");
		 * Vertex physics = new Vertex("Physics"); Vertex bio = new Vertex("Biology");
		 * Vertex calculus = new Vertex("Calculus"); Vertex mat215 = new
		 * Vertex("MAT215"); Vertex atom = new Vertex("Atom"); Vertex proton = new
		 * Vertex("Proton"); Vertex neutron = new Vertex("Neutron"); Vertex mass = new
		 * Vertex("Mass");
		 * 
		 * masterList.add(new Edge(math, chem, 0.2)); masterList.add(new
		 * Edge(math,physics,.01)); masterList.add(new Edge(math,calculus,.91));
		 * masterList.add(new Edge(chem, neutron, 0.5)); masterList.add(new Edge(chem,
		 * proton, 0.65)); masterList.add(new Edge(proton, atom, 0.999));
		 * masterList.add(new Edge(physics, bio, 0.25)); masterList.add(new
		 * Edge(physics, atom, 0.75)); masterList.add(new Edge(atom, physics, 0.75));
		 * masterList.add(new Edge(physics, mass, 0.9)); masterList.add(new
		 * Edge(calculus, mat215, 0.25)); masterList.add(new Edge(calculus, math, 0.9));
		 * masterList.add(new Edge(mat215, atom, 0.25));
		 */

	}

	public static void getAllVertices() {
		for (Edge fl : masterList) {
			vertexList.add(fl.src);
			vertexList.add(fl.dest);
			keyValVertexList.put(fl.src.name, fl.src);
			keyValVertexList.put(fl.dest.name, fl.dest);
			HashSet<Edge> neighbours = new HashSet<Edge>();

			if (fl.src.neighbours != null) {
				neighbours = fl.src.neighbours;
				neighbours.add(fl);
				fl.src.neighbours = neighbours;
			} else {
				neighbours.add(fl);
				fl.src.neighbours = neighbours;
			}
		}
		System.out.println(vertexList.size());
		System.out.println(keyValVertexList.size());
		System.out.println(masterList.size());
	}

	public boolean isFileExist() throws FileNotFoundException {
		
		File file = new File(getClass().getClassLoader().getResource("adjacencyList.txt").getFile());
		
		System.out.println(file.getPath());
		if (file.length() > 0) {
			return true;
		}
		return false;
	}
}

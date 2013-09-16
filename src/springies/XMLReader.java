package springies;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import jboxGlue.Mass;
import jboxGlue.MovableMass;
import jboxGlue.Muscle;
import jboxGlue.Spring;

public class XMLReader {

	public ArrayList<Spring> mySpringList = new ArrayList<Spring>();
	HashMap<String, Mass> myMassMap = new HashMap<String, Mass>();
	
	private String xmlFile;
	public XMLReader(String xmlFile){
		this.xmlFile= xmlFile;
	}
	
	public Document docIn() {
		File file = new File(xmlFile);

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(file);
			return doc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, Mass> makeMasses(){
		float x;
		float y;
		float mass;
		double vx;
		double vy;
		
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("mass");
		for (int i = 0; i < nodes.getLength(); i++) {
			vx = 0;
			vy = 0;
			mass = 0;
			Node massItem = nodes.item(i);
			NamedNodeMap nodeMap = massItem.getAttributes();
			
			x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
			y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
			try{mass = Float.parseFloat(nodeMap.getNamedItem("mass").getNodeValue());}
			catch(Exception e){}

			try{vx = Double.parseDouble(nodeMap.getNamedItem("vx").getNodeValue());}
			catch(Exception e){}
			
			try{vy = Double.parseDouble(nodeMap.getNamedItem("vy").getNodeValue());}
			catch(Exception e){}
			
			myMassMap.put(nodeMap.getNamedItem("id").getNodeValue(), new MovableMass(x, y, vx, vy, mass));
		}
		
		nodes = doc.getElementsByTagName("fixed");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node massItem = nodes.item(i);
			NamedNodeMap nodeMap = massItem.getAttributes();
			
			x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
			y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
			
			myMassMap.put(nodeMap.getNamedItem("id").getNodeValue(), new Mass(x, y, 0));
		}
		return myMassMap;
	}
	
	public void makeSprings(){
		Mass one;
		Mass two;
		double restLength;
		double constant;
		
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("spring");
		for (int i = 0; i < nodes.getLength(); i++) {
			restLength = -1;
			constant = 1;
					
			Node springItem = nodes.item(i);
			NamedNodeMap nodeMap = springItem.getAttributes();
			
			one = myMassMap.get(nodeMap.getNamedItem("a").getNodeValue());
			two = myMassMap.get(nodeMap.getNamedItem("b").getNodeValue());

			try{restLength = Double.parseDouble(nodeMap.getNamedItem("restlength").getNodeValue());}
			catch(Exception e){}

			try{constant = Double.parseDouble(nodeMap.getNamedItem("constant").getNodeValue());}
			catch(Exception e){}
			
			if(restLength == -1){
				new Spring(one, two, constant);
			}
			else{
				new Spring(one, two, restLength, constant);
			}
		}
	//	new Spring(myMassMap.get("m1"), myMassMap.get("m13"), 29.0, 0.005);
	}
	
	public void makeMuscles(){
		Mass one;
		Mass two;
		double restLength;
		double constant;
		double amplitude;
		
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("muscle");
		for (int i = 0; i < nodes.getLength(); i++) {
			restLength = -1;
			constant = 1;
					
			Node springItem = nodes.item(i);
			NamedNodeMap nodeMap = springItem.getAttributes();
			
			one = myMassMap.get(nodeMap.getNamedItem("a").getNodeValue());
			two = myMassMap.get(nodeMap.getNamedItem("b").getNodeValue());
			
			amplitude = Double.parseDouble(nodeMap.getNamedItem("amplitude").getNodeValue());
			
			try{restLength = Double.parseDouble(nodeMap.getNamedItem("restlength").getNodeValue());}
			catch(Exception e){}

			try{constant = Double.parseDouble(nodeMap.getNamedItem("constant").getNodeValue());}
			catch(Exception e){}
			
			if(restLength == -1){
				new Muscle(one, two, amplitude);
			}
			else{
				new Muscle(one, two, restLength, constant, amplitude);
			}
		}
	}
	
	public double[] readGravity(){
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("gravity");
		
		Node item = nodes.item(0);
		NamedNodeMap nodeMap = item.getAttributes();
		
		double direction = Double.parseDouble(nodeMap.getNamedItem("direction").getNodeValue());
		double magnitude = Double.parseDouble(nodeMap.getNamedItem("magnitude").getNodeValue());
		double ret[] = {direction, magnitude};
		return ret;
	}
		
	public double readViscosity(){
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("viscosity");
		
		Node item = nodes.item(0);
		NamedNodeMap nodeMap = item.getAttributes();
		
		return Double.parseDouble(nodeMap.getNamedItem("magnitude").getNodeValue());
	}
	
	public double[] readcm(){
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("centermass");
		
		Node item = nodes.item(0);
		NamedNodeMap nodeMap = item.getAttributes();
		double magnitude = Double.parseDouble(nodeMap.getNamedItem("magnitude").getNodeValue());	
		double exponent = Double.parseDouble(nodeMap.getNamedItem("exponent").getNodeValue());
		double ret[] = {magnitude, exponent};
		return ret;
	}
	
	public double[] readWallMag(){
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("wall");
		double ret[] = new double[4];
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node massItem = nodes.item(i);
			NamedNodeMap nodeMap = massItem.getAttributes();
			ret[i] = Double.parseDouble(nodeMap.getNamedItem("magnitude").getNodeValue());
		}
		return ret;
	}
	
	public double[] readWallExp(){
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("wall");
		double ret[] = new double[4];
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node massItem = nodes.item(i);
			NamedNodeMap nodeMap = massItem.getAttributes();
			ret[i] = Double.parseDouble(nodeMap.getNamedItem("exponent").getNodeValue());
		}
		return ret;
	}
	
	/*public ArrayList<Spring> getSpring() {
		
		Document doc = docIn();
		NodeList nodes = doc.getElementsByTagName("spring");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node massItem = nodes.item(i);
			NamedNodeMap nodeMap = massItem.getAttributes();
			
			int len = nodeMap.getLength();
			//System.out.println(len);
			for (int j = 0; j <= nodeMap.getLength()-len; j += len) {
				String a = ""; String b = ""; double rest = 0; double constant = 0;
				
				for(int k=0; k <len; k++){
					String name = nodeMap.item(j+k).getNodeName();
					if(name.equals("a")){
						a = (nodeMap.item(j+k).getNodeValue().toString());
					}
					if(name.equals("b")){
						b = (nodeMap.item(j+k).getNodeValue().toString());
					}
					if(name.equals("restlength")){
						rest = Double.parseDouble(nodeMap.item(j+k).getNodeValue().toString());
					}
					if(name.equals("constant")){
						constant = Double.parseDouble(nodeMap.item(j+k).getNodeValue().toString());
					}
				}

				int m1 = Integer.parseInt(a.substring(1));
				int m2 = Integer.parseInt(b.substring(1));
				MovableMass mass1 = myMassList.get(m1);
				MovableMass mass2 = myMassList.get(m2);
				Spring spring;
				
				if(len == 2){
					spring = new Spring(mass1, mass2);
					mySpringList.add(spring);

				}
				else if(len == 3) {
					spring = new Spring(mass1, mass2, rest);
					mySpringList.add(spring);
				}
				else{
					System.out.println(rest);
					spring = new Spring(mass1, mass2, rest, constant);
					mySpringList.add(spring);
				}
			}
		}
		return mySpringList;
	}
*/}

package ph.edu.dlsu.unbindosmdroid;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


public class PathingAlgo {
	
	ArrayList<Node> nodesList = new ArrayList<>();
	ArrayList<Way> wayList = new ArrayList<>();
	ArrayList<Way> traffic = new ArrayList<>();
	ArrayList<Way> flooded = new ArrayList<>();
	int weather = 0;
	int departure = 0;
	
	ArrayList<Integer> speeds = new ArrayList<Integer>();
	

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/
	
	public Node calcPath(Node from, Node to) {
		speeds.add(60);
		speeds.add(27);
		speeds.add(10);

		ArrayList<GeoPoint> list = new ArrayList<>();

		Node n1 = new Node(14.5649213, 120.99394669999992);
		Node n2 = new Node(14.570302983627025   ,120.99148213863373 );
		Node n3 = new Node(14.582119423673806 ,  120.98474442958832);
		Node n4 = new Node(14.586054635153847 ,  120.98274886608124);

		Node n6 = new Node(14.570302983627025 ,  120.99148213863373);
		Node n7 = new Node(14.576792758935902  , 120.99707186222076);
		Node n8 = new Node(14.580894198356967, 120.99955022335052);
		Node n9 = new Node(14.58548356660996, 120.99391758441925);
		Node n10 = new Node(14.582597052035679, 120.98463714122772);
		Node n11 = new Node(14.586334977351532, 120.98237335681915);

		nodesList.add(n1);
		nodesList.add(n2);
		nodesList.add(n3);
		nodesList.add(n4);

		//nodesList.add(n5);
		nodesList.add(n6);
		nodesList.add(n7);
		nodesList.add(n8);
		nodesList.add(n9);
		nodesList.add(n10);
		nodesList.add(n11);

		wayList.add(new Way(n1,n2));
		wayList.add(new Way(n2,n3));
		wayList.add(new Way(n3,n4));
		wayList.add(new Way(n4,n11));

		wayList.add(new Way(n1,n6));
		wayList.add(new Way(n6,n7));
		wayList.add(new Way(n7,n8));
		wayList.add(new Way(n8,n9));
		wayList.add(new Way(n9,n10));
		wayList.add(new Way(n10,n11));

		traffic.add(new Way(n2,n3, 2));

		//flooded.add(new Way(n3,n4));
		traffic.add(new Way(n3,n4, 2));
		weather = 2;

		from = n1;
		to = n11;

		
		ArrayList<Node> closed = new ArrayList<>();
		ArrayList<Node> open = new ArrayList<>();
		
		open.add(from);
		
		Node currNode = from;
		if (open.size() == 0)
			System.out.println("Wala");
		while (!nEquals(currNode, to)) {
			System.out.println("Daan!!!!");
			currNode = getMinCost(open, to);
			removeFrom(open, currNode);
			closed.add(currNode);
			if(currNode == null)
				System.out.println("pota null ba si currnode");
			if (nEquals(currNode, to))
				return currNode;
			
			ArrayList<Node> neighbors = getNeighbors(currNode);
			System.out.println(neighbors.size());
			for (int i = 0; i < neighbors.size(); i++){
				if (isIn(open, neighbors.get(i))) {
					if (getFrom(open, neighbors.get(i)).getG() > neighbors.get(i).getG()) {
						getFrom(open, neighbors.get(i)).setParent(currNode);
						getFrom(open, neighbors.get(i)).setG(neighbors.get(i).getG());
					}
				}
				else {
					neighbors.get(i).setParent(currNode);
					open.add(neighbors.get(i));
				}
			}

		}
		return currNode;
	}
	
	public ArrayList<Node> getNeighbors(Node currNode) {
		ArrayList<Node> neighbors = new ArrayList<>();
		System.out.println(wayList.size());
		for (int i = 0; i < wayList.size(); i++) {


			if (!isInWay(flooded, wayList.get(i))) {

				if (nEquals(wayList.get(i).getStart(), currNode) && !isIn(neighbors, wayList.get(i).getEnd())) {
					System.out.println("Lats: " + wayList.get(i).getStart().getLati() + " " + currNode.getLati());
					System.out.println("Longs: " + wayList.get(i).getStart().getLongi() + " " + currNode.getLongi());
					System.out.println("Added!");
					neighbors.add(new Node(wayList.get(i).getEnd(), currNode.getG() + getTravelTime(wayList.get(i))));
				}
				else if (nEquals(wayList.get(i).getEnd(), currNode) && !isIn(neighbors, wayList.get(i).getStart())) {
					neighbors.add(new Node(wayList.get(i).getStart(), currNode.getG() + getTravelTime(wayList.get(i))));
				}
			}
		}
		return neighbors;
	}
	
	public Node getMinCost(ArrayList<Node> open, Node to){
		Node smallest = null;
		if (open.size() > 0)
			smallest = open.get(0);
		for (int i = 1; i < open.size(); i++) {
			if (smallest.getCost(to) > open.get(i).getCost(to)) {
				smallest = open.get(i);
			}
		}
		
		return smallest;
	}
	
	public boolean nEquals(Node n1, Node n2) {
		if (Double.compare(n1.getLati(), n2.getLati()) == 0 && Double.compare(n1.getLongi(), n2.getLongi()) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isIn(ArrayList<Node> list, Node node) {
		for (int i = 0; i < list.size(); i++) {
			if (nEquals(list.get(i), node)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInWay(ArrayList<Way> list, Way way) {
		for (int i = 0; i < list.size(); i++) {
			if (nEquals(list.get(i).getStart(), way.getStart()) && nEquals(list.get(i).getEnd(), way.getEnd())) {
				return true;
			}
			else if (nEquals(list.get(i).getStart(), way.getEnd()) && nEquals(list.get(i).getEnd(), way.getStart())) {
				return true;
			}
		}
		return false;
	}

	public Node getFrom(ArrayList<Node> list, Node node) {
		for (int i = 0; i < list.size(); i++) {
			if (nEquals(list.get(i), node)) {
					return list.get(i);
			}
		}
		return null;
	}
	
	public void removeFrom(ArrayList<Node> list, Node node) {
		for (int i = 0; i < list.size(); i++) {
			if (nEquals(list.get(i), node)) {
				list.remove(i);
				return;
			}
		}
	}
	
	public double getTravelTime(Way way) {
		int speed = 27;
		for (int i = 0; i < traffic.size(); i++) {
			if (traffic.get(i) == way) {
				speed = speeds.get(traffic.get(i).getTraffic_status());
			}
		}
		speed -= weather;
		
		return  (way.getDistance()/speed);
	}

}

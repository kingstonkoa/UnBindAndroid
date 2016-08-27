package ph.edu.dlsu.unbindosmdroid;

public class Node {
	double lati;
	double longi;
	double g;
	Node parent;
	
	public Node() {
		lati = 0;
		longi = 0;
		g = 0;
		parent = null;
	}
	
	public Node(double lati, double longi) {
		this.lati = lati;
		this.longi = longi;
		g = 0;
		parent = null;
	}
	
	public Node(Node node, double g) {
		this.lati = node.getLati();
		this.longi = node.getLongi();
		this.g += g;
		parent = null;
	}
	
	public double getLati() {
		return lati;
	}
	
	public double getLongi() {
		return longi;
	}
	
	public void setLati(float lati) {
		this.lati = lati;
	}
	
	public void setLongi(float longi) {
		this.longi = longi;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setG(double g) {
		this.g = g;
	}
	
	public double getG() {
		return g;
	}
	
	public float getCost(Node to) {
		return (float) (g + getDistance(this, to));
	}
	
	public double getDistance(Node start, Node end) {
		// Haversine formula 
		  int R = 6371; // Radius of the earth in km
		  double dLat = deg2rad(end.getLati() - start.getLati());  // deg2rad below
		  double dLon = deg2rad(end.getLongi() - start.getLongi()); 
		  double a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(start.getLati())) * Math.cos(deg2rad(start.getLati())) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2)
		    ; 
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		  double d = R * c; // Distance in km
		  return d;
	}
	
	public double deg2rad(double deg) {
		  return deg * (3.14/180);
	}
}	

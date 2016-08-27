package ph.edu.dlsu.unbindosmdroid;

public class Way {
	Node start;
	Node end;
	int traffic_status;
	
	public Way() {
		start = new Node();
		end = new Node();
		this.traffic_status = 0;
	}

	public Way(Node start, Node end) {
		this.start = start;
		this.end= end;
	}
	
	public Way(Node start, Node end, int traffic_status) {
		this.start = start;
		this.end= end;
		this.traffic_status = traffic_status;
	}
	
	public Node getStart() {
		return start;
	}
	
	public Node getEnd() {
		return end;
	}
	
	public void setEnd(Node end) {
		this.end = end;
	}
	
	public void setStart(Node start) {
		this.start = start;
	}
	
	public double getDistance() {
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
	
	public int getTraffic_status() {
		return traffic_status;
	}
	
	public void setTraffic_status(int traffic_status) {
		this.traffic_status = traffic_status;
	}

}

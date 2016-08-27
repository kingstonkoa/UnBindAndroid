package ph.edu.dlsu.unbindosmdroid;

/**
 * Created by Kingston on 8/27/2016.
 */
public class Point {
    private double longtitude;
    private double latitude;

    public Point(double latitude, double longtitude) {
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}

package ph.edu.dlsu.unbindosmdroid;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    final Context context = this;
    private MapView         mMapView;
    private MapController   mMapController;
    private Button          startTravelButton;
    private int hour;
    private int minute;
    private String AMorPM;
    private String start;
    private String end;
    private TextView etaTv;
    private TextView etalabel;
    private Point initialPoint;
    private Point destidPoint;

    private String your_IP_address = "192.168.0.171:8080"; /* Enter your IP address : port */
    private String your_web_app = "route"; /* Replace this with your own web app name */
    private String baseUrl = "http://" + your_IP_address + "/" + your_web_app + "/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.osm_main);

        startTravelButton = (Button)findViewById(R.id.start_travel_et);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setUseDataConnection(true);
        mMapView.getTileProvider().clearTileCache();
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(13);
        GeoPoint gPt0 = new GeoPoint(14.565468, 120.993169);
        mMapController.setCenter(gPt0);
        etaTv = (TextView) findViewById(R.id.eta_computed_tv);
        etalabel = (TextView) findViewById(R.id.eta_tv);
        //Geocoder myGeocoder;


        startTravelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText departureHour = (EditText) promptsView.findViewById(R.id.hour_et);
                final EditText departureMinuite = (EditText) promptsView.findViewById(R.id.minute_et);;
                final EditText departureAMPM = (EditText) promptsView.findViewById(R.id.am_pm_et);

                final EditText startPoint = (EditText) promptsView.findViewById(R.id.start_et);
                final EditText destination = (EditText) promptsView.findViewById(R.id.destination_et);

                departureAMPM.setFocusable(false);
                departureAMPM.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                departureAMPM.setClickable(false);

                departureAMPM.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (MotionEvent.ACTION_UP == event.getAction()) {
                            if(departureAMPM.getText().toString().equals("AM"))
                                departureAMPM.setText("PM");
                            else
                                departureAMPM.setText("AM");

                        }
                        return true; // return is important...
                    }
                });

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("DONE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        hour = Integer.parseInt(departureHour.getText().toString());
                                        minute = Integer.parseInt(departureMinuite.getText().toString());
                                        AMorPM = departureAMPM.getText().toString();
                                        start = startPoint.getText().toString();
                                        end = destination.getText().toString();



                                        etalabel.setText("ETA: ");
                                        if(Geocoder.isPresent()){
                                            try {
                                                String location = startPoint.getText().toString();
                                                Geocoder gc = new Geocoder(context);
                                                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

                                                List<Point> ll = new ArrayList<Point>(addresses.size()); // A list to save the coordinates if they are available
                                                for(Address a : addresses){
                                                    if(a.hasLatitude() && a.hasLongitude()){
                                                        ll.add(new Point(a.getLatitude(), a.getLongitude()));
                                                    }
                                                }
                                                initialPoint = ll.get(0);
                                            } catch (IOException e) {
                                                // handle the exception
                                            }
                                            try {
                                                String location = destination.getText().toString();
                                                Geocoder gc = new Geocoder(context);
                                                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

                                                List<Point> ll = new ArrayList<Point>(addresses.size()); // A list to save the coordinates if they are available
                                                for(Address a : addresses){
                                                    if(a.hasLatitude() && a.hasLongitude()){
                                                        ll.add(new Point(a.getLatitude(), a.getLongitude()));
                                                    }
                                                }
                                                destidPoint = ll.get(0);
                                            } catch (IOException e) {
                                                // handle the exception
                                            }


                                        }


                                        hour = Integer.valueOf(departureHour.getText().toString());
                                        minute = Integer.valueOf(departureMinuite.getText().toString());

                                        etaTv.setText((computation()));
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });
    }

    private String computation() {
        PathingAlgo pa = new PathingAlgo();
        double minutes;
        Node n;
        ArrayList<Node> paths = new ArrayList<Node>();
        Node n1 = new Node(14.5649213, 120.99394669999992);
        Node n11 = new Node(14.586334977351532, 120.98237335681915);
        n = pa.calcPath(n1, n11);
        //n =pa.calcPath(new Node(initialPoint.getLatitude(), initialPoint.getLongtitude()), new Node(destidPoint.getLatitude(), destidPoint.getLongtitude()));
        minutes = n.getG();
        paths.add(n);
        while (n.getParent() != null) {
            System.out.println("G " + n.getG());
            n = n.getParent();
            paths.add(n);
        }
        Collections.reverse(paths);

        ArrayList<Point> longlatlist = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++)
        {
            longlatlist.add(new Point(paths.get(i).getLati(), paths.get(i).getLongi()));
        }
        draw(longlatlist);

        return String.valueOf(Float.parseFloat(String.valueOf(minutes)));
    }

    public void draw(ArrayList<Point> LongLatList) {
        PathOverlay myPath = new PathOverlay(Color.GREEN, this);
        for(int i = 0; i < LongLatList.size(); i++)
        {
            myPath.addPoint(new GeoPoint(LongLatList.get(i).getLatitude(), LongLatList.get(i).getLongtitude()));
        }
        mMapView.getOverlays().add(myPath);
        Paint pPaint = myPath.getPaint();
        pPaint.setStrokeWidth(15);
        myPath.setPaint(pPaint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
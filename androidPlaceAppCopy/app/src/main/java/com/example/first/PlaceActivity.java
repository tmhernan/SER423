package com.example.first;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class PlaceActivity extends AppCompatActivity {
    private PlaceLibrary places;
    private TextView stud_numberTV;
    private ListView placeLV;
    private String selectedPlace;
    private String selectedDistancePlace;
    private EditText nameBox, descriptionBox, catBox, titleBox, addBox, eleBox, LatBox, LonBox, imageBox; // used in the alert dialog for adding a student
    private String[] placeNames;

    private String[] availTitles;
    private String[] availPrefixes;
    private String[] colLabels;
    private int[] colIds;
    private List<HashMap<String,String>> arrListOfMaps;
    private String address_title = "";
    private String address_street = "";
    private String elevation = "";
    private String latitude = "";
    private String longitude = "";
    private String image = "";
    private String place_decription = "";
    private String place_category = "";
    private Spinner placeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_display);
        placeLV = (ListView)findViewById(R.id.placeDescription_list_view);
        placeSpinner = findViewById(R.id.spinner);
        Intent intent = getIntent();
        selectedPlace = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";
        placeNames = intent.getExtras().getStringArray("names");
        android.util.Log.d("place", selectedPlace);
        this.prepareAdapter();
        this.setupDistanceSpinner();

        //PLACE LIST DESCRIPTION = JUST A DESCRIPTION COLUMN
        SimpleAdapter sa = new SimpleAdapter(this, arrListOfMaps, R.layout.place_list_description, colLabels, colIds);
        placeLV.setAdapter(sa);

        // set up a back button to return to the view main activity / view
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception action bar: "+ex.getLocalizedMessage());
        }
        setTitle(selectedPlace +" Information");
    }


    // create the menu items for this activity, placed in the action bar.
    //where the trash can is
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.places_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected() id: "+item.getItemId()
                +" title "+item.getTitle());
        switch (item.getItemId()) {
            // the user selected the up/home button (left arrow at left of action bar)
            case android.R.id.home:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> home");
                Intent i = new Intent();
                i.putExtra("places", places);
                this.setResult(RESULT_OK,i);
                finish();
                return true;
            // the user selected the action (garbage can) to remove the student
            case R.id.action_remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removePlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareAdapter() {
        android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");
        colLabels = this.getResources().getStringArray(R.array.col_header_placeDescription);
        colIds = new int[]{R.id.place_description};
        android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");

        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select * from place where place_name=?;",
                    new String[]{selectedPlace});

            while (cur.moveToNext()){
                address_title = cur.getString(0);
                address_street = cur.getString(1);
                elevation = Double.toString(cur.getDouble(2));
                latitude = Double.toString(cur.getDouble(3));
                longitude = Double.toString(cur.getDouble(4));
                image = cur.getString(6);
                place_decription = cur.getString(7);
                place_category = cur.getString(8);
            }

            cur.close();

            arrListOfMaps = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> title = new HashMap<>();
            title.put("Place Description", "Place Description");
            arrListOfMaps.add(title);
            android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");


            //FILLING name: row 2
            HashMap<String, String> name = new HashMap<>();
            name.put("Place Description", "Name: " + selectedPlace);
            arrListOfMaps.add(name);
            android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");

            Log.w("name", "mapping: " + selectedPlace + " " + selectedPlace);


            //FILLING description: row 3
            HashMap<String, String> description = new HashMap<>();
            description.put("Place Description", "Description: " + place_decription);
            arrListOfMaps.add(description);

            Log.w("description", "mapping: " + place_decription + " " + place_decription);

            //FILLING category: row 4
            HashMap<String, String> category = new HashMap<>();
            category.put("Place Description", "Category: " + place_category);
            arrListOfMaps.add(category);

            //FILLING addressTitle: row 4
            HashMap<String, String> addressTitle = new HashMap<>();
            if (address_title == null){
                address_title = "not applicable";
            }
            addressTitle.put("Place Description", "Address Title: " + address_title);
            arrListOfMaps.add(addressTitle);

            //FILLING address: row 5
            HashMap<String, String> address = new HashMap<>();
            if (address_street == null){
                address_street = "not applicable";
            }
            address.put("Place Description", "Address: " + address_street);
            arrListOfMaps.add(address);

            //FILLING elevation: row 6
            HashMap<String, String> elevationHash = new HashMap<>();
            elevationHash.put("Place Description", "Elevation: " + elevation);
            arrListOfMaps.add(elevationHash);

            //FILLING elevation: row 7
            HashMap<String, String> latitudeHash = new HashMap<>();
            latitudeHash.put("Place Description", "Latitude: " + latitude);
            arrListOfMaps.add(latitudeHash);

            //FILLING elevation: row 8
            HashMap<String, String> longitudeHash = new HashMap<>();
            longitudeHash.put("Place Description", "Longitude: " + longitude);
            arrListOfMaps.add(longitudeHash);

            //FILLING elevation: row 8
            HashMap<String, String> imageHash = new HashMap<>();
            imageHash.put("Place Description", "Image: " + image);
            arrListOfMaps.add(imageHash);
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup place description fields");
        }
    }

    private void setupDistanceSpinner(){
        try{
            ArrayAdapter distanceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, placeNames);
            placeSpinner.setAdapter(distanceAdapter);
            placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDistancePlace = parent.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView <?> parent) {
                }
            });
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup place spinner");
        }
    }

    public void addClicked (View v) {
        android.util.Log.w("PREPARE ", "In Addclicked");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Edit");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.nameBox = new EditText(this);
        nameBox.setHint("Name");
        layout.addView(nameBox);

        this.descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);

        this.catBox = new EditText(this);
        catBox.setHint("Category");
        layout.addView(catBox);

        this.titleBox = new EditText(this);
        titleBox.setHint("Address Title*");
        layout.addView(titleBox);

        this.addBox = new EditText(this);
        addBox.setHint("Address*");
        layout.addView(addBox);

        this.eleBox = new EditText(this);
        eleBox.setHint("Elevation");
        eleBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(eleBox);

        this.LatBox = new EditText(this);
        LatBox.setHint("Latitude");
        LatBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(LatBox);

        this.LonBox = new EditText(this);
        LonBox.setHint("Longitude");
        LonBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(LonBox);

        this.imageBox = new EditText(this);
        imageBox.setHint("Image");
        layout.addView(imageBox);

        TextView opt = new TextView(this);
        opt.setText(" * = Optional Fields");
        layout.addView(opt);

        dialog.setView(layout);
        dialog.setNegativeButton("Cancel", editListener);
        dialog.setPositiveButton("Submit Edit", editListener);
        dialog.show();
    }

    public void calculateClicked(View v){
        double otherLong = 0.0;
        double otherLat = 0.0;

        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select latitude, longitude from place where place_name=?;",
                    new String[]{selectedDistancePlace});

            if(cur.moveToFirst()){
                otherLong = cur.getDouble(cur.getColumnIndex("longitude"));
                otherLat = cur.getDouble(cur.getColumnIndex("latitude"));
            }
            Toast.makeText(this, "fields: " + otherLong + " "  + otherLat,  Toast.LENGTH_LONG).show();
            calcGCD(otherLong, otherLat);
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to calculate GCD");
        }
    }

    public void calcGCD(double otherLong, double otherLat){
        if (selectedPlace.equals(selectedDistancePlace)) {
            AlertDialog.Builder sameName = new AlertDialog.Builder(PlaceActivity.this);
            sameName.setTitle("Error.");
            sameName.setMessage("You must pick a different place.");
            sameName.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            sameName.show();
        }

        double otherPlaceLat = Math.toRadians(otherLat);
        double otherPlaceLon = Math.toRadians(otherLong);
        double placeLat = Math.toRadians(Double.parseDouble(latitude));
        double placeLon = Math.toRadians(Double.parseDouble(longitude));

        double ang = Math.acos(Math.sin(placeLat) * Math.sin(otherPlaceLat)
                + Math.cos(placeLat) * Math.cos(otherPlaceLat) * Math.cos(otherPlaceLon - placeLon));
        ang = Math.toDegrees(ang);
        double d = 60 * ang;

        //DEBUG
        //Toast.makeText(this, "fields: " + d,  Toast.LENGTH_LONG).show();

    }

    DialogInterface.OnClickListener removeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int whichButton) {
            android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                    (whichButton==DialogInterface.BUTTON_POSITIVE));
            android.util.Log.d(this.getClass().getSimpleName(),"onClick button? "+
                    dialog);
            if(whichButton == DialogInterface.BUTTON_POSITIVE) {
                android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
                String delete = "delete from place where place_name=?;";
                try {
                    PlaceDB db = new PlaceDB((PlaceActivity.this));
                    SQLiteDatabase crsDB = db.openDB();
                    crsDB.execSQL(delete, new String[]{selectedPlace});
                    crsDB.close();
                    db.close();

                    Intent i = new Intent();
                    setResult(RESULT_OK,i);
                    finish();

                }catch(Exception e){
                    android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
                }
            }
        }
    };

    //if nothing was changed, put in different alert dialog
    DialogInterface.OnClickListener editListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int whichButton) {
            if(whichButton == DialogInterface.BUTTON_POSITIVE) {
                android.util.Log.d(this.getClass().getSimpleName(), "edit Clicked");
                Boolean invalidformat = false;
                Boolean invalidNumFormat = false;

                String userAddressTitle = PlaceActivity.this.titleBox.getText().toString();
                String userAddressStreet = PlaceActivity.this.addBox.getText().toString();
                String userPlaceName = PlaceActivity.this.nameBox.getText().toString();
                String userImage = PlaceActivity.this.imageBox.getText().toString();
                String userDescription = PlaceActivity.this.descriptionBox.getText().toString();
                String userCategory = PlaceActivity.this.catBox.getText().toString();
                String userEle = PlaceActivity.this.eleBox.getText().toString();
                String userLat = PlaceActivity.this.LatBox.getText().toString();
                String userLon = PlaceActivity.this.LonBox.getText().toString();
                double eleNum = 0.0, latNum = 0.0, lonNum = 0.0;

                if(userAddressTitle.length()<1 && userAddressStreet.length()<1 && userPlaceName.length()<1 &&
                        userImage.length()<1 && userDescription.length()<1 && userCategory.length()<1 &&
                        userEle.length()<1 && userLat.length()<1 && userLon.length()<1){
                    AlertDialog.Builder empty = new AlertDialog.Builder(PlaceActivity.this);
                    empty.setTitle("Error.");
                    empty.setMessage("Form empty. Try again.");
                    empty.setNegativeButton("Ok", this);
                    empty.show();
                    invalidformat = true;
                }

                if(userPlaceName.length()>0) {
                    for (int i = 0; i < placeNames.length; i++) {
                        if (placeNames[i].equalsIgnoreCase(userPlaceName)) {
                            AlertDialog.Builder name = new AlertDialog.Builder(PlaceActivity.this);
                            name.setTitle("Error. Your entry could not be added");
                            name.setMessage("That place name already exists.");
                            name.setNegativeButton("Ok", this);
                            name.show();
                            invalidformat = true;
                            break;
                        }
                    }
                }else{
                    userPlaceName = selectedPlace;
                }

                //no null check?
                if (userAddressTitle.length() < 1) {
                    userAddressTitle = address_title;
                }

                //no null check?
                if (userAddressStreet.length() < 1) {
                    userAddressStreet = address_street;
                }

                if(userEle.length()>0) {
                    try {
                        eleNum = Double.parseDouble(userEle);
                    }catch(Exception ex){
                        invalidNumFormat = true;
                    }
                }else{
                    eleNum = Double.parseDouble(elevation);
                }

                if(userLat.length()>0) {
                    try {
                        latNum = Double.parseDouble(userLat);
                    }catch(Exception ex){
                        invalidNumFormat = true;
                    }
                }else{
                    latNum = Double.parseDouble(latitude);
                }

                if(userLon.length()>0) {
                    try {
                        lonNum = Double.parseDouble(userLon);
                    }catch(Exception ex){
                        invalidNumFormat = true;
                    }
                }else{
                    lonNum = Double.parseDouble(longitude);
                }

                if (userDescription.length() < 1) {
                    userDescription = place_decription;
                }

                if (userCategory.length() < 1) {
                    userCategory = place_category;
                }

                if (userImage.length() < 1) {
                    userImage = image;
                }

                if (invalidNumFormat){
                    invalidformat = true;
                    invalidNumAlert();
                }

                if (!invalidformat) {
                    try {
                        PlaceDB db = new PlaceDB((PlaceActivity.this));
                        SQLiteDatabase crsDB = db.openDB();
                        ContentValues hm = new ContentValues();
                        hm.put("address_title", userAddressTitle);
                        hm.put("address_street", userAddressStreet);
                        hm.put("elevation", eleNum);
                        hm.put("latitude", latNum);
                        hm.put("longitude", lonNum);
                        hm.put("place_name", userPlaceName);
                        hm.put("image", userImage);
                        hm.put("place_decription", userDescription);
                        hm.put("place_category", userCategory);

                        crsDB.update("place", hm, "place_name=?", new String[]{selectedPlace});
                        crsDB.close();
                        db.close();
                    } catch (Exception e) {
                        android.util.Log.w(this.getClass().getSimpleName(), " error trying to delete student");
                    }

                    AlertDialog.Builder succDialog = new AlertDialog.Builder(PlaceActivity.this);
                    succDialog.setTitle("Success!");
                    succDialog.setMessage("Your changes were made.");
                    succDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                                Intent i = new Intent();
                                setResult(RESULT_OK, i);
                                finish();
                            }
                        }
                    });
                    succDialog.show();
                }
            }
        }
    };

    public void invalidNumAlert(){
        AlertDialog.Builder dbDialog = new AlertDialog.Builder(PlaceActivity.this);
        dbDialog.setTitle("Error. Your entry could not be added");
        dbDialog.setMessage("Elevation, latitude and longitude require a number.");
        dbDialog.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dbDialog.show();
    }

    private void removePlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Place "+this.selectedPlace+"?");
        dialog.setNegativeButton("Cancel", removeListener);
        dialog.setPositiveButton("Remove", removeListener);
        dialog.show();
    }
}

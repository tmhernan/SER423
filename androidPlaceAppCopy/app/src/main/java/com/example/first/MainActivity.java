package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener {

    private EditText nameBox, descriptionBox, catBox, titleBox, addBox, eleBox, LatBox, LonBox, imageBox; // used in the alert dialog for adding a student
    private ListView placesLV;   // the list view for displaying a place
    private PlaceLibrary places;  // a collection of places (serializable)
    private String[] placeNames;

    private String[] colLabels;
    private int[] colIds;
    private List<HashMap<String,String>> fillMaps;

    /*
    Called when the activity is first created. This is where you should do all of your normal static set up:
    create views, bind data to lists, etc. This method also provides you with a Bundle containing the activity's
    previously frozen state, if there was one.

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesLV = (ListView) findViewById(R.id.place_list_view);
        this.setUpPlacesList();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
        placesLV.setAdapter(sa);
        placesLV.setOnItemClickListener(this);
        setTitle("Places");
    }

    private String setUpPlacesList(){
        String ret = "unknown";
        colLabels = this.getResources().getStringArray(R.array.col_header);
        colIds = new int[] {R.id.place_firstTV};

        try{
            //Calling DB and putting names into placenames array
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select place_name from place;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            while(cur.moveToNext()){
                try{
                    al.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            placeNames = (String[]) al.toArray(new String[al.size()]);
            ret = (placeNames.length>0 ? placeNames[0] : "unknown");
            Arrays.sort(this.placeNames);
            //Need to go through place names array and add to map to hook up to adapter

            this.prepareAdapter();
            cur.close();
            db.close();
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
        return ret;
    }

    // this method generates the data needed to create a new list view simple adapter.
    private void prepareAdapter(){

        //FOR ADAPTER
        //fill colLabel string array with col_header portion of array file: NAME
        colLabels = this.getResources().getStringArray(R.array.col_header);

        //FOR ADAPTER
        //fill colIDs string array with col_header portion of array file: NAME
        //integer array used to store the Id’s of the views
        //The views that should display column in the “from” parameter.
        // These should all be TextViews. The first N views in this list are
        // given the values of the first N columns in the “from” parameter.

        //THIS IS THE TEXT VIEW ID IN PLACE_LIST_ITEM
        colIds = new int[] {R.id.place_firstTV};

        //sets this class's placeNames string array to
        //hold all places inside Place Library
        ///this.placeNames = places.getNames();

        ///Arrays.sort(this.placeNames);

        //FOR ADAPTER
        fillMaps = new ArrayList<HashMap<String,String>>();

        //create hashmap for column headers
        HashMap<String,String> titles = new HashMap<>();

        // first row contains column headers
        titles.put("Name","Name");

        //add the column header to an arraylist of type Hashmaps each containing <Key: String, Value: String>
        fillMaps.add(titles);

        // fill in the remaining rows with first last and student id
        for (int i = 0; i < placeNames.length; i++) {

            //Don't need
            //String[]firstNLast = studNames[i].split(" ");

            HashMap<String,String> map = new HashMap<>();

            //print to console
            //Log.d(this.getClass().getSimpleName(),"mapping: "+firstNLast[0]+" "+firstNLast[1]);

            map.put("Name", this.placeNames[i]);

            android.util.Log.w(this.getClass().getSimpleName(),placeNames[i]);

            fillMaps.add(map);
        }

    }

    // create the main_activity_menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    BUTTON ACTION ADD student, i think ???
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_add:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> add");
                this.newStudentAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    ON ITEM CLICK - WHEN PLACE IS CHOSEN
    DISPLAY ACTIVITY CLASS

    //https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener

    Callback method to be invoked when an item in this AdapterView has been clicked.
    parent = AdapterView: The AdapterView where the click happened.
    view = View: The view within the AdapterView that was clicked (this will be a view provided by the adapter)
    position = int: The position of the view in the adapter.
    id = long: The row id of the item that was clicked.

    listview.onitemclicklistener method

     */

    //When user selects a place to look at
    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        android.util.Log.w("method","Inside onItemClick Method");


        //index that the user pushed
        if(position > 0 && position <= placeNames.length) {

            //DEBUG index that the user pushed
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + placeNames[position-1]);

            //STUDENT DISPLAY ACTIVITY
            Intent displayPlace = new Intent(this, PlaceActivity.class);

            //putextra extends data to the next intent ("Variable name", string-to-be-sent)
            ///displayPlace.putExtra("places", places);
            displayPlace.putExtra("selected", placeNames[position-1]);
            displayPlace.putExtra("names", placeNames);
            this.startActivityForResult(displayPlace, 1);
        }
    }

    // called when the finish() method is called in the StudentDisplayActivity. This occurs
    // when done displaying (and possibly modifying students). In case a student has been removed,
    // must update the list view (via a new adapter).
    //THIS IS TO UPDATE THE LIST OF PLACES AFTER THE USER DELETES IT

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        this.setUpPlacesList();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
        placesLV.setAdapter(sa);
        placesLV.setOnItemClickListener(this);
        setTitle("Places");
    }


    //Dialog box adding a student/place
    private void newStudentAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add a Place Description");
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
        titleBox.setHint("Address Title");
        layout.addView(titleBox);

        this.addBox = new EditText(this);
        addBox.setHint("Address");
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

        dialog.setView(layout);
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Add", this);
        dialog.show();
    }


    //Logic for adding a new place
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            Boolean formatCheck = false;
            String userAddressTitle = this.titleBox.getText().toString();
            String userAddressStreet = this.addBox.getText().toString();
            String userPlaceName = this.nameBox.getText().toString();
            String userImage = this.imageBox.getText().toString();
            String userDescription = this.descriptionBox.getText().toString();
            String userCategory = this.catBox.getText().toString();
            String userEle = this.eleBox.getText().toString();
            String userLat = this.LatBox.getText().toString();
            String userLon = this.LonBox.getText().toString();
            double eleNum = 0.0, latNum = 0.0, lonNum = 0.0;

            for (int i = 0; i < placeNames.length; i++) {
                if (placeNames[i].equalsIgnoreCase(userPlaceName)){
                    AlertDialog.Builder dbDialog = new AlertDialog.Builder(this);
                    dbDialog.setTitle("Error. Your entry could not be added");
                    dbDialog.setMessage("That place name already exists.");
                    dbDialog.setNegativeButton("Ok", this);
                    dbDialog.show();
                    formatCheck = true;
                    break;
                }
            }

            if (userEle == null || userEle.length() < 1 ||
                    userLat == null || userLat.length() < 1 ||
                    userLon == null || userLon.length() < 1 ||
                    userPlaceName == null || userPlaceName.length() < 1 ||
                    userImage == null || userImage.length() < 1 ||
                    userDescription == null || userDescription.length() < 1 ||
                    userCategory == null || userCategory.length() < 1) {
                formatCheck = true;
                AlertDialog.Builder dbDialog = new AlertDialog.Builder(this);
                dbDialog.setTitle("Error. Your entry could not be added");
                dbDialog.setMessage("You are missing fields. Only address title and street can be empty.");
                dbDialog.setNegativeButton("Ok", this);
                dbDialog.show();
            }else{
                try {
                    eleNum = Double.parseDouble(userEle);
                    latNum = Double.parseDouble(userLat);
                    lonNum = Double.parseDouble(userLon);
                }catch(Exception ex){
                    AlertDialog.Builder dbDialog = new AlertDialog.Builder(this);
                    formatCheck = true;
                    dbDialog.setTitle("Error. Your entry could not be added");
                    dbDialog.setMessage("Elevation, latitude and longitude require a number only.");
                    dbDialog.setNegativeButton("Ok", this);
                    dbDialog.show();
                }
            }


            if (userAddressTitle == null || userAddressTitle.length() < 1){
                userAddressTitle = null;
            }

            if (userAddressStreet == null || userAddressStreet.length() < 1){
                userAddressStreet = null;
            }

            if (!formatCheck) {
                try {
                        PlaceDB db = new PlaceDB((Context) this);
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

                        android.util.Log.w("onclick", "message is " + nameBox.getText() + eleBox.getText()
                                + this.titleBox.getText() + eleNum + latNum + lonNum
                                + this.addBox.getText() + this.imageBox.getText() + this.descriptionBox.getText()
                                + this.catBox.getText());

                        crsDB.insert("place", null, hm);
                        crsDB.close();
                        db.close();
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "unable to add entry to database" + ex.getMessage() + "Stacktrace: " + ex.getStackTrace());
                    }
            }
            setUpPlacesList();
            SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
            placesLV.setAdapter(sa);
            placesLV.setOnItemClickListener(this);
            setTitle("Places");
        }
    }


}

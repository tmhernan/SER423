package com.example.first;
import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class PlaceActivity extends AppCompatActivity {
    //AdapterView.OnItemSelectedListener,

    private PlaceLibrary places;
    private TextView stud_numberTV;
    private ListView placeLV;
    private String selectedPlace;
    private EditText nameBox, descriptionBox, catBox, titleBox, addBox, eleBox, LatBox, LonBox, imageBox; // used in the alert dialog for adding a student

    private String[] availTitles;
    private String[] availPrefixes;
    private String[] colLabels;
    private int[] colIds;
    private Spinner courseSpinner;
    private List<HashMap<String,String>> arrListOfMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Text "Make Changes to Place"
        //Two Buttons: "Edit" and "Remove"
        //List view with ID: placeDescription_list_view
        setContentView(R.layout.place_display);

        //This is a list view variable
        //Container for list view: place description

        placeLV = (ListView)findViewById(R.id.placeDescription_list_view);

        //I took this out. I listed the name of the place in
        //the list view so no need to repeat it
        //stud_numberTV = (TextView)findViewById(R.id.stud_numberTV);

        /* SPINNER
        //availTitles = this.getResources().getStringArray(R.array.course_titles);
        //availPrefixes = this.getResources().getStringArray(R.array.course_prefixes);
        // courseSpinner = (Spinner)findViewById(R.id.courseSpinner);
        ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, availPrefixes);
        courseSpinner.setAdapter(anAA);
        courseSpinner.setOnItemSelectedListener(this);
        selectedCourse = availPrefixes.length>0 ? availPrefixes[0] : getString(R.string.unknown);
        */

        Intent intent = getIntent();

        selectedPlace = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";

        android.util.Log.d("place", selectedPlace);

        this.prepareAdapter();

        //PLACE LIST DESCRIPTION = JUST A DESCRIPTION COLUMN
        SimpleAdapter sa = new SimpleAdapter(this, arrListOfMaps, R.layout.place_list_description, colLabels, colIds);
        placeLV.setAdapter(sa);
        //this is the listener to select a course
        //placeLV.setOnItemClickListener(this);


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

        //FOR ADAPTER
        //fill colIDs string array with col_header portion of array file: NAME
        //integer array used to store the Id’s of the views
        //The views that should display column in the “from” parameter.
        // These should all be TextViews. The first N views in this list are
        // given the values of the first N columns in the “from” parameter.

        //THIS IS THE TEXT VIEW ID IN PLACE_LIST_DESCRIPTION
        colIds = new int[]{R.id.place_description};

        //Need to get the place by name
        ///PlaceDescription aPlace = places.get(selectedPlace);
        android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");

        try{
        PlaceDB db = new PlaceDB((Context)this);
        SQLiteDatabase crsDB = db.openDB();
        Cursor cur = crsDB.rawQuery("select * from place where place_name=?;",
                new String[]{selectedPlace});
        String address_title = "";
        String address_street = "";
        String elevation = "";
        String latitude = "";
        String longitude = "";
        String image = "";
        String place_decription = "";
        String place_category = "";


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

            Log.w("name", "mapping: " + elevation + " " + latitude + " " + longitude);

            // the model, first row is header strings for the columns
            arrListOfMaps = new ArrayList<HashMap<String, String>>();

            android.util.Log.w("PREPARE ADAPTER", "PREPARE ADAPTER");

            //FILL header row, first one
            //each map is a row, titles will be a row, arrListOfMaps is the whole thing
            //this is showing
            HashMap<String, String> title = new HashMap<>();
            //PLACE DESCRIPTION IS WHAT YOU'RE PUTTING IN PLACE LIST DESCRITPION, IDIOT
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
            addressTitle.put("Place Description", "Address Title: " + address_title);
            arrListOfMaps.add(addressTitle);

            //FILLING address: row 5
            HashMap<String, String> address = new HashMap<>();
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
            longitudeHash.put("Place Description", "Image: " + image);
            arrListOfMaps.add(imageHash);

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }






    }



/*
    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedCourse = courseSpinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner item "+
                courseSpinner.getSelectedItem().toString() + " selected.");
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"In onNothingSelected: No item selected");

    }

    // returns -1 if the course prefix is not found in takes. Otherwise, the index into the vector
    // where it is found.

    private int findPrefix(Vector<Course> takes, String prefix) {
        int ret = -1;
        for(int i=0; i<takes.size(); i++){
            Course aCrs = takes.get(i);
            if (aCrs.prefix.equalsIgnoreCase(prefix)){
                ret = i;
            }
        }
        return ret;
    }
*/
    // add the course selected from all courses spinner from the student's takes, if not already present
    //THIS IS THE EDIT BUTTON
    //NEED TO TRANSFORM TO MAKE A DIALOG BOX OPEN
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
        //dialog.setNegativeButton("Cancel", this);
        //dialog.setPositiveButton("Submit Edit", this);
        dialog.show();
    }



// show an alert view for the user to confirm removing the selected student

    private void removePlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Place "+this.selectedPlace+"?");
        dialog.setNegativeButton("Cancel", myListener);
        dialog.setPositiveButton("Remove", myListener);
        dialog.show();
    }

/*
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
                PlaceDB db = new PlaceDB((Context) this);
                SQLiteDatabase crsDB = db.openDB();
                crsDB.execSQL(delete, new String[]{this.selectedPlace});
                crsDB.close();
                db.close();

                Intent i = new Intent();
                //i.putExtra("places", places);
                this.setResult(RESULT_OK,i);
                finish();

            }catch(Exception e){
                android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
            }
        }
    }

 */

    DialogInterface.OnClickListener myListener = new DialogInterface.OnClickListener() {
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
                    //i.putExtra("places", places);
                    setResult(RESULT_OK,i);
                    finish();

                }catch(Exception e){
                    android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
                }
            }
        }
    };
}

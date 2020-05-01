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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) 2020 Tiffany Hernandez,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. Exception: Instructor Tim Lindquist and Arizona
 * State University has the right to build and evaluate this software package
 * for the purpose of determining a grade and program assessment.
 *
 * @author Tiffany Hernandez
 * @version April 25, 2020
 */

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener {
    private EditText nameBox, descriptionBox, catBox, titleBox, addBox, eleBox, LatBox, LonBox, imageBox;
    private ListView placesLV;
    //private PlaceLibrary places;
    private String[] placeNames;

    private String[] colTag;
    private int[] colNum;
    private List<HashMap<String,String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesLV = (ListView) findViewById(R.id.place_list_view);
        this.setUpPlacesList();
        SimpleAdapter simpleA = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colTag, colNum);
        placesLV.setAdapter(simpleA);
        placesLV.setOnItemClickListener(this);
        setTitle("Places");
    }

    private void setUpPlacesList(){
        colTag = this.getResources().getStringArray(R.array.col_header);
        colNum = new int[] {R.id.place_firstTV};

        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase sqldb = db.openDB();
            Cursor cursor = sqldb.rawQuery("select place_name from place;", new String[]{});
            ArrayList<String> arrL = new ArrayList<String>();
            while(cursor.moveToNext()){
                try{
                    arrL.add(cursor.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            placeNames = (String[]) arrL.toArray(new String[arrL.size()]);
            Arrays.sort(this.placeNames);
            this.prepareAdapter();
            cursor.close();
            db.close();
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
    }

    private void prepareAdapter(){
        colTag = this.getResources().getStringArray(R.array.col_header);
        colNum = new int[] {R.id.place_firstTV};
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        titles.put("Name","Name");
        fillMaps.add(titles);
        for (int i = 0; i < placeNames.length; i++) {
            HashMap<String,String> map = new HashMap<>();
            map.put("Name", this.placeNames[i]);
            android.util.Log.w(this.getClass().getSimpleName(),placeNames[i]);
            fillMaps.add(map);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        android.util.Log.w("method","Inside onItemClick Method");
        if(position > 0 && position <= placeNames.length) {
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + placeNames[position-1]);
            Intent displayPlace = new Intent(this, PlaceActivity.class);
            displayPlace.putExtra("selected", placeNames[position-1]);
            displayPlace.putExtra("names", placeNames);
            this.startActivityForResult(displayPlace, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        this.setUpPlacesList();
        SimpleAdapter simpleA = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colTag, colNum);
        placesLV.setAdapter(simpleA);
        placesLV.setOnItemClickListener(this);
        setTitle("Places");
    }

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

            if (userEle.length() < 1 || userLat.length() < 1 || userLon.length() < 1 ||
                userPlaceName.length() < 1 || userImage.length() < 1 || userDescription.length() < 1 ||
                userCategory.length() < 1) {
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


            if (userAddressTitle.length() < 1){
                userAddressTitle = null;
            }

            if (userAddressStreet.length() < 1){
                userAddressStreet = null;
            }

            if (!formatCheck) {
                try {
                        PlaceDB db = new PlaceDB((Context) this);
                        SQLiteDatabase sqldb = db.openDB();
                        ContentValues cv = new ContentValues();
                        cv.put("address_title", userAddressTitle);
                        cv.put("address_street", userAddressStreet);
                        cv.put("elevation", eleNum);
                        cv.put("latitude", latNum);
                        cv.put("longitude", lonNum);
                        cv.put("place_name", userPlaceName);
                        cv.put("image", userImage);
                        cv.put("place_decription", userDescription);
                        cv.put("place_category", userCategory);

                        android.util.Log.w("onclick", "message is " + nameBox.getText() + eleBox.getText()
                                + this.titleBox.getText() + eleNum + latNum + lonNum
                                + this.addBox.getText() + this.imageBox.getText() + this.descriptionBox.getText()
                                + this.catBox.getText());

                        sqldb.insert("place", null, cv);
                        sqldb.close();
                        db.close();
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "unable to add entry to database" + ex.getMessage() + "Stacktrace: " + ex.getStackTrace());
                    }
            }
            setUpPlacesList();
            SimpleAdapter simpleA = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colTag, colNum);
            placesLV.setAdapter(simpleA);
            placesLV.setOnItemClickListener(this);
            setTitle("Places");
        }
    }
}

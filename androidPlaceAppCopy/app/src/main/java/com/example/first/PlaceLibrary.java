package com.example.first;

import android.app.Activity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

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

public class PlaceLibrary extends Object implements Serializable {

    public Hashtable<String, PlaceDescription> places;
    private static final boolean debugOn = false;

    public PlaceLibrary(Activity parent) {
        debug("creating a new student collection");
        places = new Hashtable<String, PlaceDescription>();
        try {
            this.resetFromJsonFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from students json file" + ex.getMessage());
        }
    }
    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), "debug: " + message);
    }

    public boolean resetFromJsonFile(Activity parent) {
        boolean ret = true;
        try {
            //clears json file
            places.clear();

            //gets json file
            InputStream inputStream = parent.getApplicationContext().getResources().openRawResource(R.raw.places);
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));

            // note that the json is in a multiple lines of input so need to read line-by-line
            StringBuffer strBuffer = new StringBuffer();
            while (bufferReader.ready()) {
                strBuffer.append(bufferReader.readLine());
            }
            //placing the whole file in a string
            String placesJsonStr = strBuffer.toString();

            //placing the whole file in a json object
            JSONObject placesJson = new JSONObject(new JSONTokener(placesJsonStr));

            //android.util.Log.w("place","importing LIBRARY : " + placesJson.toString() + "\n\n");

            //Assigning iterator to json object keys
            Iterator<String> it = placesJson.keys();
            while (it.hasNext()) {

                //iterator is named it.next and
                //here it's saving the key name into pName string varible.
                String pName = it.next();

                //breaking down file into json objects by place
                JSONObject aPlace = placesJson.optJSONObject(pName);

                //android.util.Log.w("place","importing place named " + pName + " json is: " + aPlace.toString() + "\n");

                //Put place in hashtable
                if (aPlace != null) {

                    PlaceDescription place = new PlaceDescription(aPlace.toString());
                    places.put(pName, place);
                }
            }
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception reading json file: " + ex.getMessage());
            ret = false;
        }
        return ret;
    }

    public boolean add(PlaceDescription aPlace) {
        boolean ret = true;
        debug("adding student named: " + ((aPlace == null) ? "unknown" : aPlace.name));
        try {
            places.put(aPlace.name, aPlace);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    public boolean remove(String aName) {
        debug("removing student named: " + aName);
        return ((places.remove(aName) == null) ? false : true);
    }


    public String[] getNames(){
        String[] result = {};
        debug("getting " + places.size() + " places names.");
        if (places.size() > 0) {
            result = (String[]) (places.keySet()).toArray(new String[0]);
        }
        return result;
    }
    public String getByName(String aName) {
        String ret = "unknown";
        String[] keys = (String[]) (places.keySet()).toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            PlaceDescription aStud = places.get(keys[i]);
            if (aStud.name.equals(aName)) {
                ret = aStud.name;
                break;
            }
        }
        return ret;
    }

    public PlaceDescription get(String aName) {
        PlaceDescription ret = new PlaceDescription("unknown", "unknown", "unknown",
                "unknown", "unknown", 0.0, 0.0,
        0.0);
        PlaceDescription aPlace = places.get(aName);
        if (aPlace != null) {
            ret = aPlace;
        }
        return ret;
    }

}

package com.example.first;

import android.media.audiofx.AudioEffect;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Vector;
import java.util.jar.Attributes;


public class PlaceDescription implements Serializable {
    private static final boolean debugOn = false;

    public String name;
    public String description;
    public String category;
    public String addressTitle;
    public String address;
    public double elevation;
    public double latitude;
    public double longitude;

    public PlaceDescription (String name, String description, String category,
            String addressTitle, String address, double elevation, double latitude,
            double longitude){

        this.name = name;
        this.description = description;
        this.category = category;
        this.addressTitle = addressTitle;
        this.address = address;
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //NEED TO FILL THIS IN
    public PlaceDescription(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("name");
            description = jo.getString("description");
            category = jo.getString("category");
            addressTitle = jo.getString("address-title");
            address = jo.getString("address-street");
            elevation = jo.getDouble("elevation");
            latitude = jo.getDouble("latitude");
            longitude = jo.getDouble("longitude");

        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
    }

    public PlaceDescription(JSONObject jsonObj){
        try{
            debug("constructor from json received: " + jsonObj.toString());
            //optString = getString w/ returns empty string if doesn't exist
            name = jsonObj.optString("name","unknown");
            description = jsonObj.optString("description", "unknown");
            category = jsonObj.optString("category", "unknown");
            addressTitle = jsonObj.optString("addressTitle", "unknown");
            address = jsonObj.optString("address", "unknown");
            elevation = jsonObj.optInt("elevation", 0);
            latitude = jsonObj.optInt("latitude", 0);
            longitude = jsonObj.optInt("longitude", 0);

            android.util.Log.w("JSON constructor", "DETAILS " + name + description + category +"\n");

        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Place from json string");
        }
    }

    public JSONObject toJson(){
        JSONObject jOjbect = new JSONObject();
        try{
            jOjbect.put("name",name);
            jOjbect.put("description",description);
            jOjbect.put("category",category);
            jOjbect.put("addressTitle",addressTitle);
            jOjbect.put("address",address);
            jOjbect.put("elevation",elevation);
            jOjbect.put("latitude",latitude);
            jOjbect.put("longitude",longitude);

        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Place from json string");
        }
        return jOjbect;
    }

    public String toJsonString(){
        String returnedString = "";
        try{
            returnedString = this.toJson().toString();
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Place from json string");
        }
        return returnedString;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Place ").append(name).append(" is at ");
        sb.append(address).append(" . ");

        return sb.toString();
    }

    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), message);
    }
}

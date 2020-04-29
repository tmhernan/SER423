package com.example.first;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class PlaceDB extends SQLiteOpenHelper {
    private static final boolean debugon = true;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "placesdb";
    private String dbPath;
    private SQLiteDatabase crsDB;
    private final Context context;

    public PlaceDB(Context context){
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        // place the database in the files directory. Could also place it in the databases directory
        // with dbPath = context.getDatabasePath("dbName"+".db").getPath();
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"db path is: "+
                context.getDatabasePath("placesdb"));
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    /**
     * Does the database exist and has it been initialized? This method determines whether
     * the database needs to be copied to the data/data/pkgName/files directory by
     * checking whether the file exists. If it does it checks to see whether the db is
     * uninitialized or whether it has the course table.
     * @return false if the database file needs to be copied from the assets directory, true
     * otherwise.
     */
    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean crsTabExists = false;
        try{
            String path = dbPath + dbName + ".db";
            debug("placesdb --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("placesdb --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='place';", null);
                    if(tabChk == null){
                        debug("placesdb --> checkDB","check for place table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("placesdb --> checkDB","check for place table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if(crsTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM place", null);
                        c.moveToFirst();
                        while(!c.isAfterLast()) {
                            String crsName = c.getString(5);
                            //int crsid = c.getInt(1);
                            debug("placesdb --> checkDB","Place table has Place Name: "+
                                    crsName+"\t");
                            c.moveToNext();
                        }
                        crsTabExists = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("placesdb->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return crsTabExists;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("placesdb --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.placesdb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("placesdb --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("placesdb --> openDB", "opened db at path: " + crsDB.getPath());
        }else{
            try {
                this.copyDB();
                crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return crsDB;
    }

    @Override
    public synchronized void close() {
        if(crsDB != null)
            crsDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }
}

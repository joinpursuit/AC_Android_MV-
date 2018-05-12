package nyc.c4q.doggos.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DoggoDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DOGGO_DB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME_BREEDS = "BREEDS";
    private static final String COL_NAME_ID = "_ID";
    private static final String COL_NAME_BREED = "BREED";
    private static final String CREATE_BREED_TABLE = "CREATE TABLE " + TABLE_NAME_BREEDS + " (" +
            COL_NAME_ID + " INTEGER PRIMARY KEY," +
            COL_NAME_BREED + " TEXT)";

    private static final String TABLE_NAME_DOGGOS = "DOGGOS";
    private static final String COL_NAME_DOGGO_IMAGE_URL = "DOGGO_IMG_URL";
    private static final String CREATE_DOGGO_TABLE = "CREATE TABLE " + TABLE_NAME_DOGGOS + " (" +
            COL_NAME_ID + " INTEGER PRIMARY KEY," +
            COL_NAME_BREED + " TEXT," +
            COL_NAME_DOGGO_IMAGE_URL + " TEXT)";

    private static DoggoDbHelper instance;

    public static DoggoDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DoggoDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DoggoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BREED_TABLE);
        db.execSQL(CREATE_DOGGO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BREEDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOGGOS);
            onCreate(db);
        }
    }

    public void saveBreedNames(List<String> breedNames) {
        SQLiteDatabase db = getWritableDatabase();

        // Clear out any existing entries in that table
        db.delete(TABLE_NAME_BREEDS, null, null);

        // Loop thru new names and add to table
        for (String name : breedNames) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME_BREED, name);
            db.insert(TABLE_NAME_BREEDS, null, contentValues);
        }

        db.close();
    }

    public List<String> getBreedNames() {
        SQLiteDatabase db = getReadableDatabase();

        List<String> names = new ArrayList<>();

        // Get all breed name values from the table
        Cursor cursor = db.query(TABLE_NAME_BREEDS, null, null, null,
                null, null, null);

        // Make sure the cursor isn't empty
        if (cursor.moveToFirst()) {
            // Loop thru the rows in the cursor and grab the breed name from each one
            while (!cursor.isAfterLast()) {
                names.add(cursor.getString(cursor.getColumnIndex(COL_NAME_BREED)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return names;
    }

    public void saveDoggoImageUrls(String breedName, List<String> doggoImageUrls) {
        SQLiteDatabase db = getWritableDatabase();

        // Clear out any existing entries in that table for that breed name
        db.delete(TABLE_NAME_DOGGOS, COL_NAME_BREED + " = ?", new String[]{breedName});

        // Loop thru new urls and add to table
        for (String url : doggoImageUrls) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME_BREED, breedName);
            contentValues.put(COL_NAME_DOGGO_IMAGE_URL, url);
            db.insert(TABLE_NAME_DOGGOS, null, contentValues);
        }

        db.close();
    }

    public List<String> getDoggoImageUrls(String breedName) {
        SQLiteDatabase db = getReadableDatabase();

        List<String> urls = new ArrayList<>();

        // Get all urls for the specified breed
        Cursor cursor = db.query(TABLE_NAME_DOGGOS, null, COL_NAME_BREED + " = ?",
                new String[]{breedName},null, null, null);

        // Make sure the cursor isn't empty
        if (cursor.moveToFirst()) {
            // Loop thru the rows in the cursor and grab the breed name from each one
            while (!cursor.isAfterLast()) {
                urls.add(cursor.getString(cursor.getColumnIndex(COL_NAME_DOGGO_IMAGE_URL)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return urls;
    }
}

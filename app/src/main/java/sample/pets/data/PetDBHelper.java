package sample.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silpa on 11/7/17.
 */

public class PetDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG=PetDBHelper.class.getSimpleName();

    public static final String DATABASE_NAME="shelter.db";
    public static final int DATABASE_VERSION=1;
    public PetDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
      String SQL_CREATE_PET_TABLE="CREATE TABLE " + PetContract.PetEntry.TABLE_NAME+
              "("+ PetContract.PetEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+ PetContract.PetEntry.COLUMN_PET_NAME+" TEXT NOT NULL,"+
              PetContract.PetEntry.COLUMN_BREED_NAME+" TEXT,"+ PetContract.PetEntry.COLUMN_PET_GENDER+" INTEGER NOT NULL,"+
              PetContract.PetEntry.COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0);";
db.execSQL(SQL_CREATE_PET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

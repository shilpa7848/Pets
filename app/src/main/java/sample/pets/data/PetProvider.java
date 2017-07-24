package sample.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by silpa on 17/7/17.
 */

public class PetProvider extends ContentProvider{
    public static final int PETS=100;
    public static final int PET_ID=101;
    public static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    public PetDBHelper dbHelper;
    public static final String LOG_TAG=PetProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PETS);
    }
    @Override
    public boolean onCreate() {
        dbHelper=new PetDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        int match= sUriMatcher.match(uri);
        switch (match){
            case PETS:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                selection= PetContract.PetEntry._ID +"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                default:
                    throw  new IllegalArgumentException("Cannot Query Unknown Uri"+uri);
        }

    cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("MIME type is not identified" + uri +"with match"+match);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
      final  int match= sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri,values);
            default:
                throw  new IllegalArgumentException("insert pet is interuppted"+uri);

        }
    }
    public  Uri insertPet(Uri uri,ContentValues values){
SQLiteDatabase db=dbHelper.getWritableDatabase();
        long id=db.insert(PetContract.PetEntry.TABLE_NAME,null,values);
        if(id==-1){
            Log.e(LOG_TAG,"Failed to add row"+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int rowsDeleted;
        int match=sUriMatcher.match(uri);
        switch (match){
            case PETS:
                rowsDeleted= db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case PET_ID:
                selection= PetContract.PetEntry._ID+"#";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted= db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("row is not deleted"+uri);
        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match=sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return updatePet(uri,values,selection,selectionArgs);
            case PET_ID:
                selection= PetContract.PetEntry._ID+"#";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not entered"+uri);
        }

    }
    public int updatePet( Uri uri, ContentValues values,String selection, String[] selectionArgs){

        if(values.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)){
            String name=values.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if(name==null){
                throw new IllegalArgumentException("Pet name is required");
            }
        }
        if(values.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)){
            Integer weight=values.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if(weight!=null & weight<0){
                throw new IllegalArgumentException("Pet name is required");
            }
        }
        if(values.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)){
            Integer gender=values.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if(gender==null){
                throw new IllegalArgumentException("Pet name is required");
            }
        }
        if(values.size()==0){
            return 0;
        }
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int rowsUpdated=db.update(PetContract.PetEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;

    }
}

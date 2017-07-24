package sample.pets;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import sample.pets.adapter.PetCursorAdapter;
import sample.pets.data.PetContract;
import sample.pets.data.PetDBHelper;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER = 0;
    PetCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });
//        displayDatabaseInfo();

        ListView petListView = (ListView) findViewById(R.id.list_item);

        adapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(adapter);
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,EditorActivity.class);
                Uri currentPets=ContentUris.withAppendedId(PetContract.PetEntry.COMPLETE_URI,id);
                intent.setData(currentPets);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

//    public void displayDatabaseInfo(){
////         dbHelper= new PetDBHelper(this);
////        SQLiteDatabase db=dbHelper.getReadableDatabase();
//         String[] projection={PetContract.PetEntry._ID, PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_BREED_NAME, PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.COLUMN_PET_WEIGHT};
//       Cursor cursor=getContentResolver().query(PetContract.PetEntry.COMPLETE_URI,projection,null,null,null);
////        Cursor cursor= db.rawQuery("SELECT * FROM "+ PetContract.PetEntry.TABLE_NAME,null);
////        TextView display = (TextView) findViewById(R.id.text_view_pet);
////       try {
////           display.setText("No of Pets" + cursor.getCount()+"\n\n");
////           display.append(PetContract.PetEntry._ID+" - "+ PetContract.PetEntry.COLUMN_PET_NAME +" - "+ PetContract.PetEntry.COLUMN_BREED_NAME +" - "+ PetContract.PetEntry.COLUMN_PET_GENDER + " - "+ PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");
////           int idIndex=cursor.getColumnIndex(PetContract.PetEntry._ID);
////           int nameIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
////           int breedIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED_NAME);
////           int genderIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
////           int weightIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
////
////           while (cursor.moveToNext()){
////               int currentId=cursor.getInt(idIndex);
////               String currentName=cursor.getString(nameIndex);
////               String currentBreed=cursor.getString(breedIndex);
////               int currentGender=cursor.getInt(genderIndex);
////               int currentWeight=cursor.getInt(weightIndex);
////               display.append("\n" +currentId+ " - "+currentName + " - " +currentBreed+ " - "+currentGender+ " - "+currentWeight);
////           }
////
////       }
////       finally {
////           cursor.close();
//
//        ListView petView=(ListView)findViewById(R.id.list_item);
//        PetCursorAdapter adapter=new PetCursorAdapter(this,cursor);
//        petView.setAdapter(adapter);
//       }


    public void insertPet(){
//      SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,"Snoopy");
        values.put(PetContract.PetEntry.COLUMN_BREED_NAME,"puppy");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.mMale);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,8);
        Uri newUri=getContentResolver().insert(PetContract.PetEntry.COMPLETE_URI,values);

    }
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetContract.PetEntry.COMPLETE_URI, null, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_empty_data:
                insertPet();
                //             displayDatabaseInfo();
                return true;
            case R.id.delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_BREED_NAME};
        return new CursorLoader(this,
                PetContract.PetEntry.COMPLETE_URI,
                projection,
                null,
                null,
                null);

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

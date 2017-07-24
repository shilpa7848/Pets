package sample.pets;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import sample.pets.data.PetContract;
import sample.pets.data.PetDBHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
        EditText mName;
        EditText mBreed;
        EditText mWeight;
        Spinner mGender;
        int mGenderValue= PetContract.PetEntry.mUnknown;
    private static final int EDITOR_LOADER=0;
    Uri currentPetsUri;
    public Boolean mPetHasChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent=getIntent();
         currentPetsUri=intent.getData();
        if(currentPetsUri==null){
            setTitle("Add a Pet");
        }else{
            setTitle("Edit the Pet");
//          getLoaderManager().initLoader(EDITOR_LOADER,null,this);
        }
        mName.setOnTouchListener(listener);
        mBreed.setOnTouchListener(listener);
        mGender.setOnTouchListener(listener);
        mWeight.setOnTouchListener(listener);
        mName=(EditText)findViewById(R.id.edit_pet_name);
        mBreed=(EditText)findViewById(R.id.edit_breed_name);
        mWeight=(EditText)findViewById(R.id.weight_of_pet);
        mGender=(Spinner)findViewById(R.id.spinner);
        setUpSpinner();
    }
    private View.OnTouchListener listener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetHasChanged=true;
            return false;
        }
    };
    public void setUpSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.Gender_Array_options, R.layout.support_simple_spinner_dropdown_item);
        genderSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mGender.setAdapter(genderSpinnerAdapter);
        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.Male))) {
                        mGenderValue = PetContract.PetEntry.mMale;
                    } else if (selection.equals(getString(R.string.Female))) {
                        mGenderValue = PetContract.PetEntry.mFemale;
                    } else
                        mGenderValue = PetContract.PetEntry.mUnknown;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGenderValue = 0;
            }
        });

    }
    private void savePet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mName.getText().toString().trim();
        String breedString = mBreed.getText().toString().trim();
        String weightString = mWeight.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (currentPetsUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
                TextUtils.isEmpty(weightString) && mGenderValue == PetContract.PetEntry.mUnknown) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, nameString);
        values.put(PetContract.PetEntry.COLUMN_BREED_NAME, breedString);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, mGenderValue);
        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int weight = 0;
        if (!TextUtils.isEmpty(weightString)) {
            weight = Integer.parseInt(weightString);
        }
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight);

        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (currentPetsUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(PetContract.PetEntry.COMPLETE_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving pet",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Pet is saved succesfully",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(currentPetsUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Update is Failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Updated",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
               savePet();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_BREED_NAME,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};
        return new CursorLoader(this,
                currentPetsUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
if(cursor==null||cursor.getCount()<1){
    return;
}
if(cursor.moveToFirst()){
    int nameIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
    int breedIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED_NAME);
    int genderIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
    int weightIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
    String currentName=cursor.getString(nameIndex);
    String currentBreed=cursor.getString(breedIndex);
    int currentGender=cursor.getInt(genderIndex);
    int currentWeight=cursor.getInt(weightIndex);
    mName.setText(currentName);
    mBreed.setText(currentBreed);
    mWeight.setText(currentWeight);
    switch (currentGender){
        case PetContract.PetEntry.mMale:
            mGender.setSelection(1);
            break;
        case PetContract.PetEntry.mUnknown:
            mGender.setSelection(0);
            break;
        case PetContract.PetEntry.mFemale:
            mGender.setSelection(2);
            break;

    }
}
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mBreed.setText("");
        mWeight.setText("");
        mGender.setSelection(0);
    }
}

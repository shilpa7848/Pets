package sample.pets.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import sample.pets.R;
import sample.pets.data.PetContract;

/**
 * Created by silpa on 18/7/17.
 */

public class PetCursorAdapter extends CursorAdapter {
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView petName=(TextView) view.findViewById(R.id.petname);
        TextView breedName=(TextView) view.findViewById(R.id.breedname);
        int petIndex= cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_BREED_NAME);
        String pet=cursor.getString(petIndex);
        String breed=cursor.getString(breedIndex);
        petName.setText(pet);
        breedName.setText(breed);
    }
}

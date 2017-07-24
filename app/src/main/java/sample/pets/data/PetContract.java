package sample.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by silpa on 11/7/17.
 */

public final class PetContract {

    public static final String CONTENT_AUTHORITY="sample.pets";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_PETS="pets";

    public PetContract() {
    }

    public static abstract class PetEntry implements BaseColumns{
        public static final String CONTENT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public static final Uri COMPLETE_URI= Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
        public static final String TABLE_NAME="pets";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_BREED_NAME="breed";
        public static final int mUnknown=0;
        public static final int mMale=1;
        public static final int mFemale=2;
        public static final String COLUMN_PET_NAME="name";
        public static final String COLUMN_PET_WEIGHT="weight";
        public static final String COLUMN_PET_GENDER="gender";

    }
}

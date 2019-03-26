package e.azzam.petsapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PetContract {

    public static final String CONTENT_AUTHORITY ="e.azzam.petsapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PETS_PATH = "pets"; //nama tabel database


    public abstract static class PetEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PETS_PATH);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PETS_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PETS_PATH;


        public static final String TABLE_NAME = "pets";
        public static final String ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                                                  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                  COLUMN_PET_NAME + " TEXT NOT NULL, " +
                                                  COLUMN_PET_BREED + " TEXT," +
                                                  COLUMN_PET_GENDER + " INTEGER NOT NULL DEFAULT " +
                                                  GENDER_UNKNOWN + ", " +
                                                  COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0" +
                                                   ");";

        public static boolean isValidGender(Integer gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE){
                return true;
            }
            return false;
        }



    }
}

package SQLDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class photoFrameDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "photoFrame.DB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "PhotoFrame";
    public static final String COLUMN_ID = "pId";
    public static final String COLUMN_PHOTO_FRAME_STYLE_NAME = "styleName";
    public static final String COLUMN_RESOURCE = "resource";

    private static final String CREATE_PHOTO_FRAME_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PHOTO_FRAME_STYLE_NAME + " TEXT NOT NULL, " +
                    COLUMN_RESOURCE + " TEXT NOT NULL)" ;

    public photoFrameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PHOTO_FRAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

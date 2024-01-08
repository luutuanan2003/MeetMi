package SQLDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ModelClass.PhotoFrame;

public class photoFrameDatabaseManager {

    private photoFrameDatabaseHelper pFhelper;
    private Context context;
    private SQLiteDatabase database;

    public photoFrameDatabaseManager (Context c)
    {
        context = c;
    }

    public photoFrameDatabaseManager open() throws SQLException
    {
        pFhelper = new photoFrameDatabaseHelper(context);
        database = pFhelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        if (pFhelper != null) {
            pFhelper.close();
        }
    }

    public void insertPhotoFrameStyle(PhotoFrame pf) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(photoFrameDatabaseHelper.COLUMN_PHOTO_FRAME_STYLE_NAME, pf.getStyleName());
        contentValue.put(photoFrameDatabaseHelper.COLUMN_RESOURCE, pf.getResource());

        long result = database.insert(photoFrameDatabaseHelper.TABLE_NAME, null, contentValue);
    }
}

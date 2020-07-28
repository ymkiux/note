package wooyun.note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import wooyun.note.model.Write;


public class DbManager {
    private Context context;
    private DbOpenHelper databaseOpenHelper;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    private static DbManager instance;

    public DbManager(Context context) {
        this.context = context;
        databaseOpenHelper = new DbOpenHelper(context);
        dbReader = databaseOpenHelper.getReadableDatabase();
        dbWriter = databaseOpenHelper.getWritableDatabase();
    }

    public static synchronized DbManager getInstance(Context context) {
        if (instance == null) {
            instance = new DbManager(context);
        }
        return instance;
    }

    // 添加到数据库
    public void addToDB(String title, String content, String time) {
        //  组装数据
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.TITLE, title);
        cv.put(DbOpenHelper.CONTENT, content);
        cv.put(DbOpenHelper.TIME, time);
        dbWriter.insert(DbOpenHelper.TABLE_NAME, null, cv);
    }

    //  读取数据
    public void readFromDB(List<Write> noteList) {
        Cursor cursor = dbReader.query(DbOpenHelper.TABLE_NAME, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                Write note = new Write();
                note.setId(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DbOpenHelper.TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(DbOpenHelper.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(DbOpenHelper.TIME)));
                noteList.add(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  更新数据
    public void updateNote(int noteID, String title, String content, String time) {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.ID, noteID);
        cv.put(DbOpenHelper.TITLE, title);
        cv.put(DbOpenHelper.CONTENT, content);
        cv.put(DbOpenHelper.TIME, time);
        dbWriter.update(DbOpenHelper.TABLE_NAME, cv, "_id = ?", new String[]{noteID + ""});
    }

    //  删除数据
    public void deleteNote(int noteID) {
        dbWriter.delete(DbOpenHelper.TABLE_NAME, "_id = ?", new String[]{noteID + ""});
    }

    // 根据id查询数据
    public Write readData(int noteID) {
        Cursor cursor = dbReader.rawQuery("SELECT * FROM note WHERE _id = ?", new String[]{noteID + ""});
        cursor.moveToFirst();
        Write note = new Write();
        note.setId(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(DbOpenHelper.TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(DbOpenHelper.CONTENT)));
        return note;
    }
}



package gov.moandor.notepad.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.util.GlobalContext;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NAME = "notepad.db";
    private static final int VERSION = 2;
    
    private static final String CREATE_ARTICLE_TABLE = "create table " + Table.Article.TABLE_NAME + "("
            + Table.Article.ID + " integer primary key, " + Table.Article.TITLE + " text, " + Table.Article.TEXT
            + " text, " + Table.Article.LAST_MODIFIED + " integer)";
    
    private static DatabaseHelper sInstance;
    
    private DatabaseHelper() {
        super(GlobalContext.getInstance(), NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
        case 1:
            Cursor cursor = db.rawQuery("select * from " + Table.Article.TABLE_NAME, null);
            List<Article> articles = DatabaseUtils.getArticles(cursor);
            cursor.close();
            for (Article article : articles) {
                article.lastModified = System.currentTimeMillis();
            }
            db.execSQL("drop table " + Table.Article.TABLE_NAME);
            db.execSQL(CREATE_ARTICLE_TABLE);
            for (Article article : articles) {
                ContentValues values = new ContentValues();
                values.put(Table.Article.ID, article.id);
                values.put(Table.Article.TITLE, article.title);
                values.put(Table.Article.TEXT, article.text);
                values.put(Table.Article.LAST_MODIFIED, article.lastModified);
                db.insert(Table.Article.TABLE_NAME, null, values);
            }
        }
    }
    
    public static DatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DatabaseHelper();
        }
        return sInstance;
    }
}

package gov.moandor.notepad.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import gov.moandor.notepad.BuildConfig;
import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import gov.moandor.notepad.util.Logger;

public class DatabaseUtils {
    private static DatabaseHelper sHelper;
    
    private static DatabaseHelper getHelper() {
        if (sHelper == null) {
            sHelper = DatabaseHelper.getInstance();
        }
        return sHelper;
    }
    
    public static List<Article> getAllArticles() {
        synchronized (getHelper()) {
            SQLiteDatabase database = getHelper().getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from " + Table.Article.TABLE_NAME, null);
            List<Article> result = getArticles(cursor);
            cursor.close();
            database.close();
            return result;
        }
    }
    
    public static void insertOrUpdateArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(Table.Article.TITLE, article.title);
        values.put(Table.Article.TEXT, article.text);
        values.put(Table.Article.LAST_MODIFIED, article.lastModified);
        synchronized (getHelper()) {
            SQLiteDatabase database = getHelper().getReadableDatabase();
            Cursor cursor =
                    database.query(Table.Article.TABLE_NAME, null, Table.Article.ID + "=" + article.id, null, null,
                            null, null);
            if (cursor != null && cursor.getCount() > 0) {
                database.close();
                database = getHelper().getWritableDatabase();
                database.update(Table.Article.TABLE_NAME, values, Table.Article.ID + "=" + article.id, null);
            } else {
                database.close();
                database = getHelper().getWritableDatabase();
                article.id = database.insert(Table.Article.TABLE_NAME, null, values);
            }
            cursor.close();
            database.close();
        }
    }
    
    public static void deleteArticle(Article article) {
        synchronized (getHelper()) {
            SQLiteDatabase database = getHelper().getWritableDatabase();
            database.delete(Table.Article.TABLE_NAME, Table.Article.ID + "=" + article.id, null);
        }
    }
    
    public static void editArticleTitles(long[] ids, String title) {
        synchronized (getHelper()) {
            SQLiteDatabase database = getHelper().getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Table.Article.TITLE, title);
            for (long id : ids) {
                database.update(Table.Article.TABLE_NAME, values, Table.Article.ID + "=" + id, null);
            }
        }
    }
    
    static List<Article> getArticles(Cursor cursor) {
        List<Article> result = new ArrayList<Article>();
        while (cursor.moveToNext()) {
            Article article = new Article();
            try {
                article.id = cursor.getLong(cursor.getColumnIndex(Table.Article.ID));
                article.title = cursor.getString(cursor.getColumnIndex(Table.Article.TITLE));
                article.text = cursor.getString(cursor.getColumnIndex(Table.Article.TEXT));
                article.lastModified = cursor.getLong(cursor.getColumnIndex(Table.Article.LAST_MODIFIED));
            } catch (IllegalStateException e) {
                Logger.logExcpetion(e);
            }
            result.add(article);
        }
        return Utilities.sortByTime(result);
    }
}

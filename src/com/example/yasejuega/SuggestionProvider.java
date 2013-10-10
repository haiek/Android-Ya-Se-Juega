//NOT USING IT, DELETE!

package com.example.yasejuega;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class SuggestionProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.appsrox.forexwiz.provider/symbol");
     
    private static final int ALL = 1;
    private static final int ROW_ID = 2;
    private static final int SEARCH = 3;
     
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(CONTENT_URI.getAuthority(), "symbol", ALL);
        matcher.addURI(CONTENT_URI.getAuthority(), "symbol/#", ROW_ID);
        matcher.addURI(CONTENT_URI.getAuthority(), SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
        matcher.addURI(CONTENT_URI.getAuthority(), SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
    }
     
    private SQLiteDatabase db;
 
    @Override
    public boolean onCreate() {
        //DbHelper dbHelper = new DbHelper(getContext());
        //db = dbHelper.getReadableDatabase();
        return true;
    }
     
    private static final HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();
    static {
        PROJECTION_MAP.put("_id", "_id");
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, 
                            "code AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_2, 
                            "country || ' ' || name AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, 
                            "_id AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
    }
 
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        //builder.setTables(Symbol.TABLE_NAME);
         
        switch(matcher.match(uri)) {
        case SEARCH:
            String query = uri.getLastPathSegment();
            builder.appendWhere("isTracked = 0 AND (");
            builder.appendWhere("name LIKE '" + query + "%' OR ");
            builder.appendWhere("country LIKE '" + query + "%' OR ");
            builder.appendWhere("code LIKE '" + query + "%')");
            builder.setProjectionMap(PROJECTION_MAP);
            break;
        }
        return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }
 
    @Override
    public String getType(Uri uri) {
        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
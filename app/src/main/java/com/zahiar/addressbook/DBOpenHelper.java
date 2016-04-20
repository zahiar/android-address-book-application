package com.zahiar.addressbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class extends class SQLiteOpenHelper, which handles the creation of a
 * SQLite database.
 * 
 * @author Zahiar Ahmed
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	public final static String TABLE_NAME = "addressBook";

	/**
	 * Default constructor which initialises the SQLiteOpenHelper, which is used
	 * to help manage the database.
	 * 
	 * @param context To use to open or create the database
	 * @param name Of the database file, or null for an in-memory database
	 * @param factory To use for creating cursor objects, or null for the default
	 * @param version Number of the database (starting at 1)
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * This method creates the table with the necessary fields to store our
	 * data.
	 * 
	 * Table: addressBook Fields: {id, name, number, email}.
	 * 
	 * @param db The database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name VARCHAR(128) NOT NULL,"
				+ "number VARCHAR(11) NOT NULL,"
				+ "email VARCHAR(128) NOT NULL" + ");");
	}

	/**
	 * This overrides the onUpgrade method, however we do not require its
	 * functionality, therefore this has not been implemented.
	 * 
	 * @param db The database
	 * @param oldVersion The old database version
	 * @param newVersion The new database version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not implemented
	}
}

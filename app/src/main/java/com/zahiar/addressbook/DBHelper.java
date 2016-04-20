package com.zahiar.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class provides the CRUD operations to the database.
 * 
 * @author Zahiar Ahmed
 */
public class DBHelper {

	private DBOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor dbRecords;

	/**
	 * Default constructor which initialises the database.
	 * 
	 * @param context To use to open or create the database.
	 */
	public DBHelper(Context context) {
		dbOpenHelper = new DBOpenHelper(context, DBOpenHelper.TABLE_NAME, null,
				1);
	}

	/**
	 * Opens a handle to a writable database if one does not already exist.
	 */
	public void open() {
		if (db == null) {
			db = dbOpenHelper.getWritableDatabase();
		}
	}

	/**
	 * Closes the handle to the database, if one already exists.
	 */
	public void close() {
		if (db != null) {
			db.close();
		}
	}

	/**
	 * Convenience method which opens a handle to a writable database and
	 * retrieves all the data stored within it.
	 */
	public void initialise() {
		open();
		getDBEntries();
	}

	/**
	 * Retrieves all the entries in the DB and stores them in the variable
	 * dbRecords.
	 */
	public void getDBEntries() {
		dbRecords = db.query(DBOpenHelper.TABLE_NAME, new String[] { "id",
				"name", "number", "email" }, null, null, null, null, null);
	}

	/**
	 * Updates variable dbRecords, by requerying the database to get the latest
	 * data and also keeps track of the current cursor position.
	 */
	public void refreshDBEntries() {
		int currentCursorPosition = dbRecords.getPosition();
		getDBEntries();

		dbRecords.move(currentCursorPosition);
	}

	/**
	 * Retrieves the current record's data.
	 * 
	 * @return String array in the following format {name, number, email}
	 */
	private String[] getCurrentEntry() {
		String[] entry = new String[3];

		entry[0] = dbRecords.getString(1);
		entry[1] = dbRecords.getString(2);
		entry[2] = dbRecords.getString(3);

		return entry;
	}

	/**
	 * Retrieves the first record's data.
	 * 
	 * @return String array in the following format {name, number, email}
	 */
	public String[] getFirstEntry() {
		if (dbRecords.moveToFirst()) {
			return getCurrentEntry();
		}

		return null;
	}

	/**
	 * Retrieves the last record's data.
	 * 
	 * @return String array in the following format {name, number, email}
	 */
	public String[] getLastEntry() {
		if (dbRecords.moveToLast()) {
			return getCurrentEntry();
		}

		return null;
	}

	/**
	 * Retrieves the next record's data.
	 * 
	 * @return String array in the following format {name, number, email}
	 */
	public String[] getNextEntry() {
		if (dbRecords.moveToNext()) {
			return getCurrentEntry();
		} else {
			/**
			 * This sets the cursor to the last record, which prevents the issue
			 * of the user clicking the "Previous" button twice, in order to see
			 * the previous contact.
			 */
			dbRecords.moveToLast();
		}

		return null;
	}

	/**
	 * Retrieves the previous record's data.
	 * 
	 * @return String array in the following format {name, number, email}
	 */
	public String[] getPreviousEntry() {
		if (dbRecords.moveToPrevious()) {
			return getCurrentEntry();
		} else {
			/**
			 * This sets the cursor to the first record, which prevents the
			 * issue of the user clicking the "Next" button twice, in order to
			 * see the next contact.
			 */
			dbRecords.moveToFirst();
		}

		return null;
	}

	/**
	 * Deletes the current record, and if successful, requery's the database to
	 * retrieve the updated data.
	 * 
	 * @return Returns true if successful, otherwise false
	 */
	public Boolean deleteCurrentEntry() {
		int cursorId;

		try {
			cursorId = dbRecords.getInt(0);
		} catch (Exception e) {
			return false;
		}

		int deleteRecord = db.delete(DBOpenHelper.TABLE_NAME, "id = "
				+ cursorId, null);

		if (deleteRecord > 0) {
			refreshDBEntries();

			return true;
		}

		return false;
	}

	/**
	 * Inserts a new record into the database.
	 * 
	 * @param name Person's name
	 * @param number Person's number
	 * @param email Person's email address
	 *
	 * @return Returns true if successful, otherwise false
	 */
	public Boolean addNewEntry(String name, String number, String email) {
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("number", number);
		cv.put("email", email);

		long insertRecord = db.insert(DBOpenHelper.TABLE_NAME, null, cv);

		return (insertRecord != -1);
	}

}

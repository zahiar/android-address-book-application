package com.zahiar.addressbook;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * This is the main activity which allows the user to browse through their
 * address book.
 * 
 * @author Zahiar Ahmed
 */
public class MainActivity extends Activity {

	public static final int ACTIVITY_ADD_ENTRY = 1;

	private DBHelper dbHelper;
	private TextView nameTextView;
	private TextView numberTextView;
	private TextView emailTextView;

	/**
	 * This method initialises the database, sets up the text views and displays
	 * the first entry.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbHelper = new DBHelper(this);
		dbHelper.initialise();

		nameTextView = (TextView) findViewById(R.id.textViewName);
		numberTextView = (TextView) findViewById(R.id.textViewNumber);
		emailTextView = (TextView) findViewById(R.id.textViewEmail);

		showFirstEntry();
	}

	/**
	 * This method closes the database handle when the application is about to
	 * be destroyed.
	 */
	@Override
	protected void onDestroy() {
		dbHelper.close();

		super.onDestroy();
	}

	/**
	 * Updates the screen by displaying the new data provided.
	 * 
	 * @param entry String array containing the following data {name, number, email}
	 */
	public void updateContactOnScreen(String[] entry) {
		nameTextView.setText(entry[0]);
		numberTextView.setText(entry[1]);
		emailTextView.setText(entry[2]);
	}

	/**
	 * Retrieve's the first entry from the database and displays it onscreen.
	 */
	public void showFirstEntry() {
		String[] entry = dbHelper.getFirstEntry();

		if (entry != null) {
			updateContactOnScreen(entry);
		}
	}

	/**
	 * Retrieve's the last entry from the database and displays it onscreen.
	 */
	public void showLastEntry() {
		String[] entry = dbHelper.getLastEntry();

		if (entry != null) {
			updateContactOnScreen(entry);
		}
	}

	/**
	 * Retrieve's the previous entry from the database and displays it onscreen.
	 * If unsuccessful, a message is displayed onscreen.
	 */
	public void showPreviousEntry(View v) {
		String[] entry = dbHelper.getPreviousEntry();

		if (entry != null) {
			updateContactOnScreen(entry);
		} else {
			Toast.makeText(this, "No more entries to show", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Retrieve's the next entry from the database and displays it onscreen. If
	 * unsuccessful, a message is displayed onscreen.
	 */
	public void showNextEntry(View v) {
		String[] entry = dbHelper.getNextEntry();

		if (entry != null) {
			updateContactOnScreen(entry);
		} else {
			Toast.makeText(this, "No more entries to show", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Handle's the delete button event by deleting the currently displayed
	 * contact. If successful, the entry is deleted and the next available
	 * contact is shown, if no contacts are available then an empty contact is
	 * shown instead. If unsuccessful, then a status message is shown.
	 * 
	 * @param v The view
	 */
	public void deleteCurrentEntry(View v) {
		boolean entryDeleted = dbHelper.deleteCurrentEntry();

		if (entryDeleted) {
			String[] entry = dbHelper.getNextEntry();

			if (entry != null) {
				updateContactOnScreen(entry);
			} else {
				entry = dbHelper.getPreviousEntry();

				if (entry != null) {
					updateContactOnScreen(entry);
				} else {
					entry = new String[] { " ", " ", " " };

					updateContactOnScreen(entry);
				}
			}
		} else {
			Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Handle's the add button event, by starting the AddEntry activity.
	 * 
	 * @param v The view
	 */
	public void addNewEntry(View v) {
		Intent myIntent = new Intent(this, AddEntryActivity.class);
		startActivityForResult(myIntent, ACTIVITY_ADD_ENTRY);
	}

	/**
	 * Handle's the result returned by a child activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_ADD_ENTRY) {
			if (resultCode == RESULT_OK) {
				dbHelper.refreshDBEntries();

				showLastEntry();
			}
		}
	}

}

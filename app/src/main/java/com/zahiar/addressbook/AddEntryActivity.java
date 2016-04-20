package com.zahiar.addressbook;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;

/**
 * This is activity allows the user to add a new entry.
 * 
 * @author Zahiar Ahmed
 */
public class AddEntryActivity extends Activity {

	private DBHelper dbHelper;
	private EditText nameEntry;
	private EditText numberEntry;
	private EditText emailEntry;

	/**
	 * This method initialises the database and sets up the entry fields.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_entry);

		dbHelper = new DBHelper(this);
		dbHelper.initialise();

		nameEntry = (EditText) findViewById(R.id.editTextName);
		numberEntry = (EditText) findViewById(R.id.editTextNumber);
		emailEntry = (EditText) findViewById(R.id.editTextEmail);
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
	 * Checks if the string is empty.
	 * 
	 * @param input String to be validated
	 * 
	 * @return Returns true if it is empty, otherwise false.
	 */
	private boolean isStringEmpty(String input) {
		if (input.trim().isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * Validates whether the email address is valid or not.
	 * 
	 * @param email The email address inputted by the user.
	 * 
	 * @return Returns true if it is valid, otherwise false.
	 */
	private boolean isEmailValid(String email) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		return emailPattern.matcher(email).matches();
	}

	/**
	 * Handle's the add button event by inserting the user's entry into the
	 * database, provided that it is valid. If successfully stored, a success
	 * message is shown and the activity is closed returning a successful
	 * result.
	 * 
	 * If it was unsuccessful, then an error message is shown.
	 * 
	 * @param v The view
	 */
	public void addEntry(View v) {
		String name = nameEntry.getText().toString();
		String number = numberEntry.getText().toString();
		String email = emailEntry.getText().toString();

		if (isStringEmpty(name)) {
			Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (isStringEmpty(number) && isStringEmpty(email)) {
			Toast.makeText(this,
					"You must enter either a number or an email address",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!isStringEmpty(email) && !isEmailValid(email)) {
			Toast.makeText(this, "Email address is invalid", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Boolean newEntryAdded = dbHelper.addNewEntry(name, number, email);

		if (newEntryAdded) {
			Toast.makeText(this, "Entry added", Toast.LENGTH_SHORT).show();

			setResult(RESULT_OK);
			finish();
		} else {
			Toast.makeText(this, "Something went wrong, please try again.",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Handle's the cancel button event by closing the activity and returning a
	 * cancelled result.
	 * 
	 * @param v The view
	 */
	public void cancelEntry(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
}

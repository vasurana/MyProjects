package com.example.beacon1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class UserDatabase {

	private static final String TAG = UserDatabase.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	public static final String KEY_ROWID = "_id";
	private DbHelper ourHelper;
	private SQLiteDatabase ourDatabase;
	private final Context ourContext;
	
	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHONENUMBER = "phonenumber";

	public class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT,"
					+ KEY_PHONENUMBER + " TEXT NOT NULL )";
			db.execSQL(CREATE_LOGIN_TABLE);

			Log.d(TAG, "Database tables created");
		}

		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

			// Create tables again
			onCreate(db);
		}

	}

	/**
	 * Storing user details in database
	 * */
	public UserDatabase(Context c) {
		ourContext = c;
	}

	public long addUser(String name, String email, String number) {

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_PHONENUMBER, number); // Phone Number

		// Inserting Row
		return ourDatabase.insert(TABLE_USER, null, values);
		
	}

	/**
	 * Getting user data from database
	 * */
	public String getUserDetails() {
		String user;
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		// SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = ourDatabase.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		user = cursor.getString(1);

		cursor.close();
		ourDatabase.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Recreate database Delete all tables and create them again
	 * */
	/*public void deleteUsers() {
		// SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		ourDatabase.delete(TABLE_USER, null, null);
		ourDatabase.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}*/

	public void updateEntry(String name, String email, String number) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_NAME, name);
		cvUpdate.put(KEY_EMAIL, email);
		cvUpdate.put(KEY_PHONENUMBER, number);
		ourDatabase.update(TABLE_USER, cvUpdate, KEY_ROWID + "=" + 1, null);

	}

	public void close() {
		// TODO Auto-generated method stub
		ourHelper.close();
	}

	public UserDatabase open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

}

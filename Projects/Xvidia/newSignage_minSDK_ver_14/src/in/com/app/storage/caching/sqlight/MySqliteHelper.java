package in.com.app.storage.caching.sqlight;

import in.com.app.AppMain;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *  This is a SQLite Helper class
 * @author Ravi@Xvidia Technologies
 * @version 1.0
  *
 */
public class MySqliteHelper extends SQLiteOpenHelper {

	// public static variables for caching image table
	public static final String TABLE_NAME_SETTING = "SETTING_TABLE";
	public static final String TABLE_NAME_MEDIA_TABLE = "MEDIA_INFO_TABLE";
	public static final String TABLE_NAME_MEDIA_TIME_TABLE = "MEDIA_TIME_TABLE";
	public static final String TABLE_NAME_LAYOUT_TIME_TABLE = "MEDIA_LAYOUT_TABLE";
	public static final String TABLE_NAME_ONOFF_TIME_SCREEN_TABLE = "ONOFF_SCREEN_TABLE";
	public static final String TABLE_NAME_ONOFF_TIME_APP_TABLE = "ONOFF_APP_TABLE";
	public static final String TABLE_NAME_ONOFF_TIME_BOX_TABLE = "ONOFF_BOX_TABLE";
	public static final String TABLE_NAME_RESOURCE_TABLE = "RESOURCE_TABLE";
	public static final String COLUMN_CACHING_API_ID = "apiCachingId";
	public static final String COLUMN_CACHING_DATA = "cachingData";
	public static final String COLUMN_MEDIA_NAME = "mediaName";
	public static final String COLUMN_MEDIA_DATE = "mediaDate";
	public static final String COLUMN_MEDIA_DOWNLOAD_COUNT = "downloadCount";
	public static final String COLUMN_MEDIA_TIME_ID = "mediaTimeId";
	public static final String COLUMN_MEDIA_TIME_BOXID = "mediaTimeBoxId";
	public static final String COLUMN_MEDIA_TIME_LAYOUT_ID = "mediaTimeLayoutId";
	public static final String COLUMN_MEDIA_TIME_MEDIA_ID = "mediaTimeMediaId";
	public static final String COLUMN_MEDIA_TIME_START_TIME = "mediaTimeStartTime";
	public static final String COLUMN_MEDIA_TIME_END_TIME = "mediaTimeEndTime";
	public static final String COLUMN_MEDIA_TIME_DURATION = "mediaTimeDuration";

	public static final String COLUMN_LAYOUT_TIME_ID = "layoutTimeId";
	public static final String COLUMN_LAYOUT_TIME_BOXID = "layoutTimeBoxId";
	public static final String COLUMN_LAYOUT_TIME_LAYOUT_ID = "layoutTimeLayoutId";
	public static final String COLUMN_LAYOUT_TIME_START_TIME = "layoutTimeStartTime";
	public static final String COLUMN_LAYOUT_TIME_END_TIME = "layoutTimeEndTime";

	public static final String COLUMN_ONOFF_TIME_ID = "onOffTimeId";
	public static final String COLUMN_ONOFF_TIME_BOXID = "lonOffTimeBoxId";
	public static final String COLUMN_ONOFF_TIME_ON = "onOffTimeOnTime";
	public static final String COLUMN_ONOFF_TIME_OFF= "onOffTimeOffTime";
	public static final String COLUMN_ONOFF_TIME_SAVING= "onOffTimeSaving";
	
	private static final String DATABASE_NAME = "xibosql.db";
	private static final int DATABASE_VERSION =3;

	/* Caching table create sql statement */

	private static final String TABLE_CREATE_CACHING = "create table "
			+ TABLE_NAME_SETTING + "(" + COLUMN_CACHING_API_ID
			+ " integer primary key autoincrement, " + COLUMN_CACHING_DATA
			+ " text );";
	
	private static final String TABLE_CREATE_MEDIA_INFO = "create table "
			+ TABLE_NAME_MEDIA_TABLE + "(" + COLUMN_MEDIA_NAME
			+ " text primary key, " + COLUMN_MEDIA_DOWNLOAD_COUNT
			+ " text, " + COLUMN_MEDIA_DATE
			+ " integer);";
	private static final String TABLE_CREATE_MEDIA_TIME = "create table "
			+ TABLE_NAME_MEDIA_TIME_TABLE + "(" + COLUMN_MEDIA_TIME_ID
			+ " text primary key, " 
			+ COLUMN_MEDIA_TIME_BOXID+ " text, " 
			+ COLUMN_MEDIA_TIME_LAYOUT_ID+ " text, " 
			+ COLUMN_MEDIA_TIME_MEDIA_ID+ " text, " 
			+ COLUMN_MEDIA_TIME_START_TIME+ " text, " 
			+ COLUMN_MEDIA_TIME_END_TIME+ " text, "  
			+ COLUMN_MEDIA_TIME_DURATION+ " text);";
	private static final String TABLE_CREATE_LAYOUT_TIME = "create table "
			+ TABLE_NAME_LAYOUT_TIME_TABLE + "(" + COLUMN_LAYOUT_TIME_ID
			+ " text primary key, " 
			+ COLUMN_LAYOUT_TIME_BOXID+ " text, " 
			+ COLUMN_LAYOUT_TIME_LAYOUT_ID+ " text, " 
			+ COLUMN_LAYOUT_TIME_START_TIME+ " text, " 
			+ COLUMN_LAYOUT_TIME_END_TIME+ " text);";
	
	private static final String TABLE_CREATE_ONOFF_SCREEN_TIME = "create table "
			+ TABLE_NAME_ONOFF_TIME_SCREEN_TABLE + "(" + COLUMN_ONOFF_TIME_ID
			+ " text primary key, " 
			+ COLUMN_ONOFF_TIME_BOXID+ " text, "  
					+ COLUMN_ONOFF_TIME_SAVING+ " text, "  
			+ COLUMN_ONOFF_TIME_ON+ " text, " 
			+ COLUMN_ONOFF_TIME_OFF+ " text);";
	private static final String TABLE_CREATE_ONOFF_APP_TIME = "create table "
			+ TABLE_NAME_ONOFF_TIME_APP_TABLE + "(" + COLUMN_ONOFF_TIME_ID
			+ " text primary key, " 
			+ COLUMN_ONOFF_TIME_BOXID+ " text, " 
			+ COLUMN_ONOFF_TIME_SAVING+ " text, "  
			+ COLUMN_ONOFF_TIME_ON+ " text, " 
			+ COLUMN_ONOFF_TIME_OFF+ " text);";
	private static final String TABLE_CREATE_ONOFF_BOX_TIME = "create table "
			+ TABLE_NAME_ONOFF_TIME_BOX_TABLE+ "(" + COLUMN_ONOFF_TIME_ID
			+ " text primary key, " 
			+ COLUMN_ONOFF_TIME_BOXID+ " text, " 
			+ COLUMN_ONOFF_TIME_SAVING+ " text, "  
			+ COLUMN_ONOFF_TIME_ON+ " text, " 
			+ COLUMN_ONOFF_TIME_OFF+ " text);";
	private static final String TABLE_CREATE_RESOURCE = "create table "
			+ TABLE_NAME_RESOURCE_TABLE + "(" + COLUMN_CACHING_API_ID
			+ " text primary key , " + COLUMN_CACHING_DATA
			+ " blob );";
	
	

	private static MySqliteHelper INSTANCE = null;// = new MySqliteHelper(AppMain.getAppMainContext());

	public static MySqliteHelper getInstance(Context ctx) {
		if (INSTANCE == null)
			INSTANCE = new MySqliteHelper(ctx);
		return INSTANCE;
	}

	private MySqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CREATE_CACHING);
		database.execSQL(TABLE_CREATE_RESOURCE);
		database.execSQL(TABLE_CREATE_MEDIA_INFO);
		database.execSQL(TABLE_CREATE_MEDIA_TIME);
		database.execSQL(TABLE_CREATE_LAYOUT_TIME);
		database.execSQL(TABLE_CREATE_ONOFF_SCREEN_TIME);
		database.execSQL(TABLE_CREATE_ONOFF_BOX_TIME);
		database.execSQL(TABLE_CREATE_ONOFF_APP_TIME);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.w(MySqliteHelper.class.getName(),
//				"Upgrading database from version " + oldVersion + " to "
//						+ newVersion + ", which will destroy all old data");
		try{
			switch(oldVersion) {
		   case 1:
			   db.execSQL(TABLE_CREATE_MEDIA_INFO);
		   case 2:
			   db.execSQL("ALTER TABLE "+TABLE_NAME_MEDIA_TABLE+ " ADD COLUMN "+COLUMN_MEDIA_DATE+" TEXT");
		   case 3:
			   db.execSQL(TABLE_CREATE_MEDIA_TIME);  
			   db.execSQL(TABLE_CREATE_LAYOUT_TIME);
				db.execSQL(TABLE_CREATE_ONOFF_SCREEN_TIME);
				db.execSQL(TABLE_CREATE_ONOFF_BOX_TIME);
				db.execSQL(TABLE_CREATE_ONOFF_APP_TIME);
		   }
		} catch (SQLException e) {
//			e.printStackTrace();
		}
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_CACHING);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_RESOURCE);
//		onCreate(db);
		
	}
}

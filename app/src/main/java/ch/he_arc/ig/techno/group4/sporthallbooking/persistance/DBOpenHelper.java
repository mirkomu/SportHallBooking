package ch.he_arc.ig.techno.group4.sporthallbooking.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    // @goals This class aims to show the constant to use for the DBOpenHelper */
    public static class Constants implements BaseColumns {
        // The database name
        public static final String DATABASE_NAME = "localDB.db";

        // The database version
        public static final int DATABASE_VERSION = 1;

        // The table Name
        public static final String MY_TABLE = "User";

        // Noms de colonnes
        // /!\Si vous utilisez une base de données, les noms des colonnes ont
        // les mêmes que ceux de
        // votre base, de même pour les index.
        // My Column ID and the associated explanation for end-users
        public static final String KEY_COL_ID = "_id";// Mandatory

        // My Column Name and the associated explanation for end-users
        public static final String KEY_COL_LASTNAME = "lastName";

        // My Column First Name and the associated explanation for end-users
        public static final String KEY_COL_FIRSTNAME = "firstName";


        // Index des colonnes
        // The index of the column ID
        public static final int ID_COLUMN = 1;

        // The index of the column FIRST NAME
        public static final int FIRSTNAME_COLUMN = 2;

        // The index of the column LASTNAME
        public static final int LASTNAME_COLUMN = 3;

    }

    // The static string to create the database.
    private static final String DATABASE_CREATE = "create table "
            + Constants.MY_TABLE + "(" + Constants.KEY_COL_ID
            + " integer primary key autoincrement, "
            + " INTEGER, " + Constants.KEY_COL_FIRSTNAME + " TEXT, "
            + Constants.KEY_COL_LASTNAME + " TEXT)";


    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the new database using the SQL string Database_create
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        // Drop the old database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.MY_TABLE);
        // Create the new one
        onCreate(db);
        // or do a smartest stuff
    }
}




package dam.easypocket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class CollectionDBHelper extends SQLiteOpenHelper implements BaseColumns {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Pocket.db";
    private static final String COLLECTIONS_TABLE_NAME = "Collections";
    private static final String COLLECTIONS_COLUMN_OWNER = "owner";
    static final String COLLECTIONS_COLUMN_NAME = "name";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CollectionDBHelper.COLLECTIONS_TABLE_NAME + " (" +
                    CollectionDBHelper._ID + " INTEGER PRIMARY KEY," +
                    CollectionDBHelper.COLLECTIONS_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    CollectionDBHelper.COLLECTIONS_COLUMN_OWNER + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CollectionDBHelper.COLLECTIONS_TABLE_NAME;

    private SQLiteDatabase db;

    CollectionDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);

        // Gets the data repository in write mode
        db = this.getWritableDatabase();
    }

    SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Creates a new Collection
     * @param name name of the collection
     * @param owner owner of the collection
     * @param columnName String array with the names for the columns of the table
     * @param columnType String array with the type for the columns of the table
     * @return id of the new collection
     */
    long insertCollection (String name, String owner, String[] columnName, String[] columnType) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CollectionDBHelper.COLLECTIONS_COLUMN_NAME, name);
        values.put(CollectionDBHelper.COLLECTIONS_COLUMN_OWNER, owner);

        // Insert the new row, returning the primary key value of the new row

        long insertID = db.insert(CollectionDBHelper.COLLECTIONS_TABLE_NAME, null, values);

        // Creates table associated with the collection
        if(columnName.length != columnType.length){
            return -1;
        }
        String query = "CREATE TABLE IF NOT EXISTS " + name + "(";

        for(int i = 0; i < columnName.length; i++){
            if(i == 0){
                query += columnName[i] + " " + columnType[i];
            } else{
                query += ","+columnName[i] + " " + columnType[i];
            }

        }

        query += ")";

        db.execSQL(query);

        return insertID;
    }

    /**
     * Retrieves collections which name matches the given string
     * @param name pattern to search in collections names
     * @return cursor with the results of the search
     */
    Cursor getCollections(String name) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CollectionDBHelper._ID,
                CollectionDBHelper.COLLECTIONS_COLUMN_NAME,
                CollectionDBHelper.COLLECTIONS_COLUMN_OWNER
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = CollectionDBHelper.COLLECTIONS_COLUMN_NAME + " = ?";
        String[] selectionArgs = {
                name
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = CollectionDBHelper.COLLECTIONS_COLUMN_NAME + " DESC";

        return db.query(
                CollectionDBHelper.COLLECTIONS_TABLE_NAME,      // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                selectionArgs,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                       // The sort order
        );
    }

    /**
     * Updates the name of a collection
     * @param newName new name of the collection
     * @param oldName old name of the collection
     * @return int
     */
    int updateCollection(String newName, String oldName) {

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(CollectionDBHelper.COLLECTIONS_COLUMN_NAME, newName);

        // Which row to update, based on the title
        String selection = CollectionDBHelper.COLLECTIONS_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {
                oldName
        };

        int res = db.update(
                CollectionDBHelper.COLLECTIONS_TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        String query = "ALTER TABLE " + oldName + " RENAME TO " + newName;
        db.execSQL(query);

        return res;
    }

    /**
     * Removes a collection and its data
     * @param name name of the collection to remove
     * @return int
     */
    int deleteCollection (String name) {

        // Define 'where' part of query.
        String selection = CollectionDBHelper.COLLECTIONS_COLUMN_NAME + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {
                name
        };
        // Issue SQL statement.
        int res = db.delete(CollectionDBHelper.COLLECTIONS_TABLE_NAME, selection, selectionArgs);

        String query = "DROP TABLE IF EXISTS " + name;
        db.execSQL(query);

        return res;

    }

    /**
     * Inserts data into a collection
     * @param collectionName name of the collection to insert data
     * @param values String containing the value for each column
     * @return true if insertion was successful
     */
    boolean insertToCollection (String collectionName, String[] values) {

        if (values.length == 0){
            return false;
        }

        String valuesFormatted = "";

        for (String value : values) {
            if(valuesFormatted.equals("")){
                valuesFormatted = "'" + value + "'";
            } else{
                valuesFormatted += ", '" + value + "'";
            }
        }

        String query = "INSERT INTO " + collectionName + " VALUES(" + valuesFormatted + ")";
        db.execSQL(query);

        return true;
    }

    String getDataTypeColumn(String collectionName, String columnName){

        Cursor NameSearcher = db.rawQuery("select typeof (" + columnName + ") from " + collectionName, null);
        return NameSearcher.getString(0);
    }

}
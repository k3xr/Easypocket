package dam.easypocket;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(testCollectionsDB());
    }

    private boolean testCollectionsDB(){
        CollectionDBHelper db = new CollectionDBHelper(this.getApplicationContext());

        db.deleteCollection("testCollectionNew");

        String[] columns = new String[3];
        columns[0] = "column1";
        columns[1] = "column2";
        columns[2] = "column3";

        String[] columnTypes = new String[3];
        columnTypes[0] = "VARCHAR";
        columnTypes[1] = "INT";
        columnTypes[2] = "BOOLEAN";

        long insertID = db.insertCollection("testCollection", "testOwner", columns, columnTypes);

        boolean cond1 = insertID == 1;

        Cursor cursor = db.getCollections("testCollection");

        cursor.moveToFirst();
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionDBHelper._ID));
        String collectionName = cursor.getString(cursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));

        boolean cond2 = insertID == itemId;
        boolean cond3 = collectionName.equals("testCollection");

        db.updateCollection("testCollectionNew", "testCollection");

        cursor = db.getCollections("testCollectionNew");

        cursor.moveToFirst();
        collectionName = cursor.getString(cursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));

        boolean cond4 = collectionName.equals("testCollectionNew");

        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book1', 40, 1);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book2', 90, 1);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book3', 87090, 1);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book4', 0, 0);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book5', 1, 1);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book6', 498, 0);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book7', 4235, 1);");
        db.getDb().execSQL("INSERT INTO testCollectionNew VALUES('book8', 600, 1);");

        Cursor resultSet = db.getDb().rawQuery("Select * from testCollectionNew",null);
        resultSet.moveToFirst();

        TextView text = (TextView)findViewById(R.id.crearModifColeccion);

        for(int i = 0; i < resultSet.getCount(); i++){
            resultSet.moveToPosition(i);
            text.setText(text.getText()+"\n"+resultSet.getString(0)+": "+resultSet.getInt(1));
        }
        resultSet.close();

        return cond1 && cond2 && cond3 && cond4;
    }
}

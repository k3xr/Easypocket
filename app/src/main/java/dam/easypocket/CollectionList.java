package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectionList extends BaseActivity {

    private String currentCollectionSelected;

    private Button addElement;
    private Button editDesign;
    private Button explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_test);

        testCollectionsDB();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV);

        CollectionDBHelper db = new CollectionDBHelper(this.getApplicationContext());

        addElement = (Button)findViewById(R.id.buttonAddElement);
        addElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, AddItem.class);
                intent.putExtra("currentCollectionSelected",currentCollectionSelected);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        editDesign = (Button)findViewById(R.id.buttonEditarDisenio);
        editDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, EditCollection.class);
                intent.putExtra("currentCollectionSelected",currentCollectionSelected);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        explore = (Button)findViewById(R.id.buttonExplorar);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, SummaryVisual.class);
                intent.putExtra("currentCollectionSelected",currentCollectionSelected);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        Button addCollection = (Button)findViewById(R.id.buttonAddCollection);
        addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, EditCollection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from Collections", null)) {
            while (allCollectionsCursor.moveToNext()) {
                Button item = new Button(this);
                String collectionName = allCollectionsCursor.getString(allCollectionsCursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));
                item.setText(collectionName);
                ll.addView(item);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElement.setEnabled(true);
                        editDesign.setEnabled(true);
                        explore.setEnabled(true);
                        Button currentB = (Button) v;

                        LinearLayout lay = (LinearLayout) findViewById(R.id.linSV);

                        for (int i = 0; i < lay.getChildCount(); i++) {
                            View collection = lay.getChildAt(i);
                            if (collection instanceof Button) {
                                Button buttonCol = (Button) collection;
                                buttonCol.getBackground().clearColorFilter();
                            }
                        }
                        currentB.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        currentCollectionSelected = currentB.getText().toString();
                    }
                });
            }
        }
    }

    /**
     * Tests the CollectionDBHelper class methods
     * @return true if tests are ok
     */
    private boolean testCollectionsDB(){
        CollectionDBHelper db = new CollectionDBHelper(this.getApplicationContext());

        db.deleteCollection("books");
        db.deleteCollection("suitcase");

        String[] columns = new String[3];
        columns[0] = "column1";
        columns[1] = "column2";
        columns[2] = "column3";

        String[] columnTypes = new String[3];
        columnTypes[0] = "VARCHAR";
        columnTypes[1] = "INT";
        columnTypes[2] = "BOOLEAN";

        long insertID = db.insertCollection("booksOld", "testOwner", columns, columnTypes);

        boolean cond1 = insertID == 1;

        Cursor cursor = db.getCollections("booksOld");

        cursor.moveToFirst();
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CollectionDBHelper._ID));
        String collectionName = cursor.getString(cursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));

        boolean cond2 = insertID == itemId;
        boolean cond3 = collectionName.equals("booksOld");

        db.updateCollection("books", "booksOld");

        cursor = db.getCollections("books");

        cursor.moveToFirst();
        collectionName = cursor.getString(cursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));

        boolean cond4 = collectionName.equals("books");

        String[] values = {"book1", "40", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"book1", "540", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"book2", "6540", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"book3", "4350", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"book4", "40436", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"book5", "403", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"book6", "40654", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"book7", "440", "1"};
        db.insertToCollection("books", values);

        Cursor resultSet = db.getDb().rawQuery("Select * from books",null);
        resultSet.moveToFirst();

//        TextView text = (TextView)findViewById(R.id.misColecciones);

        for(int i = 0; i < resultSet.getCount(); i++){
            resultSet.moveToPosition(i);
//            text.setText(text.getText()+"\n"+resultSet.getString(0)+": "+resultSet.getInt(1));
        }
        resultSet.close();

        db.insertCollection("suitcase", "testOwner", columns, columnTypes);

        return cond1 && cond2 && cond3 && cond4;
    }
}

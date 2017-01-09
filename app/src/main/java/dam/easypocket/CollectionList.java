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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CollectionList extends BaseActivity {

    private String currentCollectionSelected;

    private Button addElement;
    private Button editDesign;
    private Button explore;

    private CollectionDBHelper db;

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

        db = new CollectionDBHelper(this.getApplicationContext());

        onResume();

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
                intent.putExtra("currentCollectionSelected", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV);
        ll.removeAllViews();
        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from Collections", null)) {
            while (allCollectionsCursor.moveToNext()) {
                Button item = new Button(this);
                String collectionName = allCollectionsCursor.getString(allCollectionsCursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));
                item.setText(collectionName);

                ImageButton delItem = new ImageButton(this);
                delItem.setImageResource(R.drawable.delete);
                delItem.setAdjustViewBounds(true);

                LinearLayout llh = new LinearLayout(this);
                llh.setOrientation(LinearLayout.HORIZONTAL);

                llh.addView(item);
                llh.addView(delItem);

                ViewGroup.LayoutParams params = item.getLayoutParams();
                params.width = 850;
                item.setLayoutParams(params);

                item.setLayoutParams(params);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                delItem.setLayoutParams(lp);

                ll.addView(llh);

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

                            for(int index = 0; index<((ViewGroup)collection).getChildCount(); ++index) {
                                View colButton = ((ViewGroup)collection).getChildAt(index);
                                if (colButton instanceof Button) {
                                    Button buttonCol = (Button) colButton;
                                    buttonCol.getBackground().clearColorFilter();
                                }
                            }
                        }
                        currentB.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                        currentCollectionSelected = currentB.getText().toString();
                    }
                });

                delItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout r = (LinearLayout)v.getParent();
                        for(int index = 0; index<(r).getChildCount(); ++index) {
                            View colButton = (r).getChildAt(index);
                            if (colButton instanceof Button) {
                                Button buttonCol = (Button) colButton;
                                db.deleteCollection(buttonCol.getText().toString());
                                onResume();
                                break;
                            }
                        }
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
        columns[0] = "title";
        columns[1] = "pages";
        columns[2] = "owned";

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

        String[] values = {"The Hunger Games", "40", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"Harry Potter", "540", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"Pride and Prejudice", "6540", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"Twilight", "4350", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"The Book Thief", "40436", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"Animal Farm", "403", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"The Da Vinci Code", "40654", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"Divergent", "440", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"Romeo and Juliet", "4890", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"The Alchemist", "4420", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"City of Bones", "440", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"Dracula", "460", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"The Secret Garden", "40", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"The Princess Bride", "940", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"The Stand", "9460", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"Dune", "96", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"A Game of Thrones", "966", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"The Odyssey", "660", "0"};
        db.insertToCollection("books", values);
        values = new String[]{"The Little Prince", "960", "1"};
        db.insertToCollection("books", values);
        values = new String[]{"The Lovely Bones", "10", "0"};
        db.insertToCollection("books", values);

//        Cursor resultSet = db.getDb().rawQuery("Select * from books",null);
//        resultSet.moveToFirst();
//
//        for(int i = 0; i < resultSet.getCount(); i++){
//            resultSet.moveToPosition(i);
//            System.out.println(resultSet.getString(0)+": "+resultSet.getInt(1));
//        }
//        resultSet.close();

        columns = new String[2];
        columns[0] = "item";
        columns[1] = "amount";

        columnTypes = new String[2];
        columnTypes[0] = "VARCHAR";
        columnTypes[1] = "INT";

        db.insertCollection("suitcase", "testOwner", columns, columnTypes);

        return cond1 && cond2 && cond3 && cond4;
    }
}

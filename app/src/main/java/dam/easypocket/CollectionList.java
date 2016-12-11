package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CollectionList extends BaseActivity {

    private String currentCollectionSelected;
    private String collectionName;

    private Button addElement;
    private Button editDesign;
    private Button explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_test);

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
                startActivity(intent);
            }
        });

        Button addCollection = (Button)findViewById(R.id.buttonAddCollection);
        addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, EditCollection.class);
                startActivity(intent);
            }
        });

        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from Collections", null)) {
            while (allCollectionsCursor.moveToNext()) {
                Button item = new Button(this);
                collectionName = allCollectionsCursor.getString(allCollectionsCursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));
                item.setText(collectionName);
                ll.addView(item);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElement.setEnabled(true);
                        editDesign.setEnabled(true);
                        explore.setEnabled(true);
                        Button currentB = (Button) v;
                        currentCollectionSelected = currentB.getText().toString();
                    }
                });
            }
        }
    }
}

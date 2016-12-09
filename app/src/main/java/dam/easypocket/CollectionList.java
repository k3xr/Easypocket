package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CollectionList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
                startActivity(intent);
            }
        });

        editDesign = (Button)findViewById(R.id.buttonEditarDisenio);
        editDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, EditCollection.class);
                startActivity(intent);
            }
        });

        explore = (Button)findViewById(R.id.buttonExplorar);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent intent = new Intent(CollectionList.this, SummaryVisual.class);
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
                        //falta "cargar" el nombre de la colección actual (tabla) para pasársela luego a los botones al clickear
                        //como coger el nombre del texto del boton actual que se acaba de hacer click?
                        //currentCollectionSelected = ;
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_collections) {
//            Intent intent = new Intent(this, CollectionList.class);
//            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

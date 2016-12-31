package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//hace referencia a la 1.e
public class SummaryVisual extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String currentCollection;
    private CollectionDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_visual);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");
        db = new CollectionDBHelper(this.getApplicationContext());

        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV_1e);
        TextView collectionName = (TextView) findViewById(R.id.myCollection_1e);
        collectionName.setText(currentCollection.toUpperCase());
        collectionName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ll.removeAllViews();

        Button toDeepSearch = (Button) findViewById(R.id.buttonDeepSearch_1e);
        toDeepSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                Intent openCollectionList = new Intent(SummaryVisual.this, DeepSearching.class);
                openCollectionList.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(openCollectionList);
            }
        });

        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+currentCollection, null)) {

            while (allCollectionsCursor.moveToNext()) {
                LinearLayout filaContenedor = new LinearLayout(this);
                TextView item = new TextView(this);

                String rawData = allCollectionsCursor.getString(0);

                item.setText(rawData);

                filaContenedor.addView(item);
                ll.addView(filaContenedor);
            }
        }
        catch(Exception e){
            ll.removeAllViews();
            System.out.print("Tabla sin columnas");
        }
    }

    protected void onResume(){
        super.onResume();

        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");
        db = new CollectionDBHelper(this.getApplicationContext());

        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV_1e);

        ll.removeAllViews();
        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+currentCollection, null)) {

            while (allCollectionsCursor.moveToNext()) {
                LinearLayout filaContenedor = new LinearLayout(this);
                TextView item = new TextView(this);

                String rawData = allCollectionsCursor.getString(0);

                item.setText(rawData);

                filaContenedor.addView(item);
                ll.addView(filaContenedor);
            }
        }
        catch(Exception e){
            ll.removeAllViews();
            System.out.print("Tabla sin columnas");
        }
    }
}

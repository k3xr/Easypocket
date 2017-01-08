package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


//hace referencia a la 1.e
public class SummaryVisual extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        Button toDeepSearch = (Button) findViewById(R.id.buttonDeepSearch_1e);
        toDeepSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open new activity
                String currentCollection = getIntent().getExtras().getString("currentCollectionSelected");
                Intent intent = new Intent(SummaryVisual.this, DeepSearching.class);
                intent.putExtra("currentCollectionSelected",currentCollection);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }

    protected void onResume(){
        super.onResume();

        String currentCollection = getIntent().getExtras().getString("currentCollectionSelected");
        CollectionDBHelper db = new CollectionDBHelper(this.getApplicationContext());

        TextView collectionName = (TextView) findViewById(R.id.summary_1e);
        if (currentCollection != null) {
            collectionName.setText(currentCollection.toUpperCase());
        }
        else {
            collectionName.setText(R.string.summary);
        }
        collectionName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+ currentCollection, null)) {
            stk.removeAllViews();
            String[] columnNames = allCollectionsCursor.getColumnNames();

            TableRow.LayoutParams tableRowParams =
                    new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin = 15;
            int topMargin = 0;
            int rightMargin = 15;
            int bottomMargin = 0;

            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            TableRow tbrow0 = new TableRow(this);
            for (String columnName : columnNames) {
                TextView tv0 = new TextView(this);
                tv0.setText(columnName);
                tv0.setTextColor(Color.WHITE);
                tv0.setTextSize(20.0f);
                tv0.setLayoutParams(tableRowParams);
                tv0.setGravity(Gravity.CENTER);
                tbrow0.addView(tv0);
            }
            tbrow0.setBackgroundResource(R.drawable.row_border);
            stk.addView(tbrow0);
            while (allCollectionsCursor.moveToNext()) {
                TableRow tbrow1 = new TableRow(this);
                for (int i = 0; i < columnNames.length; i++) {
                    TextView tv0 = new TextView(this);
                    tv0.setText(allCollectionsCursor.getString(i));
                    tv0.setTextColor(Color.WHITE);
                    tv0.setGravity(Gravity.CENTER);
                    tbrow1.addView(tv0);
                }
                tbrow1.setBackgroundResource(R.drawable.row_border);
                tbrow1.setLayoutParams(tableRowParams);
                stk.addView(tbrow1);
            }
            stk.setBackgroundColor(Color.DKGRAY);
        }
        catch(Exception e){
            stk.removeAllViews();
            System.out.print("Tabla sin columnas");
        }
    }
}

package dam.easypocket;

import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


//hace referencia a la 1.e
public class SummaryVisual extends AppCompatActivity {

    private String currentCollection;
    private CollectionDBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_visual);

        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");
        db = new CollectionDBHelper(this.getApplicationContext());
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV_1e);

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
            System.out.print("Tabla sin columnas");
        }
    }
}

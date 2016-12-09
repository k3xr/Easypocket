package dam.easypocket;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//hace referencia a la 1.b
public class EditCollection extends AppCompatActivity {

    private String currentCollection;
    private TextView currentNameCollection;
    private CollectionDBHelper db;
    private LinearLayout scrollLayout_1b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_collection);
        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");

        currentNameCollection = (TextView)findViewById(R.id.nombreColeccion);
        currentNameCollection.setText(currentCollection);

        db = new CollectionDBHelper(this.getApplicationContext());

        scrollLayout_1b = (LinearLayout) findViewById(R.id.llCampos_1b);

        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+currentCollection, null)) {
            String[] columnName = allCollectionsCursor.getColumnNames();
            int i = 0;
            while (i<columnName.length) {
                //Button item = new Button(this);
                TextView item = new TextView(this);
                //String columnName = allCollectionsCursor.getString(allCollectionsCursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));
                //String[] columnName = allCollectionsCursor.getColumnNames();
                //String columnContent = allCollectionsCursor.getString(allCollectionsCursor.getColumnIndexOrThrow(columnName));
                //String columnType = db.getDataTypeColumn(currentCollection, columnName);
                String currentDataType =  db.getDataTypeColumn(currentCollection,columnName[i]);
                item.setText(columnName[i]+" "+ currentDataType);
                scrollLayout_1b.addView(item);
                i++;
                /*item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addElement.setEnabled(true);
                        editDesign.setEnabled(true);
                        explore.setEnabled(true);
                        Button currentB = (Button) v;
                        currentCollectionSelected = currentB.getText().toString();
                    }
                });*/
            }
        }
    }
}

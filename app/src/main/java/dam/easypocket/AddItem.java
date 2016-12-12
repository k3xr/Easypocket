package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//hace referencia a la 1.d
public class AddItem extends AppCompatActivity {

    private String currentCollection;
    private TextView currentNameCollection;
    private CollectionDBHelper db;
    private LinearLayout scrollLayout_1d;

    Button saveAndExitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveAndExitButton = (Button)findViewById(R.id.saveAndExitButton_1d);
        cancelButton = (Button)findViewById(R.id.cancelButton_1d);

        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");

        currentNameCollection = (TextView)findViewById(R.id.nombreColeccion);
        currentNameCollection.setText(currentCollection);

        db = new CollectionDBHelper(this.getApplicationContext());

        scrollLayout_1d = (LinearLayout) findViewById(R.id.llCampos_1d);



        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+currentCollection, null)) {
            String[] columnName = allCollectionsCursor.getColumnNames();
            int i = 0;
            while (i<columnName.length) {
                LinearLayout filaContenedor = new LinearLayout(this);
                TextView item = new TextView(this);
                //TextView itemType = new TextView(this);
                EditText entradaDatos = new EditText(this);
                //String currentDataType =  db.getDataTypeColumn(currentCollection,columnName[i]);
                item.setText(columnName[i]);

                if (Build.VERSION.SDK_INT < 23) {

                    item.setTextAppearance(this, android.R.style.TextAppearance);

                } else {

                    item.setTextAppearance(android.R.style.TextAppearance);
                }
                //entradaDatos.setText(currentDataType);
                entradaDatos.setMaxWidth(600);
                entradaDatos.setMinWidth(400);
                scrollLayout_1d.addView(filaContenedor);
                filaContenedor.addView(item);
                filaContenedor.addView(entradaDatos);
                i++;
            }
        }
        catch(Exception e){
            System.out.print("Tabla sin columnas");
        }


        saveAndExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollLayout_1b.removeView((View)v.getParent());
                Intent intent = new Intent(AddItem.this, CollectionList.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollLayout_1b.removeView((View)v.getParent());
                Intent intent = new Intent(AddItem.this, CollectionList.class);
                startActivity(intent);
            }
        });
    }
}

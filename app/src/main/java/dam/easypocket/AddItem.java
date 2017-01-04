package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//hace referencia a la 1.d
public class AddItem extends AppCompatActivity {

    private String currentCollection;
    private TextView currentNameCollection;
    private CollectionDBHelper db;
    private LinearLayout scrollLayout_1d;

    Button saveAndExitButton;
    Button saveAndContButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveAndExitButton = (Button)findViewById(R.id.saveAndExitButton_1d);
        saveAndContButton = (Button)findViewById(R.id.saveMoreButton_1d);
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

        saveAndContButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lay = (LinearLayout) findViewById(R.id.llCampos_1d);

                ArrayList<String> fieldsCollector = new ArrayList<String>();

                for (int i = 0; i < lay.getChildCount(); i++) {

                    LinearLayout segundoIter = (LinearLayout)lay.getChildAt(i);
                    for(int j = 0; j < segundoIter.getChildCount(); j++){

                        View collection = segundoIter.getChildAt(j);

                        //Log.d("Prueba0", "contenido: "+collection.getClass());
                        if (collection instanceof EditText) {
                            EditText datoGuarda = (EditText) collection;
                            fieldsCollector.add(datoGuarda.getText().toString());
                            datoGuarda.setText("");
                            //System.out.print("dentro del if del bucle");
                            //Log.d("Prueba1", "Dentro del bucle y del if");
                        }
                    }
                }

                //System.out.print("fuera del bucle");
                //Log.d("Prueba2", "Fuera del bucle y del if");
                //Log.d("Prueba3", "Tamaño campos: "+fieldsCollector.size());
                String[] fieldsCollected = new String[fieldsCollector.size()];
                fieldsCollected = fieldsCollector.toArray(fieldsCollected);

                db.insertToCollection(currentCollection, fieldsCollected);

            }
        });


        saveAndExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollLayout_1b.removeView((View)v.getParent());

                LinearLayout lay = (LinearLayout) findViewById(R.id.llCampos_1d);

                ArrayList<String> fieldsCollector = new ArrayList<String>();

                for (int i = 0; i < lay.getChildCount(); i++) {

                    LinearLayout segundoIter = (LinearLayout)lay.getChildAt(i);
                    for(int j = 0; j < segundoIter.getChildCount(); j++){

                        View collection = segundoIter.getChildAt(j);

                        //Log.d("Prueba0", "contenido: "+collection.getClass());
                        if (collection instanceof EditText) {
                            EditText datoGuarda = (EditText) collection;
                            fieldsCollector.add(datoGuarda.getText().toString());
                            //System.out.print("dentro del if del bucle");
                            //Log.d("Prueba1", "Dentro del bucle y del if");
                        }
                    }
                }

                //System.out.print("fuera del bucle");
                //Log.d("Prueba2", "Fuera del bucle y del if");
                //Log.d("Prueba3", "Tamaño campos: "+fieldsCollector.size());
                String[] fieldsCollected = new String[fieldsCollector.size()];
                fieldsCollected = fieldsCollector.toArray(fieldsCollected);

                db.insertToCollection(currentCollection, fieldsCollected);

                Intent intent = new Intent(AddItem.this, CollectionList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

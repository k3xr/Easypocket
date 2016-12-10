package dam.easypocket;

import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

//hace referencia a la 1.b
public class EditCollection extends AppCompatActivity {

    private String currentCollection;
    private TextView currentNameCollection;
    private CollectionDBHelper db;
    private LinearLayout scrollLayout_1b;

    private ArrayList<String> addedColumnsName;
    private ArrayList<String> addedColumnstype;

    private Button addField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_collection);
        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");

        currentNameCollection = (TextView)findViewById(R.id.nombreColeccion);
        currentNameCollection.setText(currentCollection);

        addField = (Button) findViewById(R.id.buttonAddCampo_1b);

        db = new CollectionDBHelper(this.getApplicationContext());

        scrollLayout_1b = (LinearLayout) findViewById(R.id.llCampos_1b);

        try (Cursor allCollectionsCursor = db.getDb().rawQuery("Select * from "+currentCollection, null)) {
            String[] columnName = allCollectionsCursor.getColumnNames();
            int i = 0;
            while (i<columnName.length) {
                LinearLayout filaContenedor = new LinearLayout(this);
                TextView item = new TextView(this);
                TextView itemType = new TextView(this);
                String currentDataType =  db.getDataTypeColumn(currentCollection,columnName[i]);
                item.setText(columnName[i]);

                if (Build.VERSION.SDK_INT < 23) {

                    item.setTextAppearance(this, android.R.style.TextAppearance);

                } else {

                    item.setTextAppearance(android.R.style.TextAppearance);
                }
                itemType.setText(currentDataType);
                scrollLayout_1b.addView(filaContenedor);
                filaContenedor.addView(item);
                filaContenedor.addView(itemType);
                i++;
            }
        }
        catch(Exception e){
            System.out.print("Tabla sin columnas");
        }

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generaNuevoFormulario();
            }
        });
    }

    private boolean generaNuevoFormulario(){
        scrollLayout_1b = (LinearLayout) findViewById(R.id.llCampos_1b);
        LinearLayout contenedorIntermedio = new LinearLayout(this);

        TextView TittleForm = new TextView(this);
        EditText entradaNombre = new EditText(this);
        Button cancelarOperacion = new Button(this);

        cancelarOperacion.setText("Cancel");
        TittleForm.setText("Write: ");
        entradaNombre.setMaxWidth(600);
        entradaNombre.setMinWidth(400);

        contenedorIntermedio.addView(TittleForm);
        contenedorIntermedio.addView(entradaNombre);
        contenedorIntermedio.addView(cancelarOperacion);
        scrollLayout_1b.addView(contenedorIntermedio);

        cancelarOperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollLayout_1b.removeView((View)v.getParent());
            }
        });

        return true;
    }
}

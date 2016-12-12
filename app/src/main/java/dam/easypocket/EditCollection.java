package dam.easypocket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//hace referencia a la 1.b
public class EditCollection extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private String currentCollection;
    private TextView currentNameCollection;
    private CollectionDBHelper db;
    private LinearLayout scrollLayout_1b;

    private ArrayList<String> addedColumnsName;
    private ArrayList<String> addedColumnstype;

    private ArrayList<Integer> idEditText;

    private Button buttonGuardarYSalir;
    private Button buttonCancelarYSalir;

    private Button addField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addedColumnsName = new ArrayList<String>();
        idEditText = new ArrayList<Integer>();

        currentCollection = getIntent().getExtras().getString("currentCollectionSelected");

        currentNameCollection = (TextView)findViewById(R.id.nombreColeccion);
        currentNameCollection.setText(currentCollection);

        addField = (Button) findViewById(R.id.buttonAddCampo_1b);
        buttonGuardarYSalir = (Button) findViewById(R.id.buttonGuardarYSalir_1b);
        buttonCancelarYSalir = (Button) findViewById(R.id.buttonCancelarYSalir_1b);

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

        buttonGuardarYSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recogeEntradasNuevasColumnas();

                if(addedColumnsName!=null) {
                    for (int i = 0; i < addedColumnsName.size(); i++) {
                        db.addColumn(currentNameCollection.getText().toString(), addedColumnsName.get(i));
                    }
                }
                Intent intent = new Intent(EditCollection.this, CollectionList.class);
                startActivity(intent);
            }
        });

        buttonCancelarYSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollLayout_1b.removeView((View)v.getParent());
                Intent intent = new Intent(EditCollection.this, CollectionList.class);
                startActivity(intent);
            }
        });
    }

    private boolean generaNuevoFormulario(){
        scrollLayout_1b = (LinearLayout) findViewById(R.id.llCampos_1b);
        LinearLayout contenedorIntermedio = new LinearLayout(this);

        TextView TittleForm = new TextView(this);
        EditText entradaNombre = new EditText(this);
        Button cancelarOperacion = new Button(this);

        //no genera un ID por defecto, hay que asignarselo??
        //idEditText.add(entradaNombre.getId());
        //prueba, da -1
        //entradaNombre.setText((Integer.toString(entradaNombre.getId())));

        //se podría usar setID? genera conflictos con otros ID puestos anteriormente?

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
                //idEditText.remove(0);
                //View viewPadre = (View)v.getParent();
                scrollLayout_1b.removeView((View)v.getParent());
            }
        });
        return true;
    }

    private boolean recogeEntradasNuevasColumnas(){
        if(idEditText!=null){
            if(idEditText.size()>0){
                for(int i = 0; i < idEditText.size();i++) {
                    addedColumnsName.add(((EditText)findViewById(idEditText.get(i))).getEditableText().toString());
                }
            }
        }
        return true;
    }
}

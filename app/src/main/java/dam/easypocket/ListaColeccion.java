package dam.easypocket;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class ListaColeccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_coleccion);

        LinearLayout ll = (LinearLayout) findViewById(R.id.linSV);

        CollectionDBHelper db = new CollectionDBHelper(this.getApplicationContext());
        Cursor cursor = db.getCollections("testCollectionNew");

        cursor.moveToFirst();
        String collectionName = cursor.getString(cursor.getColumnIndexOrThrow(CollectionDBHelper.COLLECTIONS_COLUMN_NAME));

        Button item = new Button(this);
        item.setText(collectionName);
        item.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        ll.addView(item);

    }
}

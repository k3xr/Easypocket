package dam.easypocket;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBConnection dbConnection = new DBConnection();
        dbConnection.easyPocketDatabase = openOrCreateDatabase("pocket",MODE_PRIVATE,null);
        dbConnection.createTestDatabase();
        SQLiteDatabase db = dbConnection.getEasyPocketDatabase();


        Cursor resultSet = db.rawQuery("Select * from Libros",null);
        resultSet.moveToFirst();

        TextView text = (TextView)findViewById(R.id.textView);

        for(int i = 0; i < resultSet.getCount(); i++){
            resultSet.moveToPosition(i);
            text.setText(text.getText()+"\n"+resultSet.getString(0)+": "+resultSet.getInt(1));
        }
    }
}

package dam.easypocket;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class DBConnection extends AppCompatActivity {

    SQLiteDatabase easyPocketDatabase;

    public DBConnection(){
        easyPocketDatabase = openOrCreateDatabase("pocket",MODE_PRIVATE,null);
    }

    public void createTestDatabase(){
        easyPocketDatabase.execSQL("Delete FROM Libros");
        easyPocketDatabase.execSQL("CREATE TABLE IF NOT EXISTS Libros(Nombre VARCHAR, Paginas INT);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro1',40);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro2',90);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro3',87090);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro4',0);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro5',1);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro6',498);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro7',4235);");
        easyPocketDatabase.execSQL("INSERT INTO Libros VALUES('libro8',600);");
    }

    public SQLiteDatabase getEasyPocketDatabase(){
        return easyPocketDatabase;
    }

}

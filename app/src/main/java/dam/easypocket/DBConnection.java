package dam.easypocket;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class DBConnection extends AppCompatActivity {

    public SQLiteDatabase easyPocketDatabase;

    public DBConnection(){
//        easyPocketDatabase = this.openOrCreateDatabase("pocket",MODE_PRIVATE,null);

        String[] columnas = new String[4];
        columnas[0] = "columna1";
        columnas[1] = "columna2";
        columnas[2] = "columna3";
        columnas[3] = "columna4";
        String[] tiposColumnas = new String[4];
        tiposColumnas[0] = "VARCHAR";
        tiposColumnas[1] = "INT";
        tiposColumnas[2] = "BOOLEAN";
        tiposColumnas[3] = "DATESTAMP";
        createTable("tablaTest", columnas, tiposColumnas);
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

    public boolean createTable(String tableName, String[] columnName, String[] columnType){
        if(columnName.length != columnType.length){
            return false;
        }

        String query = "CREATE TABLE IF NOT EXISTS "+tableName+"(";

        for(int i = 0; i < columnName.length; i++){
            if(i == 0){
                query += columnName[i] + " " + columnType[i];
            }
            else{
                query += ","+columnName[i] + " " + columnType[i];
            }

        }

        query += ")";
        System.out.println(query);
        return true;
    }

}

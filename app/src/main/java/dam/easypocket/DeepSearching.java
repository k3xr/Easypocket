package dam.easypocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DeepSearching extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_searching);


        String currentCollection = getIntent().getExtras().getString("currentCollectionSelected");

        Button searchButton = (Button) findViewById(R.id.buttonSearch_3a);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFunction();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void searchFunction(){
        EditText textToSearch = (EditText) findViewById(R.id.editTextSearch_3a);
    }
}

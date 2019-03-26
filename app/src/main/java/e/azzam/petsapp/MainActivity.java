package e.azzam.petsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import e.azzam.petsapp.Adapters.PetCatalogAdapter;
import e.azzam.petsapp.data.PetContract;
import e.azzam.petsapp.data.PetDBHelper;

public class MainActivity extends AppCompatActivity {

    private PetDBHelper dbHelper;
    private TextView textViewCount;
    private ListView rvPetCatalog;
    private FloatingActionButton fab;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new PetDBHelper(this);
        textViewCount = (TextView) findViewById(R.id.tv_count);
        rvPetCatalog = (ListView) findViewById(R.id.rv_pet_catalog);
        fab = (FloatingActionButton) findViewById(R.id.fabtn);
        emptyView = (View) findViewById(R.id.empty_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo(){
        String[] projection = {PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};
//        String selection = PetContract.PetEntry.COLUMN_PET_GENDER + "=?";
//        String[] selectionargs = {String.valueOf(PetContract.PetEntry.GENDER_MALE)};

       final Cursor cursor = dbHelper.getReadableDatabase().query(PetContract.PetEntry.TABLE_NAME,
                projection,null,null, null, null, null);

            textViewCount.setText("data table pet consist : " + cursor.getCount() + " rows \n\n");
            textViewCount.append(PetContract.PetEntry._ID + " - " +
                                 PetContract.PetEntry.COLUMN_PET_NAME + " - " +
                                 PetContract.PetEntry.COLUMN_PET_BREED + " - " +
                                 PetContract.PetEntry.COLUMN_PET_GENDER + " - " +
                                 PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");
       final int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.ID);
       final int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
       final int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
       final int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
       final int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
//            while (cursor.moveToNext()){
//                int id = cursor.getInt(idColumnIndex);
//                String name = cursor.getString(nameColumnIndex);
//                String breed = cursor.getString(breedColumnIndex);
//                int gender = cursor.getInt(genderColumnIndex);
//                int weight = cursor.getInt(weightColumnIndex);
//
//                textViewCount.append("\n" + id +
//                        " - " + name +
//                        " - " + breed +
//                        " - " + gender +
//                        " - " + weight
//                );

        PetCatalogAdapter adapter = new PetCatalogAdapter(this,R.layout.item_list_pet,cursor,0);
        rvPetCatalog.setAdapter(adapter);
        rvPetCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.EXTRA_ID, cursor.getLong(idColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_NAME, cursor.getString(nameColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_BREED, cursor.getString(breedColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_GENDER, cursor.getInt(genderColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_WEIGHT, cursor.getInt(weightColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_IS_EDIT_MODE,true);
                startActivity(intent);
            }

        });

        if (cursor.getCount() > 0){
            rvPetCatalog.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            rvPetCatalog.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        adapter.changeCursor(cursor);

        }

//    }



    public void onAddpet (View view) {
        Intent editorIntent = new Intent(this, EditorActivity.class);
        startActivity(editorIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void insert (){
        ContentValues cp = new ContentValues();
        cp.put(PetContract.PetEntry.COLUMN_PET_NAME, "Si Meong");
        cp.put(PetContract.PetEntry.COLUMN_PET_BREED, "Kucing Kampung");
        cp.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        cp.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 4);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, cp);
//       if (newRowID == -1) {
//            Toast.makeText(this, "Error Inputting Data", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Inserting Data Success", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_dummy:
                insert();
                displayDatabaseInfo();
                break;
            case R.id.action_deleteallpet:
                deleteAllpets();
                displayDatabaseInfo();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllpets(){
        dbHelper.getWritableDatabase().execSQL("delete from " + PetContract.PetEntry.TABLE_NAME);
    }

}

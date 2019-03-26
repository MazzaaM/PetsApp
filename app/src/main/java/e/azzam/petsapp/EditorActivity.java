package e.azzam.petsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import e.azzam.petsapp.data.PetContract;
import e.azzam.petsapp.data.PetDBHelper;

public class EditorActivity extends AppCompatActivity {

    private TextInputEditText etName;
    private TextInputEditText etBreed;
    private TextInputEditText etWeight;
    private Spinner spGender;

    public static final String EXTRA_ID = "petid";
    public static final String EXTRA_NAME = "petname";
    public static final String EXTRA_BREED = "petbreed";
    public static final String EXTRA_WEIGHT = "petweight";
    public static final String EXTRA_GENDER = "petgender";
    public static final String EXTRA_IS_EDIT_MODE = "isEditMode";

    private int mGender;
    private String mName;
    private String mBreed;
    private int mWeight;
    private PetDBHelper petDBHelper;
    private boolean isEditMode = false;
    private long id;

    private long newRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        etName = (TextInputEditText) findViewById(R.id.et_name);
        etBreed = (TextInputEditText) findViewById(R.id.et_breed);
        etWeight = (TextInputEditText) findViewById(R.id.et_weight);
        spGender = (Spinner) findViewById(R.id.sp_gender);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        petDBHelper = new PetDBHelper(this);

        ArrayAdapter genderArrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_gender_option, android.R.layout.simple_spinner_item);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spGender.setAdapter(genderArrayAdapter);
        Intent intent = getIntent();

        if (intent.getExtras()!=null){
            id = intent.getLongExtra(EXTRA_ID, -1);
            etName.setText(intent.getStringExtra(EXTRA_NAME));
            etBreed.setText(intent.getStringExtra(EXTRA_BREED));
            etWeight.setText(String.valueOf(intent.getIntExtra(EXTRA_WEIGHT,0)));
            int position = intent.getIntExtra(EXTRA_GENDER,0);
            spGender.setSelection(position);
            isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE,false);
        }


        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedItem)) {
                    switch (selectedItem) {
                        case "Male":
                            mGender = 1;
                            break;
                        case "Female":
                            mGender = 2;
                            break;
                        default:
                            mGender = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                mName = etName.getText().toString();
                mBreed = etBreed.getText().toString();
                mWeight = (etWeight.getText().toString().equals("") ? 0 : Integer.parseInt(etWeight.getText().toString()));
                if (mName.isEmpty() || mBreed.isEmpty()){
                    etName.setError("Name Can't be empty");
                    etBreed.setError("Breed Can't be empty");
                } else {
                    if (!isEditMode){
                        savePet();
                        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();

                    }else {
                        updatePet();
                        Toast.makeText(this,"Data Updated", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;

            case R.id.action_delete:
                deletePet();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void savePet() {
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, mName);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, mBreed);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, mWeight);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, mGender);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
    }

    private void deletePet(){
        String[] args = {String.valueOf(id)};
        petDBHelper.getWritableDatabase().delete(PetContract.PetEntry.TABLE_NAME, PetContract.PetEntry._ID+ "=?", args);

    }

    private void updatePet(){
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, mName);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, mBreed);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, mWeight);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, mGender);
        petDBHelper.getWritableDatabase()
                .update(PetContract.PetEntry.TABLE_NAME, values,
                        PetContract.PetEntry._ID + "=?", new String[]{String.valueOf(id)});
    }
}

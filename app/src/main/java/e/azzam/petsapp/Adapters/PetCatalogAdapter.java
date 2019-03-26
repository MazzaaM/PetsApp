package e.azzam.petsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import e.azzam.petsapp.EditorActivity;
import e.azzam.petsapp.R;
import e.azzam.petsapp.data.PetContract;

public class PetCatalogAdapter extends android.support.v4.widget.ResourceCursorAdapter {

    private TextView tvName;
    private TextView tvBreed;

    public PetCatalogAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.ID);
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        final int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
        final int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvBreed = (TextView) view.findViewById(R.id.tv_breed);
        tvName.setText(cursor.getString(nameColumnIndex));
        tvBreed.setText(cursor.getString(breedColumnIndex));

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, EditorActivity.class);
//                intent.putExtra(EditorActivity.EXTRA_ID, cursor.getLong(idColumnIndex));
//                intent.putExtra(EditorActivity.EXTRA_NAME, tvName.getText().toString());
//                intent.putExtra(EditorActivity.EXTRA_BREED, tvBreed.getText().toString());
//                intent.putExtra(EditorActivity.EXTRA_GENDER, cursor.getInt(genderColumnIndex));
//                intent.putExtra(EditorActivity.EXTRA_WEIGHT, cursor.getInt(weightColumnIndex));
//                context.startActivity(intent);
//            }
//        });

    }
}

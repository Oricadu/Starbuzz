package com.hfad.starbuzz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try{
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRINK",
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[] {Integer.toString(drinkId)},
                    null, null, null);

            if (cursor.moveToFirst()){
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

                TextView name = (TextView) findViewById(R.id.name);
                TextView description = (TextView) findViewById(R.id.description);
                ImageView photo = (ImageView) findViewById(R.id.photo);
                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);

                name.setText(nameText);
                description.setText(descriptionText);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                boolean isChecked = (cursor.getInt(3) == 1);
                favorite.setChecked(isChecked);

            }

            cursor.close();
            db.close();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    "Database is unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }



    }

    public void onFavoriteClicked(View view){
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            ContentValues drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());


            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK",
                        drinkValues,
                        "_id = ?",
                        new String[]{Integer.toString(drinkId)});
                db.close();
            }catch (SQLiteException e){

                Toast toast = Toast.makeText(DrinkActivity.this,
                        "DB unavailable",
                        Toast.LENGTH_SHORT);
                toast.show();
            }


    }



    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean>{
        private ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            ContentValues drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];


            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try{
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK",
                        drinkValues,
                        "_id = ?",
                        new String[]{Integer.toString(drinkId)});
                db.close();
                return true;

            }catch (SQLiteException e){
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success){
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "DB unavailable",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    /*private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean>{
        private ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            ContentValues drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId = drinks[0];


            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try{
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK",
                        drinkValues,
                        "_id = ?",
                        new String[]{Integer.toString(drinkId)});
                db.close();
                return true;

            }catch (SQLiteException e){
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success){
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "DB unavailable",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }*/

}
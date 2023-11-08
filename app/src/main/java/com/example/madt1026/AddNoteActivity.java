package com.example.madt1026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AddNoteActivity extends AppCompatActivity {
    EditText edTitle;
    EditText edNote;

    // Adding the "Back" button, Finding IDs onCreate, showing the Add Note layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_note);

        this.edTitle = findViewById(R.id.edTitle);
        this.edNote = findViewById(R.id.edNote);
        edTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    // onBtnSaveAndCloseClick adds the new note to Shared Preferences
    public void onBtnSaveAndCloseClick(View view) {
        String titleToAdd = this.edTitle.getText().toString();
        String noteToAdd = this.edNote.getText().toString();

        if (titleToAdd.isEmpty() || noteToAdd.isEmpty()) {
            // Error message pops-up if either the Title or Body Text is empty (they both must be filled to add a new note)
            Toast.makeText(this, "Both Title and Body Text are required.", Toast.LENGTH_SHORT).show();
        } else {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            SharedPreferences sharedPref = this.getSharedPreferences(Constants.NOTES_FILE, this.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, null);
            Set<String> newSet = new HashSet<>();

            if (savedSet != null) {
                newSet.addAll(savedSet);
            }

            String fullNote = titleToAdd + "\n\n" + noteToAdd;
            newSet.add(fullNote);

            editor.putString(Constants.NOTE_KEY, fullNote);
            editor.putString(Constants.NOTE_KEY_DATE, formattedDate);
            editor.putStringSet(Constants.NOTES_ARRAY_KEY, newSet);
            editor.apply();
            finish();
        }
    }

    // Back Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


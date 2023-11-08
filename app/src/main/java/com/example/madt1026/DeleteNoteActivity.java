package com.example.madt1026;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spinnerNotes;
    private Button btnDeleteNote;

    // Layout display and giving the Spinner elements to show onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_delete_note);

        spinnerNotes = findViewById(R.id.spinnerNotes);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);

        // Spinner elements from SharedPreferences
        List<String> noteTitles = noteTitlesForSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, noteTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotes.setAdapter(adapter);

        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedNoteTitle = spinnerNotes.getSelectedItem().toString();

                // Deleting the chosen note
                removeNoteFromSharedPreferences(selectedNoteTitle);

                // Spinner update
                noteTitles.remove(selectedNoteTitle);
                adapter.notifyDataSetChanged();

                Toast.makeText(DeleteNoteActivity.this, "Note deleted: " + selectedNoteTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Getting the note titles from Shared Preferences
    private List<String> noteTitlesForSpinner() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
        Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, new HashSet<>());
        return new ArrayList<>(savedSet);
    }

    // Removing the chosen note from Shared Preferences
    private void removeNoteFromSharedPreferences(String noteTitle) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
        Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, new HashSet<>());

        // Remove the note tile (from the savedSet)
        savedSet.remove(noteTitle);

        // Clearing Shared Preferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        // Notes that need to stay are being reinserted
        Set<String> updatedSet = new HashSet<>();
        updatedSet.addAll(savedSet);

        editor.putStringSet(Constants.NOTES_ARRAY_KEY, updatedSet);
        editor.apply();
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



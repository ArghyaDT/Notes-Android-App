package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.myapplication16.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetails extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEdited = false;
    TextView deleteNoteTextVIewBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.nots_content);
        saveNoteBtn= findViewById(R.id.save_non_transition_alpha_note_btn);
        pageTitleTextView= findViewById(R.id.page_title);
        deleteNoteTextVIewBtn = findViewById(R.id.delete_note_text_viewBtn);

        //recive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId !=null && !docId.isEmpty()){
            isEdited = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEdited){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextVIewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v)-> saveNote());

        deleteNoteTextVIewBtn.setOnClickListener((v)-> deleteNoteFromFirebase());
    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent= contentEditText.getText().toString();

        if(noteTitle==null || noteContent.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEdited){
            //update existing node
            documentReference=Utlity.getCollectionReference().document(docId);
        }
        else{
            //create new node
            documentReference=Utlity.getCollectionReference().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utlity.showToast(NoteDetails.this, "Note added successfully");
                    finish();
                }else{
                    Utlity.showToast(NoteDetails.this, "Fail while adding note");
                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference=Utlity.getCollectionReference().document(docId);




        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note deleted
                    Utlity.showToast(NoteDetails.this, "Note deleted successfully");
                    finish();
                }else{
                    Utlity.showToast(NoteDetails.this, "Fail while deleting note");
                }
            }
        });
    }
}
package com.example.androidfirebasecurdoperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText name, email, contact;
    private Spinner spinnerCity;
    private AutoCompleteTextView autoCompleteTextViewLang;
    private Button buttonAddUser;
    private ProgressBar progressBar;

    String stringLang[] = {"Android", "Java", "Kotlin", "PHP", "CSS", "Bootstrap", "HTML", "jQuery"};
    ArrayAdapter arrayAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        autoCompleteTextViewLang = findViewById(R.id.select_lang);
        progressBar = findViewById(R.id.progressBar);
        buttonAddUser = findViewById(R.id.btn_add_user);
        spinnerCity = findViewById(R.id.spinnerCity);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, stringLang);
        autoCompleteTextViewLang.setAdapter(arrayAdapter);
        autoCompleteTextViewLang.setThreshold(1);

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String NAME = name.getText().toString().trim();
                String EMAIL = email.getText().toString().trim();
                String CONTACT = contact.getText().toString().trim();
                String CITY = spinnerCity.getSelectedItem().toString().trim();
                String LANG = autoCompleteTextViewLang.getText().toString().trim();

                if (NAME.isEmpty()) {
                    name.setError("Name required");
                    name.requestFocus();
                    return;
                }

                if (EMAIL.isEmpty()) {
                    email.setError("Email required");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
                    email.setError("Enter valid email address");
                    email.requestFocus();
                    return;
                }

                if (CONTACT.isEmpty()) {
                    contact.setError("Contact required");
                    contact.requestFocus();
                    return;
                }

                if (LANG.isEmpty()) {
                    autoCompleteTextViewLang.setError("Language required");
                    autoCompleteTextViewLang.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                String uniqueId = databaseReference.push().getKey();
                User user = new User(uniqueId, NAME, EMAIL, CONTACT, CITY, LANG);

                databaseReference.child(uniqueId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_LONG).show();
                            name.setText("");
                            email.setText("");
                            contact.setText("");
                            autoCompleteTextViewLang.setText("");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void openViewDataActivity(View view) {
        startActivity(new Intent(MainActivity.this, UserActivity.class));
    }
}
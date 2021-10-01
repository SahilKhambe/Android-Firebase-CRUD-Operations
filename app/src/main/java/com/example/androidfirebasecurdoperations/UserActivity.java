package com.example.androidfirebasecurdoperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private ListView listView;
    List<User> list;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    private static final String NAME_KEY = "NAME_";
    private static final String EMAIL_KEY = "EMAIL_";
    private static final String CONTACT_KEY = "CONTACT_";
    private static final String CITY_KEY = "CITY_";
    private static final String LANG_KEY = "LANG_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressBar_UserActivity);

        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    list.add(user);
                }
                UserAdapter userAdapter = new UserAdapter(UserActivity.this, list);
                listView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = list.get(position);
                        Intent intent = new Intent(UserActivity.this, ViewUserData.class);
                        intent.putExtra(NAME_KEY, user.getName());
                        intent.putExtra(EMAIL_KEY, user.getEmail());
                        intent.putExtra(CONTACT_KEY, user.getContact());
                        intent.putExtra(CITY_KEY, user.getCity());
                        intent.putExtra(LANG_KEY, user.getLanguage());
                        startActivity(intent);
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = list.get(position);
                        showUpdateDialog(user.getId(), user.getName(), user.getEmail(), user.getContact(), user.getCity(), user.getLanguage());

                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String id, final String name, final String email, final String contact, String city, String lang) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.update_layout, null);
        builder.setView(view);

        final EditText edit_name = view.findViewById(R.id.update_name);
        final EditText edit_email = view.findViewById(R.id.update_email);
        final EditText edit_contact = view.findViewById(R.id.update_contact);
        final Spinner spinner_city = view.findViewById(R.id.update_city);
        final AutoCompleteTextView autoCompleteTextView_ = view.findViewById(R.id.update_lang);
        Button btn_button = view.findViewById(R.id.update_button);
        Button btn_delete = view.findViewById(R.id.delete_button);

        builder.setTitle("Updating for " + name);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_name = edit_name.getText().toString().trim();
                String u_email = edit_email.getText().toString().trim();
                String u_contact = edit_contact.getText().toString().trim();
                String u_city = spinner_city.getSelectedItem().toString().trim();
                String u_lang = autoCompleteTextView_.getText().toString().trim();

                if (u_name.isEmpty()) {
                    edit_name.setError("Name required");
                    edit_name.requestFocus();
                    return;
                }

                if (u_email.isEmpty()) {
                    edit_email.setError("Email required");
                    edit_email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(u_email).matches()) {
                    edit_email.setError("Enter valid email address");
                    edit_email.requestFocus();
                    return;
                }

                if (u_contact.isEmpty()) {
                    edit_contact.setError("Contact required");
                    edit_contact.requestFocus();
                    return;
                }

                if (u_lang.isEmpty()) {
                    autoCompleteTextView_.setError("Language required");
                    autoCompleteTextView_.requestFocus();
                    return;
                }

                updateUser(id, u_name, u_email, u_contact, u_city, u_lang);
                alertDialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(id);
                alertDialog.dismiss();
            }
        });
    }

    private void deleteUser(String id) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean updateUser(String id, String u_name, String u_email, String u_contact, String u_city, String u_lang) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(id);

        User user = new User(id, u_name, u_email, u_contact, u_city, u_lang);

        databaseReference.setValue(user);

        Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_LONG).show();

        return true;
    }
}

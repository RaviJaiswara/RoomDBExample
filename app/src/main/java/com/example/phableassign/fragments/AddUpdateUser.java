package com.example.phableassign.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phableassign.DatabaseClient;
import com.example.phableassign.MainActivity;
import com.example.phableassign.R;
import com.example.phableassign.model.User;

public class AddUpdateUser extends Fragment {

    private EditText editTextName, editTextEmail;
    private TextView heading;
    private Button button;
    private String type;
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_update_user, container, false);

        if (getArguments() != null){
            type = getArguments().getString("type");
        }

        heading = view.findViewById(R.id.heading);
        button = view.findViewById(R.id.button);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);

        if (type.equalsIgnoreCase("edit")){
            heading.setText("Edit User");
            button.setText("Update");
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Bundle bundle = getArguments();
            user= (User) bundle.getSerializable("user");
            loadUser(user);
        }

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("add")) {
                    saveUser();
                }else {
                    updateUser(user);
                }
            }
        });

        return view;
    }

    private void loadUser(User user) {
        editTextName.setText(user.getName());
        editTextEmail.setText(user.getEmail());
    }

    private void saveUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;
        }

        class SaveUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                User user = new User();
                user.setName(name);
                user.setEmail(email);

                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .userDao()
                        .insert(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

        SaveUser saveUser = new SaveUser();
        saveUser.execute();
    }

    private void updateUser(final User user) {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Task required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Desc required");
            editTextEmail.requestFocus();
            return;
        }

        class UpdateUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                user.setName(name);
                user.setEmail(email);
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .userDao()
                        .update(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

        UpdateUser updateUser = new UpdateUser();
        updateUser.execute();
    }

}

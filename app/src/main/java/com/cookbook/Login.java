package com.cookbook;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cookbook.Model.CurrentUser;
import com.cookbook.Model.Model;
import com.cookbook.Model.ModelFireBase;
import com.cookbook.Model.User;

public class Login extends Fragment {
    Button login_btn, register_btn;
    TextView password, username, error;
    ModelFireBase modelFirebase = new ModelFireBase();
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        login_btn = v.findViewById(R.id.login_button);
        register_btn = v.findViewById(R.id.register_button);
        username = v.findViewById(R.id.editTextName);
        password = v.findViewById(R.id.editTextPassword);
        error = v.findViewById(R.id.errorLogin_tv);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return v;
    }

    private void login() {
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();
        modelFirebase.getUser(enteredUsername, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if(user == null) {
                    error.setText("Incorrect Username or Password");
                } else {
                    if (user.getPassword().equals(enteredPassword)) {
                        CurrentUser.instance.getUser().setName(enteredUsername);
                        Navigation.findNavController(v).popBackStack();
                    } else {
                        error.setText("Incorrect Username or Password");
                    }
                }
            }
        });
    }

    private void register() {
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();
        if(enteredUsername.length() < 1 || enteredPassword.length() < 1) {
            error.setText("Please enter valid username and password");
            return;
        }

        modelFirebase.getUser(enteredUsername, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user != null) {
                    error.setText("Username alerady exists");
                } else {
                    User newUser = new User();
                    newUser.setName(enteredUsername);
                    newUser.setPassword(enteredPassword);
                    modelFirebase.addUser(newUser, new Model.AddUserListener() {
                        @Override
                        public void onComplete() {
                            error.setText("User Created Sucessfuly");
                            username.setText("");
                            password.setText("");
                        }
                    });
                }
            }
        });

    }
}
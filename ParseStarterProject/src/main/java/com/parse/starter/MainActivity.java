/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {
   Boolean loginActiveUser = false;
   Button loginSignupButton;
   TextView toggleSignupMode;

   public void redirectLogin(){
     if( ParseUser.getCurrentUser() != null){
       Intent intent = new Intent(this, UserListActivity.class);
       startActivity(intent);
     }
   }

   public void toggleLogMode(View view){
    if(loginActiveUser){
      loginActiveUser = false;
      loginSignupButton.setText("Sign Up");
      toggleSignupMode.setText("Or, Log In");
    }else{
      loginActiveUser = true;
      loginSignupButton.setText("Log In");
      toggleSignupMode.setText("Or, Sign Up");

    }
   }

   public void signupLogin(View view){
     EditText usernameEditText = (EditText) findViewById(R.id.usernameTextView);
     EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

     if(loginActiveUser){

       ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
         @Override
         public void done(ParseUser user, ParseException e) {
           if(e == null){
            redirectLogin();
           }else{
             String message = e.getMessage();
             if(message.toLowerCase().contains("java")) {
               Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
             }else{
               Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
             }
           }
         }
       });
     }else {

       ParseUser user = new ParseUser();
       user.setUsername(usernameEditText.getText().toString());
       user.setPassword(passwordEditText.getText().toString());

       user.signUpInBackground(new SignUpCallback() {
         @Override
         public void done(ParseException e) {
           if (e == null) {
             redirectLogin();

           } else {
             String message = e.getMessage();
             if(message.toLowerCase().contains("java")) {
               Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
             }else{
               Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
             }
           }
         }
       });
     }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Whatsapp Login");

    loginSignupButton = findViewById(R.id.signupButton);
    toggleSignupMode = findViewById(R.id.toggleLoginModeTextView);
    if(ParseUser.getCurrentUser() != null){
      redirectLogin();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}
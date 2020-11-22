package com.example.sendemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    EditText title,content;
    Button sendemail;
    private static ArrayList<String> listcustomer = new ArrayList<>();
    private static ArrayList<String> email = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = (ListView) findViewById(R.id.lv);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        sendemail = (Button) findViewById(R.id.send);

        ArrayAdapter adapter= new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1, listcustomer);
        lv.setAdapter(adapter);


        database = FirebaseDatabase.getInstance();
        root = database.getReference("Users");

        //load data from firebase

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listcustomer.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keynode : dataSnapshot.getChildren() )
                {

                    keys.add(keynode.getKey());
                    String s ="fullname: "+  keynode.child("Customer").child("fullname").getValue() + "\n"
                               + "age: "  +keynode.child("Customer").child("age").getValue() + "\n"
                           + "email: " +keynode.child("Customer").child("email").getValue();
                    listcustomer.add(s);
                    adapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendEmail();

        adapter.notifyDataSetChanged();
    }
    // send email to all email in customer
    protected void sendEmail() {
       sendemail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               root.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       List<String> keys = new ArrayList<>();
                       for(DataSnapshot keynode : dataSnapshot.getChildren() )
                       {

                           keys.add(keynode.getKey());
                           email.add(keynode.child("Customer").child("email").getValue()+"");

                       }

                       String[] TO = email.toArray(new String[0]);
                       Intent emailIntent = new Intent(Intent.ACTION_SEND);
                       emailIntent.setData(Uri.parse("mailto:"));
                       emailIntent.setType("text/plain");


                       emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

                       emailIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
                       emailIntent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());

                       try {
                           startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                           finish();
                           Log.i("Finished sending email", "");
                       } catch (android.content.ActivityNotFoundException ex) {
                           Toast.makeText(MainActivity.this,
                                   "There is no email client installed.", Toast.LENGTH_SHORT).show();
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });



           }
       });
    }
}
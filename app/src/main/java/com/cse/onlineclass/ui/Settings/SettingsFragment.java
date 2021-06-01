package com.cse.onlineclass.ui.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.onlineclass.R;
import com.cse.onlineclass.ui.home.HomeFragment;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment {

    Spinner spinner1,spinner2;
    Button button1;
    EditText Name;
    TextView toasttext;
    ImageView Image;
    Firebase ref;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        LayoutInflater inflater1 = getLayoutInflater();
        View layout=inflater.inflate(R.layout.custom_toast,(ViewGroup)root.findViewById(R.id.custom_toast));
        Firebase.setAndroidContext(getContext());
        spinner1 = root.findViewById(R.id.spinner1);
        spinner2 = root.findViewById(R.id.spinner2);
        button1 = root.findViewById(R.id.button1);
        toasttext=layout.findViewById(R.id.textViewtoast);
        Image=layout.findViewById(R.id.imageView2);
        Name = root.findViewById(R.id.Name);
        final Toast toast= new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        ref=new Firebase("https://nct-online-class.firebaseio.com/");
        SharedPreferences sharedpref= getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpref.edit();
        if(sharedpref.getBoolean("Keyok",true)) {
            final String key1 = ref.child("users").push().getKey();
            editor.putString("Key",key1);
            editor.apply();
        }

        String[] value = {"Select Year","1st Year","2nd Year","3rd Year","4th Year"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.style_spinner, arrayList);
        spinner1.setAdapter(arrayAdapter);

        String[] value1 = {"Choose Dept","CSE","MECH","IT","EEE","ECE"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(value1));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.style_spinner,arrayList1);
        spinner2.setAdapter(arrayAdapter1);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpref= getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedpref.edit();
                String Key2=sharedpref.getString("Key","");
                if(spinner1.getSelectedItem().toString().equals("Select Year")){
                    toasttext.setTextSize(40);
                    toasttext.setText("Select Year");
                    Image.setImageResource(R.drawable.ic_customtoast);
                    toast.show();
                    editor.putBoolean("Year",false);
                    editor.apply();
                }
                if(spinner1.getSelectedItem().toString().equals("1st Year")){
                    toasttext.setTextSize(20);
                    toasttext.setText("Not Have\nResources");
                    Image.setImageResource(R.drawable.ic_toastbad);
                    toast.show();
                    editor.putBoolean("Year",false);
                    editor.apply();
                }
                if(spinner1.getSelectedItem().toString().equals("2nd Year")){
                    editor.putBoolean("Year",true);
                    ref.child("users").child(Key2).child("Year").setValue("year2");
                    editor.apply();
                }
                if(spinner1.getSelectedItem().toString().equals("3rd Year")){
                    toasttext.setTextSize(20);
                    toasttext.setText("Sorry\nTimetable\nNot Available");
                    Image.setImageResource(R.drawable.ic_toastbad);
                    toast.show();
                    editor.putBoolean("Year",false);
                    editor.apply();
                }
                if(spinner1.getSelectedItem().toString().equals("4th Year")){
                    toasttext.setTextSize(20);
                    toasttext.setText("Resources\nNot\nAvailable");
                    Image.setImageResource(R.drawable.ic_toastbad);
                    toast.show();
                    editor.putBoolean("Year",false);
                    editor.apply();
                }
                if(spinner2.getSelectedItem().toString().equals("Choose Dept")){
                    toasttext.setTextSize(40);
                    toasttext.setText("Choose Dept");
                    Image.setImageResource(R.drawable.ic_toastbad);
                    toast.show();
                    editor.putBoolean("Year",false);
                    editor.apply();

                }
                else{
                    String Deptm=spinner2.getSelectedItem().toString();
                    ref.child("users").child(Key2).child("Dept").setValue(Deptm);
                    editor.putBoolean("Dept",true);
                    editor.apply();


                }
                if(TextUtils.isEmpty(Name.getText().toString())){
                    toasttext.setTextSize(40);
                    toasttext.setText("Enter Name");
                    Image.setImageResource(R.drawable.ic_customtoast);
                    toast.show();

                }
                else {
                    String userName = Name.getText().toString();
                    ref.child("users").child(Key2).child("Name").setValue(userName);
                    editor.putBoolean("Name1",true);
                    editor.apply();
                }
                if(sharedpref.getBoolean("Name1",false)&&sharedpref.getBoolean("Dept",false)&&sharedpref.getBoolean("Year",false)){
                    editor.putBoolean("Set", false);
                    editor.putBoolean("Keyok",false);
                    editor.apply();
                    toasttext.setTextSize(40);
                    toasttext.setText("Saved");
                    Image.setImageResource(R.drawable.ic_customtoast);
                    toast.show();
                    Fragment mfragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mfragment).commit();
                }

            }
        });
        return root;
    }
}
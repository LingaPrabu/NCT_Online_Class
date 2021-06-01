package com.cse.onlineclass.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.onlineclass.R;
import com.cse.onlineclass.ui.Settings.SettingsFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    TextView text;
    Button butt;
    Firebase mref;
    Firebase ref,newref;
    TextView name;
    int day,prd;
    String year,urlnew;
    String dept;
    TextView toasttext;
    ImageView Image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        LayoutInflater inflater1 = getLayoutInflater();
        View layout=inflater.inflate(R.layout.custom_toast,(ViewGroup)root.findViewById(R.id.custom_toast));
        Firebase.setAndroidContext(getContext());
        toasttext=layout.findViewById(R.id.textViewtoast);
        Image=layout.findViewById(R.id.imageView2);
        name=(TextView) root.findViewById(R.id.textViewName);
        text = (TextView) root.findViewById(R.id.textView5);
        butt = (Button) root.findViewById(R.id.btn1);
        final Toast toast= new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myKey", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("Set",true)){
            Fragment mfragment = new SettingsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mfragment).commit();
        }
        String Key=sharedPreferences.getString("Key","");
        newref=new Firebase("https://nct-online-class.firebaseio.com/users/"+Key+"/");
        newref.child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Name=dataSnapshot.getValue(String.class);
                name.setText(Name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        prd=findPeriod();
        Calendar cal=Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_WEEK);
        newref.child("Dept").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String depart=dataSnapshot.getValue(String.class);
                dept=depart;

                newref.child("Year").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Year=dataSnapshot.getValue(String.class);
                        year=Year;


                        mref=new Firebase("https://nct-online-class.firebaseio.com/TimeTable/"+year+"/"+dept+"/"+day+"/"+prd);
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String period=dataSnapshot.getValue(String.class);
                                if(period!=null) {
                                    text.setText("Get Ready For " +period);
                                }
                                ref=new Firebase("https://nct-online-class.firebaseio.com/TimeTable/"+year+"/"+dept+"/link/"+period);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String url=dataSnapshot.getValue(String.class);
                                        urlnew=url;
                                        butt.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (prd == -1 || day == 1) {
                                                    toasttext.setTextSize(20);
                                                    toasttext.setText("Enjoy The Day No Work Burden\nAnd\nNo Meetings At This Time");
                                                    Image.setImageResource(R.drawable.ic_customtoast);
                                                    toast.show();
                                                } else {
                                                    shareURL(urlnew);
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        return root;
    }

    private void shareURL(String urllk) {

        Intent Share = new Intent(Intent.ACTION_VIEW, Uri.parse(urllk));
        Share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Share.setPackage("com.android.chrome");
        try{
            startActivity(Share);
        }
        catch (ActivityNotFoundException ex)
        {
            Share.setPackage(null);
            startActivity(Share);
        }
        startActivity(Share);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static int findPeriod()
    {
        float ti=time();
        if (ti>09.20&&ti<10.20)
            return 1;
        else if (ti>10.20&&ti<11.25)
            return 2;
        else if (ti>11.35&&ti<12.45)
            return 3;
        else if (ti>13.50&&ti<14.50)
            return 4;
        else if (ti>14.50&&ti<16.00)
            return 5;
        else
            return -1;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static float time()
    {	float ca;
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("HH.mm");
        LocalDateTime n=LocalDateTime.now();
        ca=Float.parseFloat(dtf.format(n));
        return(ca);
    }
}
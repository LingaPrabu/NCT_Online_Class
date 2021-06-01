package com.cse.onlineclass.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cse.onlineclass.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

Firebase mref;
private WebView a;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Firebase.setAndroidContext(getContext());
        a=(WebView)root.findViewById(R.id.about_webview);
        a.setWebViewClient(new MyBrowser());

        a.getSettings().setLoadsImagesAutomatically(true);
        a.getSettings().setJavaScriptEnabled(true);
        mref=new Firebase("https://nct-online-class.firebaseio.com/About/");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String abouturl=dataSnapshot.getValue(String.class);
                a.loadUrl(abouturl);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





        return root;
    }

private class MyBrowser extends WebViewClient{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
}
package com.example.ll.demo02.fragment;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ll.demo02.R;
import com.example.ll.demo02.fragment.dummy.DummyContent;

public class FragmentActivity extends AppCompatActivity implements FragmentOne.OnFragmentInteractionListener,
        ItemFragment.OnListFragmentInteractionListener, PlusOneFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        Toast.makeText(this, item.details, Toast.LENGTH_SHORT).show();

    }
}

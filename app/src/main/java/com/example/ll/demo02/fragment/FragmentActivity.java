package com.example.ll.demo02.fragment;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.ll.demo02.R;
import com.example.ll.demo02.fragment.dummy.DummyContent;

public class FragmentActivity extends AppCompatActivity implements FragmentOne.OnFragmentInteractionListener,
        ItemFragment.OnListFragmentInteractionListener, PlusOneFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Activity由于各种原因重新创建时，savedInstanceState不为null，此时Fragment已经创建过了，没有必要再次重新创建
        // Activity被回收后重新启动导致Fragment重叠
        if (savedInstanceState == null) {
            PlusOneFragment fragment = new PlusOneFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.add_fragment, fragment);
            transaction.commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        Toast.makeText(this, item.details, Toast.LENGTH_SHORT).show();

    }
}

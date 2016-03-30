package com.example.ll.demo02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.ll.demo02.fragment.FragmentActivity;

public class MainActivity extends BaseActivity {


    private RecyclerView recycler_view_test_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view_test_rv = (RecyclerView) findViewById(R.id.recycler_view_test_rv);

        MyAdapter myAdapter = new MyAdapter(this);
        myAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
                switch (position + 1) {
                    case 1:
                        startActivity(new Intent(MainActivity.this,FragmentActivity.class));     //FragmentOneActivity
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.setAdapter(myAdapter);


    }


}

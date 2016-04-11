package com.example.ll.demo02.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ll.demo02.R;

public class FragmentOne extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btn_jump1,btn_jump2,btn_jump3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentOne() {
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentOne newInstance(String param1, String param2) {
        FragmentOne fragment = new FragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_activity, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_jump1 = (Button) getActivity().findViewById(R.id.btn_jump1);
        btn_jump2 = (Button) getActivity().findViewById(R.id.btn_jump2);
        btn_jump3 = (Button) getActivity().findViewById(R.id.btn_jump3);

        btn_jump1.setOnClickListener(this);
        btn_jump2.setOnClickListener(this);
        btn_jump3.setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_jump1:
                Toast.makeText(getActivity(), "btn_jump1", Toast.LENGTH_SHORT).show();


                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                ItemFragment itemgragment = new ItemFragment();
                fragmentTransaction.replace(R.id.add_fragment,itemgragment);
                fragmentTransaction.commit();

                break;
            case R.id.btn_jump2:
                Toast.makeText(getActivity(), "btn_jump1", Toast.LENGTH_SHORT).show();

                FragmentManager fmm = getFragmentManager();
                FragmentTransaction fragmentTra = fmm.beginTransaction();
                PlusOneFragment plusOne = new PlusOneFragment();
                fragmentTra.replace(R.id.add_fragment, plusOne);
                fragmentTra.commit();

                break;
            default:
                break;
        }

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.example.ll.demo02;

import android.Manifest;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.ll.demo02.appintro.FirstFragment;
import com.example.ll.demo02.appintro.FourthFragment;
import com.example.ll.demo02.appintro.SampleSlide;
import com.example.ll.demo02.appintro.SecondFragment;
import com.example.ll.demo02.appintro.ThirdFragmen;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class DefaultIntro extends AppIntro2 implements FirstFragment.OnFragmentInteractionListener,SecondFragment.OnFragmentInteractionListener
            ,ThirdFragmen.OnFragmentInteractionListener,FourthFragment.OnFragmentInteractionListener{

    private FirstFragment first_fragment = FirstFragment.newInstance("","");
    private SecondFragment second_fragment = SecondFragment.newInstance("","");
    private ThirdFragmen third_fragment = ThirdFragmen.newInstance("","");
    private FourthFragment fourth_fragment = FourthFragment.newInstance("","");


    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);
        addSlide(SampleSlide.newInstance(R.layout.fragment_first));
        addSlide(SampleSlide.newInstance(R.layout.fragment_second));
        addSlide(SampleSlide.newInstance(R.layout.fragment_third));
        addSlide(SampleSlide.newInstance(R.layout.fragment_fourth));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
//        addSlide(AppIntroFragment.newInstance("title", "description", R.drawable.ic_launcher, R.color.cardview_shadow_end_color));

        // OPTIONAL METHODS


        // SHOW or HIDE the statusbar
        showStatusBar(true);



        // Edit the color of the nav bar on Lollipop+ devices
        setNavBarColor("#3F51B5");

        // Turn vibration on and set intensity
        // NOTE: you will need to ask VIBRATE permission in Manifest if you haven't already
        setVibrate(true);
        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
//        setFadeAnimation(); // OR
        setZoomAnimation(); // OR
//        setFlowAnimation(); // OR
//        setSlideOverAnimation(); // OR
//        setDepthAnimation(); // OR
//        setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
    }


    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

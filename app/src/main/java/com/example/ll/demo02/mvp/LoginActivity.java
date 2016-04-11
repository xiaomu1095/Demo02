package com.example.ll.demo02.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.ll.demo02.R;
import com.example.ll.demo02.mvp.presenter.LoginPresenter;
import com.example.ll.demo02.mvp.presenter.LoginPresenterImpl;
import com.example.ll.demo02.mvp.view.LoginView;
import com.example.ll.demo02.utils.KeyBoardUtil;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private LoginPresenter presenter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.button);
        if (button != null){
            button.setOnClickListener(this);
        }

        presenter = new LoginPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        username.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, Main2Activity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        KeyBoardUtil.closeKeybord(v,LoginActivity.this);
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }
}

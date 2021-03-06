/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.api.login.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.login.event.RefreshTokenEvent;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 测试登录以及 token 获取
 */
public class LoginTestActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText name;

    @BindView(R.id.edit_password)
    EditText password;

    @BindView(R.id.text_state)
    TextView text_state;

    @OnClick(R.id.login)
    public void login(View view) {
        mDiycode.login(name.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.login_right)
    public void login_right(View view) {
        mDiycode.login("diytest", "slooptest");
    }

    @OnClick(R.id.login_error)
    public void login_error(View view) {
        mDiycode.login("diytest", "slooptest1");
    }

    @OnClick(R.id.logout)
    public void logout(View view) {
        mDiycode.logout();
        text_state.setText("登出");
    }

    @OnClick(R.id.get_token)
    public void get_token(View view) {
        Token token = mDiycode.getCacheToken();

        String state = "当前状态：";

        if (null != token) {
            state = state + "获取缓存 token 成功\n"
                    + "token type    = " + token.getToken_type() + "\n"
                    + "created at    = " + token.getCreated_at() + "\n"
                    + "expires in    = " + token.getExpires_in() + "\n"
                    + "access token  = " + token.getAccess_token() + "\n"
                    + "refresh token = " + token.getRefresh_token() + "\n";
        } else {
            state = state + "获取缓存 token 失败\n";
        }

        text_state.setText(state);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        String state = "当前状态：";
        if (event.isOk()) {
            Token token = event.getBean();
            Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
            state = state + "登录成功\n"
                    + "uuid          = " + event.getUUID() + "\n"
                    + "state         = " + event.getCode() + "\n"
                    + "state message = " + event.getCodeDescribe() + "\n"
                    + "token type    = " + token.getToken_type() + "\n"
                    + "created at    = " + token.getCreated_at() + "\n"
                    + "expires in    = " + token.getExpires_in() + "\n"
                    + "access token  = " + token.getAccess_token() + "\n"
                    + "refresh token = " + token.getRefresh_token() + "\n";
        } else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();

            state = state + "登录失败\n"
                    + "uuid          = " + event.getUUID() + "\n"
                    + "state         = " + event.getCode() + "\n"
                    + "state message = " + event.getCodeDescribe() + "\n";
        }

        text_state.setText(state);
    }


    @OnClick(R.id.refresh_token)
    public void resresh_token(View view) {
        mDiycode.refreshToken();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTokenEvent(RefreshTokenEvent event) {
        String state = "当前状态：";

        if (event.isOk()) {
            Token token = event.getBean();

            if (null != token) {
                state = state + "刷新 token 成功\n"
                        + "token type    = " + token.getToken_type() + "\n"
                        + "created at    = " + token.getCreated_at() + "\n"
                        + "expires in    = " + token.getExpires_in() + "\n"
                        + "access token  = " + token.getAccess_token() + "\n"
                        + "refresh token = " + token.getRefresh_token() + "\n";
            } else {
                state = state + "获取缓存token失败\n";
            }

        } else {
            state = state + "刷新 token 失败\n"
                    + "uuid          = " + event.getUUID() + "\n"
                    + "state         = " + event.getCode() + "\n"
                    + "state message = " + event.getCodeDescribe() + "\n";
        }
        text_state.setText(state);

        // text_state.setText("当前状态：刷新失败");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}

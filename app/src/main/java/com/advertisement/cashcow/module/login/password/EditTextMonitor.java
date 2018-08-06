package com.advertisement.cashcow.module.login.password;

import android.text.Editable;
import android.text.TextWatcher;

import io.reactivex.subjects.PublishSubject;

/**
 * 作者：吴国洪 on 2018/6/22
 * 描述：EditText监听
 */
public class EditTextMonitor implements TextWatcher {

    private PublishSubject<String> mPublishSubject;

    EditTextMonitor(PublishSubject<String> publishSubject) {
        mPublishSubject = publishSubject;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mPublishSubject.onNext(s.toString());
    }


}

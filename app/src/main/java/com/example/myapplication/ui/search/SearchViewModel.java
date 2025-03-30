package com.example.myapplication.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> _text = new MutableLiveData<>();

    public SearchViewModel() {
        _text.setValue("보관함"); // 초기 텍스트 값을 "보관함"으로 설정
    }

    public LiveData<String> getText() {
        return _text;
    }

//    public void updateText(String newText) {
//        _text.setValue(newText); // 텍스트 업데이트
//    }
}

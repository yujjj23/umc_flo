package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

//public class TrackFragment extends Fragment {
//
//    private ImageView ivExtraImage;
//
//    public TrackFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_track_list, container, false);
//    }
//}

public class TrackFragment extends Fragment {

    private ImageView ivExtraImage;

    public TrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_list, container, false);

        // ImageView 초기화
        ivExtraImage = view.findViewById(R.id.iv_extra_image);

        // 이미지 클릭 이벤트 설정
        ivExtraImage.setOnClickListener(v -> {
            if (ivExtraImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.btn_toggle_off).getConstantState()) {
                ivExtraImage.setImageResource(R.drawable.btn_toggle_on);  // 이미지 변경
            } else {
                ivExtraImage.setImageResource(R.drawable.btn_toggle_off);  // 원래 이미지로 변경
            }
        });

        return view;
    }
}


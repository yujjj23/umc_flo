package com.example.myapplication.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.Album;


public class TrackFragment extends Fragment {

    private ImageView ivExtraImage;
    private Album album; // 앨범 데이터를 저장할 변수

    public TrackFragment() {
        // Required empty public constructor
    }

    // 앨범 데이터를 설정하는 메서드
    public void setAlbumData(Album album) {
        this.album = album; // album 데이터를 설정
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

        // album 데이터로 UI 업데이트 (예시)
        if (album != null) {
            // 예시: 앨범의 이미지나 정보를 UI에 반영
            // 예: 앨범 이미지 설정 (앨범에 이미지를 추가했다고 가정)
            // ivAlbumCover.setImageResource(album.getImageResourceId()); // 해당 앨범의 이미지 리소스 설정
        }

        return view;
    }
}


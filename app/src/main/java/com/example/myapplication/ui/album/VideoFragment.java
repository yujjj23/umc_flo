package com.example.myapplication.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.Album;


public class VideoFragment extends Fragment {

    private Album album; // 앨범 데이터를 저장할 변수

    public VideoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        // album 데이터를 이용한 UI 업데이트 (예시)
        if (album != null) {
            // 예: 앨범에 대한 정보를 기반으로 UI를 업데이트하는 코드 추가
            // 예: 앨범 제목, 아티스트 등을 TextView에 설정
            // TextView albumTitleTextView = view.findViewById(R.id.albumTitle);
            // albumTitleTextView.setText(album.getTitle());
        }

        return view;
    }
}

package com.example.myapplication.ui.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SavedSongsAdapter;
import com.example.myapplication.SavedSongsFragment;
import com.example.myapplication.SearchPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.ui.search.SearchViewModel;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Fragment 초기화
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.likedSongsRecyclerView; // RecyclerView 연결

        // ViewModel 초기화
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // TabLayout과 ViewPager 설정
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;
        SearchPagerAdapter adapter = new SearchPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("저장한 곡");
                    break;
                case 1:
                    tab.setText("음악 파일");
                    break;
                case 2:
                    tab.setText("저장앨범");
                    break;
            }
        }).attach();

        binding.loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });

        // 전체선택 텍스트 클릭 시 BottomSheetDialog 실행
        binding.selectAllText.setOnClickListener(v -> showSelectAllBottomSheet());

        return root;
    }

    // BottomSheetDialog 띄우는 함수
    private void showSelectAllBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.bottom_sheet_select_all, null);

        // 각 버튼 클릭 시 동작 정의
        sheetView.findViewById(R.id.btn_image_1).setOnClickListener(v -> {
            Toast.makeText(getContext(), "버튼 1 클릭", Toast.LENGTH_SHORT).show();
        });

        sheetView.findViewById(R.id.btn_image_2).setOnClickListener(v -> {
            Toast.makeText(getContext(), "버튼 2 클릭", Toast.LENGTH_SHORT).show();
        });

        sheetView.findViewById(R.id.btn_image_3).setOnClickListener(v -> {
            Toast.makeText(getContext(), "버튼 3 클릭", Toast.LENGTH_SHORT).show();
        });

        // 전체 삭제 버튼 클릭 시 처리
        sheetView.findViewById(R.id.btn_image_4).setOnClickListener(v -> {
            handleDeleteAllLikedSongs();
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }



    private void handleDeleteAllLikedSongs() {
        // ViewPager에서 SavedSongsFragment 찾아서 함수 호출
        Fragment fragment = getChildFragmentManager().findFragmentByTag("f0"); // 첫 번째 탭
        if (fragment instanceof SavedSongsFragment) {
            ((SavedSongsFragment) fragment).deleteAllLikedSongs();
        }

        bottomSheetDialog.dismiss();
        Toast.makeText(getContext(), "저장한 곡을 모두 삭제했습니다", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();  // initViews 호출
    }

    private int getJwt() {
        SharedPreferences spf = getActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE);
        return spf.getInt("jwt", 0);
    }

    private void initViews() {
        int jwt = getJwt();
        if (jwt == 0) {
            binding.loginButton.setText("로그인");
            binding.loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            binding.loginButton.setText("로그아웃");
            binding.loginButton.setOnClickListener(v -> {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                logout();  // 로그아웃 호출
                startActivity(new Intent(getActivity(), MainActivity.class));
            });
        }
    }

    private void logout() {
        SharedPreferences spf = getActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.remove("jwt");
        editor.apply();
    }


}



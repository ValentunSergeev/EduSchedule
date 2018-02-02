package com.valentun.eduschedule.ui.screens.main.choose_type;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenChooseTypeBinding;
import com.valentun.eduschedule.ui.screens.main.IBarView;

public class ChooseTypeFragment extends MvpAppCompatFragment implements MvpView {

    @InjectPresenter
    ChooseTypePresenter presenter;

    private ScreenChooseTypeBinding binding;
    private IBarView barView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ScreenChooseTypeBinding.inflate(inflater, container, false);
        barView = (IBarView) getActivity();

        barView.getToolbarContainer().setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.chooseTypeNext.setOnClickListener(v -> {
            String type = getTypeFromButtonId(binding.radioGroup.getCheckedRadioButtonId());
            presenter.nextClicked(type);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        barView.getToolbarContainer().setVisibility(View.VISIBLE);
    }

    private String getTypeFromButtonId(int checkedRadioButtonId) {
        String type;

        switch (checkedRadioButtonId) {
            case R.id.type_student:
                type = Constants.TYPE_STUDENT;
                break;
            case R.id.type_teacher:
                type = Constants.TYPE_TEACHER;
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }

        return type;
    }
}

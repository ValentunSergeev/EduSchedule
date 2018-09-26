package com.valentun.eduschedule.ui.screens.main.info;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenInfoBinding;
import com.valentun.eduschedule.ui.screens.main.IBarView;

import net.cachapa.expandablelayout.ExpandableLayout;

public class InfoFragment extends MvpAppCompatFragment implements MvpView {
    private ScreenInfoBinding binding;
    private IBarView barView;

    @InjectPresenter
    InfoPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ScreenInfoBinding.inflate(inflater, container, false);
        barView = (IBarView) getActivity();

        barView.setUpArrowEnabled(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.expandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state == ExpandableLayout.State.EXPANDED) {
                binding.infoScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        binding.infoExpander.setOnClickListener(v -> {
            binding.expandableLayout.toggle();

            updateExpandDrawable();
        });

        binding.helpInfo.setOnClickListener(v -> {
            copyWallet();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.info);
        updateExpandDrawable();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        barView.setUpArrowEnabled(false);
    }

    private void copyWallet() {
        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(Constants.WALLET_CLIP_LABEL, Constants.WALLET_VALUE);
        manager.setPrimaryClip(data);

        presenter.copyClicked();
    }

    private void updateExpandDrawable() {
        int drawableRes = binding.expandableLayout.isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down;
        binding.infoExpander.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0);
    }
}

package com.valentun.eduschedule.ui.common.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import io.reactivex.Observable;

public class OptionsDialog {
    private String[] items;
    private Integer titleId;

    public OptionsDialog(String... items) {
        this.items = items;
    }

    public OptionsDialog(int title, String... items) {
        this.titleId = title;
        this.items = items;
    }

    public Observable<Integer> show(Context context) {
        return Observable.create(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if (titleId != null) {
                builder.setTitle(titleId);
            }

            builder.setItems(items, (dialog, which) -> e.onNext(which));
            builder.show();
        });
    }
}

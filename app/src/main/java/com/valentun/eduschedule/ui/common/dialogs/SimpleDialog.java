package com.valentun.eduschedule.ui.common.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.valentun.eduschedule.MyApplication;

import io.reactivex.Observable;

@SuppressWarnings("ALL")
public class SimpleDialog {
    public static final int POSITIVE_CLICK = 0;
    public static final int NEGATIVE_CLICK = 1;
    public static final int NEUTRAL_CLICK = 2;
    private boolean notCloseOnClick = false;

    protected String positiveText;
    protected String negativeText;
    protected String neutralText;

    protected String title;
    protected String message;

    protected SimpleDialog(){}

    public Observable<Integer> show(Context context) {
        return Observable.create(source -> {
           AlertDialog.Builder builder =  new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(title)
                    .setMessage(message);
            if (positiveText != null)
                builder.setPositiveButton(positiveText, (dialogInterface, i) -> {
                    source.onNext(POSITIVE_CLICK);
                });
            if (negativeText != null)
                builder.setNegativeButton(negativeText, (dialogInterface, i) ->
                    source.onNext(NEGATIVE_CLICK));
            if (neutralText != null)
                builder.setNeutralButton(neutralText, ((dialogInterface, i) ->
                        source.onNext(NEUTRAL_CLICK)));

            AlertDialog dialog = builder.show();
        });
    }

    public static class Builder {
        private SimpleDialog dialog = new SimpleDialog();

        public Builder setPositiveText(@StringRes int textRes) {
            dialog.positiveText = MyApplication.INSTANCE.getString(textRes);
            return this;
        }

        public Builder setNegativeText(@StringRes int textRes) {
            dialog.negativeText = MyApplication.INSTANCE.getString(textRes);
            return this;
        }

        public Builder setPositiveText(String text) {
            dialog.positiveText = text;
            return this;
        }

        public Builder setNegativeText(String text) {
            dialog.negativeText = text;
            return this;
        }

        public Builder setTitle(String text) {
            dialog.title = text;
            return this;
        }

        public Builder setTitle(@StringRes int textRes) {
            dialog.title = MyApplication.INSTANCE.getString(textRes);
            return this;
        }

        public Builder setMessage(String text) {
            dialog.message = text;
            return this;
        }

        public Builder setMessage(@StringRes int textRes) {
            dialog.message = MyApplication.INSTANCE.getString(textRes);
            return this;
        }

        public SimpleDialog build() {
            return dialog;
        }
    }
}

package cn.lvsong.lib.library.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.IdRes;

import java.lang.ref.WeakReference;

/**
 * Created by Jooyer on 2017/7/22
 */

public class JDialogViewHelper {
    private Context mContext;
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;
     OnJAlertDialogCLickListener mOnJAlertDialogCLickListener;
    public JDialogViewHelper(Context context, int viewLayoutResId) {
        mViews = new SparseArray<>();
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public JDialogViewHelper(Context context, View view) {
        mViews = new SparseArray<>();
        mContentView = view;
    }

    public void setText(@IdRes int viewId, CharSequence charSequence) {
        TextView textView = getView(viewId);
        if (null != textView) {
            textView.setText(charSequence);
        }
    }

    public void setOnClick(final int position, @IdRes int viewId) {
        View view = getView(viewId);
        if (null != view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnJAlertDialogCLickListener){
                        mOnJAlertDialogCLickListener.onClick(v,position);
                    }
                }
            });
        }
    }


    public View getContentView() {
        return mContentView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = null;
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        if (null != viewWeakReference) {
            view = viewWeakReference.get();
        }
        if (null == view) {
            view = mContentView.findViewById(viewId);
            if (null != view)
                mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }


    public void setOnJAlertDialogCLickListener(OnJAlertDialogCLickListener onJAlertDialogCLickListener) {
        mOnJAlertDialogCLickListener = onJAlertDialogCLickListener;
    }
}

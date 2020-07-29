package cn.lvsong.lib.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ProjectName: android
 * @ClassName: NineImageAdapter
 * @Description: NineImageLayout适配器
 * @Author: ChenYangQi
 * @CreateDate: 2020/6/3 17:48
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class NineImageAdapter {
    public abstract int getItemCount();

    public abstract View createView(LayoutInflater inflater, ViewGroup parent, int position);

    public abstract void bindView(View view, int position);

    public void OnItemClick(int position, View view) {

    }
}

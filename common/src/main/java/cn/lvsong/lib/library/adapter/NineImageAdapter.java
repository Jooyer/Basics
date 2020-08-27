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

    /**
     * 点击事件
     * @param position --> 点击位置,从0开始计算
     * @param view --> 当前点击的Viwe
     */
    public void OnItemClick(int position, View view) {

    }
}

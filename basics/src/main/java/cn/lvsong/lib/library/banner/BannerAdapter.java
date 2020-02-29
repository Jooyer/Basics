package cn.lvsong.lib.library.banner;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Desc: 可以使用这个 Adapter 也可以不使用,随意
 * Author: Jooyer
 * Date: 2019-08-28
 * Time: 14:09
 */
public abstract class BannerAdapter<T> extends RecyclerView.Adapter<BannerHolder> {

    public List<T> mData;
    private int mLayoutId;

    public BannerAdapter(List<T> data, int layoutId) {
        mData = data;
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false));
    }

    @Override
    abstract public void onBindViewHolder(@NonNull BannerHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

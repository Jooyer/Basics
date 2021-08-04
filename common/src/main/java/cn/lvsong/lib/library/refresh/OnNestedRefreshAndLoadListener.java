package cn.lvsong.lib.library.refresh;

import androidx.annotation.NonNull;

/**
 * Desc: 刷新加载回调
 * Author: Jooyer
 * Date: 2019-10-22
 * Time: 15:10
 */
public abstract class OnNestedRefreshAndLoadListener {

    /**
     * 开始刷新
     */
    public abstract void onRefresh(@NonNull NestedRefreshLayout refreshLayout);

    /**
     * 开始加载更多
     */
    public void onLoad(@NonNull NestedRefreshLayout refreshLayout) {

    }

    /**
     * 当用户拖动时,对拖动距离进行回调
     *
     * @param distance          --> 滑动距离,  distance> 0 向上滑动(加载操作) , distance < 0 向下滑动(刷新操作)
     * @param headerMaxDistance --> HeaderView 最大移动距离
     * @param footerMaxDistance --> FooterView 最大移动距离
     */
    public void onMoveDistance(@NonNull NestedRefreshLayout refreshLayout, int headerMaxDistance, int footerMaxDistance, int distance) {

    }

    /**
     * 有些时候需要知道头部动画执行结束时,可以使用此方法
     */
    public void onRefreshAnimatorEnd() {
    }
}

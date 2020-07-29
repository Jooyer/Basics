package cn.lvsong.lib.library.refresh;

/**
 * Desc: 刷新加载回调
 * Author: Jooyer
 * Date: 2019-10-22
 * Time: 15:10
 */
public abstract class OnRefreshAndLoadListener {

    public abstract void onRefresh(PowerRefreshLayout refreshLayout);

    public void onLoad(PowerRefreshLayout refreshLayout) {

    }

    /**
     * 当用户拖动时,对拖动距离进行回调
     * @param distance > 0 向上滑动(加载操作) , distance < 0 向下滑动(刷新操作)
     * @param headerMaxDistance  --> HeaderView 最大移动距离
     * @param footerMaxDistance  --> FooterView 最大移动距离
     */
    public void onMoveDistance(PowerRefreshLayout refreshLayout, int headerMaxDistance,int footerMaxDistance,int distance){

    }

}

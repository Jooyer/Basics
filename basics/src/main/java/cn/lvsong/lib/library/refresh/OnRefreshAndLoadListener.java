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

}

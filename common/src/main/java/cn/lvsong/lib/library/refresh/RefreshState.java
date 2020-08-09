package cn.lvsong.lib.library.refresh;

/**
 * Desc: 刷新和加载时各个状态
 * Author: Jooyer
 * Date: 2019-10-24
 * Time: 11:54
 */
public interface RefreshState {
    int DEFAULT = 1;//默认情况
    int HEADER_AUTO= 2;//自动刷新,此时需要对 HeaderView 文案或者其他动效进行更改,否则会显示下拉刷新的状态
    int HEADER_DRAG = 3;//下拉刷新
    int HEADER_RELEASE = 4;//释放立即刷新
    int HEADER_REFRESHING = 5;//正在刷新
    int HEADER_COMPLETED = 6;//刷新完成
    int HEADER_FAILURE = 7;//刷新完成

    int FOOTER_PULL = 8;//加载更多
    int FOOTER_RELEASE = 9;//释放加载更多
    int FOOTER_LOADING = 10;//正在加载
    int FOOTER_COMPLETED = 11;//加载完成
    int FOOTER_NO_MORE = 12;//没有更多数据
    int FOOTER_FAILURE = 13;//没有更多数据

}

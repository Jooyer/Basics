package cn.lvsong.lib.library.refresh;

/**
 * Desc: 刷新和加载时各个状态
 * Author: Jooyer
 * Date: 2019-10-24
 * Time: 11:54
 */
public interface RefreshState {
    int DEFAULT = 0x010;//默认情况
    int HEADER_AUTO= 0x011;//自动刷新,此时需要对 HeaderView 文案或者其他动效进行更改,否则会显示下拉刷新的状态
    int HEADER_DRAG = 0x012;//下拉刷新
    int HEADER_RELEASE = 0x013;//释放立即刷新
    int HEADER_READY = 0x014;//准备刷新
    int HEADER_REFRESHING = 0x015;//正在刷新
    int HEADER_COMPLETED = 0x016;//刷新完成
    int HEADER_CANCEL = 0x017;//刷新取消

    int FOOTER_PULL = 0x018;//加载更多
    int FOOTER_RELEASE = 0x019;//释放加载更多
    int FOOTER_READY = 0x020;//准备加载
    int FOOTER_LOADING = 0x021;//正在加载
    int FOOTER_COMPLETED = 0x022;//加载完成
    int FOOTER_CANCEL = 0x023;//加载取消
    int FOOTER_NO_MORE = 0x024;//没有更多数据

}

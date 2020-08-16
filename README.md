# 在common库中,封装了大家常用的控件,有部分控件直接使用其他大佬的,在此感谢诸位大佬!感谢开源!如果用得上记得点赞收藏



[toc]

# UnNestedRefreshLayout

## **用法:**  
具体参考: cn.lvsong.lib.demo.UnNestedRefreshActivity
```kotlin
        nrl_container.setOnRefreshAndLoadListener(object : OnNormalRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: UnNestedRefreshLayout) {
                Log.e("Test", "onRefresh==============")
                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    mBaseAdapter?.notifyDataSetChanged()
                    nrl_container.setFinishRefresh(true)
                }, 3000)
            }

            override fun onLoad(refreshLayout: UnNestedRefreshLayout) {
                Log.e("Test", "onLoad==============")
                for (i in data.size until data.size + 5) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    mBaseAdapter?.notifyDataSetChanged()
                    nrl_container.setFinishLoad(true)
                    // 设置没有数据了
                    nrl_container.setNoMoreData(true)
                }, 1000)
            }
        })
```
## **属性介绍:**

| 属性名称                | 取值类型 | 作用和取值                                                   |
| ----------------------- | -------- | :----------------------------------------------------------- |
| unrl_no_more_first_time | integer  | 第一次加载时,如果没有更多数据,底部显示什么     1: 不显示 Footer,禁用加载更多了,默认值     2: 显示没有更多数据 |
| unrl_scroll_time        | integer  | 当成功刷新/成功加载后,隐藏Header/Footer滚动时间,默认800ms    |
|                         |          |                                                              |

## **公共方法:**

| 方法名称                            | 作用                    |
| ----------------------------------- | ----------------------- |
| setRefreshable(boolean refreshable) | 是否可以刷新,默认是true |
| setLoadable(boolean loadable)       | 是否可以加载更多,默认是true |
| setFinishRefresh(boolean isSuccess) | 是否成功刷新 |
| setFinishRefresh(boolean isSuccess, long delay) | 关闭动画延迟时间,通常调用上一个方法默认800ms,可以自定义 |
| setFinishLoad(boolean isSuccess) | 是否加载成功 |
| setFinishLoad(boolean isSuccess, long delay) | 关闭动画延迟时间,通常调用上一个方法默认800ms,可以自定义 |
| setNoMoreData(boolean noMoreData) | 是否有更多加载数据 |
| setAutoRefresh() | 如果需要进入页面自动刷新则调用此方法 |
| addHeader(@NonNull View header) | 添加自定义Header,需实现 ==IHeaderWrapper== 接口,有默认值 |
| addFooter(@NonNull View footer) | 添加自定义Footer,需实现 ==IFooterWrapper== 接口,有默认值 |
| setRefreshRatio(float refreshRatio) | 设置头部可以下拉的比率, 默认是2倍HeaderView高度 |
| setLoadRatio(float loadRatio) | 设置底部可以下拉的比率, 默认是2倍FooterView高度 |
| setOnRefreshAndLoadListener(OnNestedRefreshAndLoadListener listener) | 设置刷新与加载回调 |


# NestedRefreshLayout
## **用法:** 

具体参考: cn.lvsong.lib.demo.LazyFragment

```kotlin
   snl_container.setOnRefreshAndLoadListener(object : OnNestedRefreshAndLoadListener() {
            override fun onRefresh(refreshLayout: NestedRefreshLayout) {
                data.clear()
                for (i in 0 until 10) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.setFinishRefresh(true)
                }, 1000)
            }

            override fun onLoad(refreshLayout: NestedRefreshLayout) {
                for (i in data.size until data.size + 6) {
                    data.add("-----$i------")
                }
                refreshLayout.postDelayed({
                    rv_list.adapter?.notifyDataSetChanged()
                    snl_container.setFinishLoad(true)
                }, 1000)
            }
        })
```



## **属性介绍:** 

| 属性名称               | 取值类型 | 作用和取值                                                   |
| ---------------------- | -------- | :----------------------------------------------------------- |
| nrl_no_more_first_time | integer  | 第一次加载时,如果没有更多数据,底部显示什么     1: 不显示 Footer,禁用加载更多了,默认值     2: 显示没有更多数据 |
| nrl_scroll_time        | integer  | 当成功刷新/成功加载后,隐藏Header/Footer滚动时间,默认800ms    |
|                        |          |                                                              |

## **公共方法:** 

| 方法名称                                                     | 作用                                                     |
| ------------------------------------------------------------ | -------------------------------------------------------- |
| setRefreshable(boolean refreshable)                          | 是否可以刷新,默认是true                                  |
| setLoadable(boolean loadable)                                | 是否可以加载更多,默认是true                              |
| setFinishRefresh(boolean isSuccess)                          | 是否成功刷新                                             |
| setFinishRefresh(boolean isSuccess, long delay)              | 关闭动画延迟时间,通常调用上一个方法默认800ms,可以自定义  |
| setFinishLoad(boolean isSuccess)                             | 是否加载成功                                             |
| setFinishLoad(boolean isSuccess, long delay)                 | 关闭动画延迟时间,通常调用上一个方法默认800ms,可以自定义  |
| setNoMoreData(boolean noMoreData)                            | 是否有更多加载数据                                       |
| setAutoRefresh()                                             | 如果需要进入页面自动刷新则调用此方法                     |
| addHeader(@NonNull View header)                              | 添加自定义Header,需实现 ==IHeaderWrapper== 接口,有默认值 |
| addFooter(@NonNull View footer)                              | 添加自定义Footer,需实现 ==IFooterWrapper== 接口,有默认值 |
| setRefreshRatio(float refreshRatio)                          | 设置头部可以下拉的比率, 默认是2倍HeaderView高度          |
| setLoadRatio(float loadRatio)                                | 设置底部可以下拉的比率, 默认是2倍FooterView高度          |
| setOnRefreshAndLoadListener(OnNestedRefreshAndLoadListener listener) | 设置刷新与加载回调                                       |


# StatusManager
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# Banner
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# CustomToolbar
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# CustomMenu
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# CustomSearchView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# LeftImgAndRightTextView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# TopImgAndBottomTextView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# BubbleLinearLayout
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# JAlertDialog
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# SmartPopupWindow
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# ArcView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# DanceView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# ArrangeView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# BackArrowView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# BageView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# CloseView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 


# CountDownView
## **用法:** 
## **属性介绍:** 
## **公共方法:** 














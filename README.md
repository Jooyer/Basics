



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
| ----------------------- | -------- | ------------------------------------------------------------ |
| unrl_no_more_first_time | integer  | 第一次加载时,如果没有更多数据,底部显示什么     1: 不显示 Footer,禁用加载更多了,默认值     2: 显示没有更多数据 |
| unrl_scroll_time        | integer  | 当成功刷新/成功加载后,隐藏Header/Footer滚动时间,默认800ms    |

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
| ---------------------- | -------- | ------------------------------------------------------------ |
| nrl_no_more_first_time | integer  | 第一次加载时,如果没有更多数据,底部显示什么     1: 不显示 Footer,禁用加载更多了,默认值     2: 显示没有更多数据 |
| nrl_scroll_time        | integer  | 当成功刷新/成功加载后,隐藏Header/Footer滚动时间,默认800ms    |

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

具体参考: cn.lvsong.lib.demo.StatusActivity

```kotlin
    val statusManager = StatusManager.newBuilder(this)
            .contentView(R.layout.xxx) // 这里加载的是自己的布局文件
            .loadingView(getLoadingViewLayoutId())
            .emptyDataView(getEmptyDataViewLayoutId())
         .netWorkErrorView(getNetWorkErrorViewLayoutId())
            .errorView(getErrorViewLayoutId())
            .retryViewId(R.id.view_retry_load_data)
            .setLoadingViewBackgroundColor(Color.RED)
            .onRetryListener(this)
            .build()
        statusManager.showLoading()
setContentView(setContentView)

```



## **属性介绍:**  

| 属性名称   | 取值类型 | 作用和取值           |
| ---------- | -------- | -------------------- |
| mDelayTime | long     | 延迟显示 ContentView |



## **公共方法:**  

| 构建StatusManager方法名称                                    | 作用                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| loadingView(@LayoutRes loadingLayoutResId: Int)              | 设置loading布局                                              |
| netWorkErrorView(@LayoutRes netWorkErrorLayout: Int)         | 设置网络错误布局                                             |
| emptyDataView(@LayoutRes noDataLayout: Int)                  | 设置空数据布局                                               |
| errorView(@LayoutRes errorLayout: Int)                       | 设置加载错误布局                                             |
| contentView(contentLayoutView: View)                         | 设置内容View, LayoutInflater.from(context)     .inflate(R.layout.activity_xxx, null) |
| contentView(@LayoutRes contentLayout: Int)                   | 设置内容View,其实就是自己编写的activity_xxx/fragment_xxx     |
| netWorkErrorRetryViewId(@IdRes netWorkErrorRetryViewId: Int) | 设置网络加载异常重试按钮ID                                   |
| emptyDataRetryViewId(@IdRes emptyDataRetryViewId: Int)       | 设置空数据时重试按钮ID                                       |
| errorRetryViewId(@IdRes errorRetryViewId: Int)               | 设置错误时重试按钮ID                                         |
| retryViewId(@IdRes retryViewId: Int)                         | 设置重试按钮ID, 此时需要上面三个需要重试的界面在布局中ID必须和此设置的ID一致 |
| setLoadingViewBackgroundColor(@ColorRes bgColor: Int)        | 如果使用默认的菊花loading,可以通过此方法设置loading背景色    |
| delayTime(delayTime: Long)                                   | 当调用 statusMananger.showContent()时延迟多久显示contentView,默认是1200ms |
| onRetryListener(onRetryListener: OnRetryListener)            | 点击重试按钮后回调,里面做了仿抖动处理                        |

-------------


| Statusmanager方法名称         | 作用                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| setTransY(transY: Int)        | 将loading,error等view上下移动,如果你希望loading时显示自定义的toolbar,那么可以试试此方法 |
| showLoading()                 | 显示loading                                                  |
| showContent()                 | 显示内容,默认延迟显示1200ms                                  |
| delayShowContent(delay: Long) | 延迟显示内容                                                 |
| showEmptyData()               | 显示空视图                                                   |
| showError()                   | 显示错误视图                                                 |
| showNetWorkError()            | 显示网络异常                                                 |
| getRootLayout()               | 返回StatusManager容器                                        |




# Banner
## **用法:** 

具体参考: cn.lvsong.lib.demo.BannerActivity

步骤一: 在xml中引入

```xml
    <cn.lvsong.lib.library.banner.BannerLayout
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        app:banner_indicator_height="10dp"
        app:banner_indicator_margin="3dp"
        app:banner_normal_indicator_drawable="@drawable/normal"
        app:banner_select_indicator_drawable="@drawable/select"
        app:banner_loop_time="3000"
        app:banner_show_indicator="true"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        />
```

步骤二: 继承自BannerAdapter实现 adapter

```kotlin
class CustomAdapter(data: List<String>, layoutId: Int) :
    BannerAdapter<String>(data, layoutId) {
    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        holder.itemView.findViewById<AppCompatTextView>(R.id.tv_position).text = "$position"
        ImageLoad.loader.loadImage(holder.itemView.findViewById<AppCompatImageView>(R.id.iv_banner), mData[position])
    }

}
```

步骤三:

```kotlin
  banner.setBannerAdapter(adapter, DensityUtil.dp2pxRtInt(10)) // 第二参数表,每一个item间隔
```

## **属性介绍:** 
| 属性名称                         | 取值类型             | 取值和作用                  |
| -------------------------------- | -------------------- | --------------------------- |
| banner_indicator_height          | reference\|dimension | 指示器高度                  |
| banner_indicator_margin          | reference\|dimension | 指示器内部 View  左右Margin |
| banner_show_indicator            | boolean              | 是否显示指示器              |
| banner_loop_time                 | integer              | 轮播时间间隔,默认3000ms     |
| banner_select_indicator_drawable | reference            | 指示器选中状态图片          |
| banner_normal_indicator_drawable | reference            | 指示器默认状态图片          |



## **公共方法:**  

| 方法名称                                                     | 作用                                          |
| ------------------------------------------------------------ | --------------------------------------------- |
| fun setBannerAdapter(         adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,         spaceWidth: Int = 0 ) | 绑定banner适配器                              |
| onPause()                                                    | 暂停滑动                                      |
| onResume()                                                   | 继续滑动                                      |
| onStop()                                                     | 停止滑动                                      |
| setOnPositionChangeListener(positionChangeListener: OnPositionChangeListener) | 如果需要知道当前显示的下标,可以通过此回调获取 |




# CustomToolbar
## **用法:**  

具体参考: cn.lvsong.lib.demo.CustomToolbarActivity ,

PS: 一般右侧显示一个文本按钮(或者2个图标按钮,或者更多按钮),如果需要同时设置多个,发现重叠,有margin_right的属性,可以通过调节大小

```xml
  <cn.lvsong.lib.library.view.CustomToolbar
        android:id="@+id/ct_8"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_50"
        app:ct_center_text_color="@color/color_333333"
        app:ct_center_text_info="隐藏底部分割线"
        app:ct_center_text_size="@dimen/text_size_16"
        app:ct_bottom_divider_visible="false"
        />
```

如上在xml中写入,就可以完成点击左侧返回并设置title

## **属性介绍:**  

| 属性名称                     | 取值类型           | 取值和作用                                                   |
| ---------------------------- | ------------------ | ------------------------------------------------------------ |
| ct_left_arrow_visible        | boolean            | 左侧箭头是否显示                                             |
| ct_left_arrow_width          | dimension\|integer | 左侧箭头宽度                                                 |
| ct_left_arrow_height         | dimension\|integer | 左侧箭头高度                                                 |
| ct_left_arrow_padding        | dimension\|integer | 左侧箭头 Padding(上下左右)                                   |
| ct_left_arrow_left_margin    | dimension\|integer | 左侧箭头左侧 Margin                                          |
| ct_left_arrow_color          | color\|reference   | 左侧箭头的颜色                                               |
| ct_left_arrow_style          | enum               | 左侧箭头模式, material_design(Material Design),wechat_design(微信风格) |
| ct_left_text_visible         | boolean            | 左侧文本是否显示                                             |
| ct_left_text_info            | reference\|string  | 左侧文本                                                     |
| ct_left_text_size            | dimension          | 左侧文本大小                                                 |
| ct_left_text_color           | color\|reference   | 左侧文本颜色                                                 |
| ct_left_text_left_margin     | dimension\|integer | 左侧文本左侧 Margin                                          |
| ct_center_text_info          | reference\|string  | 中间文本                                                     |
| ct_center_text_size          | dimension          | 中间文本大小                                                 |
| ct_center_text_color         | color\|reference   | 中间文本颜色                                                 |
| ct_right_text_visible        | boolean            | 右侧文本是否可见                                             |
| ct_right_text_info           | reference\|string  | 右侧文本                                                     |
| ct_right_text_size           | dimension          | 右侧文本大小                                                 |
| ct_right_text_color          | color\|reference   | 右侧文本颜色                                                 |
| ct_right_text_right_margin   | dimension\|integer | 右侧文本右侧 Margin                                          |
| ct_right_text_bold           | boolean            | 右侧文本是否加粗,类似苹果中黑效果,默认是加粗的               |
| ct_right_image_visible       | boolean            | 右侧图片1是否可见                                            |
| ct_right_image_drawable      | reference          | 右侧图片1 Drawable                                           |
| ct_right_image_width         | dimension\|integer | 右侧图片1宽度                                                |
| ct_right_image_height        | dimension\|integer | 右侧图片1高度                                                |
| ct_right_image_padding       | dimension\|integer | 右侧图片1 Padding(上下左右)                                  |
| ct_right_image_right_margin  | dimension\|integer | 右侧图片1右侧 rightMargin                                    |
| ct_right_image2_visible      | boolean            | 右侧图片2是否可见                                            |
| ct_right_image2_drawable     | reference          | 右侧图片2 Drawable                                           |
| ct_right_image2_width        | dimension\|integer | 右侧图片2宽度                                                |
| ct_right_image2_height       | dimension\|integer | 右侧图片2高度                                                |
| ct_right_image2_padding      | dimension\|integer | 右侧图片2 Padding(上下左右)                                  |
| ct_right_image2_right_margin | dimension\|integer | 右侧图片2右侧 rightMargin                                    |
| ct_right_mav_visible         | boolean            | 右侧更多按钮是否可见,默认不可见                              |
| ct_right_mav_width           | dimension\|integer | 右侧更多按钮宽度,主要是设置点击范围                          |
| ct_right_mav_height          | dimension\|integer | 右侧更多按钮高度,主要是设置点击范围                          |
| ct_right_mav_right_margin    | dimension\|integer | 右侧更多按钮rightMargin                                      |
| ct_right_mav_color           | color\|reference   | 右侧更多按钮的颜色                                           |
| ct_right_mav_dot_radius      | dimension\|integer | 右侧更多按钮大小,圆点半径                                    |
| ct_right_mav_orientation     | enum               | 右侧更多按钮排列方向，水平或垂直, horizontal(水平),vertical(垂直),默认垂直 |
|                              |                    |                                                              |

## **公共方法:**  

| 方法名称                                                     | 作用                                 |
| ------------------------------------------------------------ | ------------------------------------ |
| setRightTextVisible(visable: Int)                            | 右侧文本是否显示                     |
| setRightImageVisible(visible: Int)                           | 右侧图一显示                         |
| setRightImage2Visible(visible: Int)                          | 右侧图二显示                         |
| setRightText(text: String)                                   | 设置右侧文字                         |
| setRightImage(resource: Int)                                 | 设置右侧图一图片                     |
| setRightImage2(resource: Int)                                | 设置右侧图二图片                     |
| setCenterText(text: String)                                  | 设置标题                             |
| setLeftText(text: String)                                    | 设置左侧文字                         |
| setLeftArrowVisible(visible: Int)                            | 左侧箭头是否可见                     |
| setLeftArrowClickListener(listener: OnClickListener)         | 左侧箭头点击,默认有点击,设置则会覆盖 |
| setLeftTextViewClickListener(listener: View.OnClickListener) | 左侧文本点击,默认有点击,设置则会覆盖 |
| setRightImageListener(listener: View.OnClickListener)        | 右侧图一点击                         |
| setRightImage2Listener(listener: View.OnClickListener)       | 右侧图二点击                         |
| setMoreViewListener(listener: View.OnClickListener)          | 右侧更多点击                         |
| setRightTextListener(listener: View.OnClickListener)         | 右侧文本点击                         |




# CustomMenu
## **用法:** 

具体参考: cn.lvsong.lib.demo.CustomMenuActivity

```xml
        <cn.lvsong.lib.library.view.CustomMenu
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:cm_bottom_divider_color="@color/color_2878FF"
            app:cm_bottom_divider_left_margin="@dimen/height_10"
            app:cm_bottom_divider_visible="true"
            app:cm_left_image_drawable="@drawable/ic_baseline_alarm_add_24"
            app:cm_left_image_margin="@dimen/padding_10"
            app:cm_left_text_color="@color/color_333333"
            app:cm_left_text_info="左侧图片右侧图片+小红点"
            app:cm_left_text_left_margin="@dimen/padding_5"
            app:cm_right_near_image_drawable="@drawable/select"
            app:cm_right_near_image_height="@dimen/height_10"
            app:cm_right_near_image_right_margin="@dimen/padding_2"
            app:cm_right_near_image_top_margin="@dimen/padding_12"
            app:cm_right_near_image_visible="true"
            app:cm_right_near_image_width="@dimen/width_10"
            app:cm_right_text_info="版本更新"
            app:cm_right_text_right_margin="@dimen/padding_2" />
```

## **属性介绍:**  

| 属性名称                         | 取值类型           | 取值和作用                                                   |
| -------------------------------- | ------------------ | ------------------------------------------------------------ |
| cm_left_image_visible            | boolean            | 最左侧图标是否可见                                           |
| cm_left_image_drawable           | reference          | 最左侧图标的 drawable                                        |
| cm_left_image_width              | dimension\|integer | 最左侧图标宽度                                               |
| cm_left_image_height             | dimension\|integer | 最左侧图标高度                                               |
| cm_left_image_left_margin        | dimension\|integer | 最左侧图标LeftMargin                                         |
| cm_left_text_info                | reference\|string  | 紧靠左侧的文本                                               |
| cm_left_text_size                | dimension\|integer | 紧靠左侧的文本大小                                           |
| cm_left_text_color               | color\|reference   | 紧靠左侧的文本颜色                                           |
| cm_left_text_left_margin         | dimension\|integer | 紧靠左侧的文本LeftMargin                                     |
| cm_right_text_visible            | boolean            | 紧靠右侧的文本是否可见                                       |
| cm_right_text_info               | reference\|string  | 紧靠右侧的文本                                               |
| cm_right_text_size               | dimension\|integer | 紧靠右侧的文本大小                                           |
| cm_right_text_color              | color\|reference   | 紧靠右侧的文本颜色                                           |
| cm_right_text_right_margin       | dimension\|integer | 紧靠右侧的文本RightMargin                                    |
| cm_right_near_image_visible      | boolean            | 紧靠右侧图标是否可见                                         |
| cm_right_near_image_drawable     | reference          | 紧靠右侧图标的 drawable                                      |
| cm_right_near_image_width        | dimension\|integer | 紧靠右侧图标宽度                                             |
| cm_right_near_image_height       | dimension\|integer | 紧靠右侧图标高度                                             |
| cm_right_near_image_right_margin | dimension\|integer | 紧靠右侧图标rightMargin,距离最右侧                           |
| cm_right_near_image_top_margin   | dimension\|integer | 紧靠右侧图标顶部Margin,距离最上面,默认0dp                    |
| cm_right_arrow_visible           | boolean            | 最右侧箭头的是否可见                                         |
| cm_right_arrow_color             | color\|reference"  | 最右侧箭头的 颜色                                            |
| cm_right_arrow_padding           | dimension\|integer | 最右侧箭头 Padding(上下左右)                                 |
| cm_right_arrow_orientation       | enum               | 最右侧箭头方向, 向左(left)或向右(right),默认right            |
| cm_right_arrow_style             | enum               | 最右侧箭头模式,material_design(Material Design),wechat_design(微信风格),默认material_design |
| cm_right_arrow_width             | dimension\|integer | 最右侧箭头宽度,方便点击区域                                  |
| cm_right_arrow_height            | dimension\|integer | 最右侧箭头高度,方便点击区域                                  |
| cm_right_arrow_right_margin      | dimension\|integer | 最右侧箭头rightMargin                                        |
| cm_bottom_divider_visible        | boolean            | 底部分割线是否可见                                           |
| cm_bottom_divider_color          | color\|reference   | 底部分割线颜色                                               |
| cm_bottom_divider_left_margin    | dimension\|integer | 底部分割线leftMargin                                         |
| cm_bottom_divider_right_margin   | dimension\|integer | 底部分割线rightMargin                                        |



## **公共方法:** 

| 方法名称                                            | 作用               |
| --------------------------------------------------- | ------------------ |
| setRightTextVisible(isVisible: Boolean)             | 右侧文本是否显示   |
| setRightImageViewVisible(isVisible: Boolean)        | 右侧图标是否显示   |
| setLeftText(text: String?)                          | 设置左侧文本       |
| setLeftText(text: String?, color: Int)              | 设置左侧文本和颜色 |
| setRightText(text: String?)                         | 设置右侧文本       |
| setRightText(text: String?, color: Int)             | 设置右侧文本和颜色 |
| setLeftImage(@DrawableRes resource: Int)            | 设置左侧图标       |
| setRightImage(@DrawableRes resource: Int)           | 设置右侧图标       |
| setMoreViewListener(listener: View.OnClickListener) | 设置箭头点击事件   |




# CustomSearchView
## **用法:** 

具体参考: cn.lvsong.lib.demo.CustomMenuActivity

```xml
        <cn.lvsong.lib.library.view.CustomSearchView
            android:id="@+id/csv_1"
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:csv_input_container_drawable="@drawable/ic_gray_solid_rectangle_r5"
            app:csv_input_container_margin_right="@dimen/padding_10"
            app:csv_input_hint_text="请输入搜索内容"
            app:csv_search_btn_bg_color="@color/color_2878FF"
            app:csv_search_btn_text_color="@color/color_FFFFFF"
            app:csv_search_btn_width="@dimen/width_40" />
```



## **属性介绍:** 

| 属性名称                         | 取值类型             | 取值和作用                                                   |
| -------------------------------- | -------------------- | ------------------------------------------------------------ |
| csv_left_arrow_visible           | boolean              | 是否显示返回按钮                                             |
| csv_left_arrow_color             | color\|reference     | 左侧箭头的颜色                                               |
| csv_left_arrow_padding           | dimension\|integer   | 左侧箭头 Padding(上下左右)                                   |
| csv_left_arrow_style             | enum                 | 左侧箭头模式,material_design(Material Design),wechat_design(微信风格),默认是material_design |
| csv_show_search_icon             | boolean              | 是否显示搜索图标,默认显示                                    |
| csv_search_icon_color            | color\|reference     | 搜索图标颜色                                                 |
| csv_show_clear_icon              | boolean              | 是否显示清除图标,默认显示                                    |
| csv_clear_icon_color             | color\|reference     | 清除图标颜色                                                 |
| csv_input_container_height       | dimension\|reference | 中间搜索容器高度,最小高度 30dp                               |
| csv_input_container_margin_left  | dimension\|reference | 中间搜索容器leftMargin                                       |
| csv_input_container_margin_right | dimension\|reference | 中间搜索容器rightMargin                                      |
| csv_input_container_drawable     | reference            | 中间搜索容器drawable,也就是容器的背景                        |
| csv_input_left_padding           | dimension\|reference | 中间搜索输入框leftPadding                                    |
| csv_default_search_text          | string\|reference    | 默认的搜索文本                                               |
| csv_input_text_size              | dimension\|reference | 中间搜索输入框文本大小                                       |
| csv_input_text_color             | color\|reference     | 中间搜索输入框文本颜色                                       |
| csv_input_hint_text              | string\|reference    | 中间搜索输入框提示文本                                       |
| csv_input_hint_color             | color\|reference     | 中间搜索输入框提示文本颜色                                   |
| csv_input_max_length             | integer              | 中间搜索输入框最大输入文本长度,默认不限制                    |
| csv_need_jump                    | boolean              | 点击整个搜索控件,此时如果需要跳转,则设置为true               |
| csv_search_btn_visible           | boolean              | 是否显示搜索按钮,默认显示                                    |
| csv_search_btn_text              | string\|reference    | 搜索按钮显示文本                                             |
| csv_search_btn_text_size         | dimension\|reference | 搜索按钮文本大小                                             |
| csv_search_btn_text_color        | color\|reference     | 搜索按钮文本颜色                                             |
| csv_search_btn_bg_color          | color\|reference     | 搜索按钮背景颜色                                             |
| csv_search_btn_width             | dimension\|reference | 搜索按钮宽度                                                 |
| csv_search_btn_height            | dimension\|reference | 搜索按钮高度                                                 |
| csv_search_btn_margin_right      | dimension\|reference | 搜索按钮宽度rightMargin                                      |
| csv_bottom_divider_visible       | boolean              | 底部分割线 ,默认隐藏                                         |
| csv_bottom_divider_color         | color\|reference     | 底部分割线颜色                                               |



## **公共方法:** 


# CustomEditMenu
## **用法:** 

具体参考: cn.lvsong.lib.demo.CustomMenuActivity

```
      <cn.lvsong.lib.library.view.CustomEditMenu
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:cem_bottom_divider_color="@color/color_8A8EA3"
            app:cem_bottom_divider_visible="true"
            app:cem_left_image_drawable="@drawable/ic_baseline_assignment_returned_24"
            app:cem_left_image_margin="@dimen/padding_10"
            app:cem_left_text_info="左侧图片右侧输入框+图片"
            app:cem_right_image_drawable="@drawable/ic_baseline_alarm_add_24"
            app:cem_right_image_right_margin="@dimen/padding_10"
            app:cem_right_input_hint_color="@color/color_B1B6D1"
            app:cem_right_input_hint_text="请输入昵称" />
```



## **属性介绍:** 

| 属性名称                        | 取值类型           | 取值和作用                                                |
| ------------------------------- | ------------------ | --------------------------------------------------------- |
| cem_left_image_visible          | boolean            | 最左侧图标是否可见                                        |
| cem_left_image_drawable         | reference          | 最左侧图标的 drawable                                     |
| cem_left_image_width            | dimension\|integer | 最左侧图标宽度                                            |
| cem_left_image_height           | dimension\|integer | 最左侧图标高度                                            |
| cem_left_image_margin           | dimension\|integer | 最左侧图标leftMargin                                      |
| cem_left_text_info              | reference\|string  | 紧靠左侧的文本                                            |
| cem_left_text_size              | dimension\|integer | 紧靠左侧的文本大小                                        |
| cem_left_text_color             | color\|reference   | 紧靠左侧的文本颜色                                        |
| cem_left_text_left_margin       | dimension\|integer | 紧靠左侧的文本leftMargin                                  |
| cem_right_input_visible         | boolean            | 右侧输入框的文本是否可见                                  |
| cem_right_input_left_padding    | dimension\|integer | 右侧输入框leftPadding                                     |
| cem_right_input_right_padding   | dimension\|integer | 右侧输入框rightPadding                                    |
| cem_right_input_width           | dimension\|integer | 右侧输入框宽度                                            |
| cem_right_input_text            | reference\|string  | 右侧输入框的文本,非提示文本                               |
| cem_right_input_hint_text       | reference\|string  | 右侧输入框的提示文本                                      |
| cem_right_input_text_size       | dimension\|integer | 右侧输入框的文本大小                                      |
| cem_right_input_text_length     | integer            | 右侧输入框的文本长度                                      |
| cem_right_input_text_color      | color\|reference   | 右侧输入框的文本颜色                                      |
| cem_right_input_hint_color      | color\|reference   | 右侧输入框的提示文本颜色                                  |
| cem_right_input_right_margin    | dimension\|integer | 右侧输入框的文本rightMargin                               |
| cem_right_input_type            | enum               | 右侧输入框输入的类型,默认是文本, 如果输入number则表示数字 |
| cem_right_image_visible         | boolean            | 最右侧图标的是否可见                                      |
| cem_right_image_drawable        | reference          | 最右侧图标的 drawable                                     |
| cem_right_image_width           | dimension\|integer | 最右侧图标宽度                                            |
| cem_right_image_height          | dimension\|integer | 最右侧图标高度                                            |
| cem_right_image_right_margin    | dimension\|integer | 最右侧图标高度rightMargin                                 |
| cem_bottom_divider_visible      | boolean            | 底部分割线是否可见                                        |
| cem_bottom_divider_color        | color\|reference   | 底部分割线颜色                                            |
| cem_bottom_divider_left_margin  | dimension\|integer | 底部分割线leftMargin                                      |
| cem_bottom_divider_right_margin | dimension\|integer | 底部分割线rightMargin                                     |

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














# 在common库中,封装了大家常用的控件,有部分控件直接使用部分大佬的,网络请求使用wanandroid开放api,在此感谢诸位大佬!感谢开源!如果用得上记得点赞收藏!

![非嵌套滑动加载](https://github.com/Jooyer/Basics/blob/master/gif/1.gif)
![NestedScroll嵌套滑动刷新加载](https://github.com/Jooyer/Basics/blob/master/gif/2.gif)
![懒加载](https://github.com/Jooyer/Basics/blob/master/gif/3.gif)
![使用StatusManager](https://github.com/Jooyer/Basics/blob/master/gif/4.gif)
![Banner](https://github.com/Jooyer/Basics/blob/master/gif/5.gif)
![自定义ToolBar](https://github.com/Jooyer/Basics/blob/master/gif/6.gif)
![自定义Menu](https://github.com/Jooyer/Basics/blob/master/gif/7.gif)
![聊天气泡](https://github.com/Jooyer/Basics/blob/master/gif/8.gif)
![自定义View集合1](https://github.com/Jooyer/Basics/blob/master/gif/9.gif)
![自定义View集合2](https://github.com/Jooyer/Basics/blob/master/gif/10.gif)
![自定义View集合3](https://github.com/Jooyer/Basics/blob/master/gif/11.gif)
![自定义View集合4](https://github.com/Jooyer/Basics/blob/master/gif/12.gif)

[![](https://jitpack.io/v/Jooyer/Basics.svg)](https://jitpack.io/#Jooyer/Basics)

## 依赖步骤:

### 第一步:

```groovy
	allprojects {
		repositories {
			// 加入 jitpack
			maven { url 'https://jitpack.io' }
		}
	}
```

### 第二步:

```groovy
	dependencies { // 注意,从v1.2.4开始,将默认使用viewbinging, 如果采用 kotlin-android-extensions ,最高版本只支持到 v1.2.3
	      // 自定义View和工具类均在词库,可以单独依赖
          implementation 'com.github.Jooyer.Basics:common:1.2.9'
          // 封装了 Retrofit + ViewModle + 协程,需要和ui库配合使用
          implementation 'com.github.Jooyer.Basics:net:1.2.9'
          // 封装了BaseActivity 和 BaseFragment(懒加载包含在内),还有网络请求开始/成功/网络失败/其他失败及重试等,需和net库配合使用
          implementation 'com.github.Jooyer.Basics:ui:1.2.9'
	}
```


## v1.0.1变化:
1.非嵌套刷新控件上拉时会超过限定值, 非嵌套刷新控件先上拉不松手下滑无效
2.嵌套刷新控件上拉没有到达加载高度时松手会里面恢复没有平滑效果
3.LeftImgAndRightTextView属性进行调整
4.TopImgAndBottomTextView属性进行调整
5.其他控件文字大小属性调整

## v1.0.2变化:
1.CustomSearchView增加光标是否显示属性
2.CustomSearchView增加设置cursorDrawable
3.CustomToolbar增加底部分割线为阴影同时保留设置为线,(阴影高度为5dp,会挤压标题等显示)
4.CustomToolbar增加设置阴影颜色
5.CustomToolbar增加设置背景颜色(ct_background_color),Shadow模式下有效,反之直接用系统android:background即可

## v1.0.3变化:
1. 对CustomSearchView的cursorDrawable适配9.0
2. 完善多视图StatusManager控件

## v1.0.4变化:
1. 紧急修复由于StatusManager中将 CustomToolbar 或者 自定义的Toolbar 等实现  StatusProvider 接口的控件,移除原有的父容器,添加到RootStatusLayout容器中,导致UI界面无法使 CustomToolbar 或者 自定义的Toolbar 等实现  StatusProvider 接口的控件.




   ```kotlin
   ... 省略部分 ...
   
   	                val childParent = child.parent as ViewGroup
                   // 移除掉原来的,则会导致界面错位,添加一个占位的,但是占位视图设置为 View.INVISIBLE(不可见的)
                   childParent.removeView(child)
                   // 占位View
                   val view = View(context)
                   view.setBackgroundColor(Color.TRANSPARENT)
                   // 保证原来的ID不变,是防止其他控件对此控件有依赖或者位置关系
                   view.id = child.id
                   view.visibility = View.INVISIBLE
                   childParent.addView(view, 0, params)
                   child.id = R.id.ct_tool_bar
                   addView(child, params)
   
   ... 省略部分 ...
   PS: 具体源码请参考: cn.lvsong.lib.library.state.RootStatusLayout.getCustomToolbar(viewGroup: ViewGroup)
   ```


## v1.0.5变化:
1. 对CustomMenu添加设置底部分割线高度
2. 对CustomMenu添加设右侧箭头线宽
3. 修复StatusManager如果使用contentView(contentLayoutView: View) 因其内部未初始化造成异常

## v1.0.6变化:
1. 增加折叠控件ExpandableTextView
2. 对CustomToolbar增加动态设置字体颜色
3. 修复其他bug

## v1.0.7变化:
1. 对ExpandableTextView增加属性
2. 对CustomSearchView右侧清除文本图标增加必要属性和方法

## v1.0.8变化:
1. 对banner增加缩放效果(Gallery)
2. 对banner在列表中显示异常处理
3. 对NestedRefreshLayout属性进行调整,使其易读易懂
4. 部分其他控件自定义属性增加必要注释

## v1.0.9变化:
1. StatusManager设置LoadingView背景色时异常
2. 对ImageLoader增加高斯模糊
3. 添加Glide在 placeHolder 时圆角占位图处理方案

## v1.0.10变化:
1. 修复banner数据为空和数据只有一个异常
2. 添加滚动通告NoticeView控件
3. 对搜索控件点击右侧按钮回调方法增加当前输入框的字符串

## v1.0.11变化:
1. 修复banner滑动方向异常(手动右滑松手后自动滑动还往右)
2. 修复ExpandTextView越界问题
3. 修复BadgeView设置数值不显示问题
4. 对LeftImgAndRightTextView增加方法
5. 修改keyboardUtil.openKeyboard()方法参数
6. 对StatusManager中StatusProvider增加支持阴影的方法

## v1.0.12变化:
1. 对ShadowLayout进行调整
2. 对CustomToolbar阴影调整
3. 增加语音动画View
4. 移出部分不需要的类
5. 更新各个依赖库版本

## v1.0.13变化:
1. 修复Banner滑动bug
2. 调整Banner指示器
3. 调整StatusManager

## v1.0.14变化:
1. 去掉gson,更换moshi
2. 去掉glide,更换coil
3. 去掉glide相关的自定义类
4. 调整刷新控件,增加自动刷新延迟方法,增加自动刷新回调,方便针对性做逻辑处理

## v1.0.15变化:
1. 对刷新控件增加对动画结束的支持
2. 调整StatusManager中方法
3. 对Banner指示器进行调整,但是如果Banner数量大于2手动滑动还是有bug

## v1.0.16变化:
1. 注释部分无用的日志,删除部分无用类
2. 对跳转市场评价AppraiseUtils进行调整
3. 对statu管理器增加StartAndStopAnimController,精准控制加载动画,具体参考: ChrysanthemumView 和 StatusActivity

## v1.1.0变化:
1. 增加网络状态监听, 调整网络请求状态参数和增加了网络异常的方法
2. 修复banner多张图片时滑动的异常和指示器显示异常
3. 调整部分依赖库版本
4. 将部分java代码转为kotlin

## v1.1.1变化:
1. 调整网络封装参数
2. 对AppraiseUtils过滤包错误修复
3. 针对banner从一个到更新多个时不自动滚动修复
4. 移出banner部分方法无用参数
5. 对ClickUtil参数调整
6. 增加权限请求类
7. 更新state中loading,error等UI

## v1.1.2变化:
1. 增加RecyclerView分割线
2. 增加设置Banner指示器位置上下偏移方法
3. 修改了自定义搜索,自定义Toolbar等属性和方法
4. 调整了一些其他类

## v1.1.3变化:
1. 对CustomToolbar左侧箭头增加设置背景的属性
2. 2. 对SelectFactory增加设置四个圆角半径的方法

## v1.1.4变化:
    1. 对Banner增加圆角
    2. 新增RecyclerView 线性分割线
    3. 对CustomSearchView增加左侧放大镜大小设置
    4. 对JAlertDialog增加延迟关闭方法

## v1.1.5变化:
    1. StatusManager增加了针对Loading结束时回调(如转圈圈当需要结束时在其转一整圈时回调,这样界面结束动画看起来比较顺滑不突兀)
    2. 更新使用StatusManager时数据为空或者异常等界面默认UI效果
    3. 对权限请求XpermissionsUtils适配了新的Activity Result 回调方法
    4. 对搜索控件 CustomSearchView 增加了autoSearch 方法,更方便控制搜索
    
## v1.1.6变化:
    1. 更新手机号正则 
    2. 对XPermissionUtils进行去掉过期方法,适配新API
    3. 对StatusManager Loading动画结束后回调进行调整,对控件隐藏增加动画   
        
## v1.1.7变化:
    1. 修复StatusManager多次Loading异常
    2. 更新屏幕截图方法
    3. 对RSAUtil和Base64Util进行了调整
    4. 对其他控件和者工具类做了优化 
            
## v1.1.9变化:
    1. 修复1.1.8依赖无法下载
    2. 增加显示与隐藏右侧Image方法
    3. 对AESUtil中base64进行调整
            
## v1.2.0变化:
    1.  修复ClickUtil.withTrigger() 防抖动点击无效的问题

## v1.2.1变化:
1. ClickUtil中 withTrigger() 和 click() 结合使用时不要用在列表中,使用clickWithTrigger()代替
2. AppraiseUtil需要注意在Android11的适配,具体看此类的头部注释
3. FileUtil.getFileSize()计算不正确的处理


## v1.2.2变化:
1. FileUtil计算文件大小异常修复
2. 增加TimeUtil格式化时分秒方法
3. 对CustomToolbar右侧图片控件增加是否可用方法


## v1.2.3变化:
1. 更改和添加网络注解 
2. 修改网络监听的逻辑 
3. 移出引起空指针异常的NetworkTypeUtil类 
4. 移出5.0以下广播监听网络相关,即只支持5.0及以上的网络监听


## v1.2.4变化:
1. 去掉 kotlin-android-extensions , 采用 ViewBinding


## v1.2.5变化:
1. 修复BaseFragment 开启 useStatusManager 是造成崩溃


## v1.2.6变化:
1. 修复下拉刷新时状态未及时更新
2. 调整CustomMenu右侧箭头与图片依赖关系
3. CustomMenu可以通过index属性设置箭头在图片的上面


## v1.2.7变化:
1. 修复CutomToolbar设置中间文本时颜色无效问题
2. 调整StatusConfig方法(使用无变化)
3. 增加一点注释


## v1.2.8变化:
1. 更新banner图片
2. 完善AES/RSA加密解密界面
3. 调整 KeyboardUtil 方法参数
4. 移出不需要的文件


## v1.2.9变化:
1. 修复仿微信单张图片时显示异常
2. 针对BaseViewModel增加了泛型处理
3. 修改了Fragment可见性
4. 调整了部分自定义View展示异常问题
5. 完善加密解密界面UI和逻辑


[toc]


# ArcView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```xml
        <cn.lvsong.lib.library.view.ArcView
        android:id="@+id/btn_5"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_200"
        android:layout_margin="@dimen/padding_5"
        app:av_arc_control_offset="@dimen/padding_20"
        app:av_arc_offset="@dimen/padding_20"
        app:av_background_color="@color/color_2878FF" />

```

## **属性介绍:**

| 属性名称              | 取值类型         | 取值和作用                                    |
| --------------------- | ---------------- | --------------------------------------------- |
| av_arc_offset         | float\|dimension | 曲线与直线交点处 相对于图形高度的偏移量,默认0 |
| av_background_color   | reference\|color | 背景色                                        |
| av_arc_control_offset | float\|dimension | 曲线控制点相对于控件高度偏移量,默认0          |

==PS: 如果设置控件高度为200dp, av_arc_offset = 20dp,则相当于矩形部分高度是180dp, av_arc_control_offset取值<=av_arc_offset,也就是如果想弧度更大,则av_arc_offset取值更大==

## **公共方法:**

暂无!


# ArrangeView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

步骤一:

```xml
    <cn.lvsong.lib.library.view.ArrangeView
        android:id="@+id/av"
        android:layout_width="wrap_content"
        android:layout_height="@dimen/height_40"
        android:layout_marginStart="@dimen/padding_10"
        android:layout_marginTop="@dimen/padding_10"
        app:av_first_show_top="false"
        app:av_show_count="6"
        app:av_space_width="@dimen/padding_8"
        />
```

步骤二: 

```java
        val data = arrayListOf<String>(
            "https://upload.jianshu.io/users/upload_avatars/2631077/dc99c361412c?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/1300228/1169f257-ab3b-44f2-bed2-57282511eb8f.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/7133325/f4370cf6-cf4d-4839-9b54-87beaa767d48?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/7290998/f64f5ef0-def0-4b26-beb3-b9d88f060ba0.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/2558050/7761b285-2805-4534-9870-ba7dcc7538ec.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"
        )
        av.setAdapter(object : ArrangeView.ArrangeAdapter(data) {
            override fun getRangeView(position: Int, parent: ViewGroup): View {
                val view = LayoutInflater.from(this@CustomActivity1)
                    .inflate(R.layout.item_arrange_view_peoples, parent, false)
                ImageLoad.loader.loadImgWithCircleAndRing(
                    view.findViewById(R.id.iv_range_view),
                    getItem(position),
                    2F,
                    Color.WHITE
                )
                return view
            }
        } )
```



## **属性介绍:**

| 属性名称          | 取值类型             | 取值和作用                      |
| ----------------- | -------------------- | ------------------------------- |
| av_space_width    | reference\|dimension | 每一个Item重叠部分,默认20dp     |
| av_show_count     | integer              | 一共显示多个ItemView,默认4个    |
| av_first_show_top | integer              | 是否开头的显示在最上方,默认不是 |

## **公共方法**

| 方法名称                            | 作用       |
| ----------------------------------- | ---------- |
| setAdapter(adapter: ArrangeAdapter) | 设置适配器 |




# BackArrowView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```xml
   <cn.lvsong.lib.library.view.BackArrowView
        android:id="@+id/bav"
        android:layout_width="@dimen/width_40"
        android:layout_height="@dimen/height_40"
        android:layout_marginTop="@dimen/padding_10"
        app:bav_arrow_style="material_design"
        app:bav_arrow_color="@color/color_2878FF"
        app:bav_stroke_width="1.5dp"
        />
```

## **属性介绍:**

| 属性名称          | 取值类型           | 取值和作用                                                   |
| ----------------- | ------------------ | ------------------------------------------------------------ |
| bav_arrow_color   | color              | 箭头颜色,默认#666666                                         |
| bav_stroke_width  | integer\|dimension | 箭头线宽,默认2dp                                             |
| bav_arrow_padding | dimension\|integer | padding 使得里面 × 变小,默认1dp                              |
| bav_arrow_style   | enum               | 箭头模式, material_design(Material Design),默认值; wechat_design(微信风格) |

## **公共方法:**

| 方法名称                       | 作用         |
| ------------------------------ | ------------ |
| setArrowColor(int arrowColor)  | 设置箭头颜色 |
| setArrowStyle(int arrowStyle)  | 设置箭头模式 |
| setArrowPadding(float padding) | 设置内边距   |




# BadgeView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
    <cn.lvsong.lib.library.view.BadgeView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/padding_5"
        app:bv_number="111"
        app:bv_text_size="@dimen/text_size_14"
        app:bv_stoke_width="1.5dp"
        app:bv_more_style="plus"
        app:bv_text_medium="false"
        app:bv_background_color="@color/color_2878FF"
        app:bv_lr_padding="@dimen/padding_8"
        app:bv_tb_padding="@dimen/padding_5"
        />
```

## **属性介绍:**

| 属性名称            | 取值类型           | 取值和作用                                              |
| ------------------- | ------------------ | ------------------------------------------------------- |
| bv_tb_padding       | dimension\|integer | 上下间隔,默认3dp,最小1dp                                  |
| bv_lr_padding       | dimension\|integer | 左右间隔,默认5dp,最小1dp                                        |
| bv_stoke_width      | dimension\|float | 轮廓宽度,默认1dp,                                       |
| bv_stoke_color      | color              | 裸辞颜色,默认白色                                       |
| bv_text_size        | dimension\|integer | 文字大小,默认14dp                                       |
| bv_text_color       | color              | 文字颜色,默认白色                                       |
| bv_text_medium      | boolean            | 文字中粗,默认是的                                       |
| bv_background_color | color              | 背景色,默认红色                                         |
| bv_number           | integer            | 显示数值                                                |
| bv_more_style       | enum               | 显示风格,>99显示(dot)...,  或者显示(plus)+ ,默认显示... |

## **公共方法:**

| 方法名称               | 作用             |
| ---------------------- | ---------------- |
| setNumber(number: Int) | 设置具体消息数量 |



# Banner(RecyclerView实现)
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
| 属性名称                         | 取值类型             | 取值和作用                                                   |
| -------------------------------- | -------------------- | ------------------------------------------------------------ |
| banner_indicator_height          | reference\|dimension | 指示器高度                                                   |
| banner_indicator_margin          | reference\|dimension | 指示器内部 View  左右Margin                                  |
| banner_show_indicator            | boolean              | 是否显示指示器                                               |
| banner_loop_time                 | integer              | 轮播时间间隔,默认3000ms                                      |
| banner_select_indicator_drawable | reference            | 指示器选中状态图片                                           |
| banner_normal_indicator_drawable | reference            | 指示器默认状态图片                                           |
| banner_item_scroll_time        | integer              | ItemView 滑动时滑过一屏所需时间,默认1200 |



## **公共方法:**

| 方法名称                                                     | 作用                                          |
| ------------------------------------------------------------ | --------------------------------------------- |
| fun setBannerAdapter(         adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,         spaceWidth: Int = 0 ) | 绑定banner适配器                              |
| onPause()                                                    | 暂停滑动                                      |
| onResume()                                                   | 继续滑动                                      |
| onStop()                                                     | 停止滑动                                      |
| setOnPositionChangeListener(positionChangeListener: OnPositionChangeListener) | 如果需要知道当前显示的下标,可以通过此回调获取 |


# BubbleLinearLayout
## **用法:**

具体参考:

```xml
    <cn.lvsong.lib.library.bubble.BubbleLinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:bubbleColor="@color/color_B1B6D1"
        >

        <androidx.appcompat.widget.AppCompatImageView
            android:id="@+id/iv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:adjustViewBounds="true"
            android:layout_margin="@dimen/padding_10"
            />
        

    </cn.lvsong.lib.library.bubble.BubbleLinearLayout>
```

## **属性介绍:**

| 属性名称           | 取值类型           | 取值和作用                                                   |
| ------------------ | ------------------ | ------------------------------------------------------------ |
| bll_arrow_width    | dimension\|integer | 箭头宽度,默认25px                                            |
| bll_around_radius          | float\|dimension   | 四周圆角每一个圆角半径,默认20px                               |
| bll_arrow_height   | dimension\|integer | 箭头高度,默认25px                                            |
| bll_arrow_position | dimension\|integer | 箭头位置,当arrowLocation确定时箭头初始位置的偏移量,,默认50px |
| bll_bubble_color   | color              | 气泡背景色                                                   |
| bll_arrow_center   | boolean            | 箭头居中,此时arrowPosition无效,默认false                     |
| bll_arrow_location | enum               | 箭头方向,默认在左边 ,可取: left,right,top,bottom,top_right   |



## **公共方法:**

| 方法名称                                                     | 作用                                         |
| ------------------------------------------------------------ | -------------------------------------------- |
| setArrowAngle(angle: Float)                                  | 设置四周圆角每一个圆角角度                   |
| setArrowHeight(arrowHeight: Float)                           | 设置箭头高度                                 |
| setArrowLocation(arrowLocation:  BubbleDrawable.ArrowLocation) | 设置箭头方向                                 |
| setArrowPosition(arrowPosition: Float)                       | 设置箭头位置                                 |
| setArrowWidth(arrowWidth: Float)                             | 设置箭头宽度                                 |
| setArrowCenter(arrowCenter: Boolean)                         | 设置箭头居中,此时arrowPosition无效,默认false |
| setBubbleColor(bubbleColor: Int)                             | 设置气泡背景色                               |


# ChrysanthemumView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
    <cn.lvsong.lib.library.refresh.ChrysanthemumView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/padding_20"
        android:layout_marginStart="@dimen/padding_20"
        app:chrysanthemum_view_color="@color/color_E95C5B5B"
        app:chrysanthemum_view_radius="@dimen/padding_15"
        app:chrysanthemum_view_width="@dimen/padding_8"
        app:chrysanthemum_view_height="@dimen/padding_3"
        app:chrysanthemum_flower_count="10"
        />
```



## **属性介绍:**

| 属性名称        | 取值类型           | 取值和作用                    |
| --------------- | ------------------ | ----------------------------- |
| cv_flower_count | integer            | 花瓣的数量,一般去偶数 8,10,12 |
| cv_view_radius  | dimension\|integer | 菊花半径                      |
| cv_view_width   | dimension\|integer | 每一片花瓣的长度              |
| cv_view_height  | dimension\|integer | 每一片花瓣的厚度              |
| cv_view_color   | color              | 花瓣的颜色                    |



## **公共方法:**

| 方法名称 | 作用                                             |
| -------- | ------------------------------------------------ |
| onStartAnimator()  | 开始旋转,如果控件可见就会自动执行,不必手动调用   |
| onStopAnimator()   | 暂停旋转,如果控件不可见就会自动执行,不必手动调用 |




# CloseView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
    <cn.lvsong.lib.library.view.CloseView
        android:id="@+id/cv2"
        android:layout_width="@dimen/width_40"
        android:layout_height="@dimen/height_40"
        android:layout_margin="@dimen/padding_5"
        app:cv_circle_line_color="@color/main_theme_color"
        app:cv_circle_line_width="2dp"
        app:cv_circle_has_bg="true"
        app:cv_circle_bg_padding="2dp"
        app:cv_circle_bg_color="@color/color_8A8EA3"
        app:cv_line_width="1.5dp"
        app:cv_mode="circle" />
```



## **属性介绍:**

| 属性姓名             | 取值类型           | 取值和作用                                                   |
| -------------------- | ------------------ | ------------------------------------------------------------ |
| cv_line_width        | integer\|dimension | 叉叉的线高                                                   |
| cv_line_color        | color              | 叉叉的颜色                                                   |
| cv_line_padding      | integer\|dimension | padding 使得里面 × 变小                                      |
| cv_mode              | enum               | 模式，普通模式(normal)只有叉叉、圆边模式(circle)带圆形背景   |
| cv_circle_line_color | color              | 圆的轮廓线颜色                                               |
| cv_circle_line_width | integer\|dimension | 圆的轮廓线宽,如果指定为0 ,则不绘制圆环                       |
| cv_circle_has_bg     | boolean            | 是否绘制背景,默认不绘制,只有在 cv_mode = circle才有效,此时就不要再设置 android:background |
| cv_circle_bg_color   | color              | 背景色,只有在 cv_mode = circle才有效                         |
| cv_circle_bg_padding | dimension\|integer | padding 使得在 cv_mode = circle 时,轮廓线往内变小,同时点击面积没有变小 |



## **公共方法:**

| 方法名称                      | 作用                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| setColor(int color)           | 设置叉叉的颜色,默认#444444                                   |
| setLineWidth(float lineWidth) | 设置叉叉线的宽度(厚度),默认1.5dp                             |
| setLinePadding(float padding) | 使得里面 × 变小,这样不影响点击范围,默认4dp                   |
| setMode(int mode)             | 设置控件是圆形(此时背景色和圆环才有效果)还是方形,默认方形(1) |
| setHasBg(boolean hasBg)       | 设置控件是否拥有背景色,默认false                             |
| setBgColor(int bgColor)       | 设置控件背景色.默认透明                                      |




# CountDownView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
    <cn.lvsong.lib.library.view.CountDownView
        android:id="@+id/cdv"
        android:layout_width="@dimen/width_60"
        android:layout_height="@dimen/height_60"
        android:layout_margin="@dimen/padding_5"
        app:cdv_bg_color="@android:color/holo_red_dark"
        app:cdv_ring_color="@color/main_theme_color"
        app:cdv_ring_width="@dimen/padding_5"
        app:cdv_text_info="5s"
        />
```



## **属性介绍:**

| 属性名称       | 取值类型           | 取值和作用 |
| -------------- | ------------------ | ---------- |
| cdv_ring_color | color              | 圆环颜色   |
| cdv_ring_width | integer\|dimension | 圆环宽度   |
| cdv_bg_color   | color              | 背景色     |
| cdv_text_color | color              | 文本颜色   |
| cdv_text_size  | integer\|dimension | 文本大小   |
| cdv_text_info  | string\|reference  | 文本内容   |



## **公共方法:**

| 方法名称                       | 作用                        |
| ------------------------------ | --------------------------- |
| startCountDown(duration: Long) | 开始倒计时,注意单位为==秒== |



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

| 方法名称                                                 | 作用                         |
| -------------------------------------------------------- | ---------------------------- |
| setLeftImageVisible(visible: Int)                        | 设置左侧图标是否可见         |
| setLeftImageDrawable(drawable: Drawable)                 | 设置左侧图标                 |
| setLeftText(text: String?)                               | 设置左侧文本                 |
| setLeftText(text: String?,@ColorInt color: Int)          | 设置左侧文本和字体颜色       |
| setRightEditTextVisible(isVisible: Boolean)              | 设置右侧输入框是否可见       |
| setRightEditText(text: String?,@ColorInt color: Int)     | 设置右侧输入框文本和颜色     |
| setRightEditText(text: String?)                          | 设置右侧输入框文本           |
| setRightEditHintText(text: String?,@ColorInt color: Int) | 设置右侧输入框提示文本和颜色 |
| setRightEditHintText(text: String?)                      | 设置右侧输入框提示文本       |
| setRightImageVisible(visible: Int)                       | 设置右侧图标是否可见         |
| setRightImageDrawable(drawable: Drawable)                | 设置右侧图标                 |



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
| cm_right_arrow_padding           | dimension\|integer | 最右侧箭头 Padding(上下左右),默认1dp                         |
| cm_right_arrow_orientation       | enum               | 最右侧箭头方向, 向左(left)或向右(right),默认right            |
| cm_right_arrow_style             | enum               | 最右侧箭头模式,material_design(Material Design),wechat_design(微信风格),默认material_design |
| cm_right_arrow_width             | dimension\|integer | 最右侧箭头宽度,方便点击区域                                  |
| cm_right_arrow_height            | dimension\|integer | 最右侧箭头高度,方便点击区域                                  |
| cm_right_arrow_stroke            | dimension\|float   | 最右侧箭头线条宽度,默认2dp                                   |
| cm_right_arrow_right_margin      | dimension\|integer | 最右侧箭头rightMargin                                        |
| cm_bottom_divider_visible        | boolean            | 底部分割线是否可见                                           |
| cm_bottom_divider_height         | dimension\|float   | 底部分割线高度,默认1px                                       |
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
            app:csv_input_cursor_visible="true"
            app:csv_input_hint_text="请输入搜索内容"
            app:csv_search_btn_bg_color="@color/color_2878FF"
            app:csv_search_btn_text_bold="true"
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
| csv_search_icon_show             | boolean              | 是否显示搜索图标,默认显示                                    |
| csv_search_icon_color            | color\|reference     | 搜索图标颜色                                                 |
| csv_clear_icon_show              | boolean              | 是否显示清除图标,默认显示                                    |
| csv_clear_icon_color             | color\|reference     | 清除图标颜色                                                 |
| csv_clear_icon_line_width        | integer\|dimension   | 清除图标线宽(厚度),默认1.5dp                                 |
| csv_clear_icon_line_padding      | dimension\|integer   | 使得里面 × 变小同时点击面积没有变小,默认0dp                  |
| csv_clear_icon_margin_right      | dimension\|integer   | 清除图标rightMargin,默认6dp                                  |
| csv_clear_icon_mode              | enum                 | 清除图标模式，普通模式normal(默认)、圆形模式circle           |
| csv_clear_icon_circle_radius     | integer\|dimension   | 清除图标半径,只有在 cv_mode = circle才有效,且此时必须设置,否则图标较大,默认为10dp |
| csv_clear_icon_has_bg            | boolean              | 是否绘制背景,默认不绘制,只有在 cv_mode = circle才有效,此时就不要再设置 android:background |
| csv_clear_icon_bg_color          | color                | 背景色,只有在 cv_mode = circle才有效,默认透明                |
| csv_input_container_height       | dimension\|reference | 中间搜索容器高度,最小高度 30dp,默认36dp                      |
| csv_input_container_margin_left  | dimension\|reference | 中间搜索容器leftMargin                                       |
| csv_input_container_margin_right | dimension\|reference | 中间搜索容器rightMargin                                      |
| csv_input_container_drawable     | reference            | 中间搜索容器drawable,也就是容器的背景                        |
| csv_input_left_padding           | dimension\|float     | 中间搜索输入框leftPadding                                    |
| csv_default_search_text          | string\|reference    | 默认的搜索文本                                               |
| csv_input_text_size              | dimension\|float     | 中间搜索输入框文本大小                                       |
| csv_input_text_color             | color\|reference     | 中间搜索输入框文本颜色                                       |
| csv_input_hint_text              | string\|reference    | 中间搜索输入框提示文本                                       |
| csv_input_hint_color             | color\|reference     | 中间搜索输入框提示文本颜色                                   |
| csv_input_max_length             | integer              | 中间搜索输入框最大输入文本长度,默认不限制                    |
| csv_input_cursor_visible         | boolean              | 中间搜索输入框光标是否显示,默认显示                          |
| csv_input_cursor_drawable        | reference            | 中间搜索输入框光标自定义drawable,不需要可以不设置,也可以设置一个自定义的drawable |
| csv_need_jump                    | boolean              | 点击整个搜索控件,此时如果需要跳转,则设置为true               |
| csv_search_btn_visible           | boolean              | 是否显示搜索按钮,默认显示                                    |
| csv_search_btn_text              | string\|reference    | 搜索按钮显示文本                                             |
| csv_search_btn_text_size         | dimension\|float     | 搜索按钮文本大小                                             |
| csv_search_btn_text_color        | color\|reference     | 搜索按钮文本颜色                                             |
| csv_search_btn_text_bold         | boolean              | 右侧文本是否加粗,类似苹果中黑效果,默认是true(加粗的)         |
| csv_search_btn_bg_color          | color\|reference     | 搜索按钮背景颜色                                             |
| csv_search_btn_width             | dimension\|reference | 搜索按钮宽度                                                 |
| csv_search_btn_height            | dimension\|reference | 搜索按钮高度                                                 |
| csv_search_btn_margin_right      | dimension\|reference | 搜索按钮宽度rightMargin                                      |
| csv_bottom_divider_visible       | boolean              | 底部分割线 ,默认隐藏                                         |
| csv_bottom_divider_color         | color\|reference     | 底部分割线颜色                                               |

## **公共方法:**

| 方法名称                                        | 作用             |
| ----------------------------------------------- | ---------------- |
| setOnSearchListener(listener: OnSearchListener) | 对各种操作的回调 |



# CustomToolbar
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomToolbarActivity ,

PS: 一般右侧显示一个文本按钮(或者2个图标按钮,或者更多按钮),如果需要同时设置多个,发现重叠,有margin_right的属性,可以通过调节大小

```xml
    <cn.lvsong.lib.library.view.CustomToolbar
        android:id="@+id/ct_9"
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_50"
        app:ct_center_text_color="@color/color_333333"
        app:ct_center_text_info="将底部分割线style改为line"
        app:ct_center_text_size="@dimen/text_size_16"
        app:ct_left_arrow_color="@color/color_2878FF"
        app:ct_left_arrow_height="@dimen/height_50"
        app:ct_left_arrow_padding="@dimen/padding_3"
        app:ct_left_arrow_style="material_design"
        app:ct_left_arrow_width="@dimen/width_40"
        app:ct_right_image2_drawable="@drawable/ic_baseline_assignment_returned_24"
        app:ct_right_image2_right_margin="@dimen/padding_30"
        app:ct_right_image2_visible="true"
        app:ct_right_image2_width="@dimen/width_40"
        app:ct_right_mav_color="@color/color_666666"
        app:ct_right_mav_dot_radius="@dimen/padding_2"
        app:ct_right_mav_height="@dimen/height_50"
        app:ct_right_mav_orientation="vertical"
        app:ct_right_mav_right_margin="@dimen/padding_2"
        app:ct_right_mav_visible="true"
        app:ct_right_mav_width="@dimen/width_40"
        app:ct_bottom_divider_color="@color/color_666666"
        />
```



## **属性介绍:**

| 属性名称                         | 取值类型           | 取值和作用                                                   |
| -------------------------------- | ------------------ | ------------------------------------------------------------ |
| ct_left_arrow_visible            | boolean            | 左侧箭头是否显示, 默认true                                   |
| ct_left_arrow_width              | dimension\|integer | 左侧箭头宽度, 默认40dp                                       |
| ct_left_arrow_height             | dimension\|integer | 左侧箭头高度, 默认50dp                                       |
| ct_left_arrow_padding            | dimension\|integer | 左侧箭头 Padding(上下左右), 默认3dp                          |
| ct_left_arrow_left_margin        | dimension\|integer | 左侧箭头左侧 Margin, 默认0dp                                 |
| ct_left_arrow_color              | color\|reference   | 左侧箭头的颜色, 默认#999999                                  |
| ct_left_arrow_style              | enum               | 左侧箭头模式, ,默认Material Design, material_design(Material Design),wechat_design(微信风格) |
| ct_left_text_visible             | boolean            | 左侧文本是否显示, 默认false                                  |
| ct_left_text_info                | reference\|string  | 左侧文本                                                     |
| ct_left_text_size                | dimension          | 左侧文本大小, 默认15dp                                       |
| ct_left_text_color               | color\|reference   | 左侧文本颜色, 默认#333333                                    |
| ct_left_text_left_margin         | dimension\|integer | 左侧文本左侧 Margin, 默认25dp                                |
| ct_center_text_info              | reference\|string  | 中间文本                                                     |
| ct_center_text_size              | dimension          | 中间文本大小, 默认18dp                                       |
| ct_center_text_color             | color\|reference   | 中间文本颜色,默认 #333333                                    |
| ct_right_text_visible            | boolean            | 右侧文本是否可见, 默认false                                  |
| ct_right_text_info               | reference\|string  | 右侧文本                                                     |
| ct_right_text_size               | dimension          | 右侧文本大小, 默认15dp                                       |
| ct_right_text_color              | color\|reference   | 右侧文本颜色, 默认#333333                                    |
| ct_right_text_right_margin       | dimension\|integer | 右侧文本右侧 Margin, 默认12dp                                |
| ct_right_text_bold               | boolean            | 右侧文本是否加粗,类似苹果中黑效果,默认是加粗的               |
| ct_right_image_visible           | boolean            | 右侧图片1是否可见,默认false                                  |
| ct_right_image_checked           | boolean            | 右侧图片是否选中,默认false                                   |
| ct_right_image_drawable          | reference          | 右侧图片1 Drawable                                           |
| ct_right_image_drawable_checked  | reference          | 右侧图片1 选中后Drawable                                     |
| ct_right_image_width             | dimension\|integer | 右侧图片1宽度,默认22dp                                       |
| ct_right_image_height            | dimension\|integer | 右侧图片1高度,默认22dp                                       |
| ct_right_image_padding           | dimension\|integer | 右侧图片1 Padding(上下左右),默认0dp                          |
| ct_right_image_right_margin      | dimension\|integer | 右侧图片1右侧 rightMargin,默认5dp                            |
| ct_right_image2_visible          | boolean            | 右侧图片2是否可见,默认false                                  |
| ct_right_image2_checked          | boolean            | 右侧图片2是否选中,默认false                                  |
| ct_right_image2_drawable         | reference          | 右侧图片2 Drawable                                           |
| ct_right_image2_drawable_checked | reference          | 右侧图片2 选中后Drawable                                     |
| ct_right_image2_width            | dimension\|integer | 右侧图片2宽度,默认22dp                                       |
| ct_right_image2_height           | dimension\|integer | 右侧图片2高度,默认22dp                                       |
| ct_right_image2_padding          | dimension\|integer | 右侧图片2 Padding(上下左右),默认0dp                          |
| ct_right_image2_right_margin     | dimension\|integer | 右侧图片2右侧 rightMargin,默认10dp                           |
| ct_right_mav_visible             | boolean            | 右侧更多按钮是否可见,默认不可见,默认false                    |
| ct_right_mav_width               | dimension\|integer | 右侧更多按钮宽度,主要是设置点击范围, 默认40dp                |
| ct_right_mav_height              | dimension\|integer | 右侧更多按钮高度,主要是设置点击范围, 默认40dp                |
| ct_right_mav_right_margin        | dimension\|integer | 右侧更多按钮rightMargin,默认5dp                              |
| ct_right_mav_color               | color\|reference   | 右侧更多按钮的颜色, 默认#2878FF                              |
| ct_right_mav_dot_radius          | dimension\|integer | 右侧更多按钮大小,圆点半径,默认2dp                            |
| ct_right_mav_orientation         | enum               | 右侧更多按钮排列方向，水平或垂直, horizontal(水平),vertical(垂直), 默认垂直 |
| ct_bottom_divider_visible        | boolean            | 底部分割线是否可见, 默认true                                 |
| ct_bottom_divider_color          | color\|reference   | 底部分割线颜色,底部分割线颜色,如果分割线不可见则此属性无效 |
| ct_background_color              | color\|reference   | 背景色,默认白色,如果设置了阴影则使用 layout_background_color,否则使用下面属性设置背景色 |

## **公共方法:**

| 方法名称                                                     | 作用                                                       |
| ------------------------------------------------------------ | ---------------------------------------------------------- |
| setRightImageListener(listener: View.OnClickListener)        | 设置最右侧图片点击                                         |
| setRightImage2Listener(listener: View.OnClickListener)       | 设置右起倒数第二图片点击                                   |
| setMoreViewListener(listener: View.OnClickListener)          | 设置更多点击                                               |
| setRightTextListener(listener: View.OnClickListener)         | 设置右侧显示的文本                                         |
| setLeftArrowClickListener(listener: View.OnClickListener)    | 设置左侧箭头点击,默认有点击事件,点击后结束当前所在Activity |
| setLeftTextViewClickListener(listener: View.OnClickListener) | 设置左侧文本点击,默认有点击事件,点击后结束当前所在Activity |
| setLeftArrowColor(@ColorRes leftArrowColor: Int)             | 设置左侧箭头颜色                                           |
| setRightTextVisible(visible: Int)                            | 设置右侧文本控件是否显示                                   |
| setRightImageVisible(visible: Int)                           | 设置最右侧图片控件是否显示                                 |
| setRightImage2Visible(visible: Int)                          | 设置右起倒数第二图片控件是否显示                           |
| setRightText(text: String)                                   | 设置右侧显示文本                                           |
| setRightTextColor(@ColorInt rightTextColor: Int)             | 设置右侧文颜色                                             |
| setRightImageDrawable(@DrawableRes drawableId: Int)          | 设置右侧图片控件显示图片                                   |
| setRightImageCheckedDrawable(@DrawableRes drawableId: Int)   | 设置右侧图片控件显示选中图片                               |
| setRightImageChecked(checked:Boolean)                        | 设置右侧图片控件是否显示选中效果                           |
| getRightImageChecked()                                       | 获取右侧图片控件是否选中                                   |
| setRightImage2Drawable(@DrawableRes drawableId: Int)         | 设置右起倒数第二图片控件显示图片                           |
| setRightImage2CheckedDrawable(@DrawableRes drawableId: Int)  | 设置右起倒数第二图片控件显示选中图片                       |
| setRightImage2Checked(checked:Boolean)                       | 设置右起倒数第二图片控件是否显示选中效果                   |
| getRightImage2Checked()                                      | 获取右起倒数第二图片控件是否选中                           |
| setCenterText(text: String)                                  | 设置中间文本内容                                           |
| setCenterTextColor(@ColorInt centerTextColor: Int)           | 设置中间文颜色                                             |
| setLeftText(text: String)                                    | 设置左侧文本内容                                           |
| setLeftTextColor(@ColorInt leftTextColor: Int)               | 设置左侧文颜色                                             |
| setLeftArrowVisible(visible: Int)                            | 设置左侧箭头是否显示                                       |
| setMoveViewVisible(visible: Int)                             | 设置右边更多按钮是否显示                                   |




# DanceView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```xml
    <cn.lvsong.lib.library.view.DanceView
        android:layout_width="@dimen/width_40"
        android:layout_height="@dimen/height_40"
        android:layout_marginStart="@dimen/padding_40"
        android:layout_marginTop="@dimen/padding_10"
        app:dv_height_ratio="0.7"
        app:dv_pillar_color="@color/color_2878FF"
        app:dv_pillar_count="4"
        app:dv_pillar_duration="1200"
        app:dv_space_ratio="0.5" />
```



## **属性介绍:**

| 属性名称           | 取值类型 | 取值和作用                       |
| ------------------ | -------- | -------------------------------- |
| dv_pillar_count    | integer  | 柱子数量,默认是4个               |
| dv_pillar_duration | integer  | 动画执行时间,默认1000毫秒        |
| dv_space_ratio     | float    | 空白间隔与柱子宽的比率,默认0.8   |
| dv_height_ratio    | float    | 柱子高度与控件高度的比率,默认0.8 |
| dv_pillar_color    | color    | 柱子颜色,默认是红色              |



## **公共方法:**

| 方法名称         | 作用                                             |
| ---------------- | ------------------------------------------------ |
| startAnimator()  | 开始动画,如果控件可见就会自动执行,不必手动调用   |
| cancelAnimator() | 取消动画,如果控件不可见就会自动执行,不必手动调用 |



# ExpandaleTextView

## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity4

```kotlin
        etv_text_view.setOriginalText("据海关总署网站9月18日消息，因从印度尼西亚进口1批冻带鱼1个外包装样本检出新冠病毒核酸阳性，根据海关总署公告2020年第103号的规定，全国海关自即日起暂停接受印度尼西亚PT.PUTRI INDAH（注册编号为CR010-02）水产品生产企业产品进口申报1周，期满后自动恢复。")

```




## **属性介绍:**

| 属性名称                | 取值类型          | 取值和作用                                  |
| ----------------------- | ----------------- | ------------------------------------------- |
| etv_collapse_max_lines  | integer           | 折叠时文本显示行数,默认3行                  |
| etv_expand_drawable     | reference         | 展开时图标                                  |
| etv_collapse_drawable   | reference         | 折叠时图标                                  |
| etv_switch_mode         | enum              | 显示模式,TEXT(文本),ICON(图标)              |
| etv_expand_text         | reference\|string | 展开时文本,默认文本为: 收起 ,TEXT模式下有效 |
| etv_collapse_text       | reference\|string | 折叠时文本,默认文本为: 展开 ,TEXT模式下有效 |
| etv_expand_text_color   | color             | 展开文本颜色,默认#2878FF ,TEXT模式下有效    |
| etv_collapse_text_color | color             | 折叠文本颜色,默认#2878FF ,TEXT模式下有效    |



## **公共方法:**



# FixedCursorEditText

| 方法名称                                            | 作用                                  |
| --------------------------------------------------- | ------------------------------------- |
| initWidth(int width)                                | 初始化TextView的可展示宽度            |
| setHasAnimation(boolean hasAnimation)               | 设置是否有动画,默认false              |
| setMaxLines(int maxLines)                           | 设置折叠时显示的最多行数              |
| setOpenSuffix(String openSuffix)                    | 设置展开后缀                          |
| setCloseSuffix(String closeSuffix)                  | 设置收起后缀                          |
| setOpenSuffixColor(@ColorInt int openSuffixColor)   | 设置展开后缀文本颜色                  |
| setCloseSuffixColor(@ColorInt int closeSuffixColor) | 设置收起后缀文本颜色                  |
| setCloseInNewLine(boolean closeInNewLine)           | 是否在展开时 对折叠文本/图标 另起一行 |



## **用法:**

```xml
    <cn.lvsong.lib.library.view.FixedCursorEditText
        android:id="@+id/et_right_name_menu"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:background="@android:color/transparent"
        android:contentDescription="@null"
        android:gravity="center_vertical|end"
        android:maxLines="1"
        android:textColor="@color/color_333333"
        android:textCursorDrawable="@drawable/ic_fixed_edit_cursor_bg"
        android:textSize="@dimen/text_size_14"/>
```



## **作用介绍:**

 Android 处理EditText光标显示在hint文字之前的问题,即输入之前光标在提示文本最前面,写入文字后光标跑到了输入文本后面, 修改为输入前光标在提示文本末尾

## **公共方法:**

暂无


# JAlertDialog
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```java
        mExitDialog = JAlertDialog.Builder(this@CustomActivity1)
            .setCancelable(false) //设置是否点击外面可以取消
            .setContentView(view) // 绑定视图,可以是view也可以是layout资源文件
            .setHasAnimation(false) // 是否有动画效果,如果不调用setAnimation()则使用默认的 
//            .setAnimation() // 设置动画效果
            .setWidthAndHeight( // 设置弹框大小
                DensityUtil.getScreenWidth() - DensityUtil.dp2pxRtInt(38F) * 2,
                DensityUtil.dp2pxRtInt(130F)
            )
            //            .setText() // 可以放入TextView的id 和 在此控件显示的内容
            .setOnClick(R.id.btn_cancel_exit_dialog) // 设置点击的按钮ID,注意下面onClick(view: View, position: Int)中position和这里添加顺序有关
            .setOnClick(R.id.btn_sure_exit_dialog)// 设置点击的按钮ID,注意下面onClick(view: View, position: Int)中position和这里添加顺序有关
            .setOnJAlertDialogCLickListener(object : OnJAlertDialogCLickListener {
                override fun onClick(view: View, position: Int) {
                    // 参数中 position和添加点击按钮顺序有关
                    mExitDialog.dismiss()
                }
            })
            .create() // 这里调用create()则返回dialog
//            .show() // 这里调用show()则直接显示
```



## **属性介绍:**

暂无

## **公共方法:**

| 方法名称                                                     | 作用                                   |
| ------------------------------------------------------------ | -------------------------------------- |
| setContentView(view: View)                                   | 设置Dialog的视图                       |
| setContentView(layoutResId: Int)                             | 设置Dialog的视图                       |
| setCancelable(cancelable: Boolean)                           | 设置在弹框外面是否可以点击取消         |
| setText(@IdRes viewId: Int, text: CharSequence)              | 放入TextView的id 和 在此控件显示的内容 |
| setFromTop(marginTop: Int)                                   | 距离顶部距离,此时则弹框顶部显示        |
| setFromBottom()                                              | 设置Dialog在底部显示                   |
| setAnimation(@StyleRes styleAnim: Int)                       | 设置Dialog动画                         |
| setHasAnimation(hasAnimation: Boolean)                       | 设置是否使用动画,默认是true            |
| setFullWidth()                                               | 设置Dialog宽度占满                     |
| setWidthAndHeight(width: Int, height: Int)                   | 设置Dialog宽高                         |
| setOnClick(@IdRes viewId: Int)                               | 设置Dialog点击View                     |
| setOnJAlertDialogCLickListener(onJAlertDialogCLickListener: OnJAlertDialogCLickListener) | 设置点击事件                           |
| setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener) | 设置取消Dialog事件                     |
| setOnOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) | 设置Dialog消失回调                     |
| setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener) | 设置Dialog OnKeyListener               |
| create()                                                     | 生成Dialog返回                         |
| show()                                                       | 显示Dialog并返回                       |
| delayShow(delay: Int)                                        | 延迟显示,必须 Dialog 来调用            |




# LeftImgAndRightTextView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomMenuActivity

```xml
          <cn.lvsong.lib.library.view.LeftImgAndRightTextView
            android:id="@+id/lirt_test"
            android:layout_width="wrap_content"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:lirt_back_color="@color/color_DDDDDD"
            app:lirt_back_color_checked="@color/color_8A8EA3"
            app:lirt_checked="false"
            app:lirt_icon_drawable="@drawable/ic_baseline_alarm_add_24"
            app:lirt_icon_drawable_checked="@drawable/ic_baseline_assignment_returned_24"
            app:lirt_icon_height="@dimen/height_20"
            app:lirt_icon_width="@dimen/width_20"
            app:lirt_spacing="@dimen/padding_15"
            app:lirt_text_color="@color/color_999999"
            app:lirt_text_color_checked="@color/color_333333"
            app:lirt_text_info="左边图片,右边文字,点击试试"
            app:lirt_text_info_checked="此时显示的是被选中时文本"
            app:lirt_text_size="@dimen/text_size_14" />
```

## **属性介绍:**

| 属性名称               | 取值类型           | 取值和作用                                                |
| ---------------------- | ------------------ | --------------------------------------------------------- |
| lirt_checked           | boolean            | 是否被选中,默认false                                                |
| lirt_back_color         | color              | 控件默认背景色,不设置则没有                              |
| lirt_back_color_checked    | color              | 控件选中后背景色,不设置则没有                           |
| lirt_text_color         | color              | 文本默认颜色                                              |
| lirt_text_color_checked    | color              | 文本点击后颜色                                            |
| lirt_icon_drawable      | reference          | 默认图标                                                  |
| lirt_icon_drawable_checked | reference          | 点击后图片                                                |
| lirt_icon_width | integer\|dimension | 图片宽度,默认30dp |
| lirt_icon_height | integer\|dimension | 图片高度,默认30dp |
| lirt_text_info | string             | 文本内容                                                  |
| lirt_text_info_checked | string | 选中后文本内容 |
| lirt_text_size          | float\|dimension   | 文本大小                                                  |
| lirt_spacing           | dimension\|integer | 文本和图片的间隔,默认8dp                                    |
| lirt_icon_location | enum               | 设置图片所在方向,可取iconLeft(默认值),iconRight,iconUp,iconBottom |



## **公共方法:**

| 方法名称                                            | 作用                              |
| --------------------------------------------------- | --------------------------------- |
| setIconPosition(int position)                       | 设置图片所在方向                  |
| setBackColor(int backColor)                         | 设置控件背景色                    |
| setBackColorChecked(int backColorChecked)           | 设置控件被按下时的背景色          |
| setIconDrawable(Drawable iconDrawable)              | 设置icon的图片                    |
| setIconDrawablePress(Drawable iconDrawableChecked)  | 设置被按下时的icon的图片          |
| setTextColor(@ColorRes  int textColor)              | 设置文字的颜色                    |
| setTextColorChecked(@ColorRes int textColorChecked) | 设置被按下时文字的颜色            |
| setText(CharSequence text)                          | 设置显示的文本内容                |
| setTextSize(float size)                             | 设置文本字体大小                  |
| setCheckedTextInfo(text:String)                     | 设置选中的文本                    |
| setTextInfo(text:String)                            | 设置普通文本                      |
| setSpacing(int spacing)                             | 设置两个控件之间的间距,单位==dp== |
| setChecked(checked: Boolean)                        | 设置是否选中                      |
| isChecked()                                         | 是否选中                          |
| toggle()                                            | 切换当前check状态                 |




# MediumTextView
## **用法:**

```xml
    <cn.lvsong.lib.library.view.MediumTextView
        android:id="@+id/tv_text_right_text"
        android:layout_width="wrap_content"
        android:layout_height="@dimen/height_40"
        android:layout_marginStart="@dimen/padding_10"
        android:text="扫一扫"
        android:textColor="@color/color_FFFFFF"
        android:textSize="@dimen/text_size_14"
        android:gravity="center_vertical"
        />
```



## **用法介绍:**

只需在布局中引用即可

## **公共方法:**

暂无


# MoreActionView
## **用法:**

```xml
    <cn.lvsong.lib.library.view.MoreActionView
        android:id="@+id/mav_right_icon_menu"
        android:layout_width="@dimen/width_40"
        android:layout_height="@dimen/height_40"
        android:layout_marginEnd="@dimen/padding_10"
        app:mav_color="@color/color_2878FF"
        app:mav_dot_radius="@dimen/padding_2"
        app:mav_orientation="horizontal"/>
```



## **属性介绍:**

| 属性名称        | 取值类型         | 取值和作用                                          |
| --------------- | ---------------- | --------------------------------------------------- |
| mav_color       | color            | 点的颜色,默认 #444444                               |
| mav_dot_radius  | float\|dimension | 点大小,默认2dp                                      |
| mav_orientation | enum             | 排列方向，水平(horizontal)或垂直(vertical),默认水平 |



## **公共方法:**

| 方法名称                        | 作用       |
| ------------------------------- | ---------- |
| setDotRadius(float dotRadius)   | 设置点大小 |
| setOrientation(int orientation) | 设置方向   |
| setColor(int color)             | 设置点颜色 |




# MovingBallView(在APP module中)
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
           <cn.lvsong.lib.library.view.MovingBallView
        android:id="@+id/mbv_audio_record_publish_dynamic"
        android:layout_width="@dimen/width_100"
        android:layout_height="@dimen/height_100"
        android:layout_marginTop="@dimen/padding_20"
        app:mbv_ball_duration="60000"
        app:mbv_ball_radius="@dimen/padding_6"
        app:mbv_bg_line_width="@dimen/padding_1"
        app:mbv_microphone_width="@dimen/padding_20"
        app:mbv_moved_line_end_color="@color/color_333333"
        app:mbv_moved_line_start_color="@color/color_B1B6D1"
        app:mbv_moved_line_width="@dimen/padding_2"
        app:mbv_quadrilateral_length="@dimen/padding_20"
        app:mbv_round_corner="@dimen/padding_8"
        app:mbv_solid_circle_radius="@dimen/padding_30"
        app:mbv_triangle_length="@dimen/padding_20" />
```



## **属性介绍:**

| 属性名称                        | 取值类型         | 取值和作用                                         |
| ------------------------------- | ---------------- | -------------------------------------------------- |
| mbv_bg_line_color               | color            | 背景线条颜色,默认灰色                              |
| mbv_bg_line_width               | dimension\|float | 背景线条宽度,默认5dp                               |
| mbv_moved_line_start_color      | color            | 走过线条颜色,渐变起始颜色,默认是红色(没有渐变了)   |
| mbv_moved_line_end_color        | color            | 走过线条颜色,渐变结束颜色,默认是红色(没有渐变了)   |
| mbv_moved_line_width            | dimension\|float | 走过线条宽度,默认6dp                               |
| mbv_ball_color                  | color            | 圆球颜色,默认红色                                  |
| mbv_ball_radius                 | dimension\|float | 圆球半径,默认10dp                                  |
| mbv_ball_duration               | integer          | 圆球环绕一周时间,默认30000毫秒                     |
| mbv_solid_circle_radius         | dimension\|float | 中间实心圆半径,默认0                               |
| mbv_solid_circle_gradient_angle | float            | 中间实心圆渐变角度,默认0                           |
| mbv_triangle_length             | dimension\|float | 中间准备播放的三角形边长,默认0                     |
| mbv_quadrilateral_length        | dimension\|float | 中间停止状态的四边形边长,默认0                     |
| mbv_round_corner                | dimension\|float | 三角形,四边形边圆角大小,默认5dp                    |
| mbv_microphone_width            | dimension\|float | 录音话筒的宽度,其他尺寸按与宽度的比例计算,默认20dp |



## **公共方法:**

| 方法名称      | 作用         |
| ------------- | ------------ |
| startRecord() | 开始录制     |
| stopRecord()  | 停止录制     |
| playRecord()  | 开始播放     |
| resetRecord() | 重置录制按钮 |




# NineImageLayout
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity3

步骤一:

```xml
        <cn.lvsong.lib.library.view.NineImageLayout
            android:id="@+id/nl_images2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/padding_50"
            app:nl_image_gap="@dimen/padding_10"
            app:nl_keep_place="false"
            app:nl_left_padding="@dimen/padding_10"
            app:nl_right_padding="@dimen/padding_10"
            app:nl_single_image_width_ratio="0.8" />
	
```

步骤二:

```
  // 数据集合
  private val mImages2 = arrayListOf(
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg",
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1545980553,2413955112&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2412068931,3031791558&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=286946846,3770652173&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3820948238,3810516733&fm=26&gp=0.jpg"
    )
    
    // 设置数据适配器
          nl_images2.setAdapter(object : NineImageAdapter() {
            override fun getItemCount() = mImages2.size

            override fun bindView(view: View, pos: Int) {
                if (1 == mImages2.size) { //处理单张图片
                    Glide.with(view)
                        .asBitmap()
                        .load(mImages2[pos])
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                nl_images.setSingleImage(
                                    resource.width,
                                    resource.height,
                                    view.findViewById(R.id.iv_nine_item)
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    ImageLoad.loader.loadImage(
                        view.findViewById<AppCompatImageView>(R.id.iv_nine_item),
                        mImages2[pos]
                    )
                } else {
                    ImageLoad.loader.loadImage(
                        view.findViewById<AppCompatImageView>(R.id.iv_nine_item),
                        mImages2[pos]
                    )
                }
            }

            override fun OnItemClick(position: Int, view: View) {
                // 点击事件,根据需要重写
            }

            override fun createView(inflater: LayoutInflater, parent: ViewGroup, position: Int) =
                layoutInflater.inflate(R.layout.item_nine_image, parent, false)
        })
```





## **属性介绍:**

| 属性名称                    | 取值类型           | 取值和作用                                                   |
| --------------------------- | ------------------ | ------------------------------------------------------------ |
| nl_single_image_width_ratio | float              | 单张图片时宽度与屏幕宽度比值,默认是0.8                       |
| nl_left_padding             | dimension\|integer | 控件leftPadding,默认5dp,设置leftMargin无效                   |
| nl_right_padding            | dimension\|integer | 控件rightPadding,默认5dp,设置rightMargin无效                 |
| nl_item_gap                 | dimension\|integer | 图片之间间隙大小,非控件的,默认5dp                            |
| nl_keep_place               | boolean            | 2张图或者4张图片时,是否要和3张图大小保持一致,其他张数无效,默认是false |



## **公共方法:**

| 方法名称                                         | 作用               |
| ------------------------------------------------ | ------------------ |
| setSingleImage(int width, int height, View view) | 单张图片的展示处理 |
| setAdapter(NineImageAdapter adapter)             | 设置数据源         |

PS: ==一张图也需要设置适配器==


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
| nrl_no_more_first_time | enum  | 第一次加载时,如果没有更多数据,底部显示什么,NOT_SHOW:默认值, 不显示 Footer,禁用加载更多了;NO_MORE: 显示没有更多数据 |
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


# PhoneEditText
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity2

```xml
    <cn.lvsong.lib.library.view.PhoneEditText
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_50"
        android:layout_margin="@dimen/padding_20"
        android:background="@color/color_EEEEEE"
        android:hint="请输入电话号码"
        android:paddingStart="@dimen/padding_10"
        android:paddingEnd="@dimen/padding_10"
        android:textColor="@color/color_333333"
        android:textColorHint="@color/color_666666" />
```



## **属性介绍:**

暂无

## **公共方法:**

| 方法名称                                                     | 作用                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| setOnPhoneEditTextChangeListener(OnPhoneEditTextChangeListener listener) | 设置输入变化监听,返回的是参数中phoneNum是去掉空格后的电话号码 |
| getPhoneText()                                               | 获得不包含空格的电话号码                                     |




# PickerView
## **用法:**

具体参考:cn.lvsong.lib.demo.CustomActivity2

步骤一

```xml

    <cn.lvsong.lib.library.view.PickerView
        android:id="@+id/pickerView"
        android:layout_width="wrap_content"
        android:layout_height="@dimen/height_220"
        android:layout_marginTop="@dimen/padding_20"
        app:pv_max_alpha="255"
        app:pv_max_text_size="@dimen/text_size_20"
        app:pv_min_alpha="100"
        app:pv_min_text_size="@dimen/text_size_14"
        app:pv_text_color="@color/color_333333" />
```

步骤二:

```kotlin
    private fun setAddress(){
        val data =  arrayListOf<String>(*resources.getStringArray(R.array.province_info))
        mCurChoicePlace = data[3]
        pickerView.setData(data)
        pickerView.setSelected(3)
        pickerView.setOnSelectListener {
                text -> Log.e("Login", "======== $text");
        }
    }
```



## **属性介绍:**

| 属性名称         | 取值类型           | 取值和作用   |
| ---------------- | ------------------ | ------------ |
| pv_max_text_size | dimension\|integer | 字体最大值   |
| pv_min_text_size | dimension\|integer | 字体最小值   |
| pv_text_color    | color              | 文字颜色     |
| pv_max_alpha     | integer            | 透明度最大值 |
| pv_min_alpha     | integer            | 透明度最小值 |



## **公共方法:**

| 方法名称                                       | 作用               |
| ---------------------------------------------- | ------------------ |
| setData(@NonNull List<String> data)            | 设置数据           |
| setSelected(int selected)                      | 设置默认选中项     |
| setOnSelectListener(onSelectListener listener) | 设置滑动选中项回调 |



# PolygonSettingView
## **用法:**
具体参考: cn.lvsong.lib.demo.CustomActivity3

```xml
    <cn.lvsong.lib.library.view.PolygonSettingView
        android:id="@+id/psv_test"
        android:layout_width="@dimen/width_30"
        android:layout_height="@dimen/height_30"
        android:layout_marginTop="@dimen/padding_20"
        android:layout_marginStart="@dimen/padding_20"
        app:psv_color="@color/color_666666"
        app:psv_line_width="@dimen/padding_2"
        app:psv_num="6"
        />
```



## **属性介绍:**

| 属性名称       | 取值类型         | 取值和作用                                                   |
| -------------- | ---------------- | ------------------------------------------------------------ |
| psv_color      | color            | 颜色,必须设置                                                |
| psv_num        | integer          | 多边形多少条边，至少4条边，如果小于4条，会强制4条,如果图形没有摆正,可以设置android:rotation="xx" |
| psv_line_width | float\|dimension | 边的线宽,默认1.5dp                                           |



## **公共方法:**

| 方法名称                    | 作用              |
| --------------------------- | ----------------- |
| setChecked(boolean checked) | 设置是否选中      |
| isChecked()                 | 是否选中          |
| toggle()                    | 切换当前check状态 |




# RoundImageView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity3

```xml
    <cn.lvsong.lib.library.view.RoundImageView
        android:id="@+id/riv_cover_item"
        android:layout_width="@dimen/width_70"
        android:layout_height="@dimen/height_70"
        android:layout_marginStart="@dimen/padding_14"
        android:layout_marginTop="@dimen/padding_20"
        android:src="@drawable/ic_launcher_background"
        app:riv_border_color="@android:color/transparent"
        app:riv_border_width="4dp"
        app:riv_round_radius="@dimen/padding_5"
        app:riv_mask_type="ROUNDRECTANGLE"
        />
```



## **属性介绍:**

| 属性名称         | 取值类型           | 取值和作用                                                   |
| ---------------- | ------------------ | ------------------------------------------------------------ |
| riv_round_radius | dimension\|integer | 四周圆角半径,默认10px, 在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| riv_border_width | dimension\|float   | 边框半径,默认0,在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| riv_border_color | color              | 边框颜色,默认透明,在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| riv_mask_type    | enum               | 圆角的类型,当取 CIRCLE 时,riv_round_radius无效;当取 ROUNDRECTANGLETOP 时riv_border_color,riv_border_width无效, 矩形(RECTANGLE),CIRCLE(圆形),ROUNDRECTANGLE(四周圆角),ROUNDRECTANGLETOP(左上和右上有圆角) |




## **公共方法:**

| 方法名称                                            | 作用                                                         |
| --------------------------------------------------- | ------------------------------------------------------------ |
| setRadius(int radius)                               | 设置四周圆角半径, 在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| setBorderColor(@ColorInt int color)                 | 设置边框颜色,在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| setBorderColorResource(@ColorRes int colorResource) | 设置边框颜色,在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| setBorderWidth(float borderWidth)                   | 设置边框半径,在圆角类型为 ROUNDRECTANGLE或者ROUNDRECTANGLETOP时有效 |
| setMaskType(MaskType maskType)                      | 设置圆角的类型,矩形(RECTANGLE),CIRCLE(圆形),ROUNDRECTANGLE(四周圆角),ROUNDRECTANGLETOP(左上和右上有圆角) |



# ShadowLayout
## **用法:**

具体参考:  cn.lvsong.lib.demo.CustomActivity3

```xml
        <cn.lvsong.lib.library.view.ShadowLayout
            android:id="@+id/layout_container_mobile"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/padding_20">

            <cn.lvsong.lib.library.view.CustomMenu
                android:layout_width="match_parent"
                android:layout_height="@dimen/height_45"
                android:layout_margin="@dimen/padding_10"
                app:cm_left_image_visible="false"
                app:cm_left_text_color="@android:color/holo_red_light"
                app:cm_left_text_info="ShadowLayout阴影效果"
                app:cm_left_text_size="@dimen/text_size_16"
                app:cm_right_arrow_visible="false"
                app:cm_right_text_color="@color/color_666666"
                app:cm_right_text_info="ShadowLayout"
                app:cm_right_text_visible="true"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_around_radius="@dimen/padding_5"
                app:layout_background_color="@color/color_FFFFFF"
                app:layout_offset_x="0dp"
                app:layout_offset_y="@dimen/padding_2"
                app:layout_shadow_color="@color/color_9AA3A3A3"
                app:layout_shadow_radius="@dimen/padding_5" />

        </cn.lvsong.lib.library.view.ShadowLayout>

```



## **属性介绍:**

| 属性名称            | 取值类型         | 取值和作用                                                   |
| ------------------- | ---------------- | ------------------------------------------------------------ |
| layout_shadow_color     | color            | 阴影颜色,默认无 |
| layout_background_color | color            | 背景颜色,默认无                               |
| layout_offset_x         | dimension\|float | 阴影水平偏移,默认0                                           |
| layout_offset_y         | dimension\|float | 阴影垂直偏移,默认0                                           |
| layout_shadow_radius    | dimension\|float | 阴影圆角,默认0                                               |
| layout_around_radius    | dimension\|float | 子控件四周圆角大小,默认0                                      |



## **公共方法:**

暂无


# SmartPopupWindow
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```java
        val view = LayoutInflater.from(this).inflate(R.layout.item_top_menu, null)
        view.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_FFFFFF))
            .setCornerRadius(DensityUtil.dp2pxRtInt(10F))
            .create()
        view.setOnClickListener {
            mPopupWindow.dismiss()
        }
        mPopupWindow = SmartPopupWindow.Builder
            .build(this, view)
            .setSize(DensityUtil.dp2pxRtInt(200), DensityUtil.dp2pxRtInt(168))
            .setAlpha(0.4f)                   //背景灰度     默认全透明
            .setOutsideTouchDismiss(false)    //点击外部消失  默认true（消失）
            //            .setAnimationStyle() // 设置动画
            .createPopupWindow()
            
            
            // 点击按钮显示
           fun onClick(view: View) {
        // 后面2个参数控制 Popup 方向
        mPopupWindow.showAtAnchorView(view, VerticalPosition.BELOW, HorizontalPosition.CENTER)

    }     
```



## **属性介绍:**

暂无

## **公共方法:**

| 方法名称                                | 作用                                     |
| --------------------------------------- | ---------------------------------------- |
| setSize(int width, int height)          | 设置 popup 宽高                          |
| setAnimationStyle(int animationStyle)   | 设置动画资源                             |
| setAlpha(float alpha)                   | 设置弹框透明度                           |
| setOutsideTouchDismiss(boolean dismiss) | 设置点击弹框外面是否取dismiss,默认是true |




# StarsView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity3

```xml
    <cn.lvsong.lib.library.view.StarsView
        android:id="@+id/sv_star"
        android:layout_margin="@dimen/padding_20"
        android:layout_width="@dimen/width_30"
        android:layout_height="@dimen/height_30"
        app:stv_checked_color="@color/main_theme_color"
        app:stv_default_color="@color/color_2878FF"
        app:stv_edge_line_width="@dimen/padding_2"
        app:stv_num="5"
        app:stv_style="fill" />
```



## **属性介绍:**

| 属性名称            | 取值类型         | 取值和作用                                             |
| ------------------- | ---------------- | ------------------------------------------------------ |
| stv_default_color   | color            | 星星的默认颜色,默认Color.GREEN                         |
| stv_checked_color   | color            | 星星的选中颜色,Color.RED                               |
| stv_num             | integer          | 星星的角个数,默认5个,至少3条边，如果小于3条，会强制3条 |
| stv_edge_line_width | float\|dimension | 边的线宽,默认1dp                                       |
| stv_style           | enum             | 填充风格,fill(填满),stroke(描边),默认描边              |



## **公共方法:**

| 方法名称                    | 作用              |
| --------------------------- | ----------------- |
| setChecked(boolean checked) | 设置是否选中      |
| isChecked()                 | 是否选中          |
| toggle()                    | 切换当前check状态 |



# StatusManager

## **用法:**

==注意: 请仔细看下面这段代码==

```kotlin
   /**
         * 如果希望Toolbar不给遮挡,有以下2种解决办法
         * 1. 使用本库自带的 CustomToolbar,
         * 2. 自定义的Toolbar实现 StatusProvider 接口
         * PS: 无论上面哪种方式,都将导致控件被添加到了 RootStatusLayout 中,
         * 此时在UI界面是无法正常使用上面定义的控件, 只需使用 mStatusManager.getCustomView()
         * 即可获取 CustomToolbar 或者 自定义的Toolbar (需自行强转)
         */
        (mStatusManager!!.getCustomView() as CustomToolbar).setMoreViewListener(object :
            OnClickFastListener() {
            override fun onFastClick(v: View) {
               // TODO 
            }
        })
```

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
| showLoading()                 | 显示loading                                                  |
| showContent()                 | 显示内容,默认延迟显示1200ms                                  |
| delayShowContent(delay: Long) | 延迟显示内容                                                 |
| showEmptyData()               | 显示空视图                                                   |
| showError()                   | 显示错误视图                                                 |
| showNetWorkError()            | 显示网络异常                                                 |
| getRootLayout()               | 返回StatusManager容器                                        |
| getCustomView()               | 返回 CustomToolbar 或者 自定义的Toolbar 等实现  StatusProvider 接口的控件, ==如果需要操作原xml中的控件,只能通过此方法== |



# TopImgAndBottomTextView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity3

```xml
          <cn.lvsong.lib.library.view.TopImgAndBottomTextView
            android:layout_width="match_parent"
            android:layout_height="@dimen/height_80"
            android:layout_marginTop="@dimen/padding_10"
            app:tibt_icon_drawable_checked="@drawable/ic_baseline_assignment_returned_24"
            app:tibt_icon_height="@dimen/height_20"
            app:tibt_icon_width="@dimen/width_20"
            app:tibt_icon_drawable="@drawable/ic_baseline_alarm_add_24"
            app:tibt_checked="false"
            app:tibt_spacing="@dimen/padding_2"
            app:tibt_text_info="TopImgAndBottomTextView实现上面图片,下面文字"
            app:tibt_text_color="@color/color_333333"
            app:tibt_text_size="@dimen/text_size_12" />
```



## **属性介绍:**

| 属性名称                   | 取值类型           | 取值和作用                 |
| -------------------------- | ------------------ | -------------------------- |
| tibt_state_checked         | boolean            | 是否被选中,默认false       |
| tibt_icon_drawable         | reference          | 未选中Drawable             |
| tibt_icon_drawable_checked | reference          | 选中Drawable               |
| tibt_icon_width            | integer\|dimension | 图片宽度,默认30dp          |
| tibt_icon_height           | integer\|dimension | 图片高度,默认30dp          |
| tibt_spacing               | integer\|dimension | 文本距离图片间隔,默认10dp  |
| tibt_text_info             | string\|reference  | 文本内容                   |
| tibt_text_color            | color              | 文字颜色,默认#999999       |
| tibt_text_color_checked    | color              | 选中时文字颜色,默认#333333 |
| tibt_text_size             | float\|dimension   | 文字大小,默认14dp          |



## **公共方法:**

| 方法名称                                      | 作用                     |
| --------------------------------------------- | ------------------------ |
| setChecked(isChecked: Boolean)                | 设置是否被选中,默认false |
| setText(text: String)                         | 设置文本内容             |
| setTextColor(@ColorRes textColor: Int)        | 设置文本颜色             |
| setTextCheckedColor(@ColorRes textColor: Int) | 设置选中的文本颜色       |
| setTextSize(textSize:Float)                   | 设置字体大小,默认14dp    |
| setNormalImage(drawable: Drawable)            | 设置图标                 |
| setCheckedImage(drawable: Drawable)           | 设置选中的图标           |
| setChecked(checked: Boolean)                  | 设置是否选中             |
| isChecked()                                   | 是否选中                 |
| toggle()                                      | 切换当前check状态        |



# TopMenu
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity1

```java
        val data = arrayListOf<MenuItem>(
            MenuItem(R.drawable.ic_baseline_alarm_add_24, "举报"),
            MenuItem(R.drawable.ic_baseline_assignment_returned_24, "拉黑"),
            MenuItem(R.drawable.ic_baseline_alarm_add_24, "删除消息")
        )
        val adapter =
            object : CommonAdapter<MenuItem>(this@CustomActivity1, R.layout.item_top_menu, data) {
                override fun convert(holder: ViewHolder, bean: MenuItem, position: Int) {
                    holder.getView<AppCompatImageView>(R.id.aiv_icon_left_image).visibility =
                        View.GONE
                    holder.getView<MediumTextView>(R.id.tv_text_right_text).text =
                        data[position].text
                }
            }

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                Toast.makeText(
                    this@CustomActivity1,
                    "点击了${data[position].text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val itemDecoration = LinearDividerItemDecoration(
            this@CustomActivity1,
            DensityUtil.dp2pxRtInt(1F),
            ContextCompat.getColor(this@CustomActivity1, R.color.color_EEEEEE)
        )
         // 作用间隔根据需要自己处理
        itemDecoration.setDividerPaddingLeft(DensityUtil.dp2pxRtInt(14F))
        itemDecoration.setDividerPaddingRight(DensityUtil.dp2pxRtInt(14F))
        mTopMenu = TopMenu(this@CustomActivity1, adapter)
            .setWidth(DensityUtil.dp2pxRtInt(125F))
            .setHeight(DensityUtil.dp2pxRtInt(124F))
            .setBackDark(false)
             .setBubbleColor(Color.GREEN)
            .setPaddingTop(DensityUtil.dp2pxRtInt(15))
            // 使得弹框右侧距离屏幕间隔, 如果间隔够了,箭头位置还没有对准控件中间,
            // 可以在BubbleRecyclerView所在布局中使用 brv_arrow_offset
            .setPopupXOffset(-DensityUtil.dp2pxRtInt(2F))
            // 使得弹框上下偏移
            .setPopupYOffset(-DensityUtil.dp2pxRtInt(5F))
            .setItemDecoration(itemDecoration)
            
     // 显示
      mTopMenu.show(it, null, null)       
```



## **属性介绍:**

暂无

## **公共方法:**

| 方法名称                                                     | 作用                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| setWidth(int width)                                          | 设置宽度                                                     |
| setHeight(int height)                                        | 设置高度                                                     |
| setPopupXOffset(int popupOffset)                             | 设置弹窗偏X方向移距离,默认0                                  |
| setPopupYOffset(int yOffset)                                 | 设置弹窗偏Y方向移距离,默认0                                  |
| setBackDark(boolean isShowBackground)                        | 设置背景是否变暗,默认true                                    |
| setShowAnimationStyle(boolean isShowAnimationStyle)          | 设置是否显示动画,默认true,如果没有调用 setAnimationStyle() 则使用默认动画 |
| setAnimationStyle(int animationStyle)                        | 设置动画                                                     |
| setArrowOffset(float offset)                                 | 箭头位置,当arrowLocation确定时箭头初始位置的偏移量,默认50px  |
| setItemDecoration(RecyclerView.ItemDecoration itemDecoration) | 设置分割线                                                   |
| setAroundRadius(float aroundRadius)                          | 设置四周圆角大小,默认20px                                    |
| setArrowHeight(float arrowHeight)                            | 设置箭头高度,默认25px                                        |
| setArrowWidth(float arrowWidth)                              | 设置箭头宽度,默认25px                                        |
| setArrowCenter(boolean arrowCenter)                          | 设置箭头是否在中心位置,默认false,此属性在右上角弹框模式下用不上 |
| setBubbleColor(@ColorInt int bubbleColor)                    | 设置Bubble颜色,默认红色(Color.RED)                           |
| setPaddingTop(int paddingTop)                                | 设置控件顶部内边距                                           |
| showAsDropDown(View anchor)                                  | 显示弹框                                                     |
| showAsDropDown(View anchor, int offsetX, int offsetY)        | 显示弹框                                                     |
| show(View anchor, Rect frame, Point origin)                  | 显示弹框                                                     |
| dismiss()                                                    | 取消弹框                                                     |




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
| unrl_no_more_first_time |   enum  | 第一次加载时,如果没有更多数据,底部显示什么,NOT_SHOW:默认值, 不显示 Footer,禁用加载更多了;NO_MORE: 显示没有更多数据  |
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




# VoiceAnimView

## **用法:**

```xml
    <cn.lvsong.lib.library.view.VoiceAnimView
        android:id="@+id/vav_test"
        android:layout_width="@dimen/width_30"
        android:layout_height="@dimen/height_30"
        android:layout_margin="@dimen/padding_20"
        android:rotation="90" />
```


## **属性介绍:**

| 属性名称          | 取值类型 | 取值和作用                 |
| ----------------- | -------- | -------------------------- |
| vav_arc_count     | integer  | 弧形线条数量,默认3条       |
| vav_normal_color  | color    | 正常颜色, 默认值 #c9c9c9   |
| vav_checked_color | color    | 选中颜色, 默认值 #4b4b4b   |
| vav_anim_duration | integer  | 动画执行时间,默认 2000毫秒 |

## **公共方法:**

| 方法名称               | 作用                                         |
| ---------------------- | -------------------------------------------- |
| setStyle(style: Style) | 设置样式,Style.ROUND(圆角), Style.RECT(直角) |
| startAnim()            | 开启动画                                     |
| stopAnim()             | 结束动画                                     |










# 在common库中,封装了大家常用的控件,有部分控件直接使用其他大佬的,在此感谢诸位大佬!感谢开源!如果用得上记得点赞收藏

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
        app:av_bezier_ratio="1.2"
        app:av_color="@color/color_2878FF"
        app:av_rectangle_height="@dimen/height_150" />
```

## **属性介绍:**

| 属性名称            | 取值类型             | 取值和作用                                  |
| ------------------- | -------------------- | ------------------------------------------- |
| av_rectangle_height | reference\|dimension | 矩形的高度,非圆弧部分高度                   |
| av_color            | reference\|color     | 背景色                                      |
| av_bezier_ratio     | float                | 贝塞尔曲线y点高度与控件高度的比值,默认是1.0 |

==PS: 如果设置控件高度为200,矩形高度(av_rectangle_height)为150,则弧形高度最大200 -150, 通过av_bezier_ratio可以改变其高度==

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
| bav_arrow_color   | color              | 箭头颜色                                                     |
| bav_stroke_width  | integer\|dimension | 箭头线宽                                                     |
| bav_arrow_padding | dimension\|integer | padding 使得里面 × 变小                                      |
| bav_arrow_style   | enum               | 箭头模式, material_design(Material Design),wechat_design(微信风格) |

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
| bv_tb_padding       | dimension\|integer | 上下间隔,默认3dp                                        |
| bv_lr_padding       | dimension\|integer | 左右间隔,默认5dp                                        |
| bv_stoke_width      | dimension\|integer | 轮廓宽度,默认1dp,                                       |
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
| bll_angle          | float\|dimension   | 四周圆角每一个圆角角度,默认20°                               |
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
| start()  | 开始旋转,如果控件可见就会自动执行,不必手动调用   |
| stop()   | 暂停旋转,如果控件不可见就会自动执行,不必手动调用 |




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

| 方法名称            | 作用           |
| ------------------- | -------------- |
| setColor(int color) | 设置叉叉的颜色 |




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

| 方法名称                                        | 作用             |
| ----------------------------------------------- | ---------------- |
| setOnSearchListener(listener: OnSearchListener) | 对各种操作的回调 |



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




# FixedCursorEditText
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
            android:layout_width="wrap_content"
            android:layout_height="@dimen/height_50"
            android:layout_marginTop="@dimen/padding_10"
            app:lirt_iconDrawable="@drawable/ic_baseline_alarm_add_24"
            app:lirt_spacing="@dimen/padding_5"
            app:lirt_style="iconBottom"
            app:lirt_text="文字在上,图片在下" />
```

## **属性介绍:**

| 属性名称               | 取值类型           | 取值和作用                                                |
| ---------------------- | ------------------ | --------------------------------------------------------- |
| lirt_backColor         | color              | 控件默认背景色                                            |
| lirt_backColorPress    | color              | 控件点击后背景色                                          |
| lirt_textColor         | color              | 文本默认颜色                                              |
| lirt_textColorPress    | color              | 文本点击后颜色                                            |
| lirt_iconDrawable      | reference          | 默认图标                                                  |
| lirt_checked           | boolean            | 是否被选中                                                |
| lirt_iconDrawablePress | reference          | 点击后图片                                                |
| lirt_text              | string             | 文本内容                                                  |
| lirt_textSize          | float\|dimension   | 文本大小                                                  |
| lirt_spacing           | dimension\|integer | 文本和图片的间隔                                          |
| lirt_style             | enum               | 设置图片所在方向,可取iconLeft,iconRight,iconUp,iconBottom |



## **公共方法:**

| 方法名称                                         | 作用                              |
| ------------------------------------------------ | --------------------------------- |
| setIconPosition(int position)                    | 设置图片所在方向                  |
| setBackColor(int backColor)                      | 设置控件背景色                    |
| setBackColorPress(int backColorPress)            | 设置控件被按下时的背景色          |
| setIconDrawable(Drawable iconDrawable)           | 设置icon的图片                    |
| setIconDrawablePress(Drawable iconDrawablePress) | 设置被按下时的icon的图片          |
| setTextColor(@ColorRes  int textColor)           | 设置文字的颜色                    |
| setTextColorPress(@ColorRes int textColorPress)  | 设置被按下时文字的颜色            |
| setText(CharSequence text)                       | 设置显示的文本内容                |
| getText()                                        | 获取显示的文本                    |
| setTextSize(float size)                          | 设置文本字体大小                  |
| setSpacing(int spacing)                          | 设置两个控件之间的间距,单位==dp== |




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

| 属性名称       | 取值类型         | 取值和作用                                        |
| -------------- | ---------------- | ------------------------------------------------- |
| psv_color      | color            | 颜色,必须设置                                     |
| psv_num        | integer          | 多边形多少条边，至少3条边，如果小于3条，会强制3条 |
| psv_line_width | float\|dimension | 边的线宽,默认1.5dp                                |



## **公共方法:**

暂无


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
| riv_mask_type    | enum               | 圆角的类型,矩形(RECTANGLE),CIRCLE(圆形),ROUNDRECTANGLE(四周圆角),ROUNDRECTANGLETOP(左上和右上有圆角) |



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
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/padding_20"
        app:sl_background_color="@color/color_E95C5B5B"
        app:sl_layout_radius="@dimen/padding_10"
        app:sl_offset_x="0dp"
        app:sl_offset_y="5dp"
        app:sl_shadow_color="@color/color_29b1b6d1"
        app:sl_shadow_radius="@dimen/padding_12">

```



## **属性介绍:**

| 属性名称            | 取值类型         | 取值和作用                                                   |
| ------------------- | ---------------- | ------------------------------------------------------------ |
| sl_shadow_color     | color            | 阴影颜色,默认颜色 #22000000,==注意颜色值必须是8位,即有alpha== |
| sl_background_color | color            | 背景颜色,默认Integer.MIN_VALUE                               |
| sl_offset_x         | dimension\|float | 阴影水平偏移,默认0                                           |
| sl_offset_y         | dimension\|float | 阴影垂直偏移,默认0                                           |
| sl_shadow_radius    | dimension\|float | 阴影圆角,默认0                                               |
| sl_layout_radius    | dimension\|float | ShadowLayout圆角,默认0                                       |



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

| 属性名称            | 取值类型         | 取值和作用                                |
| ------------------- | ---------------- | ----------------------------------------- |
| stv_default_color   | color            | 星星的默认颜色,默认Color.GREEN            |
| stv_checked_color   | color            | 星星的选中颜色,Color.RED                  |
| stv_num             | integer          | 星星的角个数,默认5个                      |
| stv_edge_line_width | float\|dimension | 边的线宽,默认1dp                          |
| stv_style           | enum             | 填充风格,fill(填满),stroke(描边),默认描边 |



## **公共方法:**




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



# TopImgAndBottomTextView
## **用法:**

具体参考: cn.lvsong.lib.demo.CustomActivity3

```xml
    <cn.lvsong.lib.library.view.TopImgAndBottomTextView
        android:layout_width="match_parent"
        android:layout_height="@dimen/height_80"
        android:layout_marginTop="@dimen/padding_10"
        app:tibt_checked_drawable="@drawable/ic_baseline_assignment_returned_24"
        app:tibt_iv_checked="false"
        app:tibt_iv_height="@dimen/height_20"
        app:tibt_iv_width="@dimen/width_20"
        app:tibt_normal_drawable="@drawable/ic_baseline_alarm_add_24"
        app:tibt_tv_margin_top="@dimen/padding_2"
        app:tibt_tv_text="TopImgAndBottomTextView实现上面图片,下面文字"
        app:tibt_tv_text_color="@color/color_333333"
        app:tibt_tv_text_size="@dimen/text_size_12" />
```



## **属性介绍:**

| 属性名称                   | 取值类型           | 取值和作用                 |
| -------------------------- | ------------------ | -------------------------- |
| tibt_normal_drawable       | reference          | 未选中Drawable             |
| tibt_checked_drawable      | reference          | 选中Drawable               |
| tibt_iv_width              | integer\|dimension | 图片宽度,默认30dp          |
| tibt_iv_height             | integer\|dimension | 图片高度,默认30dp          |
| tibt_state_checked         | boolean            | 是否被选中,默认false       |
| tibt_tv_margin_top         | integer\|dimension | 文本距离图片间隔,默认10dp  |
| tibt_tv_text               | string\|reference  | 文本内容                   |
| tibt_tv_text_color         | color              | 文字颜色,默认#999999       |
| tibt_tv_text_checked_color | color              | 选中时文字颜色,默认#333333 |
| tibt_tv_text_size          | float\|dimension   | 文字大小,默认14dp          |



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
| setArrowOffset(float offset)                                 | 设置箭头偏移量                                               |
| setItemDecoration(RecyclerView.ItemDecoration itemDecoration) | 设置分割线                                                   |
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












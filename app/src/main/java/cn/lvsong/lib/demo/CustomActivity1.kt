package cn.lvsong.lib.demo

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.demo.databinding.ActivityCustom1Binding
import cn.lvsong.lib.library.adapter.CommonAdapter
import cn.lvsong.lib.library.adapter.MultiItemTypeAdapter
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.decorator.LinearDividerDecoration
import cn.lvsong.lib.library.dialog.JAlertDialog
import cn.lvsong.lib.library.dialog.OnJAlertDialogCLickListener
import cn.lvsong.lib.library.popupwindow.HorizontalPosition
import cn.lvsong.lib.library.popupwindow.SmartPopupWindow
import cn.lvsong.lib.library.popupwindow.VerticalPosition
import cn.lvsong.lib.library.topmenu.MenuItem
import cn.lvsong.lib.library.topmenu.TopMenu
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.SelectorFactory
import cn.lvsong.lib.library.view.ArrangeView
import cn.lvsong.lib.library.view.MediumTextView
import cn.lvsong.lib.ui.BaseActivity
import cn.lvsong.lib.ui.BaseViewModel
import coil.load
import coil.transform.CircleCropTransformation

class CustomActivity1 : BaseActivity<ActivityCustom1Binding, BaseViewModel>() {

    override fun needUseImmersive() = 1

    private lateinit var mTopMenu: TopMenu

    private lateinit var mExitDialog: JAlertDialog
    private lateinit var mExitDialog2: JAlertDialog

    private lateinit var mPopupWindow: SmartPopupWindow
    private lateinit var mPopupWindow2: SmartPopupWindow

    override fun getLayoutId() = R.layout.activity_custom1

    override fun getViewBinging(view: View): ActivityCustom1Binding {
        return ActivityCustom1Binding.bind(view)
    }

    override fun setLogic() {
        initTopMenu()
        initExitDialog()
        initPop()

        setArrayViewAdapter()

        mBinding?.btn1?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn2?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn3?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn4?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()
    }

    private fun setArrayViewAdapter() {
        val data = arrayListOf<String>(
            "https://upload.jianshu.io/users/upload_avatars/2631077/dc99c361412c?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/1300228/1169f257-ab3b-44f2-bed2-57282511eb8f.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/7133325/f4370cf6-cf4d-4839-9b54-87beaa767d48?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/7290998/f64f5ef0-def0-4b26-beb3-b9d88f060ba0.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240",
            "https://upload.jianshu.io/users/upload_avatars/2558050/7761b285-2805-4534-9870-ba7dcc7538ec.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"
        )
        mBinding?.av?.setAdapter(object : ArrangeView.ArrangeAdapter(data) {
            override fun getRangeView(position: Int, parent: ViewGroup): View {
                val view = LayoutInflater.from(this@CustomActivity1)
                    .inflate(R.layout.item_arrange_view_peoples, parent, false)
                view.findViewById<AppCompatImageView>(R.id.iv_range_view).load(getItem(position)) {
                    transformations(CircleCropTransformation())  // https://www.jb51.net/article/208978.htm  / https://blog.csdn.net/vitaviva/article/details/113064062
                }

                return view
            }
        })
    }


    override fun bindEvent() {
        mBinding?.toolbar?.setMoreViewListener(View.OnClickListener {
            mTopMenu.show(it, null, null)
        })
    }


    private fun initTopMenu() {
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
                mTopMenu.dismiss()
                Toast.makeText(
                    this@CustomActivity1,
                    "点击了${data[position].text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val itemDecoration = LinearDividerDecoration.create(
            ContextCompat.getColor(this@CustomActivity1, R.color.color_EEEEEE),
            DensityUtil.dp2pxRtInt(1),DensityUtil.dp2pxRtInt(14),0,
            DensityUtil.dp2pxRtInt(14),0,RecyclerView.VERTICAL
        )
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
    }


    private fun initExitDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_exit_login_setting, null)
        view.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_FFFFFF))
            .setCornerRadius(DensityUtil.dp2pxRtInt(10F))
            .create()
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
                    mExitDialog.dismiss()
                }
            })
            .create() // 这里调用create()则返回dialog
//            .show() // 这里调用show()则直接显示

        val view2 = LayoutInflater.from(this).inflate(R.layout.dialog_exit_login_setting, null)
        view2.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_FFFFFF))
            .setCornerRadius(DensityUtil.dp2pxRtInt(10F))
            .create()

        mExitDialog2 = JAlertDialog.Builder(this@CustomActivity1)
            .setCancelable(false)
            .setContentView(view2)
            .setHasAnimation(false)
            .setFromBottom() // 还可以在顶部
            .setWidthAndHeight(
                DensityUtil.getScreenWidth() - DensityUtil.dp2pxRtInt(38F) * 2,
                DensityUtil.dp2pxRtInt(130F)
            )
            .setOnClick(R.id.btn_cancel_exit_dialog)
            .setOnClick(R.id.btn_sure_exit_dialog)
            .setOnJAlertDialogCLickListener(object : OnJAlertDialogCLickListener {
                override fun onClick(view: View, position: Int) {
                    mExitDialog2.dismiss()
                }
            }).create()
    }

    private fun initPop() {
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

        val view2 = LayoutInflater.from(this).inflate(R.layout.item_top_menu, null)
        view2.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_2878FF))
            .setCornerRadius(DensityUtil.dp2pxRtInt(10F))
            .create()
        view2.setOnClickListener {
            mPopupWindow2.dismiss()
        }
        mPopupWindow2 = SmartPopupWindow.Builder
            .build(this, view2)
            .setSize(DensityUtil.dp2pxRtInt(200), DensityUtil.dp2pxRtInt(168))
            .setAlpha(0.5f)                   //背景灰度     默认全透明
            .setOutsideTouchDismiss(false)    //点击外部消失  默认true（消失）
            .createPopupWindow()
    }

    /**
     * 中间dialog
     */
    fun onCenterDialog(view: View) {
        mExitDialog.delayShow(view, 500)
    }

    /**
     * 底部,  setFromBottom() --> 可以改为 setFromTop,则从顶部弹出
     */
    fun onBottomDialog(view: View) {
        mExitDialog2.show()
    }

    fun onPopupWindowShowAtAnchorView(view: View) {
        // 后面2个参数控制 Popup 方向
        mPopupWindow.showAtAnchorView(view, VerticalPosition.BELOW, HorizontalPosition.CENTER)

    }

    fun onShowAtLocation(view: View) {
        mPopupWindow2.showAtLocation(
            view, Gravity.CENTER,
            DensityUtil.dp2pxRtInt(10), DensityUtil.dp2pxRtInt(10)
        )
    }

}
package cn.lvsong.lib.library.topmenu;

/**
 * 每一项菜单
 * Created by Jooyer on 2017/2/10
 */
public class MenuItem {

    private int icon;
    private String text;
    public MenuItem() {
    }

    public MenuItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.runtoinfo.youxiao.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;


import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

@SuppressWarnings("all")
public class PopuMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

    private Context context;
    private View view;
    public PopuMenu(Context context, View anchor) {
        super(context, anchor);
        this.context = context;
        this.view = anchor;
    }

    public void showPopuMenu(List<Drawable> list){
        PopupMenu popupMenu = new PopuMenu(context, view);
        //MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        for (int i =0; i < list.size(); i++) {
            menu.add(Menu.NONE, Menu.FIRST + i, i, "").setIcon(list.get(i));
        }
        setIconEnable(menu, true);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void setIconEnable(Menu menu, boolean enable)
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //传入参数
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

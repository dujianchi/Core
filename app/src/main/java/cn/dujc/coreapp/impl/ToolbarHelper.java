package cn.dujc.coreapp.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.ui.IBaseUI;
import cn.dujc.coreapp.R;

public class ToolbarHelper implements IToolbar {

    @Override
    public View normal(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar_normal, parent, false);
    }

    @Override
    public int statusBarColor(Context context) {
        return ContextCompat.getColor(context, R.color.colorPrimaryDark);
    }

    @Override
    public int statusBarMode() {
        return LIGHT;
    }

    @Override
    public int toolbarStyle() {
        return LINEAR;
    }

    @Override
    public List<Class<? extends IBaseUI>> exclude() {
        return null;
    }
}

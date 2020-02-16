package cn.dujc.widget.tablayout;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface ITab<T> {

    public ITab<T> create();

    @NonNull
    public View getView(@NonNull ViewGroup parent);

    public void onTabUpdate(int position, T data);
    public void onTabSelected(int position);
    public void onTabUnselected(int position);

}

package cn.dujc.core.gallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.gallery.adapter.GalleryAdapter;
import cn.dujc.core.ui.impl.BaseListFragment;

public class GalleryFragment extends BaseListFragment {

    private final List<Uri> mList = new ArrayList<>();
    private boolean mHasPermission = false;

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new GalleryAdapter(mList);
    }

    @Nullable
    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        return new GridLayoutManager(mActivity, 3);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        loadData(mList.size());
    }

    @Override
    public void reload() {
        if (mHasPermission) {
            loadData(0);
        } else {
            permissionKeeper().requestPermissionsNormal(starter().newRequestCode(mActivity.getClass())
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        mHasPermission = true;
        reload();
    }

    private void loadData(int start) {
        ContentResolver resolver = mActivity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Images.Media._ID + " desc limit 50 offset " + start);
        if (start == 0) mList.clear();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                mList.add(imageUri);
            }
            notifyDataSetChanged(cursor.getCount() == 0);
            cursor.close();
        } else {
            notifyDataSetChanged(true);
        }
    }
}

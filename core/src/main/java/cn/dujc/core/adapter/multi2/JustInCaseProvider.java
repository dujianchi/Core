package cn.dujc.core.adapter.multi2;

import android.content.Context;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;

/**
 * 无用的，不占布局大小的，为了类型安全而引入的一个adapter实现器
 */
public class JustInCaseProvider implements ViewProvider {

    @Override
    public int layoutId() {
        return R.layout.core_adapter_just_in_case;
    }

    @Override
    public void convert(Context context, BaseQuickAdapter adapter, BaseViewHolder helper, Object data) { }

}

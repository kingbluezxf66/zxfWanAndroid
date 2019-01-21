package com.example.zxf.myapp.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.SystemBean;
import java.util.List;
import java.util.Random;

public class SystemMultiAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SystemMultiAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_system_first);
        addItemType(TYPE_LEVEL_1, R.layout.item_system_second);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int[] imageList = {R.mipmap.an_bear, R.mipmap.an_bear, R.mipmap.an_cat, R.mipmap.an_chicken, R.mipmap.an_ciwei,
                R.mipmap.an_cow, R.mipmap.an_dog, R.mipmap.an_dragon, R.mipmap.an_eagle, R.mipmap.an_elephant, R.mipmap.an_fox,};
        Random random = new Random();
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                SystemBean systemFirst = (SystemBean) item;
                helper.setImageResource(R.id.iv_show, imageList[random.nextInt(11)])
                        .setImageResource(R.id.iv_entry, systemFirst.isExpanded() ? R.mipmap.ic_down : R.mipmap.ic_entry)
                        .setText(R.id.tv_name, systemFirst.getName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = helper.getAdapterPosition();
                        if (systemFirst.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                SystemBean.ChildrenBean systemSecond = (SystemBean.ChildrenBean) item;
                       helper.setText(R.id.tv_name, systemSecond.getName())
                       .addOnClickListener(R.id.tv_name);
                break;
        }
    }
}

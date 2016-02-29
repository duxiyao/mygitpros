//package com.kjstudy.frag.adapter;
//
//import org.kymjs.kjframe.ui.BindView;
//
//import android.widget.TextView;
//
//import com.imbase.R;
//import com.kjstudy.bean.data.ContentEntity;
//import com.kjstudy.bean.data.TSUserInfo;
//import com.kjstudy.core.util.cache.CacheManager;
//import com.kjstudy.ext.AdapterBase;
//import com.kjstudy.plugin.CircleImageView;
//
//public class AdapterMapItem extends
//        AdapterBase<ContentEntity, AdapterMapItem.Holder> {
//    static class Holder {
//        @BindView(id = R.id.civ)
//        CircleImageView civ;
//
//        @BindView(id = R.id.tv_addr)
//        TextView tvAddr;
//    }
//
//    @Override
//    protected int getItemLayout() {
//        return R.layout.item_layout_map;
//    }
//
//    @Override
//    protected void process(int position, Holder h) {
//        ContentEntity m = mDatas.get(position);
//        TSUserInfo uinfo = m.getUinfo();
//        if (uinfo != null)
//            CacheManager.inflateHeadFront(uinfo.getPhotoUrl(), h.civ,
//                    R.drawable.personal_head_round);
//        h.tvAddr.setText(m.getAddress());
//    }
//}

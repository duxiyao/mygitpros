package com.kjstudy.frag;

import java.util.List;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.imbase.R;
import com.kjstudy.bean.EntityT;
import com.kjstudy.bean.data.ContentEntity;
import com.kjstudy.bean.data.EnLocalPOI;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.net.interfacebean.IEnSearchPOI;
import com.kjstudy.core.util.GUtil;
import com.kjstudy.core.util.JsonUtil;
import com.kjstudy.core.util.cache.CacheManager;
import com.kjstudy.ext.AdapterBase;
import com.kjstudy.plugin.CircleImageView;

public class NearByListFrag extends BFrag {

    OnRefreshListener2<ListView> mLis = new OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//            doRefreshCurPageAll();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            doRefreshNextPage();
        }
    };

    HttpCallBack mCb = new HttpCallBack() {
        public void onSuccess(String t) {
            if (GUtil.isEmpty(t))
                return;
            try {
                EntityT<EnLocalPOI> ens = JsonUtil.json2ET(t, EnLocalPOI.class);
                EnLocalPOI elp = ens.getData();
                mServerAllCount = elp.getTotal();
                if (elp.getStatus() == 0 && elp.getSize() > 0) {
                    mCurPageIndex++;
                    List<ContentEntity> datas = elp.getContents();
                    if(datas!=null&&datas.size()>0)
                        mAdapterMapItem.addDatas(datas);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        };

        public void onFinish() {
            mPullToR.onRefreshComplete();
        };
    };

    @BindView(id = R.id.lv_nearby)
    private PullToRefreshListView mPullToR;

    private ListView mLvNearBy;

    private int mCurPageIndex = 0;

    private int mServerAllCount;

    private AdapterMapItem mAdapterMapItem;

    private final String PAGE_COUNT = "10";

    @Override
    protected int getLayoutId() {
        return R.layout.frag_layout_nearby_list;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mAdapterMapItem = new AdapterMapItem();
        mPullToR.setOnRefreshListener(mLis);
        mLvNearBy = mPullToR.getRefreshableView();
        mLvNearBy.setAdapter(mAdapterMapItem);
        doRefreshNextPage();
    }

    private void doRefreshCurPageAll() {
        IEnSearchPOI en = new IEnSearchPOI();
        en.setType("by_register_time");
        en.setPage_index("0");
        en.setPage_size(String.valueOf(mCurPageIndex*Integer.parseInt(PAGE_COUNT)));
        Req.searchPOI(en, mCb);
    }

    private void doRefreshNextPage() {
        IEnSearchPOI en = new IEnSearchPOI();
        en.setPage_index(String.valueOf(mCurPageIndex));
        en.setPage_size(PAGE_COUNT);
        en.setType("by_register_time");
        Req.searchPOI(en, mCb);
    }

    public static class AdapterMapItem extends
            AdapterBase<ContentEntity, AdapterMapItem.Holder> {
        public static class Holder {
            @BindView(id = R.id.civ)
            CircleImageView civ;

            @BindView(id = R.id.tv_addr)
            TextView tvAddr;
        }

        @Override
        protected int getItemLayout() {
            return R.layout.item_layout_map;
        }

        @Override
        protected void process(int position, Holder h) {
            ContentEntity m = mDatas.get(position);
            TSUserInfo uinfo = m.getUinfo();
            if (uinfo != null)
                CacheManager.inflateHeadFront(uinfo.getPhotoUrl(), h.civ,
                        R.drawable.personal_head_round);
            h.tvAddr.setText(m.getAddress());
        }
    }
}

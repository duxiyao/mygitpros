package com.kjstudy.ext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.utils.DensityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imbase.MyApplication;
import com.imbase.R;

public abstract class AdapterBaseN<D, H> extends BaseAdapter {
    protected Context mContext;

    protected List<D> mDatas;

    private View emptyView;

    private ViewGroup parent;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 空数据时，充满listview剩下所有布局显示。
     */
    public final static int MATCH_REMAINING_PLACE = 0x00000000;

    /**
     * 空数据显示的view需要多大空间，就使用多大空间，自适应布局。
     */
    public final static int WRAP_REMAINING_PLACE = 0x00000001;

    /**
     * 显示方式。 {@link #MATCH_REMAINING_PLACE} {@link #WRAP_REMAINING_PLACE}
     */
    private int displayPlace = MATCH_REMAINING_PLACE;

    /**
     * 空数据时的布局资源文件Id。
     */
    private int resLayoutId = R.layout.empty;

    private String mShowTxtWhenEmpty;

    // private int defaultNetBrokenLayId = R.layout.load_failed_hint;
    //
    // private View netBrokenView;
    //
    // public void showNetBrokenView(View v,ViewGroup parent) {
    // if (v == null) {
    // netBrokenView = LayoutInflater.from(mContext).inflate(
    // defaultNetBrokenLayId, null);
    // } else {
    // netBrokenView = v;
    // }
    // }

    /**
     * 设置空数据时显示的资源文件Id。
     * 
     * @param resLayoutId
     */
    public void setEmptyView(int resLayoutId) {
        this.resLayoutId = resLayoutId;
    }

    public void setEmptyView(View v) {
        emptyView = v;
    }

    /**
     * 设置空数据显示时的方式。全显示或者自适显示。
     * 
     * @param displayPlace
     */
    public void setDisplayPlace(int displayPlace) {
        this.displayPlace = displayPlace;
    }

    private boolean checkItemSize() {
        if (mDatas == null || mDatas.size() == 0)
            return true;
        return false;
    }

    protected AdapterBaseN() {
        this.mContext = MyApplication.getInstance().getApplicationContext();
    }

    public void setDatas(List<D> datas) {
        setDatas(datas, true);
    }

    public void add(D d) {
        if (null == mDatas)
            mDatas = new ArrayList<D>();
        if (mDatas.contains(d))
            remove(d, false);

        List<D> tmp = new ArrayList<D>();
        for (D t : mDatas) {
            tmp.add(t);
        }
        tmp.add(d);
        setDatas(tmp);
    }

    public void remove(D d) {
        remove(d, true);
    }

    public void setDatas(List<D> datas, boolean isNotify) {
        if (null != mDatas)
            mDatas.clear();
        List<D> tmp = new ArrayList<D>();
        if (null != datas) {
            for (D d : datas) {
                tmp.add(d);
            }
        }
        this.mDatas = tmp;
        if (isNotify) {
            if (!Looper.getMainLooper().getThread().getName()
                    .equals(Thread.currentThread().getName())) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            } else
                notifyDataSetChanged();
        }
    }

    private void remove(D d, boolean isNotify) {
        if (null == mDatas || mDatas.size() <= 0)
            return;
        List<D> tmp = new ArrayList<D>();
        if (mDatas.contains(d)) {
            for (D t : mDatas) {
                if (!t.equals(d))
                    tmp.add(t);
            }
        }

        if (isNotify)
            setDatas(tmp);
        else
            setDatas(tmp, isNotify);
    }

    @Override
    public int getCount() {
        if (checkItemSize())
            return 1;
        if (parent != null) {
            parent.removeViewInLayout(emptyView);
        }
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mDatas || position > mDatas.size())
            return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected int getItemLayout() {
        try {
            Class cls = getGenericType(1);
            BindLayout bl = (BindLayout) cls.getAnnotation(BindLayout.class);
            if (bl != null)
                return bl.id();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected abstract void process(int position, D d, H h);

    private View getEmptyView(int position, View convertView, ViewGroup parent) {
        final int screenWidth = DensityUtils.getScreenW();
        final int screenHeight = DensityUtils.getScreenH();
        final int listViewBottom = parent.getBottom();
        AbsListView.LayoutParams params = null;
        switch (displayPlace) {
        case MATCH_REMAINING_PLACE:
            params = new AbsListView.LayoutParams(screenWidth, listViewBottom);
            break;
        case WRAP_REMAINING_PLACE:
            params = new AbsListView.LayoutParams(screenWidth,
                    LayoutParams.WRAP_CONTENT);
            break;
        default:
            break;
        }
        if (emptyView == null)
            emptyView = LayoutInflater.from(mContext)
                    .inflate(resLayoutId, null);
        emptyView.setLayoutParams(params);
        emptyView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mShowTxtWhenEmpty)) {
            showWhenEmpty();
        }
        return emptyView;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (this.parent == null) {
            this.parent = parent;
        }
        if (checkItemSize()) {
            return getEmptyView(position, convertView, parent);
        }
        H holder = null;
        if (null == convertView || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    getItemLayout(), null);

            holder = initHolder(position, convertView);
        } else {
            holder = (H) convertView.getTag();
        }

        if (holder != null) {
            if (null != mDatas && position < mDatas.size()
                    && mDatas.get(position) != null) {
                try {
                    process(position, mDatas.get(position), holder);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return convertView;
    }

    protected H initHolder(int position, View convertView) {
        H holder = getHolder();
        if (holder != null){
            AnnotateUtil.FATHERNAME=BaseAdapter.class.getName();
            AnnotateUtil.initBindView(holder, convertView);
        }
        if (holder != null)
            convertView.setTag(holder);
        return holder;
    }

    private H getHolder() {
        try {
            Class cls = getGenericType(1);
            Object obj = cls.newInstance();
            return (H) obj;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public void setShowTxtWhenEmpty(String txt) {
        mShowTxtWhenEmpty = txt;
    }

    private void showWhenEmpty() {
        if (emptyView != null && emptyView instanceof TextView) {
            if (R.layout.empty == emptyView.getId()) {
                ((TextView) emptyView.findViewById(R.id.tv))
                        .setText(mShowTxtWhenEmpty);
            } else
                ((TextView) emptyView).setText(mShowTxtWhenEmpty);
        }
    }
}

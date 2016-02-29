package com.kjstudy.ext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.utils.DensityUtils;

import android.content.Context;
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

public abstract class AdapterBase<T, V> extends BaseAdapter {
    protected Context mContext;

    protected List<T> mDatas;

    private View emptyView;

    private ViewGroup parent;

    // private HashMap<Integer, V> mHolderMem;
    // private WeakHashMap<Integer, View> mHolderView;

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

    protected AdapterBase() {
        this.mContext = MyApplication.getInstance().getApplicationContext();
        // mHolderMem = new HashMap<Integer, V>();
        // mHolderView = new WeakHashMap<Integer, View>();
    }

    protected AdapterBase(Context context) {
        this.mContext = context;
        // mHolderMem = new HashMap<Integer, V>();
        // mHolderView = new WeakHashMap<Integer, View>();
    }

    public void setDatas(List<T> datas) {
        setDatas(datas, true);
    }

    public void add(T o) {
        if (null == mDatas)
            mDatas = new ArrayList<T>();
        if (mDatas.contains(o))
            remove(o, false);

        List<T> tmp = new ArrayList<T>();
        for (T t : mDatas) {
            tmp.add(t);
        }
        tmp.add(o);
        setDatas(tmp);
    }

    public void remove(T o) {
        remove(o, true);
    }

    private void setDatas(List<T> datas, boolean isNotify) {
        if (null != mDatas)
            mDatas.clear();
        List<T> tmp = new ArrayList<T>();
        if (null != datas) {
            for (T t : datas) {
                tmp.add(t);
            }
        }
        this.mDatas = tmp;
        if (isNotify)
            notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        addDatas(datas, true);
    }

    public void addDatas(List<T> datas, boolean isNotify) {
        if (null == mDatas)
            mDatas = new ArrayList<T>();
        if (null != datas) {
            for (T t : datas) {
                mDatas.add(t);
            }
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    private void remove(T o, boolean isNotify) {
        if (null == mDatas || mDatas.size() <= 0)
            return;
        List<T> tmp = new ArrayList<T>();
        if (mDatas.contains(o)) {
            for (T t : mDatas) {
                if (!t.equals(o))
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

    protected abstract int getItemLayout();

    protected V init(int position, View v) {
        return null;
    }

    protected abstract void process(int position, V h);

    protected void multiplex(int position, V h) {
    }

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
        V holder = null;
        if (null == convertView || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    getItemLayout(), null);

            holder = initHolder(position, convertView);
        } else {
            holder = (V) convertView.getTag();
            multiplex(position, holder);
        }

        // mViewMem.get(position);
        if (holder != null) {
            process(position, holder);
            // process(position, holder, assis);
        }

        // View v = mHolderView.get(position);

        return convertView;
    }

    protected V initHolder(int position, View convertView) {
        V holder;
        holder = init(position, convertView);
        if (holder == null) {
            holder = getHolder();
            if (holder != null){
                AnnotateUtil.FATHERNAME=BaseAdapter.class.getName();
                AnnotateUtil.initBindView(holder, convertView);
            }
        }
        if (holder != null)
            convertView.setTag(holder);
        return holder;
    }

    private V getHolder() {
        try {
            return (V) getGenericType(1).newInstance();
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

    protected <W> W find(int id, View v) {
        if (null == v)
            throw new RuntimeException(
                    "error:convertView can not load null . In AdapterBase2.getView through LayoutInflater.from(mContext).inflate");
        return (W) v.findViewById(id);
    }

    protected void setTxt(TextView tv, String s) {
        if (TextUtils.isEmpty(s))
            tv.setText("");
        else
            tv.setText(s);
    }
}

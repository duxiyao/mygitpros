package com.kjstudy.test.act;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.imbase.R;
import com.kjstudy.test.view.ImgView;

public class ImgHeadAct extends KJActivity {

	@BindView(id = R.id.iv)
	private ImgView mIv;

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_img_head);
	}

	@Override
	public void initWidget() {
		super.initWidget();

		List<String> l = new ArrayList<String>();
		l.add("http://202.106.149.197:8083/icallImgs/20150907111906808.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150428102022735.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150506061834330.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150206071226060.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150907111906808.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150428102022735.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150506061834330.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150206071226060.jpg");
		l.add("http://202.106.149.197:8083/icallImgs/20150907111906808.jpg");
		mIv.setImgs(l);
	}
}

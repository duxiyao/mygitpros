package com.vending.machines.act;

import java.util.List;

import org.myframe.MActivity;
import org.myframe.ui.BindLayout;
import org.myframe.ui.BindView;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dwin.dwinapi.MySerialPort;
import com.dwin.dwinapi.OnReceSerialPortData;
import com.vending.machines.R;

/**
 * @author Administrator
 * 首页，选择商品
 */
@BindLayout(barId = R.layout.bar_txt_btn, contentId = R.layout.act_choice)
public class ChoiceAct extends MActivity {

	@BindView(id = R.id.btn_obtain)
	private Button mObtainBtn;

	@Override
	protected void onResume() {
		super.onResume();
		MySerialPort.getDefault().setOnRecePortData(new OnReceSerialPortData() {

			@Override
			public void onReceive(String datas) {
				 System.out.println("---->"+datas);
			}

			@Override
			public void onError(String ermsg, String datas) {
				System.out.println("OnReceSerialPortData.onError");
			}
		});
		mObtainBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MySerialPort.getDefault().queryRiceLeft();
			}
		});
	}
}

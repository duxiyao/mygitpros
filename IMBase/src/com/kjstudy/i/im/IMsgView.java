package com.kjstudy.i.im;

import android.content.Context;
import android.view.View;

import com.kjstudy.bean.ImMessage;

public interface IMsgView {
	View getView(ImMessage msg);
}

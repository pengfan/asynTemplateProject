package com.codingpower.loadstyle.asyn;

import com.codingpower.loadstyle.asyn.event.LoadingFinishEvent;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class AsynManager {
	private Context mContext;

	private RelativeLayout mInnerLayout;
	private ProgressBar mProgressBar;
	private RelativeLayout mParentLayout;
	private LoadingFinishListener mLoadingFinishListener;

	private boolean mLoading;

	public AsynManager(RelativeLayout relativeLayout, boolean loading) {
		mContext = relativeLayout.getContext();
		LayoutParams layoutParams = relativeLayout.getLayoutParams();
		ViewGroup parent = (ViewGroup) relativeLayout.getParent();

		int index = parent.indexOfChild(relativeLayout);
		mParentLayout = new RelativeLayout(relativeLayout.getContext());
		parent.removeView(relativeLayout);
		parent.addView(mParentLayout, index);
		mParentLayout.setLayoutParams(layoutParams);
		mParentLayout.addView(relativeLayout);
		mInnerLayout = relativeLayout;
		initStatusViews();
		setLoadingStatus(loading);
		EventBus.getDefault().register(this);
	}

	public void initStatusViews() {
		mProgressBar = new ProgressBar(mContext);
		mParentLayout.addView(mProgressBar);
		RelativeLayout.LayoutParams rLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		mProgressBar.setLayoutParams(rLayoutParams);
	}

	public void setLoadingStatus(boolean loading) {
		mLoading = loading;
		if (loading) {
			mProgressBar.setVisibility(View.VISIBLE);
			mInnerLayout.setVisibility(View.GONE);
		} else {
			mProgressBar.setVisibility(View.GONE);
			mInnerLayout.setVisibility(View.VISIBLE);
		}
	}

	public void onEventMainThread(LoadingFinishEvent event) {
		if (mLoadingFinishListener != null) {
			mLoadingFinishListener.onFinish(event);
		}
		setLoadingStatus(false);
	}

	public void destory() {
		EventBus.getDefault().unregister(this);
	}

	public void setLoadingFinishListener(LoadingFinishListener listener) {
		mLoadingFinishListener = listener;
	}

	public static interface LoadingFinishListener {
		public void onFinish(LoadingFinishEvent event);
	}

}

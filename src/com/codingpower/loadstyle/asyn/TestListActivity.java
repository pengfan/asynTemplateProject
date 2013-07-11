package com.codingpower.loadstyle.asyn;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.codingpower.loadstyle.asyn.AsynManager.LoadingFinishListener;
import com.codingpower.loadstyle.asyn.event.LoadingFinishEvent;

import de.greenrobot.event.EventBus;

public class TestListActivity extends BaseActivity {

	private RelativeLayout asynLayout;
	private AsynManager asynManager;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_list);
		asynLayout = findView(R.id.asynLayout);
		ListView listView = findView(R.id.listView);
		adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		asynManager = new AsynManager(asynLayout, true);
		initAsynStatus();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<String> res = new ArrayList<String>();
				for(int i = 0 ; i < 20; i++)
				{
					res.add("test" + i);
				}
				LoadingFinishEvent event = new LoadingFinishEvent(res);
				event.setResult(res);
				EventBus.getDefault().post(event);
			}
		}).start();
	}

	private void initAsynStatus() {
		asynManager.setLoadingFinishListener(new LoadingFinishListener() {
			@Override
			public void onFinish(LoadingFinishEvent event) {
				Object item = null;
				if (event.getResult() != null && !event.getResult().isEmpty()) {
					item = event.getResult().get(0);
				}
				if (item != null && item instanceof String) {
					adapter.addAll(event.getResult());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		asynManager.destory();
	}
	
}

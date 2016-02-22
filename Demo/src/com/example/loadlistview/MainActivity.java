package com.example.loadlistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztx.pullloadlistview.PullLoadListview;
import com.ztx.pullloadlistview.PullLoadListview.OnPullLoadListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements OnPullLoadListener {
	private PullLoadListview listview;
	private List<Map<String, String>> dataList = new ArrayList<>();
	private SimpleAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (PullLoadListview) findViewById(R.id.listview);
		initListView();
		initDataList();
		initAdapter();

	}

	private void initListView() {
		PullLoadListview.mLayout = "bottom_footer";
		listview.setOnPullLoadListener(this);
	}

	private void initAdapter() {
		mAdapter = new SimpleAdapter(this, dataList, R.layout.item_lv,
				new String[] { "ItemTitle", "ItemText" }, new int[] {
						R.id.tv_01, R.id.tv_02 });
		listview.setAdapter(mAdapter);
	}

	private void initDataList() {
		for (int i = 0; i < 30; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title....." + i);
			map.put("ItemText", "This is text.....");
			dataList.add(map);
		}
	}

	@Override
	public void onLoad() {
		initDataList();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				listview.onPullLoadCompleteLoad();
				mAdapter.notifyDataSetChanged();
			}
		}, 3000);
	}

}

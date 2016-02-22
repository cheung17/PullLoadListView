package com.ztx.pullloadlistview;

import com.ztx.pulloadlistview.util.MResource;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PullLoadListview extends ListView implements OnScrollListener {
	private OnPullLoadListener onPullLoadListener;
	private View mFooterView;
	private int mLastVisibleItem;
	private int mTotalItem;
	private boolean mIsloading = false;
	public static String mLayout = "";
	private Context mContext;

	public PullLoadListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public PullLoadListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public PullLoadListview(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		mContext = context;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mTotalItem = totalItemCount;
		// 最后一个可见的Item的position 就等于第一个可见item的position + 当前可见item数量
		mLastVisibleItem = firstVisibleItem + visibleItemCount;
	}

	public void setOnPullLoadListener(OnPullLoadListener onPullLoadListener) {
		this.onPullLoadListener = onPullLoadListener;
		this.setOnScrollListener(this);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		try {
			mFooterView = inflater.inflate(MResource.getIdByName(mContext,
					"layout", PullLoadListview.mLayout), null, false);
		} catch (Exception e) {
			Log.i("PullLoadListView", "no footerView layout");
		}

		this.addFooterView(mFooterView);
		mFooterView.setVisibility(View.GONE);
	}

	/**
	 * * * scrollState=SCROLL_STATE_TOUCH_SCROLL :仍在滑动 *
	 * scrollState=SCROLL_STATE_FLING 页面惯性滑动 * scrollState=SCROLL_STATE_IDLE
	 * :滑动结束
	 * 
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 当最会一个可见的item的position等于总item且不为0时且停止滚动及达到加载条件
		if (scrollState == SCROLL_STATE_IDLE && mLastVisibleItem != 0
				&& mLastVisibleItem == mTotalItem) {
			// 达到上拉加载条件
			if (!mIsloading) {
				// 如果不是正在加载状态,达到加载条件
				onPullLoadListener.onLoad();
				// 设置状态为正在加载
				mIsloading = true;
				mFooterView.setVisibility(View.VISIBLE);

			}

		}
	}

	public void onPullLoadCompleteLoad() { // 加载完成时回调
		// 设置状态为刷新完毕
		mIsloading = false;
		mFooterView.setVisibility(View.GONE);
		// footerView.findViewById(R.id.load_layout).setVisibility(View.GONE);
	}

	public interface OnPullLoadListener {
		public void onLoad();
	}
}

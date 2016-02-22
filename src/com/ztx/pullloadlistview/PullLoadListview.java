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
		// ���һ���ɼ���Item��position �͵��ڵ�һ���ɼ�item��position + ��ǰ�ɼ�item����
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
	 * * * scrollState=SCROLL_STATE_TOUCH_SCROLL :���ڻ��� *
	 * scrollState=SCROLL_STATE_FLING ҳ����Ի��� * scrollState=SCROLL_STATE_IDLE
	 * :��������
	 * 
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// �����һ���ɼ���item��position������item�Ҳ�Ϊ0ʱ��ֹͣ�������ﵽ��������
		if (scrollState == SCROLL_STATE_IDLE && mLastVisibleItem != 0
				&& mLastVisibleItem == mTotalItem) {
			// �ﵽ������������
			if (!mIsloading) {
				// ����������ڼ���״̬,�ﵽ��������
				onPullLoadListener.onLoad();
				// ����״̬Ϊ���ڼ���
				mIsloading = true;
				mFooterView.setVisibility(View.VISIBLE);

			}

		}
	}

	public void onPullLoadCompleteLoad() { // �������ʱ�ص�
		// ����״̬Ϊˢ�����
		mIsloading = false;
		mFooterView.setVisibility(View.GONE);
		// footerView.findViewById(R.id.load_layout).setVisibility(View.GONE);
	}

	public interface OnPullLoadListener {
		public void onLoad();
	}
}

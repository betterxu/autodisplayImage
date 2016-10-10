package com.itheima.autoadvertisment.view;

import java.util.ArrayList;
import java.util.List;

import com.example.autoadvertiment.R;
import com.itheima.autoadvertisment.cache.ImageCache;



import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class AutoScrollViewPager extends ViewPager {

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		imageCache = new ImageCache(getContext());
	}
	private boolean isLooping = false;
	private int pageCount = 2;
	private List<String> titles = null;
	private TextView titleView;
	private ImageCache imageCache = null;
	private List<String> imgurls = new ArrayList<String>();
	private List<ImageView> dots = new ArrayList<ImageView>();
	private int currPageIndex = 0;
	public Handler mHandler;
	private LinearLayout layoutDot;
	private  int mpreposition;
	private OnViewItemClickListener listener;
	public void setTitles(List<String> titles, TextView titleView) {
		this.titles = titles;
		this.titleView = titleView;
		this.titleView.setText(titles.get(0));
	}
	public void setImgurls(List<String> imgurls) {
		this.imgurls = imgurls;
	}
	public void setLooping(boolean flag) {
		isLooping = flag;
	}
	public void init(int pageNumber, LinearLayout layoutDot)
	{  mHandler=new Handler();
		pageCount = pageNumber;
		this.layoutDot=layoutDot;
		
		Log.e("check", pageCount+"");
		for(int i=0;i<pageCount;i++)
		{
			ImageView imageView =new ImageView(getContext());
			imageView.setImageResource(R.drawable.dots_selector);
			imageView.setSelected(false);
      LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
    		  LinearLayout.LayoutParams.WRAP_CONTENT);
	p.rightMargin=6;
      
		if(i==0)
		{
			imageView.setSelected(true);
			
		}
		layoutDot.addView(imageView, p);
		}
		PagerAdapter adapter=new ImageViewAdapter();
		this.setAdapter(adapter);
		OnPageChangeListener listener=new MyViewPagerListener();
		this.setOnPageChangeListener(listener);
	}
	private class MyViewPagerListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
		int position=arg0%pageCount;
		ImageView child=(ImageView) layoutDot.getChildAt(position);
		child.setSelected(true);
		ImageView prechild=(ImageView) layoutDot.getChildAt(mpreposition);
		prechild.setSelected(false);;
		mpreposition=position;	
		if (titleView != null && titles != null) {
			titleView.setText(titles.get(position ));
		}
		}
		
		
	} 
private class ImageViewAdapter extends PagerAdapter
{

	@Override
	public int getCount() {
		if(isLooping)
		{
			Log.e("check", "count+");
			return Integer.MAX_VALUE;
		}else {
			Log.e("check", pageCount+"de");
			return pageCount;
		}
		
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.e("check", "image+");
		ImageView image=new ImageView(getContext());
		image.setScaleType(ScaleType.CENTER_CROP);
		image.setBackgroundResource(R.drawable.home_scroll_default);
		ViewPager.LayoutParams p = new ViewPager.LayoutParams();
		p.width = ViewPager.LayoutParams.MATCH_PARENT;
		p.height = ViewPager.LayoutParams.MATCH_PARENT;
		container.addView(image, p);
		
    if(imgurls != null&&imgurls.size()>0)
    {
    	imageCache.disPlayImage(image, imgurls.get(position%pageCount));
    	
    }
	
    OnTouchListener listener=new  MyOnTouchListener();
    image.setOnTouchListener(listener);
   
	return image;
	}
	private class MyOnTouchListener implements OnTouchListener
	{
		private int downX = 0;
	private long downTime = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopScorll();
			downX=(int) event.getX();
			downTime=System.currentTimeMillis();
			break;
		   case MotionEvent.ACTION_MOVE:
			   Log.i("check", "move+");
				break;
		   case MotionEvent.ACTION_UP:
			   int upX = (int) event.getX();
			   if(System.currentTimeMillis()-downTime<300&&downX==upX)
			   {
				   if(listener!=null)
				   {
					   listener.onItemClick("yes Im Scrolling");
					   
				   }
				  
			   }
			   Log.i("check", "up+");
			   startScorll();
				break;
       case MotionEvent.ACTION_CANCEL://页面滑动时监听
    	  
    	   Log.i("check", "cancel+");
    	   startScorll();
			break;
		default:
			break;
		}
		  	return true;
		  	//onTouchEvent的返回值如果为true 表示你已经处理了此时间 他的父组件将不会继续处理 
            // 如果为false 则他的父组件将继续处理此事件 ,如果有点击事件需要设置为false
            //监听不到ACTION_MOVE事件 可能是他的子控件onTouchEvent返回true了 或者 他的父组件拦截了此事件
		}
		
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((ImageView)object);
	}
	}

public void startScorll(){
	Log.e("check", "satrt+");
	new task().start();
}
public void stopScorll()
{Log.e("check", "stop+");
mHandler.removeCallbacksAndMessages(null);	
}
class task implements  Runnable {
	public void start(){
		// 要将之前发送的消息移除掉,避免发送重复消息
	mHandler.removeCallbacksAndMessages(null);// 移除所有消息和Runnable(post)
	mHandler.postDelayed(this, 3000);// 发送延时3秒的Runnable
		System.out.println("1");
	}
	public void run() {
		int currentitem=getCurrentItem();
		currentitem++;
	setCurrentItem(currentitem);
	System.out.println("2");
	mHandler.postDelayed(this, 3000);
		
	}
}
public static interface OnViewItemClickListener
{
	public void onItemClick(String str);
	}
public void setOnViewItemClickListener(OnViewItemClickListener listener)
{
	this.listener=listener;
}
}


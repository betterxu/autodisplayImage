package com.example.autoadvertisment;

import java.util.ArrayList;
import java.util.List;

import com.example.autoadvertiment.R;
import com.itheima.autoadvertisment.view.AutoScrollViewPager;
import com.itheima.autoadvertisment.view.AutoScrollViewPager.OnViewItemClickListener;

import android.app.Activity;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	AutoScrollViewPager sroll_viewpager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sroll_viewpager = (AutoScrollViewPager) findViewById(R.id.sroll_viewpager);
		LinearLayout layoutDot = (LinearLayout) findViewById(R.id.layout_dots);
		List<String> imgurl=new ArrayList<String>();
		imgurl.add(	"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1143767357,3667706699&fm=58");
		imgurl.add(	"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2055558260,722501473&fm=58");
		imgurl.add(	"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=4265802323,3532472824&fm=58");
				
	sroll_viewpager.setImgurls(imgurl);
	List<String> titles=new ArrayList<String>();
	
	titles.add("最最最最!");
	titles.add("込込込込込込込!");
	titles.add("狎狎狎狎!");
	TextView titleView=(TextView) findViewById(R.id.titleview);
	sroll_viewpager.setLooping(true);
	sroll_viewpager.init(titles.size(), layoutDot);
	sroll_viewpager.setTitles(titles, titleView);
	
	sroll_viewpager.setOnViewItemClickListener(new OnViewItemClickListener() {
		
		@Override
		public void onItemClick(String str) {
			Toast.makeText(getBaseContext(), str, 0).show();
			
		}
	});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		sroll_viewpager.startScorll();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		sroll_viewpager.stopScorll();
	}
	
}

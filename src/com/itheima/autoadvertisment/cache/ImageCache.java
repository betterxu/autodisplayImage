package com.itheima.autoadvertisment.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageCache {
	private LruCache<String, Bitmap> cache = null;
 private Context context;
 private Handler mHandler;
	
	private ExecutorService threadPool = null;
	private File localDir = null;
	public ImageCache(Context context) {
		this.context=context;
		localDir=context.getCacheDir();
	     threadPool=Executors.newFixedThreadPool(4);
	  
	long maxSize=Runtime.getRuntime().maxMemory()/8;
	cache=new LruCache<String, Bitmap>((int) maxSize)
	{
		@Override
		protected int sizeOf(String key, Bitmap value) {
			
			int rowbytes=value.getRowBytes();
			int rowcount=value.getHeight();
			return rowbytes*rowcount;
		}
	};
	}
	public  void disPlayImage(ImageView imageView, String url) {
		Bitmap bitmap = getFromCache(url);
  if(bitmap!=null)
  {
	  imageView.setImageBitmap(bitmap);
  return ;
  }
  bitmap = getFromLocal(url);
  if (bitmap != null) {
		
		imageView.setImageBitmap(bitmap);
		return;
	}
  getFromNet(imageView, url);
	return;
	}
	private  class  ImageRunable implements Runnable
	{
		private String url;
		private ImageView imageView;
      
		public ImageRunable(ImageView imageView, String url) {
			super();
			this.url=url;
			this.imageView=imageView;
		}

		@Override
		public void run() {
			try {
				URL urlobj=new URL(url);
		HttpURLConnection  conn=(HttpURLConnection) urlobj.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
			if(conn.getResponseCode()==200)
			{
				InputStream input=conn.getInputStream();
				byte[] bytes=StreamUtils.readInputStream(input);
				final Bitmap bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				if(bitmap!=null)
				{
					imageView.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
				
					/*
					 *子线程更新ui方法二
					 mHandler=new Handler();
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
					 
					 * 
					 */
					cache.put(url, bitmap);
					writeToLocal(url,bitmap);
				}
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		private void writeToLocal(String url, Bitmap bitmap) throws IOException {
			try {
				String rightFileName = URLEncoder.encode(url, "utf-8");
				
				File sdCardDir=Environment.getExternalStorageDirectory();
				System.out.println(sdCardDir.getAbsolutePath()+"/"+rightFileName);
				File imageFile=new File(sdCardDir.getAbsolutePath()+"/"+rightFileName+".png");
				if(!imageFile.exists())
				{
					imageFile.createNewFile();
					
				}
				FileOutputStream fileOutputStream=new FileOutputStream(imageFile);
				bitmap.compress(CompressFormat.JPEG, 80, fileOutputStream);
			    fileOutputStream.close();
				
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	private void getFromNet(ImageView imageView, String url) {
		ImageRunable r=new ImageRunable(imageView, url);
		threadPool.execute(r);
		
	}
	private Bitmap getFromLocal(String url) {
		// linux http://www.baidu.com/1.jpg
				try {// %ss%dd
					String rightFileName = URLEncoder.encode(url, "utf-8");
					File imgeFile = new File(localDir.getAbsolutePath() + "/" + rightFileName);
				
					Bitmap bitmap = BitmapFactory.decodeFile(imgeFile.getAbsolutePath());
					
					cache.put(url, bitmap);
					return bitmap;
				} catch (Exception e) {
					
				}
				return null;
	}
	private Bitmap getFromCache(String url) {
	
		return cache.get(url);
	}
}

package com.itheima.autoadvertisment.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
public static String readStream(InputStream is)
{
	try {
		byte[] buf=readInputStream(is);
	    
	return new String(buf);
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
	
	return null;
	
}
public static byte[] readInputStream(InputStream is) throws IOException
{
	ByteArrayOutputStream bos=new ByteArrayOutputStream();
	
	byte[] buff=new byte[1024];
	
	int len=-1;
	while((len=is.read(buff))!=-1)
	{
		
		bos.write(buff, 0, len);
	}
	is.close();
	return bos.toByteArray();
}
}

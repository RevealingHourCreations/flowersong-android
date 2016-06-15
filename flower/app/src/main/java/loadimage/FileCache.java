package loadimage;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.content.Context;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context)
	{

		cacheDir=context.getExternalCacheDir();
	}

	public File getFile(String url)
	{
		String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void deleteFile(String url)
	{
		try
		{
			String filename = URLEncoder.encode(url);
			File f = new File(cacheDir, filename);
			f.delete();
		}catch (Exception e) 
		{
			e.printStackTrace();
		}


	}

	public void clear()
	{
		if(cacheDir == null)
			return;
		File[] files=cacheDir.listFiles();
		if(files==null)
			return;
		for(File f:files)
			f.delete();
	}

	public long getSize()
	{
		if(cacheDir==null)
			return 0;
		return cacheDir.length();
	}

	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=2048;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);

			}
		}
		catch(Exception ex){}
	}

}
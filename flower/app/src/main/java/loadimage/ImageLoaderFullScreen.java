package loadimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.flowersong.R;


public class ImageLoaderFullScreen
{

    FileCache fileCache;
    int scale_size=70;
    boolean storeToFileCache = true;
    int imageheigth;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    // final int stub_id=R.drawable.upload_profile_screen;
    final int stub_id=R.drawable.placeholders;

    public ImageLoaderFullScreen(Context context)
    {
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(2);

    }

//	public void DisplayImage(String url, ImageView imageView,int size,boolean store,ProgressBar progressBar)
//
//	{
//		//System.out.println("DISPLAY IMAGE FOR WALL========>");
//		storeToFileCache=store;
//		scale_size = size;
//		imageViews.put(imageView, url);
//		//from SD cache
//		File f= null;
//		Bitmap bitmap=null;
//		if( storeToFileCache )
//		{
//			f=fileCache.getFile(url);
//			bitmap=decodeFile(f,scale_size);
//		}
//		if(bitmap!=null)
//		{
//			imageView.setImageBitmap(bitmap);
//		}
//		else
//		{
//			queuePhoto(url, imageView,progressBar);
//			imageView.setImageResource(stub_id);
//		}
//	}

//	//display image for home
//	public void DisplayImagehome(String url, ImageView imageView,int size,boolean store,boolean delete)
//
//	{
//		//System.out.println("DISPLAY IMAGE FOR WALL========>");
//		if(delete)
//		{
//			fileCache.deleteFile(url);
//		}
//		storeToFileCache=store;
//		scale_size = size;
//		imageViews.put(imageView, url);
//		//from SD cache
//		File f= null;
//		Bitmap bitmap=null;
//		if( storeToFileCache )
//		{
//			f=fileCache.getFile(url);
//			bitmap=decodeFile(f,scale_size);
//		}
//		if(bitmap!=null)
//		{
//			imageView.setImageBitmap(bitmap);
//		}
//		else
//		{
//			queuePhoto(url, imageView);
//			imageView.setImageResource(stub_id);
//		}
//	}
    //shailesh chages for the samsung s3 only for display images lare on wall


    public void DisplayImageofwall(String url, ImageView imageView,int size,boolean store,ProgressBar progressBar)

    {
        storeToFileCache=store;
        scale_size = size;
        //imageView.getLayoutParams().height =341;
        imageViews.put(imageView, url);
        //from SD cache
        File f= null;
        Bitmap bitmap=null;
        if( storeToFileCache )
        {
            //f=fileCache.getFile(url);
            //bitmap=decodeFile(f,scale_size);
            //bitmap=getbitmapoffline(f);
        }
        if(bitmap!=null)
        {
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            imageView.setImageResource(stub_id);
            queuePhotoforwall(url, imageView,progressBar);

        }
    }
    //for wall
    private void queuePhotoforwall(String url, ImageView imageView,ProgressBar progressBar)
    {
        try
        {
            PhotoToLoadForWall p=new PhotoToLoadForWall(url, imageView,progressBar);
            executorService.submit(new PhotosLoaderforwall(p));
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



//	private void queuePhoto(String url, ImageView imageView,ProgressBar progressBar)
//	{
//		try
//		{
//			PhotoToLoad p=new PhotoToLoad(url, imageView);
//			executorService.submit(new PhotosLoader(p));
//		} catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}


    public Bitmap getBitmapforwall(String url, ImageView imageView)
    {
        File f=fileCache.getFile(url);
        Bitmap bitmap=null;
        if(f.exists())
        {
            //System.out.println("if catche file is there2222222222222222");
            bitmap=getbitmapoffline(f);

//            if(bitmap!=null)
//            {
//                int nDeviceWidth = AppController.width;
//                int width_tmp=bitmap.getWidth(), height_tmp=bitmap.getHeight();
//                int Height = (int)Math.floor((height_tmp * 1.0d/width_tmp)*nDeviceWidth);
//                imageView.getLayoutParams().height =Height;
//            }

        }
        else
        {
            //System.out.println("else if catche file is not there2222222222222222");
            HttpURLConnection conn = null;
            InputStream is=null;
            InputStream is1=null;
            InputStream is2=null;
            try {

                URL imageUrl = new URL(url);
                conn = (HttpURLConnection)imageUrl.openConnection();
                // conn.setConnectTimeout(3000);
                //conn.setReadTimeout(3000);
                conn.setInstanceFollowRedirects(true);
                is=conn.getInputStream();
                if(is!=null)
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) > -1 )
                    {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    is1 = new ByteArrayInputStream(baos.toByteArray());
                    is2 = new ByteArrayInputStream(baos.toByteArray());
                }

                if(storeToFileCache)
                {
                    OutputStream os = new FileOutputStream(f);
                    FileCache.CopyStream(is1, os);
                    os.close();
                    //bitmap = decodeFile(f,scale_size);
                    File ff=fileCache.getFile(url);
                    bitmap=getbitmapoffline(ff);

                    //System.out.println("inifififififififififififififififfi");
                }
                else
                {
                    //System.out.println("elseelselelselelselelesellesleleselse");
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is1,null,o);

                    //Find the correct scale value. It should be the power of 2.
                    final int REQUIRED_SIZE=200;
                    int width_tmp=o.outWidth, height_tmp=o.outHeight;
                    //System.out.println("sssssssssssss"+o.outWidth);
                    //System.out.println("sssssssssssss"+o.outHeight);
                    imageheigth=height_tmp;
                    int scale=1;
                    while(true)
                    {
                        if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                            break;
                        width_tmp/=2;
                        height_tmp/=2;
                        scale*=2;
                    }

                    //decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize=scale;
                    o2.inPreferredConfig = Bitmap.Config.RGB_565;//Bitmap.Config ARGB_8888,RGB_565
                    bitmap= BitmapFactory.decodeStream(is2, null, o2);
                }

                //this function work for to set the heigth of imageview as per device
//                if(bitmap!=null)
//                {
//                    int nDeviceWidth = AppController.width;
//                    int width_tmp=bitmap.getWidth(), height_tmp=bitmap.getHeight();
//                    int Height = (int)Math.floor((height_tmp * 1.0d/width_tmp)*nDeviceWidth);
//                    imageView.getLayoutParams().height =Height;
//                }



            } catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
            finally{

                try {
                    if(is!=null)
                    {
                        is.close();
                    }
                    if(is1!=null)
                    {
                        is1.close();
                    }
                    if(is2!=null)
                    {
                        is2.close();
                    }
                    if(conn!=null)
                        conn.disconnect();
                } catch (Exception e)
                {

                }

            }

        }
        return bitmap;

    }



    public Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
        HttpURLConnection conn = null;
        InputStream is=null;
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            conn = (HttpURLConnection)imageUrl.openConnection();
            // conn.setConnectTimeout(3000);
            //conn.setReadTimeout(3000);
            conn.setInstanceFollowRedirects(true);
            is=conn.getInputStream();
            if(storeToFileCache)
            {
                OutputStream os = new FileOutputStream(f);
                FileCache.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f,scale_size);
            }
            else
            {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inScaled = false;
                o2.inSampleSize=(int) 1;
                o2.inPreferredConfig = Bitmap.Config.RGB_565;//Bitmap.Config ARGB_8888,RGB_565
                bitmap= BitmapFactory.decodeStream(is, null, o2);
                //System.out.println("IMAGE SIZE wall thumbnails=====>"+bitmap.getByteCount());
                //bitmap = BitmapFactory.decodeStream(is);
            }

            return bitmap;
        } catch (Exception ex){
            //ex.printStackTrace();
            return null;
        }
        finally{

            try {
                if(is!=null)
                    is.close();
                if(conn!=null)
                    conn.disconnect();
            } catch (Exception e) {

            }

        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f,int size)
    {
        if( f==null )
            return null;
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=scale_size;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            imageheigth=height_tmp;
            int scale=1;
            while(true)
            {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;//Bitmap.Config ARGB_8888,RGB_565
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e)
        {

        }
        return null;
    }

    public Bitmap getbitmapoffline(File f)
    {
        Bitmap bitmap=null;
        try
        {


            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=200;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            //System.out.println("sssssssssssss"+o.outWidth);
            //System.out.println("sssssssssssss"+o.outHeight);
            imageheigth=height_tmp;
            int scale=1;
            while(true)
            {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;//Bitmap.Config ARGB_8888,RGB_565
            return bitmap= BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }


    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public ProgressBar progressBar;
        public PhotoToLoad(String u, ImageView i,ProgressBar p)
        {
            url=u;
            imageView=i;
            progressBar=p;
        }
    }

    //Task for the queue
    private class PhotoToLoadForWall
    {
        public String url;
        public ImageView imageView;
        ProgressBar progressBar;
        public PhotoToLoadForWall(String u, ImageView i,ProgressBar p)
        {
            url=u;
            imageView=i;
            progressBar=p;

        }
    }

    class PhotosLoader implements Runnable
    {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad)
        {
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run()
        {

            if(imageViewReused(photoToLoad))
                return;
            try{
                Bitmap bmp=getBitmap(photoToLoad.url);
                //Bitmap bmp=getBitmap(photoToLoad.url, photoToLoad.imageView);
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                Activity a=(Activity)photoToLoad.imageView.getContext();
                a.runOnUiThread(bd);
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        }
    }
    //photoloder thread for wall
    class PhotosLoaderforwall implements Runnable
    {
        PhotoToLoadForWall photoToLoad;
        PhotosLoaderforwall(PhotoToLoadForWall photoToLoad)
        {
            this.photoToLoad=photoToLoad;
        }

        @SuppressLint("NewApi")
        @Override
        public void run()
        {

            if(imageViewReusedforwall(photoToLoad))
            {

                return;
            }
            try{
                //	            Bitmap bmp=getBitmap(photoToLoad.url);
                Bitmap bmp=getBitmapforwall(photoToLoad.url, photoToLoad.imageView);
                if(bmp==null)
                {
                    //System.out.println("nullnullnullnullnullnullnullnuilnuilnilnin");
                    File f = null;
                    f=fileCache.getFile(photoToLoad.url);
                    bmp=getbitmapoffline(f);
//                    if(bmp!=null)
//                    {
//                        int nDeviceWidth = AppController.width;
//                        int width_tmp=bmp.getWidth(), height_tmp=bmp.getHeight();
//                        int Height = (int)Math.floor((height_tmp * 1.0d/width_tmp)*nDeviceWidth);
//                        photoToLoad.imageView.getLayoutParams().height =Height;
//                    }
                }
                BitmapDisplayerforwall bd=new BitmapDisplayerforwall(bmp, photoToLoad);
                Activity a=(Activity)photoToLoad.imageView.getContext();
                a.runOnUiThread(bd);
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        }
    }


    boolean imageViewReused(PhotoToLoad photoToLoad)
    {
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    boolean imageViewReusedforwall(PhotoToLoadForWall photoToLoad)
    {
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }


    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
            {

                photoToLoad.imageView.setImageBitmap(bitmap);
            }
            else
            {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }


    class BitmapDisplayerforwall implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoadForWall photoToLoad;
        public BitmapDisplayerforwall(Bitmap b, PhotoToLoadForWall p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReusedforwall(photoToLoad))
                return;
            if(bitmap!=null)
            {

                //System.out.println("iiiiiiiiiiiiiiiiifffffffffffffffffffffffffff");
                photoToLoad.imageView.setImageBitmap(bitmap);
                photoToLoad.progressBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                //System.out.println("eeeeeeeeeeeeeeelllllllllllllllllllllllllllllll");
                photoToLoad.imageView.setImageResource(stub_id);
                photoToLoad.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void clear()
    {
        try{
            if(executorService!=null)
                executorService.shutdown();
            if(fileCache.getSize()>104857600 ) //100mb
                fileCache.clear();
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
}

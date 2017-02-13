package codefirst.async_task;

/**
 * Created by cheng on 2017/2/12.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**异步加载图片   */
public class ImageAsync {

    private ListView listView;
    private Set<LoadImageByAsyncTask> taskSet;

    private ImageView icon;
    private String iconUrl;

    private LruCache<String,Bitmap> lruCache;   //缓存算法类
/**构造方法，使用缓存类   +getter/setter 方法 ---以空间换时间 */
    public ImageAsync(ListView listView) {

        this.listView=listView;
        taskSet=new HashSet<>();


        int maxMemory= (int) Runtime.getRuntime().maxMemory();  //获取最大的运行内存
        int cacheSize=maxMemory/4;      //设定缓存的大小
        lruCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {    //每次存入缓存的时候调用
                return value.getByteCount();    //返回当前存入的图片的大小
            }
        };
    }
    //增加到缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromUrl(url)!=null){
            lruCache.put(url,bitmap);
        }
    }
    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){
        return lruCache.get(url);
    }


    private Handler handler=new Handler(){ //用于子线程给主线程传递消息（  先从子线程中接收消息--对象
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(icon.getTag().equals(iconUrl)){  //当URL与图片一致时，才会加载
                icon.setImageBitmap((Bitmap) msg.obj);  //将传来的ImageView 的图片转化为另一张图片
            }
        }
    };

    //线程中显示图片----【使用线程的方式】
    public void showImageByThread(ImageView imageView, final String url){
        icon=imageView;     //接收传来的图片 --要改变
        iconUrl=url;    //URL保存起来，与图片固定的
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=getBitmapFromUrl(url);
                Message message=Message.obtain();
                message.obj=bitmap;     //接收对象
                handler.sendMessage(message);       //发送消息
            }
        }).start();
    }
        //加载可见的item(从起始到终止的）--滑动停止时
    public void loadImage(int start ,int end){
        for (int i = 0; i < end; i++) {
            String imageUrl=MyAdapter.itemUrl[i];       //取得保存的URL---作为tag
            Bitmap bitmap=getBitmapFromUrl(imageUrl);
            if (bitmap==null) {     //若缓存中无，则从网上加载
                LoadImageByAsyncTask loadImageByAsyncTask = new LoadImageByAsyncTask(imageUrl);  //取得传来的对象
                loadImageByAsyncTask.execute(imageUrl);     //加载图片
                taskSet.add(loadImageByAsyncTask);  //在task集合中保存
            }  else{
                ImageView imageView= (ImageView) listView.findViewWithTag(imageUrl);
                imageView.setImageBitmap(bitmap);       //缓存中有则直接使用
            }
        }
    }


//将一个URL对象转化为 一张图片
    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;
        InputStream stream = null;
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            stream=new BufferedInputStream(connection.getInputStream());
            bitmap= BitmapFactory.decodeStream(stream);
            connection.disconnect();    //释放资源
            Thread.sleep(1); //模拟网速不好  延迟时间-1000
            return bitmap;
        }  catch (IOException e) {
            e.printStackTrace();
           } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();     //释放
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    // 显示图片---【使用异步加载的方式】
    public void showImageByAsyncTask(ImageView imageView, final String url){
        Bitmap bitmap=getBitmapFromCache(url);      //先从缓存中看是否有对象

        if (bitmap==null) {
            imageView.setImageResource(R.mipmap.ic_launcher);   //设置默认的
        }  else{
            imageView.setImageBitmap(bitmap);       //缓存中有则直接使用
        }


        /*if (bitmap==null) {     //若缓存中无，则从网上加载
            LoadImageByAsyncTask loadImage = new LoadImageByAsyncTask(url);  //取得传来的对象
            loadImage.execute(url);     //加载图片
        }  else{
                imageView.setImageBitmap(bitmap);       //缓存中有则直接使用
          }*/
    }
    //取消所有正在运行的任务
    public void cancelAllTask() {
         if(taskSet!=null){
             for (LoadImageByAsyncTask task: taskSet) {
                task.cancel(false); //取消任务
             }
         }
    }

    private class LoadImageByAsyncTask extends AsyncTask<String ,Void,Bitmap>{

 //       private ImageView icon;     --改成换ListView中的tag来存储
        private String iconUrl;

        public LoadImageByAsyncTask(String iconUrl) {
  //          icon=image;
            this.iconUrl=iconUrl;       //取得两个对象
        }
/**执行 具体的耗时操作  */
        @Override
        protected Bitmap doInBackground(String... params) {
            String url=params[0];
            Bitmap bitmap= getBitmapFromUrl(url);   //从网络获取图片
            if (bitmap != null) {
                addBitmapToCache(url,bitmap);   //将不在缓存中的图片 加入缓存
            }
            return bitmap;
        }
/**执行 UI的操作  */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ImageView imageView= (ImageView) listView.findViewWithTag(iconUrl);
            if(imageView!=null && bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
            /*if(icon.getTag().equals(iconUrl)) {          //将URL与图片匹配起来
                icon.setImageBitmap(bitmap);    //设置图片
            }*/
        }
    }
}

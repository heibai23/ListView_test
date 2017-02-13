package codefirst.async_task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView async_task_lv;
    private static String URL="http://www.imooc.com/api/teacher?type=4&num=30";
                                                        //要解析的网址json数据
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        async_task_lv= (ListView) findViewById(R.id.lv_async);
        new MyAsyncTask().execute(URL);    //启动任务


    }
    /**自定义的异步加载内部类 --实现网络的异步访问  */
    class MyAsyncTask extends AsyncTask<String,Void,List<MyBean>>{
//具体的耗时工作
        @Override
        protected List<MyBean> doInBackground(String... params) {
            return getJson(params[0]);
        }
//任务的收尾工作
        @Override
        protected void onPostExecute(List<MyBean> myBeen) {
            super.onPostExecute(myBeen);
            adapter=new MyAdapter(MainActivity.this,myBeen,async_task_lv);
            async_task_lv.setAdapter(adapter);
        }
    }
/**获取Json数据的方法--将json数据换成所需的数据类型的数据(MyBean) */
    private List<MyBean> getJson(String url) {
        List<MyBean> list = new ArrayList<>();  //先=null ，后又赋值，可能值赋不进去
        try {
                    //类似打开网络 new URL().openConnection().getInputStream()---所以需要 权限
            String jsonResult = readStream(new URL(url).openStream());
                                 //利用 URL类方法，直接打开流---返回类型为InputStream
            JSONObject jsonObject;
            MyBean myBean;
            try {
                jsonObject = new JSONObject(jsonResult);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    myBean = new MyBean();
                    myBean.newIconUrl = jsonObject.getString("picSmall");
                    myBean.newTitle = jsonObject.getString("name");
                    myBean.newContent = jsonObject.getString("description");
                    list.add(myBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
              }
        }  catch (IOException e){
             e.printStackTrace();
        }
       Log.d("tag", "getJson: " + list.get(2).newTitle);

        return list;
    }
/**通过inputstream 访问网页  */
    private String readStream(InputStream inputStream){
        InputStreamReader reader;   //读入流
        String result="";   //存储
        try {
            String line="";     //按行读取
            reader=new InputStreamReader(inputStream,"utf-8");  //转化格式，将字节流变为字符流
            BufferedReader bufReader=new BufferedReader(reader);    //读取字符流
            while ((line=bufReader.readLine())!=null){      //按行读取
                result+=line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

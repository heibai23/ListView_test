package codefirst.listview_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String iconName_res[]={"通讯录","漂亮","日历","相机","闹钟","游戏",
            "短信","音乐","河流","设置","气球","天气","地图","视频"};   //名称数据源
    private int iconImage_res[]={R.mipmap.address_book,R.mipmap.beau,R.mipmap.calendar,
        R.mipmap.camera,R.mipmap.clock,R.mipmap.games_control,R.mipmap.messenger,
        R.mipmap.ringtone,R.mipmap.riven,R.mipmap.settings,R.mipmap.speech_balloon,
        R.mipmap.weather,R.mipmap.world,R.mipmap.youtube};

    private List<ItemBean> dataList;     //装载数据源
    private MyAdapter adapter;
    private ListView listItem_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initAdapter();
        listItem_lv= (ListView) findViewById(R.id.lv_main);
        listItem_lv.setAdapter(adapter);        //加载适配器
        listItem_lv.setOnItemClickListener(this);

    }

    private void initAdapter() {
        adapter=new MyAdapter(dataList,this);

    }

    //初始化数据源
    private void initData() {
        dataList=new ArrayList<>();
        for(int i=0;i<iconImage_res.length;i++){
            dataList.add(new ItemBean(iconImage_res[i],iconName_res[i]));
                    //将每一个Item添加进List
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if(position==1){
            Intent toast_intent=new Intent(this,ToastAcitvity.class);
            startActivity(toast_intent);
        }
        if(position==2){
            Intent alertDialot_intent=new Intent(this,AlertDialogActivity.class);
            startActivity(alertDialot_intent);
        }
        if(position==3){
            Intent notification_intent=new Intent(this,NotificationActivity.class);
            startActivity(notification_intent);
        }
    }
}

package codefirst.async_task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cheng on 2017/2/12.
 */

public class MyAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<MyBean> beanList;
    private LayoutInflater inflater;
    private ImageAsync imageLoad;       //创建对象，生成【一个】缓存池

    private int startItem;      //滚动时的起始项
    private int endItem;    // 。。最后项
    public static String[] itemUrl;    //存储所有item的图片的URL

    public MyAdapter(Context context, List<MyBean> bean, ListView listView){
        beanList=bean;
        inflater=LayoutInflater.from(context);

        imageLoad=new ImageAsync(listView);
        itemUrl=new String[bean.size()];     //初始化
        for (int i = 0; i < bean.size(); i++) {
            itemUrl[i]=bean.get(i).newIconUrl;      //将图片URL保存
        }
    }
    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_layout_lv,null);
            viewHolder.iconImageId= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.title= (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.description= (TextView) convertView.findViewById(R.id.tv_description);
            convertView.setTag(viewHolder);
        }  else {
                viewHolder= (ViewHolder) convertView.getTag();  //关联起来
        }

        viewHolder.iconImageId.setImageResource(R.mipmap.ic_launcher);
        String url=beanList.get(position).newIconUrl;
        viewHolder.iconImageId.setTag(url);
            //缓冲池中的图片，将其与url固定起来，避免缓存图片出错提高加载
//利用LruCache,就要避免每次都生成一个缓存池
//        ImageAsync imageLoad=new ImageAsync();
 //       imageLoad.showImageByThread(viewHolder.iconImageId,beanList.get(position).newIconUrl);
                        //       使用线程的方式
        imageLoad.showImageByAsyncTask(viewHolder.iconImageId,beanList.get(position).newIconUrl);
                        //  使用异步加载的方式
        viewHolder.title.setText(beanList.get(position).newTitle);      //数据源list中的对象
        viewHolder.description.setText(beanList.get(position).newContent);
        return convertView;
    }
/**滑动监听事件*/
    @Override   //滚动状态
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){     //滚动停止的状态--则要加载可见项
            imageLoad.loadImage(startItem,endItem);

        }  else{    //不加载--其他状态
            imageLoad.cancelAllTask();  //取消
          }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        startItem=firstVisibleItem;
        endItem=startItem+visibleItemCount;     //起始的item+可见的item数

    }

    /**关联控件与布局  */
    class ViewHolder{
        public ImageView iconImageId;
        public TextView title;
        public TextView description;
    }
}

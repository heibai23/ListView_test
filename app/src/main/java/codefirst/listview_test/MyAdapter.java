package codefirst.listview_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by cheng on 2017/1/25.
 */
//自定义的适配器

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> dataList;      //装载数据源
    //private Context context;    //getView（）方法的布局装载器需要用到
    private LayoutInflater layoutInflater;      //布局装载器

    public MyAdapter(List<ItemBean> dataList, Context context) {
        this.layoutInflater=LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }
//对象
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=new ViewHolder(); //装载id的中介
        if(convertView==null){      //将二者绑定
            convertView=layoutInflater.inflate(R.layout.lv_item,null);
            viewHolder.itemImage= (ImageView) convertView.findViewById(R.id.iv_icon);
                        //用来装载控件，这样可以不用每次去findId
            viewHolder.itemName= (TextView) convertView.findViewById(R.id.tv_iconName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ItemBean bean=dataList.get(position);   //通过ViewHolder找到对应控件
        viewHolder.itemImage.setImageResource(bean.itemImageId);
        viewHolder.itemName.setText(bean.itemName);
        return convertView;
        //findID没有再用一个中介去保存
   /*     if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.lv_item,null);
                //缓存机制
            ItemBean bean=dataList.get(position);       //一个项
            ImageView itemImage= (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView itemName= (TextView) convertView.findViewById(R.id.tv_iconName);
            itemImage.setImageResource(bean.itemImageId);
            itemName.setText(bean.itemName);
        }*/
        //没有用到缓存
     /*   View view=layoutInflater.inflate(R.layout.lv_item,null);
     /*                       //将一个布局文件转化为一个view
        ItemBean bean=dataList.get(position);       //一个item项的内容
        ImageView imageView= (ImageView) view.findViewById(R.id.iv_icon);
        TextView textView= (TextView) view.findViewById(R.id.tv_iconName);
        imageView.setImageResource(bean.itemImageId);   //对象调用成员属性
        textView.setText(bean.itemName);

        return view;*/
        //return convertView;

    }
//自定义泛型----内部类
    private class ViewHolder {
        private ImageView itemImage;
        private TextView itemName;      //一个ListView项的名称
    }
}

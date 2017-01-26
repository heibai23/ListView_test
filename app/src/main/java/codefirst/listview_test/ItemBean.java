package codefirst.listview_test;

/**
 * Created by cheng on 2017/1/25.
 */
//一个ListView 的item项所包含的东西
public class ItemBean {

    public int itemImageId;        //一个Item项所包含的的数据
    public String itemName;         //可在其他类中引用

    public ItemBean(int itemImageId, String itemName) {
        this.itemImageId = itemImageId;
        this.itemName = itemName;
    }
}

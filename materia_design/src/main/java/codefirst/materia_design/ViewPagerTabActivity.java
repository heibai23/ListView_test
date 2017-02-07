package codefirst.materia_design;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheng on 2017/2/6.
 */

public class ViewPagerTabActivity extends FragmentActivity {

    private ViewPager tab_system_vp;
    private ViewPagerIndictor tab_design_vp;
    private List<String> title_list= Arrays.asList("短信","日记","心愿");
    private List<ViewpagerFragment> fragments_list=new ArrayList<>();
    private FragmentPagerAdapter fragment_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
    
        initView();
        initData();

        tab_system_vp.setAdapter(fragment_adapter);


    }


    private void initData() {
        for (String title:title_list) {
            ViewpagerFragment fragment=ViewpagerFragment.newInstance(title);
            fragments_list.add(fragment);
        }
        fragment_adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {

                return fragments_list.get(position);
            }

            @Override
            public int getCount() {
                return fragments_list.size();
            }
        };
    }

    private void initView() {
        tab_system_vp= (ViewPager) findViewById(R.id.vp_tab_system);
        tab_design_vp= (ViewPagerIndictor) findViewById(R.id.vp_tab_design);
    }

}

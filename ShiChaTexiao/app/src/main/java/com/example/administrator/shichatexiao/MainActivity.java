package com.example.administrator.shichatexiao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * 视差特效实现思路：
 * 1、解析OnTouche，Action_down,Action_Move,Action_up,业务逻辑复杂
 * 2、重写ListView的puverScrollBy方法，继承式自定义控件，根据用户下拉的距离，动态的修改HeaderView的高度
 *     a、拷贝文本资源到项目中，自定义控件继承ListView
 *     b、使用自定义控件，向头部添加布局，设置适配器
 *     c、使用视图树，把imageView出啊给我们的自定义控件
 */
public class MainActivity extends AppCompatActivity {

    private ParallaxListView plv;
    private static final String[] NAMES = {"宋江", "卢俊义", "吴用",
            "公孙胜", "关胜", "林冲", "秦明", "呼延灼", "花荣", "柴进", "李应", "朱仝", "鲁智深",
            "武松", "董平", "张清", "杨志", "徐宁", "索超", "戴宗", "刘唐", "李逵", "史进", "穆弘",
            "雷横", "李俊", "阮小二", "张横", "阮小五", " 张顺", "阮小七", "杨雄", "石秀", "解珍",
            " 解宝", "燕青", "朱武", "黄信", "孙立", "宣赞", "郝思文", "韩滔", "彭玘", "单廷珪",
            "魏定国", "萧让", "裴宣", "欧鹏", "邓飞", " 燕顺", "杨林", "凌振", "蒋敬", "吕方",
            "郭 盛", "安道全", "皇甫端", "王英", "扈三娘", "鲍旭", "樊瑞", "孔明", "孔亮", "项充",
            "李衮", "金大坚", "马麟", "童威", "童猛", "孟康", "侯健", "陈达", "杨春", "郑天寿",
            "陶宗旺", "宋清", "乐和", "龚旺", "丁得孙", "穆春", "曹正", "宋万", "杜迁", "薛永", "施恩",
            "周通", "李忠", "杜兴", "汤隆", "邹渊", "邹润", "朱富", "朱贵", "蔡福", "蔡庆", "李立",
            "李云", "焦挺", "石勇", "孙新", "顾大嫂", "张青", "孙二娘", " 王定六", "郁保四", "白胜",
            "时迁", "段景柱", "岳狗"};
    private ImageView iv_headview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //A、VIew
        plv = (ParallaxListView) findViewById(R.id.plv);
        //C、ListView添加头布局，ListView上拉加载，下拉刷新
        View headview = View.inflate(this, R.layout.headview, null);
        plv.addHeaderView(headview);
        //
        iv_headview = headview.findViewById(R.id.iv_header);
        //等View界面绘制完毕时，得到已经绘制完控件的宽高，查这个方法并做笔记
        iv_headview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //测量完宽高
                plv.setIv_header(iv_headview);
                //释放资源
                iv_headview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        //B、使用ListView的ArrayAdapter，添加文本的item
        plv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,NAMES));
        }
    }

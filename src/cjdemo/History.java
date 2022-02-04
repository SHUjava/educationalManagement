package cjdemo;

import jdbctest.DBConnector;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

/**
 *
 * @author YangJunhao
 * @function 导出绩点走势柱状图
 * @updateDate: 2022/2/5
 * @updateContent: 1、还未加入到整体框架中，在注释中提供了一个简单的调用样例
 * 2、美观上可能还需要进一步调整
 */
public class History {
    //构建容器面板，用于存放已经画好的图形报表
    private final ChartPanel frame;
    //用于获取数据
    public static CategoryDataset getDataset(int ID, String semester) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        DBConnector test = new DBConnector();
        Object[][] history = test.getEverySemesterGPA(ID, semester);
        for (int i = 0; i < 12; i++) {
            ds.addValue(Double.parseDouble(String.valueOf(history[i][1])), "", history[i][0].toString());
            if (history[i + 1][0] == null) {
                break;
            }
        }
        return ds;
    }

    //在构造方法中将图形报表初始化
    public History(int ID,String semester){
        //获取数据
        CategoryDataset dataset = getDataset(ID, semester);
        //创建图形实体对象
        JFreeChart chart=ChartFactory.createBarChart3D(//工厂模式
                "绩点走势图", //图形的标题
                "学期", //目录轴的显示标签(X轴)
                "绩点",  //数据轴的显示标签(Y轴)
                dataset, //数据集
                PlotOrientation.VERTICAL, //垂直显示图形
                true,  //是否生成图样
                false, //是否生成提示工具
                false);//是否生成URL链接

        //设置总的背景颜色
        chart.setBackgroundPaint(ChartColor.white);
        // 获得图表对象
        CategoryPlot p = chart.getCategoryPlot();
        // 设置图的背景颜色
        p.setBackgroundPaint(ChartColor.WHITE);
        //设置图的边框
        p.setOutlinePaint(ChartColor.white);
        BarRenderer customBarRenderer = (BarRenderer) p.getRenderer();
        //取消柱子上的渐变色
        customBarRenderer.setBarPainter(new StandardBarPainter());
        customBarRenderer.setItemMargin(-0.01);
        //设置柱子的颜色
        customBarRenderer.setBaseOutlinePaint(ChartColor.blue);
        Color c = new Color(75, 177, 196);
        customBarRenderer.setSeriesPaint(0, c);
        //设置柱子宽度
        customBarRenderer.setMaximumBarWidth(0.03);
        customBarRenderer.setMinimumBarLength(0.1);
        //设置柱子间距
        customBarRenderer.setItemMargin(0.06);
        //设置阴影,false代表没有阴影
        customBarRenderer.setShadowVisible(false);
        CategoryPlot plot=chart.getCategoryPlot();//获取图形区域对象
        //------------------------------------------获取X轴
        CategoryAxis domainAxis=plot.getDomainAxis();
        domainAxis.setLabelFont(new Font("黑体",Font.BOLD,14));//设置X轴的标题的字体
        domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));//设置X轴坐标上的字体
        //-----------------------------------------获取Y轴
        ValueAxis rangeAxis=plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));//设置Y轴坐标上的标题字体
        //设置图样的文字样式
        chart.getLegend().setItemFont(new Font("黑体",Font.BOLD ,15));
        //设置图形的标题
        chart.getTitle().setFont(new Font("宋体",Font.BOLD ,20));
        frame =new ChartPanel(chart,true);//将已经画好的图形报表存放到面板中
    }
    //构建一个方法，用于获取存放了图形的面板(封装：隐藏具体实现)
    public ChartPanel getChartPanel(){
        return frame;
    }
}
/* 一个调用样例
public class Test extends JFrame {

    //在构造方法中初始化窗体
    public Test(){
        //----------------------设置窗体大小
        this.setSize(800,600);
        //---------------------------将报表面板添加到窗体中
        this.add(new History(1001002,"2021-2022春季").getChartPanel());
        //----------------------设置窗体大小不可变
        this.setResizable(false);
        //----------------------设置窗体相对于屏幕居中
        this.setLocationRelativeTo(null);
        //------------------------设置窗体可见
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Test();
    }
}*/
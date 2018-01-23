package com.example.shuwnyuan.barchart;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initResources();

        mChart = (BarChart) findViewById(R.id.chart);

        final ArrayList<BarEntry> bar1List = new ArrayList<>();
        final ArrayList<BarEntry> bar2List = new ArrayList<>();
        final ArrayList<BarEntry> bar3List = new ArrayList<>();
        final ArrayList<String> xLabels = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            bar1List.add(new BarEntry(i, (float) randInt()));
            bar2List.add(new BarEntry(i, (float) randInt()));
            bar3List.add(new BarEntry(i, (float) randInt()));

            xLabels.add("entry " + i);
        }


        BarDataSet bar1Set = new BarDataSet(bar1List, "Bar 1");
        bar1Set.setColor(bar1Color);
        bar1Set.setAxisDependency(YAxis.AxisDependency.LEFT);
        bar1Set.setDrawValues(false);

        BarDataSet bar2Set = new BarDataSet(bar2List, "Bar 2");
        bar2Set.setColor(bar2Color);
        bar2Set.setAxisDependency(YAxis.AxisDependency.LEFT);
        bar2Set.setDrawValues(false);

        BarDataSet bar3Set = new BarDataSet(bar3List, "Bar 3");
        bar3Set.setColor(bar3Color);
        bar3Set.setAxisDependency(YAxis.AxisDependency.LEFT);
        bar3Set.setDrawValues(false);

        // (0.20 + 0.05) * 3 + 0.25 = 1.00 -> interval per "group"
        final float groupSpace = 0.25f;
        final float barSpace = 0.05f;
        final float barWidth = 0.2f;

        BarData barData = new BarData(bar1Set, bar2Set, bar3Set);
        barData.setBarWidth(barWidth);

        // make this BarData object grouped
        barData.groupBars(0, groupSpace, barSpace); // start at x = 0


        mChart.setData(barData);

        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(backgroundColor);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);



        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMaximum(200);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);
        xAxis.setAxisMaximum(barData.getXMax() + 1);
        xAxis.setLabelRotationAngle(-40f);

        IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 0 && value <= xLabels.size() - 1) {
                    return xLabels.get((int) value);
                }

                // to avoid IndexOutOfBoundsException on xLabels, if (value < 0 || value > xLabels.size() - 1)
                return "";
            }
        };

        xAxis.setValueFormatter(xAxisFormatter);


        // display bar value during highlight
        YMarkerView mv = new YMarkerView(this);
        mv.setChartView(mChart);    // For bounds control
        mChart.setMarker(mv);       // Set the marker to the chart


        mChart.invalidate();
    }

    private void initResources() {
        Resources.Theme theme = this.getTheme();
        TypedValue typedValue = new TypedValue();

        theme.resolveAttribute(R.attr.color1, typedValue, true);
        bar1Color = typedValue.data;

        theme.resolveAttribute(R.attr.color2, typedValue, true);
        bar2Color = typedValue.data;

        theme.resolveAttribute(R.attr.color3, typedValue, true);
        bar3Color = typedValue.data;

        theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        backgroundColor = typedValue.data;
    }

    // generate random number between 1 to 200
    public static int randInt() {
        int min = 1;
        int max = 200;

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    private BarChart mChart;

    private int bar1Color;
    private int bar2Color;
    private int bar3Color;
    private int backgroundColor;

}


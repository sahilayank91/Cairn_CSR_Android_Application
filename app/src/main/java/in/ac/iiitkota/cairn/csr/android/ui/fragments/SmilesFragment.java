package in.ac.iiitkota.cairn.csr.android.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import in.ac.iiitkota.cairn.csr.android.R;

/**
 * Created by Joey Pinto (Zedacross) on 30/3/17.
 */



public  class SmilesFragment extends Fragment {


    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;
    public boolean created=false;
    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;
    private int dataType = DEFAULT_DATA;


    public ArrayList<Long> head_count;
    public  ArrayList<Long>dates;
    public  String scope="";
    private TextView title;

    public SmilesFragment() {
    }

    public void setData(String scope,ArrayList<Long>smiles,ArrayList<Long>dates){
        this.scope=scope;
        this.head_count =smiles;
        this.dates=dates;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.myactivity_viewpager_fragment_ui, container, false);

        chart = (ColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setZoomEnabled(false);
        created=true;
         title=(TextView)rootView.findViewById(R.id.graph_title);


        resetViewport();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateData();
    }

    public void resetViewport() {
        chart.startDataAnimation();
        generateData();
        title.setText(scope.toUpperCase());


    }
    public void generateData() {
        if(scope.equals("week")) {
            int numColumns = 7;
            List<Column> columns = new ArrayList<>();
            Calendar calendar=Calendar.getInstance();
            for (int i = 0; i < numColumns; ++i) {
                List<SubcolumnValue> values = new ArrayList<>();
                calendar.setTime(new Date(dates.get(i)));
                values.add(new SubcolumnValue(head_count.get(i), getColorSelect(i)));
                Column column = new Column(values);
                column.setHasLabels(head_count.get(i)!=0);

                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }
            data = new ColumnChartData(columns);
            ArrayList<AxisValue> axisValues = new ArrayList<AxisValue>();
            for (int i = 0; i < numColumns; i++) {
                calendar.setTime(new Date(dates.get(i)));
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT, Locale.getDefault()));
                axisValues.add(axisValue);
            }
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Day");
                axisY.setName("Smiles");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);


            chart.setColumnChartData(data);

        }
        if(scope.equals("month")) {
            int numColumns = 5;
            List<Column> columns = new ArrayList<>();
            Calendar calendar=Calendar.getInstance();
            for (int i = 0; i < numColumns; ++i) {
                List<SubcolumnValue> values = new ArrayList<>();
                calendar.setTime(new Date(dates.get(i)));
                values.add(new SubcolumnValue(head_count.get(i), getColorSelect(i)));
                Column column = new Column(values);
                column.setHasLabels(head_count.get(i)!=0);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }
            data = new ColumnChartData(columns);
            ArrayList<AxisValue> axisValues = new ArrayList<AxisValue>();
            for (int i = 0; i < numColumns; i++) {
                calendar.setTime(new Date(dates.get(i)));
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel("Week "+(i+1));
                axisValues.add(axisValue);
            }
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Week");
                axisY.setName("Smiles");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);


            chart.setColumnChartData(data);

        }
        if(scope.equals("year")) {
            int numColumns = 12;
            List<Column> columns = new ArrayList<>();
            Calendar calendar=Calendar.getInstance();
            for (int i = 0; i < numColumns; ++i) {
                List<SubcolumnValue> values = new ArrayList<>();
                calendar.setTime(new Date(dates.get(i)));
                values.add(new SubcolumnValue(head_count.get(i), getColorSelect(i)));
                Column column = new Column(values);
                column.setHasLabels(head_count.get(i)!=0);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }
            data = new ColumnChartData(columns);
            ArrayList<AxisValue> axisValues = new ArrayList<AxisValue>();
            for (int i = 0; i < numColumns; i++) {
                calendar.setTime(new Date(dates.get(i)));
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.getDefault()));
                axisValues.add(axisValue);
            }
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Year");
                axisY.setName("Smiles");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);


            chart.setColumnChartData(data);

        }
        //reset();

    }





    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            // Toast.makeText(getActivity(), String.valueOf(value.getValue()), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
    public static int getColorSelect(int i){
        switch(i%5){
            case 0:return ChartUtils.COLOR_GREEN;
            case 1:return ChartUtils.COLOR_RED;
            case 2:return ChartUtils.COLOR_VIOLET;
            case 3:return ChartUtils.COLOR_BLUE;
            case 4:return ChartUtils.COLOR_ORANGE;

            default: return Color.BLACK;

        }


    }




}

package com.dev.fitface.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.fitface.R;
import com.dev.fitface.models.Attendance;
import com.dev.fitface.models.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomListReportAdapter extends BaseAdapter implements View.OnClickListener{
    private ArrayList<Report> reports;
    private Context context;

    public CustomListReportAdapter(ArrayList<Report> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.attendance_detail_item_listview,null);

        Report report=reports.get(i);
        TextView session=(TextView)view.findViewById(R.id.session);
        TextView room=(TextView)view.findViewById(R.id.room);
        TextView sessDate=(TextView)view.findViewById(R.id.date);
        TextView checkin=(TextView)view.findViewById(R.id.checkin);
        TextView status=(TextView)view.findViewById(R.id.textViewStatus);
//        Date date = new Date(report.getSessdate()*1000);
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String formattedDate = sdf.format(date);
        session.setText("Session: "+ report.getSessionid());
        room.setText(report.getRoom()+" "+ report.getCampus());
        sessDate.setText("12/5/2021");
        status.setText(String.valueOf(report.getStatusid()));
        session.setText(String.valueOf(report.getSessionid()));
        room.setText(report.getRoom()+ report.getCampus());
        sessDate.setText("1/4/2021");
        status.setText("Vắng");
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}

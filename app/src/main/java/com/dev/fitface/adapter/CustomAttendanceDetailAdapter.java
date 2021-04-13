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

public class CustomAttendanceDetailAdapter extends BaseAdapter implements View.OnClickListener{
    private ArrayList<Report> reports;
    private Context context;

    public CustomAttendanceDetailAdapter(ArrayList<Report> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.attendance_detail_item_listview,null);

        Report report=reports.get(i);
        TextView session=(TextView)view.findViewById(R.id.session);
        TextView room=(TextView)view.findViewById(R.id.room);
        TextView sessDate=(TextView)view.findViewById(R.id.date);
        TextView checkin=(TextView)view.findViewById(R.id.checkin);
        TextView status=(TextView)view.findViewById(R.id.textViewStatus);
        Date date = new Date(report.getSessdate()*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        session.setText(report.getSessionid());
        room.setText(report.getRoom()+" "+ report.getCampus());
        sessDate.setText(formattedDate);
        status.setText(report.getStatusid());
        return view;
    }
}

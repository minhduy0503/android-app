package com.dev.fitface.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.fitface.R;
import com.dev.fitface.models.Attendance;


import java.util.ArrayList;

public class CustomListAttendanceAdapter extends BaseAdapter implements View.OnClickListener{
    private ArrayList<Attendance> attendances;
    private Context context;

    public CustomListAttendanceAdapter(ArrayList<Attendance> userInfos, Context context) {
        this.attendances = userInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return attendances.size();
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
        view=layoutInflater.inflate(R.layout.attendance_item_listview,null);

        Attendance attendance=attendances.get(i);
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView email=(TextView)view.findViewById(R.id.email);
        TextView b=(TextView)view.findViewById(R.id.textViewb);
        TextView c=(TextView)view.findViewById(R.id.textViewc);
        TextView v=(TextView)view.findViewById(R.id.textViewv);
        TextView t=(TextView)view.findViewById(R.id.textViewt);
        name.setText(attendance.getName());
        email.setText(attendance.getEmail());
        b.setText("B: "+attendance.getB());
        c.setText("C: "+attendance.getC());
        v.setText("V: "+attendance.getV());
        t.setText("T: "+attendance.getT());
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    // getting the popup menu
//    private void showPopupMenu(View view){
//        PopupMenu popupMenu=new PopupMenu(context,view);
//        popupMenu.getMenuInflater().inflate(R.menu.option_menu,popupMenu.getMenu());
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.edit:
//                        Toast.makeText(context, "Edit !", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.remove:
//                        Toast.makeText(context, "Remove !", Toast.LENGTH_SHORT).show();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//    }
//
//    //file search result
//    public void filterResult(ArrayList<UserInfo> newUserInfos){
//        userInfos=new ArrayList<>();
//        userInfos.addAll(newUserInfos);
//        notifyDataSetChanged();
//    }
}

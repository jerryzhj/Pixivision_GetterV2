package com.zhj.pixivision_getter.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.pixivision_getter.R;
import com.zhj.pixivision_getter.entity.Pixivision;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class ListAdapter extends BaseAdapter {
    private List<Pixivision> list ;
    private Context context;

    public ListAdapter(Context c,List<Pixivision> list) {
        this.context = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_items,null);
        TextView textView_name = (TextView) view.findViewById(R.id.TextView_name);
        TextView textView_date = (TextView) view.findViewById(R.id.TextView_date);
        textView_name.setText(list.get(i).getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String dateString =sdf.format(list.get(i).getDate());
        textView_date.setText(dateString);
        final int index = i;
        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Toast.makeText(context,index+"aa"+list.get(index).toString(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}

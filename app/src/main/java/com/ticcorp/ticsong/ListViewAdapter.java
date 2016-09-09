package com.ticcorp.ticsong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticcorp.ticsong.activitySupport.ListData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ListViewAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ListData> mListData = new ArrayList<ListData>();
    Bitmap mPicBitmap = null;

    public ListViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);

            holder.mRank = (TextView) convertView.findViewById(R.id.txt_rank);
            holder.mPic = (ImageView) convertView.findViewById(R.id.img_profile);
            holder.mName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.mScore = (TextView) convertView.findViewById(R.id.txt_score);
            holder.mLevel = (TextView) convertView.findViewById(R.id.txt_level);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ListData mData = mListData.get(position);

        // 이미지 URL에서 불러오는 부분
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

                    URL url = new URL(mData.mPicURL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    mPicBitmap = BitmapFactory.decodeStream(is);
                    // 이미지 불러오는 부분 끝
                    Log.i("ticlog", "Img load success / " + mData.mPicURL);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("ticlog", "Img load fail");
                }
            }
        };
        mThread.start();
        try {
            mThread.join();

            holder.mRank.setText(mData.mRank);
            if (mPicBitmap != null) {
                holder.mPic.setImageBitmap(mPicBitmap);
            } else {
                holder.mPic.setImageResource(R.drawable.profile_main_image);
            }
            holder.mName.setText(mData.mName);
            holder.mScore.setText(mData.mScore);
            holder.mLevel.setText(mData.mLevel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView mRank;
        public ImageView mPic;
        public TextView mName;
        public TextView mScore;
        public TextView mLevel;
    }

    public void addItem(String mRank, String mPicURL, String mName, String mScore, String mLevel) {
        ListData addInfo = null;
        addInfo = new ListData();
        addInfo.mRank = mRank;
        addInfo.mPicURL = mPicURL;
        addInfo.mName = mName;
        addInfo.mScore = mScore;
        addInfo.mLevel = mLevel;

        mListData.add(addInfo);
    }

    public void remove(int position) {
        mListData.remove(position);
        dataChange();
    }

    public void sort() {
        Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
        dataChange();
    }

    public void dataChange() {
        notifyDataSetChanged();
    }
}

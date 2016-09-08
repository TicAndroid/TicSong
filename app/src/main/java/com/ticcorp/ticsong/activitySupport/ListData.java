package com.ticcorp.ticsong.activitySupport;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by yPeA on 2016. 9. 3..
 */
public class ListData {
    public String mRank;
    public String mPicURL;
    public String mName;
    public String mScore;
    public String mLevel;

    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(ListData listData_1, ListData listData_2) {
            return sCollator.compare(listData_1.mScore, listData_2.mScore);
        }
    };
}

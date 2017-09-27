package me.blog.korn123.easydiary.diary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import me.blog.korn123.commons.utils.CommonUtils;
import me.blog.korn123.commons.utils.DateUtils;
import me.blog.korn123.commons.utils.EasyDiaryUtils;
import me.blog.korn123.commons.utils.FontUtils;
import me.blog.korn123.easydiary.R;

/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

public class DiaryCardArrayAdapter extends ArrayAdapter<DiaryDto> {

    private final Context mContext;
    private final List<DiaryDto> mList;
    private final int mLayoutResourceId;
    private String mQuery;

    public void setCurrentQuery(String query) {
        this.mQuery = query;
    }

    public String getCurrentQuery() {
        return this.mQuery;
    }

    public DiaryCardArrayAdapter(Context context, int layoutResourceId, List<DiaryDto> list) {
        super(context, layoutResourceId, list);
        this.mContext = context;
        this.mList = list;
        this.mLayoutResourceId = layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity)this.mContext).getLayoutInflater();
            row = inflater.inflate(this.mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textView1 = ((TextView)row.findViewById(R.id.text1));
            holder.textView2 = ((TextView)row.findViewById(R.id.text2));
            holder.textView3 = ((TextView)row.findViewById(R.id.text3));
            holder.imageView = ((ImageView) row.findViewById(R.id.weather));
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        DiaryDto diaryDto = mList.get(position);
        float fontSize = diaryDto.getFontSize() > 0 ? diaryDto.getFontSize() : CommonUtils.loadFloatPreference(mContext, "font_size", (int)holder.textView1.getTextSize());
        if (StringUtils.isEmpty(diaryDto.getTitle())) {
            holder.textView1.setVisibility(View.GONE);
        } else {
            holder.textView1.setVisibility(View.VISIBLE);
        }
        holder.textView1.setText(diaryDto.getTitle());
        holder.textView2.setText(diaryDto.getContents());
        holder.textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        holder.textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);

        // highlight current mQuery
        if (StringUtils.isNotEmpty(mQuery)) {
            EasyDiaryUtils.highlightString(holder.textView1, mQuery);
            EasyDiaryUtils.highlightString(holder.textView2, mQuery);
        }
        holder.textView3.setText(DateUtils.getFullPatternDateWithTime(diaryDto.getCurrentTimeMillis()));
        holder.textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        EasyDiaryUtils.initWeatherView(holder.imageView, diaryDto.getWeather());
        initFontStyle(holder, diaryDto.getFontName());

        return row;
    }

    private void initFontStyle(ViewHolder holder, String fontName) {
        if (StringUtils.isNotEmpty(fontName)) {
            Typeface typeface = FontUtils.createTypeface(mContext, mContext.getAssets(), fontName);
            holder.textView1.setTypeface(typeface);
            holder.textView2.setTypeface(typeface);
            holder.textView3.setTypeface(typeface);
        } else {
            FontUtils.setTypeface(mContext, mContext.getAssets(), holder.textView1);
            FontUtils.setTypeface(mContext, mContext.getAssets(), holder.textView2);
            FontUtils.setTypeface(mContext, mContext.getAssets(), holder.textView3);
        }
    }

    private static class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageView;
    }
}

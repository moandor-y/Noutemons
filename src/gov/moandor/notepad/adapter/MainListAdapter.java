package gov.moandor.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gov.moandor.notepad.R;
import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.util.GlobalContext;
import gov.moandor.notepad.widget.TimeTextView;

import java.util.ArrayList;
import java.util.List;

public class MainListAdapter extends BaseAdapter {
    private List<Article> mArticles = new ArrayList<Article>();
    private LayoutInflater mInflater = GlobalContext.getInstance().getActivity().getLayoutInflater();
    private List<Integer> mCheckedItemPositions = new ArrayList<Integer>();
    
    @Override
    public int getCount() {
        return mArticles.size();
    }
    
    @Override
    public Article getItem(int position) {
        return mArticles.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return mArticles.get(position).id;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_list_item, parent, false);
            holder = initViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article article = mArticles.get(position);
        if (!article.title.equals(holder.title.getTag())) {
            holder.title.setText(article.title);
            holder.title.setTag(article.title);
        }
        if (!article.text.equals(holder.preview.getTag())) {
            holder.preview.setText(article.text);
            holder.preview.setTag(article.text);
        }
        holder.time.setTime(article.lastModified);
        if (mCheckedItemPositions.contains(position)) {
            convertView.setBackgroundResource(R.color.ics_blue_semi);
        } else {
            convertView.setBackgroundResource(0);
        }
        return convertView;
    }
    
    public void update(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
    }
    
    public void addOrUpdate(Article article) {
        int i;
        int count = mArticles.size();
        for (i = 0; i < count; i++) {
            if (mArticles.get(i).id == article.id) {
                mArticles.remove(i);
                break;
            }
        }
        mArticles.add(i, article);
    }
    
    private ViewHolder initViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.preview = (TextView) view.findViewById(R.id.preview);
        holder.time = (TimeTextView) view.findViewById(R.id.time);
        return holder;
    }
    
    public void onItemCheckedStateChanged(int position, boolean checked) {
        if (checked) {
            mCheckedItemPositions.add(position);
        } else {
            mCheckedItemPositions.remove(Integer.valueOf(position));
        }
    }
    
    public Integer[] getCheckedItemPositions() {
        int size = mCheckedItemPositions.size();
        return mCheckedItemPositions.toArray(new Integer[size]);
    }
    
    public void clearCheckedPositions() {
        mCheckedItemPositions.clear();
    }
    
    public Article[] getCheckedItems() {
        int size = mCheckedItemPositions.size();
        Article[] result = new Article[size];
        for (int i = 0; i < size; i++) {
            result[i] = getItem(mCheckedItemPositions.get(i));
        }
        return result;
    }
    
    private class ViewHolder {
        public TextView title;
        public TextView preview;
        public TimeTextView time;
    }
}

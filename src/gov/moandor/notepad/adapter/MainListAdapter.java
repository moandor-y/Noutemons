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
    private static final int PREVIEW_LENGTH = 20;
    
    private List<Article> mArticles = new ArrayList<Article>();
    private LayoutInflater mInflater = GlobalContext.getInstance().getActivity().getLayoutInflater();
    private List<Integer> mCheckedItemPositions = new ArrayList<Integer>();
    
    @Override
    public int getCount() {
        return mArticles.size();
    }
    
    @Override
    public Object getItem(int position) {
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
        if (article.preview == null) {
            buildPreview(article);
        }
        if (!article.title.equals(holder.title.getTag())) {
            holder.title.setText(article.title);
            holder.title.setTag(article.title);
        }
        if (!article.preview.equals(holder.preview.getTag())) {
            holder.preview.setText(article.preview);
            holder.preview.setTag(article.preview);
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
        article.preview = null;
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
    
    private void buildPreview(Article article) {
        if (article.text.length() > PREVIEW_LENGTH) {
            article.preview = article.text.substring(0, PREVIEW_LENGTH).replace("\n", " ") + "...";
        } else {
            article.preview = article.text.replace("\n", " ");
        }
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
    
    public List<Integer> getCheckedItemPositions() {
        return mCheckedItemPositions;
    }
    
    private class ViewHolder {
        public TextView title;
        public TextView preview;
        public TimeTextView time;
    }
}
package gov.moandor.notepad.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import gov.moandor.notepad.R;
import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.util.GlobalContext;

public class TextViewerActivity extends AbsActivity {
    private static final String ARTICLE;
    
    static {
        String packageName = GlobalContext.getInstance().getPackageName();
        ARTICLE = packageName + ".ARTICLE";
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_viewer);
        TextView text = (TextView) findViewById(R.id.text);
        Article article = getIntent().getParcelableExtra(ARTICLE);
        text.setText(article.text);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(article.title);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public static void start(Article article, Context context) {
        Intent intent = new Intent();
        intent.setClass(GlobalContext.getInstance(), TextViewerActivity.class);
        intent.putExtra(ARTICLE, article);
        context.startActivity(intent);
    }
}

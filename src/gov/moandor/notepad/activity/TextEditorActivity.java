package gov.moandor.notepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import gov.moandor.notepad.R;
import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.fragment.EditTitleDialogFragment;

public class TextEditorActivity extends AbsActivity {
    public static final String ARTICLE = "article";
    private static final String EDIT_TEXT_DIALOG_FRAGMENT_TAG = "edit_title_dialog_fragment";
    
    private EditText mContent;
    private Article mArticle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        mContent = (EditText) findViewById(R.id.content);
        mArticle = getIntent().getParcelableExtra(ARTICLE);
        if (mArticle != null) {
            mContent.setText(mArticle.text);
            getActionBar().setTitle(mArticle.title);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_text_editor, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.save:
            save();
            return true;
        case R.id.edit_title:
            EditTitleDialogFragment editTitleDialog = new EditTitleDialogFragment();
            editTitleDialog.setOnEditFinishedListener(mTitleEditFinishedListener);
            if (mArticle != null) {
                Bundle args = new Bundle();
                args.putString(EditTitleDialogFragment.OLD_TITLE, mArticle.title);
                editTitleDialog.setArguments(args);
            }
            editTitleDialog.show(getFragmentManager(), EDIT_TEXT_DIALOG_FRAGMENT_TAG);
            return true;
        }
        return false;
    }
    
    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }
    
    private void save() {
        String content = mContent.getText().toString();
        if (mArticle == null && !TextUtils.isEmpty(content)) {
            mArticle = new Article();
            mArticle.text = content;
            if (content.length() > 10) {
                mArticle.title = content.substring(0, 10);
            } else {
                mArticle.title = content;
            }
            setResult();
        } else if (mArticle != null && !TextUtils.isEmpty(content) && !content.equals(mArticle.text)) {
            mArticle.text = content;
            setResult();
        }
    }
    
    private void setResult() {
        Intent data = new Intent();
        data.putExtra(ARTICLE, mArticle);
        setResult(RESULT_OK, data);
    }
    
    private EditTitleDialogFragment.OnEditFinishedListener mTitleEditFinishedListener =
            new EditTitleDialogFragment.OnEditFinishedListener() {
                @Override
                public void onEditFinished(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        getActionBar().setTitle(result);
                        if (mArticle == null) {
                            mArticle = new Article();
                        }
                        mArticle.title = result;
                        setResult();
                    }
                }
            };
}

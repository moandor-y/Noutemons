package gov.moandor.notepad.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import gov.moandor.notepad.R;
import gov.moandor.notepad.adapter.MainListAdapter;
import gov.moandor.notepad.bean.Article;
import gov.moandor.notepad.database.DatabaseUtils;
import gov.moandor.notepad.fragment.ConfirmingDialogFragment;
import gov.moandor.notepad.fragment.EditTitleDialogFragment;

import java.util.List;

public class MainActivity extends AbsActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_TEXT_EDIT = 0;
    private static final String DELETION_DIALOG_FRAGMENT = "deletion_dialog";
    private static final String EDIT_TEXT_DIALOG_FRAGMENT = "edit_title_dialog";
    
    private MainListAdapter mAdapter;
    private ListView mListView;
    private ActionMode mActionMode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new MainListAdapter(getLayoutInflater());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setMultiChoiceModeListener(new ListMultiChoiceModeListener());
        refresh();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        final Article article = data.getParcelableExtra(TextEditorActivity.ARTICLE);
        article.lastModified = System.currentTimeMillis();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatabaseUtils.insertOrUpdateArticle(article);
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                refresh();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    
    private void refresh() {
        new AsyncTask<Void, Void, List<Article>>() {
            @Override
            protected List<Article> doInBackground(Void... params) {
                return DatabaseUtils.getAllArticles();
            }
            
            @Override
            protected void onPostExecute(List<Article> result) {
                mAdapter.update(result);
                mAdapter.notifyDataSetChanged();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add:
            Intent intent = new Intent();
            intent.setClass(this, TextEditorActivity.class);
            startActivityForResult(intent, REQUEST_TEXT_EDIT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, TextEditorActivity.class);
        intent.putExtra(TextEditorActivity.ARTICLE, mAdapter.getItem(position));
        startActivityForResult(intent, REQUEST_TEXT_EDIT);
    }
    
    private void startDeletionTask(final Article article) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatabaseUtils.deleteArticle(article);
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                refresh();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    
    private void startTitleEditTask(final Integer[] positions, final String title) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int count = positions.length;
                long[] ids = new long[count];
                for (int i = 0; i < count; i++) {
                    ids[i] = mAdapter.getItem(positions[i]).id;
                }
                DatabaseUtils.editArticleTitles(ids, title);
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                refresh();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    
    private class ListMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.main_list_long_click, menu);
            mActionMode = mode;
            return true;
        }
        
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mListView.getCheckedItemCount() <= 1) {
                menu.findItem(R.id.view_mode).setVisible(true);
            } else {
                menu.findItem(R.id.view_mode).setVisible(false);
            }
            return true;
        }
        
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.delete:
                delete();
                break;
            case R.id.edit_title:
                editTitle();
                break;
            case R.id.view_mode:
                TextViewerActivity.start(mAdapter.getCheckedItems()[0], MainActivity.this);
                break;
            }
            return false;
        }
        
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearCheckedPositions();
            mAdapter.notifyDataSetChanged();
            mActionMode = null;
        }
        
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            mAdapter.onItemCheckedStateChanged(position, checked);
            mAdapter.notifyDataSetChanged();
            mode.invalidate();
        }
        
        private void delete() {
            ConfirmingDialogFragment confirmingDialog = new ConfirmingDialogFragment();
            Bundle args = new Bundle();
            args.putString(ConfirmingDialogFragment.MESSAGE, getString(R.string.confirming_deletion));
            confirmingDialog.setArguments(args);
            confirmingDialog.setOnConfirmListener(new OnConfirmLinstener());
            confirmingDialog.show(getFragmentManager(), DELETION_DIALOG_FRAGMENT);
        }
        
        private void editTitle() {
            EditTitleDialogFragment editTitleDialog = new EditTitleDialogFragment();
            editTitleDialog.setOnEditFinishedListener(new EditTitleFinishedListener());
            Integer[] checkedPositions = mAdapter.getCheckedItemPositions();
            if (checkedPositions.length > 1) {
                String prevTitle = mAdapter.getItem(0).title;
                boolean displayOldTitle = true;
                for (int position : checkedPositions) {
                    String title = mAdapter.getItem(position).title;
                    if (!title.equals(prevTitle)) {
                        displayOldTitle = false;
                        break;
                    }
                    prevTitle = title;
                }
                if (displayOldTitle) {
                    Bundle args = new Bundle();
                    args.putString(EditTitleDialogFragment.OLD_TITLE, prevTitle);
                    editTitleDialog.setArguments(args);
                }
            } else {
                String title = mAdapter.getItem(checkedPositions[0]).title;
                Bundle args = new Bundle();
                args.putString(EditTitleDialogFragment.OLD_TITLE, title);
                editTitleDialog.setArguments(args);
            }
            editTitleDialog.show(getFragmentManager(), EDIT_TEXT_DIALOG_FRAGMENT);
        }
        
        private class EditTitleFinishedListener implements EditTitleDialogFragment.OnEditFinishedListener {
            @Override
            public void onEditFinished(String result) {
                startTitleEditTask(mAdapter.getCheckedItemPositions(), result);
            }
        }
        
        private class OnConfirmLinstener implements ConfirmingDialogFragment.OnConfirmListener {
            @Override
            public void onConfirm() {
                for (int position : mAdapter.getCheckedItemPositions()) {
                    startDeletionTask(mAdapter.getItem(position));
                }
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
    }
}

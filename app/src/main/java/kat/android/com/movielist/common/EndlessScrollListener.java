package kat.android.com.movielist.common;

import android.widget.AbsListView;
import android.widget.ListView;

//when user scrolls to the end of list , automatically load next page
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    public static final int DEFAULT_VISIBLE_THRESHOLD = 2;

    private int visibleThreshold;
    private int currentPage;
    private int previousTotal;
    private boolean loading;

    public EndlessScrollListener() {
        this(DEFAULT_VISIBLE_THRESHOLD);
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        currentPage = 1;
        previousTotal = 0;
        loading = true;
    }

    //strange load logic ))
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        ListView listView = (ListView) view;
        int headerViewsCount = listView.getHeaderViewsCount();
        int footerViewsCount = listView.getFooterViewsCount();
        int liquidTotalItemCount = totalItemCount - headerViewsCount - footerViewsCount;
        if (loading) {
            if (liquidTotalItemCount > previousTotal) {
                loading = false;
                previousTotal = liquidTotalItemCount;
                currentPage++;
            }
        }
        if (!loading && hasMoreDataToLoad() && (firstVisibleItem + visibleItemCount + visibleThreshold) >= (totalItemCount - footerViewsCount)) {
            loading = true;
            loadMoreData(currentPage);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    protected abstract boolean hasMoreDataToLoad();

    protected abstract void loadMoreData(int page);

    public boolean isLoading() {
        return loading;
    }

}

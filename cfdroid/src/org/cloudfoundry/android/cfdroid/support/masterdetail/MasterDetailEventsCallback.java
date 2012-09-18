package org.cloudfoundry.android.cfdroid.support.masterdetail;

import java.util.List;

/**
 * Interface used as a way of communicating from the master fragment back to the activity.
 * 
 * @see http://developer.android.com/training/basics/fragments/communicating.html
 * 
 * @author ebottard
 *
 * @param <I> the kind of domain object we're dealing with
 */
public interface MasterDetailEventsCallback<I>  {
	
	public static final String KEY_SELECTION = "selection";
	
	public static final String RIGHT_PANE_LAYOUT_ID = "layout";

	void onLeftPaneSelection(int position);
	
	void onNewData(List<I> data);

}

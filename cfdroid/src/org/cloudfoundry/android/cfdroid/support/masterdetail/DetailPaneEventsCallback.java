package org.cloudfoundry.android.cfdroid.support.masterdetail;

/**
 * Interface used to push selection change down to the detail pane.  
 * 
 * @author ebottard
 *
 */
public interface DetailPaneEventsCallback {
	
	public static final String KEY_SELECTION = "selection";
	
	void selectionChanged();

}

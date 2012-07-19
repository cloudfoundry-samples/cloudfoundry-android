package org.cloudfoundry.android.cfdroid.targets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cloudfoundry.android.cfdroid.Preferences_;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.R.id;
import org.cloudfoundry.android.cfdroid.R.menu;

import android.app.Activity; 
import android.os.Bundle;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.view.*;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
 
@EActivity(android.R.layout.list_content)
@OptionsMenu(R.menu.add)
public class TargetsActivity extends SherlockActivity {

	@ViewById(android.R.id.list)
	ListView listView;

	private List<CloudTarget> targets = new ArrayList<CloudTarget>();
	
	@InstanceState
	HashSet<Integer> selection = new HashSet<Integer>();

	@Bean
	TargetSelectionAdapter adapter;

	@Pref
	Preferences_ preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("cf", "wtf");
		targets = TargetUtils.fromPrefs(preferences);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		TargetUtils.toPrefs(targets, preferences);
	}
	
	@AfterViews
	void setup() {
		adapter.init(targets, selection);
		listView.setAdapter(adapter);
	}

	@ItemClick(android.R.id.list)
	public void click() {

	}
	
	@OptionsItem(R.id.add)
	public void add() {
		
	}
	
	

}

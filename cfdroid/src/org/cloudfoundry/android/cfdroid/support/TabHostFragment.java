package org.cloudfoundry.android.cfdroid.support;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.applications.ApplicationControlFragment;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * A fragment that can display a {@link TabHost} whose contents are themselves
 * fragments.
 * 
 * @author ebottard
 * 
 */
//TODO http://stackoverflow.com/questions/4149953/androidorientation-vertical-does-not-work-for-tabwidget
public abstract class TabHostFragment extends RoboSherlockFragment implements TabHost.OnTabChangeListener{

	private static final String TAB = "tab";

	@InjectView(android.R.id.tabhost)
	protected TabHost tabHost;
	
	private TabContentFactory dummy = new TabContentFactory() {
		@Override
		public View createTabContent(String tag) {
			View view = new View(getActivity());
			view.setMinimumWidth(0);
			view.setMinimumHeight(0);
			return view;
		}
	};
	
	private static class TabInfo<F extends Fragment> {
		public TabInfo(Class<F> klass) {
			this.klass = klass;
		}
		private Class<? > klass;
		private F fragment;
	}
	
	private Map<String, TabInfo<?>> infos = new HashMap<String, TabHostFragment.TabInfo<?>>();
	
	private TabInfo<?> currentTabInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tabs, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tabHost.setup();
		tabHost.setOnTabChangedListener(this);
		setupTabs();
		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
		}
	}
	
	protected abstract void setupTabs();

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(TAB, tabHost.getCurrentTabTag());
	}
	
	public <F extends Fragment> void addTab(String tag, String label, Class<F> klass) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setContent(dummy);
		tabSpec.setIndicator(label);
		TabInfo<F> info = new TabInfo<F>(klass);
		infos.put(tag, info);
		tabHost.addTab(tabSpec);
	}
	
	public void clearAll() {
		if (currentTabInfo != null && currentTabInfo.fragment != null) {
			FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
			tx.detach(currentTabInfo.fragment);
		}
		tabHost.clearAllTabs();
		currentTabInfo = null;
		infos.clear();
		FrameLayout realtabcontent = new FrameLayout(getActivity());
		realtabcontent.setId(R.id.realtabcontent);
		tabHost.getTabContentView().addView(realtabcontent);
	}

	@Override
	public void onTabChanged(String tabId) {
		TabInfo info = infos.get(tabId);
		if (info != currentTabInfo) {
			FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
			if (currentTabInfo != null && currentTabInfo.fragment != null) {
				tx.detach(currentTabInfo.fragment);
			}

			if (info.fragment == null) {
				info.fragment = Fragment.instantiate(getActivity(), info.klass.getName());
				tx.add(R.id.realtabcontent, info.fragment, tabId);
			} else {
				tx.attach(info.fragment);
			}

			currentTabInfo = info;
			tx.commit();
			//getActivity().getSupportFragmentManager().executePendingTransactions();
		}
	}

}

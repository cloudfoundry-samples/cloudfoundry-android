package org.cloudfoundry.android.cfdroid.targets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.R.id;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
 
@ContentView(R.layout.target_list)
public class TargetsActivity extends RoboSherlockFragmentActivity {

	private static final String SELECTION_KEY = "selection";

	@InjectView(R.id.list)
	private ListView listView;
	
	@InjectView(R.id.done)
	private Button done;

	private List<CloudTarget> targets = new ArrayList<CloudTarget>();
	
	private HashSet<Integer> selection = new HashSet<Integer>();

	private TargetSelectionAdapter adapter;

	@Inject
	private TargetsPreferences targetsPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		targets = targetsPreferences.fromPrefs();
		if (savedInstanceState!=null) {
			selection = (HashSet<Integer>) savedInstanceState.getSerializable(SELECTION_KEY);
		}
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				click((CloudTarget) parent.getItemAtPosition(position));
			}
		});
		
		adapter = new TargetSelectionAdapter(getLayoutInflater(), targets, this);
		adapter.init(targets, selection);
		listView.setAdapter(adapter);
		
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TargetsActivity.this.finish();
			}
		});

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SELECTION_KEY, selection);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        switch (item.getItemId()) {
            case id.add:
                add();
                return true;
            default:
                return false;
        }
    }
	
    @Override
    protected void onPause() {
    	targetsPreferences.toPrefs(targets);
    	super.onPause();
    }
    
	public void click(final CloudTarget target) {
		FragmentManager fm = getSupportFragmentManager();
		TargetEditDialogFragment dialog = new TargetEditDialogFragment(target) {
			@Override
			public void onResult(CloudTarget result) {
				// Nothing here as target will
				// have been edited in place
			}
		};
		dialog.show(fm, "fragment_edit_target");

	}
	
	public void add() {
		FragmentManager fm = getSupportFragmentManager();
		TargetEditDialogFragment dialog = new TargetEditDialogFragment(new CloudTarget("", "")) {
			@Override
			public void onResult(CloudTarget result) {
				targets.add(result);
			}
		};
		dialog.show(fm, "fragment_edit_target");
	}
	
	

}

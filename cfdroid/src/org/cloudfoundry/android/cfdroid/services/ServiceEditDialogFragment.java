package org.cloudfoundry.android.cfdroid.services;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.cloudfoundry.android.cfdroid.CloudFoundry;
import org.cloudfoundry.android.cfdroid.R;
import org.cloudfoundry.android.cfdroid.support.AsyncLoader;
import org.cloudfoundry.android.cfdroid.support.BaseTextWatcher;
import org.cloudfoundry.android.cfdroid.support.ItemListAdapter;
import org.cloudfoundry.android.cfdroid.support.TaskWithDialog;
import org.cloudfoundry.client.lib.CloudService;
import org.cloudfoundry.client.lib.ServiceConfiguration;

import roboguice.fragment.RoboDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * A fragment for editing a (to be added) {@link CloudService}.
 * 
 * @author Eric Bottard
 */
public class ServiceEditDialogFragment extends RoboDialogFragment implements
		LoaderCallbacks<List<ServiceConfiguration>> {

	@Inject
	private CloudFoundry client;

	private Spinner choices;
	
	private EditText name;
	
	private Button okButton;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.service_edit, null);
		choices = (Spinner) v.findViewById(R.id.type);
		name = (EditText) v.findViewById(R.id.name);
		getLoaderManager().initLoader(0, null, this);
		
		name.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == IME_ACTION_DONE && ready()) {
					createService();
					return true;
				}
				return false;
			}


		});
		
		name.addTextChangedListener(new BaseTextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				updateEnablement();
			}
		});
		
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.edit_service_dialog_title)
				.setView(v)
				.setCancelable(true)
				.setPositiveButton(R.string.create,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								createService();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		// workaround for http://code.google.com/p/android/issues/detail?id=6360 ...
		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface di) {
				okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				updateEnablement();
			}
		});
		
		return dialog;
	}
	
	private boolean ready() {
		return !name.getText().toString().trim().isEmpty();
	}

	private void createService() {
		new TaskWithDialog<Void>(getActivity(), R.string.working) {
			@Override
			public Void call() throws Exception {
				ServiceConfiguration sc = (ServiceConfiguration) choices.getSelectedItem();
				client.createService(name.getText().toString().trim(), sc);
				return null;
			}
		}.execute();
	}

	
	@Override
	public Loader<List<ServiceConfiguration>> onCreateLoader(int id, Bundle args) {
		return new AsyncLoader<List<ServiceConfiguration>>(getActivity()) {
			@Override
			public List<ServiceConfiguration> loadInBackground() {
				List<ServiceConfiguration> serviceConfigurations = client.getServiceConfigurations();
				Collections.sort(serviceConfigurations, new Comparator<ServiceConfiguration>() {
					@Override
					public int compare(ServiceConfiguration lhs,
							ServiceConfiguration rhs) {
						return lhs.getVendor().compareTo(rhs.getVendor());
					}
				});
				return serviceConfigurations;
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<ServiceConfiguration>> loader,
			List<ServiceConfiguration> data) {
		choices.setAdapter(new ItemListAdapter<ServiceConfiguration, ServiceConfigurationView>(
				R.layout.service_list_item, getActivity().getLayoutInflater(),
				data) {
			@Override
			protected ServiceConfigurationView createView(View view) {
				return new ServiceConfigurationView(view);
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<List<ServiceConfiguration>> loader) {

	}

	public void updateEnablement() {
		if (okButton != null) {
			okButton.setEnabled(ready());
		}
	}
}

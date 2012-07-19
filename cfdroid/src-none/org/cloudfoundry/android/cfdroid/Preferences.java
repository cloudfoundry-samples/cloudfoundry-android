package org.cloudfoundry.android.cfdroid;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(Scope.UNIQUE)
public interface Preferences {
	
	// See https://github.com/excilys/androidannotations/issues/264
	@DefaultString("https://api.cloudfoundry.com|CloudFoundry||http://api.eu01.aws.af.cm|AppFog AWS EU||http://api.ebottard.cloudfoundry.me:8080|Micro")
	/*Set<String>*/ String targets();
	
}

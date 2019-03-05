/*
 *  Copyright © 2010, Motorola Mobility, Inc. All rights reserved unless otherwise explicitly indicated.
 *  Redistribution and use in source and binary forms, with or without modification, are permitted provided 
 *  that the following conditions are met:
 *  
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following 
 *  disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Motorola Mobility, Inc. nor the names of its contributors may be used to endorse or promote products
 *  derived from this software without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.mot.dev;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * When enabled as a Device Administrator this class lets you control
 * some device policies and notifies user when there is interesting activity.
 * Overriding the onReceive() method prevents any of the convenience methods from being called.
 */
public class DeviceManagementReceiver extends DeviceAdminReceiver {
	
	private static String TAG = "DeviceManagementSample"; 

	/*
    static SharedPreferences getSamplePreferences(Context context) {
    	Log.v( TAG, " calling getSharedPreferences: " + DeviceAdminReceiver.class.getName() );
        return context.getSharedPreferences( DeviceAdminReceiver.class.getName(), 0 );
    }
    
    static String PREF_PASSWORD_QUALITY = "password_quality";
    static String PREF_PASSWORD_LENGTH = "password_length";
    static String PREF_MAX_FAILED_PW = "max_failed_pw";
    */
    

    void showToast(Context context, CharSequence msg) {
    	Log.v( TAG, msg.toString() );
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled( Context context, Intent intent ) {
        showToast( context, "Sample Device Admin: enabled" );
    }

    @Override
    public CharSequence onDisableRequested( Context context, Intent intent ) {
        return "Thank you for using our Device Management app!";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast( context, "Sample Device Admin: disabled" );
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast( context, "Sample Device Admin: pw changed" );
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast( context, "Sample Device Admin: pw failed" );
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast( context, "Sample Device Admin: pw succeeded" );
    }
        
} // end DeviceManagementReceiver class

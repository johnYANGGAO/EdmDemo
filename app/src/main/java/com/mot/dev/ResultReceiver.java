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

import com.motorola.app.admin.EdmErrorCode;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/*
 * This receiver listens for the "mot.app.admin.edm.ERROR_STATUS" Broadcast Action and will be called by framework to return result 
 * codes (error code)
 */
public class ResultReceiver extends BroadcastReceiver {
	
	// Used for debugging purposes
	private String TAG = "DeviceManagementSample";
	
    void showToast( Context context, CharSequence msg ) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    
	@Override
	public void onReceive( Context context, Intent intent ) {
		Log.v( TAG, " ResultReceiver.onReceive " );
		
		String intentString = intent.getAction();
        Log.v( TAG, "onReceive(), received intent:" + intentString );
        if ( intentString.equals( EdmErrorCode.ACTION_EDM_ERROR_STATUS.toString() )) {
        	
        	// A successful calls to DevicePolicyManagerExt.getVpnById() will include a Bundle identified by "EXTRA_ARGS"
        	Bundle extraArgs = intent.getBundleExtra( "EXTRA_ARGS" );
        	if ( extraArgs != null ) {
        		// Extract the VPN configuration values
                Log.d( TAG,"VPN_ID = " + extraArgs.getString("VPN_ID") );
                Log.d( TAG,"VPN_TYPE = " + extraArgs.getString("VPN_TYPE") );
                Log.d( TAG,"VPN_NAME = " + extraArgs.getString("VPN_NAME") );
                Log.d( TAG,"VPN_SERVER_NAME = " + extraArgs.getString("VPN_SERVER_NAME") );
                Log.d( TAG,"VPN_DOMAIN_SUFFICES = " + extraArgs.getString("VPN_DOMAIN_SUFFICES") );
                Log.d( TAG,"L2TP_SECRET_ENABLED = " + extraArgs.getString("L2TP_SECRET_ENABLED") );
                Log.d( TAG,"L2TP_SECRET = " + extraArgs.getString("L2TP_SECRET") );
                Log.d( TAG,"CA_CERT = " + extraArgs.getString("CA_CERT") );
                Log.d( TAG,"USER_CERT = " + extraArgs.getString("USER_CERT") );
                Log.d( TAG,"PSK = " + extraArgs.getString("PSK") );
                Log.d( TAG,"ENCRYPT_ENABLED = " + extraArgs.getString( "ENCRYPT_ENABLED") );
             }
        	
        	// Check error code
        	int code = (int)intent.getIntExtra( EdmErrorCode.ERROR_CODE.toString(), 0 );        	
        	
        	// Handle error codes accordingly. In this sample application we simply display a Toast to user
            switch( code ) {
            
            case EdmErrorCode.EDM_ERROR_STATUS_SUCCESS:
            	Log.v( TAG, "EDM_ERROR_STATUS_SUCCESS" );
            	showToast( context, " You got it!! " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_CANCELLED:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_CANCELLED" );
            	showToast( context, " Error :( " + code );
            	break; 
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_DELETION_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_DELETION_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_EMPTY_CERTDATA:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_EMPTY_CERTDATA" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_INVALID_ARGUMENTS:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_INVALID_ARGUMENTS" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_INVALID_CERTNAME:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_INVALID_CERTNAME" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_INVALID_CERTTYPE:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_EMPTY_CERTDATA" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_TOO_LARGE_CERTIFICATE:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_TOO_LARGE_CERTIFICATE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_UNABLE_TO_SAVE_CERTIFICATE:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_UNABLE_TO_SAVE_CERTIFICATE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_CERT_WRONG_PASSWORD_OR_INVALID_DATA:
            	Log.v( TAG, "EDM_ERROR_STATUS_CERT_WRONG_PASSWORD_OR_INVALID_DATA" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_EAS_ACCT_ADD_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_EAS_ACCT_ADD_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_EAS_ACCT_ALREADY_EXISTS:
            	Log.v( TAG, "EDM_ERROR_STATUS_EAS_ACCT_ALREADY_EXISTS" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_EAS_ACCT_DELETE_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_EAS_ACCT_DELETE_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_EAS_ACCT_FETCH_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_EAS_ACCT_FETCH_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_PATTERN_FAILED_TO_DELETE:
            	Log.v( TAG, "EDM_ERROR_STATUS_PATTERN_FAILED_TO_DELETE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_PATTERN_NOT_FOUND_TO_DELETE:
            	Log.v( TAG, "EDM_ERROR_STATUS_PATTERN_NOT_FOUND_TO_DELETE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_SD_CARD_NOT_FOUND:
            	Log.v( TAG, "EDM_ERROR_STATUS_SD_CARD_NOT_FOUND" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_ADD_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_ADD_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_CA_CERTIFICATE:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_CA_CERTIFICATE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_ID:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_ID" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_ID_DELETE_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_ID_DELETE_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_ID_FETCH_FAILED:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_ID_FETCH_FAILED" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_IPSEC_PRE_SHARED_KEY:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_IPSEC_PRE_SHARED_KEY" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_L2TP_SECRET:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_L2TP_SECRET" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_SERVER_NAME:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_SERVER_NAME" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_USER_CERTIFICATE:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_USER_CERTIFICATE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_INVALID_VPN_TYPE:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_INVALID_VPN_TYPE" );
            	showToast( context, " Error :( " + code );
            	break;
            	
            case EdmErrorCode.EDM_ERROR_STATUS_VPN_NAME_ALREADY_EXISTS:
            	Log.v( TAG, "EDM_ERROR_STATUS_VPN_NAME_ALREADY_EXISTS" );
            	showToast( context, " Error :( " + code );
            	break;
          
            default:
            	showToast( context, " Code: " + code );     	           
       	 
            }
        	
        }
	}

}

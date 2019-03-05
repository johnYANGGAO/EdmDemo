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


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
// com.motorola.app.admin is the EDM SDK API
import com.motorola.app.admin.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.io.FileReader;



import com.mot.dev.R;

/*
 * The main Activity for this sample application
 *
 */
public class DMActivity extends Activity {

	// UI members
    private Button mWipeDataButton;
    private Button mEmailEasAddButton;
    private static int sizeOfUUIDArray = 10;
    private static UUID[] uuis = new UUID[sizeOfUUIDArray];
    private Button mActiveSyncIDButton;
    private Button mDeleteEasAccButton;
    private Button mEasVpnAddAccButton;
    private Button mEasVpnDeleteAccButton;
    private Button mEasVpnByIDButton;
    private Button mCertificateInstButton;
    private CheckBox checkBox = null;
    // Identifier used in onActivityResult method
	static final int RESULT_ENABLE = 1;
	// A DevicePolicyManager class member.
	private DevicePolicyManager mDPM;
	// The DevicePolicyManagerExt class member.
	// Many of the DevicePolicyManagerExt class method calls
	// require a DevicePolicyManager object as parameter
	private DevicePolicyManagerExt mDPMExt;
	private ActivityManager mAM;
	private ComponentName mDeviceManagementSample;
	// The Device admin Receiver class to listen to Core Admin Operations like Enable/Disable
	DeviceManagementReceiver mDMReceiver;
	// EasConfig member used for configuring email account
    private EasConfig easconfig;
    // The name of the certificate located on SDCard
    private static final String CERT_NAME = "<certificate name on SDCard>";

    // String used to identify the VPN configuration.
    private final String VPN_ID = "Give a value here"; //Eg. 12345678
    // Depending on the VPN configuration type private members.
    // This sample application only uses type L2tpConfig
    //private L2tpConfig  l2tpconfig;
    private PptpConfig pptpconfig;
    //private L2tpIpsecConfig l2tpipsecconfig;
    //private L2tpIpsecPskConfig l2tpipsecpskconfig;

    private CertificateConfig certConfig;
    // For logging purposes
    static String TAG = "DeviceManagementSample";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       // instantiate our class members
       mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
       mAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
       mDMReceiver = new DeviceManagementReceiver( );
       mDeviceManagementSample = new ComponentName( this, DeviceManagementReceiver.class );
       mDPMExt = new DevicePolicyManagerExt(  );

       // Setup UI
       setContentView( R.layout.main );

       // Checkbox to enable application as device administrator
       checkBox = ( CheckBox ) findViewById( R.id.enable_disable_checkbox );
       checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
           public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
               if ( isChecked ) {
            	   Log.v( TAG, " onCheckedChanged isChecked : " + isChecked );
            	   // Launch the device administrator Activity
                   Intent intent = new Intent( DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN );
                   intent.putExtra( DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceManagementSample );
                   startActivityForResult( intent, RESULT_ENABLE );
               } else {
            	   mDPM.removeActiveAdmin( mDeviceManagementSample );
                   updateButtonStates();
               }

           }
       });

       // Instantiate buttons and set listeners
       mWipeDataButton = (Button)findViewById(R.id.wipe_data);
       mWipeDataButton.setOnClickListener(mWipeDataListener);

       mEmailEasAddButton = (Button)findViewById(R.id.email_add);
       mEmailEasAddButton.setOnClickListener(mEmailAddListner);

       mActiveSyncIDButton = (Button)findViewById(R.id.get_active_syncid);
       mActiveSyncIDButton.setOnClickListener(mActiveSyncIDListner);

       mDeleteEasAccButton = (Button)findViewById(R.id.eas_email_delete);
       mDeleteEasAccButton.setOnClickListener(mActiveEmailDeleteIDListner);

       mEasVpnAddAccButton = (Button)findViewById(R.id.eas_vpn_add);
       mEasVpnAddAccButton.setOnClickListener(mVpnAddIDListener);

       mEasVpnDeleteAccButton = (Button)findViewById(R.id.eas_vpn_delete);
       mEasVpnDeleteAccButton.setOnClickListener(mVpnDeleteIDListner);

       mEasVpnByIDButton = (Button)findViewById(R.id.get_vpn_id);
       mEasVpnByIDButton.setOnClickListener( mVpnByIDListener );

       mCertificateInstButton = (Button)findViewById(R.id.cert_inst);
       mCertificateInstButton.setOnClickListener(mCertInstAddListner);
   }


   /*
    * Disable or enable the UI controls as needed. When enabled as a device
    * administrator, enable the UI. When disabled, disable the UI controls to prevent user from using them
    */
   void updateButtonStates() {
       boolean active = mDPM.isAdminActive(mDeviceManagementSample);
       if (active) {
           mEmailEasAddButton.setEnabled( true );
           mCertificateInstButton.setEnabled( true );
           mActiveSyncIDButton.setEnabled( true );
           mDeleteEasAccButton.setEnabled( true );
           mEasVpnAddAccButton.setEnabled( true );
           mEasVpnDeleteAccButton.setEnabled( true );
           mEasVpnByIDButton.setEnabled( true );
           mWipeDataButton.setEnabled(true);

       } else {
           mEmailEasAddButton.setEnabled( false );
           mCertificateInstButton.setEnabled( false );
           mActiveSyncIDButton.setEnabled( false );
           mDeleteEasAccButton.setEnabled( false );
           mEasVpnAddAccButton.setEnabled( false );
           mEasVpnDeleteAccButton.setEnabled( false );
           mEasVpnByIDButton.setEnabled( false );
           mWipeDataButton.setEnabled( false );

       }
   }



   @Override
   protected void onResume() {
       super.onResume();
       updateButtonStates();
   }


   /*
    * Called by the framework returning result of Enable Device Administrator screen.
    * Generally called to return result of Activities launched via startActivityForResult()
    */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode) {
           case RESULT_ENABLE:
               if (resultCode == Activity.RESULT_OK) {
                   Log.v(TAG, "Admin enabled!");
               } else {
            	   // User hit Cancel
            	   checkBox.setChecked( false );
                   Log.v(TAG, "Admin not enabled");
               }
               return;
       }
       super.onActivityResult( requestCode, resultCode, data );
   }




   /*
    * Listener called when user selects the 'Certificate Inst.' button
    */
   private OnClickListener mCertInstAddListner = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You can't lock my screen because you are a monkey!");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }
           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
           if (active) {

        	   char[] in = new char[ 5120 ]; // 5 kbs
        	   byte[] bytes = new byte[5120];
        	   int size = 0;

        	   boolean found = false;

        	   FileReader reader = null;
        	   certConfig = new CertificateConfig();
        	   certConfig.setCertificateId( "Test cert name" );
        	   certConfig.setCertType( "PKCS12" ); // // "CERT" or "PKCS12" are valid parameters;
        	   certConfig.setPassword( "<some password>" );

        	   // Read data from SDCard for testing
        	   String state = Environment.getExternalStorageState();
        	    if ( Environment.MEDIA_MOUNTED.equals(state) ) {
        	    	// Get an array of File[]
        	    	File[] files = Environment.getExternalStorageDirectory().listFiles();
        	    	for( File f : files ) {
        	    		Log.v( TAG, " FILE NAME: " + f.getName() );
        	    		if( f.getName().equals( CERT_NAME) == true ) {
        	    			Log.v( TAG, "Found cert: " + CERT_NAME );
        	    			found = true;
        	    			try {
        	    				// Now read it
            	    			reader = new FileReader( f );

        	    				size = reader.read( in );
        	    				Log.v( TAG, " cert size: " + size );
        	    				bytes = in.toString().getBytes();

        	    				certConfig.setData( bytes );

        	    			} catch( IOException ioe ) {
        	    				Log.v( TAG, " SOME ERROR happened while reading cert from sd card. " );
        	    			}

        	    			// Now set the cert
        	        	    Log.v( TAG, " calling installCertificate " );
        	        	    mDPMExt.installCertificate( certConfig, mDPM );

        	        	    // We're finished here. Get out of this loop
        	        	    break;
        	    		}
        	    	}

        	    	// if not found logic
        	    	if( found == false ) {
        	    		Toast.makeText( DMActivity.this, "Where's" + CERT_NAME + "?", Toast.LENGTH_SHORT ).show();
        	    	}


        	    } else {
        	    	Toast.makeText( DMActivity.this, "Can't access SD Card", Toast.LENGTH_SHORT ).show();
        	    }

        	    if( reader != null ) {
        	    	try {
    					reader.close();
    				} catch ( IOException e ) {
    					e.printStackTrace();
    				}
        	    }
           }
       }
   };


   /*
    * A UUID used when configuring the EasConfig parameter
    * for DevicePolicyManagerExt.addEmailAccount()
    */
   public UUID generateUUID() {
   	UUID tempy = UUID.randomUUID();
   	uuis[ 0 ] = tempy;
   	return tempy;
   }


   /*
    * Listener called when user selects the 'Add Email' button
    */
   private OnClickListener mEmailAddListner = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this );
               builder.setMessage("You can't lock my screen because you are a monkey!");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }

           boolean active = mDPM.isAdminActive( mDeviceManagementSample );
           if (active) {
               easconfig = new EasConfig();
               // Set a UUID as identifier
               easconfig.setConfigurationId( generateUUID() );
               easconfig.setEmailAddress( "<email address>" );
               easconfig.setHostServer( "<server name or IP address>" );
               easconfig.setUserName( "<username>" );
               easconfig.setPassword( "<password>" );
               easconfig.setIsSsl( true ); // true or false

               // Logs
               Log.v( TAG, easconfig.getEmailAddress() );
               Log.v( TAG, easconfig.getHostServer() );
               Log.v( TAG, easconfig.getUserName() );
               Log.v( TAG, easconfig.getPassword() );

               mDPMExt.addEmailAccount( easconfig, mDPM );
           }
       }
   };


   /*
    * Listener called when user selects the 'Delete Email' button
    */
   private OnClickListener mActiveEmailDeleteIDListner = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this );
               builder.setMessage("You want to get the device ID !");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }


           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
           if (active) {
           	Log.v( TAG, " mActiveEmailDeleteIDListner START"  );
               easconfig = new EasConfig();

          //     easconfig.setConfigurationId( uuis[0] );
               easconfig.setEmailAddress( "<email address>" );
               easconfig.setHostServer( "<server name or IP address>" );
               easconfig.setUserName( "<username>" );
               easconfig.setPassword( "<password>" );
               easconfig.setIsSsl( true ); // true or false
               mDPMExt.removeEmailAccount( easconfig, mDPM );
               Log.v( TAG, " mActiveEmailDeleteIDListner END"  );
           }
       }
   };


   /*
    * Listener called when user selects the 'Get Sync ID' button
    */
  private OnClickListener mActiveSyncIDListner = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You want to get the decice ID !");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }


           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
           if (active) {
        	   // Asynchronous call to get Active Sync ID - Received in SyncIdReceiver.
               mDPMExt.getActiveSyncDeviceId( mDPM );
           }
       }
   };


   /*
    * Listener called when user selects the 'Get VPN ID' button
    */
  private OnClickListener mVpnByIDListener = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You want to get the decice ID !");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }


			boolean active = mDPM.isAdminActive(mDeviceManagementSample);
			if (active) {
				Log.v( TAG, " mVpnByIDListener called " );
				// Fetch Vpn.
				pptpconfig =  new PptpConfig();
				// VPN_ID is a class member used as an identifier for a VPN configuration
				pptpconfig.setId( VPN_ID );
				mDPMExt.getVpnById( pptpconfig, mDPM );
			}
		}
  };


  /*
   * Listener called when user selects the 'Delete VPN' button
   */
  private OnClickListener mVpnDeleteIDListner = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You want to get the decice ID !");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }
           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
           if (active) {
        	   // Identify the VPN configuration to delete using identifier - VPN_ID
				pptpconfig =  new PptpConfig();
				pptpconfig.setId( VPN_ID );
				mDPMExt.deleteVpn( pptpconfig, mDPM );
           }
       }
  };


  /*
   * Listener called when user selects the 'Add VPN' button
   */
  private OnClickListener mVpnAddIDListener = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You want to get the decice ID !");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }
           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
           if (active) {
        	   Log.v( TAG, " mVpnAddIDListener START " );

                 // For PptpConfig
                pptpconfig = new PptpConfig();
                pptpconfig.setName("<set name for account>");
                pptpconfig.setId( VPN_ID ); // set the ID for the account
                pptpconfig.setServer("<server or server IP>");
                pptpconfig.setDnsSearchDomain("<search domain here is available>");
                pptpconfig.setEncryptionEnabled( true ); // true or false
                mDPMExt.configureVpn(pptpconfig,mDPM);


               Log.v( TAG, " mVpnAddIDListener END " );
           }
       }
   };





   /*
    * Listener called when user selects the 'Wipe data' button
    */
   private OnClickListener mWipeDataListener = new OnClickListener() {
       public void onClick(View v) {
           if (mAM.isUserAMonkey()) {
               // Don't trust monkeys to do the right thing!
               AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
               builder.setMessage("You can't wipe my data because you are a monkey!");
               builder.setPositiveButton("I admit defeat", null);
               builder.show();
               return;
           }
           AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
           builder.setMessage(" This will erase all of your data.  Are you sure?");
           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   AlertDialog.Builder builder = new AlertDialog.Builder( DMActivity.this);
                   builder.setMessage( "This is not a test. "
                           + "This WILL erase all of your SDCard data! "
                           + "Are you sure?");
                   builder.setPositiveButton( "Wipe!", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           boolean active = mDPM.isAdminActive(mDeviceManagementSample);
                           if (active) {
                                 /* Example for calling the wipePattern(DevicePolicyManager dpm, java.lang.String pattrn) method
                                  * The first parameter you already know from other DevicePolicyManagerExt methods.
                                  * The second parameter is a String representation of your pattern matching regular expression
                                  * Example #1. Want to delete all PDF documents on the SDCard? "^(.*)\.pdf$"
                                 */

								mDPMExt.wipeSdCard( mDPM );
								Toast.makeText( DMActivity.this, "Wiped!", Toast.LENGTH_LONG).show();
                           }
                       }
                   });
                   builder.setNegativeButton( "Stop here!", null );
                   builder.show();
               }
           });
           builder.setNegativeButton( "No!", null);
           builder.show();
       }
   };

} // end DMActivity class
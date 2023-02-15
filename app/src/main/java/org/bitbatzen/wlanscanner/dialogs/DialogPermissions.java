/*
 *  Copyright (C) 2020 Benjamin W. (bitbatzen@gmail.com)
 *
 *  This file is part of WLANScanner.
 *
 *  WLANScanner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WLANScanner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WLANScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bitbatzen.wlanscanner.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.bitbatzen.wlanscanner.MainActivity;
import org.bitbatzen.wlanscanner.R;

import java.util.List;


public class DialogPermissions
        extends Dialog
        implements android.view.View.OnClickListener {

    public Activity activity;

    public TextView permissionHeader;
    public TextView permissionInfo;
    public TextView permissionFooter;
    public Button buttonOk;

    public List<String> permissionsToRequest;


    public DialogPermissions(Activity activity, List<String> permissionsToRequest) {
        super(activity);
        this.activity               = activity;
        this.permissionsToRequest   = permissionsToRequest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_permissions);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        permissionHeader    = (TextView) findViewById(R.id.tv_dialog_permission_header);
        permissionInfo      = (TextView) findViewById(R.id.tv_dialog_permission_info);
        permissionFooter    = (TextView) findViewById(R.id.tv_dialog_permission_footer);

        if (android.os.Build.VERSION.SDK_INT < 26) {
            permissionHeader.setText("The app requires all requested permissions to work properly.");
            permissionInfo.setVisibility(View.GONE);
        } else {
            // API VERSION >= 26
            String requirements = "";
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                permissionHeader.setText("To get scan results the following requirements must be met:");
                requirements += "• the app has the permission to query the location services";
            }
            
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                requirements += "\n\n• the location services on the device are enabled (Settings > Location)";
            }

            permissionInfo.setText(requirements);
        }

        permissionFooter.setText("If your system supports it, you can grant the permissions in the next step.");

        buttonOk = (Button) findViewById(R.id.button_dialog_ok);
        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dialog_ok:
                break;
            default:
                break;
        }

        dismiss();

        MainActivity ma = (MainActivity) activity;
        ma.requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]));
    }
}
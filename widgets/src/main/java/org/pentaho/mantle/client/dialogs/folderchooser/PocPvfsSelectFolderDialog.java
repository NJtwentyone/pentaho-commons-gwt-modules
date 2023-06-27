package org.pentaho.mantle.client.dialogs.folderchooser;
/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2023 Hitachi Vantara..  All rights reserved.
 */

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.panel.VerticalFlexPanel;

public class PocPvfsSelectFolderDialog extends PromptDialogBox {

  private static final String PVFS_TITLE = "Pvfs Select Title";
  private static final String PVFS_OK = "ok";
  private static final String PVFS_URL_HOST = "localhost";
  private static final String PVFS_CANCEL = "cancel";

  public VerticalPanel dialogContent;

  private static final String PVFS_URL_OPEN_FORMAT="http://%s:8080/pentaho/osgi/@pentaho/di-plugin-file-open-save-new-js@9.6.0.0-SNAPSHOT/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon";

  private static final String PVFS_URL_OPEN_EXAMPLE="http://localhost:8080/pentaho/osgi/@pentaho/di-plugin-file-open-save-new-js@9.6.0.0-SNAPSHOT/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon";


  public PocPvfsSelectFolderDialog( String selectedPath ) {
    super( PVFS_TITLE , PVFS_OK, PVFS_CANCEL,false, true );
    initializeDialogContent();
  }

  void initializeDialogContent() {
    dialogContent = new VerticalFlexPanel();
    Frame frame = new Frame(); // NOTE: look at org.pentaho.mantle.client.dialogs.scheduling.ScheduleParamsWizardPanel#setParametsUrl(String) for use of Frame
    frame.setUrl( openUrl() );
    dialogContent.add( frame );
    setContent( dialogContent );
  }

  String openUrl() {
    return openUrl( PVFS_URL_HOST );
  }

  String openUrl ( String host ) {
    return String_simpleFormat( PVFS_URL_OPEN_FORMAT, host );
  }

  public static String String_simpleFormat( final String strFormat, final String... args) {
    String[] split = strFormat.split("%s");
    final StringBuffer msg = new StringBuffer();
    for (int pos = 0; pos < split.length - 1; pos += 1) {
      msg.append(split[pos]);
      msg.append(args[pos]);
    }
    msg.append(split[split.length - 1]);
    return msg.toString();
  }
}

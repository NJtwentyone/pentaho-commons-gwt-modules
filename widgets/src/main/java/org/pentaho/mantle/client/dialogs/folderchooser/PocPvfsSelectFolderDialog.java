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
package org.pentaho.mantle.client.dialogs.folderchooser;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.panel.VerticalFlexPanel;

/**
 * Proof of Concept class just to display PVFS javascript file browser dialog.
 * NOTE: No decision is being made on coding style or design.
 *
 * Please see https://hv-eng.atlassian.net/browse/BACKLOG-37949 on how to deploy PVFS file browser.
 */
public class PocPvfsSelectFolderDialog extends PromptDialogBox {

  private static final String PVFS_TITLE = "Pvfs Select Title";
  private static final String PVFS_OK = "ok";
  private static final String PVFS_CANCEL = "ok";
  // full example: http://127.0.0.1:8080/pentaho/osgi/@pentaho/di-plugin-file-open-save-new-js@9.6.0.0-SNAPSHOT/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon
  private static final String PVFS_URL_OPEN_FORMAT = "http://%1$s:%2$s%3$s/di-plugin-file-open-save-new-js@%4$s/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon";

  public VerticalPanel dialogContent;

  public PocPvfsSelectFolderDialog( String selectedPath ) {
    super( PVFS_TITLE , PVFS_OK, PVFS_CANCEL ,false, true );
    initializeDialogContent();
  }

  String getOpenUrl() {
    String host = System.getenv().getOrDefault( "POC_PVFS_HOST", "localhost" );
    String port = System.getenv().getOrDefault( "POC_PVFS_PORT", "8080" );
    String contextPath = System.getenv().getOrDefault( "POC_PVFS_CONTEXT_PATH", "/pentaho/osgi" );
    String version = System.getenv().getOrDefault( "POC_PVFS_VERSION", "9.6.0.0-SNAPSHOT" );
    return getOpenUrl( host, port, contextPath, version );
  }

  String getOpenUrl(String host, String port, String contextPath, String version) {
    return String.format( PVFS_URL_OPEN_FORMAT, host, port, contextPath, version );
  }

  void initializeDialogContent() {
    dialogContent = new VerticalFlexPanel();
    String url = getOpenUrl();
    Frame frame = new Frame( url ); // NOTE: look at org.pentaho.mantle.client.dialogs.scheduling.ScheduleParamsWizardPanel#setParametsUrl(String) for use of Frame
    dialogContent.add( frame );
    setContent( dialogContent );
  }


  /*
   * TODO integrate with {@link org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox#setCallback( IDialogCallback )
   * follow code at {@link org.pentaho.mantle.client.dialogs.scheduling.NewScheduleDialog#createUI }
   */
}

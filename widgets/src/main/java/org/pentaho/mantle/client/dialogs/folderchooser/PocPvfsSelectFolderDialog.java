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

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.panel.VerticalFlexPanel;

public class PocPvfsSelectFolderDialog extends PromptDialogBox {

  private static final String PVFS_TITLE = "Pvfs Select Title";
  private static final String PVFS_OK = "ok";
  private static final String PVFS_CANCEL = "cancel";

  // URL CONFIGURATION
  private static final String PVFS_URL_HOST = "localhost";
  private static final String PVFS_URL_PORT = "8080";
  private static final String PVFS_URL_CONTEXT_PATH = "/pentaho/osgi";
  private static final String PVFS_URL_VERSION = "9.6.0.0-SNAPSHOT";

  public VerticalPanel dialogContent;
  public Frame frame;

  public String jsObject;

  public String testSelectJsonStr = "{\"path\":\"/users/devuser/documents/someFolder\", \"name\":\"someFileName.txt\"}";

  /**
   * Example full url: http://localhost:8080/pentaho/osgi/@pentaho/di-plugin-file-open-save-new-js@9.6.0.0-SNAPSHOT/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon
   */
  private static final String PVFS_URL_OPEN_FORMAT="http://%s:%s%s/@pentaho/di-plugin-file-open-save-new-js@%s/index.html#!/selectFileFolder?providerFilter=default&filter=TXT,CSV,ALL&defaultFilter=TXT&origin=spoon";

  /**
   * Proof of Concept class just to display PVFS javascript file browser dialog.
   * NOTE: No decision is being made on coding style or design.
   *
   * Please see https://hv-eng.atlassian.net/browse/BACKLOG-37949 on how to deploy PVFS file browser.
   */
  public PocPvfsSelectFolderDialog( String selectedPath ) {
    super( PVFS_TITLE , PVFS_OK, PVFS_CANCEL,false, true ); // TODO get rid of double set of OK/Cancel button 1 from PromptDialogBox, 1 from PVFS Dialog
    initializeDialogContent();
    setFrameUrl( selectedPath );
  }

  void initializeDialogContent() {
    dialogContent = new VerticalFlexPanel();
    dialogContent.getElement().setId( "pvfs-panel" ); // adding id just for debugging
    frame = new Frame(); // NOTE: look at org.pentaho.mantle.client.dialogs.scheduling.ScheduleParamsWizardPanel#setParametsUrl(String) for use of Frame
    frame.getElement().setId( "pvfs-panel-frame" ); // adding id just for debugging
    dialogContent.add( frame );

    // TODO set better defaults and use existing logic for sizing + investigate WCAG for PVFS select folder
    dialogContent.setHeight( "600px" ); // hard coded just for poc
    frame.setHeight( "600px" ); // hard coded just for poc
    setResponsive( true );
    setSizingMode( DialogSizingMode.FILL_VIEWPORT_WIDTH );
    setWidthCategory( DialogWidthCategory.EXTRA_LARGE );

    exportSelect(this); // inject select() into client
    setContent( dialogContent );
  }

  void setFrameUrl(String selectPath) {
    frame.setUrl( openUrl() ); // TODO consume selectPath
  }

  String openUrl() {
    return openUrl( PVFS_URL_HOST, PVFS_URL_PORT, PVFS_URL_CONTEXT_PATH, PVFS_URL_VERSION );
  }

  String openUrl ( String host, String port, String contextPath, String version ) {
    return String_simpleFormat( PVFS_URL_OPEN_FORMAT, host, port, contextPath, version );
  }

  public String getSelectedPath() {
    return ( jsObject != null )
      ? constructPath(jsObject)
      : "<nothing-was-passed-back>";
  }

  public void setJsObject(String jsonObject) {
    jsObject = jsonObject;
  }

  /* TODO convert from JSNI to JsInterop, only used JSNI because syntax seems easier
   JSNI - https://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJSNI.html
    from page : "(INFO: For new implementations use the future-proof JsInterop instead. JSNI will be removed with GWT 3.)"
   JsInterop - https://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJsInterop.html
   */

  /**
   * Have to inject a javascript function "select" on the client. The PVFS browser select calls this function
   * to pass back the selected file.
   * See https://github.com/pentaho/pentaho-kettle/blob/9.3.0.4/plugins/file-open-save-new/core/src/main/javascript/app/services/providers/local.service.js#L170-L178
   * You will see something like this:
   * <code>
   *    function open(file) {
   *           select(JSON.stringify({
   *             name: file.name,
   *             path: file.path,
   *             parent: file.parent,
   *             connection: file.connection,
   *             provider: file.provider
   *     }));
   *    }
   * </code>
   *
   *  the function select needs to be added by calling client.
   *
   *  PDI SWT had this implementations: https://github.com/pentaho/pentaho-kettle/blob/9.3.0.4/plugins/file-open-save-new/core/src/main/java/org/pentaho/di/plugins/fileopensave/dialog/FileOpenSaveDialog.java#L167-L177
   *  <code>
   *        new BrowserFunction( browser, "select" ) {
   *       @Override public Object function( Object[] arguments ) {
   *           ... ... ...
   *           closeBrowserWithParameters( arguments );
   *            ... ... ...
   *       }
   *     };
   *  </code>
   *  arguments is the json string and #closeBrowserWithParameters() will eventually parse the json
   * @param x
   */
  public static native void exportSelect(PocPvfsSelectFolderDialog x) /*-{
    $wnd.select = $entry(function(amt) {
      x.@org.pentaho.mantle.client.dialogs.folderchooser.PocPvfsSelectFolderDialog::setJsObject(Ljava/lang/String;)(amt);
    });
  }-*/;
/**
  * TODO update PVFS File Browse select javascript code to call window.parent.select(...) OR look into maybe just putting select() function on iframe
  * This is due to dialog is rendered in an iframe, so `window` refers to ifrome.
  * Then `window.parent` actually refers to PUC window where `$wnd` is referencing.
 */

  public static native void testFnSelect(String strTestJson) /*-{
    $wnd.select(strTestJson);
  }-*/;

  /**
   * Following code at : https://github.com/pentaho/pentaho-kettle/blob/9.3.0.4/ui/src/main/java/org/pentaho/di/ui/core/events/dialog/SelectionAdapterFileDialog.java#L326-L349
   * @param jsonStr
   * @return
   */
  public String constructPath( String jsonStr ) {
    JSONObject jsonObjectSelect =(JSONObject) JSONParser.parseStrict( jsonStr );
    String path  = jsonObjectSelect.get( "path" ).toString();
    String filename = jsonObjectSelect.get( "name" ).toString();
    // if jsonStr  isFile, then filename is already included, if jsonStr  isFolder then filename will be blank
    String fullPath = path.replace( "\"","" );
    return fullPath;
  }

  /*
   * FIXME can't use String.format in GWT get weird compilation
   *
   *  $> ... '.../pentaho/repo/pentaho-commons-gwt-modules/assemblies/widgets/target/.generated'
   *      'org.pentaho.gwt.widgets.Widgets' 'org.pentaho.gwt.widgets.client.filechooser.FileChooser'
   *       'org.pentaho.gwt.widgets.client.formatter.JSTextFormatter' 'org.pentaho.mantle.SchedulingDialogs'
   */
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

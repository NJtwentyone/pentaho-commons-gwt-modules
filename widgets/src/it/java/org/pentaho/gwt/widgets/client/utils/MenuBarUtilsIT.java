package org.pentaho.gwt.widgets.client.utils;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * see https://www.gwtproject.org/doc/latest/DevGuideTesting.html for reference
 */
public class MenuBarUtilsIT extends GWTTestCase {

  @Override
  // NOTE: path to org/pentaho/gwt/widgets/Widgets.gwt.xml
  public String getModuleName() {
    return "org.pentaho.gwt.widgets.Widgets"; //$NON-NLS-1$
  }

  public void testGetParentMenu_JSNI() {
    /**
     * NOTE: JSNI tests will fail, not testable through GWTTestCase, if necessary will have to via selenium
     * this is due to this due to JSNI is only compiled from JAVA into Javascript in production compilation.
     *
     * https://stackoverflow.com/questions/2543462/gwt-best-practice-for-unit-testing-mocking-jsni-methods
     *
     * TODO:
     *  - might be possible to play around with -Dgwt.args="..."
     *  - might be possible to test with -generateJsInteropExports
     *  -- if we convert JSNI to JSinterop
     *
     */

    MenuBar childMenuBar = new MenuBar();
    childMenuBar.setTitle( "child menu bar" );
    MenuBar parentMenuBar = new MenuBar();
    parentMenuBar.setTitle( "parent menu bar" );
    parentMenuBar.addItem("child", childMenuBar );

    MenuBar actualMenuBar = MenuBarUtils.getParentMenu( childMenuBar );
    assertNotNull( actualMenuBar );
    assertEquals( "parent menu bar", actualMenuBar.getTitle());

  }
  public void testGetParentMenu_nonJSNI() { // NOTE: Sanity check a test case works

    MenuBar childMenuBar = new MenuBar(true);
    childMenuBar.setTitle( "child menu bar" );
    MenuBar parentMenuBar = new MenuBar(true);
    parentMenuBar.setTitle( "parent menu bar" );
    MenuItem childMenuItem = parentMenuBar.addItem("child", childMenuBar );
//    MenuItem childMenuItem = new MenuItem("child", childMenuBar );

    MenuBar actualMenuBar = childMenuItem.getParentMenu();

    assertNotNull( actualMenuBar );
    assertEquals( "parent menu bar", actualMenuBar.getTitle());

    assertEquals(0, parentMenuBar.getItemIndex(  childMenuItem ));
  }

}
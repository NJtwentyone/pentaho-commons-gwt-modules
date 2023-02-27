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

package org.pentaho.gwt.widgets.client.text;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.TestCase;



public class PocXmlParserIT extends GWTTestCase {


  public void testWTF() throws Exception {
  /**
   * testing can I call code from package 'com.google.gwt.xml.client'
   * should be able to test xml parsing logic in package org.pentaho.ui.xul.gwt.tags
   * such as GwtToolbarbutton#init( com.google.gwt.xml.client.Element srcEle, XulDomContainer container )
   * where srcEle is:
   *  <toolbarbutton id="editContentButton" image="images/spacer.gif" disabledimage="images/spacer.gif"
   *         onclick="mantleXulHandler.editContentClicked()" tooltiptext="${editContent}" />
   */
  Document document = XMLParser.parse( "<note></note>" );
  assertEquals( "note", document.getFirstChild().getNodeName() );
  }



  // followed naming convetion from ToolTipIT.java
  // moduleName is project dependent, please update for new project/location
  public String getModuleName() {
    return "org.pentaho.gwt.widgets.Widgets"; //$NON-NLS-1$
  }


}

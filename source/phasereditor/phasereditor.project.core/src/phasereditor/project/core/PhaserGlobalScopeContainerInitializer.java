// The MIT License (MIT)
//
// Copyright (c) 2015 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.project.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;
import org.eclipse.wst.jsdt.core.compiler.libraries.SystemLibraryLocation;

import phasereditor.inspect.core.InspectCore;

public class PhaserGlobalScopeContainerInitializer extends JsGlobalScopeContainerInitializer {

	static class PhaserLibraryLocation extends SystemLibraryLocation {
		@Override
		public char[][] getLibraryFileNames() {
			return new char[][] { "phaser-api.js".toCharArray() };
		}

		@Override
		protected String getPluginId() {
			return InspectCore.RESOURCES_PLUGIN_ID;
		}

		@Override
		public IPath getLibraryPathInPlugin() {
			return new Path("phaser-custom/api/");
		}
	}

	private LibraryLocation _libLocation;

	@Override
	public LibraryLocation getLibraryLocation() {
		if (_libLocation == null) {
			_libLocation = new PhaserLibraryLocation();
		}
		return _libLocation;
	}

	@Override
	public String getDescription() {
		return "Phaser API " + InspectCore.PHASER_VERSION + " Library";
	}

	@Override
	public boolean allowAttachJsDoc() {
		return true;
	}

	@Override
	public IIncludePathEntry[] getIncludepathEntries() {
		// attach JSDoc to the phaser lib.
		try {
			String jsdocKey = IIncludePathAttribute.JSDOC_LOCATION_ATTRIBUTE_NAME;

			IIncludePathEntry[] entries = super.getIncludepathEntries();

			File bundleFile = FileLocator.getBundleFile(Platform.getBundle(InspectCore.RESOURCES_PLUGIN_ID));

			String jsdocValue = new File(bundleFile, "phaser-master/docs").getAbsoluteFile().getCanonicalPath();

			IIncludePathAttribute[] extraAttrs = { JavaScriptCore.newIncludepathAttribute(jsdocKey, jsdocValue) };

			// i am sure there is only one entry.

			IIncludePathEntry oldEntry = entries[0];

			IIncludePathEntry newEntry = JavaScriptCore.newLibraryEntry(oldEntry.getPath(),
					oldEntry.getSourceAttachmentPath(), oldEntry.getSourceAttachmentRootPath(),
					oldEntry.getAccessRules(), extraAttrs, true);

			entries[0] = newEntry;

			return entries;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
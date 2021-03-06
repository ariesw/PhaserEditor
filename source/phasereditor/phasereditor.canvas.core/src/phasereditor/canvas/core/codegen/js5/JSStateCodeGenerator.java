// The MIT License (MIT)
//
// Copyright (c) 2015, 2016 Arian Fornaris
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
package phasereditor.canvas.core.codegen.js5;

import phasereditor.canvas.core.CanvasModel;
import phasereditor.canvas.core.codegen.BaseStateGenerator;

/**
 * @author arian
 *
 */
public class JSStateCodeGenerator extends BaseStateGenerator {

	public JSStateCodeGenerator(CanvasModel model) {
		super(model);
	}

	@Override
	public void generateHeader() {
		String classname = _settings.getClassName();
		String baseclass = _settings.getBaseClass();

		// CLASS

		line("/**");
		line(" * " + classname + ".");
		line(" */");
		openIndent("function " + classname + "() {");
		line();
		line(baseclass + ".call(this);");

		trim(() -> {
			userCode(_settings.getUserCode().getState_constructor_before());
			userCode(_settings.getUserCode().getState_constructor_after());
		});

		closeIndent("}");
		line();

		line("/** @type " + baseclass + " */");
		line("var " + classname + "_proto = Object.create(" + baseclass + ".prototype);");
		line(classname + ".prototype = " + classname + "_proto;");
		line(classname + ".prototype.constructor = " + classname + ";");
		line();

		trim(() -> {
			generateInitMethod(classname);
			line();
			generatePreloadMethod(classname);
			line();
		});

		openIndent(classname + ".prototype.create = function () {");

		trim(() -> {
			line();
			userCode(_settings.getUserCode().getCreate_before());
			line();
		});
	}

	private void generatePreloadMethod(String classname) {
		openIndent(classname + ".prototype.preload = function () {");

		generatePreloadMethodBody();

		closeIndent("};");
	}

	private void generateInitMethod(String classname) {
		// INIT

		openIndent(classname + ".prototype.init = function (" + _settings.getUserCode().getState_init_args() + ") {");
		generateInitMethodBody();
		closeIndent("};");
	}

	@Override
	public void generateFooter() {
		trim(() -> {
			userCode(_settings.getUserCode().getCreate_after());
		});

		closeIndent("};");
		line();
		section(END_GENERATED_CODE, getYouCanInsertCodeHere());
	}

}

// The MIT License (MIT)
//
// Copyright (c) 2015, 2018 Arian Fornaris
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
package phasereditor.canvas.core;

import org.json.JSONObject;

import phasereditor.assetpack.core.BitmapFontAssetModel;
import phasereditor.bmpfont.core.BitmapFontModel.RenderArgs;

/**
 * @author arian
 *
 */
public class BitmapTextModel extends AssetSpriteModel<BitmapFontAssetModel> {

	public static final int DEF_FONT_SIZE = 32;
	public static final String TYPE_NAME = "bitmapText";
	public static final String PROPSET_TEXT = "text";
	public static final String PROPSET_SIZE = "fontSize";
	public static final String PROPSET_MAX_WIDTH = "maxWidth";
	public static final int DEF_MAX_WIDTH = 0;

	private String _text;
	private int _fontSize;
	private int _maxWidth;

	public BitmapTextModel(GroupModel parent, BitmapFontAssetModel assetKey) {
		super(parent, assetKey, TYPE_NAME);
		_text = "Bitmap Font";
		_fontSize = DEF_FONT_SIZE;
		_maxWidth = DEF_MAX_WIDTH;
	}

	public BitmapTextModel(GroupModel parent, JSONObject obj) {
		super(parent, TYPE_NAME, obj);
	}

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public int getFontSize() {
		return _fontSize;
	}

	public void setFontSize(int fontSize) {
		_fontSize = fontSize;
	}

	public int getMaxWidth() {
		return _maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		_maxWidth = maxWidth;
	}

	@Override
	protected void writeInfo(JSONObject jsonInfo, boolean saving) {
		super.writeInfo(jsonInfo, saving);

		boolean prefabInstance = isPrefabInstance();

		if (isOverriding(PROPSET_TEXT)) {
			if (prefabInstance) {
				jsonInfo.put("text", _text);
			} else {
				jsonInfo.put("text", _text, "");
			}
		}

		if (isOverriding(PROPSET_SIZE)) {
			if (prefabInstance) {
				jsonInfo.put("fontSize", _fontSize);
			} else {
				jsonInfo.put("fontSize", _fontSize, DEF_FONT_SIZE);
			}
		}

		if (isOverriding(PROPSET_MAX_WIDTH)) {
			if (prefabInstance) {
				jsonInfo.put("maxWidth", _maxWidth);
			} else {
				jsonInfo.put("maxWidth", _maxWidth, DEF_MAX_WIDTH);
			}
		}

	}

	@Override
	public void readInfo(JSONObject jsonInfo) {
		super.readInfo(jsonInfo);

		_text = jsonInfo.optString("text", "");
		_fontSize = jsonInfo.optInt("fontSize", DEF_FONT_SIZE);
		_maxWidth = jsonInfo.optInt("maxWidth", DEF_MAX_WIDTH);
	}

	public RenderArgs createRenderArgs() {
		return new RenderArgs(_text, _maxWidth);
	}

}

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
package phasereditor.canvas.ui.shapes;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import phasereditor.assetpack.core.FrameData;
import phasereditor.assetpack.core.IAssetFrameModel;
import phasereditor.assetpack.core.IAssetKey;
import phasereditor.assetpack.core.ImageAssetModel;
import phasereditor.canvas.core.TileSpriteModel;
import phasereditor.ui.ImageCache;

/**
 * @author arian
 *
 */
public class TileSpriteNode extends Pane implements ISpriteNode {

	private FrameData _frame;
	private TileSpriteControl _control;

	public TileSpriteNode(TileSpriteControl control) {
		_control = control;

		updateFromModel();
	}

	@Override
	public boolean contains(double localX, double localY) {
		if (!getClip().contains(localX, localY)) {
			return false;
		}

		for (Node node : getChildren()) {
			Point2D point = node.parentToLocal(localX, localY);
			if (node.contains(point)) {
				return true;
			}
		}

		return false;
	}

	public void updateFromModel() {
		IAssetKey assetKey = _control.getModel().getAssetKey();

		Image image;

		if (assetKey instanceof ImageAssetModel) {
			image = ImageCache.getFXImage(((ImageAssetModel) assetKey).getUrlFile());
			Rectangle rect = new Rectangle(0, 0, (int) image.getWidth(), (int) image.getHeight());
			_frame = new FrameData();
			_frame.src = rect;
			_frame.dst = rect;
			_frame.srcSize = new Point(rect.width, rect.height);
		} else {
			IAssetFrameModel frameModel = (IAssetFrameModel) assetKey;
			image = ImageCache.getFXImage(frameModel.getImageFile());
			_frame = frameModel.getFrameData();
		}

		TileSpriteModel model = _control.getModel();

		double width = model.getWidth();
		double height = model.getHeight();

		setWidth(width);
		setHeight(height);

		// GraphicsContext g2 = getGraphicsContext2D();
		// g2.clearRect(0, 0, width, height);

		double xoffs = model.getTilePositionX() % _frame.srcSize.x;
		double yoffs = model.getTilePositionY() % _frame.srcSize.y;

		double x0 = _frame.src.x;
		double y0 = _frame.src.y;
		double w0 = _frame.src.width;
		double h0 = _frame.src.height;

		double x1;
		double y1;

		if (xoffs == 0) {
			x1 = _frame.dst.x * model.getTileScaleX();
		} else if (xoffs < 0) {
			x1 = xoffs + _frame.dst.x * model.getTileScaleX();
		} else {
			x1 = xoffs - (_frame.srcSize.x + _frame.dst.x) * model.getTileScaleX();
		}

		if (yoffs == 0) {
			y1 = _frame.dst.y * model.getTileScaleY();
		} else if (yoffs < 0) {
			y1 = yoffs + _frame.dst.y * model.getTileScaleY();
		} else {
			y1 = yoffs - (_frame.srcSize.y + _frame.dst.y) * model.getTileScaleY();
		}

		// the non-trimmed size
		double w1 = _frame.srcSize.x * model.getTileScaleX();
		double h1 = _frame.srcSize.y * model.getTileScaleY();

		// the trimmed-real size, but scaled
		double w2 = w0 * model.getTileScaleX();
		double h2 = h0 * model.getTileScaleY();

		getChildren().clear();

		for (double x = x1; x < getWidth(); x += w1) {
			for (double y = y1; y < getHeight(); y += h1) {
				ImageView img = new ImageView(image);
				img.setViewport(new Rectangle2D(x0, y0, w0, h0));
				img.relocate(x, y);
				img.setFitWidth(w2);
				img.setFitHeight(h2);
				getChildren().add(img);
			}
		}

		setClip(new javafx.scene.shape.Rectangle(0, 0, width, height));

		setPrefWidth(width);
		setPrefHeight(height);
		setMinWidth(width);
		setMinHeight(height);
		setMaxWidth(width);
		setMaxHeight(height);
	}

	@Override
	public TileSpriteModel getModel() {
		return _control.getModel();
	}

	@Override
	public TileSpriteControl getControl() {
		return _control;
	}

	@Override
	public Node getNode() {
		return this;
	}
}

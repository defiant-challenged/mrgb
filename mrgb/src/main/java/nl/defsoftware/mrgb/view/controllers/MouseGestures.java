package nl.defsoftware.mrgb.view.controllers;

/**
 *
 * @author D.L. Ettema
 * @date 7 Jun 2016
 */
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGestures {

	final DragContext dragContext = new DragContext();

	GraphController graph;

	public MouseGestures(GraphController graph) {
		this.graph = graph;
	}

	public void makeDraggable(final Node node) {
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			Node node = (Node) event.getSource();

			double scale = graph.getScale();

			dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
			dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			Node node = (Node) event.getSource();

			double offsetX = event.getScreenX() + dragContext.x;
			double offsetY = event.getScreenY() + dragContext.y;

			// adjust the offset in case we are zoomed
			double scale = graph.getScale();

			offsetX /= scale;
			offsetY /= scale;

			node.relocate(offsetX, offsetY);
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
		}
	};

	class DragContext {
		double x;
		double y;
	}
}

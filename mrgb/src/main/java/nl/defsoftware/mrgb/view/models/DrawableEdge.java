package nl.defsoftware.mrgb.view.models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

/**
 * 
 * @author D.L. Ettema
 * @date 27 September 2016
 *
 */
public class DrawableEdge extends Line {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    
    public DrawableEdge(double xDelta, double yDelta) {
        
        setEndX(xDelta);
        setEndY(yDelta);

        setStrokeWidth(3.0);
        setFill(Paint.valueOf("BLACK"));
        setOpacity(0.5);
        setStartX(0);
        setStartY(0);
        setRotate(0.0);
    }
    
    public DrawableEdge(DrawableSequence from, DrawableSequence to) {
        setStrokeWidth(3.0);
        setFill(Paint.valueOf("BLACK"));
        setOpacity(0.5);
        
        startXProperty().bind(from.layoutXProperty().add(from.getBoundsInParent().getWidth() / 2.0));
        startYProperty().bind(from.layoutYProperty().add(from.getBoundsInParent().getHeight() / 2.0));

        endXProperty().bind(to.layoutXProperty().add( to.getBoundsInParent().getWidth() / 2.0));
        endYProperty().bind(to.layoutYProperty().add( to.getBoundsInParent().getHeight() / 2.0));
        
        startX = from.layoutXProperty().get();
        startY = from.layoutYProperty().get();
        endX = to.layoutXProperty().get();
        endY = to.layoutYProperty().get();
    }
    
    public double getLength() {
        return getEndY();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(endX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DrawableEdge other = (DrawableEdge) obj;
        if (Double.doubleToLongBits(endX) != Double.doubleToLongBits(other.endX))
            return false;
        if (Double.doubleToLongBits(endY) != Double.doubleToLongBits(other.endY))
            return false;
        if (Double.doubleToLongBits(startX) != Double.doubleToLongBits(other.startX))
            return false;
        if (Double.doubleToLongBits(startY) != Double.doubleToLongBits(other.startY))
            return false;
        return true;
    }
}

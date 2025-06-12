package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public abstract class ElementLook {
    /**
     * The nodes constituting that look must be gathered within a group.*
     */
    private final Group group;
    /**
     * The list of the shapes that are used to define the look.
     * In order to have a better collision detection, all nodes that are shapes
     * should be added to the group with addShape(), instead of addNode().
     * Meanwhile, those added with addNode() imply to create a rectangle shape
     * that is added to shapes. This rectangle has the dimensions of the bounding box of the
     * node.
     *
     */
    private final List<Shape> shapes;

    /**
     * The game element associated to this look
     */
    protected GameElement element;
    /**
     * the depth to enforce a particular order when painting the looks associated to game elements.
     *
     * By default, all elements are at depth 0 but it can set to a negative value.
     * The behavior is to show the look of elements at depth -1 below those at depth 0, -2 below -1, ...
     * The look of elements at the same depth are painted in the order they are added to the root pane
     */
    protected int depth;
    /**
     * An ElementLook can be put directly in the Group of the RootPane. In this case parent is null.
     * But it can also be put in a ContainerLook. In this case, parent is a reference to this containerLook.
     * Since the location in the RootPane is given by the location of the associated GameElement, this location cannot be
     * used directly to set the translateX a& translateY of the Group within the ElementLook. Indeed, as soon as a javafx node,
     * including a Group, is put in another Group, its position is relative to the Group. This is why, when location of a Gameelement changes,
     * the location of the associated ElementLook must be computed within the parent ElementLook (cf. onLocationChanged() below)
     */
    protected ElementLook parent;

    public static final int ANCHOR_CENTER = 0;
    public static final int ANCHOR_TOPLEFT = 1;
    /**
     * define the anchor point of the look within its bounding box.
     * The anchor point has always (0,0) coordinates in the local space of the look.
     * By default it is set to the center of the bounding box, so the top-left corner
     *  of the bounding box has a negative x value.
     *  It can also be set to the top-left corner of the bounding box, and in that case
     *  the top-left corner is in (0,0)
     * It influences the way the look will be rendered at x,y coordinates stored in the associated element.
     * If anchor is center, then the center of the look will be located in x,y.
     * If anchor is top-left, the top-left corner of the look will be located in x,y*
     * Some looks are easier to place when their anchor is top-left, e.g. walls.
     *
     * This attribute is mainly used when the look is managed by a layout, when the look
     * must be aligned within its container cell.
     */
    protected int anchorType;

    /**
     * A reference to the RootPane is needed in case of a GameElement is removed from a ContainerElement. In such a
     * case, the ElementLook of the GameElement is also removed from the ContainerLook. Thus, it becomes parent-less
     * and must be attached once again to the RootPane group.
     * This attributes is set by the RootPane itself during init() of the game stage.
     */
    private RootPane rootPane;

    public ElementLook(GameElement element, int depth) {
        this.element = element;
        group = new Group();
        group.setTranslateX(element.getX());
        group.setTranslateY(element.getY());
        shapes = new ArrayList<>();
        this.depth = depth;
        parent = null;
        anchorType = ANCHOR_CENTER;
        rootPane = null;
    }

    public ElementLook(GameElement element) {
        this(element, 0);
    }


    public GameElement getElement() {
        return element;
    }

    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ElementLook getParent() {
        return parent;
    }

    public void setParent(ElementLook parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent!=null?true:false;
    }

    public int getAnchorType() {
        return anchorType;
    }

    public void setAnchorType(int anchorType) {
        this.anchorType = anchorType;
    }

    public RootPane getRootPane() {
        return rootPane;
    }

    public void setRootPane(RootPane rootPane) {
        this.rootPane = rootPane;
    }

    public int getHeight() {
        Bounds b = group.getBoundsInLocal();
        return (int)b.getHeight();
    }

    public int getWidth() {
        Bounds b = group.getBoundsInLocal();
        return (int)b.getWidth();
    }

    /**
     * move the location of the group within the root pane space, and thus within the scene.
     * This method MUST NEVER be called directly. It is automatically called whenever
     * a game element is moved in space, creating a "LocationChange" event, for example when an animation is created, or
     * when the element is put/moved within a container. In that case, an event is created,
     * then the controller processes this event by telling the container look to put the
     * element look in the desired layout cell, which in returns computes the position in the
     * root pane and then changes the location of the element. This change generates a "LocationChange" event,
     * which is processed by the controller, and then by the gamestage view by calling this method.
     */
    public void onLocationChange() {
        Logger.trace("look location of ["+this+"] changed to "+element.getX()+","+element.getY());
        group.setTranslateX(element.getX());
        group.setTranslateY(element.getY());
    }
    /**
     * show or hide the nodes of this look.
     * This method MUST NEVER be called directly. It is automatically called whenever
     * the visibility of a game element is changed.
     */
    public void onVisibilityChange() {
        boolean visible = element.isVisible();
        for(Node node : group.getChildren()) {
            node.setVisible(visible);
        }
    }

    /**
     * Change the look if the associated game element is selected or unselected.
     * By default, this method does nothing but it can be overridden in subclasses to
     * change the aspect of node when the element is selected.
     */
    public void onSelectionChange() { }

    public Group getGroup() {
        return group;
    }

    public void clearGroup() {
        group.getChildren().clear();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void clearShapes() {
        shapes.clear();
    }
    public void addShape(Shape shape) {
        group.getChildren().add(shape);
        shapes.add(shape);
    }

    public void addNode(Node node) {
        // assuming that node IS NOT a Shape, create a rectangle that matches the bounds
        Bounds b = node.getBoundsInParent();
        Rectangle r = new Rectangle(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
        r.setFill(Color.TRANSPARENT);
        group.getChildren().add(node);
        group.getChildren().add(r);
        shapes.add(r);
    }

    /**
     * Determine if a point is within the bounds of one of the nodes of this look
     * @param point a point in the scene coordinate space.
     * @return <code>true</code> if it is within, otherwise <code>false</code>.
     */
    public boolean isPointWithin(Coord2D point) {
        for(Node node : group.getChildren()) {
            Bounds b = node.localToScene(node.getBoundsInParent());
            if ( (point.getX() >= b.getMinX()) &&  (point.getX() <= b.getMaxX()) && (point.getY() >= b.getMinY()) && (point.getY() <= b.getMaxY()) ) return true;
        }
        return false;
    }

    /**
     * By default, do nothing. If any change in the look must occur, this method must be overridden
     * and its code manipulates Shape/Nodes/... created by render().
     */
    public void onFaceChange() {    }

    /**
     * render() is used to create the visual shape of the element
     * It is normally called only once, when the look is added to the GameStageView
     */
    protected abstract void render();

    /**
     * moveTo() is called automatically when a look is moved within a container look
     * @param x
     * @param y
     */
    public void moveTo(double x, double y) {
        // first change the lcoation of the associated GameElement without creating a location event because
        // moving the look is done just after that.
        element.setLocation(x,y, false);
        // second, move the look group
        Logger.trace("look location of ["+this+"] changed to "+x+","+y);
        group.setTranslateX(x);
        group.setTranslateY(y);
    }

}

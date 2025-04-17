package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.GameElement;

public abstract class ElementLook {

    protected GameElement element;
    protected String[][] shape; // a buffer of String that is used to store the visual aspect of the element
    protected int width; // the width of the view port
    protected int height; // the height of the viewport
    /**
     * the depth to enforce a particular order when painting the looks associated to game elements.
     *
     * By default, all elements are at depth 0 but it can set to a negative value.
     * The behavior is to show the look of elements at depth -1 below those at depth 0, -2 below -1, ...
     * The look of elements at the same depth are painted in the order they are added to the root pane
     */
    protected int depth;
    /**
     * By default, an element look is not owned by a layout. As soon as it is managed by a layout, its rendering
     * on the viewport is managed by the layout.
     */
    protected ElementLook parent;

    public static final int ANCHOR_CENTER = 0;
    public static final int ANCHOR_TOPLEFT = 1;
    /**
     * define the anchor point of the look within its bounding box.
     * By default it is set to the center of the bounding box but it can also
     * be set to the top-left corner of the bounding box.
     * It influences the way the look will be rendered at given x,y coordinates
     * If anchor is center, then the center of the look will be located in x,y.
     * If anchor is top-left, the top-left corner of the look will be located in x,y
     * Some looks are easier to place when their anchor is top-left, e.g. walls.
     */
    protected int anchorType;

    /**
     * A reference to the RootPane is needed in case of a GameElement is removed from a ContainerElement. In such a
     * case, the ElementLook of the GameElement is also removed from the ContainerLook. Thus, it becomes parent-less
     * and must be attached once again to the RootPane group.
     * This attributes is set by the RootPane itself during init() of the game stage.
     */
    private RootPane rootPane;

    public ElementLook(GameElement element, int width, int height, int depth) {
        this.element = element;
        if (width < 0) width = 0;
        if (height < 0) height = 0;
        this.width = width;
        this.height = height;
        shape = new String[height][width];
        clearShape();
        this.depth = depth;
        parent = null;
        anchorType = ANCHOR_CENTER;
        rootPane = null;
    }

    public ElementLook(GameElement element, int width, int height) {
        this(element, width, height, 0);
    }
    public ElementLook(GameElement element) {
        this(element, 0,0, 0);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        if (width < 0) width = 0;
        if (height < 0) height = 0;
        if ((this.width != width) || (this.height != height)) {
            this.width = width;
            this.height = height;
            shape = new String[height][width];
            clearShape();
        }
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
        return parent !=null?true:false;
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

    public GameElement getElement() {
        return element;
    }

    protected void clearShape() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                shape[i][j] = " ";
            }
        }
    }

    protected void printShape() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                System.out.print(shape[i][j]);
            }
            System.out.println();
        }
    }

    public String getShapePoint(int x, int y) {
        if (shape == null) return null;
        if ((x>=0) && (x<width) && (y>=0) && (y<height)) return shape[y][x];
        return null;
    }

    /**
     * By default, if the element moves, there is nothing special to do for its look
     * because it will be placed in the viewport taking the element x,y position int account,
     * or by using a layout.
     */
    public final void onLocationChange() {}

    // clear the shape if the element is not visible.
    // and redraw it using the onLookChange callback
    public final void onVisibilityChange() {
        boolean visible = element.isVisible();
        if (!visible) {
            clearShape();
        }
        else {
            onFaceChange();
        }
    }

    /**
     * by default, do nothing because there is little chance that a text-mode game
     * allows to "select" an element. But in case of, it can be overridden.
     */
    public void onSelectionChange() {}

    /* By default, just "render" the look, i.e. calls render() so that it creates the array
        that contains the visual aspect of the look.

       WARNING: if the look is owned by a Layout, and if its size changes, then the overridden onFaceChange()
       MUST CALL update() of the layout to recompute the layout structure.
     */
    public void onFaceChange() {
        render();
    }

    /**
     * render() is used to create the visual shape of the element. It must be defined
     * for all types of looks. It is called automatically by onLookChange().
     */

    protected abstract void render();

    /**
     * moveTo() is called automatically when a look is moved within a container look
     * @param x
     * @param y
     */
    public void moveTo(double x, double y) {
        // first change the location of the associated GameElement without creating a location event because
        // moving the look is done just after that.
        element.setLocation(x,y, false);
        // second, move the look group
        Logger.trace("look location changed to "+x+","+y, this);
    }
}

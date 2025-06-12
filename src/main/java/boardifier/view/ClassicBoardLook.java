package boardifier.view;

import boardifier.model.ContainerElement;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ClassicBoardLook extends GridLook{
    protected Rectangle[][] cells;
    protected Text[] colCoords;
    protected Text[] rowCoords;
    protected Color oddColor;
    protected Color evenColor;
    protected int frameWidth;
    protected Color frameColor;
    protected boolean showCoords;
    protected int gapXToCells;
    protected int gapYToCells;

    public ClassicBoardLook(int cellSize, ContainerElement element, int depth, Color evenColor, Color oddColor, int borderWidth, Color borderColor, int frameWidth, Color frameColor, boolean showCoords) {
        super(cellSize, cellSize, element, depth, borderWidth, borderColor);
        this.evenColor = evenColor;
        this.oddColor = oddColor;
        this.frameColor = frameColor;
        this.frameWidth = frameWidth;
        this.showCoords = showCoords;
        gapXToCells = frameWidth;
        gapYToCells = frameWidth;
        if (showCoords) {
            Text text = new Text(String.valueOf(nbRows));
            text.setFont(new Font(24));
            Bounds bt = text.getBoundsInLocal();
            gapYToCells += (int) bt.getHeight();
            gapXToCells += (int) bt.getWidth()+10;
        }
        innersTop = gapYToCells;
        innersLeft = gapXToCells;
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    protected void render() {
        super.render();
        // create frame
        Rectangle frame = new Rectangle(colWidth*nbCols+2*frameWidth, rowHeight*nbRows+2*frameWidth, frameColor);
        frame.setX(gapXToCells-frameWidth);
        frame.setY(gapYToCells-frameWidth);
        addShape(frame);
        // create numbering if needed
        if (showCoords) {
            colCoords = new Text[nbCols];
            rowCoords = new Text[nbRows];
            for(int i=0;i<nbRows;i++) {
                rowCoords[i] = new Text(String.valueOf(i+1));
                rowCoords[i].setFont(new Font(24));
                rowCoords[i].setFill(Color.valueOf("0x000000"));
                Bounds bt = rowCoords[i].getBoundsInLocal();
                rowCoords[i].setX((gapXToCells-frameWidth - bt.getWidth())/2);
                rowCoords[i].setY(gapYToCells+ ((i +0.5)*rowHeight) + rowCoords[i].getBaselineOffset()/2 -4);
                addShape(rowCoords[i]);
            }
            for(int j=0;j<nbCols;j++) {
                char c = (char) (j + 'A');
                colCoords[j] = new Text(String.valueOf(c));
                colCoords[j].setFont(new Font(24));
                colCoords[j].setFill(Color.valueOf("0x000000"));
                Bounds bt = colCoords[j].getBoundsInLocal();
                colCoords[j].setX(gapXToCells+ ((j + 0.5)*colWidth) - bt.getWidth()/2);
                colCoords[j].setY(colCoords[j].getBaselineOffset());
                addShape(colCoords[j]);
            }
        }
        cells = new Rectangle[nbRows][nbCols];
        // create top spots
        for (int i=0;i< nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                Color c;
                if ((i+j)%2 == 0) {
                    c = evenColor;
                }
                else {
                    c = oddColor;
                }
                cells[i][j] = new Rectangle(colWidth, rowHeight, c);
                cells[i][j].setSmooth(false);
                cells[i][j].setStroke(c);
                cells[i][j].setStrokeWidth(0);
                cells[i][j].setStrokeMiterLimit(10);
                cells[i][j].setStrokeType(StrokeType.INSIDE);
                cells[i][j].setX(j*colWidth+gapXToCells);
                cells[i][j].setY(i*rowHeight+gapYToCells);
                addShape(cells[i][j]);
            }
        }
    }

    /* override all methods that lead to change the inners location
       because it also implies an update of all the elements
     */

    @Override
    public void setInnersTop(int innersTop) {
        // prevent to change innersTop
    }

    @Override
    public void setInnersLeft(int innersLeft) {
        // prevent to change innersLeft
    }

    @Override
    public void setInnersTopLeft(int top, int left) {
        // prevent to change innersTop & Left
    }

    @Override
    public void setRowHeight(int rowHeight) {
        setCellSize(rowHeight);
    }

    @Override
    public void setColWidth(int colWidth) {
        setCellSize(colWidth);
    }

    public void setCellSize(int cellSize) {
        if (this.rowHeight != cellSize) {
            this.rowHeight = cellSize;
            this.colWidth = cellSize;
            for (int i = 0; i < nbRows; i++) rowsHeight[i] = cellSize;
            for (int i = 0; i < nbCols; i++) colsWidth[i] = cellSize;
            updateInners();
            updateBorders();
            updateRowCoords();
            updateColCoords();
            updateBackGround();
        }
    }

    protected void updateRowCoords() {
        /* if row coordinates have not been already created, do nothing
          This case occurs when inheriting from this class and the constructor calls
          a method that leads to a call to updateRowCoords() while render() has not been yet called.
          For example, setting paddings is in this case.
         */
        if (rowCoords == null) return;
        // create numbering if needed
        if (showCoords) {
            for (int i = 1; i <= nbRows; i++) {
                rowCoords[i].setY(gapYToCells + ((i - 1 + 0.5) * rowHeight) + rowCoords[i].getBaselineOffset() / 2 - 4);
            }
        }
    }

    protected void updateColCoords() {
        /* if col coordinates have not been already created, do nothing
          This case occurs when inheriting from this class and the constructor calls
          a method that leads to a call to updateColCoords() while render() has not been yet called.
          For example, setting paddings is in this case.
         */
        if (colCoords == null) return;
        // create numbering if needed
        if (showCoords) {
            for(int j=0;j<nbCols;j++) {
                Bounds bt = colCoords[j].getBoundsInLocal();
                colCoords[j].setX(gapXToCells+ ((j + 0.5)*colWidth) - bt.getWidth()/2);
            }
        }
    }

    protected void updateBackGround() {
        /* if cells have not been already created, do nothing
          This case occurs when inheriting from this class and the constructor calls
          a method that leads to a call to updateBackground() while render() has not been yet called.
          For example, setting paddings is in this case.
         */
        if (cells == null) return;

        for (int i=0;i< nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                cells[i][j].setWidth(colWidth);
                cells[i][j].setHeight(rowHeight);
                cells[i][j].setX(j*colWidth+gapXToCells);
                cells[i][j].setY(i*rowHeight+gapYToCells);
            }
        }
    }

    @Override
    public void onFaceChange() {
        ContainerElement board = (ContainerElement)element;
        boolean[][] reach = board.getReachableCells();
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (reach[i][j]) {
                    cells[i][j].setStrokeWidth(3);
                    cells[i][j].setStrokeMiterLimit(10);
                    cells[i][j].setStrokeType(StrokeType.CENTERED);
                    cells[i][j].setStroke(Color.valueOf("0x333333"));
                } else {
                    cells[i][j].setStrokeWidth(0);
                }
            }
        }
    }
}

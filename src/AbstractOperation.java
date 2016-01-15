
import java.awt.image.BufferedImage;

/**
 * 
 * 
 */
public abstract class AbstractOperation implements MorphologicalOperation {

        protected int shapeSize;

        /**
         * Constructs a structuring element shape. The values in the structuring
         * element array are either set to 1 or 0, 1 if that position in the
         * structuring element array is a part of the structuring element shape
         * 
         * @param shape
         *            The shape of the structuring element, see @
         *            MorphologicalOperation.STRUCTURING_ELEMENT_SHAPE} for
         *            available shapes
         * @param shapeSize
         *            The size from the middle of the shape to exterior of the
         *            shape. (Total size = 2*shapeSize+1)
         * @return The constructed structuring element
         * 
         * Abstract morphological base class.
 * 
 * @author Tomas Toss
         * 
         * 
         */
        protected short[][] constructShape(STRUCTURING_ELEMENT_SHAPE shape,
                        int shapeSize) {

                int size = 2 * shapeSize + 1;
                short[][] structElem = new short[size][size];
                switch (shape) {
                case SQUARE:

                        for (int i = 0; i < size; i++) {
                                for (int j = 0; j < size; j++) {
                                        structElem[i][j] = 1;
                                }
                        }
                        break;
                case VERTICAL_LINE:
                        for (int i = 0; i < size; i++) {
                                structElem[i][shapeSize] = 1;
                        }
                        break;
                case HORIZONTAL_LINE:
                        for (int i = 0; i < size; i++) {
                                structElem[shapeSize][i] = 1;
                        }
                        break;
                default:
                        for (int i = 0; i < size; i++) {
                                for (int j = 0; j < size; j++) {
                                        structElem[i][j] = 1;
                                }
                        }
                }
                return structElem;
        }

        /**
         * Execute the morphological operation on the supplied image
         * 
         * @param img
         *            The image to operate on
         * @return The morphological adjusted image
         */
        public abstract BufferedImage execute(BufferedImage img);
        
        public int getShapeSize() {
                return shapeSize;
        }
}



import java.awt.image.BufferedImage;

/**
 * Interface for Morphological operations. A morphological operation should be
 * able to be applied to a {@link BufferedImage} via the
 * {@link MorphologicalOperation #execute(BufferedImage)}.
 * 
 * @author Tomas Toss 16 maj 2011
 * 
 */
public interface MorphologicalOperation {
        /**
         * The different shapes of a structuring element
         */
        public enum STRUCTURING_ELEMENT_SHAPE {
                SQUARE, VERTICAL_LINE, HORIZONTAL_LINE, CIRCLE
        }

        public BufferedImage execute(BufferedImage img);

        public int getShapeSize();
}
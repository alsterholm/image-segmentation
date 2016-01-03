package interfaces;

import entities.Image;

/**
 * Basic interface used for filters.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public interface Filter {
    /**
     * Apply the filter to an image.
     * @param image Image to apply filter to.
     * @return New image with applied filter.
     */
    Image apply(Image image);

    /**
     * Get the filter’s suffix used in image filenames.
     * @return Filename suffix
     */
    String getSuffix();
}

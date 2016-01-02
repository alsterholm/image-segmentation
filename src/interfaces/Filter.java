package interfaces;

import entities.Image;

/**
 * Created by andreas on 2016-01-02.
 */
public interface Filter {
    Image apply(Image image);
    String getSuffix();
}

package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class IconTransformer extends ImageIcon
{

    private static Image image;

    public IconTransformer(ImageIcon icon)
    {
        image = icon.getImage();
    }

    public void scaleIcon(int width, int height)
    {
        image = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    }

    public void setTransparentBackground()
    {
        ImageFilter filter = new RGBImageFilter()
        {
            final int transparentColor = Color.white.getRGB() | 0xFF000000;
            @Override
            public final int filterRGB(int x, int y, int rgb)
            {
                if ((rgb | 0xFF000000) == transparentColor)
                {
                    return 0x00FFFFFF & rgb;
                } else
                {
                    return rgb;
                }
            }
        };
        ImageProducer filteredImgProd = new FilteredImageSource(image.getSource(), filter);
        image = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
    }

    public ImageIcon transformToIcon()
    {
        return new ImageIcon(image);
    }
}

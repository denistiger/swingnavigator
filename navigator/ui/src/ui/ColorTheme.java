package ui;


import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 * This class describes a theme using "primary" colors.
 * You can change the colors to anything else you want.
 *
 * 1.9 07/26/04
 */
public class ColorTheme extends DefaultMetalTheme {

    public String getName() { return "SwingNavigatorTheme"; }

    private final ColorUIResource primary1 = new ColorUIResource(255, 255, 0);
    private final ColorUIResource primary2 = new ColorUIResource(0, 255, 255);
    private final ColorUIResource primary3 = new ColorUIResource(255, 0, 255);

    private final ColorUIResource secondary1 = new ColorUIResource(0, 255, 0);
    private final ColorUIResource secondary2 = new ColorUIResource(27, 56, 54);
    private final ColorUIResource secondary3 = new ColorUIResource(150, 100, 44);

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

}

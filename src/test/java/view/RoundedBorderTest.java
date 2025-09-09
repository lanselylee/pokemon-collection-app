package view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.Graphics2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoundedBorderTest {

    private RoundedBorder border;
    private final int radius = 10;
    private final Color color = Color.RED;
    private Component mockComponent;
    private Graphics mockGraphics;
    private Graphics2D mockGraphics2D;

    @BeforeEach
    void setUp() {
        border = new RoundedBorder(radius, color);
        mockComponent = mock(Component.class);
        mockGraphics = mock(Graphics.class);
        mockGraphics2D = mock(Graphics2D.class);

        // Set up the mock Graphics to return our mock Graphics2D
        when(mockGraphics.create()).thenReturn(mockGraphics2D);
    }

    @Test
    void testConstructor() {
        // Test that constructor sets values correctly
        RoundedBorder testBorder = new RoundedBorder(15, Color.BLUE);

        // We can't directly test private fields, but we can test behavior
        assertEquals(new Insets(15, 15, 15, 15), testBorder.getBorderInsets(mockComponent),
                "Border insets should match radius");
    }

    @Test
    void testPaintBorder() {
        // Call the method
        border.paintBorder(mockComponent, mockGraphics, 0, 0, 100, 50);

        // Verify that the correct methods were called
        verify(mockGraphics).create();
        verify(mockGraphics2D).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        verify(mockGraphics2D).setColor(color);
        verify(mockGraphics2D).drawRoundRect(0, 0, 99, 49, radius, radius);
        verify(mockGraphics2D).dispose();
    }

    @Test
    void testGetBorderInsets() {
        Insets insets = border.getBorderInsets(mockComponent);
        assertEquals(new Insets(radius, radius, radius, radius), insets,
                "Border insets should match radius");
    }

    @Test
    void testIsBorderOpaque() {
        assertFalse(border.isBorderOpaque(),
                "Rounded border should not be opaque");
    }

    @Test
    void testPaintBorderWithDifferentParameters() {
        // Test with different parameters
        int x = 5;
        int y = 10;
        int width = 200;
        int height = 100;

        border.paintBorder(mockComponent, mockGraphics, x, y, width, height);

        // Verify the correct drawing was done
        verify(mockGraphics2D).drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Test
    void testBorderWithZeroRadius() {
        // Create a border with zero radius
        RoundedBorder zeroBorder = new RoundedBorder(0, color);

        // Get insets
        Insets insets = zeroBorder.getBorderInsets(mockComponent);

        // Should still return insets with the radius value
        assertEquals(new Insets(0, 0, 0, 0), insets,
                "Zero radius should give zero insets");
    }

    @Test
    void testBorderWithNegativeRadius() {
        // Create a border with negative radius (edge case)
        RoundedBorder negativeBorder = new RoundedBorder(-5, color);

        // Get insets
        Insets insets = negativeBorder.getBorderInsets(mockComponent);

        // Should still return insets with the absolute value of radius
        assertEquals(new Insets(-5, -5, -5, -5), insets,
                "Negative radius should be preserved in insets");

        // Test painting with negative radius
        negativeBorder.paintBorder(mockComponent, mockGraphics, 0, 0, 100, 50);

        // Verify the drawing still happens with the negative radius
        verify(mockGraphics2D).drawRoundRect(0, 0, 99, 49, -5, -5);
    }

    @Test
    void testBorderWithNullColor() {
        // Create a border with null color (edge case)
        RoundedBorder nullColorBorder = new RoundedBorder(radius, null);

        // Paint border
        nullColorBorder.paintBorder(mockComponent, mockGraphics, 0, 0, 100, 50);

        // Verify setColor was called with null (which should be handled by Graphics2D)
        verify(mockGraphics2D).setColor(null);
    }
}

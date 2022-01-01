package com.view.graphic.dynamic;

import com.model.card.RumourCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The type Card 2D Display.
 * 
 * Gives all the methods related to card 2D display.
 */
public class Card2DDisplay extends JComponent {
    
	/**
     * The Zoom Panel
     * 
     * @see com.view.graphic.dynamic.ZoomPanel
     */
	private final ZoomPanel zoomPanel;

	/**
	 * Instantiates a new Card2DDisplay.
	 * 
	 * @param x           the abscissa value
	 * @param y           the ordinate value
	 * @param width       the width
	 * @param height      the height
	 * @param rumourCard  the rumour card
	 * @see com.view.graphic.dynamic.ZoomPanel
	 */
    Card2DDisplay(int x, int y, int width, int height, RumourCard rumourCard) {
        this.zoomPanel = new ZoomPanel(rumourCard);
        this.setBounds(x, y, width, height);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                zoomOnCard();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Zoom on card
     */
    private void zoomOnCard() {
        JFrame jFrame = new JFrame();
        jFrame.setContentPane(zoomPanel);
        jFrame.setTitle("Zoom on Rumour Card");
        float ratio = (float) 4 / 7;
        jFrame.setSize((int) (600 * ratio), 600);
        jFrame.setResizable(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        jFrame.setVisible(true);
        zoomPanel.repaint();
    }
}

package com.view;

import com.view.graphic.GraphicalTextView;
import com.view.graphic.dynamic.Graphical2DView;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Window used for the initial view choice.
 * When called using the static run method, a window will be displayed asking the view(s) that the user want.
 * Upon validation using the OK button, the view(s) will be created and send back to the GameController.
 * As a safety, if no views are selected, the OK button can't be pressed
 *
 * @see com.controller.GameController
 */
@SuppressWarnings("unused")
public class InitialViewChoice extends JDialog {
	
	/**
	 * The Content pane.
	 */
	private JPanel contentPane;
	
	/**
	 * The OK Button used to finalize user choices.
	 */
	private JButton buttonOK;
	
	/**
	 * The cancel Button used to stop selection and terminate the program.
	 */
	private JButton buttonCancel;
	
	/**
	 * The View label.
	 */
	private JLabel viewLabel;
	
	/**
	 * The Confirmation panel.
	 */
	private JPanel confirmationPanel;
	
	/**
	 * The Console check box.
	 */
	private JCheckBox consoleCheckBox;
	
	/**
	 * The Graphical 2D spinner.
	 */
	private JSpinner graphical2dSpinner;
	
	/**
	 * The Graphical 2D label.
	 */
	private JLabel graphical2dLabel;
	
	/**
	 * The Graphical text label.
	 */
	private JLabel graphicalTextLabel;
	
	/**
	 * The Graphical text spinner.
	 */
	private JSpinner graphicalTextSpinner;

	/**
	 * The constant activeView send back using the run() method.
	 */
	private static ActiveView activeView;

	/**
	 * Instantiates a new Initial view choice.
	 */
	private InitialViewChoice() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		setTitle("View choosing window");

		graphical2dSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		graphicalTextSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		//While we don't have at least 1 View, disable OK button
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				buttonOK.setEnabled(consoleCheckBox.isSelected() || (int) graphical2dSpinner.getValue() > 0 || (int) graphicalTextSpinner.getValue() > 0);
			}
		});

		pack();
		setVisible(true);
	}

	/**
	 * Run the choice window
	 *
	 * @return the chosen active view
	 */
	public static ActiveView run() {
		new InitialViewChoice();
		return activeView;
	}

	/**
	 * On press of the OK button.
	 * 
	 * @see com.view.ActiveView
	 * @see com.view.PassiveView
	 * @see com.view.CommandLineView
	 * @see com.view.graphic.dynamic.Graphical2DView
	 * @see com.view.graphic.GraphicalTextView
	 */
	private void onOK() {
		List<ActiveView> views = new ArrayList<>();

		if (consoleCheckBox.isSelected()) views.add(new CommandLineView());

		for (int i = 0; i < (int) graphical2dSpinner.getValue(); i++) {
			views.add(new Graphical2DView());
		}

		for (int i = 0; i < (int) graphicalTextSpinner.getValue(); i++) {
			views.add(new GraphicalTextView());
		}

		activeView = views.get(0);
		if (views.size() > 1) {
			Views viewList = new Views(views.get(0));
			views.forEach(v -> {
				if (v instanceof PassiveView passiveView) viewList.addView(passiveView);
			});
			activeView = viewList;
		}

		dispose();
	}

	/**
	 * On press of the cancel button.
	 */
	private void onCancel() {
		dispose();
		System.exit(0);
	}
}

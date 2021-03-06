package com.view;

import com.controller.GameController;
import com.view.graphic.GraphicalTextView;
import com.view.graphic.dynamic.Graphical2DView;
import com.view.server.ClientSideView;
import com.view.server.ServerSideView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Window used for the initial view choice.
 * When called using the static run method, a window will be displayed asking the view(s) that the user want.
 * Upon validation using the OK button, the view(s) will be created and send back to the GameController.
 * As a safety, if no views are selected, the OK button can't be pressed
 *
 * @see GameController
 */
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
	 * The Client button.
	 */

	private JRadioButton clientButton;
	/**
	 * The Server button.
	 */

	private JRadioButton serverButton;
	/**
	 * The Off button.
	 */

	private JRadioButton offButton;
	/**
	 * The Network label.
	 */

	private JLabel networkLabel;
	/**
	 * The View label.
	 */

	private JLabel viewLabel;
	/**
	 * The Server panel.
	 */

	private JPanel serverPanel;
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
	 * Run the choice window.
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

		ActiveView view = views.get(0);
		if (views.size() > 1) {
			view = new Views(views.get(0));
			for (int i = 1; i < views.size(); i++) {
				if (views.get(i) instanceof PassiveView passiveView) {
					((Views) view).addView(passiveView);
				}
			}
		}

		if (offButton.isSelected()) {
			activeView = view;
		} else if (serverButton.isSelected()) {
			activeView = new ServerSideView(view);
		} else if (clientButton.isSelected()) {
			activeView = new ClientSideView((PassiveView) view);
		}

		dispose();
	}

	/**
	 * On press of the cancel button.
	 */
	private void onCancel() {
		// add your code here if necessary
		dispose();
		System.exit(0);
	}

	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 */
	private void $$$setupUI$$$() {
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		contentPane.setEnabled(false);
		confirmationPanel = new JPanel();
		confirmationPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(confirmationPanel, gbc);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		confirmationPanel.add(panel1, gbc);
		buttonOK = new JButton();
		buttonOK.setText("OK");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(buttonOK, gbc);
		buttonCancel = new JButton();
		buttonCancel.setText("Cancel");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(buttonCancel, gbc);
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(serverPanel, gbc);
		clientButton = new JRadioButton();
		clientButton.setText("Local Client");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		serverPanel.add(clientButton, gbc);
		serverButton = new JRadioButton();
		serverButton.setText("Local Server");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		serverPanel.add(serverButton, gbc);
		offButton = new JRadioButton();
		offButton.setSelected(true);
		offButton.setText("Off");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		serverPanel.add(offButton, gbc);
		viewLabel = new JLabel();
		viewLabel.setEnabled(true);
		viewLabel.setText("Choose the view(s) you want");
		viewLabel.setVerticalAlignment(0);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(viewLabel, gbc);
		consoleCheckBox = new JCheckBox();
		consoleCheckBox.setEnabled(true);
		consoleCheckBox.setSelected(true);
		consoleCheckBox.setText("Console");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(consoleCheckBox, gbc);
		graphical2dSpinner = new JSpinner();
		graphical2dSpinner.setToolTipText("");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(graphical2dSpinner, gbc);
		graphical2dLabel = new JLabel();
		graphical2dLabel.setText("2D Graphical");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(graphical2dLabel, gbc);
		graphicalTextLabel = new JLabel();
		graphicalTextLabel.setText("Graphical Text");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(graphicalTextLabel, gbc);
		graphicalTextSpinner = new JSpinner();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(graphicalTextSpinner, gbc);
		networkLabel = new JLabel();
		networkLabel.setText("Choose network option");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(networkLabel, gbc);
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(clientButton);
		buttonGroup.add(serverButton);
		buttonGroup.add(offButton);
	}

	/**
	 * Get root JComponent.
	 *
	 * @return the JComponent
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}

}

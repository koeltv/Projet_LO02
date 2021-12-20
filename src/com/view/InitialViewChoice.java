package com.view;

import com.view.graphic.dynamic.Graphical2DView;
import com.view.server.ClientSideView;
import com.view.server.ServerSideView;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class InitialViewChoice extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JRadioButton clientButton;
	private JRadioButton serverButton;
	private JRadioButton offButton;
	private JLabel networkLabel;
	private JLabel viewLabel;
	private JPanel serverPanel;
	private JPanel confirmationPanel;
	private JCheckBox consoleCheckBox;
	private JSpinner graphical2dSpinner;
	private JLabel graphical2dLabel;
	private JLabel graphicalTextLabel;
	private JSpinner graphicalTextSpinner;

	private static ActiveView activeView;

	public InitialViewChoice() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

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

	public static ActiveView run() {
		InitialViewChoice initialViewChoice = new InitialViewChoice();
		return activeView;
	}

	private void onOK() {
		List<View> views = new ArrayList<>();

		if (consoleCheckBox.isSelected()) views.add(new CommandLineView());

		for (int i = 0; i < (int) graphical2dSpinner.getValue(); i++) {
			views.add(new Graphical2DView());
		}

		for (int i = 0; i < (int) graphicalTextSpinner.getValue(); i++) {
			views.add(new Graphical2DView());
		}

		Views viewList = null;
		if (views.size() > 1) {
			viewList = new Views((ActiveView) views.get(0));
			for (int i = 1; i < views.size(); i++) {
				viewList.addView((PassiveView) views.get(i));
			}
		}

		if (offButton.isSelected()) {
			activeView = viewList != null ? viewList : (ActiveView) views.get(0);
		} else if (serverButton.isSelected()) {
			activeView = new ServerSideView(viewList != null ? viewList : (ActiveView) views.get(0));
		} else if (clientButton.isSelected()) {
			activeView = new ClientSideView(viewList != null ? viewList : (PassiveView) views.get(0));
		}

		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
		System.exit(0);
	}
}

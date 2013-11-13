package com.t3.client.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.t3.common.models.Bundle;
import com.t3.common.utils.ConfigUltils;


/**
 * @author PV
 */
public class ConfigureDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WIDTH = 260;
	public static final int DEFAULT_HEIGHT = 215;

	private JComboBox<String> jcLanguage;
	private JTextField tfPort, tfHost;
	private JCheckBox jcbSave;
	private JButton btOK, btCancel;
	private DefaultComboBoxModel<String> mode;
	
	public ConfigureDialog(){
		this(null, true);
		initialize();

	}
	
	public ConfigureDialog(Frame frame, boolean modal) {
		super(frame, modal);
		initialize();
	}
	
	/**
	 * 
	 */
	private void initialize() {

		Box bBorder=Box.createVerticalBox();
		bBorder.setBorder(new TitledBorder("Configure"));
		
		JPanel pLaguage = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		JPanel pPort=new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		JPanel pHost=new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		JPanel pSave=new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		JPanel pButton=new JPanel(new FlowLayout(FlowLayout.CENTER)); 

		mode=new DefaultComboBoxModel<>();
		mode.addElement("English");
		mode.addElement("Tiáº¿ng Viá»‡t");

		jcLanguage=new JComboBox<>(mode);

		JLabel lbPort=new JLabel("Port:");
		JLabel lbHost=new JLabel("Host:");
		JLabel lblanguage=new JLabel("Language: ");
		
		lbHost.setPreferredSize(lblanguage.getPreferredSize());
		lbPort.setPreferredSize(lblanguage.getPreferredSize());
		

		jcbSave=new JCheckBox("Save Client Password");
		btOK=new JButton("OK");
		btCancel=new JButton("Cancel");
		
		try {
			Bundle bundle = ConfigUltils.getClientConfigure();
			jcLanguage.setSelectedIndex(bundle.getInt("lang"));
			tfPort=new JTextField(bundle.getInt("port") + "", 15);
			tfHost=new JTextField(bundle.getString("server"), 15);
			jcbSave.setSelected(bundle.getBoolean("save"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		btOK.setPreferredSize(btCancel.getPreferredSize());

		pLaguage.add(lblanguage);
		pLaguage.add(jcLanguage);

		pPort.add(lbPort); pPort.add(tfPort);
		pHost.add(lbHost); pHost.add(tfHost);

		pSave.add(jcbSave);

		pButton.add(btOK);
		pButton.add(btCancel);

		bBorder.add(pLaguage);
		bBorder.add(pPort);
		bBorder.add(pHost);
		bBorder.add(pSave);


		this.add(bBorder, BorderLayout.NORTH);
		this.add(pButton, BorderLayout.SOUTH);
		this.setTitle("Configure");
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		btCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		
		btOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
	}
	
	private void onOK() {
		try {
			String server = tfHost.getText().toString().trim();
			int port = Integer.parseInt(tfPort.getText().toString().trim());
			Bundle bundle = new Bundle();
			bundle.putInt("lang", jcLanguage.getSelectedIndex());
			bundle.putString("server", server);
			bundle.putInt("port", port);
			bundle.putBoolean("save", jcbSave.isSelected());
			
			Bundle bundle2 = ConfigUltils.getClientConfigure();
			ConfigUltils.updateClientConfigure(bundle2.getString("server"), bundle);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		this.setVisible(false);
	}
	
	private void onCancel() {
		this.setVisible(false);
	}
}

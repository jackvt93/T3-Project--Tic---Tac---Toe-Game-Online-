package com.t3.client.models;

import java.awt.event.ActionListener;

/**
 * Interface using for listen event click of cell
 * @author Luan Vu
 *
 */
public interface CellActionListener extends ActionListener {
	public void onCellClick(int row, int column);
}

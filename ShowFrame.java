package ÄÚ´æ;

import java.awt.Container;

import javax.swing.JFrame;

public class ShowFrame extends JFrame {
	private final int LENGTH=300;
	private final int WIDTH=150;
	public ShowFrame(){
		this.setSize(LENGTH,WIDTH);
		this.setTitle("MEMORYSimulation");
		Container content = this.getContentPane();
		content.add(new ContentPane());
		this.setVisible(true);
		
	}
}

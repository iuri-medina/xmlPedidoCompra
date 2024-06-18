package xmlPedidoCompra;

import xmlPedidoCompra.GUI.XmlPedidoGUI;
import javax.swing.*;  

public class App {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	XmlPedidoGUI gui = new XmlPedidoGUI();
                gui.setVisible(true);
                gui.setResizable(false);
                
            }
        });
        
		
		
	}

}

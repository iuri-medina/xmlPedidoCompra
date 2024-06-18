package xmlPedidoCompra.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import xmlPedidoCompra.Util.Conexao;

public class Loja {
	private int id;
    private String description;

    public Loja(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    


    @Override
    public String toString() {
        return description; // Display store description in the JComboBox
    }
}

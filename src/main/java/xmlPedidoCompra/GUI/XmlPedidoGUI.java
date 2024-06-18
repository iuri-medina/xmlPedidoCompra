package xmlPedidoCompra.GUI;

import xmlPedidoCompra.Model.Pedido;
import xmlPedidoCompra.Util.*;
import xmlPedidoCompra.Model.Loja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlPedidoGUI extends JFrame implements ActionListener {

	private JButton selectXMLButton;
    private JButton importOrderButton;
    private JTextField selectedXMLPathField;
    private JComboBox<Loja> comboBoxLoja;

    public XmlPedidoGUI() {
        super("Importar Pedido XML");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        setSize(new Dimension(600, 200)); // Largura: 500 pixels, Altura: 200 pixels

        // Botão para selecionar arquivo XML
        selectXMLButton = new JButton("Selecionar XML");
        selectXMLButton.addActionListener(this);
        add(selectXMLButton);

        // Campo de texto para mostrar o caminho do arquivo XML selecionado
        selectedXMLPathField = new JTextField(30);
        selectedXMLPathField.setEditable(false);
        add(selectedXMLPathField);

        // Criar e popular o JComboBox com objetos Store
        comboBoxLoja = new JComboBox<>();
        List<Loja> lojas = preencheListaLoja();
        for (Loja loja : lojas) {
            comboBoxLoja.addItem(loja);
        }
        add(comboBoxLoja);

        // Botão para importar o pedido
        importOrderButton = new JButton("Importar Pedido");
        importOrderButton.addActionListener(this);
        importOrderButton.setEnabled(false); // Desabilitado inicialmente
        add(importOrderButton);
    }

    private List<Loja> preencheListaLoja() {
        List<Loja> lojas = new ArrayList<>();

        Conexao conectaBanco = new Conexao();
        conectaBanco.getProperties();

        final String SQL_BUSCAR_LOJAS = "SELECT ID, DESCRICAO FROM LOJA ORDER BY ID;";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL_BUSCAR_LOJAS)) {

            ResultSet rsLoja = conexao.createStatement()
                    .executeQuery(SQL_BUSCAR_LOJAS);

            while (rsLoja.next()) {
                int idLoja = rsLoja.getInt("ID");
                String descricaoLoja = rsLoja.getString("DESCRICAO");
                lojas.add(new Loja(idLoja, descricaoLoja));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lojas;
    }
    
    private void importOrderFromXML(String xmlFilePath) {
        try {
            // Cria JAXB context para classe Pedido
            JAXBContext jaxbContext = JAXBContext.newInstance(Pedido.class);

            // Cria uma instancia de Unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Unmarshall XML file into Pedido object
            Pedido pedido = (Pedido) unmarshaller.unmarshal(new File(xmlFilePath));

            // Get selected Store object from comboBox
            Loja lojaSelecionada = (Loja) comboBoxLoja.getSelectedItem();

            // Extract store ID from selected Store object
            int idLoja = lojaSelecionada.getId();
            
            System.out.println(idLoja);
            
            
            
            System.out.println("Pedido importado com sucesso: " + pedido);
            // ... (Your processing or storage logic here) ...
        } catch (JAXBException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao importar pedido: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectXMLButton) {
            // Abrir o seletor de arquivos
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos XML", "xml");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedXMLPathField.setText(selectedFile.getAbsolutePath());
                importOrderButton.setEnabled(true); // Habilitar botão de importação
            }
        } else if (e.getSource() == importOrderButton) {
        	String xmlFilePath = selectedXMLPathField.getText();
            if (!xmlFilePath.isEmpty()) {
                importOrderFromXML(xmlFilePath);     
                
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um arquivo XML primeiro.");
            }
            
            
        }
    }

}

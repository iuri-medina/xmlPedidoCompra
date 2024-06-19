package xmlPedidoCompra.GUI;

import xmlPedidoCompra.Model.Pedido;
import xmlPedidoCompra.Model.Pedido.Produto;
import xmlPedidoCompra.Model.ResultadoVerificaProduto;
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
        setLayout(new FlowLayout(FlowLayout.CENTER));

        setSize(new Dimension(600, 200)); // Largura: 600 pixels, Altura: 200 pixels

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
        importOrderButton.setPreferredSize(new Dimension(150, 30));
        //importOrderButton.setBackground(Color.BLACK);
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

            // Pega o objeto Loja selecionado no combobox
            Loja lojaSelecionada = (Loja) comboBoxLoja.getSelectedItem();

            // Extrai id da loja do combobox
            int idLoja = lojaSelecionada.getId();
    
            String cnpj = pedido.getCnpj();
            
            List<Produto> produtos = pedido.getProdutos();
            
            //Verificar se cnpj do xml esta cadastrado no sistema	
            if(verificaCnpj(cnpj)) {
            	
            	for (Produto produto : produtos) {
        			//verifica se o produto esta cadastrado no sistema e retorna o objeto com os dados
        			ResultadoVerificaProduto resultado = verificaProduto(produto);
        			
        			//caso nao esteja, gera erro
        			if (!resultado.isProdutoEncontrado()) {
        				JOptionPane.showMessageDialog(null, "Erro: Codigo de barras " + produto.getCodigoEan() + " não cadastrado no sistema!");

        			}
        			
        			else {
        				//insert pedido
                    	
                    	//consultar o id do pedido para inserir itens
                    	
                    	System.out.println("Pedido cadastrado com sucesso! ");
                    	System.out.println(pedido);
        			}
            	
            	
            	}
            }
            else {
                JOptionPane.showMessageDialog(null, "CNPJ não cadastrado no sistema: " + cnpj + "\nRealize o cadastro no sistema: Cadastro -> Operarional -> Fornecedor");
            }
            

        } catch (JAXBException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao importar pedido: " + e.getMessage());
        }
      }
    
    
    int idFornecedor;

    public int getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(int idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	
	
	private boolean verificaCnpj(String cnpj) {
    	
    	 
    	  Conexao conectaBanco = new Conexao();
    	  conectaBanco.getProperties();
    	  
    	  

    	  final String SQL_VERIFICAR_CNPJ = "SELECT ID, COUNT(*) AS COUNT FROM FORNECEDOR WHERE CNPJ = " + cnpj + "GROUP BY ID;";

    	  try (Connection conexao = Conexao.conectar();
    	       PreparedStatement preparedStatement = conexao.prepareStatement(SQL_VERIFICAR_CNPJ)) {

    	   

    	    ResultSet rs = preparedStatement.executeQuery();
    	    

    	    if (rs.next()) {
    	      int count = rs.getInt("COUNT");
    	      setIdFornecedor(rs.getInt("ID"));
    	      return count > 0; // retorna true se alguma linha for encontrada
    	    }

    	  } catch (SQLException e) {
    	    e.printStackTrace();
    	  }

    	  return false; // retorna false por padrao, caso ocorra erros
    	}
	
	
	public void inserePedido() {
		
	}
	
	public ResultadoVerificaProduto verificaProduto(Produto produto) {
		  Conexao conectaBanco = new Conexao();
	  	  conectaBanco.getProperties();
		  final String SQL_VERIFICAR_PRODUTO = "SELECT ID_PRODUTO, COUNT(*) AS COUNT FROM PRODUTOAUTOMACAO WHERE CODIGOBARRAS = " + produto.getCodigoEan() + "GROUP BY ID_PRODUTO";

	  	  try (Connection conexao = Conexao.conectar();
	  	    PreparedStatement preparedStatement = conexao.prepareStatement(SQL_VERIFICAR_PRODUTO)) {
	  	    ResultSet rs = preparedStatement.executeQuery();
	  	    

	  	    if (rs.next()) {
	  	      int count = rs.getInt("COUNT");
	  	      if(count > 0) {
	  	    	  String idProduto = rs.getString("ID_PRODUTO");
	  	    	  return new ResultadoVerificaProduto(true, idProduto);
	  	    	  }
	  	      
	  	    }

	  	  } catch (SQLException e) {
	  	    e.printStackTrace();
	  	  }

	    	  return new ResultadoVerificaProduto(false, null); // retorna false por padrao, caso ocorra erros
	  	}

    
	public void inserePedidoItem(List<Produto> produtos) {
		Conexao conectaBanco = new Conexao();
  	  	conectaBanco.getProperties();

		
		for (Produto produto : produtos) {
			//verifica se o produto esta cadastrado no sistema e retorna o objeto com os dados
			ResultadoVerificaProduto resultado = verificaProduto(produto);
			
			//caso nao esteja, gera erro
			if (!resultado.isProdutoEncontrado()) {
				JOptionPane.showMessageDialog(null, "Erro: Codigo de barras " + produto.getCodigoEan() + " não cadastrado no sistema!");

			}
			//armazena o id do produto na variavel
		    String idProduto = resultado.getIdProduto();
		    
		    //se o id do produto for obtido, realiza o insert do pedido
		    if(idProduto != null) {
		    	try (Connection conexao = Conexao.conectar();
			    	    PreparedStatement preparedStatement = conexao.prepareStatement(SQL_INSERT_PRODUTO)) {
			    	    ResultSet rs = preparedStatement.executeQuery();

			    	  } catch (SQLException e) {
			    	    e.printStackTrace();
			    	  }
				
			}
		    
		    else {
		    	System.out.println("Erro: Falha ao obter ID do produto: " + produto.getCodigoEan());
				JOptionPane.showMessageDialog(null, "Erro: Falha ao obter ID do produto de codigo de barras: " + produto.getCodigoEan());

		    }
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

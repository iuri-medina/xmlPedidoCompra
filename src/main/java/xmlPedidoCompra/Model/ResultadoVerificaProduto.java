package xmlPedidoCompra.Model;

public class ResultadoVerificaProduto {
	  private boolean produtoEncontrado;
	  private String idProduto;

	  public ResultadoVerificaProduto(boolean produtoEncontrado, String idProduto) {
	    this.produtoEncontrado = produtoEncontrado;
	    this.idProduto = idProduto;
	  }

	  public boolean isProdutoEncontrado() {
	    return produtoEncontrado;
	  }

	  public String getIdProduto() {
	    return idProduto;
	  }
	  
	  
	  
		
	  
}


package xmlPedidoCompra.Model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ped")
public class Pedido {

  @XmlElement(name = "CNPJ")
  private String cnpj;
  
  @XmlElement(name = "prod")
  private List<Produto> produtos;

  /*@XmlElement(name = "xNome")
  private String nomeEmitente;

  @XmlElement(name = "cPed")
  private String codigoPedido;

  @XmlElement(name = "dEmi")
  private String dataEmissao;

  @XmlElement(name = "cPgto")
  private String codigoPagamento;

  @XmlElement(name = "xObs")
  private String observacao;

  @XmlElement(name = "xAgr")
  private String indicadorAgrega;*/


public String getCnpj() {
	return cnpj;
	}  
  
public List<Produto> getProdutos() {
	return produtos;
	}
/*public String getNomeEmitente() {
	return nomeEmitente;
}

public String getCodigoPedido() {
	return codigoPedido;
}

public String getDataEmissao() {
	return dataEmissao;
}

public String getCodigoPagamento() {
	return codigoPagamento;
}

public String getObservacao() {
	return observacao;
}

public String getIndicadorAgrega() {
	return indicadorAgrega;
}*/

public static class Produto {
	
  @XmlElement(name = "cEAN")
  private String codigoEan;
  
  @XmlElement(name = "qPed")
  private String quantidadePedida;

  @XmlElement(name = "vUnit")
  private String valorUnitario;
  
  public String getCodigoEan() {
		return codigoEan;
	}
  
  public String getQuantidadePedida() {
		return quantidadePedida;
	}
  
  public String getValorUnitario() {
		return valorUnitario;
	}

@Override
  public String toString() {
		return "Produto [codigoEan=" + codigoEan + ", quantidadePedida=" + quantidadePedida + ", valorUnitario="
			+ valorUnitario + "]";
}
  
  

  /*@XmlElement(name = "cpe")
  private String codigoProduto;

  @XmlElement(name = "xProd")
  private String descricaoProduto;

  @XmlElement(name = "prod_Cbc")
  private ProdCbc informacaoCbc; 

  @XmlElement(name = "xPed") // Renamed to avoid conflict with 'xObs'
  private String observacaoProduto;
  
  @XmlElement(name = "vUnitBru")
  private double valorUnitarioBruto;

  @XmlElement(name = "vMargLuc")
  private double valorMargemLucro;

public String getCodigoProduto() {
	return codigoProduto;
}

public String getDescricaoProduto() {
	return descricaoProduto;
}

public ProdCbc getInformacaoCbc() {
	return informacaoCbc;
}

public String getObservacaoProduto() {
	return observacaoProduto;
}

public double getValorUnitarioBruto() {
	return valorUnitarioBruto;
}

public double getValorMargemLucro() {
	return valorMargemLucro;
}*/

  
  
}
static class ProdCbc {
	  @XmlElement(name = "cEANcx")
	  private String codigoEanCaixa;

	public String getCodigoEanCaixa() {
		return codigoEanCaixa;
	} 
	  
	  
}
@Override
public String toString() {
	return "Pedido [cnpj=" + cnpj + ", produtos=" + produtos + "]";
}


}


	


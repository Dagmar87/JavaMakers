package entidade;

import java.util.Date;
import java.util.List;

public class Pedido {
	
	private int id;
	private String fornecedor;
	private Date data;
	private double valor;
	private Veiculo veiculo;
	private Usuario usuario;
	
	private List<PedidoPeca> listaPedidoPeca;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<PedidoPeca> getListaPedidoPeca() {
		return listaPedidoPeca;
	}

	public void setListaPedidoPeca(List<PedidoPeca> listaPedidoPeca) {
		this.listaPedidoPeca = listaPedidoPeca;
	}
	
}

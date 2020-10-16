package dao;

import java.util.Date;
import java.util.List;

import entidade.Peca;
import entidade.Pedido;

public interface PedidoDAO {
	
	public void inserirPedido(Pedido pedido);
	
	public List<Pedido> pesquisarPedido(String fornecedor, double valor, String placa, int ano,
			Date dataInicio, Date dataFim, String modelo, int idPeca);
	
	public List<Peca> listarPecas();
	
	public void deletarPedido(Pedido pedido);
	
	public void editarPedido(Pedido pedido);
	
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entidade.Peca;
import entidade.Pedido;
import entidade.PedidoPeca;
import entidade.Veiculo;
import util.JdbcUtil;

public class PedidoDAOImpl implements PedidoDAO{
	
	/**
	 * Metodo utilizado para recuperar o proximo id da sequence
	 * @return
	 */
	
	private Integer recuperaIdPedido(){
		
		String sql = "select S_ID_PEDIDO.nextval from dual";
		
		Integer idRetorno = null;
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);

			ResultSet res = ps.executeQuery();
			
			while (res.next()) {
				
				idRetorno = res.getInt(1);

			 }
			
			ps.close();
			conexao.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return idRetorno;
		
	}
	
	@Override
	public void inserirPedido(Pedido pedido) {
		// TODO Auto-generated method stub
		
		int idPedido = this.recuperaIdPedido();
		
		pedido.setId(idPedido);
		
		String sqlPedido = "INSERT INTO PEDIDO (ID_PEDIDO, "
				+ " FORNECEDOR, DATA, VALOR, "
				+ " ID_VEICULO, ID_USUARIO) VALUES (?,?,?,?,?,?)";
		
		String sqlVeiculo = "INSERT INTO VEICULO (PLACA, MODELO, "
				+ " FABRICANTE, ANO, COR) VALUES(?,?,?,?,?)";
		
		String sqlPedidoPeca = "INSERT INTO PEDIDOPECA (ID_PEDIDO, ID_PECA) VALUES (?,?)";
		
		Connection conexao = null;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			conexao.setAutoCommit(false);
			
			PreparedStatement ps = conexao.prepareStatement(sqlVeiculo);
			
			ps.setString(1, pedido.getVeiculo().getPlaca());
			ps.setString(2, pedido.getVeiculo().getModelo());
			ps.setString(3, pedido.getVeiculo().getFabricante());
			ps.setInt(4, pedido.getVeiculo().getAno());
			ps.setString(5, pedido.getVeiculo().getCor());
			
			ps.execute();
			
			ps = conexao.prepareStatement(sqlPedido);
			
			ps.setInt(1, pedido.getId());
			ps.setString(2, pedido.getFornecedor());
			ps.setDate(3, new java.sql.Date(pedido.getData().getTime()));
			ps.setDouble(4, pedido.getValor());
			ps.setString(5, pedido.getVeiculo().getPlaca());
			ps.setString(6, pedido.getUsuario().getCpf());

			ps.execute();
			
			for (PedidoPeca pedidoPeca : pedido.getListaPedidoPeca()) {
				ps = conexao.prepareStatement(sqlPedidoPeca);
				
				ps.setInt(1, pedido.getId());
				ps.setInt(2, pedidoPeca.getPeca().getId());
				
				ps.execute();
			}
			
			ps.close();
			conexao.commit();
			conexao.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(conexao != null){
				try {
					conexao.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			}
			
		}
		
	@Override
	public List<Peca> listarPecas() {
		// TODO Auto-generated method stub
		
		List<Peca> listaRetorno = new ArrayList<Peca>();
		
		String sql = "SELECT P.ID_PECA, P.TIPO, P.MODELO FROM PECA P ORDER BY P.MODELO ASC";
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ResultSet res = ps.executeQuery();
			
			while(res.next()){
				
				Peca peca = new Peca();
				peca.setId(res.getInt("ID_PECA"));
				peca.setTipo(res.getString("TIPO"));
				peca.setModelo(res.getString("MODELO"));
				
				listaRetorno.add(peca);
				
			}
			
			ps.close();
			conexao.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		return listaRetorno;
	}

	@Override
	public List<Pedido> pesquisarPedido(String fornecedor, double valor, String placa, int ano,
			java.util.Date dataInicio, java.util.Date dataFim, String modelo, int idPeca) {
		
		List<Pedido> listaRetorno = new ArrayList<Pedido>();
		
		String sql = "	SELECT  DISTINCT P.ID_PEDIDO," + 
				"    P.FORNECEDOR, " + 
				"    P.VALOR, " + 
				"    V.PLACA, " + 
				"    V.ANO, " + 
				"    P.DATA, " + 
				"    V.MODELO " + 
				" FROM PEDIDO P " + 
				" JOIN VEICULO V " + 
				"   ON P.ID_VEICULO = V.PLACA " + 
				" JOIN PEDIDOPECA PE " + 
				"   ON PE.ID_PEDIDO = P.ID_PEDIDO " + 
				" WHERE P.ID_VEICULO = V.PLACA " + 
							this.montarWherePesquisa(fornecedor, valor, placa, ano, dataInicio, dataFim, modelo, idPeca);
		
		System.out.println(sql);
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				
				Pedido pedido = new Pedido();
				pedido.setId(res.getInt("ID_PEDIDO"));
				pedido.setFornecedor(res.getString("FORNECEDOR"));
				pedido.setValor(res.getDouble("VALOR"));
				pedido.setData(res.getDate("DATA"));
				
				Veiculo veiculo = new Veiculo();
				veiculo.setPlaca(res.getString("PLACA"));
				veiculo.setModelo(res.getString("MODELO"));
				veiculo.setAno(res.getInt("ANO"));
				
				pedido.setVeiculo(veiculo);
				
				listaRetorno.add(pedido);

			 }
			
			ps.close();
			conexao.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaRetorno;
	}
	
	private String montarWherePesquisa(String fornecedor, double valor, String placa, int ano,
			Date dataInicio, Date dataFim, String modelo, int idPeca) {
		
		String where = "";
		
		SimpleDateFormat dataSimples = new SimpleDateFormat("dd/MM/yyyy");
		
		if(fornecedor != null && !fornecedor.isEmpty()) {
			where += " AND UPPER(P.FORNECEDOR) LIKE UPPER('%" + fornecedor + "%') ";
		}
		
		if(valor > 0) {
			where += " AND P.VALOR = " + valor;
		}
		
		if(placa != null && !placa.isEmpty()) {
			where += " AND UPPER(V.PLACA) = UPPER('%" + placa + "%') ";
		}
		
		if(ano > 0) {
			where += " AND V.ANO = " + ano;
		}
		
		if(dataInicio != null && dataFim != null) {
			where += " AND P.DATA BETWEEN TO_DATE('" + dataSimples.format(dataInicio) + "', 'DD/MM/YYYY') and "
									 + " TO_DATE('" + dataSimples.format(dataFim) + "', 'DD/MM/YYYY') ";
		}
		
		if(modelo != null && !modelo.isEmpty()) {
			where += " AND UPPER(V.MODELO) LIKE UPPER ('%" + modelo + "%') ";
		}

		if(idPeca > 0) {
			where += " AND PE.ID_PECA = " + idPeca;
		}
		
		return where;
	}

	@Override
	public void deletarPedido(Pedido pedido) {
		
		String sqlPedidoPeca = "DELETE FROM PEDIDOPECA PE WHERE pe.id_pedido = ?";
		String sqlVeiculo = "DELETE FROM VEICULO V WHERE v.placa = ?";
		String sqlPedido = "DELETE FROM PEDIDO P WHERE p.id_pedido = ?";
		
		Connection conexao = null;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			conexao.setAutoCommit(false);
			
			PreparedStatement ps = conexao.prepareStatement(sqlPedidoPeca);
			
			ps.setInt(1, pedido.getId());
			
			ps.execute();

			ps = conexao.prepareStatement(sqlPedido);
			
			ps.setInt(1, pedido.getId());
			
			ps.execute();
			
			ps = conexao.prepareStatement(sqlVeiculo);
			
			ps.setString(1, pedido.getVeiculo().getPlaca());

			ps.execute();
			
			
			ps.close();
			conexao.commit();
			conexao.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			if(conexao != null) {
				try {
					conexao.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		
	}

	@Override
	public void editarPedido(Pedido pedido) {
		// TODO Auto-generated method stub
		
		String sqlPedido = "UPDATE PEDIDO P SET P.FORNECEDOR = ?, P.DATA = ?, P.VALOR = ?, P.ID_VEICULO = ?, P.ID_USUARIO = ? "
				          + " WHERE P.ID_PEDIDO = ?";
		
		String sqlVeiculo = "UPDATE VEICULO V SET V.PLACA = ?, V.MODELO = ?, V.FABRICANTE = ?, V.ANO = ?, V.COR = ? "
						  + " WHERE V.PLACA = ?";
		
		String sqlPedidoPeca = "UPDATE PEDIDOPECA PP SET PP.ID_PECA = ?, PP.ID_PEDIDO = ? "
                             + " WHERE PP.ID_PECA = ? "
                             + " AND PP.ID_PEDIDO = ?";
		
		Connection conexao = null;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			conexao.setAutoCommit(false);
			
			PreparedStatement ps = conexao.prepareStatement(sqlVeiculo);
			
			ps.setString(1, pedido.getVeiculo().getPlaca());
			ps.setString(2, pedido.getVeiculo().getModelo());
			ps.setString(3, pedido.getVeiculo().getFabricante());
			ps.setInt(4, pedido.getVeiculo().getAno());
			ps.setString(5, pedido.getVeiculo().getCor());
			ps.setString(6, pedido.getVeiculo().getPlaca());
			
			ps.execute();
			
			ps = conexao.prepareStatement(sqlPedido);
			
			ps.setString(1, pedido.getFornecedor());
			ps.setDate(2, new java.sql.Date(pedido.getData().getTime()));
			ps.setDouble(3, pedido.getValor());
			ps.setString(4, pedido.getVeiculo().getPlaca());
			ps.setString(5, pedido.getUsuario().getCpf());
			ps.setInt(6, pedido.getId());
			
			ps.execute();
			
			for (PedidoPeca pedidoPeca : pedido.getListaPedidoPeca()){
				
				ps = conexao.prepareStatement(sqlPedidoPeca);
				
				ps.setInt(1, pedido.getId());
				ps.setInt(2, pedidoPeca.getPeca().getId());
				ps.setInt(3, pedido.getId());
				ps.setInt(4, pedidoPeca.getPeca().getId());
				
				ps.execute();
				
			}
			
			ps.close();
			conexao.commit();
			conexao.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			if(conexao != null){
				try {
					conexao.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

}

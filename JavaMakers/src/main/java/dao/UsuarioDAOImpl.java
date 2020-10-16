package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entidade.Usuario;
import util.JdbcUtil;

public class UsuarioDAOImpl implements UsuarioDAO{
	
	Usuario usuario = new Usuario();

	@Override
	public void inserir(Usuario usuario) {
		
		String sql = "INSERT INTO USUARIO (CPF, EMAIL, NOME, IDADE, SENHA) VALUES (?, ?, ?, ?, ?)";
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();

			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, usuario.getCpf());
			ps.setString(2, usuario.getEmail());
			ps.setString(3, usuario.getNome());
			ps.setInt(4, usuario.getIdade());
			ps.setString(5, usuario.getSenha());
			
			ps.execute();
			ps.close();
			conexao.close(); 
						
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

	@Override
	public Usuario pesquisar(String cpf) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT U.CPF, U.NOME, U.SENHA, U.EMAIL, U.IDADE FROM USUARIO U WHERE CPF = ?";
		
		Usuario usuario = null;
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, cpf);
						
			ResultSet res = ps.executeQuery();
			
			while(res.next()){
				
				usuario = new Usuario();
				usuario.setCpf(res.getString("CPF"));
				usuario.setNome(res.getString("NOME"));
				usuario.setSenha(res.getString("SENHA"));
				usuario.setEmail(res.getString("EMAIL"));
				usuario.setIdade(res.getInt("IDADE"));
								
			}
			
			ps.close();
			conexao.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return usuario;
		
	}
	
	@Override
	public void alterar(Usuario usuario) {
		// TODO Auto-generated method stub
		
		String sql = "UPDATE USUARIO U SET U.SENHA = ?, U.NOME = ?, U.EMAIL = ?, U.IDADE = ? WHERE CPF = ?";
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, usuario.getSenha());
			ps.setString(2, usuario.getNome());
			ps.setString(3, usuario.getEmail());
			ps.setInt(4, usuario.getIdade());
			ps.setString(5, usuario.getCpf());
			
			ps.execute();
			ps.close();
			conexao.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void remover(Usuario usuario) {
		// TODO Auto-generated method stub
		
		/*String sql = "DELETE FROM USUARIO WHERE CPF = ?";
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, cpf);
			
			ps.execute();
			ps.close();
			conexao.close();
			
		} catch (Exception e) {
			e.printStackTrace();
				
		}*/
		
	}
	
	@Override
	public List<Usuario> listarTodos() {
		// TODO Auto-generated method stub
		
		String sql = "SELECT * FROM USUARIO";
		
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		
		Connection conexao;
		
		try {
			
			conexao = JdbcUtil.getConexao();
			
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ResultSet res = ps.executeQuery();
			
			while(res.next()){
				
				usuario = new Usuario();
				usuario.setCpf(res.getString("CPF"));	
				usuario.setEmail(res.getString("EMAIL"));
				usuario.setNome(res.getString("NOME"));
				usuario.setIdade(res.getInt("IDADE"));
				usuario.setSenha(res.getString("SENHA"));
				
				listaUsuario.add(usuario);
				
			}
			
			ps.close();
			conexao.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return listaUsuario;
	}

}

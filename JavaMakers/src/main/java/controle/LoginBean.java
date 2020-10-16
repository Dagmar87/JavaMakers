package controle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import entidade.Usuario;

@ManagedBean(name="LoginBean")
@SessionScoped
public class LoginBean {
	
	private String cpfTela;
	private String senhaTela;
	private Usuario usuario;
	
	private UsuarioDAO usuarioDAO;
	
	public LoginBean(){
		this.usuario = new Usuario();
		this.usuarioDAO = new UsuarioDAOImpl();
	}
	
	//Criar logica de entrar, validando se o usuário existe e se seu cpf e senha estão corretos.
	public String entrar(){
		
		Usuario usuarioBanco = this.usuarioDAO.pesquisar(this.cpfTela);
		System.out.println(usuarioBanco);
		if(usuarioBanco != null){
			if(usuarioBanco.getSenha().equals(this.senhaTela)){
				
				HttpSession sessao =  (HttpSession)FacesContext.getCurrentInstance()
						.getExternalContext().getSession(true);
				sessao.setAttribute("usuarioLogado", usuarioBanco);
				
				return "telaPrincipal.xhtml?faces-redirect=true&amp;includeViewParams=true";
			}else {
				System.out.println("-- Senha invalida --");
			}
		} else{
			System.out.println("-- Usuario invalido --");
		}
		
		return "";
		
	}
	
	//Criar logica de salvar o usuário criado, em uma lista dentro do bean.
	public void criarUsuario(){
		
		this.usuarioDAO.inserir(this.usuario);
		this.usuario = new Usuario();
		
		System.out.println("-- Usuario Criado --");
		
	}
	
	//Criar logica de recuperar senha, validando se o usuário existe e se seus dados estão corretos, para atualizar a senha.
	public void validarUsuario(){
		System.out.println("-- Usuario Verificado --");
	}
	
	public void recuperarSenha(){
		System.out.println("-- Recupérar Senha --");
	}
	
	public String getCpfTela() {
		return cpfTela;
	}

	public void setCpfTela(String cpfTela) {
		this.cpfTela = cpfTela;
	}

	public String getSenhaTela() {
		return senhaTela;
	}

	public void setSenhaTela(String senhaTela) {
		this.senhaTela = senhaTela;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}

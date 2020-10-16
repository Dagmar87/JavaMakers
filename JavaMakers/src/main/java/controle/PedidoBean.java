package controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.PedidoDAO;
import dao.PedidoDAOImpl;
import entidade.Peca;
import entidade.Pedido;
import entidade.PedidoPeca;
import entidade.Usuario;
import entidade.Veiculo;

@ManagedBean(name="PedidoBean")
@SessionScoped
public class PedidoBean {
	
	private Pedido pedido;
	private int idPeca;
	private List<Peca> listaPecas;
	private Date dataInicio;
	private Date dataFim;
	
	private List<Pedido> listaPedidos;
	private int idPedido;
	
	private PedidoDAO pedidoDAO;
	
	public PedidoBean(){
		
		this.iniciarCampor();
		
		this.pedidoDAO = new PedidoDAOImpl();
		
		this.listaPecas = this.pedidoDAO.listarPecas();
		
	}
	
	private void iniciarCampor(){
		
		this.pedido = new Pedido();
		this.pedido.setUsuario(new Usuario());
		this.pedido.setVeiculo(new Veiculo());
		this.pedido.setData(new Date());
		this.pedido.setListaPedidoPeca(new ArrayList<PedidoPeca>());
		
		HttpSession sessao =  (HttpSession)FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		
		this.pedido.setUsuario((Usuario)sessao.getAttribute("usuarioLogado"));
		
	}
	
	public void adicionarPeca(){
		
		boolean existe = false;
		
		for (PedidoPeca pc : this.pedido.getListaPedidoPeca()){
			if(pc.getPeca().getId() == this.idPeca){
				existe = true;
			}
		}
		
		if(existe){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Erro:", "Peça já existente!") );
		}else{
			PedidoPeca pedidoPeca = new PedidoPeca();
			pedidoPeca.setPedido(this.pedido);
			
			for (Peca pecaLista : this.listaPecas) {
				if(pecaLista.getId() == this.idPeca) {
					pedidoPeca.setPeca(pecaLista);
				}
				
			}
				
			this.pedido.getListaPedidoPeca().add(pedidoPeca);
			this.idPeca = 0;
				
		}
		
	}
	
	public void salvarPedido(){
		
		this.pedidoDAO.inserirPedido(this.pedido);
		
		this.iniciarCampor();
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Sucesso", "Pedido cadastrado!") );
		
	}
	
	public String removerPeca(){
		
		PedidoPeca encontrada = null;
		
		for (PedidoPeca pc : this.pedido.getListaPedidoPeca()){
			if(pc.getPeca().getId() == this.idPeca){
				encontrada = pc;
			}
		}
		
		if(encontrada != null){
			this.pedido.getListaPedidoPeca().remove(encontrada);
		}
		
		this.idPeca = 0;
		
		return "";
		
	}
	
	public void pesquisar(){
		
		this.listaPedidos = this.pedidoDAO.pesquisarPedido(
				this.pedido.getFornecedor(),
				this.pedido.getValor(),
				this.pedido.getVeiculo().getPlaca(),
				this.pedido.getVeiculo().getAno(),
				this.dataInicio, 
				this.dataFim, 
				this.pedido.getVeiculo().getModelo(), 
				this.idPeca);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Sucesso", "Pedido pesquisado!") );
		
	}
	
	public String removerPedido(){
		
		Pedido pedidoRemover = null;
		
		for (Pedido pedidoLista : listaPedidos) {
			if(this.idPedido == pedidoLista.getId()) {
				pedidoRemover = pedidoLista;
			}
		}
		
		if(pedidoRemover != null) {
			this.pedidoDAO.deletarPedido(pedidoRemover);
			this.pesquisar();
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Sucesso", "Pedido removido!") );
						
		}
		
		return "";
		
	}
	
	public void editarPedido(){
		
		this.pedidoDAO.editarPedido(this.pedido);
		
		this.iniciarCampor();
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Sucesso", "Pedido alterado!") );
		
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Peca> getListaPecas() {
		return listaPecas;
	}

	public void setListaPecas(List<Peca> listaPecas) {
		this.listaPecas = listaPecas;
	}

	public int getIdPeca() {
		return idPeca;
	}

	public void setIdPeca(int idPeca) {
		this.idPeca = idPeca;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(List<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	
}

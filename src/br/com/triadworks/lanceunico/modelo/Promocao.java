package br.com.triadworks.lanceunico.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.triadworks.lanceunico.util.DateUtils;

@Entity
public class Promocao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	
	private String nome;
	private double valorMaximo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Enumerated(EnumType.STRING)
	private Local entrega;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.ABERTA;
	
	private boolean receberEmDinheiro = false;
	
	@JoinColumn(name="promocao_id")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Lance> lances = new ArrayList<>();
	
	public Promocao(){}
	
	public Promocao(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getValorMaximo() {
		return valorMaximo;
	}
	public void setValorMaximo(double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public List<Lance> getLances() {
		return lances;
	}
	public void setLances(List<Lance> lances) {
		this.lances = lances;
	}
	public Local getEntrega() {
		return entrega;
	}
	public void setEntrega(Local entrega) {
		this.entrega = entrega;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public boolean isReceberEmDinheiro() {
		return receberEmDinheiro;
	}
	public void setReceberEmDinheiro(boolean receberEmDinheiro) {
		this.receberEmDinheiro = receberEmDinheiro;
	}

	/**
	 * Registra um novo lance
	 */
	public void registra(Lance lance) {
		
		if(lance.getValor() <= 0){
			throw new RuntimeException("Valor igual à zero ou negativo.");
		}
		
		if(ClienteDandoLancesEmSequencia(lance.getCliente())){
			return;
		}
		
		this.lances.add(lance);
	}
	
	private boolean ClienteDandoLancesEmSequencia(Cliente cliente){
		return !lances.isEmpty() && ClienteDoUltimoLance().equals(cliente);
	}
	
	private Cliente ClienteDoUltimoLance(){
		Lance ultimo = lances.get(lances.size() -1);
		return ultimo.getCliente();
	}

	/**
	 * Verifica se promoção pode ser encerrada
	 */
	public boolean foraDaVigencia(Date dataBase) {
		int dias = DateUtils.diferencaEmDias(this.data, dataBase);
		return dias >= 30;
	}
	


	public boolean isEncerrada() {
		return this.status == Status.ENCERRADA;
	}

	public boolean isAberta() {
		return this.status == Status.ABERTA;
	}
	
}

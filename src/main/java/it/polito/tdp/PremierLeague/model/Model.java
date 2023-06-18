package it.polito.tdp.PremierLeague.model;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Player, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new PremierLeagueDAO();
		dao.listAllPlayers();
	}
	
	public void creaGrafo(double x) {
		this.grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getGiocatori(x));
		
		for(Player p1 : grafo.vertexSet()) {
			for(Player p2 : grafo.vertexSet()) {
				if(p1.getPlayerID()<p2.getPlayerID()) {
					int i = dao.getArco(p1.getPlayerID(),p2.getPlayerID());
					if(i>0) {
						Graphs.addEdgeWithVertices(this.grafo, p1, p2, i);
					}else if(i<0) {
						Graphs.addEdgeWithVertices(this.grafo, p2, p1, -i);
					}
				}
			}
		}
	}
	
	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}

	public Player BestPlayer() {
		Player p1 = null;
		int i = 0;
		for(Player p : this.grafo.vertexSet()) {
			if(this.grafo.outgoingEdgesOf(p).size()>i) {
				p1 = p;
				i = this.grafo.outgoingEdgesOf(p).size();
			}
		}
		return p1;
	}
	
	public List<Player> getSet(Player p1){
		/*Set<Player> s = new HashSet<Player>();
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p1)) {
			s.add(Graphs.getOppositeVertex(this.grafo, e, p1));
		}*/
		return Graphs.successorListOf(this.grafo, p1);
		//return s;
	}
	
	
	
	
	List<Player> best;
	int k; 
	int bestcosto = 0;
	
	private void setK(int k) {
		this.k = k;
	}
	
	public List<Player> ricorsione(int i) {
		this.setK(i);
		best = new LinkedList<>();
		LinkedList<Player> parziale = new LinkedList<Player>();
		
		HashSet<Player> pp = new HashSet<Player>(this.grafo.vertexSet());
		cerca(parziale, 0, pp);
		return best;
	}

	private void cerca(LinkedList<Player> parziale, int costo, HashSet<Player> pp) {
		
		if(parziale.size() == k) {
			if (costo>bestcosto) {
				bestcosto = costo;
				best = new LinkedList<Player>(parziale);
			}
			return;
		}
		
		for(Player p : pp) {
			if(!parziale.contains(p)) {
			parziale.add(p);
			int pi = calcola(p);
			costo += pi;
			
			HashSet<Player> ppp = new HashSet<Player>(pp);
			ppp.removeAll(Graphs.successorListOf(this.grafo, p));
			
			cerca(parziale, costo, ppp);
			
			//pp.addAll(Graphs.successorListOf(this.grafo, p));
			costo -= pi;
			parziale.remove(p);
			}
		}
		
		
	}

	private int calcola(Player p) {
		int i = 0;
		for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
			i += this.grafo.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
			i -= this.grafo.getEdgeWeight(e);
		}
		return i;
	}
	
}

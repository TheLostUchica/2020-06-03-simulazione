package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	HashMap<Integer, Player> IdMap;
	
	public HashMap<Integer, Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		IdMap = new HashMap<Integer, Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				IdMap.put(player.getPlayerID(), player);
			}
			conn.close();
			return IdMap;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getGiocatori(double x){
		
		String sql = "SELECT PlayerID FROM (SELECT PlayerID, SUM(Goals) AS goal, COUNT(PlayerID) AS n FROM actions GROUP BY PlayerID) AS t WHERE t.goal/t.n > ?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(IdMap.get(res.getInt("PlayerID")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	public int getArco(int playerID, int playerID2) {
		
		String sql = "SELECT (t1.Starts + t2.Starts) AS s, (t1.TimePlayed - t2.TimePlayed) AS p "
				+ "FROM "
				+ "(SELECT PlayerID, MatchID, TimePlayed, `Starts`, TeamID "
				+ "FROM actions "
				+ "WHERE PlayerID = ?) AS t1, (SELECT PlayerID, MatchID, TimePlayed, `Starts`, TeamID "
				+ "FROM actions "
				+ "WHERE PlayerID = ?) AS t2 "
				+ "WHERE t1.MatchId = t2.MatchID AND t1.PlayerID < t2.PlayerID AND t1.TeamID <> t2.TeamID";
		
		Connection conn = DBConnect.getConnection();
		int i = 0; 
		boolean flag = false;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, playerID);
			st.setInt(2, playerID2);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if(res.getInt("s")==2) {
					i+=res.getInt("p");
					flag = true;
				}
			}
			
			conn.close();
			if(flag==true) {
				return i;
			}else {
				return 0;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return i;
		}
	}
	//se il risultato è positivo il primo ha giocato piu minuti del secondo
			
}

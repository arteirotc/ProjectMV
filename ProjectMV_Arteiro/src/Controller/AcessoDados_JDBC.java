package Controller;

import java.sql.*;

public class AcessoDados_JDBC {

	public static void testeBanco() throws SQLException {
		// FUNCAO PARA TESTE DE CONEXAO - DEBUG

		 Connection conexao = ObterConexao();
		 
		 Statement statement = conexao.createStatement();
		 		 
		 String query = "SELECT DESCRICAO FROM PRODUTOS";
		 
		 ResultSet resultSet = statement.executeQuery(query);
		 
		 if (resultSet.next()) {

			 System.out.println(resultSet.getString("DESCRICAO"));
		 }
	}

	public static ResultSet LerBanco(String query) throws SQLException {
		//FUNCAO PARA RETORNAR DATASET DO BANCO PARA QUERYS SELECT
		
		Connection conexao = ObterConexao();
		 
		Statement statement = conexao.createStatement();
		 		 
		ResultSet resultSet = statement.executeQuery(query);
		 
		return resultSet;	
	}
	
	public static void AlterarBanco(String query) throws SQLException {
		//FUNCAO PARA INSERIR, ATUALIZAR E DELETAR REGISTROS NO BANCO
		
		Connection conexao = ObterConexao();
		 
		Statement statement = conexao.createStatement();
		 		 
		statement.executeUpdate(query);	
	}
	
	public static void ExecuteProcedure(String query) throws SQLException {
		//FUNCAO RESPONSAVEL PELA EXECUCAO DE STORE PROCEDURE COM PARAMETROS
		
		Connection conexao = ObterConexao();
		
		CallableStatement statement = conexao.prepareCall(query);
		
		statement.execute();
	}
	
	public static int CountLines(String SQL) throws SQLException {
		//FUNCAO PARA RECUPERAR CONTADOR DE LINHAS EM QUERY
		
		int CountLines = 0;
		String StringSQL;
		ResultSet bancoset;
		String arraySQL[] = new String[2];
			
		arraySQL = SQL.split("FROM");
		StringSQL = "SELECT COUNT(*) AS CONTADOR FROM " + arraySQL[1];	
		bancoset = LerBanco(StringSQL);
		
		//tentativa de Recuperar Numero de Linhas na Query
		if (bancoset.next()) {
			CountLines = bancoset.getInt("CONTADOR");
		}
		
		//Segunta tentativa de Recuperar Numero de Linhas na Query
		if (CountLines == 0) {
			bancoset = LerBanco(SQL);
			CountLines = bancoset.last() ? bancoset.getRow() : 0;
		}
		return CountLines;
	}
	
	public static int CountFields(String SQL) throws SQLException {
		//FUNCAO PARA RECUPERAR CONTADOR DE COLUNAS EM QUERY
		
		ResultSet bancoset = LerBanco(SQL);
		
		ResultSetMetaData rsmd = bancoset.getMetaData();
		
		return rsmd.getColumnCount();
	}
	
	private static Connection ObterConexao() {
		// DADOS DE ACESSO AO BANCO

		Connection conexao = null;

        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	conexao = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "260483");
        
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();

        } catch (SQLException e) {
        	e.printStackTrace();
        
        }

        return conexao;
	}

}

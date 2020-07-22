package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.AcessoDados_JDBC;


public class Produtos extends AcessoDados_JDBC {
	
	
	public static String[][] ListarProdutos(String SQL) throws SQLException {
		//FUNCAO RESPONSAVEL PELA LISTAGEM DE PRODUTOS RECUPERADOS POR SQL PARA ARRAY
		
		String[][] arrSQL = null;
		int CountFields	  = 0;
		int CountLines    = 0;
		ResultSet bancoset;
			
		//Recuperar Numero de Linhas na Query
		CountLines = CountLines(SQL);
			
		//Recuperar Numero de Colunas na Query
		CountFields = CountFields(SQL);

		//Print Debug Variable
		//System.out.println("Linhas: " + CountLines);
		//System.out.println("Colunas: " + CountFields);
		
		bancoset = LerBanco(SQL);
			
		if (bancoset.next()) {
			arrSQL = new String[CountLines][CountFields];
				
			//MONTAGEM DO ARRAY MATRIZ PARA ARMAZENAMENTO DOS DADOS DO BANCO
			for (int i = 0; i < CountLines; i++) {
				for (int j = 0; j < CountFields; j++) {
					arrSQL[i][j] = bancoset.getString(j + 1);
				}
				bancoset.next();
			}
		}
		return arrSQL;
	}
	
	public static void INSERTProdutos(String DescItem, Float ValorItem) throws SQLException {
		//FUNCAO RESPONSAVEL PELA INCLUSÃO DE REGISTRO EM BANCO
		
		String SQL = SQLGeneretor("INSERT");
		
		SQL = SQL + "'" + DescItem + "', '" + ValorItem + "'";
		
		System.out.println("SQL: " + SQL);
		
		AlterarBanco(SQL);
	}
	
	public static void UPDATEProdutos(String FieldUpdate, String FieldValue, String FieldWHere, String IDValue) throws SQLException {
		//FUNCAO RESPONSAVEL PELA EDICAO DE REGISTRO EM BANCO
		
		String SQL = SQLGeneretor("UPDATE");
		
		SQL = SQL + FieldUpdate + " = '" + FieldValue + "' ";
		
		SQL = ADDWhere(SQL, FieldWHere, IDValue);
		
		System.out.println("SQL: " + SQL);
		
		AlterarBanco(SQL);
	}
	
	public static void DELETEProdutos(String MenuItem) throws SQLException {
		//FUNCAO RESPONSAVEL PELA REMOCAO DE REGISTRO EM BANCO
		
		String SQL = SQLGeneretor("DELETE");
		
		SQL = SQL + MenuItem + "'";
		
		System.out.println("SQL: " + SQL);
		
		AlterarBanco(SQL);
	}
	
	public static String ADDWhere(String SQL, String Field, String Value) throws SQLException {
		//FUNCAO RESPONSAVEL POR ADICIONAR CLÁUSULA WHERE NO SQL
		
		int index = -1;
		
		index = SQL.indexOf("WHERE");
		
		if (index >= 0) {
			
			SQL = SQL + " AND " + Field + " = '" + Value + "'";
			
		} else {
			
			SQL = SQL + " WHERE " + Field + " = '" + Value + "'";
			
		}	
		
		return SQL;
	}
	
	public static void ExecuteStoreProc(String StoreProcedure, String Param) throws SQLException {
		//FUNCAO RESPONSAVEL PELA EXECUCAO DE STORE PROCEDURE NO BANCO
		
		String SQL = "CALL " + StoreProcedure + " (" + Param + ")";
		
		System.out.println("SQL: " + SQL);
		
		ExecuteProcedure(SQL);
	}
	
	public static String SQLGeneretor(String Type) throws SQLException {
		//CADASTRO DE QUERYS DE BANCO
		//A MELHOR OPCAO SERIA O USO DO RECURSO SWITCH CASE, MAS COMO O MESMO FOI USADO NO MENU, DEIXEI ESSA OPCAO COM IF.
		
		String QuerySelect = "SELECT ROW_ID, PRECO, DESCRICAO FROM PRODUTOS";
		String QueryInsert = "INSERT INTO PRODUTOS (ROW_ID, DESCRICAO, PRECO) VALUES (SEQ_TESTE.NEXTVAL, '";
		String QueryUpdate = "UPDATE PRODUTOS SET ";
		String QueryDelete = "DELETE FROM PRODUTOS WHERE ROW_ID = '";
		
		if (Type == "SELECT") return QuerySelect;
		if (Type == "INSERT") return QueryInsert;
		if (Type == "UPDATE") return QueryUpdate;
		if (Type == "DELETE") return QueryDelete;
		else return "NOTFOUND";
	}
}

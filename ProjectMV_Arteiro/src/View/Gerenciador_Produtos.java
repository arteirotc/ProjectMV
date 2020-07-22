package View;

import java.sql.*;
import Model.Menu;
import Controller.AcessoDados_JDBC;
import Controller.Produtos;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Gerenciador_Produtos extends AcessoDados_JDBC {

	public static void main(String[] args) throws SQLException, InterruptedException {
				
		//Declarar Variaveis Locais
		Scanner entrada = new Scanner(System.in);
		Menu[] arrayMenu = new Menu[7];
		int MenuItem;
		
		//Cadastrar Objetos Itens do Menu, em array vetor.
		//NÃO SERIA A MELHOR SOLUÇÃO, MAS GOSTARIA DE MOSTRAR O CONHECIMENTO DE ARRAY VETOR E CONCEITO DE OBJETOS.
		//UMA OUTRA SOLUCAO SERIA USAR O CONCEITO DE PONTEIRO. 
		//MAS COMO NÃO HAVERIA NECESSIDADE DE MUDAR A SEQUENCIA DOS ITENS, FOI USADO ARRAY.
		arrayMenu[0] = Menu.ObjMenu(1, "Listar Produtos");
		arrayMenu[1] = Menu.ObjMenu(2, "Detalhar Produto");
		arrayMenu[2] = Menu.ObjMenu(3, "Editar Produto");
		arrayMenu[3] = Menu.ObjMenu(4, "Incluir Produto");
		arrayMenu[4] = Menu.ObjMenu(5, "Excluir Produto");
		arrayMenu[5] = Menu.ObjMenu(6, "Reajuste no valor do Produto");
		arrayMenu[6] = Menu.ObjMenu(7, "Reajuste no valor de Todos os Produto");
		
		//Receber retorno da chamada do Menu
		do {
			//CHAMDA DA EXIBICAO DO MENU AO USUARIO
			MenuItem = ExibirMenu(arrayMenu, entrada);
			
			//CHAMADA DA FUNCAO SOLICITADA PELO USUARIO
			switch (MenuItem) {  
		       case 1 : ExibirProdutos(Produtos.SQLGeneretor("SELECT")); break;
		       case 2 : DetalharProdutos(entrada); break;
		       case 3 : EditarProdutos(entrada); break;
		       case 4 : InserirProdutos(entrada); break;
		       case 5 : ExcluirProdutos(entrada); break;
		       case 6 : ReajustarProduto(entrada); break;
		       case 7 : ReajustarProdutos(entrada); break;
		       case 8 : System.out.println("Obrigado Pelo Uso. T+"); break;  
		       default: System.out.println("Opção Inválida.");  
		     }
		} while (MenuItem < arrayMenu.length + 1);
		
		//FECHAR VARIAVEL OBJETO DE SCANNER
		entrada.close();
	}

	private static int ExibirMenu(Menu[] arrayMenu, Scanner entrada) {
		// FUNCAO PARA EXIBIR MENU EM TELA
		Menu itemMenu;
		int MenuItem = 0;
		
		System.out.println("Bem vindo ao Gerenciador de Produtos!");
		System.out.println("-------------------------------------");
		for(int i = 0; i < arrayMenu.length; i++) {
			itemMenu = arrayMenu[i];
			System.out.println("| " + itemMenu.getMenu_Opc() + " - " + itemMenu.getMenu_desc());
		}
		System.out.println("|");
		System.out.println("| 8 - Sair");
		System.out.println("-------------------------------------");
		System.out.print("Opção: ");
		
		try { 
			MenuItem = entrada.nextInt();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
		
		return MenuItem;
	}
	
	private static void DetalharProdutos(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO RESPONSAVEL PELO RETORNO DE DADOS DE UM REGISTRO ESPECIFICO
		
		int MenuItem = 0;
		
		//RECUPERAR EXEMPLO DE QUERY PARA USO
		String SQL = Produtos.SQLGeneretor("SELECT");
		
		//EXIBIR LISTA DE PRODUTOS PARA ESCOLHA PELO USUARIO
		ExibirProdutos(SQL);
		
		System.out.print("Informe o ID do Produto: ");
		try { 
			MenuItem = entrada.nextInt();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
				
		//ADICIONAR CLAUSULA WHERE NA QUERY RECUPERADA ANTERIORMENTE
		SQL = Produtos.ADDWhere(SQL, "ROW_ID", String.valueOf(MenuItem));
		
		//EXIBIR INFORMACOES DE REGISTRO ESCOLHIDO PELO USUARIO
		ExibirProdutos(SQL);
	}
	
	private static void EditarProdutos(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO RESPONSAVEL PELA EDICAO DE REGISTRO EM BANCO
		
		int MenuItem = 0;
		float ValorItem = 0;
		
		//RECUPERAR EXEMPLO DE QUERY PARA USO
		String SQL = Produtos.SQLGeneretor("SELECT");
		
		//EXIBIR LISTA DE PRODUTOS PARA ESCOLHA PELO USUARIO
		ExibirProdutos(SQL);
		
		System.out.print("Informe o ID do Produto: ");
		try { 
			MenuItem = entrada.nextInt();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
				
		//ADICIONAR CLAUSULA WHERE NA QUERY RECUPERADA ANTERIORMENTE
		SQL = Produtos.ADDWhere(SQL, "ROW_ID", String.valueOf(MenuItem));
		
		System.out.println();
		System.out.print("Informe o Valor: ");
		try { 
			ValorItem = entrada.nextFloat();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
		
		//ATUALIZAR REGISTRO ESCOLHIDO PELO USUARIO, COM NOVO VALOR.
		Produtos.UPDATEProdutos("PRECO", String.valueOf(ValorItem), "ROW_ID", String.valueOf(MenuItem));
		
		System.out.println("Registro " + MenuItem + " atualizado.");
		
		//EXIBIR INFORMACOES DE REGISTRO ESCOLHIDO PELO USUARIO
		ExibirProdutos(SQL);
	}
	
	private static void ReajustarProdutos(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO RESPONSAVEL PELA REAJUSTE DE TODOS OS PRODUTOS
		
		float ValorItem = 0;
		
		//RECUPERAR EXEMPLO DE QUERY PARA USO
		String SQL = Produtos.SQLGeneretor("SELECT");
		
		//EXIBIR LISTA DE PRODUTOS ANTES DO REAJUSTE DE VALOR
		System.out.println("Valores Antes do Reajuste:");
		ExibirProdutos(SQL);
		
		System.out.println();
		System.out.print("Informe a Porcentagem de Reajuste: ");
		try { 
			ValorItem = entrada.nextFloat();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
			
		//CHAMAR STORE PROCEDURE RESPONSAVEL PELO ALTERAÇÃO EM BLOCO DE REGISTRO.
		Produtos.ExecuteStoreProc("PKG_MV.REAJUSTE_TODOS_PRODUTOS",  String.valueOf(ValorItem));
		
		System.out.println("Registros atualizado.");
		
		//EXIBIR LISTA DE PRODUTOS APOS DO REAJUSTE DE VALOR
		System.out.println("Valores Após o Reajuste:");
		ExibirProdutos(SQL);
	}
	
	private static void ReajustarProduto(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO RESPONSAVEL PELA REAJUSTE DE VALOR DO PRODUTO ESPECIFICO
		
		int MenuItem = 0;
		float ValorItem = 0;
		
		//RECUPERAR EXEMPLO DE QUERY PARA USO
		String SQL = Produtos.SQLGeneretor("SELECT");
		
		//EXIBIR LISTA DE PRODUTOS PARA ESCOLHA PELO USUARIO
		ExibirProdutos(SQL);
		
		System.out.print("Informe o ID do Produto: ");
		try { 
			MenuItem = entrada.nextInt();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}

		//ADICIONAR CLAUSULA WHERE NA QUERY RECUPERADA ANTERIORMENTE
		SQL = Produtos.ADDWhere(SQL, "ROW_ID", String.valueOf(MenuItem));
		
		System.out.println();
		System.out.print("Informe a Porcentagem de Reajuste: ");
		try { 
			ValorItem = entrada.nextFloat();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
		
		//CHAMAR STORE PROCEDURE RESPONSAVEL PELO ALTERAÇÃO INDIVIDUAL DE REGISTRO.
		Produtos.ExecuteStoreProc("PKG_MV.REAJUSTE_POR_PRODUTO",  ValorItem + ", " + MenuItem);
		
		System.out.println("Registro " + MenuItem + " atualizado.");
		
		//EXIBIR REGISTRO DO PRODUTO APOS DO REAJUSTE DE VALOR
		ExibirProdutos(SQL);
	}
	
	private static void ExcluirProdutos(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO PARA EXCLUIR REGISTRO EM BANCO COM BUSCA POR ID
		
		int MenuItem = 0;
		
		//RECUPERAR EXEMPLO DE QUERY PARA USO
		String SQL = Produtos.SQLGeneretor("SELECT");
				
		//EXIBIR LISTA DE PRODUTOS PARA ESCOLHA PELO USUARIO
		ExibirProdutos(SQL);
		
		System.out.print("Informe o ID do Produto: ");
		
		try { 
			MenuItem = entrada.nextInt();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
				
		//REMOVER REGISTRO ESCOLHIDO PELO USUARIO
		Produtos.DELETEProdutos(String.valueOf(MenuItem));
		
		System.out.println("Registro " + MenuItem + " removido.");
		
		//EXIBIR NOVA LISTA DE PRODUTOS
		ExibirProdutos(SQL);
	}
	
	private static void InserirProdutos(Scanner entrada) throws SQLException, InterruptedException {
		//FUNCAO PARA INSERIR REGISTRO EM BANCO
		
		String DescItem = null;
		float ValorItem = 0;
		
		System.out.println();
		System.out.print("Informe a Descrição do Produto: ");
		
		try { 
			DescItem = entrada.next();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
		
		System.out.println();
		System.out.print("Informe o Valor do Produto: ");
		
		try { 
			ValorItem = entrada.nextFloat();
		} catch (InputMismatchException e) { System.out.println("Tipo de entrada não esperada");
		} catch (NoSuchElementException e) { System.out.println("Quantidade de Entrada Incorreto.");
		}
		
		//INSERIR PRODUTO NO BANCO
		Produtos.INSERTProdutos(DescItem, ValorItem);
		
		System.out.println();
		System.out.println("Registro Inserido.");
		
		//EXIBIR NOVA LISTA DE PRODUTOS
		ExibirProdutos(Produtos.SQLGeneretor("SELECT"));
	}
	
	private static void ExibirProdutos(String SQL) throws SQLException, InterruptedException {
		//FUNCAO PARA RETORNAR TODOS OS REGISTROS DE PRODUTOS
		
		System.out.println();
		System.out.println("Produtos:");
		String[][] arrayProdutos = Produtos.ListarProdutos(SQL);
		
		//System.out.println("Linhas: " + arrayProdutos.length);
		//System.out.println("Linhas: " + arrayProdutos[0].length);
		
		System.out.println("-------------------------------------");
		System.out.println("| ID | PREÇO | Colunas");
		System.out.println("-------------------------------------");
		
		for (int i = 0; i < arrayProdutos.length; i++) {
			for (int j = 0; j < arrayProdutos[i].length; j++) {
				System.out.print("| " + arrayProdutos[i][j] + " ");
			}
			System.out.println();
			System.out.println("-------------------------------------");
		}
		System.out.println();
		Thread.sleep(2000);
	}	
}

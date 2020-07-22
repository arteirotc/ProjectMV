package Model;

public class Menu {
	int    Menu_Opc;
	String Menu_desc;
	
	public int getMenu_Opc() {
		return Menu_Opc;
	}
	public void setMenu_Opc(int menu_Opc) {
		Menu_Opc = menu_Opc;
	}
	public String getMenu_desc() {
		return Menu_desc;
	}
	public void setMenu_desc(String menu_desc) {
		Menu_desc = menu_desc;
	}
	
	public static Menu ObjMenu(int Menu_Opc, String Menu_desc) {
		//FUNCAO PARA CRIAR OBJETO COM OS DADOS RECEBIDOS NO PARAMETRO
		
		Menu itemMenu;
		
		itemMenu = new Menu();
		itemMenu.setMenu_Opc(Menu_Opc);
		itemMenu.setMenu_desc(Menu_desc);
		
		return itemMenu;
	}
}

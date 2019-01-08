package Client;

public class MainClient
{
	public static void main(String[] args) 
	{
		try {
			ClientFrame cf = new ClientFrame();

			cf.setVisible(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

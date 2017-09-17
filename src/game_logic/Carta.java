package game_logic;

public class Carta
{
	private int numero;
	private String id;
	private String color;
	private String signo;
	private String especial; // toma 2 , retorno , bloqueo , cambio color, toma
							 // 4, NULL si no es esepcial

	public Carta(String id, int numero, String color, String especial, String signo)
	{
		this.id = id;
		this.numero = numero;
		this.color = color;
		this.especial = especial;
		this.signo = signo;
	}

	public String getSigno()
	{
		return signo;
	}

	public void setSigno(String signo)
	{
		this.signo = signo;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero = numero;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public String getEspecial()
	{
		return especial;
	}

	public void setEspecial(String especial)
	{
		this.especial = especial;
	}

}

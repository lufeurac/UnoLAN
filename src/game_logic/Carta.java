package game_logic;

import java.io.Serializable;

public class Carta implements Serializable
{
	private int numero;
	private String color;
	private String signo;
	private String especial; // toma 2 , retorno , bloqueo , cambio color, toma
							 // 4, NULL si no es esepcial

	public Carta(int numero, String color, String especial, String signo)
	{
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

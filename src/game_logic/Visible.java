package game_logic;

import java.io.Serializable;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nicolás Restrepo
 */
public class Visible implements Serializable
{
	private List<Carta> miMano;
	private List<Jugador> contrincantes;
	private Carta CartaActual;
}

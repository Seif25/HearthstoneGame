package model.heroes;

import model.cards.minions.Minion;
import exceptions.FullHandException;

public interface HeroListener {
	public void onHeroDeath();

	public void damageOpponent(int amount);

	public void endTurn() throws FullHandException, CloneNotSupportedException;
	
	public void onMinionDeath(Minion m);
	
	public void onUseHeroPower();

}

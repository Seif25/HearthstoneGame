package engine;

import model.cards.Card;
import model.cards.minions.Minion;

public interface GameListener {
	public void onGameOver();
	public void onEndTurn();
	public void onDamageOpponent();
	public void onMinionDeath(Minion m);
	public void onUseHeroPower();
	public void onBurned(Card c);
}

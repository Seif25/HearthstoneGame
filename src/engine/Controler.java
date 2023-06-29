package engine;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;

import sun.audio.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Graphics;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;

public class Controler implements ActionListener , GameListener{
	private Game model;
	private GameView view;
	private JButton Start;
	private JButton EndTurn1;
	private JButton EndTurn2;
	private JButton Deck1;
	private JButton Deck2;
	private boolean Pow;
	private ArrayList<JButton> P1Hand;
	private ArrayList<JButton> P2Hand;
	private ArrayList<JButton> P1Field;
	private ArrayList<JButton> P2Field;
	private Minion targetM;
	private Hero targetH;
	private JButton Confirm1;
	private JButton Confirm2;
	private JTextArea H1Info; 
	private JTextArea H2Info;
	private JPanel CFieldCards;
	private JPanel OFieldCards;
	private JPanel gameOver;
	private Card Selected;
	private JButton Burned1;
	private JButton Burned2;
	public Controler() throws FullHandException, CloneNotSupportedException, IOException{
		view = new GameView();
		P1Hand = new ArrayList<JButton>();
		P2Hand = new ArrayList<JButton>();
		P1Field = new ArrayList<JButton>();
		P2Field = new ArrayList<JButton>();
		Burned1 = new JButton();
		Burned2 = new JButton();
		     view.getReadyP1().addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(! view.getPl1().equals(null)){
					view.getReadyP1().setVisible(false);
					}
				}});
		     view.getReadyP2().addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(! view.getPl2().equals(null)){
							view.getReadyP2().setVisible(false);
						}
						 
					}});
		     Icon startB = new ImageIcon("Images/StartB.png");
		     Start= new JButton(startB);
		     Start.setPreferredSize(new Dimension(135,65));
		     Start.setOpaque(false);
		     view.getCHeroField().add(Start);
		     Start.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(view.getReadyP1().isVisible()==false && view.getReadyP2().isVisible()== false)
						try {
							StartGame();
						} catch (FullHandException f){
							fullHand(f);
						}
						catch( IOException | CloneNotSupportedException e) {
								
								e.printStackTrace();
						}
					try {
						playSound("Audio/Start.wav");
					} catch (UnsupportedAudioFileException | IOException
							| LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}});
		}
	public void StartGame() throws IOException, CloneNotSupportedException, FullHandException{
		if(view.getReadyP1().isVisible()==false && view.getReadyP2().isVisible()== false){
		model= new Game(view.getPl1(),view.getPl2());
		model.setListener(this);
		Icon endTurn = new ImageIcon("Images/EndTurn2.png");
		EndTurn1 = new JButton(endTurn);
		EndTurn2 = new JButton(endTurn);
		H1Info = new JTextArea();
		H1Info.setPreferredSize(new Dimension(300,view.getH1().getHeight()));
		H1Info.setEditable(false);
		H2Info = new JTextArea();
		H2Info.setPreferredSize(new Dimension(300,view.getH1().getHeight()));
		H2Info.setEditable(false);
		view.getH1().add(H1Info,BorderLayout.WEST);
		view.getH2().add(H2Info,BorderLayout.EAST);
		view.getOHeroField().setLayout(new BorderLayout());
		 view.getCHeroField().setLayout(new BorderLayout());
		 CFieldCards= new JPanel();
			CFieldCards.setPreferredSize(new Dimension(700,view.getCHeroField().getHeight()));
			view.getCHeroField().add(CFieldCards,BorderLayout.CENTER);
			CFieldCards.setOpaque(false);
			OFieldCards= new JPanel();
			OFieldCards.setPreferredSize(new Dimension(700,view.getOHeroField().getHeight()));
			view.getOHeroField().add(OFieldCards,BorderLayout.CENTER);
			OFieldCards.setOpaque(false);
		Icon confirmT = new ImageIcon("Images/Choose.png");
	     Confirm1 = new JButton(confirmT);
		 Confirm1.setMaximumSize(new Dimension(100,70));
		 view.getCHeroField().add(Confirm1,BorderLayout.WEST);
		 Confirm1.setVisible(false);
		 Confirm2 = new JButton(confirmT);
		 Confirm2.setMaximumSize(new Dimension(100,70));
		 view.getOHeroField().add(Confirm2,BorderLayout.EAST);
		 Confirm2.setVisible(false);
		 view.repaint();
		 view.revalidate();
		 view.getCHero().addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				targetH = view.getPl1();
				targetM = null;
				
			}});
		view.getOHero().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				targetH = view.getPl2();
				targetM = null;
				
			}});
		 Confirm1.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(Pow){
						try { if(model.getCurrentHero() instanceof Mage){
								if(targetH != null){
								((Mage)view.getPl1()).useHeroPower(targetH);
								useHP();}
							else if(targetM != null){
								((Mage)view.getPl1()).useHeroPower(targetM); }
								 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals()); 
								 H2Info.setText("This is Your Not Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
								 useHP();
						}
						else if(model.getCurrentHero() instanceof Priest){
							if(targetH != null){ 
							((Priest)view.getPl1()).useHeroPower(targetH);
							useHP();}
						else if(targetM != null){
							((Priest)view.getPl1()).useHeroPower(targetM); }
							 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals()); 
							 H2Info.setText("This is Your Not Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
							 useHP();
						}
						} catch (NotYourTurnException e1) {
							exception(e1);
						}
						catch (CloneNotSupportedException  e){
							e.printStackTrace();
						}
						catch(FullFieldException ff){
							fullField(ff);
						}
						catch(HeroPowerAlreadyUsedException h){
							attackedH(h);
						}
						catch(NotEnoughManaException n){
							noMana(n);
						}
						catch (FullHandException f){
							fullHand(f);
						}
						targetH = null;
						targetM = null;
						Confirm1.setVisible(false);
						Selected = null;
						Pow = false;
						updateMinions();
					}
						else if (Selected instanceof Spell){
							if(Selected instanceof MinionTargetSpell && targetM != null){
								try {
									int r = model.getCurrentHero().getHand().indexOf(Selected);
									model.getCurrentHero().castSpell((MinionTargetSpell) Selected, targetM);
									P1Hand.get(r).setVisible(false);
									P1Hand.remove(r);  
									updateMinions();
									updateHeros();
								} catch (NotYourTurnException e) {
									exception(e);
								}
								catch (InvalidTargetException i){
									invalidT(i);
								}
								catch(NotEnoughManaException n){
									noMana(n);
								}
				}
							else if (Selected instanceof LeechingSpell && targetM != null){
								try {
									int r = model.getCurrentHero().getHand().indexOf(Selected);
									model.getCurrentHero().castSpell((LeechingSpell) Selected, targetM);
									P1Hand.get(r).setVisible(false);
									P1Hand.remove(r); 
									updateMinions();
									updateHeros();
								} catch (NotYourTurnException e) {
									exception(e);
								}
								catch (NotEnoughManaException n){
									noMana(n);
								}
							}else if (Selected instanceof HeroTargetSpell && targetH != null){
								try {
									model.getCurrentHero().castSpell((HeroTargetSpell) Selected, targetH); 
									updateHeros();
								} catch (NotYourTurnException e) {
									exception(e);
								}
								catch (NotEnoughManaException n){
									noMana(n);
								}
								
							}
							targetH = null;
							targetM = null;
							Selected = null;
							Confirm1.setVisible(false);
							updateMinions();
						}else if (Selected instanceof Minion){ 
							if(targetM != null)
								try {
									model.getCurrentHero().attackWithMinion((Minion) Selected, targetM);
									updateMinions();
								} catch (NotYourTurnException e) {
									exception(e);
								}
							catch (NotSummonedException  e1){
								e1.printStackTrace();
							}
							catch (CannotAttackException c){
								if(((Minion) Selected).isSleeping()){
									minionSleep(c);
								}
								else{
								attackedM(c);
								}
							}
							catch (TauntBypassException t){
								taunt(t);
							}
							catch (InvalidTargetException i){
								invalidT(i);
							}
							else if(targetH != null)
								try {
									model.getCurrentHero().attackWithMinion((Minion)Selected, targetH);
									updateHeros();
								} catch (NotYourTurnException e) {
									exception(e);
								}
							catch (NotSummonedException e1){
								e1.printStackTrace();
							}
							catch (CannotAttackException c){
								if(((Minion) Selected).isSleeping()){
									minionSleep(c);
								}
								else{
								attackedM(c);
								}
							}
							catch (TauntBypassException t){
								taunt(t);
							}
							catch (InvalidTargetException i){
								invalidT(i);
							}
							Confirm1.setVisible(false);
							targetM = null;
							targetH = null;
							Selected = null;
							updateMinions();
						}
				}});
		view.getCHeroPower().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((view.getPl1() instanceof Mage || view.getPl1() instanceof Priest) && view.getPl1().equals(model.getCurrentHero())){
					Confirm1.setVisible(true);
					Pow = true;
				}else{
				 try {
					view.getPl1().useHeroPower();
					useHP();
						
				} catch (NotYourTurnException e) {
					exception(e);
				}
				 catch (CloneNotSupportedException  e1){
						e1.printStackTrace();
					}
				 catch(FullFieldException ff){
						fullField(ff);
					}
				 catch(HeroPowerAlreadyUsedException h){
						attackedH(h);
					}
				 catch(NotEnoughManaException n){
					 noMana(n);
				 }
				 catch(FullHandException f){
					 fullHand(f);
				 }
				}
			}});
		 Confirm2.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(Pow){
						try { if(model.getCurrentHero() instanceof Mage){
								if(targetH != null){ 
									((Mage)view.getPl2()).useHeroPower(targetH);
									useHP();}
							else if(targetM != null){
								((Mage)view.getPl2()).useHeroPower(targetM); }
								 H1Info.setText("This is Your Not Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals()); 
								 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
								 useHP();
						}
						else if(model.getCurrentHero() instanceof Priest){
							if(targetH != null){ 
							((Priest)view.getPl2()).useHeroPower(targetH);
							useHP();}
						else if(targetM != null){
							((Priest)view.getPl2()).useHeroPower(targetM); }
							 H1Info.setText("This is Your Not Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals()); 
							 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
							 useHP();
						}
						} catch (NotYourTurnException e1) {
							exception(e1);
						}
						catch (CloneNotSupportedException  e){
							e.printStackTrace();
						}
						catch(FullFieldException ff){
							fullField(ff);
						}
						catch(HeroPowerAlreadyUsedException h){
							attackedH(h);
						}
						catch(NotEnoughManaException n){
							noMana(n);
						}
						catch (FullHandException f){
							fullHand(f);
						}
						targetH = null;
						targetM = null;
						Selected = null;
						Confirm2.setVisible(false);
						Pow = false;
						updateMinions();
					}
					else if (Selected instanceof Spell){
						if(Selected instanceof MinionTargetSpell && targetM != null){
							try {
								int r = model.getCurrentHero().getHand().indexOf(Selected);
								model.getCurrentHero().castSpell((MinionTargetSpell) Selected, targetM);
								P2Hand.get(r).setVisible(false);
								P2Hand.remove(r); 
								updateMinions();
								updateHeros();
							} catch (NotYourTurnException e) {
								exception(e);
							}
							catch (InvalidTargetException i){
								invalidT(i);
							}
							catch(NotEnoughManaException n){
								noMana(n);
							}
						}
						else if (Selected instanceof LeechingSpell && targetM != null){
							try {
								int r = model.getCurrentHero().getHand().indexOf(Selected);
								model.getCurrentHero().castSpell((LeechingSpell) Selected, targetM);
								P2Hand.get(r).setVisible(false);
								P2Hand.remove(r);
								updateMinions();
								updateHeros();
							} catch (NotYourTurnException e) {
								exception(e);
							}
							catch(NotEnoughManaException n){
								noMana(n);
							}
						}else if (Selected instanceof HeroTargetSpell && targetH != null){
							try {
								int r = model.getCurrentHero().getHand().indexOf(Selected);
								model.getCurrentHero().castSpell((HeroTargetSpell) Selected, targetH);
								P2Hand.get(r).setVisible(false);
								P2Hand.remove(r);
 								updateHeros();
							} catch (NotYourTurnException e) {
								exception(e);
							}
							catch(NotEnoughManaException n){
								noMana(n);
							}
						}
						targetH = null;
						targetM = null;
						Selected = null;
						Confirm2.setVisible(false);
						updateMinions();
					}
					else if (Selected instanceof Minion){ 
						if(targetM != null)
							try {
								model.getCurrentHero().attackWithMinion((Minion) Selected, targetM);
								updateMinions(); 
							} catch (NotYourTurnException e) {
								exception(e);
							}
						catch (NotSummonedException  e1){
							e1.printStackTrace();
						}
						catch (CannotAttackException c){
							if(((Minion) Selected).isSleeping()){
								minionSleep(c);
							}
							else{
							attackedM(c);
							}
						}
						catch (TauntBypassException t){
							taunt(t);
						}
						catch (InvalidTargetException i){
							invalidT(i);
						}
						else if(targetH != null)
							try {
								model.getCurrentHero().attackWithMinion((Minion)Selected, targetH);
								updateHeros();
							} catch (NotYourTurnException e) {
								exception(e);
							}
						catch (NotSummonedException e1){
							e1.printStackTrace();
						}
						catch (CannotAttackException c){
							if(((Minion) Selected).isSleeping()){
								minionSleep(c);
							}
							else{
							attackedM(c);
							}
						}
						catch (TauntBypassException t){
							taunt(t);
						}
						catch (InvalidTargetException i){
							invalidT(i);
						}
						Confirm2.setVisible(false);
						targetM = null;
						targetH = null;
						Selected = null;
						updateMinions();
					}
				}});
		view.getOHeroPower().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((view.getPl2() instanceof Mage || view.getPl2() instanceof Priest) && view.getPl2().equals(model.getCurrentHero())){
					Confirm2.setVisible(true);
					Pow = true;
				}else{
				 try {
					view.getPl2().useHeroPower();
					useHP();
				} catch (NotYourTurnException e) {
					exception(e);
				}
				 catch (CloneNotSupportedException  e1){
						e1.printStackTrace();
					}
				 catch(FullFieldException ff){
						fullField(ff);
					}
				 catch(HeroPowerAlreadyUsedException h){
						attackedH(h);
					}
				 catch(NotEnoughManaException n){
					 noMana(n);
				 }
				 catch(FullHandException f){
					 fullHand(f);
				 }
				}
			}});
		EndTurn1.setPreferredSize(new Dimension(100,40));
		EndTurn2.setPreferredSize(new Dimension(100,40));
	//	EndTurn1.setBackground(Color.GRAY);
	//	EndTurn2.setBackground(Color.GRAY);
		EndTurn1.setOpaque(false);
		EndTurn2.setOpaque(false);
		Start.setVisible(false);
		view.getCHeroField().add(EndTurn1,BorderLayout.EAST);
		view.getOHeroField().add(EndTurn2,BorderLayout.WEST);
		view.repaint();
		view.revalidate();
		EndTurn1.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					model.endTurn();
					
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
				catch(FullHandException f){
					fullHand(f);
				}
			}});
		EndTurn2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					model.endTurn();
					 
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
				catch(FullHandException f){
					fullHand(f);
				}
				 
			}});
		/*EndTurn1.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        EndTurn1.setBackground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        EndTurn1.setBackground(Color.GRAY);
		    }
		});
		EndTurn2.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        EndTurn2.setBackground(Color.GREEN);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        EndTurn2.setBackground(Color.GRAY);
		    }
		});*/
				Deck1 = new JButton(""+ view.getPl1().getDeck().size());
				Deck2 = new JButton(""+ view.getPl2().getDeck().size());
				view.getH1().add(Deck1,BorderLayout.EAST);
				view.getH2().add(Deck2);
				view.getH1().add(Burned1);
				view.getH2().add(Burned2);
				Burned1.setVisible(false);
				Burned1.setPreferredSize(new Dimension(110,140));
				Burned2.setVisible(false);
				Burned2.setPreferredSize(new Dimension(110,140));
				
				if(model.getCurrentHero().equals(view.getPl1())){
					H1Info.setText("This is Your Turn.\nYour Hero HP: "+ model.getCurrentHero().getCurrentHP()+".\nYour Mana Crystals: "+model.getCurrentHero().getCurrentManaCrystals());
					H2Info.setText("This is Not Your Turn.\nYour Hero HP: "+ model.getOpponent().getCurrentHP()+".\nYour Mana Crystals: "+model.getOpponent().getCurrentManaCrystals());
					EndTurn1.setVisible(true);
					EndTurn2.setVisible(false);
					for(int i=0;i<3;i++){
						JButton B1 = new JButton(addIm(model.getCurrentHero().getHand().get(i)));
						B1.setPreferredSize(new Dimension(110,140));
						P1Hand.add(B1);
						P1Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());
						P1Hand.get(i).setToolTipText(getInfo(model.getCurrentHero().getHand().get(i)));
						view.getCHCards().add(B1,BorderLayout.LINE_START);
						B1.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								JButton b = (JButton) e.getSource();
					//			if (model.getCurrentHero().equals(view.getPl1())){
									int r= P1Hand.indexOf(b);
									Selected = (Card) view.getPl1().getHand().get(r);
									if(Selected instanceof Minion)
										try {
											view.getPl1().playMinion((Minion)Selected);
											try {
												playSound("Audio/playcard.wav");
											} catch (UnsupportedAudioFileException
													| IOException
													| LineUnavailableException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											P1Hand.get(r).setVisible(false);
											JButton b2 = new JButton(B1.getIcon());
											b2.setPreferredSize(new Dimension(110,140));
											b2.setText(view.getPl1().getField().get(view.getPl1().getField().size()-1).getName());
											b2.addActionListener(new ActionListener(){

												@Override
												public void actionPerformed(
														ActionEvent s) {
													JButton h = (JButton) s.getSource();
													if (Selected == null && !Pow){
														if(P1Field.contains(h) && view.getPl1().equals(model.getCurrentHero())){
															Selected = view.getPl1().getField().get(P1Field.indexOf(h));
															Confirm1.setVisible(true);
														}
														if(P2Field.contains(h) && view.getPl2().equals(model.getCurrentHero())){
															Selected = view.getPl2().getField().get(P2Field.indexOf(h));
															Confirm2.setVisible(true);
														}
						}
													else
								if(P1Field.contains(h)){
								targetM = view.getPl1().getField().get(P1Field.indexOf(h));
								targetH = null;
														}
														if(P2Field.contains(h)){
															targetM = view.getPl2().getField().get(P2Field.indexOf(h));
															targetH = null;
														}
													
												}});
											P1Hand.remove(r);
											P1Field.add(b2);
											CFieldCards.add(b2);
											Selected = null;
											updateHeros();
											b2.setToolTipText(UpdateInfo(model.getCurrentHero().getField().get(model.getCurrentHero().getField().size()-1)));
										} catch (NotYourTurnException e1) {
											exception(e1);
										}
									catch(FullFieldException ff){
										fullField(ff);
									}
									catch(NotEnoughManaException n){
										noMana(n);
									}
									if(Selected instanceof Spell)
										CastingSpell(Selected);
				//				}
							}});
					}
					for(int i=0;i<4;i++){
						JButton B1 = new JButton(addIm(model.getOpponent().getHand().get(i)));
						B1.setPreferredSize(new Dimension(110,140));
						P2Hand.add(B1);
						view.getOHCards().add(B1,BorderLayout.LINE_START);
						B1.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								JButton b = (JButton) e.getSource();
					//			if (model.getCurrentHero().equals(view.getPl1())){
									int r= P2Hand.indexOf(b);
									Selected = (Card) view.getPl2().getHand().get(r);
									if(Selected instanceof Minion)
										try {
											view.getPl2().playMinion((Minion)Selected);
											try {
												playSound("Audio/playcard.wav");
											} catch (UnsupportedAudioFileException
													| IOException
													| LineUnavailableException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											P2Hand.get(r).setVisible(false);
											JButton b2 = new JButton(B1.getIcon());
											b2.setPreferredSize(new Dimension(110,140));
											b2.setText(view.getPl2().getField().get(view.getPl2().getField().size()-1).getName());
											b2.addActionListener(new ActionListener(){

												@Override
												public void actionPerformed(
														ActionEvent s) {
													JButton h = (JButton) s.getSource();
													if (Selected == null && !Pow){
														if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
															Selected = view.getPl1().getField().get(P1Field.indexOf(h));
															Confirm1.setVisible(true);
														}
														if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
															Selected = view.getPl2().getField().get(P2Field.indexOf(h));
															Confirm2.setVisible(true);
														}
													}
													else
														if(P1Field.contains(h)){
															targetM = view.getPl1().getField().get(P1Field.indexOf(h));
															targetH = null;
														}
														if(P2Field.contains(h)){
															targetM = view.getPl2().getField().get(P2Field.indexOf(h));
															targetH = null;
														}
													
												}});
											P2Hand.remove(r);
											P2Field.add(b2);
											OFieldCards.add(b2);
											Selected = null;
											updateHeros();
											b2.setToolTipText(UpdateInfo(model.getCurrentHero().getField().get(model.getCurrentHero().getField().size()-1)));
										} catch (NotYourTurnException e1) {
											exception(e1);
										}
									catch(FullFieldException ff){
										fullField(ff);
									}
										catch(NotEnoughManaException n){
											noMana(n);
										}
									if(Selected instanceof Spell)
										CastingSpell(Selected);
				//				}
							}});
						}
					view.revalidate();
					view.repaint();
				}
				else if(model.getCurrentHero().equals(view.getPl2())){
					H1Info.setText("This is Not Your Turn.\nYour Hero HP: "+ model.getOpponent().getCurrentHP()+".\nYour Mana Crystals: "+model.getOpponent().getCurrentManaCrystals());
					H2Info.setText("This is Your Turn.\nYour Hero HP: "+ model.getCurrentHero().getCurrentHP()+".\nYour Mana Crystals: "+model.getCurrentHero().getCurrentManaCrystals());
					for(int i=0;i<3;i++)
					{
						JButton B1 = new JButton(addIm(model.getCurrentHero().getHand().get(i)));
						B1.setPreferredSize(new Dimension(110,140));
						P2Hand.add(B1);
						P2Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());
						P2Hand.get(i).setToolTipText(getInfo(model.getCurrentHero().getHand().get(i)));
						view.getOHCards().add(B1,BorderLayout.LINE_START);
						B1.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									JButton b = (JButton) e.getSource();
		//							if (model.getCurrentHero().equals(view.getPl2())){
										int r= P2Hand.indexOf(b);
										Selected = (Card) view.getPl2().getHand().get(r);
										if(Selected instanceof Minion)
											try {
												view.getPl2().playMinion((Minion)Selected);
												try {
													playSound("Audio/playcard.wav");
												} catch (UnsupportedAudioFileException
														| IOException
														| LineUnavailableException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												P2Hand.get(r).setVisible(false);
												JButton b2 = new JButton(B1.getIcon());
												b2.setPreferredSize(new Dimension(110,140));
												b2.setText(view.getPl2().getField().get(view.getPl2().getField().size()-1).getName());
												b2.addActionListener(new ActionListener(){

													@Override
													public void actionPerformed(
															ActionEvent s) {
														JButton h = (JButton) s.getSource();
														if (Selected == null && !Pow){
															if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
																Selected = view.getPl1().getField().get(P1Field.indexOf(h));
																Confirm1.setVisible(true);
															}
															if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
																Selected = view.getPl2().getField().get(P2Field.indexOf(h));
																Confirm2.setVisible(true);
															}
														}
														else
															if(P1Field.contains(h)){
																targetM = view.getPl1().getField().get(P1Field.indexOf(h));
																targetH = null;
															}
															if(P2Field.contains(h)){
																targetM = view.getPl2().getField().get(P2Field.indexOf(h));
																targetH = null;
															}
														
													}});
												P2Hand.remove(r);
												P2Field.add(b2);
												OFieldCards.add(b2);
												Selected = null;
												updateHeros();
												b2.setToolTipText(UpdateInfo(model.getCurrentHero().getField().get(model.getCurrentHero().getField().size()-1)));
											} catch (NotYourTurnException e1) {
												exception(e1);
											}
										catch(FullFieldException ff){
											fullField(ff);
										}
										catch (NotEnoughManaException n){
											noMana(n);
										}
										if(Selected instanceof Spell)
											CastingSpell(Selected);
			//						}
								}});
					}
					for(int i=0;i<4;i++){
						JButton B1 = new JButton(addIm(model.getOpponent().getHand().get(i)));
						B1.setPreferredSize(new Dimension(110,140));
						P1Hand.add(B1);
						view.getCHCards().add(B1,BorderLayout.LINE_START);
						B1.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								JButton b = (JButton) e.getSource();
					//			if (model.getCurrentHero().equals(view.getPl1())){
									int r= P1Hand.indexOf(b);
									Selected = (Card) view.getPl1().getHand().get(r);
									if(Selected instanceof Minion)
										try {
											view.getPl1().playMinion((Minion)Selected);
											try {
												playSound("Audio/playcard.wav");
											} catch (UnsupportedAudioFileException
													| IOException
													| LineUnavailableException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											P1Hand.get(r).setVisible(false);
											JButton b2 = new JButton(B1.getIcon());
											b2.setPreferredSize(new Dimension(110,140));
											b2.setText(view.getPl1().getField().get(view.getPl1().getField().size()-1).getName());
											b2.addActionListener(new ActionListener(){

												@Override
												public void actionPerformed(
														ActionEvent s) {
													JButton h = (JButton) s.getSource();
													if (Selected == null && !Pow){
														if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
															Selected = view.getPl1().getField().get(P1Field.indexOf(h));
															Confirm1.setVisible(true);
														}
														if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
															Selected = view.getPl2().getField().get(P2Field.indexOf(h));
															Confirm2.setVisible(true);
														}
													}
													else
														if(P1Field.contains(h)){
															targetM = view.getPl1().getField().get(P1Field.indexOf(h));
															targetH = null;
														}
														if(P2Field.contains(h)){
															targetM = view.getPl2().getField().get(P2Field.indexOf(h));
															targetH = null;
														}
													
												}});
											P1Hand.remove(r);
											P1Field.add(b2);
											CFieldCards.add(b2);
											updateHeros();
											Selected = null;
											b2.setToolTipText(UpdateInfo(model.getCurrentHero().getField().get(model.getCurrentHero().getField().size()-1)));
										} catch (NotYourTurnException e1) {
											exception(e1);
										}
									catch(FullFieldException ff){
										fullField(ff);
									}
										catch(NotEnoughManaException n){
											noMana(n);
										}
									if(Selected instanceof Spell)
										CastingSpell(Selected);
				//				}
							}});
					}
					EndTurn2.setVisible(true);
					EndTurn1.setVisible(false);
					view.revalidate();
					view.repaint();
				}
				view.getReadyP1().setVisible(false);
				view.getReadyP2().setVisible(false);
		}
	}
	@SuppressWarnings("serial")
	@Override
	public void onGameOver() {
		Icon hunter = new ImageIcon("Images/Rexxar2.png");
		Icon mage = new ImageIcon("Images/Jaina Proudmoore2.png");
		Icon paladin = new ImageIcon("Images/Uther Lightbringer2.png");
		Icon warlock = new ImageIcon("Images/Gul'dan2.png");
		Icon priest = new ImageIcon("Images/Anduin Wrynn2.png");
			if(view.getPl1().getCurrentHP()==0 || view.getPl2().getCurrentHP()==0){
			view.getCHCards().setVisible(false);
			view.getOHCards().setVisible(false);
			view.getOHeroField().setVisible(false);
			view.getCHeroField().setVisible(false);
			view.getH1().setVisible(false);
			view.getH2().setVisible(false);
			view.setLayout(new BorderLayout());
			gameOver = new JPanel(){
				public void paintComponent(Graphics g){
					Image victory = Toolkit.getDefaultToolkit().getImage("Images/victory.png");
					g.drawImage(victory, 0, 0, view.getWidth(), view.getHeight(), view);
				}
			};
			view.setContentPane(gameOver);
			gameOver.setVisible(true);
			try {
				playSound("Audio/victory.wav");
			} catch (UnsupportedAudioFileException | IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(view.getPl1().getCurrentHP()==0 && view.getPl2().getCurrentHP()!=0){
				JButton b = new JButton("Congratulations Player 2: "+view.getPl2().getName());
				b.setBackground(Color.GRAY);
				gameOver.add(b);
				view.getOHero().setPreferredSize(new Dimension(200,250));
				view.getOHero().setOpaque(false);
				if(view.getPl2() instanceof Hunter){
					view.getOHero().setIcon(hunter);
				}
				else if(view.getPl2() instanceof Mage){
					view.getOHero().setIcon(mage);
				}
				else if(view.getPl2() instanceof Paladin){
					view.getOHero().setIcon(paladin);
				}
				else if(view.getPl2() instanceof Warlock){
					view.getOHero().setIcon(warlock);
				}
				else{
					view.getOHero().setIcon(priest);
				}
				gameOver.add(view.getOHero());
			
		}
		if(view.getPl1().getCurrentHP()!=0 && view.getPl2().getCurrentHP()==0){
			
				JButton b = new JButton("Congratulations Player 1: "+view.getPl1().getName());
				b.setBackground(Color.GRAY);
				gameOver.add(b);
				view.getCHero().setPreferredSize(new Dimension(200,250));
				view.getCHero().setOpaque(false);
				if(view.getPl1() instanceof Hunter){
					view.getCHero().setIcon(hunter);
				}
				else if(view.getPl1() instanceof Mage){
					view.getCHero().setIcon(mage);
				}
				else if(view.getPl1() instanceof Paladin){
					view.getCHero().setIcon(paladin);
				}
				else if(view.getPl1() instanceof Warlock){
					view.getCHero().setIcon(warlock);
				}
				else{
					view.getCHero().setIcon(priest);
				}
				gameOver.add(view.getCHero());
		}
		Icon Quit = new ImageIcon("Images/QuitB.png");
		JButton quit = new JButton(Quit);
		quit.setPreferredSize(new Dimension(150,89));
		quit.setBackground(Color.GRAY);
		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					view.dispose();
			}
		});
		gameOver.add(quit);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		 
	}
	
	public static void main(String[] args) throws FullHandException, CloneNotSupportedException, IOException{
		new Controler();
	}
	@Override
	public void onEndTurn() {
		try {
			playSound("Audio/Endturn.wav");
		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 if(model.getCurrentHero().equals(view.getPl1())){
			 for(int i=0;i<model.getOpponent().getHand().size();i++){
				 P2Hand.get(i).setText("");
			 	 P2Hand.get(i).setIcon(null);
			 }
			 while(model.getCurrentHero().getHand().size()!=P1Hand.size()){
				 JButton Cardt = new JButton(addIm(model.getCurrentHero().getHand().get(0)));
				 Cardt.setPreferredSize(new Dimension(110,140));
				 P1Hand.add(Cardt);
				 view.getCHCards().add(Cardt);
				 Cardt.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							JButton b = (JButton) e.getSource();
							//if (model.getCurrentHero().equals(view.getPl1())){
								int r= P1Hand.indexOf(b);
								Selected = (Card) view.getPl1().getHand().get(r);
								if(Selected instanceof Minion)
									try {
										view.getPl1().playMinion((Minion)Selected);
										try {
											playSound("Audio/playcard.wav");
										} catch (UnsupportedAudioFileException
												| IOException
												| LineUnavailableException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										P1Hand.get(r).setVisible(false);
										JButton b2 = new JButton(Cardt.getIcon());
										b2.setPreferredSize(new Dimension(110,140));
										b2.setText(view.getPl1().getField().get(view.getPl1().getField().size()-1).getName());
										b2.addActionListener(new ActionListener(){

											@Override
											public void actionPerformed(
													ActionEvent s) {
												JButton h = (JButton) s.getSource();
												if (Selected == null && !Pow){
													if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
														Selected = view.getPl1().getField().get(P1Field.indexOf(h));
														Confirm1.setVisible(true);
													}
													if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
														Selected = view.getPl2().getField().get(P2Field.indexOf(h));
														Confirm2.setVisible(true);
													}
												}
												else
													if(P1Field.contains(h)){
														targetM = view.getPl1().getField().get(P1Field.indexOf(h));
														targetH = null;
													}
													if(P2Field.contains(h)){
														targetM = view.getPl2().getField().get(P2Field.indexOf(h));
														targetH = null;
													}
												
											}});
										P1Hand.remove(r);
										P1Field.add(b2);
										CFieldCards.add(b2);
										Selected = null;
										updateHeros();
										updateMinions();
									} catch (NotYourTurnException e1) {
										exception(e1);
									}
								catch(FullFieldException ff){
									fullField(ff);
								}
									catch(NotEnoughManaException n){
										noMana(n);
									}
								if(Selected instanceof Spell)
									CastingSpell(Selected);
							//}
						}});
			 }
			 for(int i=0;i<model.getCurrentHero().getHand().size();i++){
				 P1Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());
				 P1Hand.get(i).setToolTipText(getInfo(model.getCurrentHero().getHand().get(i)));
				 P1Hand.get(i).setIcon(addIm(model.getCurrentHero().getHand().get(i)));
			 }
			 for(int i =0;i<P2Hand.size();i++){
				 P2Hand.get(i).setToolTipText(null);
			 }
			for(int i =0;i<P1Field.size();i++){
				P1Field.get(i).setToolTipText(UpdateInfo(view.getPl1().getField().get(i)));
			}
			for(int i =0;i<P2Field.size();i++){
				P2Field.get(i).setToolTipText(UpdateInfo(view.getPl2().getField().get(i)));
			}
			 EndTurn1.setVisible(true);
			 EndTurn2.setVisible(false);
			 Deck1.setText(""+model.getCurrentHero().getDeck().size());
			 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 H2Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());	
			 Burned2.setVisible(false);
		 }
		 else if(model.getCurrentHero().equals(view.getPl2())){
			 for(int i=0;i<P1Hand.size();i++){
				 P1Hand.get(i).setText("");
				 P1Hand.get(i).setIcon(null);
			 }
			 while(model.getCurrentHero().getHand().size()!=P2Hand.size()){
				 JButton Cardt = new JButton(addIm(model.getOpponent().getHand().get(0)));
				 Cardt.setPreferredSize(new Dimension(110,140));
				 P2Hand.add(Cardt);
				 view.getOHCards().add(Cardt);
				 Cardt.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							JButton b = (JButton) e.getSource();
							//if (model.getCurrentHero().equals(view.getPl2())){
								int r= P2Hand.indexOf(b);
								Selected = (Card) view.getPl2().getHand().get(r);
								if(Selected instanceof Minion)
									try {
										view.getPl2().playMinion((Minion)Selected);
										try {
											playSound("Audio/playcard.wav");
										} catch (UnsupportedAudioFileException
												| IOException
												| LineUnavailableException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										P2Hand.get(r).setVisible(false);
										JButton b2 = new JButton(Cardt.getIcon());
										b2.setPreferredSize(new Dimension(110,140));
										b2.setText(view.getPl2().getField().get(view.getPl2().getField().size()-1).getName());
										P2Hand.remove(r);
										b2.addActionListener(new ActionListener(){

											@Override
											public void actionPerformed(
													ActionEvent s) {
												JButton h = (JButton) s.getSource();
												if (Selected == null && !Pow){
													if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
														Selected = view.getPl1().getField().get(P1Field.indexOf(h));
														Confirm1.setVisible(true);
													}
													if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
														Selected = view.getPl2().getField().get(P2Field.indexOf(h));
														Confirm2.setVisible(true);
													}
												}
												else
													if(P1Field.contains(h)){
														targetM = view.getPl1().getField().get(P1Field.indexOf(h));
														targetH = null;
													}
													if(P2Field.contains(h)){
														targetM = view.getPl2().getField().get(P2Field.indexOf(h));
														targetH = null;
													}
												
											}});
										P2Field.add(b2);
										OFieldCards.add(b2);
										updateHeros();
										updateMinions();
										Selected = null;
									} catch (NotYourTurnException e1) {
										exception(e1);
									}
								catch(FullFieldException ff){
									fullField(ff);
								}
								catch(NotEnoughManaException n){
									noMana(n);
								}
								if(Selected instanceof Spell)
									CastingSpell(Selected);
						//	}
						}});
			 }
			 for(int i=0;i<P2Hand.size();i++){
				 P2Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());
				 P2Hand.get(i).setToolTipText(getInfo(model.getCurrentHero().getHand().get(i)));
				 P2Hand.get(i).setIcon(addIm(model.getCurrentHero().getHand().get(i)));
			 }
			 for(int i =0;i<P1Hand.size();i++){
				 P1Hand.get(i).setToolTipText(null);
			 }
			 for(int i =0;i<P1Field.size();i++){
					P1Field.get(i).setToolTipText(UpdateInfo(view.getPl1().getField().get(i)));
				}
			for(int i =0;i<P2Field.size();i++){
					P2Field.get(i).setToolTipText(UpdateInfo(view.getPl2().getField().get(i)));
				}
			 EndTurn1.setVisible(false);
			 EndTurn2.setVisible(true);
			 Deck2.setText(""+model.getCurrentHero().getDeck().size());
			 H1Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());	
			 Burned1.setVisible(false);
		 }
		 Confirm1.setVisible(false);
		 Confirm2.setVisible(false);
		 Selected = null;
		 targetH = null;
		 targetM = null;
		
	}
	@Override
	public void onDamageOpponent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMinionDeath(Minion m) {
		if(view.getPl1().getField().contains(m)){
			int r =view.getPl1().getField().indexOf(m);
			view.getPl1().getField().remove(m);
			P1Field.get(r).setVisible(false);
			P1Field.remove(r);
		}
		if(view.getPl2().getField().contains(m)){
			int r =view.getPl2().getField().indexOf(m);
			view.getPl2().getField().remove(m);
			P2Field.get(r).setVisible(false);
			P2Field.remove(r);
		}
	}
	@Override
	public void onUseHeroPower() {
		 if(view.getPl1().equals(model.getCurrentHero()) && view.getPl1() instanceof Hunter){
			 H2Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());
			 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 useHP();
		 }
		 if(view.getPl2().equals(model.getCurrentHero()) && view.getPl2() instanceof Hunter){
			 H1Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());
			 useHP();
		 }
		 if(view.getPl1().equals(model.getCurrentHero()) && view.getPl1() instanceof Warlock){
			 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 while(model.getCurrentHero().getHand().size()!=P1Hand.size()){
			 JButton Cardt = new JButton(addIm(model.getCurrentHero().getHand().get(1)));
			 Cardt.setPreferredSize(new Dimension(110,140));
			 P1Hand.add(Cardt);
			 view.getCHCards().add(Cardt);
			 useHP();
			 Cardt.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						JButton b = (JButton) e.getSource();
			//			if (model.getCurrentHero().equals(view.getPl1())){
							int r= P1Hand.indexOf(b);
							Selected = (Card) view.getPl1().getHand().get(r);
							if(Selected instanceof Minion)
								try {
									view.getPl1().playMinion((Minion)Selected);
									try {
										playSound("Audio/playcard.wav");
									} catch (UnsupportedAudioFileException
											| IOException
											| LineUnavailableException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										playSound("Audio/playcard.wav");
									} catch (UnsupportedAudioFileException
											| IOException
											| LineUnavailableException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									P1Hand.get(r).setVisible(false);
									JButton b2 = new JButton(Cardt.getIcon());
									b2.setPreferredSize(new Dimension(110,140));
									b2.setText(view.getPl1().getField().get(view.getPl1().getField().size()-1).getName());
									b2.addActionListener(new ActionListener(){

										@Override
										public void actionPerformed(
												ActionEvent s) {
											JButton h = (JButton) s.getSource();
											if (Selected == null && !Pow){
												if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
													Selected = view.getPl1().getField().get(P1Field.indexOf(h));
													Confirm1.setVisible(true);
												}
												if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
													Selected = view.getPl2().getField().get(P2Field.indexOf(h));
													Confirm2.setVisible(true);
												}
											}
											else
												if(P1Field.contains(h)){
													targetM = view.getPl1().getField().get(P1Field.indexOf(h));
													targetH = null;
												}
												if(P2Field.contains(h)){
													targetM = view.getPl2().getField().get(P2Field.indexOf(h));
													targetH = null;
												}
											
										}});
									P1Hand.remove(r);
									P1Field.add(b2);
									CFieldCards.add(b2);
									Selected = null;
									updateHeros(); 
									updateMinions();
								} catch (NotYourTurnException e1) {
									exception(e1);
								}
							catch(FullFieldException ff){
								fullField(ff);
							}
							catch(NotEnoughManaException n){
								noMana(n);
							}
							if(Selected instanceof Spell)
								CastingSpell(Selected);
		//				}
					}});
			 Cardt.setToolTipText(getInfo(view.getPl1().getHand().get(view.getPl1().getHand().size()-1)));
		 }
			 for(int i=0;i<P1Hand.size();i++)
				 P1Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());

			 Deck1.setText(""+model.getCurrentHero().getDeck().size());
		 }
		 if(view.getPl2().equals(model.getCurrentHero()) && view.getPl2() instanceof Warlock){
			 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
			 while(model.getCurrentHero().getHand().size()!=P2Hand.size()){
			 JButton Cardt = new JButton(addIm(model.getOpponent().getHand().get(1)));
			 Cardt.setPreferredSize(new Dimension(110,140));
			 P2Hand.add(Cardt);
			 view.getOHCards().add(Cardt);
			 useHP();
			 Cardt.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						JButton b = (JButton) e.getSource();
						//if (model.getCurrentHero().equals(view.getPl2())){
							int r= P2Hand.indexOf(b);
							Selected = (Card) view.getPl2().getHand().get(r);
							if(Selected instanceof Minion)
								try {
									view.getPl2().playMinion((Minion)Selected);
									try {
										playSound("Audio/playcard.wav");
									} catch (UnsupportedAudioFileException
											| IOException
											| LineUnavailableException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									P2Hand.get(r).setVisible(false);
									JButton b2 = new JButton(Cardt.getIcon());
									b2.setPreferredSize(new Dimension(110,140));
									b2.setText(view.getPl2().getField().get(view.getPl2().getField().size()-1).getName());
									b2.addActionListener(new ActionListener(){

										@Override
										public void actionPerformed(
												ActionEvent s) {
											JButton h = (JButton) s.getSource();
											if (Selected == null && !Pow){
												if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
													Selected = view.getPl1().getField().get(P1Field.indexOf(h));
													Confirm1.setVisible(true);
												}
												if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
													Selected = view.getPl2().getField().get(P2Field.indexOf(h));
													Confirm2.setVisible(true);
												}
											}
											else
												if(P1Field.contains(h)){
													targetM = view.getPl1().getField().get(P1Field.indexOf(h));
													targetH = null;
												}
												if(P2Field.contains(h)){
													targetM = view.getPl2().getField().get(P2Field.indexOf(h));
													targetH = null;
												}
											
										}});
									P2Hand.remove(r);
									P2Field.add(b2);
									OFieldCards.add(b2);
									Selected = null;
									updateHeros(); 
									updateMinions();
								} catch (NotYourTurnException e1) {
									exception(e1);
								}
							catch(FullFieldException ff){
								fullField(ff);
							}
							catch(NotEnoughManaException n){
								noMana(n);
							}
							if(Selected instanceof Spell)
								CastingSpell(Selected);
					//	}
					}});
			 Cardt.setToolTipText(getInfo(view.getPl2().getHand().get(view.getPl2().getHand().size()-1)));
		 }
			 for(int i=0;i<P2Hand.size();i++)
				 P2Hand.get(i).setText(model.getCurrentHero().getHand().get(i).getName());

			 Deck2.setText(""+model.getCurrentHero().getDeck().size());
		 }
		 if(view.getPl1().equals(model.getCurrentHero()) && view.getPl1() instanceof Paladin){
			 Icon shr = new ImageIcon("Images/Silver Hand Recruit.png");
			 H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			 while(P1Field.size() < model.getCurrentHero().getField().size()){
				 JButton b = new JButton(shr);
				 b.setPreferredSize(new Dimension(110,140));
				 P1Field.add(b);
				 CFieldCards.add(b,BorderLayout.CENTER);
				 useHP();
				 b.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(
								ActionEvent s) {
							JButton h = (JButton) s.getSource();
							if (Selected == null && !Pow){
								if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
									Selected = view.getPl1().getField().get(P1Field.indexOf(h));
									Confirm1.setVisible(true);
								}
								if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
									Selected = view.getPl2().getField().get(P2Field.indexOf(h));
									Confirm2.setVisible(true);
								}
							}
							else
								if(P1Field.contains(h)){
									targetM = view.getPl1().getField().get(P1Field.indexOf(h));
									targetH = null;
								}
								if(P2Field.contains(h)){
									targetM = view.getPl2().getField().get(P2Field.indexOf(h));
									targetH = null;
								}
							
						}});
		 }
			 updateMinions();
		 }	
		 if(view.getPl2().equals(model.getCurrentHero()) && view.getPl2() instanceof Paladin){
			 Icon shr = new ImageIcon("Images/Silver Hand Recruit.png");
			 H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals()); 
			 while(P2Field.size() < model.getCurrentHero().getField().size()){
				 JButton b = new JButton(shr);
				 b.setPreferredSize(new Dimension(110,140));
				 P2Field.add(b);
				 OFieldCards.add(b,BorderLayout.CENTER);
				 useHP();
				 b.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(
								ActionEvent s) {
							JButton h = (JButton) s.getSource();
							if (Selected == null && !Pow){
								if(P1Field.contains(h)&& view.getPl1().equals(model.getCurrentHero())){
									Selected = view.getPl1().getField().get(P1Field.indexOf(h));
									Confirm1.setVisible(true);
								}
								if(P2Field.contains(h)&& view.getPl2().equals(model.getCurrentHero())){
									Selected = view.getPl2().getField().get(P2Field.indexOf(h));
									Confirm2.setVisible(true);
								}
							}
							else
								if(P1Field.contains(h)){
									targetM = view.getPl1().getField().get(P1Field.indexOf(h));
									targetH = null;
								}
								if(P2Field.contains(h)){
									targetM = view.getPl2().getField().get(P2Field.indexOf(h));
									targetH = null;
								}
							
						}});
		 }
			 updateMinions();
		 }
	}
	public static String getInfo(Card c){
		String s ="<HTML>"+c.getName()+ " Rarity: "+ c.getRarity()+"<br>"+"Mana: "+c.getManaCost()+"";
		 if(c instanceof Minion){
			 Minion d = (Minion)c;
			 s += "\n HP: "+d.getCurrentHP()+ "\n Att: "+d.getAttack();
			 if(d.isTaunt())
				 s += " Taunt";
			if(d.isDivine())
				s += " Divine";
			if(! d.isSleeping())
				s += " Charge";
		 }
		 return s;
	}
	public static String UpdateInfo(Minion c){
		String s ="<HTML>"+c.getName()+"<br>"+"Att: "+c.getAttack()+"\n HP: "+c.getCurrentHP()+"<HTML>";
		 if(c.isDivine())
			 s += " Divine";
		 if(c.isTaunt())
			 s += " Taunt";
		 if(c.isSleeping())
			 s += " Sleeping";
		 if(! c.isSleeping())
			 s += " Active";
		 return s;
	}
	
	public void updateHeros(){
		if(model.getCurrentHero().equals(view.getPl1())){
			H1Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
			H2Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());
		 
		}
		else{
		H1Info.setText("This is Not Your Turn.\nYour Hero HP: "+ view.getPl1().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl1().getCurrentManaCrystals());
		H2Info.setText("This is Your Turn.\nYour Hero HP: "+ view.getPl2().getCurrentHP()+".\nYour Mana Crystals: "+view.getPl2().getCurrentManaCrystals());
	}
	}
	
	public void CastingSpell(Card c){
		if(c instanceof FieldSpell){
			try {
				int r = model.getCurrentHero().getHand().indexOf(Selected);
				model.getCurrentHero().castSpell((FieldSpell) c);
				if(model.getCurrentHero().equals(view.getPl1())){
					P1Hand.get(r).setVisible(false);
					P1Hand.remove(r);}
				else{
					P2Hand.get(r).setVisible(false);
					P2Hand.remove(r);}
				Selected = null;
				updateHeros();
			} catch (NotYourTurnException e) {
				exception(e);
			}
			catch(NotEnoughManaException n){
				noMana(n);
			}}
		if(c instanceof MinionTargetSpell){
			if(model.getCurrentHero().equals(view.getPl1()))
				Confirm1.setVisible(true);
			else
				Confirm2.setVisible(true);
	}
		if(c instanceof AOESpell){
			try {
				int r = model.getCurrentHero().getHand().indexOf(Selected);
				model.getCurrentHero().castSpell(((AOESpell)c), model.getOpponent().getField()); 
				if(model.getCurrentHero().equals(view.getPl1())){
					P1Hand.get(r).setVisible(false);
					P1Hand.remove(r);}
				else{
					P2Hand.get(r).setVisible(false);
					P2Hand.remove(r);}
				Selected = null;
				updateHeros();
			} catch (NotYourTurnException e) {
				exception(e);
			}
			catch(NotEnoughManaException n){
				noMana(n);
			}
		}
		if(c instanceof LeechingSpell){
			if(model.getCurrentHero().equals(view.getPl1()))
				Confirm1.setVisible(true);
			else
				Confirm2.setVisible(true);
	}
		if(c instanceof HeroTargetSpell){
			if(model.getCurrentHero().equals(view.getPl1()))
				Confirm1.setVisible(true);
			else
				Confirm2.setVisible(true);
		}
		updateMinions();
		}
			
	
	public void updateMinions(){
		for(int i =0;i<view.getPl1().getField().size();i++){
			P1Field.get(i).setText(view.getPl1().getField().get(i).getName());
			P1Field.get(i).setToolTipText(UpdateInfo(view.getPl1().getField().get(i)));
			}
		for(int i =0;i<view.getPl2().getField().size();i++){
			P2Field.get(i).setText(view.getPl2().getField().get(i).getName());
			P2Field.get(i).setToolTipText(UpdateInfo(view.getPl2().getField().get(i)));
			}
			
	}
	public void playSound(String soundName) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	 {
		 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
		    Clip clip = AudioSystem.getClip( );
		    clip.open(audioInputStream);
		    clip.start();
		if(soundName.equals("Audio/theme.wav")){
			clip.loop(20);
	   }
	   }
	static Icon c;
	public static Icon addIm(Card m){
		String s = m.getName();
		if(m.getName().equals("Shadow Word: Death")){
			c = new ImageIcon("Images/Shadow Word Death.png");
		}
		else{
		c = new ImageIcon("Images/"+s+".png");
		}
		return c;
	}
	public void attackedH(HeroPowerAlreadyUsedException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}
		}
	public void fullHand(FullHandException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
		if(model.getCurrentHero() instanceof Warlock){
			try {
				playSound("Excep/GuldanFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Mage){
			try {
				playSound("Excep/JainaFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Hunter){
			try {
				playSound("Excep/RexxarFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Priest){
			try {
				playSound("Excep/AnduinFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				playSound("Excep/UtherFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
		else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
		if(model.getOpponent() instanceof Warlock){
			try {
				playSound("Excep/GuldanFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Mage){
			try {
				playSound("Excep/JainaFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Hunter){
			try {
				playSound("Excep/RexxarFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Priest){
			try {
				playSound("Excep/AnduinFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				playSound("Excep/UtherFullHand.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		h.printStackTrace();
	}}
	public void noMana(NotEnoughManaException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
		if(model.getCurrentHero() instanceof Warlock){
			try {
				playSound("Excep/GuldanNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Mage){
			try {
				playSound("Excep/JainaNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Hunter){
			try {
				playSound("Excep/RexxarNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getCurrentHero() instanceof Priest){
			try {
				playSound("Excep/AnduinNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				playSound("Excep/UtherNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
		else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
		if(model.getOpponent() instanceof Warlock){
			try {
				playSound("Excep/GuldanNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Mage){
			try {
				playSound("Excep/JainaNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Hunter){
			try {
				playSound("Excep/RexxarNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(model.getOpponent() instanceof Priest){
			try {
				playSound("Excep/AnduinNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				playSound("Excep/UtherNoMana.wav");
			} catch (UnsupportedAudioFileException
					| IOException
					| LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		h.printStackTrace();
	}}
	public void taunt(TauntBypassException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMustAttackTaunt.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void invalidT(InvalidTargetException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherInvalidTarget.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void attackedM (CannotAttackException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMinionAttacked.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void fullField (FullFieldException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherFullField.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void minionSleep (CannotAttackException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherMinionSleeping.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void exception (NotYourTurnException h){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Excep/GuldanGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Excep/JainaGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Excep/RexxarGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Excep/AnduinGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Excep/GuldanGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Excep/JainaGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Excep/RexxarGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Excep/AnduinGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Excep/UtherGeneralException.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			h.printStackTrace();
		}}
	public void useHP (){
		if(model.getCurrentHero().equals(view.getPl1()) || model.getCurrentHero().equals(view.getPl2())){
			if(model.getCurrentHero() instanceof Warlock){
				try {
					playSound("Audio/GuldanPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Mage){
				try {
					playSound("Audio/JainaPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Hunter){
				try {
					playSound("Audio/RexxarPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				try {
					playSound("Audio/AnduinPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Audio/UtherPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}
			else if(model.getOpponent().equals(view.getPl1()) || model.getOpponent().equals(view.getPl2())){
			if(model.getOpponent() instanceof Warlock){
				try {
					playSound("Audio/GuldanPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Mage){
				try {
					playSound("Audio/JainaPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Hunter){
				try {
					playSound("Audio/RexxarPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(model.getOpponent() instanceof Priest){
				try {
					playSound("Audio/AnduinPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					playSound("Audio/UtherPlayHP.wav");
				} catch (UnsupportedAudioFileException
						| IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}}
	@Override
	public void onBurned(Card c) {
		 if(model.getCurrentHero().equals(view.getPl1())){
			 Burned1.setVisible(true);
			 Burned1.setText("Burned Card");
			 Burned1.setToolTipText(getInfo(c));
		 }
		 else{
			 Burned2.setVisible(true);
			 Burned2.setText("Burned Card");
			 Burned2.setToolTipText(getInfo(c));
		 }
	}
}

package engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.prism.Image;

import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener{	
	private JPanel CHeroField;
	private JPanel OHeroField; 
	private JButton CHero;
	private JButton CHeroPower;
	private JButton OHero;
	private JButton OHeroPower;
	private JPanel CHCards;
	private JPanel OHCards;
	private JPanel H1;
	private JPanel H2;
	private JButton Hunter1;
	private JButton Hunter2;
	private JButton Warlock1;
	private JButton Warlock2;
	private JButton Priest1;
	private JButton Priest2;
	private JButton Mage1;
	private JButton Mage2;
	private JButton Paladin1;
	private JButton Paladin2;
	private Hero Pl1;
	private Hero Pl2;
	private JButton ReadyP1;
	private JButton ReadyP2;
	private JPanel startMenu;
	private JButton quit;
	private JButton play;
	private Icon quitB;
	private Icon sIcon;
	private Icon hunter;
	private Icon mage;
	private Icon priest;
	private Icon paladin;
	private Icon warlock;
	private JLabel startBG;
	private JPanel field;
	private BufferedImage logo = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		public GameView(){
			setLayout(new BorderLayout());
			startBG = new JLabel();
			startBG.setIcon(new ImageIcon("Images/menu.png"));
			startBG.setSize(new Dimension(getWidth(),getHeight())); 
			BufferedImageLoader loader = new BufferedImageLoader();
			try{
				logo = loader.loadImage("/logo.png");
			}catch(IOException e){
				e.printStackTrace();
			}
		try {
				playSound("Audio/theme.wav");
		} catch (UnsupportedAudioFileException | IOException
					| LineUnavailableException e1) {
				e1.printStackTrace();
		}
		getContentPane().add(startBG,BorderLayout.NORTH);
		sIcon = new ImageIcon("Images/StartB.png");
		quitB = new ImageIcon("Images/QuitB.png");
		this.setTitle("Team 270 Hearthstone");		
		setIconImage(logo);
		startMenu = new JPanel();
		startMenu.setLayout(new FlowLayout());
		startMenu.setBackground(Color.DARK_GRAY);
		startMenu.setSize(new Dimension(150,150));
		startMenu.setOpaque(true);
		quit = new JButton(quitB);
		quit.setPreferredSize(new Dimension(150,89));
		quit.setBackground(Color.DARK_GRAY);
		play = new JButton(sIcon);
		play.setPreferredSize(new Dimension(150,89));
		play.setBackground(Color.DARK_GRAY);
		quit.setOpaque(true);
		play.setOpaque(true);
		startMenu.add(play);
		startMenu.add(quit);
		startBG.add(startMenu,BorderLayout.SOUTH);
		setBounds(0,0,6000,6000);
		setVisible(true);
		add(startMenu);
		CHeroField = new JPanel();
		OHeroField = new JPanel(); 
		CHCards = new JPanel();
		OHCards = new JPanel();
		H1 = new JPanel();
		H2 = new JPanel();
		CHeroField.setVisible(false);
		OHeroField.setVisible(false);
		CHCards.setVisible(false);
		OHCards.setVisible(false);
		H1.setVisible(false);
		H2.setVisible(false);
		Icon hpower = new ImageIcon("Images/HeroPower.png");
		CHeroPower = new JButton(hpower);
		OHeroPower = new JButton(hpower);
		CHeroPower.setOpaque(false);
		OHeroPower.setOpaque(false);
		CHeroPower.setPreferredSize(new Dimension(80,45));
		OHeroPower.setPreferredSize(new Dimension(80,45));
		CHeroField.setPreferredSize(new Dimension(getWidth(),1000));
		OHeroField.setPreferredSize(new Dimension(getWidth(),1000));
		CHCards.setSize(new Dimension(getWidth(),1000));
		OHCards.setSize(new Dimension(getWidth(),1000));
		H1.setSize(new Dimension(getWidth(),1000));
		H2.setSize(new Dimension(getWidth(),1000)); 
		play.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					playSound("Audio/start1.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startBG.setVisible(false);
				remove(startBG);
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				startMenu.remove(play);
				startMenu.remove(quit);
				remove(startMenu);
				field = new JPanel(){
					public void paintComponent(Graphics g){
						java.awt.Image background = Toolkit.getDefaultToolkit().getImage("Images/background.png");
						g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
					}
				};
				setContentPane(field);
				field.setVisible(true);
				field.setLayout(new GridLayout(6,1));
				CHeroField.setVisible(true);
				OHeroField.setVisible(true);
				CHCards.setVisible(true);
				OHCards.setVisible(true);
				H1.setVisible(true);
				H2.setVisible(true);
				CHeroField.setOpaque(false);
				OHeroField.setOpaque(false);
				CHCards.setOpaque(false);
				OHCards.setOpaque(false);
				H1.setOpaque(false);
				H2.setOpaque(false);
				field.add(H2);
				field.add(OHCards);
				field.add(OHeroField);
				field.add(CHeroField);
				field.add(CHCards);
				field.add(H1);
			}
		});
		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					dispose();
			}
		});
		hunter = new ImageIcon("Images/Rexxar.png");
		mage = new ImageIcon("Images/Jaina Proudmoore.png");
		paladin = new ImageIcon("Images/Uther Lightbringer.png");
		warlock = new ImageIcon("Images/Gul'dan.png");
		priest = new ImageIcon("Images/Anduin Wrynn.png");
		Hunter1 = new JButton(hunter);
		Hunter1.setPreferredSize(new Dimension(130,110));
		Hunter1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock1.setVisible(false);
				Priest1.setVisible(false);
				Mage1.setVisible(false);
				Paladin1.setVisible(false);
				Hunter1.setVisible(false);
				CHero= new JButton(hunter);
				CHero.setPreferredSize(new Dimension(130,110));
				H1.add(CHero);
				H1.add(CHeroPower);
				try {
					playSound("Audio/RexxarStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl1= new Hunter();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		Warlock1 = new JButton(warlock);
		Warlock1.setPreferredSize(new Dimension(130,110));
		Warlock1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock1.setVisible(false);
				Priest1.setVisible(false);
				Mage1.setVisible(false);
				Paladin1.setVisible(false);
				Hunter1.setVisible(false);
				CHero= new JButton(warlock);
				CHero.setPreferredSize(new Dimension(130,110));
				H1.add(CHero);
				H1.add(CHeroPower);
				try {
					playSound("Audio/GuldanStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl1 = new Warlock();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Priest1 = new JButton(priest);
		Priest1.setPreferredSize(new Dimension(130,110));
		Priest1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock1.setVisible(false);
				Priest1.setVisible(false);
				Mage1.setVisible(false);
				Paladin1.setVisible(false);
				Hunter1.setVisible(false);
				CHero= new JButton(priest);
				CHero.setPreferredSize(new Dimension(130,110));
				H1.add(CHero);
				H1.add(CHeroPower);
				try {
					playSound("Audio/AnduinStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl1= new Priest();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Mage1 = new JButton(mage);
		Mage1.setPreferredSize(new Dimension(130,110));
		Mage1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock1.setVisible(false);
				Priest1.setVisible(false);
				Mage1.setVisible(false);
				Paladin1.setVisible(false);
				Hunter1.setVisible(false);
				CHero= new JButton(mage);
				CHero.setPreferredSize(new Dimension(130,110));
				H1.add(CHero);
				H1.add(CHeroPower);
				try {
					playSound("Audio/JainaProudmooreStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl1= new Mage();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Paladin1 = new JButton(paladin);
		Paladin1.setPreferredSize(new Dimension(130,110));
		Paladin1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock1.setVisible(false);
				Priest1.setVisible(false);
				Mage1.setVisible(false);
				Paladin1.setVisible(false);
				Hunter1.setVisible(false);
				CHero= new JButton(paladin);
				CHero.setPreferredSize(new Dimension(130,110));
				H1.add(CHero);
				H1.add(CHeroPower);
				try {
					playSound("Audio/UtherStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl1 = new Paladin();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Hunter2 = new JButton(hunter);
		Hunter2.setPreferredSize(new Dimension(130,110));
		Hunter2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock2.setVisible(false);
				Priest2.setVisible(false);
				Mage2.setVisible(false);
				Paladin2.setVisible(false);
				Hunter2.setVisible(false);
				OHero= new JButton(hunter);
				OHero.setPreferredSize(new Dimension(130,110));
				H2.add(OHero);
				H2.add(OHeroPower);
				try {
					playSound("Audio/RexxarStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl2= new Hunter();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Warlock2 = new JButton(warlock);
		Warlock2.setPreferredSize(new Dimension(130,110));
		Warlock2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock2.setVisible(false);
				Priest2.setVisible(false);
				Mage2.setVisible(false);
				Paladin2.setVisible(false);
				Hunter2.setVisible(false);
				OHero= new JButton(warlock);
				OHero.setPreferredSize(new Dimension(130,110));
				H2.add(OHero);
				H2.add(OHeroPower);
				try {
					playSound("Audio/GuldanStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl2= new Warlock();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	    Priest2 = new JButton(priest);
	    Priest2.setPreferredSize(new Dimension(130,110));
		Priest2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock2.setVisible(false);
				Priest2.setVisible(false);
				Mage2.setVisible(false);
				Paladin2.setVisible(false);
				Hunter2.setVisible(false);
				OHero= new JButton(priest);
				OHero.setPreferredSize(new Dimension(130,110));
				H2.add(OHero);
				H2.add(OHeroPower);
				try {
					playSound("Audio/AnduinStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl2 = new Priest();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Mage2 = new JButton(mage);
		Mage2.setPreferredSize(new Dimension(130,110));
		Mage2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock2.setVisible(false);
				Priest2.setVisible(false);
				Mage2.setVisible(false);
				Paladin2.setVisible(false);
				Hunter2.setVisible(false);
				OHero= new JButton(mage);
				OHero.setPreferredSize(new Dimension(130,110));
				H2.add(OHero);
				H2.add(OHeroPower);
				try {
					playSound("Audio/JainaProudmooreStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl2= new Mage();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Paladin2 = new JButton(paladin);
		Paladin2.setPreferredSize(new Dimension(130,110));
		Paladin2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Warlock2.setVisible(false);
				Priest2.setVisible(false);
				Mage2.setVisible(false);
				Paladin2.setVisible(false);
				Hunter2.setVisible(false);
				OHero= new JButton(paladin);
				OHero.setPreferredSize(new Dimension(130,110));
				H2.add(OHero);
				H2.add(OHeroPower);
				try {
					playSound("Audio/UtherStart.wav");
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e1) {
					e1.printStackTrace();
				}
				try {
					Pl2 = new Paladin();
				} catch (IOException | CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		Icon readyB= new ImageIcon("Images/ReadyB.png");
		ReadyP1 = new JButton(readyB);
		ReadyP1.setPreferredSize(new Dimension(120,50));
		ReadyP2 = new JButton(readyB);
		ReadyP2.setPreferredSize(new Dimension(120,50));
		CHCards.add(ReadyP1);
		OHCards.add(ReadyP2);
		H1.add(Hunter1);
		H1.add(Warlock1);
		H1.add(Priest1);
		H1.add(Mage1);
		H1.add(Paladin1);
		H2.add(Hunter2);
		H2.add(Warlock2);
		H2.add(Priest2);
		H2.add(Mage2);
		H2.add(Paladin2);
	}
	public static void main(String[] args){
		new GameView();
	}
	@Override
	public void actionPerformed(ActionEvent e){
		
	}
	public JPanel getCHeroField() {
		return CHeroField;
	}
	public void setCHeroField(JPanel cHeroField) {
		CHeroField = cHeroField;
	}
	public JPanel getCHCards() {
		return CHCards;
	}
	public void setCHCards(JPanel cHCards) {
		CHCards = cHCards;
	}
	public JPanel getOHCards() {
		return OHCards;
	}
	public void setOHCards(JPanel oHCards) {
		OHCards = oHCards;
	}
	public JPanel getH1() {
		return H1;
	}
	public void setH1(JPanel h1) {
		H1 = h1;
	}
	public JPanel getH2() {
		return H2;
	}
	public void setH2(JPanel h2) {
		H2 = h2;
	}
	public JButton getHunter1() {
		return Hunter1;
	}
	public void setHunter1(JButton hunter1) {
		Hunter1 = hunter1;
	}
	public JButton getHunter2() {
		return Hunter2;
	}
	public void setHunter2(JButton hunter2) {
		Hunter2 = hunter2;
	}
	public JButton getWarlock1() {
		return Warlock1;
	}
	public void setWarlock1(JButton warlock1) {
		Warlock1 = warlock1;
	}
	public JButton getWarlock2() {
		return Warlock2;
	}
	public void setWarlock2(JButton warlock2) {
		Warlock2 = warlock2;
	}
	public JButton getPriest1() {
		return Priest1;
	}
	public void setPriest1(JButton priest1) {
		Priest1 = priest1;
	}
	public JButton getPriest2() {
		return Priest2;
	}
	public void setPriest2(JButton priest2) {
		Priest2 = priest2;
	}
	public JButton getMage1() {
		return Mage1;
	}
	public void setMage1(JButton mage1) {
		Mage1 = mage1;
	}
	public JButton getMage2() {
		return Mage2;
	}
	public void setMage2(JButton mage2) {
		Mage2 = mage2;
	}
	public JButton getPaladin1() {
		return Paladin1;
	}
	public void setPaladin1(JButton paladin1) {
		Paladin1 = paladin1;
	}
	public JButton getPaladin2() {
		return Paladin2;
	}
	public void setPaladin2(JButton paladin2) {
		Paladin2 = paladin2;
	}
	public JPanel getOHeroField() {
		return OHeroField;
	}
	public void setOHeroField(JPanel oHeroField) {
		OHeroField = oHeroField;
	}
	public JButton getCHero() {
		return CHero;
	}
	public void setCHero(JButton cHero) {
		CHero = cHero;
	}
	public JButton getCHeroPower() {
		return CHeroPower;
	}
	public void setCHeroPower(JButton cHeroPower) {
		CHeroPower = cHeroPower;
	}
	public JButton getOHero() {
		return OHero;
	}
	public void setOHero(JButton oHero) {
		OHero = oHero;
	}
	public JButton getOHeroPower() {
		return OHeroPower;
	}
	public void setOHeroPower(JButton oHeroPower) {
		OHeroPower = oHeroPower;
	}
	public Hero getPl2() {
		return Pl2;
	}
	public void setPl2(Hero pl2) {
		Pl2 = pl2;
	}
	public Hero getPl1() {
		return Pl1;
	}
	public void setPl1(Hero pl1) {
		Pl1 = pl1;
	}
	public JButton getReadyP1() {
		return ReadyP1;
	}
	public void setReadyP1(JButton readyP1) {
		ReadyP1 = readyP1;
	}
	public JButton getReadyP2() {
		return ReadyP2;
	}
	public void setReadyP2(JButton readyP2) {
		ReadyP2 = readyP2;
	}
	public JPanel getStartMenu() {
		return startMenu;
	}
	public void setStartMenu(JPanel startMenu) {
		this.startMenu = startMenu;
	}
	public JButton getQuit() {
		return quit;
	}
	public void setQuit(JButton quit) {
		this.quit = quit;
	}
	public JButton getPlay() {
		return play;
	}
	public void setPlay(JButton play) {
		this.play = play;
	}
	public BufferedImage getLogo() {
		return logo;
	}
	public void setLogo(BufferedImage logo) {
		this.logo = logo;
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
}

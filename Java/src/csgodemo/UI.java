package csgodemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.script.ScriptException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class UI extends JFrame{
	
	JPanel panel, innerpanel;
	JTextArea text;
	JButton loadDemo, plot;
	JComboBox<Player> player_cb;
	ArrayList<Player> players;
	JFileChooser fc;
	JScrollPane scroll;
	
	UI(){
		super("Accuracy check alpha 0.1");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		innerpanel = new JPanel();
		innerpanel.setMaximumSize(new Dimension(512, 64));
		panel.add(innerpanel);
		 fc = new JFileChooser();
		 fc.setAcceptAllFileFilterUsed(false);
		 fc.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "CS:GO demo (.dem)";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
			        return true;
			    }
				String name = f.getName();
				return name.substring(name.lastIndexOf('.')+1).toLowerCase().equals("dem");
			}
		});
		loadDemo = new JButton();
		loadDemo.setText("Load demo");
		loadDemo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				 
				 int returnVal = fc.showOpenDialog(loadDemo);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            try {
							updateDemo(DataParser.getData(file.getAbsolutePath()));
						} catch (NoSuchMethodException | IOException | InterruptedException | ScriptException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        }
			}
		});
		
		innerpanel.add(loadDemo);
		player_cb = new JComboBox<>();
		innerpanel.add(player_cb);
		plot = new JButton("Plot");
		plot.setEnabled(false);
		plot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Plotter((Player)player_cb.getSelectedItem());
				
			}
		});
		innerpanel.add(plot);
		text = new JTextArea();
		scroll = new JScrollPane (text, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);
		add(panel);
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	protected void updateDemo(ArrayList<Player> data) {

		players = data;
		text.setText("");
		player_cb.removeAllItems();
		for (Player p : players) player_cb.addItem(p);
		plot.setEnabled(true);
		//pack();
		for (Player player : players){
		
		text.setText(text.getText() + "Name: " + player.name + "\n");
		for (String weapon : player.shots.keySet()){
			text.setText(text.getText() + "Avrg move speed with " + weapon + " is " + player.getAverageAccuracy(weapon) + "\n");
		}
		text.setText(text.getText() + "\n");
		}
		
		
		
		
		
	}
	
	class Plotter extends JFrame {
		
		static final int DRAWSIZE = 600;
		
		Player player;
		ArrayList<JCheckBox> weapon_checks;
		JLabel totalshots_l;
		JLabel avg_speed_l;
		float avg_speed;
		JPanel outterpanel, innerpanel, drawpanel;
		
		ArrayList<Float> shots;
		
		BufferedImage graph;
		
		JTextArea shots_listed;
		
		Plotter(Player p){
			super("Plot player - " + p.name);
			shots = new ArrayList<>();
			graph = new BufferedImage(DRAWSIZE, DRAWSIZE, BufferedImage.TYPE_INT_ARGB);
			updateGraph();
			player = p;
			outterpanel = new JPanel();
			add(outterpanel);
			shots_listed = new JTextArea();
			outterpanel.add(shots_listed);
			innerpanel = new JPanel ();
			innerpanel.setLayout(new BoxLayout(innerpanel, BoxLayout.Y_AXIS));
			outterpanel.add(innerpanel);
			weapon_checks = new ArrayList<>();
			for (String weapon : player.shots.keySet()){
				JCheckBox cb = new JCheckBox(weapon);
				cb.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						update();
						
					}
				});
				innerpanel.add(cb);
				weapon_checks.add(cb);
			}
			totalshots_l = new JLabel("Total shots: 0");
			innerpanel.add(totalshots_l);
			avg_speed_l = new JLabel("Avg speed: 0");
			innerpanel.add(avg_speed_l);
			drawpanel = new JPanel(){
				@Override
				protected void paintComponent(Graphics g) {
					if (graph!=null) {
						g.drawImage(graph, 0, 0, null);
					}
				}
			};
			drawpanel.setPreferredSize(new Dimension(DRAWSIZE, DRAWSIZE));
			outterpanel.add(drawpanel);
			
			pack();
			setVisible(true);
		}
		
		void update(){
			
			shots.clear();
			for (JCheckBox cb : weapon_checks){
				String weapon = cb.getText();
				if (player.shots.containsKey(weapon) && cb.isSelected()){
					shots.addAll(player.shots.get(weapon));
				}
			}
			
			avg_speed = 0;
			for (Float spd : shots){
				avg_speed += spd;
			}
			avg_speed = avg_speed / shots.size();
			
			totalshots_l.setText("Total shots: " + String.valueOf(shots.size()));
			avg_speed_l.setText("Avg speed: " + String.valueOf(avg_speed));

			
			updateGraph();
			
			pack();
			repaint();

		}
		
		void updateGraph(){
			Graphics g = graph.getGraphics();
			Graphics2D g2 = (Graphics2D) g;
			g2.clearRect(0, 0, DRAWSIZE, DRAWSIZE);
			g2.setColor(Color.lightGray);
			g2.fillRect(0, 0, DRAWSIZE, DRAWSIZE);
			
			g.setColor(Color.white);
			g.fillRect(50, 50, DRAWSIZE - 100, DRAWSIZE - 100);
			
			g.setColor(Color.BLACK);
			g.drawLine(50, 50, 50, DRAWSIZE - 50);
			g.drawLine(50, DRAWSIZE - 50, DRAWSIZE-50, DRAWSIZE-50);
			
			g.drawString("250.0 vel", DRAWSIZE-75, DRAWSIZE-25);
			g.drawString("100% of shots", 0, 50);
			
			shots.sort(new Comparator<Float>() {

				@Override
				public int compare(Float o1, Float o2) {
					if (o1 > o2) return -1;
					if (o1 < o2) return 1;
					return 0;
				}
			});
			float x1, x2, y1, y2;
			for (int i = 0; i < shots.size() - 1; i++){
				x1 = speedToX(shots.get(i));
				y1 = percentageToY(100 - (100 * i / shots.size()));
				x2 = speedToX(shots.get(i+1));
				y2 = percentageToY(100 - (100 * (i+1) / shots.size()));
				g2.draw(new Line2D.Float(x1,  y1, x2, y2));
			}
		}
		
		float speedToX(float speed){
			return 50 + (DRAWSIZE-100)*speed/250;
		}
		
		float percentageToY(float p) {
			return DRAWSIZE - 50 - (DRAWSIZE-100)*(p)/100;
		}
	}

}

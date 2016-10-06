import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Board extends JFrame {

	private Container c;

	private JTextField[] textField = new JTextField[10];

	private Button button = new Button("�Է�");
	private Button button2 = new Button("������");
	private Button button3 = new Button("���߱�");
	private Button button4 = new Button("���� �Է�");
	private Button button5 = new Button("������!!");
	private BufferedImage pinImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

	private String[] data = { "", "", "", "", "", "", "", "", "", "" };

	private JTextField item = new JTextField(10);
	private JTextField itemNum = new JTextField("���� �Է�");

	private Font font1 = new Font("Serif", Font.BOLD, 50);
	private Font font2 = new Font("����", Font.BOLD, 40);

	Clip bgmclip;

	int count = 1;

	int num = 0;
	int listNum = 0; // ��� ��
	int degree;// draw����
	int trans; // ��׷��߸��� (�����)
	int dartPinReapeat = 0; // ȿ���� �ݺ� ��
	int endReapeat = 1;// �������� ȿ���� �ݺ���
	int spinReapeat = 0;// �� ���ư��� ȿ����
	// int pinMoveX = 1000;//���� ��
	// int pinMoveY = 400;//���� ��
	int pinMoveX = 523;// ���� ��
	int pinMoveY = 700;// ���� ��

	protected int oldx, oldy, radius;
	protected int curx, cury;
	protected int rotx, roty;
	protected int rDegree = 0;

	boolean back = false;
	boolean stop = false;
	boolean pinCheck = false;

	boolean turnY = false;
	boolean startTurn = false;

	boolean backSound = true;// loop

	ImageIcon img = new ImageIcon("C:\\JavaProject\\ChooseBoard\\imgback.png");
	File dartPin = new File("C:\\JavaProject\\ChooseBoard\\dart.wav");// ��
																		// ���ư��¼Ҹ�
	File bgm = new File("C:\\JavaProject\\ChooseBoard\\boardSound.wav");// ������
																		// �Ҹ�
	File end = new File("C:\\JavaProject\\ChooseBoard\\end.wav");

	private JLabel label = new JLabel(img);

	Color[] color = { new Color(255, 102, 102), new Color(255, 204, 153), new Color(102, 255, 204),
			new Color(153, 102, 255), new Color(102, 102, 255), new Color(051, 255, 255), new Color(153, 051, 255),
			Color.orange, Color.WHITE, Color.darkGray, Color.yellow, Color.lightGray, Color.pink };

	public Board() {

		c = getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PanelA panelA = new PanelA();
		// PanelB panelB = new PanelB();

		c.add(panelA);

		setSize(1800, 2200);
		setVisible(true);

		panelA.add(itemNum);
		panelA.add(button4);

		panelA.add(button2);
		panelA.add(button3);

		panelA.add(button);
		panelA.add(item);
		panelA.add(button5);
		item.setText("��� �Է�");

		// File file = new File("C:\\JavaProject\\ChooseBoard\\��1.png");//������
		// ���ư���
		File file = new File("C:\\JavaProject\\ChooseBoard\\��3.png");// ���鿡��
																		// ���ư���

		try {
			pinImg = ImageIO.read(file);

		} catch (IOException e) {
			e.printStackTrace();
		}

		button5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {// ������
				// TODO Auto-generated method stub
				if (pinCheck == false) {
					pinCheck = true;
				} else if (pinCheck == true)
					pinCheck = false;

				playSound(dartPin, dartPinReapeat);// ȿ����
			}

		});

		button4.addActionListener(new ActionListener() {// ���� �Է�

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				listNum = Integer.parseInt(itemNum.getText());

			}
		});

		button.addActionListener(new ActionListener() {// ��� �Է�

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				data[num] = item.getText();
				System.out.println(data[num]);
				item.setText(null);
				num++;

			}
		});

		button3.addActionListener(new ActionListener() {// ���߱�

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (stop == false) {
					stop = true;

				} else if (stop == true)
					stop = false;
			}
		});

		button2.addActionListener(new ActionListener() {// ������

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				(new Time()).start();
				back = true;

			}
		});
	}

	int transCheck(boolean stop) {// ����
		if (stop == true) {
			trans = 0;
			return trans;
		} else {
			trans = 0;
			return trans;
		}
	}

	Boolean stopCheck(boolean stop) {
		if (stop == true)
			return true;
		else
			return false;

	}

	class PanelA extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			int[] xp = { 770, 950, 870, 1000, 1000, 870, 950, 770 };
			int[] yp = { 405, 475, 415, 415, 395, 395, 345, 405 };

			int startDegree = count;

			Dimension d = getSize();

			g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
			setOpaque(false);

			if (back == true) {
				g.setColor(Color.black);
				g.fillOval(270, 140, 520 - transCheck(stop), 520); // üũ

			}
			for (int i = 0; i < listNum; i++) {
				g.setFont(font1);

				g.setColor(color[i]);

				
				g.fillArc(280, 150, 500 - transCheck(stop), 500, startDegree, degree); // üũ

				if (back == true) {
					g.setColor(Color.BLACK);
					g.fillPolygon(xp, yp, xp.length);
					g.drawOval(300, 170, 460 - transCheck(stop), 460); // üũ
					g.setColor(Color.GRAY);
					g.fillOval(520, 390, 20 - transCheck(stop), 20); // üũ
					g.setColor(Color.black);
					g.setFont(font2);
					textRotate(g, 530, 405, -(startDegree * Math.PI / 180) - (degree / 2) * Math.PI / 180,
							"    " + data[i]);
					startDegree = startDegree + degree;

				}
			}
			radius = 250;
			oldx = 100;
			oldy = 100;
			if (startTurn == false) {
				g.drawImage(pinImg, pinMoveX, pinMoveY, 250, 250, null);
				
			} else {
				g.drawImage(pinImg, 545 + curx - radius / 2, 450 + cury - radius / 2, radius, radius, null);
				
			}
		}
	}

	void paint() {

		degree = 360 / listNum;

		count = count + 2;

		repaint();
	}

	private void textRotate(Graphics g, double x, double y, double theta, String label) {

		Graphics2D g2D = (Graphics2D) g;

		AffineTransform fontAT = new AffineTransform();

		Font theFont = g2D.getFont();

		fontAT.rotate(theta);
		Font theDerivedFont = theFont.deriveFont(fontAT);

		g2D.setFont(theDerivedFont);

		g2D.drawString(label, (int) x, (int) y);

		g2D.setFont(theFont);
	}

	public void rotate(int deg) {
		rDegree = rDegree + deg; // deg ����ŭ ��� �������� ��
		rDegree = rDegree % 360; // 360 �̻��� ���� ���� �Ǹ�, ���ϰ����� �������
		double dDegree = Math.toRadians(rDegree); // �������� ��ȯ
		double cosd = Math.cos(dDegree); // �ڻ��� �Լ�
		double sind = Math.sin(dDegree); // �����Լ�
		curx = (int) ((oldx - rotx) * cosd - (oldy - roty) * sind) + rotx;

		cury = (int) ((oldx - rotx) * sind + (oldy - roty) * cosd) + roty;

	}

	void moveY() {
		if (pinMoveY >= 421) {
			turnY = true;
			startTurn = true;
		} else if (pinMoveY == 500 && startTurn == true) {
			turnY = false;
		}

	}

	public void playSound(File file, int repeat) {// ȿ����
		try {
			final Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					// TODO Auto-generated method stub

					if (event.getType() == LineEvent.Type.STOP) {
						// �� �κ��� ������ ȿ������ �޸𸮿� ���� �׿��� ������ ũ���õȴ�
						clip.close();
					}
				}
			});
			clip.loop(repeat);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Time extends Thread {

		public void run() {
			double i = 14;
			while (true) {

				paint();

				try {

					Thread.sleep((long) (i));
					// playSound(bgm, spinReapeat);
					if (pinCheck == true) {

						moveY();

						if (turnY == false) {
							pinMoveY = pinMoveY - 4;
						}
						if (startTurn == true) {
							rotate(-2);
						}

					} else if (pinCheck == false) {

						pinMoveX = 523;

						pinMoveY = 700;

					}

					if (stopCheck(stop) == false) {
						if (i > 1.02) {
							if (count % 20 == 1) {
								playSound(bgm, spinReapeat);
							}
							i = i - 0.03;
						} else if (i == 1.000000000000012) {
							i = 1.0;
						}
					}

					if (stopCheck(stop) == true) {
						i = i + 0.03;
						if (count % 40 == 7) {
							playSound(bgm, spinReapeat);
						}
						if (i >= 20) {
							playSound(end, endReapeat);
							break;
						}

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Board b = new Board();

	}

}

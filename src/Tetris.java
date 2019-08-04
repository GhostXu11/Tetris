/**
 * Created by Zhengyang Xu on 2019/6/17.
 * This Tetris game is very easy to play. You can control it by using mouse. The size of square can be changed by the combox below the main area. There are also 4 new shapes can be chosen to join the game.
 * Three parameter: Scoring factor,number of rows required for each Level of difficulty,speed factor can be adjusted by the slide.
 */
import java.math.*;
import java.util.Hashtable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
//import net.coobird.thumbnailator.Thumbnails;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Tetris {
    private Timer timer = new Timer(1000, new timerListener());

    private int ROW = 20;
    private int COL = 10;

    private double LENGTH_OLD = 31;

    private boolean showGrid = true;
    private boolean isColor = true;

    private MyCanvas drawarea = new MyCanvas();
    private JFrame f = new JFrame("Tetris");
    private JLabel j1 = new JLabel();

    private JSlider slider1 = new JSlider(0,10,5);
    private JSlider slider2 = new JSlider(20,50,35);
    private JSlider slider3 = new JSlider();

    private JButton bt1 = new JButton("Exit");

    private JTextField txt1 = new JTextField("PAUSE");
    //private JTextField txt2 = new JTextField();

    private JLabel label_1 = new JLabel("Scoring factor: ");
    private JLabel label_2 = new JLabel("Rows for level: ");
    private JLabel label_3 = new JLabel("Speed factor: ");
    private JLabel label_4 = new JLabel("COL: ");
    private JLabel label_5 = new JLabel("ROW: ");
    private JLabel label_6 = new JLabel("SIZE: ");
    private JLabel label_7 = new JLabel("New shape: ");

    private String[] listData_1 = new String[]{"9", "10", "11"};
    private JComboBox<String> comboBox_1 = new JComboBox<String>(listData_1);

    private String[] listData_2 = new String[]{"19", "20", "21"};
    private JComboBox<String> comboBox_2 = new JComboBox<String>(listData_2);

    private String[] listData_3 = new String[]{"small", "large"};
    private JComboBox<String> comboBox_3 = new JComboBox<String>(listData_3);

    private String[] listData_4 = new String[]{"1", "2","3","4"};
    private JComboBox<String> comboBox_4 = new JComboBox<String>(listData_4);

    private BufferedImage image = new BufferedImage(750,750, BufferedImage.TYPE_INT_RGB);
    private Graphics g = image.createGraphics();

    private int[][] map = new int[11][21];

    private Color[] color = new Color[]{Color.green, Color.red, Color.orange, Color.blue, Color.CYAN, Color.yellow, Color.pink, Color.gray,Color.BLACK,Color.magenta,Color.darkGray,Color.lightGray};
    private final int DEFAULT = 7;
    private Color[][] mapColor = new Color[11][21];

    private int level = 1;
    private int line = 0;
    private int score = 0;

    private int M;
    private int N;
    private double S;
    private double FS = 1.00;

    private int type, state, x, y, nextType, nextState;
    private boolean newBegin = true;

    private boolean status;
    private boolean Flag_shape_1 = false;
    private boolean Flag_shape_2 = false;
    private boolean Flag_shape_3 = false;
    private boolean Flag_shape_4 = false;

    private int[][][][] shape = new int[][][][]{
            // S:
            { { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} },
                    { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} },
                    { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} },
                    { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} } },
            // Z:
            { { {1,0,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} },
                    { {0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0} },
                    { {1,0,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} },
                    { {0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0} } },
            // L:
            { { {0,0,0,0}, {1,1,1,0}, {0,0,1,0}, {0,0,0,0} },
                    { {0,0,0,0}, {0,1,1,0}, {0,1,0,0}, {0,1,0,0} },
                    { {0,0,0,0}, {0,1,0,0}, {0,1,1,1}, {0,0,0,0} },
                    { {0,0,1,0}, {0,0,1,0}, {0,1,1,0}, {0,0,0,0} } },
            // J:
            { { {0,0,0,0}, {0,0,1,0}, {1,1,1,0}, {0,0,0,0} },
                    { {0,0,0,0}, {0,1,1,0}, {0,0,1,0}, {0,0,1,0} },
                    { {0,0,0,0}, {0,1,1,1}, {0,1,0,0}, {0,0,0,0} },
                    { {0,1,0,0}, {0,1,0,0}, {0,1,1,0}, {0,0,0,0} } },
            // I:
            { { {0,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,1,0,0} },
                    { {0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0} },
                    { {0,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,1,0,0} },
                    { {0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0} } },
            // O:
            { { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0} },
                    { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  },
                    { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  },
                    { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  } },
            // T:
            { { {0,1,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} },
                    { {0,0,0,0}, {1,1,1,0}, {0,1,0,0}, {0,0,0,0} },
                    { {0,1,0,0}, {0,1,1,0}, {0,1,0,0}, {0,0,0,0} },
                    { {0,1,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0} } },


    };
    /*
    int iX(float x){return Math.round(centerX + x/pixelSize);}
    int iY(float y){return Math.round(centerY - y/pixelSize);}
    float fx(int x){return (x - centerX) * pixelSize;}
    float fy(int y){return (centerY - y) * pixelSize;}

     */
    // initial
    private void init(){
        f.setSize(750,880);
        newShape();
        timer.start();

        int maxX, maxY;
        float pixelSize, centerX, centerY, rWidth = 750.0F, rHeight = 880.0F;
        Dimension d = f.getSize();
        maxX = d.width -1;
        maxY = d.height -1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
        int LENGTH = (int)(LENGTH_OLD/pixelSize);
        int AREA_WIDTH = LENGTH*25;
        int AREA_HEIGHT = LENGTH*23;
        int MARGIN_LEFT = LENGTH*2;
        int MARGIN_UP = LENGTH;


        bt1.setBounds(Math.round(centerX + 225/pixelSize),Math.round(centerY - (-375)/pixelSize),Math.round(centerX + 350/pixelSize)-Math.round(centerX + 225/pixelSize),Math.round(centerY - (-380)/pixelSize)-Math.round(centerY - (-350)/pixelSize));
        f.add(bt1);

        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        txt1.setBounds(LENGTH*5,LENGTH*9,4*LENGTH,2*LENGTH);
        //txt1.setVisible(false);
        Font font1 = new Font("SansSerif", Font.BOLD, 40);
        txt1.setFont(font1);
        f.add(txt1);
        txt1.setVisible(false);

        slider1.setMajorTickSpacing(5);
        slider1.setMinorTickSpacing(1);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                M = slider1.getValue();
                //System.out.println("Current Value for M: " + M);
            }
        });

        slider2.setMajorTickSpacing(5);
        slider2.setMinorTickSpacing(1);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                N = slider2.getValue();
                //System.out.println("Current Value for N: " + N);
            }
        });

        Hashtable labelTable = new Hashtable();
        for (int i = 0; i <= 100; i+=10) {
            labelTable.put(new Integer(i), new JLabel(String.valueOf(i/100.0)));
        }

        slider3.setLabelTable(labelTable);
        slider3.setMajorTickSpacing(5);
        slider3.setMinorTickSpacing(1);
        slider3.setPaintTicks(true);
        slider3.setPaintLabels(true);

        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double S1 = slider3.getValue();
                S = S1 / 100;
                FS = 1 + level* S;
                //System.out.println("Current Value for S: " + FS);
            }
        });


        drawarea.setBounds(0,0,AREA_WIDTH,AREA_HEIGHT);
        f.add(drawarea);

        j1.setBounds(0,AREA_HEIGHT,AREA_WIDTH,10*LENGTH);
        f.add(j1);

        // add Scoring factor
        label_1.setBounds(0,LENGTH*23,LENGTH*3,LENGTH);
        j1.add(label_1);
        slider1.setBounds(LENGTH*3,LENGTH*23,LENGTH*4,LENGTH*2);
        j1.add(slider1);

        //add # rows for level of difficulty
        label_2.setBounds(0,LENGTH*25,LENGTH*3,LENGTH);
        j1.add(label_2);
        slider2.setBounds(LENGTH*3,LENGTH*25,LENGTH*4,LENGTH*2);
        j1.add(slider2);

        //add speed factor
        label_3.setBounds(7*LENGTH,LENGTH*23,LENGTH*3,LENGTH);
        j1.add(label_3);
        slider3.setBounds(LENGTH*10,LENGTH*23,LENGTH*6,LENGTH*2);
        j1.add(slider3);

        label_4.setBounds(7*LENGTH,LENGTH*25,LENGTH*2,LENGTH);
        j1.add(label_4);

        comboBox_1.setBounds(LENGTH*8,LENGTH*25,LENGTH*2,LENGTH);
        comboBox_1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    COL = Integer.parseInt(String.valueOf(comboBox_1.getSelectedItem()));
                    //System.out.println(COL);
                }
            }

        });
        comboBox_1.setSelectedIndex(1);
        j1.add(comboBox_1);
        label_5.setBounds(10*LENGTH,LENGTH*25,LENGTH*2,LENGTH);
        j1.add(label_5);

        comboBox_2.setBounds(LENGTH*11,LENGTH*25,LENGTH*2,LENGTH);
        comboBox_2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    ROW = Integer.parseInt(String.valueOf(comboBox_2.getSelectedItem()));
                    //System.out.println(COL);
                }
            }

        });
        comboBox_2.setSelectedIndex(1);
        j1.add(comboBox_2);

        label_6.setBounds(13*LENGTH,LENGTH*25,LENGTH*2,LENGTH);
        j1.add(label_6);

        comboBox_3.setBounds(LENGTH*14,LENGTH*25,LENGTH*3,LENGTH);
        comboBox_3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (comboBox_3.getSelectedIndex() == 0) {
                        LENGTH_OLD = 31;
                    }
                    if (comboBox_3.getSelectedIndex() == 1) {
                        LENGTH_OLD = 33;
                    }

                }
            }
        });
        comboBox_3.setSelectedIndex(0);
        j1.add(comboBox_3);

        label_7.setBounds(18*LENGTH,(int)(LENGTH*23.5),LENGTH*3,LENGTH);
        j1.add(label_7);

        comboBox_4.setBounds(LENGTH*21,(int)(LENGTH*23.5),LENGTH*2,LENGTH);
        comboBox_4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (comboBox_4.getSelectedIndex() == 0) {
                        if(Flag_shape_1 == false) {
                            shape = Arrays.copyOf(shape, shape.length + 1); //create new array from old array and allocate one more element
                            shape[shape.length - 1] = new int[4][4][4];
                            shape[shape.length - 1][0][0][0] = 0;
                            shape[shape.length - 1][0][0][1] = 0;
                            shape[shape.length - 1][0][0][2] = 0;
                            shape[shape.length - 1][0][0][3] = 0;

                            shape[shape.length - 1][0][1][0] = 0;
                            shape[shape.length - 1][0][1][1] = 0;
                            shape[shape.length - 1][0][1][2] = 1;
                            shape[shape.length - 1][0][1][3] = 0;

                            shape[shape.length - 1][0][2][0] = 0;
                            shape[shape.length - 1][0][2][1] = 1;
                            shape[shape.length - 1][0][2][2] = 1;
                            shape[shape.length - 1][0][2][3] = 0;

                            shape[shape.length - 1][0][3][0] = 0;
                            shape[shape.length - 1][0][3][1] = 0;
                            shape[shape.length - 1][0][3][2] = 0;
                            shape[shape.length - 1][0][3][3] = 0;

                            shape[shape.length - 1][1][0][0] = 0;
                            shape[shape.length - 1][1][0][1] = 0;
                            shape[shape.length - 1][1][0][2] = 0;
                            shape[shape.length - 1][1][0][3] = 0;

                            shape[shape.length - 1][1][1][0] = 0;
                            shape[shape.length - 1][1][1][1] = 1;
                            shape[shape.length - 1][1][1][2] = 1;
                            shape[shape.length - 1][1][1][3] = 0;

                            shape[shape.length - 1][1][2][0] = 0;
                            shape[shape.length - 1][1][2][1] = 0;
                            shape[shape.length - 1][1][2][2] = 1;
                            shape[shape.length - 1][1][2][3] = 0;

                            shape[shape.length - 1][1][3][0] = 0;
                            shape[shape.length - 1][1][3][1] = 0;
                            shape[shape.length - 1][1][3][2] = 0;
                            shape[shape.length - 1][1][3][3] = 0;

                            shape[shape.length - 1][2][0][0] = 0;
                            shape[shape.length - 1][2][0][1] = 0;
                            shape[shape.length - 1][2][0][2] = 0;
                            shape[shape.length - 1][2][0][3] = 0;

                            shape[shape.length - 1][2][1][0] = 0;
                            shape[shape.length - 1][2][1][1] = 1;
                            shape[shape.length - 1][2][1][2] = 1;
                            shape[shape.length - 1][2][1][3] = 0;

                            shape[shape.length - 1][2][2][0] = 0;
                            shape[shape.length - 1][2][2][1] = 1;
                            shape[shape.length - 1][2][2][2] = 0;
                            shape[shape.length - 1][2][2][3] = 0;

                            shape[shape.length - 1][2][3][0] = 0;
                            shape[shape.length - 1][2][3][1] = 0;
                            shape[shape.length - 1][2][3][2] = 0;
                            shape[shape.length - 1][2][3][3] = 0;

                            shape[shape.length - 1][3][0][0] = 0;
                            shape[shape.length - 1][3][0][1] = 0;
                            shape[shape.length - 1][3][0][2] = 0;
                            shape[shape.length - 1][3][0][3] = 0;

                            shape[shape.length - 1][3][1][0] = 0;
                            shape[shape.length - 1][3][1][1] = 1;
                            shape[shape.length - 1][3][1][2] = 0;
                            shape[shape.length - 1][3][1][3] = 0;

                            shape[shape.length - 1][3][2][0] = 0;
                            shape[shape.length - 1][3][2][1] = 1;
                            shape[shape.length - 1][3][2][2] = 1;
                            shape[shape.length - 1][3][2][3] = 0;

                            shape[shape.length - 1][3][3][0] = 0;
                            shape[shape.length - 1][3][3][1] = 0;
                            shape[shape.length - 1][3][3][2] = 0;
                            shape[shape.length - 1][3][3][3] = 0;

                            Flag_shape_1 = true;

                        }
                    }
                    if (comboBox_4.getSelectedIndex() == 1) {
                        if(Flag_shape_2 == false) {
                            shape = Arrays.copyOf(shape, shape.length + 1); //create new array from old array and allocate one more element
                            shape[shape.length - 1] = new int[4][4][4];
                            shape[shape.length - 1][0][0][0] = 0;
                            shape[shape.length - 1][0][0][1] = 0;
                            shape[shape.length - 1][0][0][2] = 0;
                            shape[shape.length - 1][0][0][3] = 0;

                            shape[shape.length - 1][0][1][0] = 0;
                            shape[shape.length - 1][0][1][1] = 0;
                            shape[shape.length - 1][0][1][2] = 1;
                            shape[shape.length - 1][0][1][3] = 0;

                            shape[shape.length - 1][0][2][0] = 0;
                            shape[shape.length - 1][0][2][1] = 0;
                            shape[shape.length - 1][0][2][2] = 1;
                            shape[shape.length - 1][0][2][3] = 0;

                            shape[shape.length - 1][0][3][0] = 0;
                            shape[shape.length - 1][0][3][1] = 0;
                            shape[shape.length - 1][0][3][2] = 1;
                            shape[shape.length - 1][0][3][3] = 0;

                            shape[shape.length - 1][1][0][0] = 0;
                            shape[shape.length - 1][1][0][1] = 0;
                            shape[shape.length - 1][1][0][2] = 0;
                            shape[shape.length - 1][1][0][3] = 0;

                            shape[shape.length - 1][1][1][0] = 0;
                            shape[shape.length - 1][1][1][1] = 1;
                            shape[shape.length - 1][1][1][2] = 1;
                            shape[shape.length - 1][1][1][3] = 1;

                            shape[shape.length - 1][1][2][0] = 0;
                            shape[shape.length - 1][1][2][1] = 0;
                            shape[shape.length - 1][1][2][2] = 0;
                            shape[shape.length - 1][1][2][3] = 0;

                            shape[shape.length - 1][1][3][0] = 0;
                            shape[shape.length - 1][1][3][1] = 0;
                            shape[shape.length - 1][1][3][2] = 0;
                            shape[shape.length - 1][1][3][3] = 0;

                            shape[shape.length - 1][2][0][0] = 0;
                            shape[shape.length - 1][2][0][1] = 0;
                            shape[shape.length - 1][2][0][2] = 0;
                            shape[shape.length - 1][2][0][3] = 0;

                            shape[shape.length - 1][2][1][0] = 0;
                            shape[shape.length - 1][2][1][1] = 0;
                            shape[shape.length - 1][2][1][2] = 1;
                            shape[shape.length - 1][2][1][3] = 0;

                            shape[shape.length - 1][2][2][0] = 0;
                            shape[shape.length - 1][2][2][1] = 0;
                            shape[shape.length - 1][2][2][2] = 1;
                            shape[shape.length - 1][2][2][3] = 0;

                            shape[shape.length - 1][2][3][0] = 0;
                            shape[shape.length - 1][2][3][1] = 0;
                            shape[shape.length - 1][2][3][2] = 1;
                            shape[shape.length - 1][2][3][3] = 0;

                            shape[shape.length - 1][3][0][0] = 0;
                            shape[shape.length - 1][3][0][1] = 0;
                            shape[shape.length - 1][3][0][2] = 0;
                            shape[shape.length - 1][3][0][3] = 0;

                            shape[shape.length - 1][3][1][0] = 0;
                            shape[shape.length - 1][3][1][1] = 1;
                            shape[shape.length - 1][3][1][2] = 1;
                            shape[shape.length - 1][3][1][3] = 1;

                            shape[shape.length - 1][3][2][0] = 0;
                            shape[shape.length - 1][3][2][1] = 0;
                            shape[shape.length - 1][3][2][2] = 0;
                            shape[shape.length - 1][3][2][3] = 0;

                            shape[shape.length - 1][3][3][0] = 0;
                            shape[shape.length - 1][3][3][1] = 0;
                            shape[shape.length - 1][3][3][2] = 0;
                            shape[shape.length - 1][3][3][3] = 0;

                            Flag_shape_2 = true;

                        }
                    }
                    if (comboBox_4.getSelectedIndex() == 2) {
                        if(Flag_shape_3 == false) {
                            shape = Arrays.copyOf(shape, shape.length + 1); //create new array from old array and allocate one more element
                            shape[shape.length - 1] = new int[4][4][4];
                            shape[shape.length - 1][0][0][0] = 0;
                            shape[shape.length - 1][0][0][1] = 0;
                            shape[shape.length - 1][0][0][2] = 0;
                            shape[shape.length - 1][0][0][3] = 0;

                            shape[shape.length - 1][0][1][0] = 0;
                            shape[shape.length - 1][0][1][1] = 1;
                            shape[shape.length - 1][0][1][2] = 0;
                            shape[shape.length - 1][0][1][3] = 0;

                            shape[shape.length - 1][0][2][0] = 0;
                            shape[shape.length - 1][0][2][1] = 0;
                            shape[shape.length - 1][0][2][2] = 1;
                            shape[shape.length - 1][0][2][3] = 0;

                            shape[shape.length - 1][0][3][0] = 0;
                            shape[shape.length - 1][0][3][1] = 0;
                            shape[shape.length - 1][0][3][2] = 0;
                            shape[shape.length - 1][0][3][3] = 1;

                            shape[shape.length - 1][1][0][0] = 0;
                            shape[shape.length - 1][1][0][1] = 0;
                            shape[shape.length - 1][1][0][2] = 0;
                            shape[shape.length - 1][1][0][3] = 0;

                            shape[shape.length - 1][1][1][0] = 0;
                            shape[shape.length - 1][1][1][1] = 0;
                            shape[shape.length - 1][1][1][2] = 0;
                            shape[shape.length - 1][1][1][3] = 1;

                            shape[shape.length - 1][1][2][0] = 0;
                            shape[shape.length - 1][1][2][1] = 0;
                            shape[shape.length - 1][1][2][2] = 1;
                            shape[shape.length - 1][1][2][3] = 0;

                            shape[shape.length - 1][1][3][0] = 0;
                            shape[shape.length - 1][1][3][1] = 1;
                            shape[shape.length - 1][1][3][2] = 0;
                            shape[shape.length - 1][1][3][3] = 0;

                            shape[shape.length - 1][2][0][0] = 0;
                            shape[shape.length - 1][2][0][1] = 0;
                            shape[shape.length - 1][2][0][2] = 0;
                            shape[shape.length - 1][2][0][3] = 0;

                            shape[shape.length - 1][2][1][0] = 0;
                            shape[shape.length - 1][2][1][1] = 1;
                            shape[shape.length - 1][2][1][2] = 0;
                            shape[shape.length - 1][2][1][3] = 0;

                            shape[shape.length - 1][2][2][0] = 0;
                            shape[shape.length - 1][2][2][1] = 0;
                            shape[shape.length - 1][2][2][2] = 1;
                            shape[shape.length - 1][2][2][3] = 0;

                            shape[shape.length - 1][2][3][0] = 0;
                            shape[shape.length - 1][2][3][1] = 0;
                            shape[shape.length - 1][2][3][2] = 0;
                            shape[shape.length - 1][2][3][3] = 1;

                            shape[shape.length - 1][3][0][0] = 0;
                            shape[shape.length - 1][3][0][1] = 0;
                            shape[shape.length - 1][3][0][2] = 0;
                            shape[shape.length - 1][3][0][3] = 0;

                            shape[shape.length - 1][3][1][0] = 0;
                            shape[shape.length - 1][3][1][1] = 0;
                            shape[shape.length - 1][3][1][2] = 0;
                            shape[shape.length - 1][3][1][3] = 1;

                            shape[shape.length - 1][3][2][0] = 0;
                            shape[shape.length - 1][3][2][1] = 0;
                            shape[shape.length - 1][3][2][2] = 1;
                            shape[shape.length - 1][3][2][3] = 0;

                            shape[shape.length - 1][3][3][0] = 0;
                            shape[shape.length - 1][3][3][1] = 1;
                            shape[shape.length - 1][3][3][2] = 0;
                            shape[shape.length - 1][3][3][3] = 0;

                            Flag_shape_3 = true;

                        }

                    }
                    if (comboBox_4.getSelectedIndex() == 3) {
                        if(Flag_shape_4 == false) {
                            shape = Arrays.copyOf(shape, shape.length + 1); //create new array from old array and allocate one more element
                            shape[shape.length - 1] = new int[4][4][4];
                            shape[shape.length - 1][0][0][0] = 0;
                            shape[shape.length - 1][0][0][1] = 1;
                            shape[shape.length - 1][0][0][2] = 0;
                            shape[shape.length - 1][0][0][3] = 0;

                            shape[shape.length - 1][0][1][0] = 0;
                            shape[shape.length - 1][0][1][1] = 1;
                            shape[shape.length - 1][0][1][2] = 0;
                            shape[shape.length - 1][0][1][3] = 0;

                            shape[shape.length - 1][0][2][0] = 0;
                            shape[shape.length - 1][0][2][1] = 0;
                            shape[shape.length - 1][0][2][2] = 1;
                            shape[shape.length - 1][0][2][3] = 0;

                            shape[shape.length - 1][0][3][0] = 0;
                            shape[shape.length - 1][0][3][1] = 0;
                            shape[shape.length - 1][0][3][2] = 0;
                            shape[shape.length - 1][0][3][3] = 0;

                            shape[shape.length - 1][1][0][0] = 0;
                            shape[shape.length - 1][1][0][1] = 0;
                            shape[shape.length - 1][1][0][2] = 0;
                            shape[shape.length - 1][1][0][3] = 0;

                            shape[shape.length - 1][1][1][0] = 0;
                            shape[shape.length - 1][1][1][1] = 1;
                            shape[shape.length - 1][1][1][2] = 1;
                            shape[shape.length - 1][1][1][3] = 0;

                            shape[shape.length - 1][1][2][0] = 1;
                            shape[shape.length - 1][1][2][1] = 0;
                            shape[shape.length - 1][1][2][2] = 0;
                            shape[shape.length - 1][1][2][3] = 0;

                            shape[shape.length - 1][1][3][0] = 0;
                            shape[shape.length - 1][1][3][1] = 0;
                            shape[shape.length - 1][1][3][2] = 0;
                            shape[shape.length - 1][1][3][3] = 0;

                            shape[shape.length - 1][2][0][0] = 1;
                            shape[shape.length - 1][2][0][1] = 0;
                            shape[shape.length - 1][2][0][2] = 0;
                            shape[shape.length - 1][2][0][3] = 0;

                            shape[shape.length - 1][2][1][0] = 0;
                            shape[shape.length - 1][2][1][1] = 1;
                            shape[shape.length - 1][2][1][2] = 0;
                            shape[shape.length - 1][2][1][3] = 0;

                            shape[shape.length - 1][2][2][0] = 0;
                            shape[shape.length - 1][2][2][1] = 1;
                            shape[shape.length - 1][2][2][2] = 0;
                            shape[shape.length - 1][2][2][3] = 0;

                            shape[shape.length - 1][2][3][0] = 0;
                            shape[shape.length - 1][2][3][1] = 0;
                            shape[shape.length - 1][2][3][2] = 0;
                            shape[shape.length - 1][2][3][3] = 0;

                            shape[shape.length - 1][3][0][0] = 0;
                            shape[shape.length - 1][3][0][1] = 0;
                            shape[shape.length - 1][3][0][2] = 1;
                            shape[shape.length - 1][3][0][3] = 0;

                            shape[shape.length - 1][3][1][0] = 1;
                            shape[shape.length - 1][3][1][1] = 1;
                            shape[shape.length - 1][3][1][2] = 0;
                            shape[shape.length - 1][3][1][3] = 0;

                            shape[shape.length - 1][3][2][0] = 0;
                            shape[shape.length - 1][3][2][1] = 0;
                            shape[shape.length - 1][3][2][2] = 0;
                            shape[shape.length - 1][3][2][3] = 0;

                            shape[shape.length - 1][3][3][0] = 0;
                            shape[shape.length - 1][3][3][1] = 0;
                            shape[shape.length - 1][3][3][2] = 0;
                            shape[shape.length - 1][3][3][3] = 0;

                            Flag_shape_4 = true;

                        }

                    }

                }
            }
        });
        comboBox_4.setSelectedIndex(-1);
        j1.add(comboBox_4);


        f.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x_1 = e.getX();
                int y_1 = e.getY();
                //System.out.println(x);
                //System.out.println(y);
                int x1 = 2 * LENGTH;
                int x2 = x1 + COL * LENGTH;
                int y1 = LENGTH;
                int y2 = y1 + ROW * LENGTH;

                if (x_1 >= x1 && x_1 <= x2 && y_1 <= y2 && y_1 >= y1) {

                    txt1.setVisible(true);
                    status = false;
                    timer.stop();
                    //newShape();

                }
                else {txt1.setVisible(false);
                status = true;
                timer.start();}

                int[] a = new int[4];
                int[] b = new int[4];
                int m = 0;

                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 4; j++){
                        if (shape[type][state][i][j]==1) {

                            a[m] = i;
                            b[m] = j;
                            m++;
                        }
                    }
                }
                //System.out.println(Arrays.deepToString(shape[1]));

                //g.fill3DRect(MARGIN_LEFT+(x+i)*LENGTH, MARGIN_UP+(y+j)*LENGTH, LENGTH, LENGTH,true);
                if( x_1>=MARGIN_LEFT+(x+a[0])*LENGTH && x_1<=MARGIN_LEFT+(x+a[0])*LENGTH+LENGTH && y_1>=MARGIN_UP+(y+b[0])*LENGTH && y_1<=MARGIN_UP+(y+b[0])*LENGTH+LENGTH){
                    newShape();
                    txt1.setVisible(true);
                    timer.stop();
                    score -= level * M;
                }
                if( x_1>=MARGIN_LEFT+(x+a[1])*LENGTH && x_1<=MARGIN_LEFT+(x+a[1])*LENGTH+LENGTH && y_1>=MARGIN_UP+(y+b[1])*LENGTH && y_1<=MARGIN_UP+(y+b[1])*LENGTH+LENGTH){
                    newShape();
                    txt1.setVisible(true);
                    timer.stop();
                    score -= level * M;
                }
                if( x_1>=MARGIN_LEFT+(x+a[2])*LENGTH && x_1<=MARGIN_LEFT+(x+a[2])*LENGTH+LENGTH && y_1>=MARGIN_UP+(y+b[2])*LENGTH && y_1<=MARGIN_UP+(y+b[2])*LENGTH+LENGTH){
                    newShape();
                    txt1.setVisible(true);
                    timer.stop();
                    score -= level * M;
                }
                if( x_1>=MARGIN_LEFT+(x+a[3])*LENGTH && x_1<=MARGIN_LEFT+(x+a[3])*LENGTH+LENGTH && y_1>=MARGIN_UP+(y+b[3])*LENGTH && y_1<=MARGIN_UP+(y+b[3])*LENGTH+LENGTH){
                    newShape();
                    txt1.setVisible(true);
                    timer.stop();
                    score -= level * M;
                }
            }
        });


        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int maxX, maxY;
                float pixelSize, centerX, centerY, rWidth = 750.0F, rHeight = 880.0F;
                Dimension d = f.getSize();
                maxX = d.width -1;
                maxY = d.height -1;
                pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
                centerX = maxX / 2;
                centerY = maxY / 2;
                int LENGTH = (int)(LENGTH_OLD/pixelSize);
                int AREA_WIDTH = LENGTH*25;
                int AREA_HEIGHT = LENGTH*23;
                bt1.setBounds(Math.round(centerX + 225/pixelSize),Math.round(centerY - (-330)/pixelSize),Math.round(centerX +300/pixelSize)-Math.round(centerX + 225/pixelSize),Math.round(centerY - (-380)/pixelSize)-Math.round(centerY - (-350)/pixelSize));
                drawarea.setPreferredSize(new Dimension(AREA_WIDTH,AREA_HEIGHT));
                txt1.setBounds(LENGTH*5,LENGTH*9,4*LENGTH,2*LENGTH);
                Font font2 = new Font("SansSerif", Font.BOLD, (int) (40 / pixelSize));
                txt1.setFont(font2);
                //j1.setBounds(0,AREA_HEIGHT,AREA_WIDTH,10*LENGTH);
                label_1.setBounds(0,(int)(LENGTH*23.5),LENGTH*4,LENGTH);
                slider1.setBounds(LENGTH*3,(int)(LENGTH*23.5),LENGTH*4,(int)(LENGTH*1.5));



                label_2.setBounds(0,LENGTH*25,LENGTH*4,LENGTH);
                slider2.setBounds(LENGTH*3,LENGTH*25,LENGTH*4,(int)(LENGTH*1.5));
                label_3.setBounds(7*LENGTH,(int)(LENGTH*23.5),LENGTH*3,LENGTH);
                slider3.setBounds(LENGTH*10,(int)(LENGTH*23.5),(int)(LENGTH*7.5),(int)(LENGTH*1.5));

                label_4.setBounds(7*LENGTH,LENGTH*25,LENGTH*2,LENGTH);
                label_5.setBounds(10*LENGTH,LENGTH*25,LENGTH*2,LENGTH);
                label_6.setBounds(13*LENGTH,LENGTH*25,LENGTH*2,LENGTH);

                comboBox_1.setBounds(LENGTH*8,LENGTH*25,LENGTH*2,LENGTH);
                comboBox_2.setBounds(LENGTH*11,LENGTH*25,LENGTH*2,LENGTH);
                comboBox_3.setBounds(LENGTH*14,LENGTH*25,LENGTH*3,LENGTH);

                label_7.setBounds(18*LENGTH,(int)(LENGTH*23.5),LENGTH*3,LENGTH);
                comboBox_4.setBounds(LENGTH*21,(int)(LENGTH*23.5),LENGTH*2,LENGTH);

            }
        });

        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    left();

                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    right();
                }

                super.mousePressed(e);
            }
        });

        f.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation()==1){

                    turn_ck();
                }
                if(e.getWheelRotation()==-1){

                    turn_cck();
                }


            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

    /*
    int iX(float x){return Math.round(centerX + x/pixelSize);}
    int iY(float y){return Math.round(centerY - y/pixelSize);}
    float fx(int x){return (x - centerX) * pixelSize;}
    float fy(int y){return (centerY - y) * pixelSize;}

     */
    private void Area(){

        int maxX, maxY;
        float pixelSize, centerX, centerY, rWidth = 750.0F, rHeight = 880.0F;
        Dimension d = f.getSize();
        maxX = d.width -1;
        maxY = d.height -1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
        int LENGTH = (int)(LENGTH_OLD/pixelSize);
        int AREA_WIDTH = LENGTH*25;
        int AREA_HEIGHT = LENGTH*25;
        int word_X = LENGTH*14;
        int word_Y = LENGTH*5;
        int MARGIN_LEFT = LENGTH*2;
        int MARGIN_UP = LENGTH;


        g.setColor(Color.white);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);

        g.setColor(Color.gray);
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(MARGIN_LEFT-offset, MARGIN_UP-offset, COL*LENGTH+offset*2, ROW*LENGTH+offset*2);
        }

        if(showGrid){
            g.setColor(Color.gray);
            for (int i = 1 ; i <= COL -1; i++){
                g.drawLine(MARGIN_LEFT+LENGTH*i, MARGIN_UP, MARGIN_LEFT+LENGTH*i, MARGIN_UP+ROW*LENGTH);
            }
            for(int i = 1; i <= ROW -1; i++){
                g.drawLine(MARGIN_LEFT, MARGIN_UP+LENGTH*i, MARGIN_LEFT+COL*LENGTH, MARGIN_UP+LENGTH*i);
            }
        }
        // show next shape

        g.setColor(Color.gray);
        g.setFont(new Font("Asas2", Font.BOLD, 20));
        g.drawString("next shape：", word_X, LENGTH*2);
        int nextX = word_X;
        int nextY = LENGTH*2;

        g.setColor(isColor?color[nextType]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[nextType][nextState][i][j]==1) {
                    g.fill3DRect(nextX+10+i*LENGTH, nextY+10+j*LENGTH, LENGTH, LENGTH,true);
                }
            }
        }
        g.setColor(Color.gray);
        g.setFont(new Font("Tetris", Font.BOLD, 15));

        g.drawString("level: " + level, word_X, word_Y+LENGTH*3);
        g.drawString("line: " + line, word_X, word_Y+LENGTH*5);
        g.drawString("Score：" + score, word_X, word_Y+LENGTH*7);
        g.drawString("New Shape: ",word_X,word_Y+LENGTH*9);
        g.drawString("1. ",word_X,word_Y+LENGTH*10);
        g.drawRect(word_X+LENGTH*2,word_Y+LENGTH*10,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*1,word_Y+LENGTH*11,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*2,word_Y+LENGTH*11,LENGTH,LENGTH);
        g.drawString("2. ",word_X+LENGTH*3,word_Y+LENGTH*10);
        g.drawRect(word_X+LENGTH*4,word_Y+LENGTH*11,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*5,word_Y+LENGTH*11,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*6,word_Y+LENGTH*11,LENGTH,LENGTH);
        g.drawString("3. ",word_X,word_Y+LENGTH*13);
        g.drawRect(word_X+LENGTH,word_Y+LENGTH*13,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*2,word_Y+LENGTH*14,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*3,word_Y+LENGTH*15,LENGTH,LENGTH);
        g.drawString("4. ",word_X+LENGTH*3,word_Y+LENGTH*13);
        g.drawRect(word_X+LENGTH*4,word_Y+LENGTH*13,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*5,word_Y+LENGTH*13,LENGTH,LENGTH);
        g.drawRect(word_X+LENGTH*6,word_Y+LENGTH*14,LENGTH,LENGTH);


        //draw down shape
        g.setColor(isColor?color[type]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[type][state][i][j]==1) {
                    g.fill3DRect(MARGIN_LEFT+(x+i)*LENGTH, MARGIN_UP+(y+j)*LENGTH, LENGTH, LENGTH,true);
                }
            }
        }
        //draw map
        for(int i = 0; i < COL; i++){
            for(int j = 0; j < ROW; j++){
                if (map[i][j] == 1) {
                    g.setColor(mapColor[i][j]);
                    g.fill3DRect(MARGIN_LEFT+i*LENGTH, MARGIN_UP+j*LENGTH, LENGTH, LENGTH,true);
                }
            }
        }
    drawarea.repaint();
    }


    private class MyCanvas extends JPanel{

        public void paint(Graphics g){
            //super.paint(g);
            int maxX, maxY;
            float pixelSize, centerX, centerY, rWidth = 750.0F, rHeight = 880.0F;
            Dimension d = f.getSize();
            maxX = d.width -1;
            maxY = d.height -1;
            pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
            centerX = maxX / 2;
            centerY = maxY / 2;
            int LENGTH = (int)(LENGTH_OLD/pixelSize);
            int AREA_WIDTH = LENGTH*25;
            int AREA_HEIGHT = LENGTH*23;
            //int width = Math.round(centerX + 360/pixelSize)-Math.round(centerX -375/pixelSize);
            //int height = Math.round(centerY - (-345)/pixelSize)-Math.round(centerY - (425)/pixelSize);
            g.drawImage(image,0,0,AREA_WIDTH,AREA_HEIGHT,null);
        }

    }

   
    private boolean check(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if ( (shape[type][state][i][j] == 1) && ( (x+i>=COL) || (x+i<0 ) || (y+j>=ROW) || (map[x+i][y+j]==1) ) ) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean GameisOver(int type, int state, int x, int y){
        return !check(type, state, x, y);
    }



    private void newShape(){
        Random rand = new Random();
        if(newBegin){
            type = rand.nextInt(shape.length);
            state = rand.nextInt(4);
            newBegin = false;
        }
        else{
            type = nextType;
            state = nextState;
        }
        nextType = rand.nextInt(shape.length);
        nextState = rand.nextInt(4);
        x = 3;
        y = 0;

        // if the game is end, restart
        if(GameisOver(type, state, x, y)){
            JOptionPane.showMessageDialog(f, "GAME IS END!");
            newGame();
        }


        Area();

    }

    // new game
    private void newGame(){
        newMap();
        score = 0;
        newBegin = true;
    }

    //clean the map
    private void newMap(){
        for(int i = 0; i < COL; i++){
            Arrays.fill(map[i],0);
        }
    }


    private void delLine(){
        boolean flag = true;
        int addScore = 0;
        int addLine = 0;
        for(int j = 0; j < ROW; j++){
            flag = true;
            for( int i = 0; i < COL; i++){
                if (map[i][j]==0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                addLine += 1;
                addScore += 10;
                for(int t = j; t > 0; t--){
                    for(int i = 0; i <COL; i++){
                        map[i][t] = map[i][t-1];
                    }
                }
            }
        }
        line += addLine;

        if( (line > 0) && (line % N == 0)){
           level += 1;
        }


        score += (addScore*addScore/COL) + level*M;
    }



    //add timelistener
    private class timerListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            timer.setDelay((int)(1000/FS));

            if(check(type, state , x, y+1) ){
                y = y + 1;
            }
            else{
                add(type, state, x, y);
                delLine();
                newShape();
            }

            Area();
        }
    }


    private void add(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4 ; j++){
                if((y+j<ROW)&&(x+i<COL)&&(x+i>=0)&&(map[x+i][y+j]==0)){
                    map[x+i][y+j]=shape[type][state][i][j];
                    mapColor[x+i][y+j]=color[isColor?type:DEFAULT];
                }
            }
        }
    }

    private void turn_ck(){
        int tmpState = state;
        state = (state + 1)%4;
        if (!check(type,state, x, y )) {
            state = tmpState;
        }
        Area();
    }

    private void turn_cck(){
        int tmpState = state;
        state = (state + 3)%4;
        if (!check(type,state, x, y )) {
            state = tmpState;
        }
        Area();
    }

    private void left(){
        if(check(type,state, x-1, y)){
            --x;
        }
        Area();
    }

    private void right(){
        if (check(type,state, x+1, y)) {
            ++x;
        }
        Area();
    }

    private void down(){
        if (check(type,state, x, y+1)) {
            ++y;
        }
        else{
            add(type, state, x, y);
            delLine();
            newShape();
        }
        Area();
    }


    public static void main(String[] args){
        new Tetris().init();
    }
}
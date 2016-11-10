package client;

import source.Daemon;
import source.ObjectReceiver;
import source.Server;
import source.World;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class Frame extends JPanel implements ObjectReceiver, Runnable,
        KeyListener {

    private Daemon virtualServer;
    private World world;
    private JFrame frame;
    private Byte id;
    private int w;
    private boolean firstReceive = true;
    private int dx = 0, dy = 0;

    public Frame() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.add(this);
        world = new World();
        addKeyListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        setFocusable(true);
        requestFocusInWindow();
        setSize(frame.getWidth(), frame.getHeight());
        setVisible(true);
        connect();
    }

    public void connect() {
        String input = JOptionPane.showInputDialog("IP");
        if (input != null) {
            String ip = "";
            int port = Server.DEFAULT_PORT;
            if (input.equalsIgnoreCase("zach")) {
                ip = "137.48.185.138";
            } else {
                String[] tokens = input.split(":");
                ip = tokens[0];
                if (tokens.length > 1) {
                    try {
                        port = Integer.parseInt(tokens[1]);
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
            try {
                Socket s = new Socket(ip, port);
                System.out.println("new Daemon");
                Daemon d = new Daemon(this);
                d.connect(s);
                Object o = d.readObject();
                if (o != null && o.equals("p")) {
                    d.send("pp");
                    id = (Byte) d.readObject();
                    virtualServer = d;
                    System.out.println("Connecting...");
                    virtualServer.start();
                    new Thread(this).run();
                }
            } catch (UnknownHostException e) {
                fail();
            } catch (IOException e) {
                fail();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            // System.out.println("UPDATE");
            world.update(.016);
            System.out.println("dx: " + dx);
            //update
            repaint();
            try {
                dx = (int) (world.getPlayers().get(id).getX() - 100) * -1;
            } catch (NullPointerException npe) {

            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {

            }
        }
    }

    private static void fail() {
        JOptionPane.showMessageDialog(null, "That didn't work.", "Guess What?",
                JOptionPane.ERROR_MESSAGE);
        System.exit(5);
    }

    @Override
    public void receive(Object o) {
        boolean isNumber = false;
        try {
            isNumber = Integer.parseInt(o.toString()) >= 0;
        } catch (NumberFormatException e) {
        }
        if (o.equals("r")) {
            repaint();
        }
        if (o.toString().contains("j")) {
            String[] corn = o.toString().split(":");
            byte id = Byte.parseByte(corn[0]);
            world.getPlayers().get(id).jump();
        } else if (firstReceive && isNumber) {
            id = Byte.parseByte(o.toString());
            firstReceive = false;
        } else if (o instanceof Color) {
            frame.setBackground((Color) o);
        } else if (o instanceof Integer) {
            w = Integer.parseInt(o.toString());
        }
        if (o instanceof World) {
            world = (World) o;
            // world.setPlayers(((World) o).getState());
        }
    }

    private void send(Serializable s) {
        try {
            virtualServer.send(id + ":" + s);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Client.send");
        }
    }

    private void pong() {
        try {
            virtualServer.send("p");
        } catch (IOException e) {
            System.out.println("Client.send");
        }
        // System.out.println("Done");

    }

    @Override
    public void paintComponent(Graphics g) {
        Image i = createImage(getWidth(), getHeight());
        Graphics h = i.getGraphics();
        bufferPaint(h);
        h.setColor(Color.white);
        try {
            h.drawString(world.getPlayers().get(id).getDeathCount() + "", 100,
                    (int) world.getPlayers().get(id).getY() - 20);
        } catch (NullPointerException npe) {
        }
        g.drawImage(i, 0, 0, null);
    }

    public void bufferPaint(Graphics g) {
        world.paint(g, dx, dy);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (world.getPlayers().get(id).isOnSurface())
                send("j");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

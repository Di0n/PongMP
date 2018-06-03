package framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public abstract class Game extends JFrame implements ActionListener
{
    public final static Dimension PLAY_AREA = new Dimension(1200, 600);

    private long lastTime;
    private boolean closing;
    private final int tickRate;
    private Timer gameTimer;

    protected final Canvas canvas;

    protected boolean antiAliasing;


    public Game(String name, int tickRate)
    {
        super(name);
        this.tickRate = tickRate;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                onClosing();
                super.windowClosing(e);
            }
        });

        canvas = new Canvas();

        canvas.setPreferredSize(PLAY_AREA);
        canvas.setMinimumSize(PLAY_AREA);
        canvas.setMaximumSize(PLAY_AREA);


        add(canvas);

        setResizable(false);

        pack();
    }

    public void start()
    {
        setVisible(true);

        loadContent();

        canvas.setIgnoreRepaint(true);
        canvas.createBufferStrategy(2);

        lastTime = System.nanoTime();

        gameTimer = new Timer(1000/tickRate, this);
        gameTimer.start();

        /*TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                Game.this.run();
            }
        };
         gameTimer.schedule(task, 100, 1000/tickRate);
        */
        /*Thread thread = new Thread()
        {
            int x;
            public void run()
            {
                while (!closing)
                {
                    gameLoop();

                    synchronized (this)
                    {
                        try
                        {
                            wait(1);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        */
    }

    @Override
    public final void actionPerformed(ActionEvent e)
    {
        long time = System.nanoTime();
        double deltaTime = (time - lastTime) / 1e9;
        lastTime = time;

        update(deltaTime);

        Graphics2D g2d = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();
        clear(g2d);

        if (antiAliasing)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform ot = g2d.getTransform();

        draw(g2d);

        g2d.setTransform(ot);
        g2d.dispose();

        BufferStrategy strategy = canvas.getBufferStrategy();
        if (!strategy.contentsLost()) {
            strategy.show();
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void clear(Graphics2D g2d)
    {
        g2d.setColor(Color.BLACK);

        /*final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        g2d.fillRect(-width / 2, -height / 2, width, height);*/
    }


    protected abstract void onClosing();
    protected abstract void loadContent();
    protected abstract void draw(Graphics2D g2d);
    protected abstract void update(double deltaTime);

    public synchronized void stop()
    {
        closing = true;
    }

    public void addKeyboardListener(KeyAdapter keyAdapter)
    {
        this.canvas.addKeyListener(keyAdapter);
    }

    public void addMouseListener(MouseAdapter mouseAdapter)
    {
        this.canvas.addMouseListener(mouseAdapter);
    }



}

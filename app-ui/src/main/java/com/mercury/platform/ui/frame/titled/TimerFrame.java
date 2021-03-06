package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 06.01.2017.
 */
public class TimerFrame extends AbstractTitledComponentFrame {
    private Timer timeAgo;
    private JLabel timeLabel;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private int mapCount = 0;
    private int chaosSpend = 0;
    public TimerFrame() {
        super();
    }

    @Override
    protected void initialize() {
        super.initialize();
        JPanel root = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JButton play = componentsFactory.getIconButton("app/timer-play.png", 16,AppThemeColor.FRAME_ALPHA, TooltipConstants.PLAY);
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                getNewTimer().start();
            }
        });
        JButton pause = componentsFactory.getIconButton("app/timer-pause.png", 16,AppThemeColor.FRAME_ALPHA,TooltipConstants.PAUSE);
        pause.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                }
            }
        });
        JButton stop = componentsFactory.getIconButton("app/timer-stop.png", 16,AppThemeColor.FRAME_ALPHA,TooltipConstants.STOP);
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                    seconds = 0;
                    minutes = 0;
                    hours = 0;
                }
            }
        });
        JLabel mapCountLabel = componentsFactory.getTextLabel("Map count: 0");
        JLabel chaosSpendLabel = componentsFactory.getTextLabel("Chaos spend: 0");
        JButton reset = componentsFactory.getIconButton("app/timer-reset.png", 16,AppThemeColor.FRAME_ALPHA, TooltipConstants.RESET);
        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                }
                timeLabel.setText("00:00:00");
                seconds = 0;
                minutes = 0;
                hours = 0;
                mapCount = 0;
                chaosSpend = 0;
                mapCountLabel.setText("Map count: 0");
                chaosSpendLabel.setText("Chaos spend: 0");
                TimerFrame.this.repaint();
            }
        });
        root.add(getTimePanel());
        root.add(play);
        root.add(pause);
        root.add(stop);
        root.add(reset);
        this.add(root,BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(null);
        miscPanel.setLayout(new BoxLayout(miscPanel,BoxLayout.Y_AXIS));

        JPanel mapCountPanel = getIncrementRow(mapCountLabel, new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mapCount++;
                mapCountLabel.setText("Map count: " + mapCount);
                TimerFrame.this.repaint();
            }
        }, new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(mapCount > 0) {
                    mapCount--;
                    mapCountLabel.setText("Map count: " + mapCount);
                    TimerFrame.this.repaint();
                }
            }
        });
        JPanel chaosSpendPanel = getIncrementRow(chaosSpendLabel, new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                chaosSpend++;
                chaosSpendLabel.setText("Chaos spend: " + chaosSpend);
                TimerFrame.this.repaint();
            }
        }, new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chaosSpend > 0) {
                    chaosSpend--;
                    chaosSpendLabel.setText("Chaos spend: " + chaosSpend);
                    TimerFrame.this.repaint();
                }
            }
        });

        miscPanel.add(mapCountPanel);
        miscPanel.add(chaosSpendPanel);
        this.add(miscPanel,BorderLayout.PAGE_END);
        this.pack();
    }
    private Timer getNewTimer(){
        timeAgo = new Timer(1000, e -> {
            String labelText = "";
            seconds++;
            if (seconds > 59) {
                minutes++;
                seconds = 0;
                if (minutes > 59) {
                    hours++;
                    minutes = 0;
                }
            }
            String secLabel = ((seconds/10.0) >= 1f)?String.valueOf(seconds) : "0" + seconds;
            String minLabel = ((minutes/10.0) >= 1f)?String.valueOf(minutes) : "0" + minutes;
            String hLabel = ((hours/10.0) >= 1f)?String.valueOf(hours) : "0" + hours;
            if (minutes == 0 && hours == 0) {
                labelText = "00:00:" + secLabel;
            } else if (minutes > 0) {
                labelText = "00:" + minLabel + ":" + secLabel;
            } else if (hours > 0) {
                labelText = hLabel + ":" + minLabel + ":" + secLabel;
            }
            timeLabel.setText(labelText);
            TimerFrame.this.repaint();
        });
        return timeAgo;
    }
    private JPanel getIncrementRow(JLabel label, MouseAdapter plusListener, MouseAdapter minusListener){
        JPanel panel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JButton plus = componentsFactory.getIconButton("app/invite.png", 14,AppThemeColor.FRAME_ALPHA,"");
        plus.addMouseListener(plusListener);
        JButton minus = componentsFactory.getIconButton("app/kick.png", 14,AppThemeColor.FRAME_ALPHA,"");
        minus.addMouseListener(minusListener);
        panel.add(label);
        panel.add(plus);
        panel.add(minus);
        return panel;
    }

    private JPanel getTimePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 18, "00:00:00");
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BUTTON, 1));
        panel.setMinimumSize(new Dimension(100,30));
        panel.setPreferredSize(new Dimension(100,30));
        panel.add(timeLabel);
        return panel;
    }

    @Override
    public void subscribe() {
    }

    @Override
    protected String getFrameTitle() {
        return "Timer";
    }
}

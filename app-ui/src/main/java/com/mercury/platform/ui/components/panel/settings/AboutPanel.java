package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.core.MercuryConstants;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Константин on 05.01.2017.
 */
public class AboutPanel extends JPanel implements HasUI {
    private ComponentsFactory componentsFactory;
    public AboutPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JPanel imgPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        imgPanel.add(componentsFactory.getIconLabel("app/app-icon-big.png"));
        this.add(imgPanel);

        JPanel aboutPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        aboutPanel.add(getInfoPanel());

        this.add(aboutPanel);
    }

    private JPanel getInfoPanel(){
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JPanel titlePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(componentsFactory.getTextLabel("MercuryTrade", FontStyle.REGULAR));
        panel.add(titlePanel);
        JPanel versionPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        versionPanel.add(componentsFactory.getTextLabel("App version: " + MercuryConstants.APP_VERSION, FontStyle.REGULAR));
        panel.add(versionPanel);

        JLabel redditButton = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,16f,"Reddit");
        redditButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.reddit.com/r/pathofexile/comments/5xjof4/introducing_mercurytrade_the_ultimate_solution/"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        JLabel githubButton = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,16f,"Github");
        githubButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Exslims/MercuryTrade/issues"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JLabel discordButton = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,16f,"Discord");
        discordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://discord.gg/aG9C8XJ"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JPanel feedbackPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        feedbackPanel.add(componentsFactory.getTextLabel("Feedback & Suggestions: ", FontStyle.REGULAR));
        feedbackPanel.add(redditButton);
        feedbackPanel.add(githubButton);
        feedbackPanel.add(discordButton);

        panel.add(feedbackPanel);
        return panel;
    }
}

package com.mercury.platform.ui.components.panel.adr.group;

import com.mercury.platform.shared.entity.adr.AdrCellDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.adr.ui.SquareMercuryIconTrackerUI;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.adr.group.AdrCellSettingsFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrGroupCellPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private AdrCellDescriptor descriptor;
    private Timeline progressTl;
    private AdrCellSettingsFrame settingsFrame;
    public AdrGroupCellPanel(AdrCellDescriptor cellDescriptor, ComponentsFactory componentsFactory) {
        super(new GridLayout(1,1));
        this.descriptor = cellDescriptor;
        this.componentsFactory = componentsFactory;
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setPreferredSize(cellDescriptor.getCellSize());
        this.setBorder(null);
        this.settingsFrame = new AdrCellSettingsFrame();
        this.settingsFrame.init();
        this.createUI();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    settingsFrame.showSettings(descriptor,e.getLocationOnScreen());
                }
            }
        });
    }

    @Override
    public void createUI() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBorder(null);
        progressBar.setFont(componentsFactory.getFont(FontStyle.BOLD,36));
        progressBar.setForeground(AppThemeColor.TEXT_DEFAULT);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(AppThemeColor.TRANSPARENT);
        progressBar.setUI(new SquareMercuryIconTrackerUI(descriptor.getIconPath()));
        progressBar.setValue((int)(descriptor.getDuration()*1000));
        progressBar.setMaximum((int)(descriptor.getDuration()*1000));
        add(progressBar,BorderLayout.CENTER);

        this.progressTl = new Timeline(progressBar);
        this.progressTl.setDuration((int)(descriptor.getDuration()*1000));
        this.progressTl.addPropertyToInterpolate("value",progressBar.getMaximum(),0);
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }
}
package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;

public class NotificationFrame extends AbstractOverlaidFrame {
    private JLabel messageLabel;
    private Timeline showAnimation;
    public NotificationFrame() {
        super();
        this.setOpacity(0.9f);
        this.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    protected void initialize() {
        messageLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER,38,"");

        showAnimation = new Timeline(this);
        showAnimation.setDuration(1400);
        showAnimation.addPropertyToInterpolate("opacity", 0.9f, 0f);
        showAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.DONE)){
                    NotificationFrame.this.setAlwaysOnTop(false);
                    NotificationFrame.this.setVisible(false);
                    NotificationFrame.this.setOpacity(0.9f);
                    messageLabel.setText("");
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });
        this.add(messageLabel);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.notificationSubject.subscribe(message -> {
            messageLabel.setText(message);
            this.pack();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.setAlwaysOnTop(true);
            this.setVisible(true);
            showAnimation.abort();
            showAnimation.play();
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout();
    }
}

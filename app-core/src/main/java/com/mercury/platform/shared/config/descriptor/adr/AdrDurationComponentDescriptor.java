package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrDurationComponentDescriptor extends AdrComponentDescriptor implements Serializable {
    private String iconPath = "default_icon.png";
    private Double duration = 3.4d;
    private Double delay = 0d;
    private float outlineThickness = 0.2f;
    private Color outlineColor = new Color(45, 55, 54,180);
    private int fontSize = 28;
    private boolean textEnable = true;
    private boolean iconEnable = true;
    private boolean maskEnable = true;
    private boolean alwaysVisible;
    private boolean invertMask;
    private boolean invertTimer;
    private String textFormat = "0.0";
    private Color backgroundColor = new Color(59, 59, 59);
    private Color foregroundColor = new Color(59, 59, 59, 190);
    private Color lowValueTextColor = new Color(224,86,60);
    private Color mediumValueTextColor = new Color(255,211,78);
    private Color defaultValueTextColor = new Color(255,250,213);
    private Color borderColor = new Color(16,110,99);
    private Double lowValueTextThreshold = 1d;
    private Double mediumValueTextThreshold = 3d;
    private Double defaultValueTextThreshold = 5d;
    private int thickness = 1;
    private Insets insets = new Insets(0,0,0,0);
    private boolean bindToTextColor;
}

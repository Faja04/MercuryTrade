package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class NotificationPanelSettings extends ConfigurationPanel{
    private List<ValuePair> inputs;
    private JPanel buttonsTable;
    private JCheckBox dismissCheckBox;
    private JCheckBox showLeagueCheckBox;
    private PlainConfigurationService<NotificationDescriptor> notificationService;
    private int id;

    public NotificationPanelSettings() {
        super();
        this.notificationService = Configuration.get().notificationConfiguration();
        this.createUI();
    }

    @Override
    public boolean processAndSave() {
        List<ResponseButtonDescriptor> buttons = new ArrayList<>();
        id = 0;
        inputs.forEach(pair -> {
            buttons.add(new ResponseButtonDescriptor(
                    id,
                    pair.kick.isSelected(),
                    pair.close.isSelected(),
                    pair.title.getText(),
                    pair.response.getText()));
            id++;
        });
        this.notificationService.get().setButtons(buttons);
        this.notificationService.get().setDismissAfterKick(this.dismissCheckBox.isSelected());
        this.notificationService.get().setShowLeague(this.showLeagueCheckBox.isSelected());
        MercuryStoreCore.buttonsChangedSubject.onNext(true);
        return true;
    }

    @Override
    public void restore() {
        verticalScrollContainer.removeAll();
        createUI();
    }

    @Override
    public void createUI() {
        JPanel otherSettings = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel settingLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "Customization");
        settingLabel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                new EmptyBorder(3,5,3,5)));

        otherSettings.add(settingLabel,BorderLayout.PAGE_START);
        otherSettings.add(closeOnKickPanel(),BorderLayout.CENTER);


        JPanel responseButtons = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel responseLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "Response buttons");
        responseLabel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                new EmptyBorder(3,5,3,5)));
        responseButtons.add(responseLabel,BorderLayout.PAGE_START);
        responseButtons.add(getButtonsTable(),BorderLayout.CENTER);

        verticalScrollContainer.add(otherSettings);
        verticalScrollContainer.add(responseButtons);
    }
    private JPanel closeOnKickPanel() {
        JPanel topPanel = componentsFactory.getTransparentPanel(new GridLayout(2,2));
        topPanel.add(componentsFactory.getTextLabel("Close Notification panel on Kick:", FontStyle.REGULAR));
        dismissCheckBox = this.componentsFactory.getCheckBox(this.notificationService.get().isDismissAfterKick());
        topPanel.add(dismissCheckBox);

        topPanel.add(componentsFactory.getTextLabel("Show league:", FontStyle.REGULAR));
        showLeagueCheckBox = this.componentsFactory.getCheckBox(this.notificationService.get().isShowLeague());
        topPanel.add(showLeagueCheckBox);

        topPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3,0,3,0)));
        topPanel.setBackground(AppThemeColor.SETTINGS_BG);
        return topPanel;
    }
    private JPanel getButtonsTable() {
        buttonsTable = componentsFactory.getTransparentPanel(new GridBagLayout());
        buttonsTable.setBackground(AppThemeColor.SETTINGS_BG);
        inputs = new ArrayList<>();
        List<ResponseButtonDescriptor> buttonsConfig = this.notificationService.get().getButtons();
        Collections.sort(buttonsConfig);
        GridBagConstraints titleColumn = new GridBagConstraints();
        GridBagConstraints valueColumn = new GridBagConstraints();
        GridBagConstraints kickColumn = new GridBagConstraints();
        GridBagConstraints closeColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        setUpGBConstants(titleColumn,valueColumn,kickColumn,closeColumn,utilColumn);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Label");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Response text");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel closeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Close");
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        buttonsTable.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        buttonsTable.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        buttonsTable.add(closeLabel,closeColumn);
        closeColumn.gridy++;
        buttonsTable.add(componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,""),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach(button ->{
            addNewRow(button.getTitle(),button.getResponseText(),button.isKick(),button.isClose(),titleColumn,valueColumn,kickColumn,closeColumn,utilColumn);
        });

        JButton addNew = componentsFactory.getBorderedButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (inputs.size() <= 12) {
                        buttonsTable.remove(addNew);
                        addNewRow("expl", "example",false,false, titleColumn, valueColumn,kickColumn,closeColumn, utilColumn);
                        buttonsTable.add(addNew, utilColumn);

                        MercuryStoreUI.packSubject.onNext(SettingsFrame.class);
                    }
                }
            }
        });
        buttonsTable.add(addNew,utilColumn);
        return buttonsTable;
    }
    private void addNewRow(String title, String value, boolean kick,
                           boolean close,
                           GridBagConstraints tC,
                           GridBagConstraints vC,
                           GridBagConstraints kC,
                           GridBagConstraints cC,
                           GridBagConstraints uC){
        JTextField titleFiled = componentsFactory.getTextField(title,FontStyle.REGULAR,15f);
        titleFiled.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(titleFiled.getText().length() > 10){
                    e.consume();
                }
            }
        });

        buttonsTable.add(titleFiled,tC);
        tC.gridy++;

        JTextField valueField = componentsFactory.getTextField(value,FontStyle.REGULAR,15f);
        buttonsTable.add(valueField,vC);
        vC.gridy++;

        JPanel kickWrapper = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox kickCheckBox = this.componentsFactory.getCheckBox(kick);
        kickCheckBox.setBackground(AppThemeColor.TRANSPARENT);
        kickWrapper.add(kickCheckBox);
        kC.gridy++;

        JCheckBox closeCheckBox = componentsFactory.getCheckBox(close,"Close notification panel on click");
        closeCheckBox.setPreferredSize(new Dimension(32,32));
        buttonsTable.add(closeCheckBox,cC);
        cC.gridy++;
        ValuePair pair = new ValuePair(titleFiled, valueField, kickCheckBox, closeCheckBox);
        inputs.add(pair);


        JButton remove = componentsFactory.getBorderedButton("x");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    inputs.remove(pair);
                    buttonsTable.remove(titleFiled);
                    buttonsTable.remove(valueField);
                    buttonsTable.remove(closeCheckBox);
                    buttonsTable.remove(remove);

                    MercuryStoreUI.packSubject.onNext(SettingsFrame.class);
                }
            }
        });
        buttonsTable.add(remove,uC);
        uC.gridy++;
    }
    private void setUpGBConstants(GridBagConstraints titleColumn,
                                  GridBagConstraints valueColumn,
                                  GridBagConstraints kickColumn,
                                  GridBagConstraints closeColumn,
                                  GridBagConstraints utilColumn){
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        kickColumn.fill = GridBagConstraints.HORIZONTAL;
        closeColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        titleColumn.weightx = 0.09f;
        valueColumn.weightx = 0.9f;
        kickColumn.weightx = 0.002f;
        closeColumn.weightx = 0.002f;
        utilColumn.weightx = 0.002f;

        titleColumn.anchor = GridBagConstraints.NORTHWEST;
        valueColumn.anchor = GridBagConstraints.NORTHWEST;
        kickColumn.anchor = GridBagConstraints.NORTHWEST;
        closeColumn.anchor = GridBagConstraints.NORTHWEST;
        utilColumn.anchor = GridBagConstraints.NORTHWEST;

        titleColumn.gridy = 0;
        titleColumn.gridx = 1;
        valueColumn.gridy = 0;
        valueColumn.gridx = 2;
        kickColumn.gridy = 0;
        kickColumn.gridx = 3;
        closeColumn.gridy = 0;
        closeColumn.gridx = 4;
        utilColumn.gridy = 0;
        utilColumn.gridx = 5;

        utilColumn.insets = new Insets(3,2,3,0);
        titleColumn.insets = new Insets(3,2,3,0);
        kickColumn.insets = new Insets(3,2,3,0);
        closeColumn.insets = new Insets(3,2,3,0);
        valueColumn.insets = new Insets(3,2,3,0);
    }
    private class ValuePair {
        private JTextField title;
        private JTextField response;
        private JCheckBox kick;
        private JCheckBox close;

        public ValuePair(JTextField title, JTextField response, JCheckBox kick, JCheckBox close) {
            this.title = title;
            this.response = response;
            this.kick = kick;
            this.close = close;
        }
    }
}

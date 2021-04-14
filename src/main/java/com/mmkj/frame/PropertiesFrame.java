package com.mmkj.frame;

import com.mmkj.StartWindows;
import com.mmkj.util.Properties;
import com.mmkj.util.StringUtils;
import com.mmkj.util.SwitchUtil;
import com.mmkj.util.WindowsUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

/**
 * @author by jicai on 2019/7/31.
 */
@Slf4j
public class PropertiesFrame extends Frame {
    private int ybasic = 500;
    private int yleng = 40;

    public PropertiesFrame() {
        this.setSize(900, 1000);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        WindowsUtil.setWindows(this);
        WindowsUtil.addPic(this);
    }

    public void setCompoments(StartWindows startWindows) {
        Properties properties = Properties.getInstance();
        Panel panel = new Panel();
        this.add(panel);
        panel.setLayout(null);

        Panel panelLeft = new Panel();
        panelLeft.setBounds(50, 60, 800, 300);
        panelLeft.setLayout(new GridLayout(startWindows.getSqlMap().size(), 3, 5, 10));

        Label selectLabel = new Label("配置列表:");
        selectLabel.setBounds(45, 20,60,30);
        panel.add(selectLabel);
        ScrollPane scrollPaneLeft = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPaneLeft.setBounds(45, 55, 810, 310);
        scrollPaneLeft.add(panelLeft);
        panel.add(scrollPaneLeft);
        CheckboxGroup checkboxGroup = new CheckboxGroup();
        properties.getMap().forEach((s, s2) -> {
            Checkbox checkbox = new Checkbox(s2);
            checkbox.setCheckboxGroup(checkboxGroup);
            if(s.equals(properties.getIdNow())){
                checkbox.setState(true);
            }
            panelLeft.add(checkbox);
        });

        Button qrButton = new Button("确认");
        qrButton.setBounds(45, 370, 70, 30);
        panel.add(qrButton);
        qrButton.addActionListener( p -> {
            Checkbox checkbox = checkboxGroup.getSelectedCheckbox();
            if(checkbox == null){
                return;
            }
            for (Map.Entry<String, String> entry : properties.getMap().entrySet()){
                if(entry.getValue().equals(checkbox.getLabel())){
                    try{
                        properties.setProperties(entry.getKey());
                        this.setVisible(false);
                        BasicInfoFrame basicInfoFrame = new BasicInfoFrame();
                        DataInfoFrame dataInfoFrame = new DataInfoFrame();
                        basicInfoFrame.setMyCompoments(dataInfoFrame, startWindows);
                    }catch (Exception e){
                        log.error("连接数据库失败:", e);
                        if(SwitchUtil.LOG){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });



        Button deleteButton = new Button("删除");
        deleteButton.setBounds(125, 370, 70, 30);
        panel.add(deleteButton);
        deleteButton.addActionListener( l -> {
            Checkbox checkbox = checkboxGroup.getSelectedCheckbox();
            if(checkbox == null){
                return;
            }
            for (Map.Entry<String, String> entry : properties.getMap().entrySet()){
                if(entry.getValue().equals(checkbox.getLabel())){
                    try{
                        properties.deleteProperties(entry.getKey());

                        PropertiesFrame newFrame = new PropertiesFrame();
                        newFrame.setCompoments(startWindows);
                        newFrame.setVisible(true);
                        this.setVisible(false);
                    }catch (Exception e){
                        log.error("删除配置失败:", e);
                        if(SwitchUtil.LOG){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });

        Button returnButton = new Button("返回主页");
        returnButton.setBounds(205, 370, 70, 30);
        panel.add(returnButton);
        returnButton.addActionListener(l -> {
            this.setVisible(false);
            BasicInfoFrame basicInfoFrame = new BasicInfoFrame();
            DataInfoFrame dataInfoFrame = new DataInfoFrame();
            basicInfoFrame.setMyCompoments(dataInfoFrame, startWindows);
        });

        Label addLabel = new Label("新增配置选项:");
        addLabel.setBounds(50, ybasic - 30,100,30);
        panel.add(addLabel);

        Label addipLabel = new Label("Ip:");
        addipLabel.setBounds(50, ybasic,50,30);
        panel.add(addipLabel);

        TextField addipText = new TextField(20);
        addipText.setBounds(165, ybasic,690,35);
        panel.add(addipText);

        Label addtableNameLabel = new Label("库名:");
        addtableNameLabel.setBounds(50,ybasic + yleng * 1,80,35);
        panel.add(addtableNameLabel);

        TextField addtableNameText = new TextField(20);
        addtableNameText.setBounds(165,ybasic + yleng * 1,690,35);
        panel.add(addtableNameText);

        Label addusernameLabel = new Label("username:");
        addusernameLabel.setBounds(50,ybasic + yleng * 2,80,35);
        panel.add(addusernameLabel);

        TextField addusernameText = new TextField(20);
        addusernameText.setBounds(165,ybasic + yleng * 2,690,35);
        panel.add(addusernameText);


        Label addpasswordLabel = new Label("password:");
        addpasswordLabel.setBounds(50,ybasic + yleng * 3,80,35);
        panel.add(addpasswordLabel);

        TextField addpasswordText = new TextField(20);
        addpasswordText.setBounds(165,ybasic + yleng * 3,690,35);
        panel.add(addpasswordText);

        Label addbasicFileStructureUrl = new Label("包名(例:com.xbz):");
        addbasicFileStructureUrl.setBounds(50,ybasic + yleng * 4,100,35);
        panel.add(addbasicFileStructureUrl);

        TextField addbasicFileStructureText = new TextField(20);
        addbasicFileStructureText.setBounds(165,ybasic + yleng * 4,690,35);
        panel.add(addbasicFileStructureText);

        Label deleteFileStartLabel = new Label("删开头(例:XbzApi):");
        deleteFileStartLabel.setBounds(50,ybasic + yleng * 5,100,35);
        panel.add(deleteFileStartLabel);

        TextField deleteFileStartText = new TextField(20);
        deleteFileStartText.setBounds(165,ybasic + yleng * 5,690,35);
        panel.add(deleteFileStartText);

        Label addeditionLabel = new Label("MySql版本:");
        addeditionLabel.setBounds(50,ybasic + yleng * 6,100,35);
        panel.add(addeditionLabel);
        CheckboxGroup addeditionGroup = new CheckboxGroup();
        Checkbox addeditionButton1 = new Checkbox("5.0");
        Checkbox addeditionButton2 = new Checkbox("8.0");
        addeditionButton1.setBounds(165,ybasic + yleng * 6,70,40);
        addeditionButton2.setBounds(235,ybasic + yleng * 6,70,40);
        panel.add(addeditionButton1);
        panel.add(addeditionButton2);
        addeditionButton1.setCheckboxGroup(addeditionGroup);
        addeditionButton2.setCheckboxGroup(addeditionGroup);
        addeditionButton1.setState( true);

        Label addswaggerLabel = new Label("开启swagger:");
        addswaggerLabel.setBounds(50,ybasic + yleng * 7,100,35);
        panel.add(addswaggerLabel);
        CheckboxGroup addswaggerGroup = new CheckboxGroup();
        Checkbox addswaggerButton1 = new Checkbox("是");
        Checkbox addswaggerButton2 = new Checkbox("否");
        addswaggerButton1.setBounds(165,ybasic + yleng * 7,70,40);
        addswaggerButton2.setBounds(235,ybasic + yleng * 7,70,40);
        panel.add(addswaggerButton1);
        panel.add(addswaggerButton2);
        addswaggerButton1.setCheckboxGroup(addswaggerGroup);
        addswaggerButton2.setCheckboxGroup(addswaggerGroup);
        addswaggerButton1.setState(true);

        TextField addmessage = new TextField(20);
        addmessage.setBounds(50,ybasic + yleng * 9,200,30);

        Button addButton = new Button("新增");
        addButton.setBounds(50, ybasic + yleng * 8, 70, 30);
        panel.add(addButton);

        addButton.addActionListener( l -> {
            String ip = addipText.getText().trim();
            String tableName = addtableNameText.getText().trim();
            String username = addusernameText.getText().trim();
            String password = addpasswordText.getText().trim();
            String basicFileStructure = addbasicFileStructureText.getText().trim();
            String deleteFileStart = deleteFileStartText.getText().trim();
            if(StringUtils.isEmpty(ip) || !ip.contains(":")){
                addipText.setText("请填写完成IP包含端口号!");
                return;
            }
            if(StringUtils.isEmpty(tableName)){
                addtableNameText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(username)){
                addusernameText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(password)){
                addpasswordText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(basicFileStructure)){
                addbasicFileStructureText.setText("请填写!");
                return;
            }
            String edition = "8.0";
            if(addeditionButton1.getState()){
                edition = "5.0";
            }

            String swagger = "false";
            if(addswaggerButton1.getState()){
                swagger = "true";
            }
            try {
                properties.setProerties(2, ip, username, password, tableName, edition, swagger, basicFileStructure, deleteFileStart);
                addipText.setText("");
                addtableNameText.setText("");
                addusernameText.setText("");
                addpasswordText.setText("");
                addbasicFileStructureText.setText("");
                addbasicFileStructureText.setText("");
                addeditionButton1.setState(true);
                addswaggerButton1.setState(true);

                PropertiesFrame newFrame = new PropertiesFrame();
                newFrame.setCompoments(startWindows);
                newFrame.setVisible(true);
                this.setVisible(false);
            }catch (Exception e){
                addmessage.setText(e.toString());
                panel.add(addmessage);
                log.error("增加配置失败:", e);
                if(SwitchUtil.LOG){
                    e.printStackTrace();
                }
            }
        });

    }
}
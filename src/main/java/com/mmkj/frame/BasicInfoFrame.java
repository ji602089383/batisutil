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

/**
 * @author by jicai on 2019/7/29.
 */
@Slf4j
public class BasicInfoFrame extends Frame {

    private int ybasic = 30;
    private int yleng = 45;

    public BasicInfoFrame(){
        this.setSize(900, 650);
        this.setTitle("MySql基本信息");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        WindowsUtil.setWindows(this);
        WindowsUtil.addPic(this);
    }

    public void setMyCompoments(DataInfoFrame frame, StartWindows startWindows){
        Panel panel = new Panel();
        // 添加面板
        this.add(panel);

        Properties properties = Properties.getInstance();
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

//        // 创建 JLabel
        Label ipLabel = new Label("Ip:");
        ipLabel.setBounds(10, ybasic,50,35);
        panel.add(ipLabel);

        TextField ipText = new TextField(20);
        ipText.setBounds(125, ybasic,750,35);
        ipText.setText(properties.getIp());
        panel.add(ipText);

        Label tableNameLabel = new Label("库名:");
        tableNameLabel.setBounds(10,ybasic + yleng * 1,80,35);
        panel.add(tableNameLabel);

        TextField tableNameText = new TextField(20);
        tableNameText.setBounds(125,ybasic + yleng * 1,750,35);
        tableNameText.setText(properties.getTableName());
        panel.add(tableNameText);

        Label usernameLabel = new Label("username:");
        usernameLabel.setBounds(10,ybasic + yleng * 2,80,35);
        panel.add(usernameLabel);

        TextField usernameText = new TextField(20);
        usernameText.setBounds(125,ybasic + yleng * 2,750,35);
        usernameText.setText(properties.getUserName());
        panel.add(usernameText);


        Label passwordLabel = new Label("password:");
        passwordLabel.setBounds(10,ybasic + yleng * 3,80,35);
        panel.add(passwordLabel);

        TextField passwordText = new TextField(20);
        passwordText.setBounds(125,ybasic + yleng * 3,750,35);
        passwordText.setText(properties.getPassWord());
        panel.add(passwordText);

        Label basicFileStructureUrl = new Label("包名(例:com.xbz):");
        basicFileStructureUrl.setBounds(10,ybasic + yleng * 4,100,35);
        panel.add(basicFileStructureUrl);

        TextField basicFileStructureText = new TextField(20);
        basicFileStructureText.setBounds(125,ybasic + yleng * 4,750,35);
        basicFileStructureText.setText(properties.getBasicFileStructure());
        panel.add(basicFileStructureText);

        Label deleteFileStartLabel = new Label("删开头(例:XbzApi):");
        deleteFileStartLabel.setBounds(10,ybasic + yleng * 5,100,35);
        panel.add(deleteFileStartLabel);

        TextField deleteFileStartText = new TextField(20);
        deleteFileStartText.setBounds(125,ybasic + yleng * 5,750,35);
        deleteFileStartText.setText(properties.getDeleteFileStart());
        panel.add(deleteFileStartText);

        Label editionLabel = new Label("MySql版本:");
        editionLabel.setBounds(10,ybasic + yleng * 6,100,35);
        panel.add(editionLabel);
        CheckboxGroup editionGroup = new CheckboxGroup();
        Checkbox editionButton1 = new Checkbox("5.0");
        Checkbox editionButton2 = new Checkbox("8.0");
        editionButton1.setBounds(125,ybasic + yleng * 6,70,40);
        editionButton2.setBounds(195,ybasic + yleng * 6,70,40);
        panel.add(editionButton1);
        panel.add(editionButton2);
        editionButton1.setCheckboxGroup(editionGroup);
        editionButton2.setCheckboxGroup(editionGroup);
        if("8.0".equals(properties.getEdition())){
            editionButton2.setState(true);
        }else {
            editionButton1.setState( true);
        }

        Label swaggerLabel = new Label("开启swagger:");
        swaggerLabel.setBounds(10,ybasic + yleng * 7,100,35);
        panel.add(swaggerLabel);
        CheckboxGroup swaggerGroup = new CheckboxGroup();
        Checkbox swaggerButton1 = new Checkbox("是");
        Checkbox swaggerButton2 = new Checkbox("否");
        swaggerButton1.setBounds(125,ybasic + yleng * 7,70,40);
        swaggerButton2.setBounds(195,ybasic + yleng * 7,70,40);
        panel.add(swaggerButton1);
        panel.add(swaggerButton2);
        swaggerButton1.setCheckboxGroup(swaggerGroup);
        swaggerButton2.setCheckboxGroup(swaggerGroup);
        if("false".equals(properties.getSwagger())){
            swaggerButton2.setState(true);
        }else {
            swaggerButton1.setState(true);
        }

        Button propertiesButton = new Button("配置列表");
        propertiesButton.setBounds(465, ybasic + yleng * 8, 100, 50);
        panel.add(propertiesButton);

        propertiesButton.addActionListener(l -> {
            this.setVisible(false);
            PropertiesFrame propertiesFrame = new PropertiesFrame();
            propertiesFrame.setCompoments(startWindows);
            propertiesFrame.setVisible(true);
        });


        // 创建登录按钮
        Button loginButton = new Button("start");
        loginButton.setBounds(355, ybasic + yleng * 8, 100, 50);
        panel.add(loginButton);


        /******* 第二页布局**********/

        loginButton.addActionListener(p ->{
            String ip = ipText.getText().trim();
            String tableName = tableNameText.getText().trim();
            String username = usernameText.getText().trim();
            String password = passwordText.getText().trim();
            String basicFileStructure = basicFileStructureText.getText().trim();
            String deleteFileStart = deleteFileStartText.getText().trim();
            if(StringUtils.isEmpty(ip) || !ip.contains(":")){
                ipText.setText("请填写完成IP包含端口号!");
                return;
            }
            if(StringUtils.isEmpty(tableName)){
                tableNameText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(username)){
                usernameText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(password)){
                passwordText.setText("请填写!");
                return;
            }
            if(StringUtils.isEmpty(basicFileStructure)){
                basicFileStructureText.setText("请填写!");
                return;
            }

            String edition = "8.0";
            if(editionButton1.getState()){
                edition = "5.0";
            }

            String swagger = "false";
            if(swaggerButton1.getState()){
                swagger = "true";
            }

            TextField message = new TextField();
            message.setBounds(125,ybasic + yleng * 9 + 60,750,35);
            try {
                properties.setProerties(1, ip, username, password, tableName, edition, swagger, basicFileStructure, deleteFileStart);
                startWindows.connectSuccess();
                this.setVisible(false);
                startWindows.getSelectMap().clear();
                startWindows.getCheckboxMapLeft().clear();
                startWindows.getCheckboxMapRight().clear();
                frame.setCompoments(startWindows);
                frame.setVisible(true);
            }catch (Exception e){
                message.setText(e.toString());
                panel.add(message);
                log.error("连接数据库失败:", e);
                if(SwitchUtil.LOG){
                    e.printStackTrace();
                }
                return;
            }
        });
        this.setVisible(true);
    }
}

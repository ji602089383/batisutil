package com.mmkj.frame;

import com.mmkj.StartWindows;
import com.mmkj.sqlfile.SqlEntity;
import com.mmkj.util.FilePath;
import com.mmkj.util.Properties;
import com.mmkj.util.SwitchUtil;
import com.mmkj.util.WindowsUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by jicai on 2019/7/29.
 */
@Slf4j
public class DataInfoFrame extends Frame {
    private int ybasic = 130;
    private int yleng = 45;

    public DataInfoFrame(){
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

    public void setCompoments(StartWindows startWindows){
        Properties properties = Properties.getInstance();
        Panel panel = new Panel();
        this.add(panel);
        panel.setLayout(null);

        TextField selectText = new TextField(20);
        selectText.setBounds(310, ybasic ,200,35);
        panel.add(selectText);

        Button selectLabel = new Button("搜索");
        selectLabel.setBounds(515, ybasic,80,35);
        panel.add(selectLabel);

        Button selectAllNow = new Button("全选当前");
        selectAllNow.setBounds(45, 200, 60, 20);
        panel.add(selectAllNow);

        Button removeAllNow = new Button("清空当前");
        removeAllNow.setBounds(115, 200, 60, 20);
        panel.add(removeAllNow);

        Button selectAll = new Button("所有");
        selectAll.setBounds(525, 200, 50, 20);
        panel.add(selectAll);

        Button removeAll = new Button("全清");
        removeAll.setBounds(585, 200, 50, 20);
        panel.add(removeAll);

        Panel panelLeft = new Panel();
        panelLeft.setBounds(50,225, 320,600);
        panelLeft.setLayout(new GridLayout(startWindows.getSqlMap().size(), 1, 5, 10));

        ScrollPane scrollPaneLeft = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPaneLeft.setBounds(45, 225, 320, 600);
        scrollPaneLeft.add(panelLeft);
        panel.add(scrollPaneLeft);

        TextArea tfRight = new TextArea();
        tfRight.setEditable(false);
        tfRight.setFont(WindowsUtil.FONT);

        Panel panelRight = new Panel();
        panelRight.setBounds(525,225, 320,600);
        panelRight.setLayout(new GridLayout(1, 1, 5, 10));
        panelRight.add(tfRight);
        panel.add(panelRight);

        //刷新数据
        //刷的是左侧所有的数据
        if(startWindows.getCheckboxMapLeft().size() > 0){
            Map<Integer, Checkbox> newMap = new HashMap<>();
            for (Map.Entry<Integer, Checkbox> checkboxEntry : startWindows.getCheckboxMapLeft().entrySet()) {
                Checkbox checkbox = new Checkbox(checkboxEntry.getValue().getLabel());
                checkbox.setFont(WindowsUtil.FONT);
                checkbox.setState(checkboxEntry.getValue().getState());
                checkbox.addItemListener(il -> {
                    Checkbox cb = (Checkbox)il.getSource();
                    Integer key = -1 ;
                    for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()) {
                        if(entry.getValue().getLabel().equals(cb.getLabel())){
                            key = entry.getKey();
                        }
                    }
                    if(cb.getState()){
                        startWindows.getCheckboxMapRight().put(key, cb);
                    }else {
                        startWindows.getCheckboxMapRight().remove(key);
                    }
                    tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
                });
                newMap.put(checkboxEntry.getKey(), checkbox);
            }
            startWindows.setCheckboxMapLeft(newMap);
        }
        //刷新左侧筛选的数据
        if(startWindows.getSelectMap().size() > 0){
            Map<Integer, Checkbox> newMap = new HashMap<>();
            for (Map.Entry<Integer, Checkbox> checkboxEntry : startWindows.getSelectMap().entrySet()) {
                newMap.put(checkboxEntry.getKey(), startWindows.getCheckboxMapLeft().get(checkboxEntry.getKey()));
            }
            startWindows.setSelectMap(newMap);
        }

        TextField messageText = new TextField(20);
        messageText.setBounds(310, 910 ,200,35);

        Button opendButton = new Button("打开文件夹");
        opendButton.setBounds(530,  910,120,40);

        Button returnButton = new Button("返回主页");
        returnButton.setBounds(455,  855,120,40);
        returnButton.addActionListener( l -> {
            this.setVisible(false);
            BasicInfoFrame basicInfoFrame = new BasicInfoFrame();
            DataInfoFrame dataInfoFrame = new DataInfoFrame();
            basicInfoFrame.setMyCompoments(dataInfoFrame, startWindows);
        });
        panel.add(returnButton);

        Button goButton = new Button("开始生成");
        goButton.setBounds(325,  855,120,40);
        goButton.addActionListener(e -> {
            try{
                if(startWindows.getCheckboxMapRight().size() == 0){
                    messageText.setText("请选择表!");
                    panel.add(messageText);
                    return;
                }
                startWindows.goStart();
                messageText.setText("生成成功!");
                panel.add(messageText);
                opendButton.addActionListener(p ->{
                    Desktop desktop = Desktop.getDesktop();
                    try{
                        desktop.open(new File(FilePath.START_PATH));
                    }catch (Exception e1){
                        messageText.setText("打开文件夹失败,文件夹路径:" + properties.getBasicUrl());
                        log.error("打开文件:", e1);
                        if(SwitchUtil.LOG){
                            e1.printStackTrace();
                        }
                    }
                });
                panel.add(opendButton);
            }catch (Exception exc){
                messageText.setText(exc.toString());
                panel.add(messageText);
                log.error("生成失败:", exc);
                if(SwitchUtil.LOG){
                    exc.printStackTrace();
                }
            }
        });
        panel.add(goButton);

        selectLabel.addActionListener(e -> {
            if(selectText.getText() == null || selectText.getText().trim().equals("")){
                startWindows.setSelectMap(new HashMap<>());
                DataInfoFrame dataInfoFrame = new DataInfoFrame();
                dataInfoFrame.setCompoments(startWindows);
                dataInfoFrame.setVisible(true);
                this.setVisible(false);
                return;
            }
            if(startWindows.selectSqlEntity(selectText.getText())){
                DataInfoFrame dataInfoFrame = new DataInfoFrame();
                dataInfoFrame.setCompoments(startWindows);
                dataInfoFrame.setVisible(true);
                this.setVisible(false);
            }
        });

        selectAll.addActionListener(l -> {
            startWindows.setCheckboxMapRight(new HashMap<>());
            for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()){
                entry.getValue().setState(true);
            }
            startWindows.getCheckboxMapRight().putAll(startWindows.getCheckboxMapLeft());
            tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
        });

        selectAllNow.addActionListener(l -> {
            if(startWindows.getSelectMap().size() == 0){
                startWindows.setCheckboxMapRight(new HashMap<>());
                for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()){
                    entry.getValue().setState(true);
                }
                startWindows.getCheckboxMapRight().putAll(startWindows.getCheckboxMapLeft());
            }else {
                for (Map.Entry<Integer, Checkbox> entry : startWindows.getSelectMap().entrySet()){
                    if(!entry.getValue().getState()){
                        entry.getValue().setState(true);
                        startWindows.getCheckboxMapRight().put(entry.getKey(), entry.getValue());
                    }
                }
            }
            tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
        });

        removeAll.addActionListener(l -> {
            startWindows.setCheckboxMapRight(new HashMap<>());
            for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()){
                entry.getValue().setState(false);
            }
            tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
        });

        removeAllNow.addActionListener(l -> {
            if(startWindows.getSelectMap().size() == 0){
                startWindows.setCheckboxMapRight(new HashMap<>());
                for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()){
                    entry.getValue().setState(false);
                }
                tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
            }else {
                for (Map.Entry<Integer, Checkbox> entry : startWindows.getSelectMap().entrySet()){
                    if(entry.getValue().getState()){
                        entry.getValue().setState(false);
                        startWindows.getCheckboxMapRight().remove(entry.getKey());
                    }
                }
            }
            tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
        });

        Map<Integer, Checkbox> map;
        if(startWindows.getSelectMap().size() > 0){
            map = startWindows.getSelectMap();
        }else {
            if(startWindows.getCheckboxMapLeft().size() == 0){
                for (Map.Entry<Integer, SqlEntity> sqlEntityEntry : startWindows.getSqlMap().entrySet()) {
                    Checkbox checkbox = new Checkbox(sqlEntityEntry.getValue().getSqlTableName());
                    checkbox.setFont(WindowsUtil.FONT);
                    checkbox.addItemListener(il -> {
                        Checkbox cb = (Checkbox)il.getSource();
                        Integer key = -1 ;
                        for (Map.Entry<Integer, Checkbox> entry : startWindows.getCheckboxMapLeft().entrySet()) {
                            if(entry.getValue().getLabel().equals(cb.getLabel())){
                                key = entry.getKey();
                            }
                        }
                        if(cb.getState()){
                            startWindows.getCheckboxMapRight().put(key, cb);
                        }else {
                            startWindows.getCheckboxMapRight().remove(key);
                        }
                        tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
                    });
                    startWindows.getCheckboxMapLeft().put(sqlEntityEntry.getKey(), checkbox);
                }
            }
            map = startWindows.getCheckboxMapLeft();
        }
        for (Map.Entry<Integer, Checkbox> mapEntry : map.entrySet()){
            panelLeft.add(mapEntry.getValue());
        }
        tfRight.setText(setRightData(startWindows.getCheckboxMapRight()));
    }

    private String setRightData(Map<Integer, Checkbox> checkboxMap){
        //右边数据
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Checkbox> checkboxEntry : checkboxMap.entrySet()) {
            sb.append(checkboxEntry.getValue().getLabel() + "\n");
        }
        return sb.toString();
    }
}

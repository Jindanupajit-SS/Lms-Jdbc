package com.smoothstack.jan2020.LmsJDBC.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SelectOption<T, R> implements Input<R> {

    private String banner = "";
    private String prompt = " > ";
    private String abortMessage = "Go back to previous menu";
    private List<T> items = new ArrayList<>();
    private Function<T, ? extends String> labelMapper = Object::toString;
    private BiFunction<T, ? super Integer, R> valueMapper = (t,i)->{return (R) i;};


    public void display() {
        System.out.println(banner);
        for (int i = 0; i < items.size(); i++) {
            String label = labelMapper.apply(items.get(i));
            System.out.printf("\t[%2d] %-50s\n", 1 + i, label);
        }
        if (abortMessage != null)
               System.out.printf("\n\t[%2d] %-50s\n", items.size()+1, abortMessage);
    }

    public R get() {
        display();
        return getValue(KeyboardScanner.getInteger(prompt,1, items.size()+(abortMessage==null?0:1)));
    }

    public R getValue(int i) {
        if ((abortMessage != null)&&(i > items.size()))
            return valueMapper.apply(null, i);
        else
            return valueMapper.apply(items.get(i-1), i);
    }

    public String getAbortMessage() {
        return abortMessage;
    }

    public void setAbortMessage(String abortMessage) {
        this.abortMessage = abortMessage;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Function<T, ? extends String> getLabelMapper() {
        return labelMapper;
    }

    public void setLabelMapper(Function<T, ? extends String> labelMapper) {
        this.labelMapper = labelMapper;
    }

    public BiFunction<T, ? super Integer, R> getValueMapper() {
        return valueMapper;
    }

    public void setValueMapper(BiFunction<T, ? super Integer, R> valueMapper) {
        this.valueMapper = valueMapper;
    }

}

package colorpanel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ColorPanel extends JPanel {

    private int red = 0;
    private int grn = 0;
    private int blu = 0;
    private int hue = 0;
    private float sat = 0;
    private float val = 0;
    private String hex = "#000000";

    private final ColorPanelSelector colorPanelSelector;
    private final ColorPanelInfo colorPanelInfo;
    private final ColorPanelHandler colorPanelHandler;

    private final ColorSplitPane colorSplitPane;

    public ColorPanel(ColorSplitPane colorSplitPane) {
        this.colorSplitPane = colorSplitPane;
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(0, 0, 1, 0, new Color(43, 43 ,43)));

        colorPanelSelector = new ColorPanelSelector(this);
        add(colorPanelSelector, BorderLayout.CENTER);

        colorPanelInfo = new ColorPanelInfo(this);
        add(colorPanelInfo, BorderLayout.LINE_END);

        colorPanelHandler = new ColorPanelHandler(this);
        add(colorPanelHandler, BorderLayout.PAGE_END);

        setVal(1);
    }

    protected void setSelectedColor(Color color) {
        red = color.getRed();
        grn = color.getGreen();
        blu = color.getBlue();
        updateFromRGB();
    }

    protected Color getSelectedColor() {
        return colorPanelHandler.getSelectedColor();
    }

    protected void onSelectedColorChange(Color color, boolean isPrimary) {
        colorSplitPane.onSelectedColorChange(color, isPrimary);
    }

    protected int getRed() {
        return red;
    }

    protected void setRed(int red) {
        this.red = red;
        updateFromRGB();
    }

    protected int getGrn() {
        return grn;
    }

    protected void setGrn(int grn) {
        this.grn = grn;
        updateFromRGB();
    }

    protected int getBlu() {
        return blu;
    }

    protected void setBlu(int blu) {
        this.blu = blu;
        updateFromRGB();
    }

    protected int getHue() {
        return hue;
    }

    protected void setHue(int hue) {
        this.hue = hue;
        updateFromHSV();
        colorPanelSelector.updateHue();
    }

    protected float getSat() {
        return sat;
    }

    protected void setSat(float sat) {
        this.sat = (int) (sat*1000) / 10f;
        updateFromHSV();
        colorPanelSelector.updateSat();
    }

    protected float getVal() {
        return val;
    }

    protected void setVal(float val) {
        this.val = (int) (val*1000) / 10f;
        updateFromHSV();
        colorPanelSelector.updateVal();
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
        colorPanelHandler.setSelectedColor(new Color(Integer.decode(hex)));
        updateFromHex();
    }

    private void updateFromRGB() {
        updateHSVFromRGB();
        updateHexFromRGB();
        colorPanelInfo.updateNotRGB();
        colorPanelSelector.updateHSV();
    }

    private void updateFromHSV() {
        updateRGBFromHSV();
        updateHexFromRGB();
        colorPanelInfo.updateNotHSV();
    }

    private void updateFromHex() {
        updateRGBFromHex();
        updateHSVFromRGB();
        colorPanelInfo.updateNotHex();
        colorPanelSelector.updateHSV();
    }

    private void updateRGBFromHex() {
        int hexInt = Integer.decode(hex);

        blu  =  hexInt & 0xff;
        grn = (hexInt & 0xff00) >> 8;
        red   = (hexInt & 0xff0000) >> 16;
    }

    private void updateRGBFromHSV() {
        Color color = Color.getHSBColor(hue/360f, sat/100f, val/100f);
        red = color.getRed();
        grn = color.getGreen();
        blu = color.getBlue();
    }

    private void updateHSVFromRGB() {
        float[] hsv = new float[3];
        Color.RGBtoHSB(red, grn, blu, hsv);

        hue = Math.round(hsv[0]*360);
        sat = ((int) (hsv[1]*1000) / 10f);
        val = ((int) (hsv[2]*1000) / 10f);
    }

    private void updateHexFromRGB() {
        String redString = String.format("%02X", red);
        String grnString = String.format("%02X", grn);
        String bluString = String.format("%02X", blu);

        hex = "#" + redString + grnString + bluString;
        colorPanelHandler.setSelectedColor(new Color(Integer.decode(hex)));
    }

    protected void notifyH() {
        colorPanelInfo.setH();
    }

    protected void notifySV() {
        colorPanelInfo.setSV();
    }
}
